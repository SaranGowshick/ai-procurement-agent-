
package com.ai.procurement.model.database.response;

import java.util.List;

public record CollectionSchemaResponse(
        String collection,
        int documentsSampled,
        List<FieldSchema> fields
) {

    public record FieldSchema(
            String path,
            List<String> types,
            boolean referenceLike
    ) {
    }
}
