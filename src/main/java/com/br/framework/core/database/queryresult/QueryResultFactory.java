package com.br.framework.core.database.queryresult;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryResultFactory {

    private final QueryMetadataFactory controller;

    public QueryResultFactory() {
        controller = new QueryMetadataFactory();
    }

    public QueryResult build(final ResultSet resultSet) throws SQLException {
        final QueryResult sqlResult = new QueryResult();

        sqlResult.setRows(controller.buildRowsFromResultSet(resultSet));
        sqlResult.setColumns(controller.buildColumnsFromResultSet(resultSet));
        sqlResult.setAliasToRow(controller.mapColumnAlias(resultSet));

        return sqlResult;
    }

}
