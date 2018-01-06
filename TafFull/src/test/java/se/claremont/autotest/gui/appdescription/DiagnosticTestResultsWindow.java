package se.claremont.autotest.gui.appdescription;

import se.claremont.autotest.javasupport.interaction.elementidentification.By;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

public class DiagnosticTestResultsWindow {

    public static JavaWindow window = new JavaWindow("^TAF.*Diagnostic run result.*");

    public static JavaGuiElement closeButton = new JavaGuiElement(window, By.byExactText("Close"), "CloseButton");

    public static JavaGuiElement failedTestsCountText = new JavaGuiElement(window,
            By.byName("FailedTestCaseCountText"),
            "FailedTestCaseCountText");
}
