package com.br.framework.core.factory.swing;

import com.br.framework.api.configurator.FrameConfigurator;
import com.br.framework.core.component.Frame;
import com.br.framework.core.database.queryresult.QueryResult;
import java.awt.Rectangle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

public final class SwingComponentFactory {

    private Map<String, Integer> columnPosition;

    private static SwingComponentFactory instance;

    private SwingComponentFactory() {
        columnPosition = new LinkedHashMap<>();
    }

    public static SwingComponentFactory getInstance() {
        return new SwingComponentFactory();
    }

    public static SwingComponentFactory getInstance(final boolean newInstance) {
        if (instance == null || newInstance) {
            instance = new SwingComponentFactory();
        }
        return instance;
    }

    public DefaultTableModel createTableModel(final Frame frame, final QueryResult result) {
        final DefaultTableModel model = new DefaultTableModel();
        if (!result.isEmpty()) {
            final List<Map<String, Object>> rows = result.getRows();
            final List<String> columns = result.getColumns();
            createColumns(model, columns, rows);
            createRows(model, rows);
        }
        checkBuildAliases(frame);
        return model;
    }

    public Map<String, Integer> getColumnPosition() {
        return columnPosition;
    }

    private void createColumns(final DefaultTableModel model, final List<String> columns, final List<Map<String, Object>> rows) {
        int counter = 0;
        columnPosition = new LinkedHashMap<>();
        for (String iterator : rows.get(0).keySet()) {
            columnPosition.put(columns.get(counter), counter);
            model.addColumn(iterator);
            counter++;
        }
    }

    private void createRows(final DefaultTableModel model, final List<Map<String, Object>> rows) {
        rows.forEach((iterator) -> {
            model.addRow(iterator.values().toArray());
        });
    }

    private void checkBuildAliases(final Frame frame) {
        columnPosition.keySet().forEach(key -> {
            if (!frame.getController().attribHasAlias(key)) {
                frame.getController().addAttribAlias(key, key);
            }
        });
    }

    public JFrame createJFrameFromConfig(final FrameConfigurator.FrameConfig config) {
        final JFrame frame = new JFrame();

        setDimension(frame,
                config.getWidth(),
                config.getHeight()
        );

        setCentered(frame, config.isCentered());

        frame.setTitle(config.getTitle());
        frame.setVisible(false);
        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        return frame;
    }

    private void setDimension(final JFrame frame, final int width, final int height) {
        frame.setBounds(0, 0, width, height);
    }

    private void setCentered(final JFrame frame, final boolean resizable) {
        if (resizable) {
            frame.setLocationRelativeTo(null);
        }
    }

    public JButton createJButton(final String content, final Rectangle bounds) {
        final JButton button = new JButton();
        button.setText(content);
        button.setBounds(bounds);
        return button;
    }

}
