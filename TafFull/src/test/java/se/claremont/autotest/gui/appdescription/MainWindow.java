package se.claremont.autotest.gui.appdescription;

import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedIdentificator;
import se.claremont.autotest.javasupport.interaction.elementidentification.By;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

public class MainWindow {

    public static JavaWindow mainWindow = new JavaWindow(".*TAF.*", "TAF Main window");

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

        public static JavaGuiElement runNameText = (JavaGuiElement)
                PositionBasedIdentificator
                        .fromAllTheElements(mainWindow.getComponentsAsJavaGuiElements())
                        .elementImmediatelyToTheRightOf(label);

        public static JavaGuiElement executionModeSpinner = new JavaGuiElement(mainWindow,
                By.byName("ExecutionModeSpinner"),
                "ExecutionModeSpinner");

        public static JavaGuiElement setRunParametersButton = new JavaGuiElement(mainWindow,
                By.byExactText("Set run parameters..."),
                "SetRunParametersButton");

        public static JavaGuiElement cliToClipboardButton = new JavaGuiElement(mainWindow,
                By.byExactText("CLI to clipboard"),
                "CliToClipboardButton");

        public static JavaGuiElement exitButton = new JavaGuiElement(mainWindow,
                By.byExactText("Exit"),
                "ExitButton"); //Should not be used since it produces System exit.
    }
}
