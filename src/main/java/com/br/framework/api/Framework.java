package com.br.framework.api;

import com.br.framework.api.configurator.FrameConfigurator;
import com.br.framework.api.services.LoggingService;
import com.br.framework.api.services.PropertiesServices;
import com.br.framework.core.database.connection.IConnectionPool;
import com.br.framework.core.database.queryresult.QueryResult;
import com.br.framework.core.database.queryresult.QueryResultFactory;
import com.br.framework.core.enumerator.ConnectionAuth;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class Framework {

    private static Framework instance;
    private static IConnectionPool connectionPool;
    private static final QueryResultFactory queryResultFactory = new QueryResultFactory();

    public static Framework getInstance() {
        return getInstance(false);
    }

    public static Framework getInstance(final Boolean newInstance) {
        if (instance == null || newInstance) {
            instance = new Framework();
        }
        return new Framework();
    }

    public static FrameConfigurator getFrameConfigurator() {
        return FrameConfigurator.getInstance();
    }

    private static void checkLogSql(final String sql) {
        if (LoggingService.isLoggingSql()) {
            LoggingService.info(Framework.class, "QUERY: ".concat(sql));
        }
    }

    private static void checkLogSql(final String sql, final Object... arguments) {
        checkLogSql(sql);
        if (LoggingService.isLoggingSql() && arguments != null && arguments.length > 0) {
            final StringBuilder argsStr = new StringBuilder();
            argsStr.append("ARGUMENTS : [");
            for (final Object iterator : arguments) {
                argsStr.append("\"").append(String.valueOf(iterator)).append("\";");
            }
            argsStr.setLength(argsStr.length() - 1);
            argsStr.append("]");
            LoggingService.info(Framework.class, argsStr.toString());
        }
    }

    public static boolean isPoolDefined() {
        return connectionPool != null;
    }

    public static void connectionPool(final IConnectionPool connectionPool) {
        Framework.connectionPool = connectionPool;
    }

    public static QueryResult run(final String query) throws SQLException {
        checkLogSql(query);
        final Connection conn = connectionPool.open();
        final PreparedStatement preparedStatement = conn.prepareStatement(query);

        final ResultSet resultSet = preparedStatement.executeQuery();
        connectionPool.close(conn);
        return queryResultFactory.build(resultSet);
    }

    public static QueryResult run(final String query, final Object... arguments) throws SQLException {
        checkLogSql(query, arguments);
        final Connection conn = connectionPool.open();
        final PreparedStatement preparedStatement = conn.prepareStatement(query);
        for (int i = 1; i <= arguments.length; i++) {
            preparedStatement.setObject(i, arguments[i - 1]);
        }

        final ResultSet resultSet = preparedStatement.executeQuery();
        connectionPool.close(conn);
        return queryResultFactory.build(resultSet);
    }

    public static void execute(final String query) throws Exception {
        checkLogSql(query);
        final Connection conn = connectionPool.open();
        final PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.execute();
        connectionPool.close(conn);
    }

    public static void execute(final String query, final Object... arguments) throws SQLException {
        checkLogSql(query, arguments);
        final Connection conn = connectionPool.open();
        final PreparedStatement preparedStatement = conn.prepareStatement(query);
        for (int i = 1; i <= arguments.length; i++) {
            preparedStatement.setObject(i, arguments[i - 1]);
        }

        preparedStatement.execute();
        connectionPool.close(conn);
    }

    public static void setDatabaseCredentials(final String url,
            final String user,
            final String password) {
        PropertiesServices.set(ConnectionAuth.URL.content(), url);
        PropertiesServices.set(ConnectionAuth.USER.content(), user);
        PropertiesServices.set(ConnectionAuth.PASSWORD.content(), password);

    }

}
