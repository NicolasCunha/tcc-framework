package com.br.framework.internal.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

public final class ConnectionPool {

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
