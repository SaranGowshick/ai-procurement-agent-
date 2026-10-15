package com.ai.procurement.connector;

import com.ai.procurement.model.database.request.DatabaseConnectionRequest;

public interface DatabaseConnector {

    boolean supports(DatabaseConnectionRequest request);

    boolean testConnection(DatabaseConnectionRequest request);

}