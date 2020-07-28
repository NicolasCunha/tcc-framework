package com.br.framework.core.factory.crud;

import com.br.framework.core.Frame;
import com.br.framework.core.controller.FrameController;
import com.br.framework.core.controller.PositionCalculator;
import com.br.framework.core.database.query.QueryResult;
import com.br.framework.core.database.query.QueryService;
import com.br.framework.core.enumerator.FrameComponent;
import com.br.framework.core.factory.swing.TableModelFactory;
import com.br.framework.core.listener.ButtonListener;
import com.br.framework.core.listener.TableListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CrudFactory {

    private enum ButtonText {

        BUTTON_EDIT_GRID("Editar/Grid");

        private final String desc;

        private ButtonText(final String str) {
            this.desc = str;
        }

        public String desc() {
            return this.desc;
        }

    }

    private final TableModelFactory modelFactory = new TableModelFactory();
    private final PositionCalculator calculator = new PositionCalculator();

    public void createCrud(final Frame frame) throws SQLException {
        createGrid(frame);
        createForm(frame);
    }

    public void createGrid(final Frame frame) throws SQLException {
        final JTable jtable = new JTable();
        final JScrollPane scrollPane = new JScrollPane(jtable);
        final TableListener tableListener = new TableListener(jtable, frame);
        final QueryResult queryResult = QueryService.run(String.valueOf(FrameController.getComponent(frame, FrameComponent.FRAME_QUERY)));
        final DefaultTableModel model = modelFactory.createTableModel(queryResult);
        final Map<String, Integer> columnPosition = modelFactory.getColumnPosition();

        jtable.setModel(model);
        scrollPane.setBounds(calculator.calculateScrollPane(frame));
        scrollPane.setViewportView(jtable);
        tableListener.createListeners();

        FrameController.addComponent(frame, FrameComponent.SWING_JSCROLL_GRID, scrollPane);
        FrameController.addComponent(frame, FrameComponent.SWING_JTABLE, jtable);
        FrameController.addComponent(frame, FrameComponent.MAP_COLUMN_POSITION, columnPosition);
        FrameController.addComponent(frame, FrameComponent.TABLE_LISTENER, tableListener);
        frame.setSqlResult(queryResult);

        FrameController.swingAdd(frame, scrollPane);
        FrameController.swingRepaint(frame);

    }

    private void createForm(final Frame frame) {
        final JFrame jframe = (JFrame) FrameController.getComponent(frame, FrameComponent.SWING_JFRAME);
        final ButtonListener buttonController = (ButtonListener) FrameController.getComponent(frame, FrameComponent.EDIT_GRID_LISTENER);
        final JScrollPane scrollPane = new JScrollPane();
        final JPanel editPanel = new JPanel();
        final JButton button = new JButton(ButtonText.BUTTON_EDIT_GRID.desc());
        button.setBounds(calculator.calculateGridViewButton(frame));
        editPanel.setLayout(null);
        scrollPane.setBounds(calculator.calculateScrollPane(frame));
        scrollPane.setVisible(false);
        scrollPane.setViewportView(editPanel);
        
        FrameController.addComponent(frame, FrameComponent.SWING_EDIT_PANEL, editPanel);
        FrameController.addComponent(frame, FrameComponent.SWING_JSCROLL_EDIT, scrollPane);
        FrameController.swingAdd(frame, scrollPane);
        frame.getSqlResult().getColumns().stream().forEachOrdered((field) -> {            
            final Map<String, Object> fields = (HashMap<String, Object>) FrameController.getComponent(frame, FrameComponent.MAP_EDIT_FIELDS);
            final JLabel label = createJLabel(frame, field);
            final JTextField swingField = new JTextField();

            swingField.setBounds(calculator.calculateFieldBounds(lastComponentX, lastComponentY, label, field));
            swingField.setText(String.valueOf(FrameController.getValue(frame, field)));
            fields.put(field, swingField);
            FrameController.addComponent(frame, FrameComponent.MAP_EDIT_FIELDS, fields);
            editPanel.add(label);
            editPanel.add(swingField);
        });

        buttonController.addGridViewButtonController(button, frame);
        FrameController.addComponent(frame, FrameComponent.BUTTON_GRID_VIEW, button);
        FrameController.swingAdd(frame, button);

        jframe.repaint();
    }

    private int lastComponentY = 0;
    private int lastComponentX = 0;

    private JLabel createJLabel(final Frame frame, final String name) {
        if (lastComponentX == 0) {
            lastComponentX = calculator.calculateLastXPosition(frame);
        }
        if (lastComponentY == 0) {
            lastComponentY = calculator.calculateLastXPosition(frame);
        } else {
            lastComponentY += calculator.increaseYPosition();
        }
        final JLabel label = new JLabel();
        label.setText(getLabelText(frame, name));
        label.setBounds(calculator.calculateLabelBounds(lastComponentX, lastComponentY, label));
        return label;
    }

    private String getLabelText(final Frame frame, final String field) {
        return frame.getSqlResult().getAliasToRow().get(field).concat(":");
    }

}
