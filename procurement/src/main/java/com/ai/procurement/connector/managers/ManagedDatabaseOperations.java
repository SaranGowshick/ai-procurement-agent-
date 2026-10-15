
package com.ai.procurement.connector.managers;

import com.ai.procurement.model.database.response.CollectionRelationshipResponse;
import com.ai.procurement.model.database.response.CollectionSchemaResponse;

import java.util.List;
import java.util.Map;

public interface ManagedDatabaseOperations {

    boolean supportsConnection(String connectionId);

    CollectionSchemaResponse discoverCollectionSchema(
            String connectionId,
            String collectionName,
            int sampleSize);

    List<CollectionRelationshipResponse> discoverRelationships(
            String connectionId,
            String collectionName,
            int sampleSize);

    List<Map<String, Object>> getFieldValues(
            String connectionId,
            String collectionName,
            List<String> fields,
            Integer limit);
}
