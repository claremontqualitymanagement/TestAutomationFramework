package se.claremont.taf.core.gui.appdescription;

import se.claremont.taf.javasupport.interaction.elementidentification.By;
import se.claremont.taf.javasupport.objectstructure.JavaGuiElement;
import se.claremont.taf.javasupport.objectstructure.JavaWindow;

public class GuiSpyWindow {

    public static JavaWindow window = new JavaWindow("^TAF.*GUI Spy.*");

    public static JavaGuiElement closeButton = new JavaGuiElement(window, By.byExactText("Close"), "CloseButton");

    public static JavaGuiElement programaticDescriptionField = new JavaGuiElement(window, By.byClass("TafHtmlTextPane"), "ProgramaticDescription");


}
