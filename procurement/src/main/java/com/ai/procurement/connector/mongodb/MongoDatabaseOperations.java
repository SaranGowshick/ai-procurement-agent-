
package com.ai.procurement.connector.mongodb;

import com.ai.procurement.connector.managers.ManagedDatabaseOperations;
import com.ai.procurement.connector.relations.DatabaseRelationshipConnector;
import com.ai.procurement.connector.relations.DatabaseSchemaConnector;
import com.ai.procurement.model.database.databasetypes.DatabaseType;
import com.ai.procurement.model.database.request.DatabaseConnectionRequest;
import com.ai.procurement.model.database.response.CollectionRelationshipResponse;
import com.ai.procurement.model.database.response.CollectionSchemaResponse;
import com.ai.procurement.model.database.response.DatabaseSchemaResponse;
import com.mongodb.DBRef;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@Component
public class MongoDatabaseOperations implements
        ManagedDatabaseOperations,
        DatabaseSchemaConnector,
        DatabaseRelationshipConnector {

    private static final int DEFAULT_LIMIT = 100;
    private static final int MAX_LIMIT = 1000;
    private static final int MAX_SAMPLE_SIZE = 100;

    private final MongoConnectionManager connectionManager;

    public MongoDatabaseOperations(
            MongoConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    // --------------------------------------------------
    // Managed connection support
    // --------------------------------------------------

    @Override
    public boolean supportsConnection(String connectionId) {
        return connectionManager.supportsManagedConnection(connectionId);
    }

    // --------------------------------------------------
    // Database schema discovery
    // --------------------------------------------------

    @Override
    public boolean supportsSchema(DatabaseConnectionRequest request) {
        return connectionManager.supports(request);
    }

    @Override
    public boolean supportsManagedSchema(String connectionId) {
        return connectionManager.supportsManagedConnection(connectionId);
    }

    @Override
    public DatabaseSchemaResponse discoverSchema(
            DatabaseConnectionRequest request) {

        validateRequest(request);

        try (MongoClient client = connectionManager.createClient(request)) {
            List<String> collections = new ArrayList<>();

            client.getDatabase(request.database())
                    .listCollectionNames()
                    .forEach(collections::add);

            return new DatabaseSchemaResponse(
                    DatabaseType.MONGODB.name(),
                    request.database(),
                    collections);

        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Unable to discover MongoDB schema",
                    exception);
        }
    }

    @Override
    public DatabaseSchemaResponse discoverManagedSchema(
            String connectionId) {

        MongoConnectionManager.ManagedConnection connection =
                connectionManager.getConnection(connectionId);

        List<String> collections = new ArrayList<>();

        connection.client()
                .getDatabase(connection.database())
                .listCollectionNames()
                .forEach(collections::add);

        return new DatabaseSchemaResponse(
                DatabaseType.MONGODB.name(),
                connection.database(),
                collections);
    }

    // --------------------------------------------------
    // Collection schema discovery
    // --------------------------------------------------

    @Override
    public CollectionSchemaResponse discoverCollectionSchema(
            String connectionId,
            String collectionName,
            int sampleSize) {

        validateCollectionName(collectionName);
        validateSampleSize(sampleSize);

        MongoConnectionManager.ManagedConnection connection =
                connectionManager.getConnection(connectionId);

        MongoCollection<Document> collection = getCollection(
                connection.client().getDatabase(connection.database()),
                collectionName);

        Map<String, Set<String>> fieldTypes = new TreeMap<>();
        int sampled = 0;

        for (Document document : collection.find().limit(sampleSize)) {
            sampled++;
            inspectSchema(document, "", fieldTypes);
        }

        List<CollectionSchemaResponse.FieldSchema> fields =
                new ArrayList<>();

        fieldTypes.forEach((path, types) ->
                fields.add(new CollectionSchemaResponse.FieldSchema(
                        path,
                        new ArrayList<>(types),
                        types.contains("DBREF"))));

        return new CollectionSchemaResponse(
                collectionName,
                sampled,
                fields);
    }

    private void inspectSchema(
            Object value,
            String path,
            Map<String, Set<String>> fieldTypes) {

        if (value == null) {
            if (!path.isEmpty()) {
                addType(path, "NULL", fieldTypes);
            }
            return;
        }

        if (value instanceof DBRef) {
            addType(path, "DBREF", fieldTypes);
            return;
        }

        if (value instanceof Document document) {
            if (document.get("$ref") instanceof String
                    && document.containsKey("$id")) {
                addType(path, "DBREF", fieldTypes);
                return;
            }

            if (!path.isEmpty()) {
                addType(path, "DOCUMENT", fieldTypes);
            }

            document.forEach((key, nestedValue) -> {
                if (path.isEmpty() && "_id".equals(key)) {
                    return;
                }

                String childPath = path.isEmpty()
                        ? key
                        : path + "." + key;

                inspectSchema(nestedValue, childPath, fieldTypes);
            });

            return;
        }

        if (value instanceof List<?> list) {
            addType(path, "ARRAY", fieldTypes);

            for (Object item : list) {
                inspectSchema(item, path + "[]", fieldTypes);
            }

            return;
        }

        addType(path, bsonType(value), fieldTypes);
    }

    private void addType(
            String path,
            String type,
            Map<String, Set<String>> fieldTypes) {

        if (path == null || path.isEmpty()) {
            return;
        }

        fieldTypes.computeIfAbsent(path, ignored -> new TreeSet<>())
                .add(type);
    }

    private String bsonType(Object value) {
        if (value instanceof String) return "STRING";
        if (value instanceof Integer
                || value instanceof Long
                || value instanceof Short) return "INTEGER";
        if (value instanceof Number) return "NUMBER";
        if (value instanceof Boolean) return "BOOLEAN";
        if (value instanceof java.util.Date) return "DATE";
        if (value instanceof org.bson.types.ObjectId) return "OBJECT_ID";
        if (value instanceof byte[]) return "BINARY";
        return value.getClass().getSimpleName().toUpperCase();
    }

    // --------------------------------------------------
    // Relationship discovery using managed connection
    // --------------------------------------------------

    @Override
    public boolean supportsRelationships(
            DatabaseConnectionRequest request) {
        return connectionManager.supports(request);
    }

    @Override
    public List<CollectionRelationshipResponse> discoverRelationships(
            String connectionId,
            String collectionName,
            int sampleSize) {

        validateCollectionName(collectionName);
        validateSampleSize(sampleSize);

        MongoConnectionManager.ManagedConnection connection =
                connectionManager.getConnection(connectionId);

        return discoverRelationships(
                connection.client().getDatabase(connection.database()),
                collectionName,
                sampleSize);
    }

    // --------------------------------------------------
    // Relationship discovery using direct request
    // --------------------------------------------------

    @Override
    public List<CollectionRelationshipResponse> discoverRelationships(
            DatabaseConnectionRequest request,
            String collectionName,
            int sampleSize) {

        validateRequest(request);
        validateCollectionName(collectionName);
        validateSampleSize(sampleSize);

        try (MongoClient client = connectionManager.createClient(request)) {
            return discoverRelationships(
                    client.getDatabase(request.database()),
                    collectionName,
                    sampleSize);
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Unable to discover MongoDB relationships",
                    exception);
        }
    }

    private List<CollectionRelationshipResponse> discoverRelationships(
            MongoDatabase database,
            String collectionName,
            int sampleSize) {

        MongoCollection<Document> collection =
                getCollection(database, collectionName);

        Map<String, String> references = new TreeMap<>();
        int sampled = 0;

        for (Document document : collection.find().limit(sampleSize)) {
            sampled++;
            inspectReferences(document, "", references);
        }

        List<CollectionRelationshipResponse> results =
                new ArrayList<>();

        final int documentsSampled = sampled;

        references.forEach((field, target) ->
                results.add(new CollectionRelationshipResponse(
                        collectionName,
                        field,
                        target,
                        "UNKNOWN",
                        documentsSampled)));

        return results;
    }

    private void inspectReferences(
            Object value,
            String path,
            Map<String, String> references) {

        if (value instanceof DBRef dbRef) {
            if (!path.isEmpty()) {
                references.put(path, dbRef.getCollectionName());
            }
            return;
        }

        if (value instanceof Document document) {
            if (document.get("$ref") instanceof String target
                    && document.containsKey("$id")) {
                if (!path.isEmpty()) {
                    references.put(path, target);
                }
                return;
            }

            document.forEach((key, nestedValue) -> {
                String childPath = path.isEmpty()
                        ? key
                        : path + "." + key;

                inspectReferences(nestedValue, childPath, references);
            });

            return;
        }

        if (value instanceof List<?> list) {
            for (Object item : list) {
                inspectReferences(item, path + "[]", references);
            }
        }
    }

    // --------------------------------------------------
    // Retrieve selected field values
    // --------------------------------------------------

    @Override
    public List<Map<String, Object>> getFieldValues(
            String connectionId,
            String collectionName,
            List<String> fields,
            Integer limit) {

        validateCollectionName(collectionName);

        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one field is required");
        }

        for (String field : fields) {
            validateFieldName(field);
        }

        int resultLimit = limit == null ? DEFAULT_LIMIT : limit;

        if (resultLimit < 1 || resultLimit > MAX_LIMIT) {
            throw new IllegalArgumentException(
                    "Limit must be between 1 and " + MAX_LIMIT);
        }

        MongoConnectionManager.ManagedConnection connection =
                connectionManager.getConnection(connectionId);

        Document projection = new Document("_id", 0);
        fields.forEach(field -> projection.append(field, 1));

        List<Map<String, Object>> results = new ArrayList<>();

        getCollection(
                connection.client().getDatabase(connection.database()),
                collectionName)
                .find()
                .projection(projection)
                .limit(resultLimit)
                .forEach(document -> {
                    Map<String, Object> row = new LinkedHashMap<>();

                    for (String field : fields) {
                        Object value = getNestedValue(document, field);

                        if (value != null) {
                            row.put(field, value);
                        }
                    }

                    results.add(row);
                });

        return results;
    }

    private Object getNestedValue(Document document, String path) {
        Object current = document;

        for (String part : path.split("\\.")) {
            if (!(current instanceof Document nested)) {
                return null;
            }

            current = nested.get(part);
        }

        return current;
    }

    // --------------------------------------------------
    // Shared helpers
    // --------------------------------------------------

    private MongoCollection<Document> getCollection(
            MongoDatabase database,
            String collectionName) {
        return database.getCollection(collectionName, Document.class);
    }

    private void validateRequest(DatabaseConnectionRequest request) {
        if (!connectionManager.supports(request)
                || isBlank(request.host())
                || isBlank(request.database())
                || isBlank(request.username())
                || request.password() == null
                || request.port() == null
                || request.port() < 1
                || request.port() > 65535) {
            throw new IllegalArgumentException(
                    "Valid MongoDB connection details are required");
        }
    }

    private void validateCollectionName(String collectionName) {
        if (collectionName == null
                || !collectionName.matches("[A-Za-z_][A-Za-z0-9_]*")) {
            throw new IllegalArgumentException(
                    "Invalid collection name");
        }
    }

    private void validateFieldName(String fieldName) {
        if (fieldName == null
                || fieldName.isBlank()
                || fieldName.startsWith(".")
                || fieldName.endsWith(".")
                || fieldName.contains("..")
                || fieldName.contains("$")
                || fieldName.indexOf('\0') >= 0) {
            throw new IllegalArgumentException(
                    "Invalid field name: " + fieldName);
        }
    }

    private void validateSampleSize(int sampleSize) {
        if (sampleSize < 1 || sampleSize > MAX_SAMPLE_SIZE) {
            throw new IllegalArgumentException(
                    "Sample size must be between 1 and "
                            + MAX_SAMPLE_SIZE);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
