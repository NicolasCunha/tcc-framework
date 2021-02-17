package com.br.framework;

import com.br.framework.internal.component.Window;
import com.br.framework.internal.component.WindowConfiguration;
import com.br.framework.internal.component.factory.WindowFactory;
import com.br.framework.internal.component.factory.CrudFactory;
import com.br.framework.internal.component.factory.SwingComponentFactory;
import java.sql.SQLException;

public class WindowBuilder {

    private WindowConfiguration config;

    private WindowBuilder() {
        // Empty.
    }

    public static WindowBuilder getInstance() {
        return new WindowBuilder().newConfiguration();
    }

    private WindowBuilder newConfiguration() {
        config = WindowConfiguration.newInstance();
        return this;
    }

    public WindowBuilder title(final String title) {
        config.setTitle(title);
        return this;
    }

    public WindowBuilder width(final int width) {
        config.setWidth(width);
        return this;
    }

    public WindowBuilder height(final int height) {
        config.setHeight(height);
        return this;
    }

    public WindowBuilder centered(final boolean centered) {
        config.setCentered(centered);
        return this;
    }

    public WindowBuilder table(final String table) {
        config.setTable(table);
        return this;
    }

    public WindowBuilder includeAttribute(final String attrib) {
        return includeAttribute(attrib, attrib);
    }

    public WindowBuilder includeAttribute(final String attrib, final String alias) {
        config.getAttributes().put(attrib, alias);
        return this;
    }

    public WindowBuilder sequence(final String seqName) {
        config.setSequence(seqName);
        return this;
    }

    public WindowBuilder primaryKey(final String field) {
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
