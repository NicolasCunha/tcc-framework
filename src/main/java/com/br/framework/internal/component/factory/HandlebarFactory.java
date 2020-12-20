package com.br.framework.internal.component.factory;

import com.br.framework.internal.component.factory.componentPosition.PositionFactory;
import com.br.framework.internal.component.Window;
import com.br.framework.internal.component.Handlebar;
import javax.swing.JButton;

public class HandlebarFactory {

    private final PositionFactory calculator = PositionFactory.getInstance();
    private final SwingComponentFactory swingFactory = SwingComponentFactory.getInstance();
    private static HandlebarFactory handlebarFactory;

    private enum ButtonTextEnum {

        GRID_FORM("Grid/Formulário"),
        INSERT("Novo Registro"),
        DELETE("Excluir Registro"),
        SAVE("Salvar Registro"),
        CLOSE_EXIT("Fechar");

        private final String desc;

        private ButtonTextEnum(final String str) {
            this.desc = str;
        }

        public String desc() {
            return this.desc;
        }

    }

    private HandlebarFactory() {
        // Empty.
    }

    public static HandlebarFactory getInstance() {
        if (handlebarFactory == null) {
            handlebarFactory = new HandlebarFactory();
        }
        return handlebarFactory;
    }

    public void build(final Window window) {
        final Handlebar handlebar = window.getHandlebar();
        // Grid / Form
        JButton button = swingFactory.createJButton(ButtonTextEnum.GRID_FORM.desc(), calculator.calculateButtonBounds(window));
        handlebar.setupGridFormBehavior(button);
        handlebar.setGridEditButton(button);
        window.getController().swingAdd(button);
        // Insert
        button = swingFactory.createJButton(ButtonTextEnum.INSERT.desc(), calculator.calculateButtonBounds(window));
        handlebar.setupInsertBehavior(button);
        handlebar.setInsertButton(button);
        window.getController().swingAdd(button);
        // Remove
        button = swingFactory.createJButton(ButtonTextEnum.DELETE.desc(), calculator.calculateButtonBounds(window));
        handlebar.setupRemoveBehavior(button);
        handlebar.setRemoveButton(button);
        window.getController().swingAdd(button);
        // Save
        button = swingFactory.createJButton(ButtonTextEnum.SAVE.desc(), calculator.calculateButtonBounds(window));
        handlebar.setupSaveBehavior(button);
        handlebar.setSaveButton(button);
        window.getController().swingAdd(button);
        // Close / Exit
        button = swingFactory.createJButton(ButtonTextEnum.CLOSE_EXIT.desc(), calculator.calculateButtonBounds(window));
        handlebar.setupCloseExitBehavior(button);
        handlebar.setExitCloseButton(button);
        window.getController().swingAdd(button);
    }

}
