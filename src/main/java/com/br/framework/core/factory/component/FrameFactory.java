package com.br.framework.core.factory.component;

import com.br.framework.api.configurator.FrameConfigurator.FrameConfig;
import com.br.framework.core.component.Frame;
import com.br.framework.core.controller.FrameController;
import com.br.framework.core.component.Handlebar;
import com.br.framework.core.component.Table;
import com.br.framework.core.database.metadata.MetadataLoader;
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
    
    private final JFrame jframe;
    private final FrameConfig config;
    
    private FrameFactory(final JFrame frame, final FrameConfig config){
        this.jframe = frame;
        this.config = config;
    }
    
    public static FrameFactory getInstance(final JFrame frame, final FrameConfig config){
        return new FrameFactory(frame, config);
    }
    
    public Frame build() {
        final Frame frame = Frame.getInstance(config);
        final Handlebar handlebar = Handlebar.getInstance(frame);
        final Table table = Table.getInstance(frame);
        final FrameController controller = FrameController.getInstance(frame);
        frame.setController(controller);
        frame.setHandlebar(handlebar);
        frame.setMetadata(MetadataLoader.load(config.getTable()));
        frame.setTable(table);
        config.setSql(buildTableSqlFromAttributes(frame, config));
        frame.getController().addComponent(FrameComponent.SWING_JFRAME, jframe);        
        return frame;
    }
    
    private String buildTableSqlFromAttributes(final Frame frame, final FrameConfig config) {
        if (config.getAttributes() == null || config.getAttributes().isEmpty()) {
            return SqlBoilerplate.SELECT_ALL_FROM.sql().concat(config.getTable());
        } else {
            StringBuilder sql = new StringBuilder();
            sql.append(SqlBoilerplate.SELECT.sql());
            config.getAttributes().keySet().forEach(key -> {
                final String value = config.getAttributes().get(key);
                sql.append(key.concat(" '").concat(value).concat("',"));
                frame.getController().addAttribAlias(key, value);
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
