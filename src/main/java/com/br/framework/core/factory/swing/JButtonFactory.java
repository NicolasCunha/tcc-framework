package com.br.framework.core.factory.swing;

import java.awt.Rectangle;
import javax.swing.JButton;

public class JButtonFactory {

    private static JButtonFactory jbuttonFactory;

    private JButtonFactory() {

    }

    public static JButtonFactory getInstance() {
        if (jbuttonFactory == null) {
            jbuttonFactory = new JButtonFactory();
        }
        return jbuttonFactory;
    }

    public JButton build(final String content, final Rectangle bounds) {
        final JButton button = new JButton();
        button.setText(content);
        button.setBounds(bounds);
        return button;
    }

}
