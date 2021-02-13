package com.br.framework.internal.component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.br.framework.internal.component.event.BoolEventCommand;
import com.br.framework.internal.component.event.VoidEventCommand;
import java.util.ArrayList;

public final class WindowConfiguration {

    private String title;
    private int width, height;
    private boolean centered;
    private String table;
    private Map<String, String> attributes;
    private String sequence;
    private String sql;
    private String pkField;
    private List<BoolEventCommand> beforeInsertEvents;
    private List<VoidEventCommand> afterInsertEvents;

    private WindowConfiguration() {
        attributes = new LinkedHashMap<>();
        beforeInsertEvents = new ArrayList<>();
        afterInsertEvents = new ArrayList<>();
    }

    public static WindowConfiguration newInstance() {
        return new WindowConfiguration();
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

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

    public String getPkField() {
        return pkField;
    }

    public void setPkField(String pkField) {
        this.pkField = pkField;
    }

    public List<BoolEventCommand> getBeforeInsertEvents() {
        return beforeInsertEvents;
    }

    public void setBeforeInsertEvents(List<BoolEventCommand> beforeInsertEvents) {
        this.beforeInsertEvents = beforeInsertEvents;
    }

    public List<VoidEventCommand> getAfterInsertEvents() {
        return afterInsertEvents;
    }

    public void setAfterInsertEvents(List<VoidEventCommand> afterInsertEvents) {
        this.afterInsertEvents = afterInsertEvents;
    }

}
