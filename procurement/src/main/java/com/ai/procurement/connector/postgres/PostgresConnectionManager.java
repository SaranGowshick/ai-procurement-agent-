
package com.ai.procurement.connector.postgres;

import com.ai.procurement.connector.DatabaseConnector;
import com.ai.procurement.connector.managers.ManagedDatabaseConnectionProvider;
import com.ai.procurement.model.database.databasetypes.DatabaseType;
import com.ai.procurement.model.database.request.DatabaseConnectionRequest;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class PostgresConnectionManager
        implements DatabaseConnector, ManagedDatabaseConnectionProvider {

    private final ConcurrentMap<String, HikariDataSource> connections =
            new ConcurrentHashMap<>();

    // ---------------------------------------------------------
    // Database support
    // ---------------------------------------------------------

    @Override
    public boolean supports(DatabaseConnectionRequest request) {
        return request != null
                && request.databaseType() == DatabaseType.POSTGRESQL;
    }

    // ---------------------------------------------------------
    // Test connection
    // ---------------------------------------------------------

    @Override
    public boolean testConnection(DatabaseConnectionRequest request) {
        if (!isValidRequest(request)) {
            return false;
        }

        try (HikariDataSource dataSource = createDataSource(request);
             Connection connection = dataSource.getConnection()) {

            return connection.isValid(3);

        } catch (SQLException | RuntimeException exception) {
            return false;
        }
    }

    // ---------------------------------------------------------
    // Create and manage connections
    // ---------------------------------------------------------

    @Override
    public String createConnection(DatabaseConnectionRequest request) {
        if (!isValidRequest(request)) {
            throw new IllegalArgumentException(
                    "Valid PostgreSQL connection details are required");
        }

        HikariDataSource dataSource = createDataSource(request);

        try {
            try (Connection connection = dataSource.getConnection()) {
                if (!connection.isValid(3)) {
                    throw new IllegalArgumentException(
                            "PostgreSQL connection validation failed");
                }
            }

            String connectionId = UUID.randomUUID().toString();
            connections.put(connectionId, dataSource);

            return connectionId;

        } catch (SQLException | RuntimeException exception) {
            dataSource.close();

            if (exception instanceof IllegalArgumentException illegalArgument) {
                throw illegalArgument;
            }

            throw new IllegalArgumentException(
                    "Unable to establish PostgreSQL connection");
        }
    }

    @Override
    public boolean supportsManagedConnection(String connectionId) {
        if (connectionId == null) {
            return false;
        }

        HikariDataSource dataSource = connections.get(connectionId);

        return dataSource != null && !dataSource.isClosed();
    }

    @Override
    public void closeConnection(String connectionId) {
        if (connectionId == null) {
            return;
        }

        HikariDataSource dataSource = connections.remove(connectionId);

        if (dataSource != null) {
            dataSource.close();
        }
    }

    // ---------------------------------------------------------
    // Provide a connection to the operations implementation
    // ---------------------------------------------------------

    public Connection getConnection(String connectionId)
            throws SQLException {

        HikariDataSource dataSource = connections.get(connectionId);

        if (dataSource == null || dataSource.isClosed()) {
            throw new IllegalArgumentException(
                    "PostgreSQL connection not found or already closed");
        }

        return dataSource.getConnection();
    }

    // ---------------------------------------------------------
    // DataSource creation
    // ---------------------------------------------------------

    private HikariDataSource createDataSource(
            DatabaseConnectionRequest request) {

        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(
                "jdbc:postgresql://"
                        + request.host()
                        + ":"
                        + request.port()
                        + "/"
                        + request.database());

        dataSource.setUsername(request.username());
        dataSource.setPassword(request.password());

        dataSource.setConnectionTimeout(5000);
        dataSource.setValidationTimeout(3000);
        dataSource.setMaximumPoolSize(5);

        return dataSource;
    }

    // ---------------------------------------------------------
    // Request validation
    // ---------------------------------------------------------

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
}
