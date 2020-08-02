package com.br.framework.core.factory.crud;

import com.br.framework.core.component.Frame;
import com.br.framework.core.controller.FrameController;
import com.br.framework.core.controller.PositionCalculator;
import com.br.framework.core.database.query.QueryResult;
import com.br.framework.api.services.QueryService;
import com.br.framework.core.enumerator.FrameComponent;
import com.br.framework.core.factory.swing.TableModelFactory;
import com.br.framework.core.component.Handlebar;
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

        BUTTON_EDIT_GRID("Editar/Grid"),
        BUTTON_INSERT("Novo Registro");

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

    /**
     * Used in the components position calculations.
     */
    private int lastComponentY = 0;
    private int lastComponentX = 0;

    public void createCrud(final Frame frame) throws SQLException {
        createGrid(frame);
        createForm(frame);
    }

    public void createGrid(final Frame frame) throws SQLException {
        final JTable jtable = new JTable();
        final JScrollPane scrollPane = new JScrollPane(jtable);
        final QueryResult queryResult = QueryService.run(frame.getConfig().getSql());
        final DefaultTableModel model = modelFactory.createTableModel(queryResult);
        final Map<String, Integer> columnPosition = modelFactory.getColumnPosition();

        jtable.setModel(model);
        scrollPane.setBounds(calculator.calculateScrollPane(frame));
        scrollPane.setViewportView(jtable);
        frame.getTable().createTableListener(jtable);
        frame.getTable().setSqlResult(queryResult);

        frame.getController().addComponent(FrameComponent.SWING_JSCROLL_GRID, scrollPane);
        frame.getController().addComponent(FrameComponent.SWING_JTABLE, jtable);
        frame.getController().addComponent(FrameComponent.MAP_COLUMN_POSITION, columnPosition);
        frame.getController().swingAdd(scrollPane);
        frame.getController().swingRepaint();

    }

    private void createForm(final Frame frame) {
        final JFrame jframe = (JFrame) frame.getController().getComponent(FrameComponent.SWING_JFRAME);
        final JScrollPane scrollPane = new JScrollPane();
        final JPanel editPanel = new JPanel();
        editPanel.setLayout(null);
        scrollPane.setBounds(calculator.calculateScrollPane(frame));
        scrollPane.setVisible(false);
        scrollPane.setViewportView(editPanel);
        createButtons(frame);
        frame.getController().addComponent(FrameComponent.SWING_EDIT_PANEL, editPanel);
        frame.getController().addComponent(FrameComponent.SWING_JSCROLL_EDIT, scrollPane);
        frame.getController().swingAdd(scrollPane);
        final Map<String, Object> fields = (HashMap<String, Object>) frame.getController().getComponent(FrameComponent.MAP_EDIT_FIELDS);
        frame.getTable().getSqlResult().getColumns().stream().forEachOrdered((field) -> {
            final JLabel label = createJLabel(frame, field);
            final JTextField swingField = new JTextField();
            swingField.setBounds(calculator.calculateFieldBounds(lastComponentX, lastComponentY, label, field));
            swingField.setText(String.valueOf(frame.getTable().getValue(field)));
            fields.put(field, swingField);
            frame.getController().addComponent(FrameComponent.MAP_EDIT_FIELDS, fields);
            editPanel.add(label);
            editPanel.add(swingField);
        });
        jframe.repaint();
    }

    private void createButtons(final Frame frame) {
        final Handlebar handlebar = frame.getHandlebar();
        JButton button = new JButton(ButtonText.BUTTON_EDIT_GRID.desc());
        button.setBounds(calculator.calculateGridViewButton(frame));
        handlebar.setupGridFormBehavior(button);
        handlebar.setGridEditButton(button);
        frame.getController().swingAdd(button);
        button = new JButton(ButtonText.BUTTON_INSERT.desc());
        button.setBounds(calculator.calculateGridViewButton(frame));
        frame.getController().swingAdd(button);
    }

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
        return frame.getTable().getSqlResult().getAliasToRow().get(field).concat(":");
    }

}
