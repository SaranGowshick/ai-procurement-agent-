
package com.ai.procurement.model.database.request;

import java.util.List;

public record DatabaseDataRequest(
        String connectionId,
        String collection,
        List<String> fields,
        Integer limit
) {
}
