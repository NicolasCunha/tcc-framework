package com.br.framework.internal.component.factory;

import com.br.framework.internal.component.WindowConfiguration;
import com.br.framework.internal.component.Window;
import com.br.framework.internal.infra.QueryResult;
import java.awt.Rectangle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

public final class SwingComponentFactory {

    private static Map<String, Integer> columnPosition;

    public static DefaultTableModel createTableModel(final Window frame, final QueryResult result) {
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

    public static Map<String, Integer> getColumnPosition() {
        Map<String, Integer> ret = columnPosition;
        columnPosition = new LinkedHashMap<>();
        return ret;
    }

    private static void createColumns(final DefaultTableModel model, final List<String> columns, final List<Map<String, Object>> rows) {
        int counter = 0;
        columnPosition = new LinkedHashMap<>();
        for (String iterator : rows.get(0).keySet()) {
            columnPosition.put(columns.get(counter), counter);
            model.addColumn(iterator);
            counter++;
        }
    }

    private static void createRows(final DefaultTableModel model, final List<Map<String, Object>> rows) {
        rows.forEach((iterator) -> {
            model.addRow(iterator.values().toArray());
        });
    }

    private static void checkBuildAliases(final Window frame) {
        columnPosition.keySet().forEach(key -> {
            if (!frame.getController().isAttributeHasAlias(key)) {
                frame.getController().addAttributeAlias(key, key);
            }
        });
    }

    public static JFrame createJFrameFromConfig(final WindowConfiguration config) {
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

    private static void setDimension(final JFrame frame, final int width, final int height) {
        frame.setBounds(0, 0, width, height);
    }

    private static void setCentered(final JFrame frame, final boolean resizable) {
        if (resizable) {
            frame.setLocationRelativeTo(null);
        }
    }

    public static JButton createJButton(final String content, final Rectangle bounds) {
        final JButton button = new JButton();
        button.setText(content);
        button.setBounds(bounds);
        return button;
    }

}
