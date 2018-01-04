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

    JavaWindow mainWindow = new JavaWindow(".*TAF.*");
    JavaGuiElement helpButton = new JavaGuiElement(mainWindow, By.byExactText("Help"), "HelpButton");
    JavaGuiElement cliTextArea = new JavaGuiElement(mainWindow, By.byName("CliCommandText"), "CliTextArea");
    JavaGuiElement resetButton = new JavaGuiElement(mainWindow, By.byExactText("Reset"), "ResetButton");

    @Before
    public void setup(){
        Assume.assumeTrue("Cannot run GUI smoke test since desktop is not supported", Desktop.isDesktopSupported());
        java = new GenericInteractionMethods(currentTestCase());
    }

    @Test
    public void startNoArgumentsSmokeTest() throws InterruptedException {
        startApplication();
        runPanelTests();
        java.click(resetButton);
        testHelpButton();
        java.click(resetButton);
        cliButtonTest();
        java.click(resetButton);
        startParametersTest();
        java.click(resetButton);
        //testApplicationExit(); //Don't do this. it exits the system.
    }

    private void runPanelTests() {
        JavaGuiElement label = new JavaGuiElement(mainWindow, By.byExactText("Test run name"), "RunNameLabel");
        JavaGuiElement runNameText = (JavaGuiElement)PositionBasedIdentificator.fromAllTheElements(mainWindow.getComponentsAsJavaGuiElements()).elementImmediatelyToTheRightOf(label);
        System.out.println(java.getText(runNameText));
        JavaGuiElement executionModeSpinner = new JavaGuiElement(mainWindow, By.byName("ExecutionModeSpinner"), "ExecutionModeSpinner");
        mainWindow.waitForWindowToAppear(5);
        java.type(runNameText, "MyTestRunName");
        java.tabToNext();
        java.verifyElementTextContains(cliTextArea, "runName=MyTestRunName");
        java.click(resetButton);
        java.verifyText(String.join(", ", java.getSelected(executionModeSpinner)), "Sequential execution");
        java.click(executionModeSpinner);
        java.type(executionModeSpinner, KeyEvent.VK_UP);
        java.verifyText(String.join(", ", java.getSelected(executionModeSpinner)), "Test classes in parallel");
        java.verifyElementTextContains(cliTextArea, "PARALLEL_TEST_EXECUTION_MODE=classes");
        java.click(resetButton);
    }

    private void startParametersTest(){
        JavaGuiElement setRunParametersButton = new JavaGuiElement(mainWindow, By.byExactText("Set run parameters..."), "SetRunParametersButton");

        JavaWindow runParametersWindow = new JavaWindow(".*Run settings");
        JavaGuiElement addParameterButton = new JavaGuiElement(runParametersWindow, By.byExactText("Add parameter"), "AddRunParameterButton");
        JavaGuiElement canceledParameterName = new JavaGuiElement(runParametersWindow, By.byExactText("TestParameterValue"), "CencelAddedParameterValueTextField");
        JavaGuiElement savedParameterName = new JavaGuiElement(runParametersWindow, By.byExactText("TestParameterName"), "AddedParameterNameLabel");
        JavaGuiElement savedParameterValue = new JavaGuiElement(runParametersWindow, By.byExactText("TestParameterValue"), "AddedParameterValueTestField");
        JavaGuiElement cancelParameterChangesButton = new JavaGuiElement(runParametersWindow, By.byExactText("Cancel"), "CancelParameterChangesButton");
        JavaGuiElement saveParametersButton = new JavaGuiElement(runParametersWindow, By.byExactText("Save"), "SaveParametersButton");

        JavaWindow addParameterWindow = new JavaWindow(".*TAF - Add.*");
        JavaGuiElement parameterNameField = new JavaGuiElement(addParameterWindow, By.byName("ParameterNameField"), "ParameterNameField");
        JavaGuiElement parameterValueField = new JavaGuiElement(addParameterWindow, By.byName("ParameterValueField"), "ParameterValueField");
        JavaGuiElement cancelButton = new JavaGuiElement(addParameterWindow, By.byExactText("Cancel"), "CancelButton");
        JavaGuiElement saveButton = new JavaGuiElement(addParameterWindow, By.byExactText("Save"), "SaveButton");

        java.click(setRunParametersButton); // Open set runting settings window
        java.verifyWindowIsDisplayed(runParametersWindow);

        java.click(addParameterButton); //Add a parameter, but cancel it
        java.type(parameterNameField, "TestParameterName");
        java.tabToNext();
        java.type(parameterValueField, "TestParameterValue");
        java.click(cancelButton);
        java.verifyElementDoesNotExist(canceledParameterName);
        java.verifyElementTextIsExactly(cliTextArea, currentTestCase().valueForFirstMatchForTestCaseDataParameter("CliText"));

        java.click(addParameterButton); //Add a parameter, and save it to list
        java.type(parameterNameField, "TestParameterName");
        java.tabToNext();
        java.type(parameterValueField, "TestParameterValue");
        java.click(saveButton);
        java.verifyObjectExistence(savedParameterName);
        java.verifyObjectExistence(savedParameterValue);

        java.click(cancelParameterChangesButton);
        java.verifyElementTextIsExactly(cliTextArea, currentTestCase().valueForFirstMatchForTestCaseDataParameter("CliText"));
        java.click(setRunParametersButton);

        /* // File picker cannot be interacted with
        JavaGuiElement loadFromFileButton = new JavaGuiElement(runParametersWindow, By.byExactText("Load settings"), "LoadFromFileButton");
        java.click(loadFromFileButton);
        JavaWindow loadSettingsFromFileWindow = new JavaWindow(".*Run settings file picker.*");
        java.verifyWindowIsDisplayed(loadSettingsFromFileWindow);
        JavaGuiElement cancelButtonInLoadDialog = new JavaGuiElement(loadSettingsFromFileWindow, By.byExactText("Avbryt"), "LoadFileCancelButton");
        java.click(cancelButtonInLoadDialog);
        */

        java.click(addParameterButton); //Add a parameter, and save it to list
        java.type(parameterNameField, "TestParameterName");
        java.tabToNext();
        java.type(parameterValueField, "TestParameterValue");
        java.click(saveButton);
        java.verifyObjectExistence(savedParameterName);
        java.verifyObjectExistence(savedParameterValue);

        //Save new parameter
        java.click(saveParametersButton);
        java.verifyElementTextContains(cliTextArea, "TestParameterName=TestParameterValue");

    }

    private void cliButtonTest(){
        String initialText = java.getText(cliTextArea);
        currentTestCase().log(LogLevel.INFO, "Initial text of CLI text area: '" + initialText + "'.");
        JavaGuiElement cliToClipboardButton = new JavaGuiElement(mainWindow, By.byExactText("CLI to clipboard"), "CliToClipboardButton");
        java.click(cliToClipboardButton);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        String clipboardContent = "";
        try {
            clipboardContent = clpbrd.getData(DataFlavor.stringFlavor).toString();
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentTestCase().addTestCaseData("CliText", java.getText(cliTextArea));
        if(clipboardContent.equals(initialText)){
            currentTestCase().log(LogLevel.VERIFICATION_PASSED, "CLI to clipboad button worked (CLI string = '" + clipboardContent + "').");
        } else {
            currentTestCase().log(LogLevel.VERIFICATION_FAILED, "Expected clipboard content to be '" + initialText + "', but it was '" + clipboardContent + "'.");
        }
    }

    private void testApplicationExit(){
        JavaGuiElement exitButton = new JavaGuiElement(mainWindow, By.byExactText("Exit"), "ExitButton");
        java.click(exitButton);
        int newNumberOfOpenWindows = ApplicationUnderTest.getWindows().size();
        if(newNumberOfOpenWindows == 0){
            currentTestCase().log(LogLevel.VERIFICATION_PASSED, "System exited from exit button.");
        }else {
            currentTestCase().log(LogLevel.VERIFICATION_FAILED, "System did not exit form exit button.");
        }
    }

    private void testHelpButton(){
        JavaWindow helpWindow = new JavaWindow(".*TAF - Help.*");
        JavaGuiElement helpWindowCloseButton = new JavaGuiElement(helpWindow, By.byExactText("Close"), "HelpCloseButton");

        java.click(helpButton);
        java.verifyWindowIsDisplayed(helpWindow);
        java.click(helpWindowCloseButton);
    }

    private void startApplication() throws InterruptedException {
        Assume.assumeTrue("Desktop not supported. Cannot execute test.", Desktop.isDesktopSupported());
        Thread.sleep(4000);
        TAF.main(null);
        mainWindow.waitForWindowToAppear(5);
    }
}
