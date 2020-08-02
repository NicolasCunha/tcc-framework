package com.br.framework.core.component;

import com.br.framework.api.configurator.FrameConfigurator.FrameConfig;
import com.br.framework.core.controller.FrameController;
import com.br.framework.core.dbmetadata.TableMetadata;
import com.br.framework.core.enumerator.FrameComponent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;

public class Frame {

    private final FrameConfig config;
    private Handlebar handlebar;
    private Table table;
    private TableMetadata metadata;
    private final Map<FrameComponent, Object> componentMap;
    private FrameController controller;

    private Frame(final FrameConfig config) {
        this.componentMap = new HashMap<>();
        this.config = config;
    }

    public static Frame newInstance(final FrameConfig config) {
        return new Frame(config);
    }

    public FrameController getController() {
        return controller;
    }

    public void setController(FrameController controller) {
        this.controller = controller;
    }

    public FrameConfig getConfig() {
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

    public Map<FrameComponent, Object> getComponentMap() {
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
        final JFrame frame = (JFrame) controller.getComponent(FrameComponent.SWING_JFRAME);
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

}
