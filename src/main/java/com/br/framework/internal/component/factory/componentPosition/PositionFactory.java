package com.br.framework.internal.component.factory.componentPosition;

import com.br.framework.internal.component.Window;
import com.br.framework.internal.component.WindowComponentEnum;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class PositionFactory {

    private static int lastButtonY = 0;

    private static PositionFactory instance;

    private PositionFactory() {

    }

    public static PositionFactory getInstance() {
        if (instance == null) {
            instance = new PositionFactory();
        } else {
            instance.resetY();
        }
        return instance;
    }

    public Rectangle calculateScrollPane(final Window frame) {
        final Rectangle rectangle = new Rectangle();
        rectangle.setBounds(
                PositionConstants.GRID_TABLE_X_POSITION,
                PositionConstants.GRID_TABLE_Y_POSITION,
                calculateScrollPaneWidth(frame),
                calculateScrollPaneHeight(frame)
        );
        return rectangle;
    }

    private int calculateScrollPaneHeight(final Window frame) {
        final JFrame jframe = (JFrame) frame.getController().getComponent(WindowComponentEnum.SWING_JFRAME);
        return jframe.getHeight() - 125;
    }

    private int calculateScrollPaneWidth(final Window frame) {
        final JFrame jframe = (JFrame) frame.getController().getComponent(WindowComponentEnum.SWING_JFRAME);
        return jframe.getWidth() / 3 * 2;
    }

    public Rectangle calculateButtonBounds(final Window frame) {
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

    private int commonXPos(final Window frame) {
        return frame.getConfig().getWidth() - PositionConstants.COMMON_X_POS;
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

    public int calculateLastXPosition(final Window frame) {
        final JScrollPane scrollEdit = (JScrollPane) frame.getController().getComponent(WindowComponentEnum.SWING_JSCROLL_EDIT);
        return scrollEdit.getX() - PositionConstants.CALC_LABEL_X_POS;
    }

    public int calculateLastYPosition(final Window frame) {
        final JScrollPane scrollEdit = (JScrollPane) frame.getController().getComponent(WindowComponentEnum.SWING_JSCROLL_EDIT);
        return scrollEdit.getY() - PositionConstants.CALC_LABEL_Y_POS;
    }

    public int increaseYPosition() {
        return PositionConstants.CALC_LABEL_INC_Y_POS;
    }

    private void resetY() {
        lastButtonY = PositionConstants.INITIAL_Y_POS;
    }

}
