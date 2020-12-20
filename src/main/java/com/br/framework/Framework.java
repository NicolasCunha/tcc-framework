package com.br.framework;

import com.br.framework.configurator.WindowConfigurator;
import com.br.framework.internal.database.QueryService;
import com.br.framework.internal.database.QueryResult;
import java.sql.SQLException;

public final class Framework {

    private static Framework instance;
    private final QueryService queryService;

    public static Framework getInstance() {
        return getInstance(false);
    }

    public static Framework getInstance(final Boolean newInstance) {
        if (instance == null || newInstance) {
            instance = new Framework();
        }
        return instance;
    }

    private Framework() {
        queryService = QueryService.getInstance(true);
    }

    public WindowConfigurator getWindowConfigurator() {
        return getWindowConfigurator(false);
    }

    public WindowConfigurator getWindowConfigurator(boolean newConfiguration) {
        if (newConfiguration) {
            return WindowConfigurator.getInstance().newConfiguration();
        } else {
            return WindowConfigurator.getInstance();
        }
    }

    public void setDatabaseCredentials(final String url,
            final String user,
            final String password) {
        queryService.setDatabaseCredentials(url, user, password);
    }

    public QueryResult query(final String query) throws SQLException {
        return queryService.query(query);
    }

    public QueryResult query(final String query, final Object... arguments) throws SQLException {
        return queryService.query(query, arguments);
    }

    public void execute(final String query) throws Exception {
        queryService.execute(query);
    }

    public void execute(final String query, final Object... arguments) throws SQLException {
        queryService.execute(query, arguments);
    }

}
