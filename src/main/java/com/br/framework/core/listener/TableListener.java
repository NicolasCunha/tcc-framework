package com.br.framework.core.listener;

import com.br.framework.core.Frame;
import com.br.framework.core.controller.FrameController;
import com.br.framework.core.database.query.QueryResult;
import com.br.framework.core.database.query.QueryService;
import com.br.framework.core.enumerator.FrameComponent;
import com.br.framework.core.factory.swing.TableModelFactory;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

public class TableListener {

    private final JTable table;
    private final Frame frame;
    private final TableModelFactory modelFactory = new TableModelFactory();

    public TableListener(final JTable table, final Frame frame) {
        this.table = table;
        this.frame = frame;
    }

    public void createListeners() {
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

                private void refresh() {
                    try {
                        final JTable jtable = (JTable) FrameController.getComponent(frame, FrameComponent.SWING_JTABLE);
                        final QueryResult queryResult = QueryService.run(String.valueOf(FrameController.getComponent(frame, FrameComponent.FRAME_QUERY)));
                        final DefaultTableModel model = modelFactory.createTableModel(queryResult);
                        jtable.setModel(model);
                        FrameController.swingRepaint(frame);
                    } catch (SQLException ex) {
                        Logger.getLogger(TableListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
    }

    private void createEnableDisableGrid() {
        if (table != null) {
            table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
                if (table.getSelectedRowCount() > 1) {
                    ((JButton) FrameController.getComponent(frame, FrameComponent.BUTTON_GRID_VIEW)).setEnabled(false);
                } else {
                    ((JButton) FrameController.getComponent(frame, FrameComponent.BUTTON_GRID_VIEW)).setEnabled(true);
                }
            });
        }
    }

}
