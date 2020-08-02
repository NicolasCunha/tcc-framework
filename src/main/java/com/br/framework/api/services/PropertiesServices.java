package com.br.framework.api.services;

import com.br.framework.core.enumerator.ConnectionAuth;

public abstract class PropertiesServices {

    public static void set(final String name, final String value) {
        System.setProperty(name, value);
    }

    public static String get(final String name) {
        return System.getProperty(name);
    }

    public static boolean isSet(final String name) {
        return get(name) == null || get(name).isEmpty();
    }

    public static void setupDatabaseAuth(
            final String url,
            final String user,
            final String password) {
        set(ConnectionAuth.URL.content(), url);
        set(ConnectionAuth.USER.content(), user);
        set(ConnectionAuth.PASSWORD.content(), password);
    }

}
