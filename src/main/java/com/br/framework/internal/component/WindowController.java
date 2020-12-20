package com.br.framework.internal.component;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

public class WindowController {

    private final Window frame;

    private WindowController(final Window frame) {
        this.frame = frame;
    }

    public static WindowController getInstance(final Window frame) {
        return new WindowController(frame);
    }

    public void swingRepaint() {
        final JFrame jframe = (JFrame) getComponent(WindowComponentEnum.SWING_JFRAME);
        jframe.repaint();
    }

    public void swingAdd(final Component c) {
        final JFrame jframe = (JFrame) getComponent(WindowComponentEnum.SWING_JFRAME);
        jframe.add(c);
    }

    public void addComponent(final WindowComponentEnum id, final Object val) {
        frame.getComponentMap().put(id, val);
    }

    public Object getComponent(final WindowComponentEnum id) {
        if (id == WindowComponentEnum.MAP_EDIT_FIELDS && frame.getComponentMap().get(id) == null) {
            return new HashMap<String, Object>();
        }
        return frame.getComponentMap().get(id);
    }

    private Map<String, Object> getAttributeAliases() {
        if (frame.getComponentMap().get(WindowComponentEnum.MAP_ATRIB_TO_ALIAS) == null) {
            frame.getComponentMap().put(WindowComponentEnum.MAP_ATRIB_TO_ALIAS, new HashMap<String, Object>());
        }
        return ((Map<String, Object>) frame.getComponentMap().get(WindowComponentEnum.MAP_ATRIB_TO_ALIAS));
    }

    public void addAttribAlias(final String attrib, final String alias) {
        getAttributeAliases().put(attrib, alias);
    }

    public boolean attribHasAlias(final String attrib) {
        return getAttributeAliases().containsKey(attrib);
    }

}
