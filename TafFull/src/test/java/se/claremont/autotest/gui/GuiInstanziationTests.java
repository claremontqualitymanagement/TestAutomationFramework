package se.claremont.autotest.gui;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import se.claremont.autotest.TAF;
import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedIdentificator;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testset.TestSet;
import se.claremont.autotest.gui.appdescription.HelpWindow;
import se.claremont.autotest.gui.appdescription.MainWindow;
import se.claremont.autotest.gui.appdescription.SetRunParameterWindow;
import se.claremont.autotest.javasupport.applicationundertest.ApplicationUnderTest;
import se.claremont.autotest.javasupport.applicationundertest.applicationstarters.ApplicationStartMechanism;
import se.claremont.autotest.javasupport.interaction.GenericInteractionMethods;
import se.claremont.autotest.javasupport.interaction.elementidentification.By;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

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
    public void startNoArgumentsSmokeTest() throws InterruptedException {
        taf.startApplication();
        taf.runPanelTests();
        taf.testHelpButton();
        taf.cliButtonTest();
        taf.startParametersTest();
        //taf.testApplicationExit(); //Don't do this. it exits the system.
    }

}
