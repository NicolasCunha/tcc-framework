package com.br.framework.core;

import com.br.framework.core.controller.FrameController;
import com.br.framework.core.database.query.QueryResult;
import com.br.framework.core.enumerator.FrameComponent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;

public class Frame {

    private QueryResult sqlResult;
    private final Map<FrameComponent, Object> componentMap;

    public Frame() {
        this.componentMap = new HashMap<>();
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

    public void show() {
        final JFrame frame = (JFrame) FrameController.getComponent(this, FrameComponent.SWING_JFRAME);
        frame.setVisible(true);
    }

}
