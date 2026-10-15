
package com.ai.procurement.model.database.request;

import com.ai.procurement.model.database.databasetypes.DatabaseType;

public record DatabaseConnectionRequest(
        DatabaseType databaseType,
        String host,
        Integer port,
        String database,
        String username,
        String password,
        String authenticationDatabase
) {
}
