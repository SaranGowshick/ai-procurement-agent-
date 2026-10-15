
package com.ai.procurement.model.database.response;

public record CollectionRelationshipResponse(
        String sourceCollection,
        String sourceField,
        String targetCollection,
        String referenceIdType,
        int documentsSampled
) {}
