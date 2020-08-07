package com.br.frame;

import com.br.framework.api.Framework;
import com.br.framework.api.services.PropertiesServices;
import com.br.framework.core.component.Frame;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FrameTest {

    protected static class FrameTestData {

        public static final String FRAME_TITLE = "Título";
        public static final String FRAME_TABLE = "aluno";
        public static final Integer FRAME_WIDTH = 1280;
        public static final Integer FRAME_HEIGHT = 720;

    }

    private static Frame frame;

    public FrameTest() {

    }

    @BeforeClass
    public static void setUpClass() {
        PropertiesServices.setupDatabaseAuth(
                "jdbc:mariadb://localhost:3306/tcc",
                "root",
                "root"
        );
        try {
            FrameTest.frame = Framework.getFrameConfigurator()
                    .title(FrameTestData.FRAME_TITLE)
                    .height(FrameTestData.FRAME_HEIGHT)
                    .width(FrameTestData.FRAME_WIDTH)
                    .table(FrameTestData.FRAME_TABLE)
                    .centered(true)
                    .build();
        } catch (Exception ex) {
            frame = null;
            Logger.getLogger(FrameTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void frameNotNull() {
        Assert.assertTrue(frame != null);
    }

    @Test
    public void frameConfigurationNotNull() {
        Assert.assertTrue(frame.getConfig() != null);
    }

    @Test
    public void frameControllerNotNull() {
        Assert.assertTrue(frame.getController() != null);
    }

    @Test
    public void frameTableNotNull() {
        Assert.assertTrue(frame.getTable() != null);
    }

    @Test
    public void frameHandlebarNotNull() {
        Assert.assertTrue(frame.getHandlebar() != null);
    }

    @Test
    public void frameConfigurationData() {
        Assert.assertTrue(frame.getConfig()
                .getTitle()
                .equalsIgnoreCase(
                        FrameTestData.FRAME_TITLE
                ));
        Assert.assertTrue(frame.getConfig()
                .getTable()
                .equalsIgnoreCase(
                        FrameTestData.FRAME_TABLE
                ));
        Assert.assertTrue(frame.getConfig()
                .getWidth() == FrameTestData.FRAME_WIDTH);
        Assert.assertTrue(frame.getConfig()
                .getHeight() == FrameTestData.FRAME_HEIGHT);
        Assert.assertTrue(frame.getConfig().isCentered());
    }

    @Test
    public void toFormMethod() {
        frame.toForm();
        Assert.assertTrue(frame.getHandlebar().isForm());
    }

    @Test
    public void toGridMethod() {
        frame.toGrid();
        Assert.assertTrue(frame.getHandlebar().isGrid());
    }

}
