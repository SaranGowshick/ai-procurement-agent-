
package com.ai.procurement.connector.relations;

import com.ai.procurement.model.database.request.DatabaseConnectionRequest;
import com.ai.procurement.model.database.response.DatabaseSchemaResponse;

public interface DatabaseSchemaConnector {

    DatabaseSchemaResponse discoverSchema(
            DatabaseConnectionRequest request
    );

    boolean supportsSchema(DatabaseConnectionRequest request);

    boolean supportsManagedSchema(String connectionId);

    DatabaseSchemaResponse discoverManagedSchema(String connectionId);
}
