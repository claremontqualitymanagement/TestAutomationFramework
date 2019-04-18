package se.claremont.taf.core.gui.appdescription;

import se.claremont.taf.javasupport.interaction.elementidentification.By;
import se.claremont.taf.javasupport.objectstructure.GuiComponent;
import se.claremont.taf.javasupport.objectstructure.JavaGuiElement;
import se.claremont.taf.javasupport.objectstructure.JavaWindow;

public class SetRunParameterWindow {

    public static JavaWindow runParametersWindow = new JavaWindow(".*Run settings", "TAF Run parameters window");

    public static JavaGuiElement addParameterButton = new JavaGuiElement(runParametersWindow,
            By.byExactText("Add parameter"),
            "AddRunParameterButton");

    public static JavaGuiElement canceledParameterName = new JavaGuiElement(runParametersWindow,
            By.byExactText("TestParameterValue"),
            "CenceledAddedParameterValueTextField");

    public static JavaGuiElement savedParameterName = new JavaGuiElement(runParametersWindow,
            By.byExactText("TestParameterName"),
            "AddedParameterNameLabel");

    public static JavaGuiElement savedParameterValue = new JavaGuiElement(runParametersWindow,
            By.byExactText("TestParameterValue"),
            "AddedParameterValueTestField");

    public static JavaGuiElement cancelParameterChangesButton = new JavaGuiElement(runParametersWindow,
            By.byExactText("Cancel"),
            "CancelParameterChangesButton");

    public static JavaGuiElement saveParametersButton = new JavaGuiElement(runParametersWindow,
            By.byExactText("Save"),
            "SaveParametersButton");

    public static GuiComponent canceledParameterValue = new JavaGuiElement(runParametersWindow,
            By.byExactText("TestParameterValue"),
            "CanceledParameterValueTestField");

    public static class AddParameterWindow {

        public static JavaWindow addParameterWindow = new JavaWindow(".*TAF - Add.*", "TAF Add run parameter window");

        public static JavaGuiElement parameterNameField = new JavaGuiElement(addParameterWindow,
                By.byName("ParameterNameField"),
                "ParameterNameField");

        public static JavaGuiElement parameterValueField = new JavaGuiElement(addParameterWindow,
                By.byName("ParameterValueField"),
                "ParameterValueField");

        public static JavaGuiElement cancelButton = new JavaGuiElement(addParameterWindow,
                By.byExactText("Cancel"),
                "CancelButton");

        public static JavaGuiElement saveButton = new JavaGuiElement(addParameterWindow,
                By.byExactText("Save"),
                "SaveButton");

    }
}
