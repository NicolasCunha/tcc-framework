package com.br.framework.internal.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryResultFactory {

    private final QueryMetadataFactory controller;

    private static QueryResultFactory instance;

    public static QueryResultFactory getInstance() {
        return getInstance(false);
    }

    public static QueryResultFactory getInstance(boolean newInstance) {
        if (instance == null || newInstance) {
            instance = new QueryResultFactory();
        }
        return instance;
    }

    private QueryResultFactory() {
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
