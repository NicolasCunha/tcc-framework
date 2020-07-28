package com.br.framework.core.factory.swing;

import com.br.framework.api.configurator.FrameConfigurator.FrameConfig;
import javax.swing.JFrame;

public class JFrameFactory {

    public JFrame build(final FrameConfig config) {
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

}
