package com.br.framework.internal.database;

import com.br.framework.Framework;
import com.br.framework.internal.tooling.InternalLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class QueryService {

    private final ConnectionPool connectionPool;
    private final QueryResultFactory queryResultFactory;
    private static QueryService instance;

    private QueryService() {
        connectionPool = new ConnectionPool();
        queryResultFactory = QueryResultFactory.getInstance(true);
    }

    public static QueryService getInstance() {
        return getInstance(false);
    }

    public static QueryService getInstance(boolean newInstance) {
        if (instance == null || newInstance) {
            instance = new QueryService();
        }
        return instance;
    }

    private void logQuery(final String query) {
        logQuery(query, new Object[0]);
    }

    private void logQuery(final String query, final Object... arguments) {
        InternalLogger.info(Framework.class, String.format("QUERY: %s", query));
        if (arguments != null && arguments.length > 0) {
            final StringBuilder argsStr = new StringBuilder();
            argsStr.append("ARGUMENTS : [");
            for (final Object iterator : arguments) {
                argsStr.append("\"").append(String.valueOf(iterator)).append("\";");
            }
            argsStr.setLength(argsStr.length() - 1);
            argsStr.append("]");
            InternalLogger.info(Framework.class, argsStr.toString());
        }
    }

    public QueryResult query(final String query) throws SQLException {
        logQuery(query);
        final Connection conn = connectionPool.open();
        final PreparedStatement preparedStatement = conn.prepareStatement(query);

        final ResultSet resultSet = preparedStatement.executeQuery();
        connectionPool.close(conn);
        return queryResultFactory.build(resultSet);
    }

    public QueryResult query(final String query, final Object... arguments) throws SQLException {
        logQuery(query, arguments);
        final Connection conn = connectionPool.open();
        final PreparedStatement preparedStatement = conn.prepareStatement(query);
        for (int i = 1; i <= arguments.length; i++) {
            preparedStatement.setObject(i, arguments[i - 1]);
        }

        final ResultSet resultSet = preparedStatement.executeQuery();
        connectionPool.close(conn);
        return queryResultFactory.build(resultSet);
    }

    public void execute(final String query) throws Exception {
        logQuery(query);
        final Connection conn = connectionPool.open();
        final PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.execute();
        connectionPool.close(conn);
    }

    public void execute(final String query, final Object... arguments) throws SQLException {
        logQuery(query, arguments);
        final Connection conn = connectionPool.open();
        final PreparedStatement preparedStatement = conn.prepareStatement(query);

        for (int i = 1; i <= arguments.length; i++) {
            preparedStatement.setObject(i, arguments[i - 1]);
        }

        preparedStatement.execute();
        connectionPool.close(conn);
    }

    public void setDatabaseCredentials(final String url,
            final String user,
            final String password) {
        connectionPool.setUrl(url);
        connectionPool.setUser(user);
        connectionPool.setPassword(password);
    }
}
