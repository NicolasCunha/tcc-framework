package com.br.framework.internal.component.factory;

import com.br.framework.internal.component.factory.componentPosition.PositionFactory;
import com.br.framework.internal.component.Window;
import com.br.framework.internal.infra.QueryResult;
import com.br.framework.internal.component.WindowComponentEnum;
import com.br.framework.FrameworkDatabase;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class CrudFactory {

    private static final PositionFactory calculator = PositionFactory.getInstance();

    /**
     * Used in the components position calculations.
     */
    private static int compY = 0;
    private static int compX = 0;

    public static void createCrud(final Window frame) throws SQLException {
        createGrid(frame);
        createForm(frame);
        HandlebarFactory.build(frame);
    }

    public static void createGrid(final Window frame) throws SQLException {
        final JTable jtable = new JTable();
        final JScrollPane scrollPane = new JScrollPane(jtable);
        final QueryResult queryResult = FrameworkDatabase.query(frame.getConfig().getSql());
        final DefaultTableModel model = SwingComponentFactory.createTableModel(frame, queryResult);
        final Map<String, Integer> columnPosition = SwingComponentFactory.getColumnPosition();

        jtable.setModel(model);
        scrollPane.setBounds(calculator.calculateScrollPane(frame));
        scrollPane.setViewportView(jtable);
        jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        frame.getTable().createTableListener(jtable);
        frame.getTable().setSqlResult(queryResult);

        frame.getController().addComponent(WindowComponentEnum.SWING_JSCROLL_GRID, scrollPane);
        frame.getController().addComponent(WindowComponentEnum.SWING_JTABLE, jtable);
        frame.getController().addComponent(WindowComponentEnum.MAP_COLUMN_POSITION, columnPosition);
        frame.getController().addComponent(scrollPane);
        frame.getController().repaint();

    }

    private static void createForm(final Window frame) {
        final JFrame jframe = (JFrame) frame.getController().getComponent(WindowComponentEnum.SWING_JFRAME);
        final JScrollPane scrollPane = new JScrollPane();
        final JPanel editPanel = new JPanel();
        editPanel.setLayout(null);
        scrollPane.setBounds(calculator.calculateScrollPane(frame));
        scrollPane.setVisible(false);
        scrollPane.setViewportView(editPanel);
        frame.getController().addComponent(WindowComponentEnum.SWING_EDIT_PANEL, editPanel);
        frame.getController().addComponent(WindowComponentEnum.SWING_JSCROLL_EDIT, scrollPane);
        frame.getController().addComponent(scrollPane);
        createLabelsAndFields(frame, editPanel);
        jframe.repaint();
    }

    private static void createLabelsAndFields(final Window frame, final JPanel editPanel) {
        final Map<String, Object> fields = (HashMap<String, Object>) frame.getController().getComponent(WindowComponentEnum.MAP_EDIT_FIELDS);
        frame.getTable().getSqlResult().getColumns().stream().forEachOrdered((field) -> {
            final JLabel label = createJLabel(frame, field);
            final JTextField swingField = new JTextField();
            swingField.setBounds(calculator.calculateFieldBounds(compX, compY, label, field));
            swingField.setText(String.valueOf(frame.getTable().getValueGrid(field)));
            fields.put(field, swingField);
            frame.getController().addComponent(WindowComponentEnum.MAP_EDIT_FIELDS, fields);
            editPanel.add(label);
            editPanel.add(swingField);
        });
    }

    private static JLabel createJLabel(final Window frame, final String name) {
        if (compX == 0) {
            compX = calculator.calculateLastXPosition(frame);
        }
        if (compY == 0) {
            compY = calculator.calculateLastXPosition(frame);
        } else {
            compY += calculator.increaseYPosition();
        }
        final JLabel label = new JLabel();
        label.setText(getLabelText(frame, name));
        label.setBounds(calculator.calculateLabelBounds(compX, compY, label));
        return label;
    }

    private static String getLabelText(final Window frame, final String field) {
        return frame.getTable().getSqlResult().getAliasToRow().get(field).concat(":");
    }

}
