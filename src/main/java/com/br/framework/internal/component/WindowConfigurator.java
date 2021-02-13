package com.br.framework.internal.component;

import com.br.framework.internal.component.factory.WindowFactory;
import com.br.framework.internal.component.factory.CrudFactory;
import com.br.framework.internal.component.factory.SwingComponentFactory;
import java.sql.SQLException;

public class WindowConfigurator {

    private WindowConfiguration config;

    private WindowConfigurator() {
        // Empty.
    }

    public static WindowConfigurator getInstance() {
        return new WindowConfigurator().newConfiguration();
    }

    private WindowConfigurator newConfiguration() {
        config = WindowConfiguration.newInstance();
        return this;
    }

    public WindowConfigurator title(final String title) {
        config.setTitle(title);
        return this;
    }

    public WindowConfigurator width(final int width) {
        config.setWidth(width);
        return this;
    }

    public WindowConfigurator height(final int height) {
        config.setHeight(height);
        return this;
    }

    public WindowConfigurator centered(final boolean centered) {
        config.setCentered(centered);
        return this;
    }

    public WindowConfigurator table(final String table) {
        config.setTable(table);
        return this;
    }

    public WindowConfigurator includeAttribute(final String attrib) {
        return includeAttribute(attrib, attrib);
    }

    public WindowConfigurator includeAttribute(final String attrib, final String alias) {
        config.getAttributes().put(attrib, alias);
        return this;
    }

    public WindowConfigurator sequence(final String seqName) {
        config.setSequence(seqName);
        return this;
    }

    public WindowConfigurator primaryKey(final String field) {
        config.setPkField(field);
        return this;
    }

    public Window build() throws SQLException, Exception {
        final Window frame;

        if (config == null) {
            throw new Exception("Frame configuration is not defined.");
        }

        if (config.getHeight() * config.getWidth() < (800 * 600)) {
            throw new Exception("Minimum resolution of 800x600 is required.");
        }

        frame = WindowFactory.build(
                SwingComponentFactory.createJFrameFromConfig(config),
                config
        );
        CrudFactory.createCrud(frame);
        config = null;
        return frame;
    }

}
