
package com.ai.procurement.model.database.request;

import java.util.List;

public record DatabaseOperationRequest(
        String operation,
        String connectionId,
        String collectionName,
        List<String> fields,
        Integer limit,
        Integer sampleSize
) {}
