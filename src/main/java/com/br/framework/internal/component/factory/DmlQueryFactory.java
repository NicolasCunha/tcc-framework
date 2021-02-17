package com.br.framework.internal.component.factory;

import com.br.framework.internal.component.WindowConfiguration;
import com.br.framework.internal.component.Window;

public class DmlQueryFactory {

    private DmlQueryFactory() {
        // Empty.
    }

    public static String createInsert(final Window window) {
        final WindowConfiguration config = window.getConfig();
        final String dbTable = config.getTable();
        final String sequence = config.getSequence();
        final String primaryKey = config.getPkField();
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("INSERT INTO %s", dbTable));
        builder.append(String.format("(%s,", primaryKey));
        config.getAttributes().keySet().stream().filter(attribute -> (!attribute.equals(primaryKey))).forEachOrdered(attribute -> {
            builder.append(attribute.concat(","));
        });
        if (builder.toString().endsWith(",")) {
            builder.setLength(builder.length() - 1);
        }
        builder.append(") ");
        builder.append(String.format("VALUES (nextval(%s), ", sequence));
        config.getAttributes().keySet().stream().filter(attribute -> (!attribute.equals(primaryKey))).forEachOrdered(attribute -> {
            builder.append("?,");
        });
        if (builder.toString().endsWith(",")) {
            builder.setLength(builder.length() - 1);
        }
        builder.append(")");
        return builder.toString();
    }

    public static String createDelete(final Window window) {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("DELETE FROM %s ", window.getConfig().getTable()));
        builder.append(String.format("WHERE %s = ? ", window.getConfig().getPkField()));
        return builder.toString();
    }

}
