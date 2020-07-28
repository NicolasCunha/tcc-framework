package com.br.framework.core.database.connection;

import com.br.framework.core.enumerator.ConnectionAuth;
import com.br.framework.core.system.FrameworkProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

public class ConnectionPool implements IConnectionPool {

    private final Stack<Connection> connectionStack = new Stack<>();

    private Connection newConnection() throws SQLException {
        return DriverManager.getConnection(
                FrameworkProperties.get(ConnectionAuth.URL.content()),
                FrameworkProperties.get(ConnectionAuth.USER.content()),
                FrameworkProperties.get(ConnectionAuth.PASSWORD.content())
        );
    }

    @Override
    public Connection open() throws SQLException {
        if (!connectionStack.isEmpty()) {
            return connectionStack.pop();
        } else {
            return newConnection();
        }
    }

    @Override
    public void close(final Connection connection) {
        connectionStack.add(connection);
    }

}
