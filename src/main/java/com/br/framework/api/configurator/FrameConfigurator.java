package com.br.framework.api.configurator;

import com.br.framework.core.Frame;
import com.br.framework.core.database.connection.ConnectionPool;
import com.br.framework.core.database.query.QueryService;
import com.br.framework.core.factory.FrameFactory;
import com.br.framework.core.factory.crud.CrudFactory;
import com.br.framework.core.factory.swing.JFrameFactory;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JFrame;

public class FrameConfigurator {

    public class FrameConfig {

        private FrameConfig() {

        }

        private String title;
        private int width, height;
        private boolean centered;
        private String table;
        private Map<String, String> attributes = new LinkedHashMap<>();
        private String sequence;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public boolean isCentered() {
            return centered;
        }

        public void setCentered(boolean centered) {
            this.centered = centered;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public Map<String, String> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<String, String> attributes) {
            this.attributes = attributes;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

    }

    private FrameConfig config;
    private Map<String, String> attributes;
    private final FrameFactory frameFactory = new FrameFactory();
    private final JFrameFactory jframeFactory = new JFrameFactory();
    private final CrudFactory crudFactory = new CrudFactory();

    private FrameConfigurator() {

    }

    public static FrameConfigurator newInstance() {
        return new FrameConfigurator();
    }

    private void checkDefInstance() {
        if (config == null) {
            config = new FrameConfig();
        }
        if (attributes == null) {
            attributes = new HashMap<>();
        }
    }

    public FrameConfigurator title(final String title) {
        checkDefInstance();
        config.setTitle(title);
        return this;
    }

    public FrameConfigurator width(final int width) {
        checkDefInstance();
        config.setWidth(width);
        return this;
    }

    public FrameConfigurator height(final int height) {
        checkDefInstance();
        config.setHeight(height);
        return this;
    }

    public FrameConfigurator centered(final boolean centered) {
        checkDefInstance();
        config.setCentered(centered);
        return this;
    }

    public FrameConfigurator table(final String table) {
        checkDefInstance();
        config.setTable(table);
        return this;
    }

    public FrameConfigurator includeAttribute(final String attrib) {
        checkDefInstance();
        config.getAttributes().put(attrib, attrib);
        return this;
    }

    public FrameConfigurator includeAttribute(final String attrib, final String alias) {
        checkDefInstance();
        config.getAttributes().put(attrib, alias);
        return this;
    }

    public FrameConfigurator sequence(final String seqName) {
        checkDefInstance();
        config.setSequence(seqName);
        return this;
    }

    public Frame build() throws SQLException, Exception {
        if (config == null) {
            throw new Exception("Frame configuration is not defined.");
        }
        if (!QueryService.isPoolDefined()) {
            QueryService.connectionPool(new ConnectionPool());
        }
        final JFrame swingFrame = jframeFactory.build(config);
        final Frame frame = frameFactory.build(swingFrame, config);
        crudFactory.createCrud(frame);
        config = null;
        return frame;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

}
