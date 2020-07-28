package com.br.framework.core.controller;

import com.br.framework.core.Frame;
import com.br.framework.core.enumerator.FrameComponent;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTable;

public abstract class FrameController {

    public static void swingRepaint(final Frame frame) {
        final JFrame jframe = (JFrame) getComponent(frame, FrameComponent.SWING_JFRAME);
        jframe.repaint();
    }

    public static void swingAdd(final Frame frame, final Component c) {
        final JFrame jframe = (JFrame) getComponent(frame, FrameComponent.SWING_JFRAME);
        jframe.add(c);
    }

    public static void addComponent(final Frame frame, final FrameComponent id, final Object val) {
        frame.getComponentMap().put(id, val);
    }

    public static Object getComponent(final Frame frame, final FrameComponent id) {
        if (id == FrameComponent.MAP_EDIT_FIELDS && frame.getComponentMap().get(id) == null) {
            return new HashMap<String, Object>();
        }
        return frame.getComponentMap().get(id);
    }

    public static void addAttribAlias(final Frame frame, final String attrib, final String alias) {
        if (frame.getComponentMap().get(FrameComponent.MAP_ATRIB_TO_ALIAS) == null) {
            frame.getComponentMap().put(FrameComponent.MAP_ATRIB_TO_ALIAS, new HashMap<String, Object>());
        }
        ((Map<String, Object>) frame.getComponentMap().get(FrameComponent.MAP_ATRIB_TO_ALIAS)).put(attrib, alias);
    }

    public static Object getValue(final Frame frame, final String name) {
        final JTable table = (JTable) getComponent(frame, FrameComponent.SWING_JTABLE);
        final int row = table.getSelectedRow();
        if (row > -1) {
            final String alias = (String) ((Map<String, Object>) frame.getComponentMap().get(FrameComponent.MAP_ATRIB_TO_ALIAS)).get(name);
            return table.getValueAt(row, table.convertColumnIndexToView(table.getColumn(alias).getModelIndex()));
        } else {
            return null;
        }
    }

    public static List<Map<String, Object>> getSelectedRows(final Frame frame) {
        final List<Map<String, Object>> rows = new ArrayList<>();
        final JTable table = (JTable) getComponent(frame, FrameComponent.SWING_JTABLE);
        if (table.getSelectedRows().length > 0) {
            final int[] selectedRows = table.getSelectedRows();
            final Map<String, Object> fields = (HashMap<String, Object>) FrameController.getComponent(frame, FrameComponent.MAP_EDIT_FIELDS);
            final Map<String, Object> row = new HashMap<>();
            for (final int selectedRow : selectedRows) {
                row.clear();
                fields.keySet().forEach((field) -> {
                    final String alias = (String) ((Map<String, Object>) frame.getComponentMap().get(FrameComponent.MAP_ATRIB_TO_ALIAS)).get(field);
                    row.put(field, table.getValueAt(selectedRow, table.convertColumnIndexToView(table.getColumn(alias).getModelIndex())));
                });
                rows.add(row);
            }
        }

        return rows;
    }

}
