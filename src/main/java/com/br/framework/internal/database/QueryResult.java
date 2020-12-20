package com.br.framework.internal.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.br.framework.internal.component.IDestroyable;

public class QueryResult implements IDestroyable {

    private List<Map<String, Object>> rows;
    private List<String> columns;
    private Integer counter;
    private Map<String, String> aliasToRow;
    private String table;

    public QueryResult() {
        counter = -1;
        columns = new ArrayList<>();
        aliasToRow = new HashMap<>();
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    public Map<String, String> getAliasToRow() {
        return aliasToRow;
    }

    public void setAliasToRow(Map<String, String> aliasToRow) {
        this.aliasToRow = aliasToRow;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getColumns() {
        return columns;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public void toBeforeFirstRecord() {
        this.counter = -1;
    }

    public void toFirstRecord() {
        this.counter = 0;
    }

    public void toLastRecord() {
        this.counter = rows.size() - 1;
    }

    public int getRowCount() {
        return rows.size();
    }

    public boolean isEmpty() {
        return rows.isEmpty();
    }

    public Map<String, Object> getCurrentRow() {
        if (isEmpty()) {
            return null;
        }
        return rows.get(counter);
    }

    public Map<String, Object> getFirstRow() {
        if (isEmpty()) {
            return null;
        }
        return rows.get(0);
    }

    public Map<String, Object> getLastRow() {
        if (isEmpty()) {
            return null;
        }
        return rows.get(rows.size() - 1);
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public boolean next() {
        if (counter < rows.size() - 1) {
            counter++;
            return true;
        }
        return false;
    }

    public Integer getInt(final String column) {
        return (Integer) rows.get(counter).get(column);
    }

    public Float getFloat(final String column) {
        return (Float) rows.get(counter).get(column);
    }

    public Double getDouble(final String column) {
        return (Double) rows.get(counter).get(column);
    }

    public Long getLong(final String column) {
        return (Long) rows.get(counter).get(column);
    }

    public String getString(final String column) {
        return String.valueOf(rows.get(counter).get(column));
    }

    public Boolean getBool(final String column) {
        return (Boolean) rows.get(counter).get(column);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Result size: ".concat(String.valueOf(getRowCount())));

        if (isEmpty()) {
            return builder.toString();
        } else {
            appendColumnNames(builder);
            appendColumnValues(builder);
            return builder.toString();
        }
    }

    private void appendColumnNames(final StringBuilder builder) {
        builder.append(System.getProperty("line.separator"));
        toFirstRecord();
        getCurrentRow().keySet().forEach((key) -> {
            builder.append("|".concat(key).concat("|"));
        });
        builder.append(System.getProperty("line.separator"));
    }

    private void appendColumnValues(final StringBuilder builder) {
        toBeforeFirstRecord();
        while (next()) {
            getCurrentRow().keySet().forEach((key) -> {
                builder.append("|".concat(getString(key)).concat("|"));
            });
            builder.append(System.getProperty("line.separator"));
        }
    }

    @Override
    public void destroy() {
        this.aliasToRow = null;
        this.columns = null;
        this.counter = null;
        this.rows = null;
        this.table = null;
    }

}
