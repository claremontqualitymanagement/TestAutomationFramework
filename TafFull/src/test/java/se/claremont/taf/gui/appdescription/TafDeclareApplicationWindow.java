package se.claremont.taf.gui.appdescription;

import se.claremont.taf.javasupport.interaction.elementidentification.By;
import se.claremont.taf.javasupport.objectstructure.JavaGuiElement;
import se.claremont.taf.javasupport.objectstructure.JavaWindow;

public class TafDeclareApplicationWindow {

    public static JavaWindow window = new JavaWindow("TAF - Declare application");

    public static JavaGuiElement nullglassPaneJPanel(){
        return new JavaGuiElement(window, By
                .byName("null.glassPane"),
                "nullglassPaneJPanel");
    }

    public static JavaGuiElement nulllayeredPaneJLayeredPane(){
        return new JavaGuiElement(window, By
                .byName("null.layeredPane"),
                "nulllayeredPaneJLayeredPane");
    }

    public static JavaGuiElement nullcontentPaneJPanel(){
        return new JavaGuiElement(window, By
                .byName("null.contentPane"),
                "nullcontentPaneJPanel");
    }

    public static JavaGuiElement declaresutmainpanelTafPanel(){
        return new JavaGuiElement(window, By
                .byName("declaresutmainpanel"),
                "declaresutmainpanelTafPanel");
    }

    public static JavaGuiElement applicationDeclarationForTestingLabelTafLabel(){
        return new JavaGuiElement(window, By
                .byName("ApplicationDeclarationForTestingLabel"),
                "applicationDeclarationForTestingLabelTafLabel");
    }

    public static JavaGuiElement labelTafLabel(){
        return new JavaGuiElement(window, By
                .byName("Label"),
                "labelTafLabel");
    }

    public static JavaGuiElement friendlyNameLabelTafLabel(){
        return new JavaGuiElement(window, By
                .byName("FriendlyName_Label"),
                "friendlyNameLabelTafLabel");
    }

    public static JavaGuiElement friendlyNameTextFieldTafTextField(){
        return new JavaGuiElement(window, By
                .byName("friendlyNameTextField"),
                "friendlyNameTextFieldTafTextField");
    }

    public static JavaGuiElement startapplicationpanelTafPanel(){
        return new JavaGuiElement(window, By
                .byName("startapplicationpanel"),
                "startapplicationpanelTafPanel");
    }

    public static JavaGuiElement applicationStartParametersLabelTafLabel(){
        return new JavaGuiElement(window, By
                .byName("ApplicationStartParametersLabel"),
                "applicationStartParametersLabelTafLabel");
    }

    public static JavaGuiElement pathToJarLabelTafLabel(){
        return new JavaGuiElement(window, By
                .byName("PathToJarLabel"),
                "pathToJarLabelTafLabel");
    }

    public static JavaGuiElement pathToJarTextFieldLocalTextField(){
        return new JavaGuiElement(window, By
                .byName("pathToJarTextField"),
                "pathToJarTextFieldLocalTextField");
    }

    public static JavaGuiElement selectButtonTafButton(){
        return new JavaGuiElement(window, By
                .byName("SelectButton"),
                "selectButtonTafButton");
    }

    public static JavaGuiElement mainClassLabelTafLabel(){
        return new JavaGuiElement(window, By
                .byName("MainClassLabel"),
                "mainClassLabelTafLabel");
    }

    public static JavaGuiElement runtimeArgumentsLabelTafLabel(){
        return new JavaGuiElement(window, By
                .byName("RuntimeArgumentsLabel"),
                "runtimeArgumentsLabelTafLabel");
    }

    public static JavaGuiElement runtimeArgumentsTextFieldLocalTextField(){
        return new JavaGuiElement(window, By
                .byName("runtimeArgumentsTextField"),
                "runtimeArgumentsTextFieldLocalTextField");
    }

    public static JavaGuiElement showAdvancedOptionsTafCheckbox(){
        return new JavaGuiElement(window, By
                .byName("ShowAdvancedOptions"),
                "showAdvancedOptionsTafCheckbox");
    }

    public static JavaGuiElement advancedparameterspanelTafPanel(){
        return new JavaGuiElement(window, By
                .byName("advancedparameterspanel"),
                "advancedparameterspanelTafPanel");
    }

    public static JavaGuiElement advancedLabelTafLabel(){
        return new JavaGuiElement(window, By
                .byName("AdvancedLabel"),
                "advancedLabelTafLabel");
    }

    public static JavaGuiElement workingFolderLabelTafLabel(){
        return new JavaGuiElement(window, By
                .byName("WorkingFolderLabel"),
                "workingFolderLabelTafLabel");
    }

