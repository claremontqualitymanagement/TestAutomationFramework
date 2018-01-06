package se.claremont.autotest.gui;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.common.testset.TestSet;
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
    public void startNoArgumentsSmokeTest(){
        taf.startApplication();
        //taf.testApplicationExit(); //Don't do this. it exits the system.
        taf.closeApplication();
    }

    @Test
    public void runPanelSmokeTest(){
        taf.startApplication();
        taf.runPanelTests();
        taf.closeApplication();
    }

    @Test
    public void helpDialogSmokeTest(){
        taf.startApplication();
        taf.testHelpButton();
        taf.closeApplication();
    }

    @Test
    public void copyCliToClipboardButtonSmokeTest(){
        taf.startApplication();
        taf.cliButtonTest();
        taf.closeApplication();
    }

    @Test
    public void runSettingsDialogueSmokeTest(){
        taf.startApplication();
        taf.startParametersTest();
        taf.closeApplication();
    }

    @Test
    public void startTestRunSmokeTest(){
        taf.startApplication();
        taf.startTestRunTest();
        taf.closeApplication();
    }

}
