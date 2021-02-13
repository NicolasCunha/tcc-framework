package com.br.framework.internal.component.factory;

import com.br.framework.internal.component.factory.componentPosition.PositionFactory;
import com.br.framework.internal.component.Window;
import com.br.framework.internal.component.Handlebar;
import javax.swing.JButton;

public class HandlebarFactory {

    private static final PositionFactory calculator = PositionFactory.getInstance();

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

    public static void build(final Window window) {
        final Handlebar handlebar = window.getHandlebar();
        // Grid / Form
        JButton button = SwingComponentFactory.createJButton(ButtonTextEnum.GRID_FORM.desc(), calculator.calculateButtonBounds(window));
        handlebar.setupGridFormBehavior(button);
        handlebar.setGridEditButton(button);
        window.getController().addComponent(button);
        // Insert
        button = SwingComponentFactory.createJButton(ButtonTextEnum.INSERT.desc(), calculator.calculateButtonBounds(window));
        handlebar.setupInsertBehavior(button);
        handlebar.setInsertButton(button);
        window.getController().addComponent(button);
        // Remove
        button = SwingComponentFactory.createJButton(ButtonTextEnum.DELETE.desc(), calculator.calculateButtonBounds(window));
        handlebar.setupRemoveBehavior(button);
        handlebar.setRemoveButton(button);
        window.getController().addComponent(button);
        // Save
        button = SwingComponentFactory.createJButton(ButtonTextEnum.SAVE.desc(), calculator.calculateButtonBounds(window));
        handlebar.setupSaveBehavior(button);
        handlebar.setSaveButton(button);
        window.getController().addComponent(button);
        // Close / Exit
        button = SwingComponentFactory.createJButton(ButtonTextEnum.CLOSE_EXIT.desc(), calculator.calculateButtonBounds(window));
        handlebar.setupCloseExitBehavior(button);
        handlebar.setExitCloseButton(button);
        window.getController().addComponent(button);
    }

}
