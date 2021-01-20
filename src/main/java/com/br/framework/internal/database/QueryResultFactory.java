package com.br.framework.internal.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryResultFactory {

    private QueryResultFactory() {
        // Empty.
    }

    public static QueryResult build(final ResultSet resultSet) throws SQLException {
        final QueryResult sqlResult = new QueryResult();

        sqlResult.setRows(QueryMetadataFactory.buildRowsFromResultSet(resultSet));
        sqlResult.setColumns(QueryMetadataFactory.buildColumnsFromResultSet(resultSet));
        sqlResult.setAliasToRow(QueryMetadataFactory.mapColumnAlias(resultSet));

        return sqlResult;
    }

}
