package com.br.framework.internal.infra;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class InternalLogger {

    private static final Map<Class, Logger> logCache = new HashMap<>();

    public static Logger getLogger(final Class clazz) {
        Logger logger = logCache.get(clazz);
        if (logger != null) {
            return logger;
        } else {
            logger = LoggerFactory.getLogger(clazz);
            logCache.put(clazz, logger);
            return logger;
        }
    }

    public static void info(final Class clazz, final String content) {
        getLogger(clazz).info(content);
    }

    public static void err(final Class clazz, final String content) {
        getLogger(clazz).info(content);
    }

}
