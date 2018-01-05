package se.claremont.autotest.gui;

import org.junit.Assume;
import se.claremont.autotest.TAF;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.gui.appdescription.HelpWindow;
import se.claremont.autotest.gui.appdescription.MainWindow;
import se.claremont.autotest.gui.appdescription.SetRunParameterWindow;
import se.claremont.autotest.javasupport.applicationundertest.ApplicationUnderTest;
import se.claremont.autotest.javasupport.interaction.GenericInteractionMethods;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class TafActions {

    GenericInteractionMethods java;
    TestCase testCase;

    public TafActions(GenericInteractionMethods java){
        this.java = java;
        if(java.testCase != null){
            testCase = java.testCase;
        } else {
            testCase = new TestCase();
        }
    }

    public void resetRunTab(){
        java.click(MainWindow.RunTabPanel.resetButton);
    }

    public void runPanelTests() {
        MainWindow.mainWindow.waitForWindowToAppear(5);
        java.type(MainWindow.RunTabPanel.runNameText, "MyTestRunName");
        java.tabToNext();
        java.verifyElementTextContains(MainWindow.RunTabPanel.cliTextArea, "runName=MyTestRunName");
        java.click(MainWindow.RunTabPanel.resetButton);
        java.verifyText(String.join(", ", java.getSelected(MainWindow.RunTabPanel.executionModeSpinner)), "Sequential execution");
        java.click(MainWindow.RunTabPanel.executionModeSpinner);
        java.type(MainWindow.RunTabPanel.executionModeSpinner, KeyEvent.VK_UP);
        java.verifyText(String.join(", ", java.getSelected(MainWindow.RunTabPanel.executionModeSpinner)), "Test classes in parallel");
        java.verifyElementTextContains(MainWindow.RunTabPanel.cliTextArea, "PARALLEL_TEST_EXECUTION_MODE=classes");
        java.click(MainWindow.RunTabPanel.resetButton);
    }

    public void startParametersTest(){
        resetRunTab();
        java.click(MainWindow.RunTabPanel.setRunParametersButton); // Open set runting settings window
        java.verifyWindowIsDisplayed(SetRunParameterWindow.runParametersWindow);

        java.click(SetRunParameterWindow.addParameterButton); //Add a parameter, but cancel it
        java.type(SetRunParameterWindow.AddParameterWindow.parameterNameField, "TestParameterName");
        java.tabToNext();
        java.type(SetRunParameterWindow.AddParameterWindow.parameterValueField, "TestParameterValue");
        java.click(SetRunParameterWindow.AddParameterWindow.cancelButton);
        java.verifyElementDoesNotExist(SetRunParameterWindow.canceledParameterName);
        java.verifyElementTextIsExactly(MainWindow.RunTabPanel.cliTextArea, testCase.valueForFirstMatchForTestCaseDataParameter("CliText"));

        java.click(SetRunParameterWindow.addParameterButton); //Add a parameter, and save it to list
        java.type(SetRunParameterWindow.AddParameterWindow.parameterNameField, "TestParameterName");
        java.tabToNext();
        java.type(SetRunParameterWindow.AddParameterWindow.parameterValueField, "TestParameterValue");
        java.click(SetRunParameterWindow.AddParameterWindow.saveButton);
        java.verifyObjectExistence(SetRunParameterWindow.savedParameterName);
        java.verifyObjectExistence(SetRunParameterWindow.savedParameterValue);

        java.click(SetRunParameterWindow.cancelParameterChangesButton);
        java.verifyElementTextIsExactly(MainWindow.RunTabPanel.cliTextArea, testCase.valueForFirstMatchForTestCaseDataParameter("CliText"));
        java.click(MainWindow.RunTabPanel.setRunParametersButton);

        /* // File picker cannot be interacted with
        JavaGuiElement loadFromFileButton = new JavaGuiElement(runParametersWindow, By.byExactText("Load settings"), "LoadFromFileButton");
        java.click(loadFromFileButton);
        JavaWindow loadSettingsFromFileWindow = new JavaWindow(".*Run settings file picker.*");
        java.verifyWindowIsDisplayed(loadSettingsFromFileWindow);
        JavaGuiElement cancelButtonInLoadDialog = new JavaGuiElement(loadSettingsFromFileWindow, By.byExactText("Avbryt"), "LoadFileCancelButton");
        java.click(cancelButtonInLoadDialog);
        */

        java.click(SetRunParameterWindow.addParameterButton); //Add a parameter, and save it to list
        java.type(SetRunParameterWindow.AddParameterWindow.parameterNameField, "TestParameterName");
        java.tabToNext();
        java.type(SetRunParameterWindow.AddParameterWindow.parameterValueField, "TestParameterValue");
        java.click(SetRunParameterWindow.AddParameterWindow.saveButton);
        java.verifyObjectExistence(SetRunParameterWindow.savedParameterName);
        java.verifyObjectExistence(SetRunParameterWindow.savedParameterValue);

        //Save new parameter
        java.click(SetRunParameterWindow.saveParametersButton);
        java.verifyElementTextContains(MainWindow.RunTabPanel.cliTextArea, "TestParameterName=TestParameterValue");

    }

    public void cliButtonTest(){
        resetRunTab();
        String initialText = java.getText(MainWindow.RunTabPanel.cliTextArea);
        testCase.log(LogLevel.INFO, "Initial text of CLI text area: '" + initialText + "'.");
        java.click(MainWindow.RunTabPanel.cliToClipboardButton);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        String clipboardContent = "";
        try {
            clipboardContent = clpbrd.getData(DataFlavor.stringFlavor).toString();
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        testCase.addTestCaseData("CliText", java.getText(MainWindow.RunTabPanel.cliTextArea));
        if(clipboardContent.equals(initialText)){
            testCase.log(LogLevel.VERIFICATION_PASSED, "CLI to clipboad button worked (CLI string = '" + clipboardContent + "').");
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "Expected clipboard content to be '" + initialText + "', but it was '" + clipboardContent + "'.");
        }
    }

    public void testApplicationExit(){
        java.click(MainWindow.RunTabPanel.exitButton);
        int newNumberOfOpenWindows = ApplicationUnderTest.getWindows().size();
        if(newNumberOfOpenWindows == 0){
            testCase.log(LogLevel.VERIFICATION_PASSED, "System exited from exit button.");
        }else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "System did not exit form exit button.");
        }
    }

    public void testHelpButton(){
        resetRunTab();
        java.click(MainWindow.RunTabPanel.helpButton);
        java.verifyWindowIsDisplayed(HelpWindow.helpWindow);
        java.click(HelpWindow.helpWindowCloseButton);
    }

    public void startApplication() throws InterruptedException {
        Assume.assumeTrue("Desktop not supported. Cannot execute test.", Desktop.isDesktopSupported());
        Thread.sleep(4000);
        TAF.main(null);
        MainWindow.mainWindow.waitForWindowToAppear(5);
    }

}
