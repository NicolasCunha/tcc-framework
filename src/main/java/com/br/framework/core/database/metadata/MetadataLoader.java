package com.br.framework.core.database.metadata;

import com.br.framework.core.database.query.QueryResult;
import com.br.framework.api.services.QueryService;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class MetadataLoader {

    public static TableMetadata load(final String table) {
        final TableMetadata metadata = TableMetadata.newInstance();
        final QueryResult queryResult = runQuery(table);
        while (queryResult.next()) {
            if (queryResult.getString("Key_name").equalsIgnoreCase("PRIMARY")) {
                metadata.getPrimaryKey().add(queryResult.getString("Column_name"));
            }
        }
        return metadata;
    }

    private static QueryResult runQuery(final String table) {        
        QueryResult qResult = null;
        try {
            qResult = QueryService.run("show keys from ".concat(table));
        } catch (SQLException ex) {
            Logger.getLogger(MetadataLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return qResult != null ? qResult : null;
    }

}
