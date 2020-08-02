package com.br.framework.core.component;

import com.br.framework.core.controller.FrameController;
import com.br.framework.core.enumerator.FrameComponent;
import com.br.framework.core.factory.swing.TableModelFactory;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Handlebar {

    private final Frame frame;
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

    public void addGridViewButtonController(final JButton button) {
        button.addActionListener((final ActionEvent e) -> {
            defaultGridViewBehavior();
        });
    }

    public void defaultGridViewBehavior() {
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
        updateEditFields();
    }

    private void updateEditFields() {
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

}
