package se.claremont.taf.gui.appdescription;

import se.claremont.taf.javasupport.interaction.elementidentification.By;
import se.claremont.taf.javasupport.objectstructure.JavaGuiElement;
import se.claremont.taf.javasupport.objectstructure.JavaWindow;

public class ApplicationDeclarationWindow {

public static JavaWindow window = new JavaWindow("^TAF.*Declare application.*");

public static JavaGuiElement friendlyNameTextField = new JavaGuiElement(window, By.byName("friendlyNameTextField").andByClass("TafTextField"), "FriendlyNameField");

public static JavaGuiElement pathToJarTextField = new JavaGuiElement(window, By.byName("pathToJarTextField"), "PathToJarTextField");

public static JavaGuiElement tryButton = new JavaGuiElement(window, By.byExactText("Try"), "TryButton");

public static JavaGuiElement cancelButton = new JavaGuiElement(window, By.byExactText("Cancel"), "CancelButton");

public static JavaGuiElement saveButton = new JavaGuiElement(window, By.byExactText("Save"), "SaveButton");

public static JavaGuiElement advancedCheckbox = new JavaGuiElement(window, By.byExactText("Show advanced options"), "AdvancedCheckbox");

public static JavaGuiElement mainClassTextField = new JavaGuiElement(window, By.byClass("JComboBox"), "MainClassTextField");

public static JavaGuiElement runtimeArgumentsTextField = new JavaGuiElement(window, By.byName("runtimeArgumentsTextField"), "RuntimeArgumentsTextField");


}
