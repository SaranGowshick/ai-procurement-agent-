
package com.ai.procurement.connector.mongodb;

import com.ai.procurement.connector.DatabaseConnector;
import com.ai.procurement.connector.managers.ManagedDatabaseConnectionProvider;
import com.ai.procurement.model.database.databasetypes.DatabaseType;
import com.ai.procurement.model.database.request.DatabaseConnectionRequest;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class MongoConnectionManager
        implements DatabaseConnector, ManagedDatabaseConnectionProvider {

    private static final long CONNECTION_TTL_MINUTES = 30;

    private final ConcurrentHashMap<String, ManagedConnection> connections =
            new ConcurrentHashMap<>();

    @Override
    public boolean supports(DatabaseConnectionRequest request) {
        return request != null
                && request.databaseType() == DatabaseType.MONGODB;
    }

    @Override
    public boolean testConnection(DatabaseConnectionRequest request) {
        if (!isValidRequest(request)) {
            return false;
        }

        try (MongoClient client = createClient(request)) {
            client.getDatabase("admin")
                    .runCommand(new Document("ping", 1));
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public String createConnection(DatabaseConnectionRequest request) {
        if (!isValidRequest(request)) {
            throw new IllegalArgumentException(
                    "Valid MongoDB connection details are required");
        }

        MongoClient client = createClient(request);

        try {
            client.getDatabase("admin")
                    .runCommand(new Document("ping", 1));

            client.getDatabase(request.database())
                    .listCollectionNames()
                    .first();

            String connectionId = UUID.randomUUID().toString();

            connections.put(
                    connectionId,
                    new ManagedConnection(
                            client,
                            request.database(),
                            Instant.now().plusSeconds(
                                    CONNECTION_TTL_MINUTES * 60)));

            return connectionId;

        } catch (Exception exception) {
            client.close();
            throw new IllegalStateException(
                    "Unable to establish MongoDB connection",
                    exception);
        }
    }

    public ManagedConnection getConnection(String connectionId) {
        if (connectionId == null || connectionId.isBlank()) {
            throw new IllegalArgumentException("Connection ID is required");
        }

        ManagedConnection connection = connections.get(connectionId);

        if (connection == null) {
            throw new IllegalArgumentException(
                    "MongoDB connection not found or expired");
        }

        if (!Instant.now().isBefore(connection.expiresAt())) {
            if (connections.remove(connectionId, connection)) {
                connection.client().close();
            }

            throw new IllegalArgumentException(
                    "MongoDB connection not found or expired");
        }

        return connection;
    }

    @Override
    public boolean supportsManagedConnection(String connectionId) {
        try {
            getConnection(connectionId);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    @Override
    public void closeConnection(String connectionId) {
        if (connectionId == null) {
            return;
        }

        ManagedConnection connection = connections.remove(connectionId);

        if (connection != null) {
            connection.client().close();
        }
    }

    public MongoClient createClient(DatabaseConnectionRequest request) {
        String authDatabase = request.authenticationDatabase();

        if (authDatabase == null || authDatabase.isBlank()) {
            authDatabase = request.database();
        }

        MongoCredential credential = MongoCredential.createCredential(
                request.username(),
                authDatabase,
                request.password().toCharArray());

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder
                        .hosts(List.of(new ServerAddress(
                                request.host(), request.port())))
                        .serverSelectionTimeout(5, TimeUnit.SECONDS))
                .credential(credential)
                .applyToSocketSettings(builder -> builder
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS))
                .build();

        return MongoClients.create(settings);
    }

    private boolean isValidRequest(DatabaseConnectionRequest request) {
        return supports(request)
                && !isBlank(request.host())
                && !isBlank(request.database())
                && !isBlank(request.username())
                && request.password() != null
                && request.port() != null
                && request.port() >= 1
                && request.port() <= 65535;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public record ManagedConnection(
            MongoClient client,
            String database,
            Instant expiresAt) {
    }
}