    public static JavaGuiElement workingFolderTextFieldLocalTextField(){
        return new JavaGuiElement(window, By
                .byName("workingFolderTextField"),
                "workingFolderTextFieldLocalTextField");
    }

    public static JavaGuiElement loadedExtraLibrariesLabelTafLabel(){
        return new JavaGuiElement(window, By
                .byName("LoadedExtraLibrariesLabel"),
                "loadedExtraLibrariesLabelTafLabel");
    }

    public static JavaGuiElement loadedExternalLibrariesTextFieldTafTextField(){
        return new JavaGuiElement(window, By
                .byName("loadedExternalLibrariesTextField"),
                "loadedExternalLibrariesTextFieldTafTextField");
    }

    public static JavaGuiElement modifiedSystemParametersLabelTafLabel(){
        return new JavaGuiElement(window, By
                .byName("ModifiedSystemParametersLabel"),
                "modifiedSystemParametersLabelTafLabel");
    }

    public static JavaGuiElement systemParametersTextFieldTafTextField(){
        return new JavaGuiElement(window, By
                .byName("systemParametersTextField"),
                "systemParametersTextFieldTafTextField");
    }

    public static JavaGuiElement environmentVariablesLabelTafLabel(){
        return new JavaGuiElement(window, By
                .byName("EnvironmentVariablesLabel"),
                "environmentVariablesLabelTafLabel");
    }

    public static JavaGuiElement modifiedEnvironmentVariablesTextFieldTafTextField(){
        return new JavaGuiElement(window, By
                .byName("modifiedEnvironmentVariablesTextField"),
                "modifiedEnvironmentVariablesTextFieldTafTextField");
    }

    public static JavaGuiElement jvmArgumentsLabelTafLabel(){
        return new JavaGuiElement(window, By
                .byName("JvmArgumentsLabel"),
                "jvmArgumentsLabelTafLabel");
    }

    public static JavaGuiElement jvmArgumentsTextFieldTafTextField(){
        return new JavaGuiElement(window, By
                .byName("jvmArgumentsTextField"),
                "jvmArgumentsTextFieldTafTextField");
    }

    public static JavaGuiElement addButtonTafButton(){
        return new JavaGuiElement(window, By
                .byName("AddButton"),
                "addButtonTafButton");
    }

    public static JavaGuiElement loadFromFileButtonTafButton(){
        return new JavaGuiElement(window, By
                .byName("LoadFromFileButton"),
                "loadFromFileButtonTafButton");
    }

    public static JavaGuiElement saveToFileButtonTafButton(){
        return new JavaGuiElement(window, By
                .byName("SaveToFileButton"),
                "saveToFileButtonTafButton");
    }

    public static JavaGuiElement correspondingCliCommandLabelTafLabel(){
        return new JavaGuiElement(window, By
                .byName("CorrespondingCliCommandLabel"),
                "correspondingCliCommandLabelTafLabel");
    }

    public static JavaGuiElement tryButtonTafButton(){
        return new JavaGuiElement(window, By
                .byName("TryButton"),
                "tryButtonTafButton");
    }

    public static JavaGuiElement cancelButtonTafButton(){
        return new JavaGuiElement(window, By
                .byName("CancelButton"),
                "cancelButtonTafButton");
    }

    public static JavaGuiElement saveButtonTafButton(){
        return new JavaGuiElement(window, By
                .byName("SaveButton"),
                "saveButtonTafButton");
    }

    public static JavaGuiElement noNamedObjectJRootPane(){
        return new JavaGuiElement(window, By
                .byClass("JRootPane"),
                "noNamedObjectJRootPane");
    }

    public static JavaGuiElement noNamedObjectJComboBox(){
        return new JavaGuiElement(window, By
                .byClass("JComboBox"),
                "noNamedObjectJComboBox");
    }

    public static JavaGuiElement noNamedObjectMetalComboBoxButton(){
        return new JavaGuiElement(window, By
                .byClass("MetalComboBoxButton"),
                "noNamedObjectMetalComboBoxButton");
    }

    public static JavaGuiElement noNamedObjectCellRendererPane(){
        return new JavaGuiElement(window, By
                .byClass("CellRendererPane"),
                "noNamedObjectCellRendererPane");
    }

    public static JavaGuiElement noNamedObjectJTextArea(){
        return new JavaGuiElement(window, By
                .byClass("JTextArea"),
                "noNamedObjectJTextArea");
    }

    public static JavaGuiElement mainclass(){
        return new JavaGuiElement(window, By
                .byExactText("<Main class>"),
                "mainclass");
    }


// Unidentified elements
// =======================


}
