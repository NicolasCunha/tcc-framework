package com.br.framework.core.component;

import com.br.framework.core.component.interfaces.Destroyable;
import com.br.framework.core.enumerator.FrameComponent;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Handlebar implements Destroyable {

    private Frame frame;
    private JButton gridEditButton;
    private JButton insertButton;
    private JButton removeButton;
    private JButton saveButton;
    private JButton exitCloseButton;

    private Handlebar(final Frame frame) {
        this.frame = frame;
    }

    public static Handlebar newInstance(final Frame frame) {
        return new Handlebar(frame);
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

    public void setupGridFormBehavior(final JButton button) {
        button.addActionListener((final ActionEvent e) -> {
            gridForm();
        });
    }

    public void setupInsertBehavior(final JButton button) {
        button.addActionListener((final ActionEvent e) -> {
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
        final JScrollPane scrollEdit = (JScrollPane) frame.getController().getComponent(FrameComponent.SWING_JSCROLL_EDIT);
        if (scrollEdit.isVisible()) {
            toGrid();
            ((JTable) frame.getController().getComponent(FrameComponent.SWING_JTABLE)).requestFocus();
        } else {
            toForm();
        }
    }

    public void toGrid() {
        final JScrollPane scrollEdit = (JScrollPane) frame.getController().getComponent(FrameComponent.SWING_JSCROLL_EDIT);
        final JScrollPane scrollGrid = (JScrollPane) frame.getController().getComponent(FrameComponent.SWING_JSCROLL_GRID);
        scrollEdit.setVisible(false);
        scrollGrid.setVisible(true);
    }

    public void toForm() {
        final JScrollPane scrollEdit = (JScrollPane) frame.getController().getComponent(FrameComponent.SWING_JSCROLL_EDIT);
        final JScrollPane scrollGrid = (JScrollPane) frame.getController().getComponent(FrameComponent.SWING_JSCROLL_GRID);
        scrollEdit.setVisible(true);
        scrollGrid.setVisible(false);
        updateFormFields();
    }

    private void updateFormFields() {
        final JTable refTable = (JTable) frame.getController().getComponent(FrameComponent.SWING_JTABLE);
        final Map<String, Object> fields = (HashMap<String, Object>) frame.getController().getComponent(FrameComponent.MAP_EDIT_FIELDS);
        if (refTable.getSelectedRow() == -1) {
            refTable.setRowSelectionInterval(0, 0);
        }
        if (refTable.getSelectedRow() != -1) {
            fields.keySet().forEach((iterator) -> {
                final JTextField field = (JTextField) fields.get(iterator);
                field.setText(String.valueOf(frame.getTable().getValue(iterator)));
            });
        }
    }

    private void insert() {
        toForm();
        getSaveButton().setEnabled(true);
    }

    private void delete() {

    }

    private void save() {
        getSaveButton().setEnabled(false);
    }

    private void closeExit() {
        System.exit(0);
    }

    @Override
    public void destroy() {
        this.frame = null;
        this.exitCloseButton = null;
        this.gridEditButton = null;
        this.insertButton = null;
        this.removeButton = null;
        this.saveButton = null;
    }

}
