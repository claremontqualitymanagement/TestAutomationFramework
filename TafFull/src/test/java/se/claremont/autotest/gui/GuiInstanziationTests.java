package se.claremont.autotest.gui;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import se.claremont.autotest.common.testset.TestSet;
import se.claremont.autotest.gui.appdescription.MainWindow;
import se.claremont.autotest.javasupport.interaction.GenericInteractionMethods;

import java.awt.*;

public class GuiInstanziationTests extends TestSet {

    GenericInteractionMethods java;
    TafActions taf;

    @Before
    public void setup(){
        Assume.assumeTrue("Cannot run GUI smoke test since desktop is not supported", Desktop.isDesktopSupported());
        java = new GenericInteractionMethods(currentTestCase());
        taf = new TafActions(java);

    }

    @Test
    public void smokeTest(){
        taf.startApplication();
        taf.runPanelInteractionTests();
        taf.runPanelHelpButtonTest();
        taf.cliButtonTest();
        taf.startParametersWindowInteractionTest();
        taf.chooseTestsWindowTests();
        taf.startTestRunTest();
        java.activateTab(MainWindow.mainWindow, "Create");
        java.activateTab(MainWindow.mainWindow, "REST");
        taf.createRestRequestTest();
        java.activateTab(MainWindow.mainWindow, "Web");
        taf.applicationDeclarationTest();
        taf.javaRecorderTest();
        taf.guiSpyTest();
        java.activateTab(MainWindow.mainWindow, "Smart");
        taf.aboutTabHelpWindowTests();
        taf.diagnosticTestRunTests();
        taf.closeApplication();
    }


}
