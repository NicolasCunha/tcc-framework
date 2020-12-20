package com.br.framework.internal.component.factory;

import com.br.framework.configurator.WindowConfiguration;
import com.br.framework.internal.component.Window;
import com.br.framework.internal.component.WindowController;
import com.br.framework.internal.component.Handlebar;
import com.br.framework.internal.component.Table;
import com.br.framework.internal.component.WindowComponentEnum;
import javax.swing.JFrame;

public final class WindowFactory {

    private final TableMetadataFactory metadataFactory;

    private static WindowFactory frameFactory;

    private WindowFactory() {
        metadataFactory = TableMetadataFactory.getInstance(true);
    }

    public static WindowFactory getInstance() {
        return getInstance(false);
    }

    public static WindowFactory getInstance(boolean newInstance) {
        if (frameFactory == null || newInstance) {
            frameFactory = new WindowFactory();
        }
        return frameFactory;
    }

    public Window build(final JFrame jframe, final WindowConfiguration config) {
        final Window frame = Window.getInstance(config);
        final Handlebar handlebar = Handlebar.withWindow(frame);
        final Table table = Table.getInstance(frame);
        final WindowController controller = WindowController.getInstance(frame);
        frame.setController(controller);
        frame.setHandlebar(handlebar);
        frame.setMetadata(metadataFactory.loadMetadata(config.getTable()));
        frame.setTable(table);
        config.setSql(buildTableSqlFromAttributes(frame, config));
        frame.getController().addComponent(WindowComponentEnum.SWING_JFRAME, jframe);
        return frame;
    }

    private String buildTableSqlFromAttributes(final Window frame, final WindowConfiguration config) {
        final String SELECT = "SELECT ";
        final String SELECT_ALL = "SELECT *";
        final String FROM = "FROM ";
        final String SELECT_ALL_FROM = SELECT_ALL.concat(FROM);
        if (config.getAttributes() == null || config.getAttributes().isEmpty()) {
            return String.format(SELECT_ALL_FROM.concat("%s"), config.getTable());
        } else {
            StringBuilder sql = new StringBuilder();
            sql.append(SELECT);
            config.getAttributes().keySet().forEach(key -> {
                final String value = config.getAttributes().get(key);
                sql.append(key.concat(" '").concat(value).concat("',"));
                frame.getController().addAttribAlias(key, value);
            });
            if (sql.toString().endsWith(",")) {
                sql.setLength(sql.length() - 1);
            }
            sql.append(" ");
            sql.append(String.format(FROM.concat("%s"), config.getTable()));
            return sql.toString();
        }

    }

}
