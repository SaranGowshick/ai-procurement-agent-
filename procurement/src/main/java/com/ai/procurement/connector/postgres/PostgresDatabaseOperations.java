
package com.ai.procurement.connector.postgres;

import com.ai.procurement.connector.managers.ManagedDatabaseOperations;
import com.ai.procurement.model.database.response.CollectionRelationshipResponse;
import com.ai.procurement.model.database.response.CollectionSchemaResponse;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Component
public class PostgresDatabaseOperations
        implements ManagedDatabaseOperations {

    private final PostgresConnectionManager connectionManager;

    public PostgresDatabaseOperations(
            PostgresConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    // ---------------------------------------------------------
    // Connection support
    // ---------------------------------------------------------

    @Override
    public boolean supportsConnection(String connectionId) {
        return connectionManager.supportsManagedConnection(connectionId);
    }

    // ---------------------------------------------------------
    // Discover table schema
    // ---------------------------------------------------------

    @Override
    public CollectionSchemaResponse discoverCollectionSchema(
            String connectionId,
            String collectionName,
            int sampleSize) {

        validateTableName(collectionName);
        validateSampleSize(sampleSize);

        Map<String, Set<String>> columnTypes = new TreeMap<>();
        int documentsSampled = 0;

        try (Connection connection =
                     connectionManager.getConnection(connectionId)) {

            String schema = connection.getSchema();

            if (schema == null || schema.isBlank()) {
                throw new IllegalStateException(
                        "Unable to determine PostgreSQL schema");
            }

            String columnsSql = """
                    SELECT column_name, data_type, udt_name
                    FROM information_schema.columns
                    WHERE table_schema = ?
                      AND table_name = ?
                    ORDER BY ordinal_position
                    """;

            try (PreparedStatement statement =
                         connection.prepareStatement(columnsSql)) {

                statement.setString(1, schema);
                statement.setString(2, collectionName);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String columnName =
                                resultSet.getString("column_name");

                        String dataType =
                                resultSet.getString("data_type");

                        String udtName =
                                resultSet.getString("udt_name");

                        Set<String> types = new LinkedHashSet<>();
                        types.add(dataType);
                        types.add(udtName);

                        columnTypes.put(columnName, types);
                    }
                }
            }

            if (columnTypes.isEmpty()) {
                throw new IllegalArgumentException(
                        "Table not found in the current schema: "
                                + collectionName);
            }

            String query = "SELECT * FROM "
                    + quoteIdentifier(schema) + "."
                    + quoteIdentifier(collectionName)
                    + " LIMIT ?";

            try (PreparedStatement statement =
                         connection.prepareStatement(query)) {

                statement.setInt(1, sampleSize);

                try (ResultSet resultSet = statement.executeQuery()) {
                    ResultSetMetaData metadata =
                            resultSet.getMetaData();

                    while (resultSet.next()) {
                        documentsSampled++;

                        for (int i = 1;
                             i <= metadata.getColumnCount(); i++) {

                            String columnName =
                                    metadata.getColumnLabel(i);

                            Object value = resultSet.getObject(i);

                            if (value != null) {
                                columnTypes.computeIfAbsent(
                                                columnName,
                                                key -> new LinkedHashSet<>())
                                        .add(
                                                value.getClass()
                                                        .getSimpleName());
                            }
                        }
                    }
                }
            }

            List<CollectionSchemaResponse.FieldSchema> fields =
                    new ArrayList<>();

            for (Map.Entry<String, Set<String>> entry
                    : columnTypes.entrySet()) {

                boolean referenceLike = isForeignKey(
                        connection,
                        schema,
                        collectionName,
                        entry.getKey());

                fields.add(
                        new CollectionSchemaResponse.FieldSchema(
                                entry.getKey(),
                                new ArrayList<>(entry.getValue()),
                                referenceLike));
            }

            return new CollectionSchemaResponse(
                    collectionName,
                    documentsSampled,
                    fields);

        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Unable to discover schema for PostgreSQL table: "
                            + collectionName,
                    exception);
        }
    }

    // ---------------------------------------------------------
    // Discover foreign-key relationships
    // ---------------------------------------------------------

    @Override
    public List<CollectionRelationshipResponse> discoverRelationships(
            String connectionId,
            String collectionName,
            int sampleSize) {

        validateTableName(collectionName);
        validateSampleSize(sampleSize);

        List<CollectionRelationshipResponse> relationships =
                new ArrayList<>();

        try (Connection connection =
                     connectionManager.getConnection(connectionId)) {

            String schema = connection.getSchema();

            if (schema == null || schema.isBlank()) {
                throw new IllegalStateException(
                        "Unable to determine PostgreSQL schema");
            }

            String sql = """
                    SELECT
                        kcu.column_name,
                        ccu.table_name AS referenced_table
                    FROM information_schema.table_constraints tc
                    JOIN information_schema.key_column_usage kcu
                      ON tc.constraint_name = kcu.constraint_name
                     AND tc.constraint_schema = kcu.constraint_schema
                     AND tc.constraint_name = kcu.constraint_name
                    JOIN information_schema.constraint_column_usage ccu
                      ON ccu.constraint_name = tc.constraint_name
                     AND ccu.constraint_schema = tc.constraint_schema
                    WHERE tc.constraint_type = 'FOREIGN KEY'
                      AND tc.table_schema = ?
                      AND tc.table_name = ?
                    ORDER BY kcu.ordinal_position
                    """;

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, schema);
                statement.setString(2, collectionName);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        relationships.add(
                                new CollectionRelationshipResponse(
                                        collectionName,
                                        resultSet.getString("column_name"),
                                        resultSet.getString("referenced_table"),
                                        "UNKNOWN",
                                        sampleSize));
                    }
                }
            }

            return relationships;

        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Unable to discover relationships for PostgreSQL table: "
                            + collectionName,
                    exception);
        }
    }

    // ---------------------------------------------------------
    // Retrieve selected field values
    // ---------------------------------------------------------

    @Override
    public List<Map<String, Object>> getFieldValues(
            String connectionId,
            String collectionName,
            List<String> fields,
            Integer limit) {

        validateTableName(collectionName);

        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one field must be specified");
        }

        for (String field : fields) {
            validateIdentifier(field, "column");
        }

        int rowLimit = limit == null ? 100 : limit;

        if (rowLimit < 1 || rowLimit > 10000) {
            throw new IllegalArgumentException(
                    "Limit must be between 1 and 10000");
        }

        try (Connection connection =
                     connectionManager.getConnection(connectionId)) {

            String schema = connection.getSchema();

            if (schema == null || schema.isBlank()) {
                throw new IllegalStateException(
                        "Unable to determine PostgreSQL schema");
            }

            String selectedColumns = fields.stream()
                    .map(PostgresDatabaseOperations::quoteIdentifier)
                    .reduce((first, second) -> first + ", " + second)
                    .orElseThrow();

            String sql = "SELECT " + selectedColumns
                    + " FROM " + quoteIdentifier(schema)
                    + "." + quoteIdentifier(collectionName)
                    + " LIMIT ?";

            List<Map<String, Object>> rows = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setInt(1, rowLimit);

                try (ResultSet resultSet = statement.executeQuery()) {
                    ResultSetMetaData metadata =
                            resultSet.getMetaData();

                    while (resultSet.next()) {
                        Map<String, Object> row =
                                new LinkedHashMap<>();

                        for (int i = 1;
                             i <= metadata.getColumnCount(); i++) {

                            row.put(
                                    metadata.getColumnLabel(i),
                                    resultSet.getObject(i));
                        }

                        rows.add(row);
                    }
                }
            }

            return rows;

        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Unable to retrieve field values from PostgreSQL table: "
                            + collectionName,
                    exception);
        }
    }

    // ---------------------------------------------------------
    // Foreign-key detection
    // ---------------------------------------------------------

    private boolean isForeignKey(
            Connection connection,
            String schema,
            String table,
            String column) throws SQLException {

        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM information_schema.key_column_usage kcu
                    JOIN information_schema.table_constraints tc
                      ON tc.constraint_name = kcu.constraint_name
                     AND tc.constraint_schema = kcu.constraint_schema
                    WHERE tc.constraint_type = 'FOREIGN KEY'
                      AND kcu.table_schema = ?
                      AND kcu.table_name = ?
                      AND kcu.column_name = ?
                )
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, schema);
            statement.setString(2, table);
            statement.setString(3, column);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getBoolean(1);
            }
        }
    }

    // ---------------------------------------------------------
    // Validation helpers
    // ---------------------------------------------------------

    private void validateTableName(String tableName) {
        validateIdentifier(tableName, "table");

        if (tableName.contains(".")) {
            throw new IllegalArgumentException(
                    "Provide a table name without a schema prefix");
        }
    }

    private void validateIdentifier(String identifier, String type) {
        if (identifier == null
                || identifier.isBlank()
                || identifier.indexOf('\0') >= 0) {

            throw new IllegalArgumentException(
                    "A valid " + type + " name is required");
        }
    }

    private void validateSampleSize(int sampleSize) {
        if (sampleSize < 1 || sampleSize > 100) {
            throw new IllegalArgumentException(
                    "Sample size must be between 1 and 100");
        }
    }

    private static String quoteIdentifier(String identifier) {
        if (identifier == null
                || identifier.isBlank()
                || identifier.indexOf('\0') >= 0) {

            throw new IllegalArgumentException(
                    "Invalid SQL identifier");
        }

        return "\"" + identifier.replace("\"", "\"\"") + "\"";
    }
}
