package com.br.framework.core.factory;

import com.br.framework.api.configurator.FrameConfigurator.FrameConfig;
import com.br.framework.core.Frame;
import com.br.framework.core.controller.FrameController;
import com.br.framework.core.Handlebar;
import com.br.framework.core.dbmetadata.MetadataLoader;
import com.br.framework.core.enumerator.FrameComponent;
import javax.swing.JFrame;

public class FrameFactory {

    private enum SqlBoilerplate {
        SELECT_ALL("SELECT *"),
        SELECT("SELECT "),
        FROM("FROM "),
        SELECT_ALL_FROM(SELECT_ALL.sql().concat(FROM.sql()));

        private final String sql;

        private SqlBoilerplate(final String sql) {
            this.sql = sql;
        }

        public String sql() {
            return this.sql;
        }
    }

    public Frame build(final JFrame jframe, final FrameConfig config) {
        final Frame frame = new Frame();
        final Handlebar handlebar = Handlebar.newInstance(frame);
        final String frameQuery = build(frame, config);
        frame.setHandlebar(handlebar);
        frame.setMetadata(MetadataLoader.load(config.getTable()));
        FrameController.addComponent(frame, FrameComponent.SWING_JFRAME, jframe);
        FrameController.addComponent(frame, FrameComponent.HANDLEBAR, handlebar);
        FrameController.addComponent(frame, FrameComponent.FRAME_CONFIG, config);
        FrameController.addComponent(frame, FrameComponent.FRAME_QUERY, frameQuery);
        return frame;
    }

    private String build(final Frame frame, final FrameConfig config) {
        if (config.getAttributes() == null || config.getAttributes().isEmpty()) {
            return SqlBoilerplate.SELECT_ALL_FROM.sql().concat(config.getTable());
        } else {
            StringBuilder sql = new StringBuilder();
            sql.append(SqlBoilerplate.SELECT.sql());
            config.getAttributes().keySet().forEach(key -> {
                final String value = config.getAttributes().get(key);
                sql.append(key.concat(" '").concat(value).concat("',"));
                FrameController.addAttribAlias(frame, key, value);
            });
            if (sql.toString().endsWith(",")) {
                sql.setLength(sql.length() - 1);
            }
            sql.append(" ");
            sql.append(SqlBoilerplate.FROM.sql().concat(config.getTable()));
            return sql.toString();
        }

    }

}
