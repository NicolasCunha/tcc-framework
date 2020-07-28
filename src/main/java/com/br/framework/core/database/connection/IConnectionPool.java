package com.br.framework.core.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionPool {

    public Connection open() throws SQLException;
    public void close(final Connection conn);

}
