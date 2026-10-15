
package com.ai.procurement.connector.relations;

import com.ai.procurement.model.database.response.CollectionRelationshipResponse;
import com.ai.procurement.model.database.request.DatabaseConnectionRequest;

import java.util.List;

public interface DatabaseRelationshipConnector {

    boolean supportsRelationships(DatabaseConnectionRequest request);

    List<CollectionRelationshipResponse> discoverRelationships(
            DatabaseConnectionRequest request,
            String collectionName,
            int sampleSize
    );
}
