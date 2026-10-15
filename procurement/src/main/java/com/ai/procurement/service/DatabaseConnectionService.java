
package com.ai.procurement.service;

import com.ai.procurement.connector.DatabaseConnector;
import com.ai.procurement.connector.relations.DatabaseSchemaConnector;
import com.ai.procurement.connector.managers.ManagedDatabaseOperations;
import com.ai.procurement.connector.managers.ManagedDatabaseConnectionProvider;
import com.ai.procurement.model.database.request.DatabaseConnectionRequest;
import com.ai.procurement.model.database.request.DatabaseOperationRequest;
import com.ai.procurement.model.database.response.DatabaseSchemaResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class DatabaseConnectionService {

    private final List<DatabaseConnector> connectors;
    private final List<DatabaseSchemaConnector> schemaConnectors;
    private final List<ManagedDatabaseConnectionProvider> connectionProviders;
    private final List<ManagedDatabaseOperations> databaseOperations;

    public DatabaseConnectionService(
            List<DatabaseConnector> connectors,
            List<DatabaseSchemaConnector> schemaConnectors,
            List<ManagedDatabaseConnectionProvider> connectionProviders,
            List<ManagedDatabaseOperations> databaseOperations) {
        this.connectors = connectors;
        this.schemaConnectors = schemaConnectors;
        this.connectionProviders = connectionProviders;
        this.databaseOperations = databaseOperations;
    }

    public boolean testConnection(DatabaseConnectionRequest request) {
        if (request == null || request.databaseType() == null) {
            return false;
        }

        return connectors.stream()
                .filter(connector -> connector.supports(request))
                .findFirst()
                .map(connector -> connector.testConnection(request))
                .orElse(false);
    }

    public String createManagedConnection(
            DatabaseConnectionRequest request) {
        if (request == null || request.databaseType() == null) {
            throw new IllegalArgumentException(
                    "Database connection details are required");
        }

        ManagedDatabaseConnectionProvider provider =
                connectionProviders.stream()
                        .filter(candidate -> candidate.supports(request))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Managed connections are not supported"));

        return provider.createConnection(request);
    }

    public DatabaseSchemaResponse discoverSchema(
            DatabaseConnectionRequest request) {
        if (request == null || request.databaseType() == null) {
            throw new IllegalArgumentException(
                    "Database connection details are required");
        }

        DatabaseSchemaConnector connector = schemaConnectors.stream()
                .filter(candidate -> candidate.supportsSchema(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Schema discovery is not supported"));

        return connector.discoverSchema(request);
    }

    public Object executeOperation(DatabaseOperationRequest request) {
        if (request == null || request.operation() == null
                || request.operation().isBlank()) {
            throw new IllegalArgumentException("Operation is required");
        }

        requireConnectionId(request.connectionId());

        String operation = request.operation()
                .trim()
                .toUpperCase(Locale.ROOT);

        return switch (operation) {
            case "DISCOVER_SCHEMA" ->
                    discoverManagedSchema(request.connectionId());

            case "DISCOVER_COLLECTION_SCHEMA" ->
                    operationsFor(request.connectionId())
                            .discoverCollectionSchema(
                                    request.connectionId(),
                                    requireCollectionName(
                                            request.collectionName()),
                                    request.sampleSize() == null
                                            ? 20 : request.sampleSize());

            case "DISCOVER_RELATIONSHIPS" ->
                    operationsFor(request.connectionId())
                            .discoverRelationships(
                                    request.connectionId(),
                                    requireCollectionName(
                                            request.collectionName()),
                                    request.sampleSize() == null
                                            ? 20 : request.sampleSize());

            case "GET_DATA" -> getData(request);

            default -> throw new IllegalArgumentException(
                    "Unsupported operation: " + operation);
        };
    }

    private DatabaseSchemaResponse discoverManagedSchema(
            String connectionId) {
        DatabaseSchemaConnector connector = schemaConnectors.stream()
                .filter(candidate ->
                        candidate.supportsManagedSchema(connectionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No schema connector found for connection ID"));

        return connector.discoverManagedSchema(connectionId);
    }

    private Object getData(DatabaseOperationRequest request) {
        if (request.fields() == null || request.fields().isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one field is required");
        }

        String collectionName =
                requireCollectionName(request.collectionName());

        List<Map<String, Object>> data =
                operationsFor(request.connectionId()).getFieldValues(
                        request.connectionId(),
                        collectionName,
                        request.fields(),
                        request.limit());

        return Map.of(
                "collection", collectionName,
                "fields", request.fields(),
                "count", data.size(),
                "data", data);
    }

    private ManagedDatabaseOperations operationsFor(String connectionId) {
        return databaseOperations.stream()
                .filter(candidate ->
                        candidate.supportsConnection(connectionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No database operations provider found for connection ID"));
    }

    private void requireConnectionId(String connectionId) {
        if (connectionId == null || connectionId.isBlank()) {
            throw new IllegalArgumentException(
                    "Connection ID is required");
        }
    }

    private String requireCollectionName(String collectionName) {
        if (collectionName == null || collectionName.isBlank()) {
            throw new IllegalArgumentException(
                    "Collection name is required");
        }
        return collectionName;
    }
}
