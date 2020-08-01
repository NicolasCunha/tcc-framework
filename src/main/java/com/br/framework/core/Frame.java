package com.br.framework.core;

import com.br.framework.core.controller.FrameController;
import com.br.framework.core.database.query.QueryResult;
import com.br.framework.core.dbmetadata.TableMetadata;
import com.br.framework.core.enumerator.FrameComponent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;

public class Frame {

    private QueryResult sqlResult;
    private Handlebar handlebar;
    private TableMetadata metadata;
    private final Map<FrameComponent, Object> componentMap;

    public Frame() {
        this.componentMap = new HashMap<>();
    }

    public TableMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(TableMetadata metadata) {
        this.metadata = metadata;
    }

    public QueryResult getSqlResult() {
        return sqlResult;
    }

    public void setSqlResult(QueryResult sqlResult) {
        this.sqlResult = sqlResult;
    }

    public Map<FrameComponent, Object> getComponentMap() {
        return componentMap;
    }

    public List<Map<String, Object>> getSelectedRows() {
        return FrameController.getSelectedRows(this);
    }

    public Handlebar getHandlebar() {
        return handlebar;
    }

    public void setHandlebar(Handlebar handlebar) {
        this.handlebar = handlebar;
    }

    public void show() {
        final JFrame frame = (JFrame) FrameController.getComponent(this, FrameComponent.SWING_JFRAME);
        frame.setVisible(true);
    }

    public void toGrid() {
        handlebar.toGrid();
    }

    public void toForm() {
        handlebar.toForm();
    }

    public void refresh() {
        handlebar.refresh();
    }

}
