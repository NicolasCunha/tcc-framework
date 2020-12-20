package com.br.framework.internal.component;

import com.br.framework.configurator.WindowConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;

public class Window implements IDestroyable {

    private WindowConfiguration config;
    private Handlebar handlebar;
    private Table table;
    private TableMetadata metadata;
    private Map<WindowComponentEnum, Object> componentMap;
    private WindowController controller;

    private Window(final WindowConfiguration config) {
        this.componentMap = new HashMap<>();
        this.config = config;
    }

    public static Window getInstance(final WindowConfiguration config) {
        return new Window(config);
    }

    public WindowController getController() {
        return controller;
    }

    public void setController(WindowController controller) {
        this.controller = controller;
    }

    public WindowConfiguration getConfig() {
        return config;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public TableMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(TableMetadata metadata) {
        this.metadata = metadata;
    }

    public Map<WindowComponentEnum, Object> getComponentMap() {
        return componentMap;
    }

    public List<Map<String, Object>> getSelectedRows() {
        return table.getSelectedRows();
    }

    public Handlebar getHandlebar() {
        return handlebar;
    }

    public void setHandlebar(Handlebar handlebar) {
        this.handlebar = handlebar;
    }

    public void show() {
        final JFrame frame = (JFrame) controller.getComponent(WindowComponentEnum.SWING_JFRAME);
        frame.setVisible(true);
    }

    public void toGrid() {
        handlebar.toGrid();
    }

    public void toForm() {
        handlebar.toForm();
    }

    public void refresh() {
        table.refresh();
    }

    public void close() {
        final JFrame frame = (JFrame) controller.getComponent(WindowComponentEnum.SWING_JFRAME);
        frame.dispose();
        frame.setVisible(false);
        this.destroy();
    }

    @Override
    public void destroy() {
        this.table.destroy();
        this.handlebar.destroy();
        this.config = null;
        this.componentMap = null;
        this.controller = null;
        this.metadata = null;
    }

}
