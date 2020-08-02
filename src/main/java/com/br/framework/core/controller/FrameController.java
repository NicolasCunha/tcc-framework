package com.br.framework.core.controller;

import com.br.framework.core.component.Frame;
import com.br.framework.core.enumerator.FrameComponent;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

public class FrameController {

    private final Frame frame;

    private FrameController(final Frame frame) {
        this.frame = frame;
    }

    public static FrameController newInstance(final Frame frame) {
        return new FrameController(frame);
    }

    public void swingRepaint() {
        final JFrame jframe = (JFrame) getComponent(FrameComponent.SWING_JFRAME);
        jframe.repaint();
    }

    public void swingAdd(final Component c) {
        final JFrame jframe = (JFrame) getComponent(FrameComponent.SWING_JFRAME);
        jframe.add(c);
    }

    public void addComponent(final FrameComponent id, final Object val) {
        frame.getComponentMap().put(id, val);
    }

    public Object getComponent(final FrameComponent id) {
        if (id == FrameComponent.MAP_EDIT_FIELDS && frame.getComponentMap().get(id) == null) {
            return new HashMap<String, Object>();
        }
        return frame.getComponentMap().get(id);
    }

    public void addAttribAlias(final String attrib, final String alias) {
        if (frame.getComponentMap().get(FrameComponent.MAP_ATRIB_TO_ALIAS) == null) {
            frame.getComponentMap().put(FrameComponent.MAP_ATRIB_TO_ALIAS, new HashMap<String, Object>());
        }
        ((Map<String, Object>) frame.getComponentMap().get(FrameComponent.MAP_ATRIB_TO_ALIAS)).put(attrib, alias);
    }

}
