
package com.ai.procurement.model.database.response;

import java.util.List;

public record DatabaseSchemaResponse(
        String databaseType,
        String database,
        List<String> objects
) {
}
