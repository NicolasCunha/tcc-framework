package com.br.framework.internal.component;

import com.br.framework.Framework;
import com.br.framework.internal.component.factory.DmlQueryFactory;
import com.br.framework.internal.database.QueryResult;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Handlebar implements IDestroyable {

    private Window window;
    private JButton gridEditButton;
    private JButton insertButton;
    private JButton removeButton;
    private JButton saveButton;
    private JButton exitCloseButton;
    private Map<String, Object> oldValues;
    private HandlebarState state;

    enum HandlebarState {
        INSERT,
        UPDATE,
        GRID,
        FORM
    }

    private Handlebar(final Window window) {
        this.window = window;
    }

    public static Handlebar withWindow(final Window window) {
        return new Handlebar(window);
    }

    public void setupGridFormBehavior(final JButton button) {
        button.addActionListener((final ActionEvent e) -> {
            gridForm();
        });
    }

    public void setupInsertBehavior(final JButton button) {
        button.addActionListener((final ActionEvent e) -> {
            this.state = HandlebarState.INSERT;
            insert();
        });
    }

    public void setupRemoveBehavior(final JButton button) {
        button.addActionListener((final ActionEvent e) -> {
            delete();
        });
    }

    public void setupSaveBehavior(final JButton button) {
        button.addActionListener((final ActionEvent e) -> {
            save();
        });
    }

    public void setupCloseExitBehavior(final JButton button) {
        button.addActionListener((final ActionEvent e) -> {
            closeExit();
        });
    }

    private void gridForm() {
        final JScrollPane scrollEdit = (JScrollPane) window.getController().getComponent(WindowComponentEnum.SWING_JSCROLL_EDIT);
        if (this.state == HandlebarState.INSERT) {
            oldValues.keySet().forEach(field -> {
                window.getTable().setValue(field, oldValues.get(field));
            });
            oldValues.clear();
        }
        if (scrollEdit.isVisible()) {
            this.state = HandlebarState.GRID;
            getInsertButton().setEnabled(true);
            getSaveButton().setEnabled(false);
            toGrid();
            ((JTable) window.getController().getComponent(WindowComponentEnum.SWING_JTABLE)).requestFocus();
        } else {
            this.state = HandlebarState.FORM;
            toForm();
        }
    }

    public void toGrid() {
        final JScrollPane scrollEdit = (JScrollPane) window.getController().getComponent(WindowComponentEnum.SWING_JSCROLL_EDIT);
        final JScrollPane scrollGrid = (JScrollPane) window.getController().getComponent(WindowComponentEnum.SWING_JSCROLL_GRID);
        scrollEdit.setVisible(false);
        scrollGrid.setVisible(true);
    }

    public void toForm() {
        final JScrollPane scrollEdit = (JScrollPane) window.getController().getComponent(WindowComponentEnum.SWING_JSCROLL_EDIT);
        final JScrollPane scrollGrid = (JScrollPane) window.getController().getComponent(WindowComponentEnum.SWING_JSCROLL_GRID);
        scrollEdit.setVisible(true);
        scrollGrid.setVisible(false);
        updateFormFields();
    }

    public boolean isGrid() {
        final JScrollPane scrollGrid = (JScrollPane) window.getController().getComponent(WindowComponentEnum.SWING_JSCROLL_GRID);
        return scrollGrid.isVisible();
    }

    public boolean isForm() {
        return !isGrid();
    }

    private void updateFormFields() {
        final JTable refTable = (JTable) window.getController().getComponent(WindowComponentEnum.SWING_JTABLE);
        final Map<String, Object> fields = (HashMap<String, Object>) window.getController().getComponent(WindowComponentEnum.MAP_EDIT_FIELDS);
        if (refTable.getSelectedRow() == -1) {
            refTable.setRowSelectionInterval(0, 0);
        }
        if (refTable.getSelectedRow() != -1) {
            fields.keySet().forEach((iterator) -> {
                final JTextField field = (JTextField) fields.get(iterator);
                field.setText(String.valueOf(window.getTable().getValue(iterator)));
            });
        }
    }

    private void insert() {
        toForm();
        getSaveButton().setEnabled(true);
        getInsertButton().setEnabled(false);
        oldValues = new LinkedHashMap<>();
        try {
            final QueryResult result = Framework.getInstance().query(String.format("select nextval(%s) seq from dual", window.getConfig().getSequence()));
            result.toFirstRecord();
            final Long nextSequence = result.getLong("seq");
            oldValues.put(window.getConfig().getPkField(), window.getTable().getValue(window.getConfig().getPkField()));
            window.getConfig().getAttributes().keySet().stream().filter(field -> (!field.equals(window.getConfig().getPkField()))).forEachOrdered(field -> {
                oldValues.put(field, window.getTable().getValue(field));
                window.getTable().setValue(field, "");
            });
            window.getTable().setValue(window.getConfig().getPkField(), nextSequence);
            updateFormFields();
        } catch (SQLException ex) {
            Logger.getLogger(Handlebar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void delete() {
        if (window.getTable().getSelectedRows().size() > 0) {
            final String query = DmlQueryFactory.getInstance().createDelete(window);
            final Object value = window.getTable().getValue(window.getConfig().getPkField());
            try {
                Framework.getInstance().execute(query, value);
                window.getTable().refresh();
                toGrid();
            } catch (SQLException ex) {
                Logger.getLogger(Handlebar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void save() {
        getSaveButton().setEnabled(false);
        getInsertButton().setEnabled(true);
        final String query = DmlQueryFactory.getInstance().createInsert(window);
        final Object[] arguments = new Object[window.getConfig().getAttributes().size() - 1];
        int i = 0;
        for (final String field : window.getConfig().getAttributes().keySet()) {
            if (!field.equals(window.getConfig().getPkField())) {
                arguments[i] = window.getTable().getValueDetalhe(field);
                i++;
            }
        }
        try {
            Framework.getInstance().execute(query, arguments);
            window.getTable().refresh();
            toGrid();
        } catch (SQLException ex) {
            Logger.getLogger(Handlebar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeExit() {
        System.exit(0);
    }

    public JButton getGridEditButton() {
        return gridEditButton;
    }

    public void setGridEditButton(JButton gridEditButton) {
        this.gridEditButton = gridEditButton;
    }

    public JButton getInsertButton() {
        return insertButton;
    }

    public void setInsertButton(JButton insertButton) {
        this.insertButton = insertButton;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    public void setRemoveButton(JButton removeButton) {
        this.removeButton = removeButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(JButton saveButton) {
        this.saveButton = saveButton;
    }

    public JButton getExitCloseButton() {
        return exitCloseButton;
    }

    public void setExitCloseButton(JButton exitCloseButton) {
        this.exitCloseButton = exitCloseButton;
    }

    @Override
    public void destroy() {
        this.window = null;
        this.exitCloseButton = null;
        this.gridEditButton = null;
        this.insertButton = null;
        this.removeButton = null;
        this.saveButton = null;
    }

}