package com.br.framework.internal.component;

import com.br.framework.internal.component.event.BoolEventCommand;
import com.br.framework.internal.component.factory.DmlQueryFactory;
import com.br.framework.Database;
import com.br.framework.internal.component.event.VoidEventCommand;
import com.br.framework.internal.logger.InternalLogger;
import com.br.framework.internal.queryResult.QueryResult;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Handlebar implements Destroyable {

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
                window.getTable().setValueGrid(field, oldValues.get(field));
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
                field.setText(String.valueOf(window.getTable().getValueGrid(iterator)));
            });
        }
    }

    private Long getNextTableSequence() throws SQLException {
        final QueryResult result = Database.query(String.format("select nextval(%s) seq from dual", window.getConfig().getSequence()));
        result.toFirstRecord();
        final Long nextSequence = result.getLong("seq");
        return nextSequence;
    }

    private void insert() {
        toForm();
        getSaveButton().setEnabled(true);
        getInsertButton().setEnabled(false);
        oldValues = new LinkedHashMap<>();
        try {
            final Long nextSequence = getNextTableSequence();
            oldValues.put(window.getConfig().getPkField(), window.getTable().getValueGrid(window.getConfig().getPkField()));
            window.getConfig().getAttributes().keySet().stream().filter(field -> (!field.equals(window.getConfig().getPkField()))).forEachOrdered(field -> {
                oldValues.put(field, window.getTable().getValueGrid(field));
                window.getTable().setValueGrid(field, "");
            });
            window.getTable().setValueGrid(window.getConfig().getPkField(), nextSequence);
            updateFormFields();
        } catch (SQLException ex) {
            InternalLogger.err(Handlebar.class, ex.getMessage());
        }
    }

    private void delete() {
        if (window.getTable().getSelectedRows().size() > 0 && runBeforeDeleteEvents()) {
            final String query = DmlQueryFactory.createDelete(window);
            final Object value = window.getTable().getValueGrid(window.getConfig().getPkField());
            try {
                Database.execute(query, value);
                window.getTable().refresh();
                toGrid();
            } catch (SQLException ex) {
                InternalLogger.err(Handlebar.class, ex.getMessage());
            }
            window.getConfig().getAfterDeleteEvents().forEach(command -> {
                command.execute();
            });
        }
    }

    private boolean runBeforeInsertEvents() {
        boolean doContinue = true;
        for (final BoolEventCommand command : window.getConfig().getBeforeInsertEvents()) {
            doContinue = doContinue && command.execute();
        }
        return doContinue;
    }

    private boolean runBeforeUpdateEvents() {
        boolean doContinue = true;
        for (final BoolEventCommand command : window.getConfig().getBeforeUpdateEvents()) {
            doContinue = doContinue && command.execute();
        }
        return doContinue;
    }

    private boolean runBeforeDeleteEvents() {
        boolean doContinue = true;
        for (final BoolEventCommand command : window.getConfig().getBeforeDeleteEvents()) {
            doContinue = doContinue && command.execute();
        }
        return doContinue;
    }

    private Object[] getInsertArguments() {
        final Object[] arguments = new Object[window.getConfig().getAttributes().size() - 1];
        int i = 0;
        for (final String field : window.getConfig().getAttributes().keySet()) {
            if (!field.equals(window.getConfig().getPkField())) {
                arguments[i] = window.getTable().getValue(field);
                i++;
            }
        }
        return arguments;
    }

    private void save() {
        getSaveButton().setEnabled(false);
        getInsertButton().setEnabled(true);
        boolean doContinue = true;
        if (state == HandlebarState.INSERT) {
            doContinue = runBeforeInsertEvents();
        } else if (state == HandlebarState.UPDATE) {
            doContinue = runBeforeUpdateEvents();
        }
        if (doContinue) {
            String query = DmlQueryFactory.createInsert(window);
            final Object[] insertArguments = getInsertArguments();
            try {
                Database.execute(query, insertArguments);
                window.getTable().refresh();
                toGrid();
                if (state == HandlebarState.INSERT) {
                    window.getConfig().getAfterInsertEvents().forEach(action -> {
                        action.execute();
                    });
                }
            } catch (SQLException ex) {
                Logger.getLogger(Handlebar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (state == HandlebarState.INSERT) {
            window.getConfig().getAfterInsertEvents().forEach(command -> {
                command.execute();
            });
        } else if (state == HandlebarState.UPDATE) {
            for (Iterator<VoidEventCommand> it = window.getConfig().getAfterUpdateEvents().iterator(); it.hasNext();) {
                VoidEventCommand command = it.next();
                command.execute();
            }
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
