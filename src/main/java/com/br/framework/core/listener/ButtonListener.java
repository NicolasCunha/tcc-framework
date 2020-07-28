package com.br.framework.core.listener;

import com.br.framework.core.Frame;
import com.br.framework.core.controller.FrameController;
import com.br.framework.core.enumerator.FrameComponent;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ButtonListener {

    private ButtonListener() {

    }

    public static ButtonListener newInstance() {
        return new ButtonListener();
    }

    public void addGridViewButtonController(final JButton button, final Frame frame) {
        button.addActionListener((final ActionEvent e) -> {
            defaultGridViewBehavior(frame);
        });
    }

    public void defaultGridViewBehavior(final Frame frame) {
        final JScrollPane scrollEdit = (JScrollPane) FrameController.getComponent(frame, FrameComponent.SWING_JSCROLL_EDIT);
        final JScrollPane scrollGrid = (JScrollPane) FrameController.getComponent(frame, FrameComponent.SWING_JSCROLL_GRID);
        if (scrollEdit.isVisible()) {
            scrollEdit.setVisible(false);
            scrollGrid.setVisible(true);
            ((JTable) FrameController.getComponent(frame, FrameComponent.SWING_JTABLE)).requestFocus();
        } else {            
            scrollEdit.setVisible(true);
            scrollGrid.setVisible(false);
            updateEditFields(frame);
        }
    }

    private void updateEditFields(final Frame frame) {
        FrameController.getSelectedRows(frame);
        final JTable table = (JTable) FrameController.getComponent(frame, FrameComponent.SWING_JTABLE);
        final Map<String, Object> fields = (HashMap<String, Object>) FrameController.getComponent(frame, FrameComponent.MAP_EDIT_FIELDS);        
        if(table.getSelectedRow() == -1){
            table.setRowSelectionInterval(0, 0);
        }
        if (table.getSelectedRow() != -1) {
            fields.keySet().forEach((iterator) -> {
                final JTextField field = (JTextField) fields.get(iterator);
                field.setText(String.valueOf(FrameController.getValue(frame, iterator)));
            });
        }
    }

}
