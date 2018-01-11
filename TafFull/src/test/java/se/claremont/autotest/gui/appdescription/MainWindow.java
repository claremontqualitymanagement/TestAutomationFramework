package se.claremont.autotest.gui.appdescription;

import se.claremont.autotest.javasupport.interaction.elementidentification.By;
import se.claremont.autotest.javasupport.objectstructure.GuiComponent;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

public class MainWindow {

    public static JavaWindow mainWindow = new JavaWindow(".*TAF.*Test Automation Framework", "TAF Main window");

    public static JavaGuiElement runTestTab = new JavaGuiElement(mainWindow, By.byTextContaining(". Run tests"), "RunTestTab");

    public static class RunTabPanel {

        public static JavaGuiElement helpButton = new JavaGuiElement(mainWindow,
                By.byExactText("Help"),
                "HelpButton");

        public static JavaGuiElement cliTextArea = new JavaGuiElement(mainWindow,
                By.byName("CliCommandText"),
                "CliTextArea");

        public static JavaGuiElement resetButton = new JavaGuiElement(mainWindow,
                By.byExactText("Reset"),
                "ResetButton");

        public static JavaGuiElement label = new JavaGuiElement(mainWindow,
                By.byExactText("Test run name"),
                "RunNameLabel");

        /*
        public static JavaGuiElement runNameText = (JavaGuiElement)
                PositionBasedIdentificator
                        .fromAllTheElements(mainWindow.getComponentsAsJavaGuiElements())
                        .elementImmediatelyToTheRightOf(label);
        */

        public static JavaGuiElement runNameText = new JavaGuiElement(mainWindow,
                By.byName("RunNameTextField"),
                "RunNameText");

        public static JavaGuiElement executionModeSpinner = new JavaGuiElement(mainWindow,
                By.byName("ExecutionModeSpinner"),
                "ExecutionModeSpinner");

        public static JavaGuiElement setRunParametersButton = new JavaGuiElement(mainWindow,
                By.byExactText("Set run parameters..."),
                "SetRunParametersButton");

        public static JavaGuiElement cliToClipboardButton = new JavaGuiElement(mainWindow,
                By.byExactText("CLI to clipboard"),
                "CliToClipboardButton");

        public static JavaGuiElement startTestRunButton = new JavaGuiElement(mainWindow,
                By.byExactText("Start test run"),
                "StartTestRunButton"); //Should not be used since it produces System exit.

        public static JavaGuiElement exitButton = new JavaGuiElement(mainWindow,
                By.byExactText("Exit"),
                "ExitButton");

        public static GuiComponent pickTestsButton = new JavaGuiElement(mainWindow,
                By.byExactText("Pick test classes..."),
                "PickTestsButton");
    }

    public static class TestRunResultsWindow{

        public static JavaWindow testRunResultsWindow = new JavaWindow("^TAF.*Run result.*", "TAF Test run results window");

        public static JavaGuiElement closeButton = new JavaGuiElement(testRunResultsWindow,
                By.byExactText("Close"),
                "RunResultsWindowCloseButton");

        public static JavaGuiElement summaryReportButton = new JavaGuiElement(testRunResultsWindow,
                By.byExactText("Summary report"),
                "SummaryReportButton");

        public static JavaGuiElement logOutputArea = new JavaGuiElement(testRunResultsWindow,
                By.byClass("TafTextArea"),
                "LogOutputArea");
    }

    public static class PickTestsWindow {

        public static JavaWindow window = new JavaWindow("^TAF.*Test class picker.*", "TAF Test run results window");

        public static JavaGuiElement individualTestsCheckbox = new JavaGuiElement(window,
                By.byTextContaining("Show all classes"),
                "ShowAllTestsCheckbox");

        public static JavaGuiElement closeButton = new JavaGuiElement(window,
                By.byExactText("Close"),
                "CloseButton");
    }

    public static class AboutPanel {

        public static JavaGuiElement helpButton = new JavaGuiElement(mainWindow, By.byExactText("Help"), "HelpButton");

        public static JavaGuiElement runDiagnosticTestsButton = new JavaGuiElement(mainWindow, By.byTextContaining("diagnostic"), "RunDiagnosticTestsButton");

    }

    public static class TestStepPanel{

        public JavaGuiElement tryRunButton = new JavaGuiElement(mainWindow, By.byExactText("Try run"), "TryRunButton");

        public JavaGuiElement mergeStepButton = new JavaGuiElement(mainWindow, By.byExactText("Merge"), "TryRunButton");

        public JavaGuiElement cloneStepButton = new JavaGuiElement(mainWindow, By.byExactText("Clone"), "CloneStepButton");

        public JavaGuiElement generateCodeButton = new JavaGuiElement(mainWindow, By.byExactText("Generate code"), "GenerateCodeButton");

        public JavaGuiElement splitTestStepButton = new JavaGuiElement(mainWindow, By.byExactText("Split"), "SplitTestStepButton");

        public JavaGuiElement removeTestStepButton = new JavaGuiElement(mainWindow, By.byExactText("Remove"), "RemoveTestStepButton");


    }

    public static class RestPanel{

        public static JavaGuiElement createStepButton = new JavaGuiElement(mainWindow, By.byExactText("Create request"), "CreateRequestButton");

        public static TestStepPanel testStepPanel = new TestStepPanel();

    }

    public static class JavaPanel{

        public static JavaGuiElement guiSpyButton = new JavaGuiElement(mainWindow, By.byExactText("GUI Spy"), "GuiSpyButton");

        public static JavaGuiElement recordButton = new JavaGuiElement(mainWindow, By.byExactText("Record"), "RecordJavaButton");

        public static JavaGuiElement declareApplicationButton = new JavaGuiElement(mainWindow, By.byExactText("Declare application"), "DeclareApplicationButton");

        public static JavaGuiElement startApplicationButton = new JavaGuiElement(mainWindow, By.byExactText("Start application"), "StartApplicationButton");

        public static TestStepPanel testStepPanel = new TestStepPanel();


    }
}
