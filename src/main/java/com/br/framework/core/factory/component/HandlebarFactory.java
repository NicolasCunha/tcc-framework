package com.br.framework.core.factory.component;

import com.br.framework.core.component.Frame;
import com.br.framework.core.component.Handlebar;
import com.br.framework.core.controller.PositionCalculator;
import com.br.framework.core.factory.swing.JButtonFactory;
import javax.swing.JButton;

public class HandlebarFactory {

    private final PositionCalculator calculator = PositionCalculator.getInstance();
    private final JButtonFactory buttonFactory = JButtonFactory.getInstance();
    private static HandlebarFactory handlebarFactory;

    private enum ButtonText {

        GRID_FORM("Grid/Formulário"),
        INSERT("Novo Registro"),
        DELETE("Excluir Registro"),
        SAVE("Salvar Registro"),
        CLOSE_EXIT("Fechar");

        private final String desc;

        private ButtonText(final String str) {
            this.desc = str;
        }

        public String desc() {
            return this.desc;
        }

    }

    private HandlebarFactory() {

    }

    public static HandlebarFactory getInstance() {
        if (handlebarFactory == null) {
            handlebarFactory = new HandlebarFactory();
        }
        return handlebarFactory;
    }

    public void build(final Frame frame) {
        final Handlebar handlebar = frame.getHandlebar();
        // Grid / Form
        JButton button = buttonFactory.build(ButtonText.GRID_FORM.desc(), calculator.calculateButtonBounds(frame));
        handlebar.setupGridFormBehavior(button);
        handlebar.setGridEditButton(button);
        frame.getController().swingAdd(button);
        // Insert
        button = buttonFactory.build(ButtonText.INSERT.desc(), calculator.calculateButtonBounds(frame));
        handlebar.setupInsertBehavior(button);
        handlebar.setInsertButton(button);
        frame.getController().swingAdd(button);
        // Remove
        button = buttonFactory.build(ButtonText.DELETE.desc(), calculator.calculateButtonBounds(frame));
        handlebar.setupRemoveBehavior(button);
        handlebar.setRemoveButton(button);
        frame.getController().swingAdd(button);
        // Save
        button = buttonFactory.build(ButtonText.SAVE.desc(), calculator.calculateButtonBounds(frame));
        handlebar.setupSaveBehavior(button);
        handlebar.setSaveButton(button);
        frame.getController().swingAdd(button);
        // Close / Exit
        button = buttonFactory.build(ButtonText.CLOSE_EXIT.desc(), calculator.calculateButtonBounds(frame));
        handlebar.setupCloseExitBehavior(button);
        handlebar.setExitCloseButton(button);
        frame.getController().swingAdd(button);
    }

}
