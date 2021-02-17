package com.br.framework.internal.component.factory;

import com.br.framework.internal.component.TableMetadata;
import com.br.framework.Database;
import com.br.framework.internal.logger.InternalLogger;
import com.br.framework.internal.queryResult.QueryResult;
import java.sql.SQLException;

public final class TableMetadataFactory {

    public static TableMetadata loadMetadata(final String table) {
        final String MARIADB_METADATA_FROM_QUERY = "show keys from %s";
        final TableMetadata metadata = TableMetadata.getInstance();
        final QueryResult queryResult;
        try {
            queryResult = Database.query(String.format(MARIADB_METADATA_FROM_QUERY, table));
            while (queryResult.next()) {
                if (queryResult.getString("Key_name").equalsIgnoreCase("PRIMARY")) {
                    metadata.getPrimaryKey().add(queryResult.getString("Column_name"));
                }
            }
        } catch (SQLException ex) {
            InternalLogger.err(TableMetadataFactory.class, ex.getMessage());
        }
        return metadata;
    }

}
