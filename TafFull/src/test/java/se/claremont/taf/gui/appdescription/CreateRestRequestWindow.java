package se.claremont.taf.gui.appdescription;

import se.claremont.taf.javasupport.interaction.elementidentification.By;
import se.claremont.taf.javasupport.objectstructure.JavaGuiElement;
import se.claremont.taf.javasupport.objectstructure.JavaWindow;

public class CreateRestRequestWindow {

    public static JavaWindow window = new JavaWindow("^TAF.*Create REST request.*");

    public static JavaGuiElement sendMethodSpinner = new JavaGuiElement(window, By.byName("SendMethodSpinner"), "SendMethodSpinner");

    public static JavaGuiElement endPointUrl = new JavaGuiElement(window, By.byName("EndPointUrlTextField"), "EndPointUrlTextField");

    public static JavaGuiElement cancelButton = new JavaGuiElement(window, By.byExactText("Cancel"), "CancelButton");

}
