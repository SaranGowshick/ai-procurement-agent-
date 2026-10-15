
package com.ai.procurement.connector.managers;

import com.ai.procurement.model.database.request.DatabaseConnectionRequest;

public interface ManagedDatabaseConnectionProvider {

    boolean supports(DatabaseConnectionRequest request);

    String createConnection(DatabaseConnectionRequest request);

    boolean supportsManagedConnection(String connectionId);

    void closeConnection(String connectionId);
}
