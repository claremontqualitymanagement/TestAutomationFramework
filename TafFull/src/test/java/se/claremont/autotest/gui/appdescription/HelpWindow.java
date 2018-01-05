package se.claremont.autotest.gui.appdescription;

import se.claremont.autotest.javasupport.interaction.elementidentification.By;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

public class HelpWindow {

    public static JavaWindow helpWindow = new JavaWindow(".*TAF - Help.*", "TAF Help window");

    public static JavaGuiElement helpWindowCloseButton = new JavaGuiElement(helpWindow, By
            .byExactText("Close"),
            "HelpCloseButton");
}
