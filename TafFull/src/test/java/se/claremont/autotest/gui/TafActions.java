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
//        java.click(MainWindow.RunTabPanel.executionModeSpinner);
//        java.type(MainWindow.RunTabPanel.executionModeSpinner, KeyEvent.VK_UP);
        java.setSelected(MainWindow.RunTabPanel.executionModeSpinner, new String[] {"Test classes in parallel"});
        java.verifyText(String.join(", ", java.getSelected(MainWindow.RunTabPanel.executionModeSpinner)), "Test classes in parallel");
        java.verifyElementTextContains(MainWindow.RunTabPanel.cliTextArea, "PARALLEL_TEST_EXECUTION_MODE=classes");
        java.click(MainWindow.RunTabPanel.resetButton);
    }

    public void startParametersTest(){
        resetRunTab();
        testCase.addTestCaseData("CliText", java.getText(MainWindow.RunTabPanel.cliTextArea));
        java.click(MainWindow.RunTabPanel.setRunParametersButton); // Open set runting settings window
        java.verifyWindowIsDisplayed(SetRunParameterWindow.runParametersWindow);

        addTestRunParameter("TestParameterName", "TestParameterValue", false);
        java.verifyElementDoesNotExist(SetRunParameterWindow.canceledParameterName);
        java.verifyElementDoesNotExist(SetRunParameterWindow.canceledParameterValue);
        java.verifyElementTextIsExactly(MainWindow.RunTabPanel.cliTextArea, testCase.valueForFirstMatchForTestCaseDataParameter("CliText"));

        addTestRunParameter("TestParameterName", "TestParameterValue", true);
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

        addTestRunParameter("TestParameterName", "TestParameterValue", true);
        java.verifyObjectExistence(SetRunParameterWindow.savedParameterName);
        java.verifyObjectExistence(SetRunParameterWindow.savedParameterValue);

        //Save new parameter
        java.click(SetRunParameterWindow.saveParametersButton);
        java.verifyElementTextContains(MainWindow.RunTabPanel.cliTextArea, "TestParameterName=TestParameterValue");
    }

    public void addTestRunParameter(String parameterName, String parameterValue, boolean saveParameter){
        java.click(SetRunParameterWindow.addParameterButton); //Add a parameter, and save it to list
        java.type(SetRunParameterWindow.AddParameterWindow.parameterNameField, parameterName);
        java.tabToNext();
        java.type(SetRunParameterWindow.AddParameterWindow.parameterValueField, parameterValue);
        java.tabToNext();
        java.wait(200);
        if(saveParameter){
            java.click(SetRunParameterWindow.AddParameterWindow.saveButton);
        } else{
            java.click(SetRunParameterWindow.AddParameterWindow.cancelButton);
        }
        java.verifyWindowIsNotDisplayed(SetRunParameterWindow.AddParameterWindow.addParameterWindow);
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

    public void startApplication() {
        Assume.assumeTrue("Desktop not supported. Cannot execute test.", Desktop.isDesktopSupported());
        if(!MainWindow.mainWindow.isShown()) {
            TAF.main(null);
            MainWindow.mainWindow.waitForWindowToAppear(5);
        }
    }

    public void startTestRunTest() {
        java.click(MainWindow.RunTabPanel.startTestRunButton);
        java.verifyWindowIsDisplayed(MainWindow.TestRunResultsWindow.testRunResultsWindow);
        ///java.wait(1000);
        java.verifyObjectIsDisplayed(MainWindow.TestRunResultsWindow.summaryReportButton);
        java.verifyElementTextContains(MainWindow.TestRunResultsWindow.logOutputArea, "Test run finished.");
        java.click(MainWindow.TestRunResultsWindow.closeButton);
        java.verifyWindowIsNotDisplayed(MainWindow.TestRunResultsWindow.testRunResultsWindow);
    }



    public void closeApplication() {
        testCase.log(LogLevel.INFO, "This is where the application should have been closed if it wasn't for the System.exit()");
        java.click(MainWindow.RunTabPanel.exitButton);
        java.closeAllWindows();
        /*
        SecurityManager securityManager = System.getSecurityManager();
        System.setSecurityManager(new TafSecurityManager());
        java.click(MainWindow.RunTabPanel.exitButton);
        System.setSecurityManager(securityManager);
        */
    }

    private void blockSystemExit(){
        System.setSecurityManager(new TafSecurityManager());
    }

    class TafSecurityManager extends SecurityManager {

        @Override
        public void checkExit(int status) {
            testCase.log(LogLevel.EXECUTED, "Application should have exited with exit code " + status + " here.");
        }
    }
}
