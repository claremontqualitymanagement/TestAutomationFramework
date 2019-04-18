package se.claremont.taf.javasupport.interaction;

import se.claremont.taf.javasupport.interaction.elementidentification.By;
import se.claremont.taf.javasupport.objectstructure.JavaGuiElement;
import se.claremont.taf.javasupport.objectstructure.JavaWindow;

/**
 * TAF GUI description for test application
 *
 * Created by jordam on 2017-02-19.
 */
@SuppressWarnings("WeakerAccess")
public class JavaTestApplication {

    public static JavaWindow window(){
        return new JavaWindow("Java.*est.*pplication");
    }

    public static JavaGuiElement panel0() {
        return new JavaGuiElement(By.byName("panel0"), "panel0_panel");
    }

    public static JavaGuiElement textField(){
        return new JavaGuiElement(By.byName("Text field"), "textField_jtextfield");
    }

    public static JavaGuiElement textfield2() {
        return new JavaGuiElement(By.byName("Textfield2"), "textfield2_textfield");
    }

    public static JavaGuiElement okbutton() {
        return new JavaGuiElement(By.byName("OkButton"), "okbutton_jbutton");
    }

    public static JavaGuiElement checkboxAwt() {
        return new JavaGuiElement(By.byName("Checkbox awt"), "checkboxAwt_checkbox");
    }

    public static JavaGuiElement checkboxSwing() {
        return new JavaGuiElement(By.byName("Checkbox swing"), "checkboxSwing_jcheckbox");
    }

    public static JavaGuiElement cancel() {
        return new JavaGuiElement(By.byName("Cancel"), "cancel_button");
    }

}