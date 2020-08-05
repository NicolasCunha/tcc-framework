package com.br.framework.core.factory.swing;

import com.br.framework.core.database.query.QueryResult;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

public class TableModelFactory {

    private static Map<String, Integer> columnPosition;

    private TableModelFactory() {

    }

    public static TableModelFactory newInstance() {
        return new TableModelFactory();
    }

    public DefaultTableModel createTableModel(final QueryResult result) {
        final DefaultTableModel model = new DefaultTableModel();
        if (!result.isEmpty()) {
            final List<Map<String, Object>> rows = result.getRows();
            final List<String> columns = result.getColumns();
            createColumns(model, columns, rows);
            createRows(model, rows);
        }
        return model;
    }

    public Map<String, Integer> getColumnPosition() {
        return columnPosition;
    }

    private void createColumns(final DefaultTableModel model, final List<String> columns, final List<Map<String, Object>> rows) {
        int counter = 0;
        columnPosition = new LinkedHashMap<>();
        for (String iterator : rows.get(0).keySet()) {
            columnPosition.put(columns.get(counter), counter);
            model.addColumn(iterator);
            counter++;
        }
    }

    private void createRows(final DefaultTableModel model, final List<Map<String, Object>> rows) {
        rows.forEach((iterator) -> {
            model.addRow(iterator.values().toArray());
        });
    }
}
