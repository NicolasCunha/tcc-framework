package com.br.framework.core.factory.component;

import com.br.framework.core.component.Frame;
import com.br.framework.core.controller.PositionCalculator;
import com.br.framework.core.database.query.QueryResult;
import com.br.framework.api.services.QueryService;
import com.br.framework.core.enumerator.FrameComponent;
import com.br.framework.core.factory.swing.TableModelFactory;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CrudFactory {

    private final TableModelFactory modelFactory;
    private final PositionCalculator calculator = PositionCalculator.getInstance();
    private final HandlebarFactory handlebarFactory = HandlebarFactory.getInstance();

    /**
     * Used in the components position calculations.
     */
    private int lastComponentY = 0;
    private int lastComponentX = 0;

    private final Frame frame;

    private CrudFactory(final Frame frame) {
        this.frame = frame;
        this.modelFactory = TableModelFactory.getInstance(frame);
    }
    
    public static CrudFactory getInstance(final Frame frame){
        return new CrudFactory(frame);
    }

    public void createCrud() throws SQLException {
        createGrid();
        createForm();
        handlebarFactory.build(frame);
    }

    public void createGrid() throws SQLException {
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

    private void createForm() {
        final JFrame jframe = (JFrame) frame.getController().getComponent(FrameComponent.SWING_JFRAME);
        final JScrollPane scrollPane = new JScrollPane();
        final JPanel editPanel = new JPanel();
        editPanel.setLayout(null);
        scrollPane.setBounds(calculator.calculateScrollPane(frame));
        scrollPane.setVisible(false);
        scrollPane.setViewportView(editPanel);
        frame.getController().addComponent(FrameComponent.SWING_EDIT_PANEL, editPanel);
        frame.getController().addComponent(FrameComponent.SWING_JSCROLL_EDIT, scrollPane);
        frame.getController().swingAdd(scrollPane);
        final Map<String, Object> fields = (HashMap<String, Object>) frame.getController().getComponent(FrameComponent.MAP_EDIT_FIELDS);
        frame.getTable().getSqlResult().getColumns().stream().forEachOrdered((field) -> {
            final JLabel label = createJLabel(field);
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

    private JLabel createJLabel(final String name) {
        if (lastComponentX == 0) {
            lastComponentX = calculator.calculateLastXPosition(frame);
        }
        if (lastComponentY == 0) {
            lastComponentY = calculator.calculateLastXPosition(frame);
        } else {
            lastComponentY += calculator.increaseYPosition();
        }
        final JLabel label = new JLabel();
        label.setText(getLabelText(name));
        label.setBounds(calculator.calculateLabelBounds(lastComponentX, lastComponentY, label));
        return label;
    }

    private String getLabelText(final String field) {
        return frame.getTable().getSqlResult().getAliasToRow().get(field).concat(":");
    }

}
