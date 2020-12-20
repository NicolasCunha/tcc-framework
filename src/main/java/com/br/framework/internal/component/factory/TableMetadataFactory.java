package com.br.framework.internal.component.factory;

import com.br.framework.internal.component.TableMetadata;
import com.br.framework.Framework;
import com.br.framework.internal.tooling.InternalLogger;
import com.br.framework.internal.database.QueryResult;
import java.sql.SQLException;

public final class TableMetadataFactory {

    private static TableMetadataFactory instance;

    private TableMetadataFactory() {
        // Empty.
    }

    public static TableMetadataFactory getInstance() {
        return getInstance(false);
    }

    public static TableMetadataFactory getInstance(boolean newInstance) {
        if (instance == null || newInstance) {
            instance = new TableMetadataFactory();
        }
        return instance;
    }

    public TableMetadata loadMetadata(final String table) {
        final String MARIADB_METADATA_FROM_QUERY = "show keys from %s";
        final TableMetadata metadata = TableMetadata.getInstance();
        final QueryResult queryResult;
        try {
            queryResult = Framework.getInstance().query(String.format(MARIADB_METADATA_FROM_QUERY, table));
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
