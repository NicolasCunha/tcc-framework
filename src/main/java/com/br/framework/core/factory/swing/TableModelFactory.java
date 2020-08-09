package com.br.framework.core.factory.swing;

import com.br.framework.core.component.Frame;
import com.br.framework.core.database.query.QueryResult;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

public class TableModelFactory {

    private static Map<String, Integer> columnPosition;

    private static TableModelFactory tableModelFactory;

    private TableModelFactory() {

    }

    public static TableModelFactory getInstance() {
        if (tableModelFactory == null) {
            tableModelFactory = new TableModelFactory();
        }
        return tableModelFactory;
    }

    public DefaultTableModel createTableModel(final Frame frame, final QueryResult result) {
        final DefaultTableModel model = new DefaultTableModel();
        if (!result.isEmpty()) {
            final List<Map<String, Object>> rows = result.getRows();
            final List<String> columns = result.getColumns();
            createColumns(model, columns, rows);
            createRows(model, rows);
        }
        checkBuildAliases(frame);
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

    private void checkBuildAliases(final Frame frame) {
        columnPosition.keySet().forEach(key -> {
            if (!frame.getController().attribHasAlias(key)) {
                frame.getController().addAttribAlias(key, key);
            }
        });
    }

}
