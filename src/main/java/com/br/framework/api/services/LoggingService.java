package com.br.framework.api.services;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LoggingService {

    private static boolean logSql = false;
    private static final Map<Class, Logger> loggers = new HashMap<>();

    private static Logger getLogger(final Class clazz) {
        Logger logger = loggers.get(clazz);
        if (logger != null) {
            return logger;
        } else {
            logger = LoggerFactory.getLogger(clazz);
            loggers.put(clazz, logger);
            return logger;
        }
    }

    public static void setShouldLogSql(final boolean value) {
        LoggingService.logSql = value;
    }

    public static boolean isLoggingSql() {
        return LoggingService.logSql;
    }

    public static void info(final Class clazz, final String content) {
        Logger logger = getLogger(clazz);
        logger.info(content);
    }

    public static void err(final Class clazz, final String content) {
        Logger logger = getLogger(clazz);
        logger.error(content);
    }

}
