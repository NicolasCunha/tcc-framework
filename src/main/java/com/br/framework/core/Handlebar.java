package com.br.framework.core;

import com.br.framework.core.controller.FrameController;
import com.br.framework.core.database.query.QueryResult;
import com.br.framework.core.database.query.QueryService;
import com.br.framework.core.enumerator.FrameComponent;
import com.br.framework.core.factory.swing.TableModelFactory;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

public class Handlebar {

    private final Frame frame;
    private JTable table;
    private final TableModelFactory modelFactory;

    private Handlebar(final Frame frame) {        
        this.frame = frame;
        this.modelFactory = new TableModelFactory();
    }

    public static Handlebar newInstance(final Frame frame) {
        return new Handlebar(frame);
    }

    public void addGridViewButtonController(final JButton button) {
        button.addActionListener((final ActionEvent e) -> {
            defaultGridViewBehavior();
        });
    }

    public void defaultGridViewBehavior() {
        final JScrollPane scrollEdit = (JScrollPane) FrameController.getComponent(frame, FrameComponent.SWING_JSCROLL_EDIT);
        if (scrollEdit.isVisible()) {
            toGrid();
            ((JTable) FrameController.getComponent(frame, FrameComponent.SWING_JTABLE)).requestFocus();
        } else {
            toForm();
        }
    }

    public void toGrid() {
        final JScrollPane scrollEdit = (JScrollPane) FrameController.getComponent(frame, FrameComponent.SWING_JSCROLL_EDIT);
        final JScrollPane scrollGrid = (JScrollPane) FrameController.getComponent(frame, FrameComponent.SWING_JSCROLL_GRID);
        scrollEdit.setVisible(false);
        scrollGrid.setVisible(true);
    }

    public void toForm() {
        final JScrollPane scrollEdit = (JScrollPane) FrameController.getComponent(frame, FrameComponent.SWING_JSCROLL_EDIT);
        final JScrollPane scrollGrid = (JScrollPane) FrameController.getComponent(frame, FrameComponent.SWING_JSCROLL_GRID);
        scrollEdit.setVisible(true);
        scrollGrid.setVisible(false);
        updateEditFields();
    }

    private void updateEditFields() {
        final JTable refTable = (JTable) FrameController.getComponent(frame, FrameComponent.SWING_JTABLE);
        final Map<String, Object> fields = (HashMap<String, Object>) FrameController.getComponent(frame, FrameComponent.MAP_EDIT_FIELDS);
        if (refTable.getSelectedRow() == -1) {
            refTable.setRowSelectionInterval(0, 0);
        }
        if (refTable.getSelectedRow() != -1) {
            fields.keySet().forEach((iterator) -> {
                final JTextField field = (JTextField) fields.get(iterator);
                field.setText(String.valueOf(FrameController.getValue(frame, iterator)));
            });
        }
    }

    public void createTableListener(final JTable table) {
        this.table = table;
        createKeyboardListener();
        createEnableDisableGrid();
    }

    private void createKeyboardListener() {
        if (table != null) {
            table.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    // Not necessary.
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (table.getSelectedRow() > -1) {
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_F5:
                                refresh();
                                break;
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
            final JTable jtable = (JTable) FrameController.getComponent(frame, FrameComponent.SWING_JTABLE);
            final QueryResult queryResult = QueryService.run(String.valueOf(FrameController.getComponent(frame, FrameComponent.FRAME_QUERY)));
            final DefaultTableModel model = modelFactory.createTableModel(queryResult);
            jtable.setModel(model);
            FrameController.swingRepaint(frame);
        } catch (SQLException ex) {
            Logger.getLogger(Handlebar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createEnableDisableGrid() {
        if (table != null) {
            table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
                if (table.getSelectedRowCount() > 1) {
                    ((JButton) FrameController.getComponent(frame, FrameComponent.BUTTON_GRID_FORM)).setEnabled(false);
                } else {
                    ((JButton) FrameController.getComponent(frame, FrameComponent.BUTTON_GRID_FORM)).setEnabled(true);
                }
            });
        }
    }

}
