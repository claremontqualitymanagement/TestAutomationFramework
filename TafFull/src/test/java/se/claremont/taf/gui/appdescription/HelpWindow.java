package se.claremont.taf.gui.appdescription;

import se.claremont.taf.javasupport.interaction.elementidentification.By;
import se.claremont.taf.javasupport.objectstructure.JavaGuiElement;
import se.claremont.taf.javasupport.objectstructure.JavaWindow;

public class HelpWindow {

    public static JavaWindow helpWindow = new JavaWindow(".*TAF - Help.*", "TAF Help window");

    public static JavaGuiElement helpWindowCloseButton = new JavaGuiElement(helpWindow, By
            .byExactText("Close"),
            "HelpCloseButton");
}
