package com.br.framework.core.controller;

import com.br.framework.api.configurator.FrameConfigurator.FrameConfig;
import com.br.framework.core.Frame;
import com.br.framework.core.enumerator.FrameComponent;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class PositionCalculator {

    private static int lastButtonY = 0;

    private class PositionConstants {

        public static final int GRID_TABLE_X_POSITION = 50;
        public static final int GRID_TABLE_Y_POSITION = 50;

        public static final int COMMON_X_POS = 200;
        public static final int INITIAL_Y_POS = 200;
        public static final int CALC_INC_Y_POS = 60;
        public static final int BUTTON_WIDTH = 150;
        public static final int BUTTON_HEIGHT = 30;

        /*
                Label
         */
        public static final int LABEL_HEIGHT = 20;
        public static final int CALC_LABEL_WIDTH = 100;
        public static final int CALC_LABEL_X_POS = 30;
        public static final int CALC_LABEL_Y_POS = 30;
        public static final int CALC_LABEL_INC_Y_POS = 50;

        /*
                TextField
         */
        public static final int CALC_FIELD_X_POS = 50;
        public static final int FIELD_WIDTH = 100;
        public static final int FIELD_HEIGHT = 20;

    }

    public Rectangle calculateScrollPane(final Frame frame) {
        final Rectangle rectangle = new Rectangle();
        rectangle.setBounds(
                PositionConstants.GRID_TABLE_X_POSITION,
                PositionConstants.GRID_TABLE_Y_POSITION,
                calculateScrollPaneWidth(frame),
                calculateScrollPaneHeight(frame)
        );
        return rectangle;
    }

    private int calculateScrollPaneHeight(final Frame frame) {
        final JFrame jframe = (JFrame) FrameController.getComponent(frame, FrameComponent.SWING_JFRAME);
        return jframe.getHeight() - 125;
    }

    private int calculateScrollPaneWidth(final Frame frame) {
        final JFrame jframe = (JFrame) FrameController.getComponent(frame, FrameComponent.SWING_JFRAME);
        return jframe.getWidth() / 3 * 2;
    }

    public Rectangle calculateGridViewButton(final Frame frame) {
        checkIncreaseButtonY();
        final Rectangle rectangle = new Rectangle();
        rectangle.setBounds(
                commonXPos(frame),
                lastButtonY,
                PositionConstants.BUTTON_WIDTH,
                PositionConstants.BUTTON_HEIGHT
        );
        return rectangle;
    }

    private int commonXPos(final Frame frame) {
        final FrameConfig config = (FrameConfig) FrameController.getComponent(frame, FrameComponent.FRAME_CONFIG);
        return config.getWidth() - PositionConstants.COMMON_X_POS;
    }

    private void checkIncreaseButtonY() {
        lastButtonY = lastButtonY == 0
                ? PositionConstants.INITIAL_Y_POS
                : lastButtonY + PositionConstants.CALC_INC_Y_POS;
    }

    public Rectangle calculateLabelBounds(final int lastCompX,
            final int lastCompY,
            final JLabel label) {
        final Rectangle rectangle = new Rectangle();
        rectangle.setBounds(
                lastCompX,
                lastCompY,
                calculateLabelWidth(label),
                PositionConstants.LABEL_HEIGHT
        );
        return rectangle;
    }

    public Rectangle calculateFieldBounds(final int lastCompX,
            final int lastCompY,
            final JLabel label,
            final String name) {
        final Rectangle rectangle = new Rectangle();
        rectangle.setBounds(
                (label.getX() * 2) + label.getWidth(),
                lastCompY,
                PositionConstants.FIELD_WIDTH,
                PositionConstants.FIELD_HEIGHT
        );
        return rectangle;
    }

    private int calculateLabelWidth(final JLabel label) {
        return label.getText().length() + PositionConstants.CALC_LABEL_WIDTH;
    }

    public int calculateLastXPosition(final Frame frame) {
        final JScrollPane scrollEdit = (JScrollPane) FrameController.getComponent(frame, FrameComponent.SWING_JSCROLL_EDIT);
        return scrollEdit.getX() - PositionConstants.CALC_LABEL_X_POS;
    }

    public int calculateLastYPosition(final Frame frame) {
        final JScrollPane scrollEdit = (JScrollPane) FrameController.getComponent(frame, FrameComponent.SWING_JSCROLL_EDIT);
        return scrollEdit.getY() - PositionConstants.CALC_LABEL_Y_POS;
    }

    public int increaseYPosition() {
        return PositionConstants.CALC_LABEL_INC_Y_POS;
    }

}
