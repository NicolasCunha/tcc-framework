package com.br.framework.internal.component;

import com.br.framework.Framework;
import com.br.framework.internal.database.QueryResult;
import com.br.framework.internal.component.factory.SwingComponentFactory;
import com.br.framework.internal.database.Database;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

public class Table implements Destroyable {

    private Window frame;
    private QueryResult sqlResult;
    private JTable jtable;

    private Table(final Window frame) {
        this.frame = frame;
    }

    public QueryResult getSqlResult() {
        return sqlResult;
    }

    public void setSqlResult(QueryResult sqlResult) {
        this.sqlResult = sqlResult;
    }

    public static Table getInstance(final Window frame) {
        return new Table(frame);
    }

    public void createTableListener(final JTable table) {
        this.jtable = table;
        createKeyboardListener();
        createEnableDisableGrid();
    }

    private void createKeyboardListener() {
        if (jtable != null) {
            jtable.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    // Not necessary.
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (jtable.getSelectedRow() > -1) {
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_F5 ->
                                refresh();
                        }
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    // Not necessary.
                }

            });
        }
    }

    public void refresh() {
        try {
            final JTable innerTable = (JTable) frame.getController().getComponent(WindowComponentEnum.SWING_JTABLE);
            final QueryResult queryResult = Database.query(frame.getConfig().getSql());
            final DefaultTableModel model = SwingComponentFactory.getInstance().createTableModel(frame, queryResult);
            innerTable.setModel(model);
            frame.getController().swingRepaint();
        } catch (SQLException ex) {
            Logger.getLogger(Handlebar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createEnableDisableGrid() {
        if (jtable != null) {
            jtable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
                if (jtable.getSelectedRowCount() > 1) {
                    frame.getHandlebar().getGridEditButton().setEnabled(false);
                } else {
                    frame.getHandlebar().getGridEditButton().setEnabled(true);
                }
            });
        }
    }

    public List<Map<String, Object>> getSelectedRows() {
        final List<Map<String, Object>> rows = new ArrayList<>();
        if (jtable.getSelectedRows().length > 0) {
            final int[] selectedRows = jtable.getSelectedRows();
            final Map<String, Object> fields = (HashMap<String, Object>) frame.getController().getComponent(WindowComponentEnum.MAP_EDIT_FIELDS);
            final Map<String, Object> row = new HashMap<>();
            for (final int selectedRow : selectedRows) {
                row.clear();
                fields.keySet().forEach((field) -> {
                    final String alias = (String) ((Map<String, Object>) frame.getComponentMap().get(WindowComponentEnum.MAP_ATRIB_TO_ALIAS)).get(field);
                    row.put(field, jtable.getValueAt(selectedRow, jtable.convertColumnIndexToView(jtable.getColumn(alias).getModelIndex())));
                });
                rows.add(row);
            }
        }

        return rows;
    }

    public Object getValueGrid(final String name) {
        final int row = jtable.getSelectedRow();
        if (row > -1) {
            final String alias = (String) ((Map<String, Object>) frame.getComponentMap().get(WindowComponentEnum.MAP_ATRIB_TO_ALIAS)).get(name);
            return jtable.getValueAt(row, jtable.convertColumnIndexToView(jtable.getColumn(alias).getModelIndex()));
        } else {
            return null;
        }
    }

    public Object getValue(final String name) {
        return ((Map<String, JTextField>) frame.getComponentMap().get(WindowComponentEnum.MAP_EDIT_FIELDS)).get(name).getText();
    }

    public void setValueGrid(final String name, final Object value) {
        final int row = jtable.getSelectedRow();
        if (row > -1) {
            final String alias = (String) ((Map<String, Object>) frame.getComponentMap().get(WindowComponentEnum.MAP_ATRIB_TO_ALIAS)).get(name);
            jtable.setValueAt(value, row, jtable.convertColumnIndexToView(jtable.getColumn(alias).getModelIndex()));
        }
    }

    @Override
    public void destroy() {
        this.sqlResult.destroy();
        this.jtable = null;
        this.frame = null;
    }

}
