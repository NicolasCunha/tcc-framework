package com.br.framework.core.database.metadata;

import com.br.framework.api.Framework;
import com.br.framework.api.services.LoggingService;
import com.br.framework.core.database.queryresult.QueryResult;
import java.sql.SQLException;

public final class MetadataLoader {

    private static final String METADATA_FROM_QUERY = "show keys from %s";

    public TableMetadata loadMetadata(final String table) {
        final TableMetadata metadata = TableMetadata.getInstance();
        final QueryResult queryResult;
        try {
            queryResult = Framework.run(String.format(METADATA_FROM_QUERY, table));
            while (queryResult.next()) {
                if (queryResult.getString("Key_name").equalsIgnoreCase("PRIMARY")) {
                    metadata.getPrimaryKey().add(queryResult.getString("Column_name"));
                }
            }
        } catch (SQLException ex) {
            LoggingService.err(MetadataLoader.class, ex.getMessage());
        }
        return metadata;
    }

}
