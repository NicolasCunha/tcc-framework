package com.br.framework;

import com.br.framework.internal.logger.InternalLogger;
import com.br.framework.internal.queryResult.QueryResult;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;

public final class Database {

    private static class ConnectionPool {

        private String url, user, password;

        private final Stack<Connection> connectionStack = new Stack<>();

        private Connection newConnection() throws SQLException {
            return DriverManager.getConnection(url, user, password);
        }

        public Connection open() throws SQLException {
            if (!connectionStack.isEmpty()) {
                return connectionStack.pop();
            } else {
                return newConnection();
            }
        }

        public void close(final Connection connection) {
            connectionStack.add(connection);
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

    private static final ConnectionPool connectionPool = new ConnectionPool();

    private static void logQuery(final String query) {
        logQuery(query, new Object[0]);
    }

    private static void logQuery(final String query, final Object... arguments) {
        InternalLogger.info(Database.class, String.format("QUERY: %s", query));
        if (arguments != null && arguments.length > 0) {
            final StringBuilder argsStr = new StringBuilder();
            argsStr.append("ARGUMENTS : [");
            for (final Object iterator : arguments) {
                argsStr.append("\"").append(String.valueOf(iterator)).append("\";");
            }
            argsStr.setLength(argsStr.length() - 1);
            argsStr.append("]");
            InternalLogger.info(Database.class, argsStr.toString());
        }
    }

    public static QueryResult query(final String query) throws SQLException {
        logQuery(query);
        final Connection conn = connectionPool.open();
        final PreparedStatement preparedStatement = conn.prepareStatement(query);

        final ResultSet resultSet = preparedStatement.executeQuery();
        connectionPool.close(conn);
        return QueryResult.getResult(resultSet);
    }

    public static QueryResult query(final String query, final Object... arguments) throws SQLException {
        logQuery(query, arguments);
        final Connection conn = connectionPool.open();
        final PreparedStatement preparedStatement = conn.prepareStatement(query);
        for (int i = 1; i <= arguments.length; i++) {
            preparedStatement.setObject(i, arguments[i - 1]);
        }

        final ResultSet resultSet = preparedStatement.executeQuery();
        connectionPool.close(conn);
        return QueryResult.getResult(resultSet);
    }

    public static void execute(final String query) throws Exception {
        logQuery(query);
        final Connection conn = connectionPool.open();
        final PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.execute();
        connectionPool.close(conn);
    }

    public static void execute(final String query, final Object... arguments) throws SQLException {
        logQuery(query, arguments);
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
        connectionPool.setUrl(url);
        connectionPool.setUser(user);
        connectionPool.setPassword(password);
    }
}
