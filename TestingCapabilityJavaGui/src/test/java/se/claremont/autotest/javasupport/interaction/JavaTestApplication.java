package se.claremont.autotest.javasupport.interaction;

import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

import java.awt.*;

/**
 * Created by jordam on 2017-02-19.
 */
@SuppressWarnings("WeakerAccess")
public class JavaTestApplication {

    public static JavaWindow window(){
        return new JavaWindow("Java.*est.*pplication");
    }

    public static JavaGuiElement panel0() {
        return new JavaGuiElement("panel0_panel", "panel0", JavaGuiElement.IdType.ELEMENT_NAME);
    }

    public static JavaGuiElement textField() {
        return new JavaGuiElement("textField_jtextfield", "Text field", JavaGuiElement.IdType.ELEMENT_NAME);
    }

    public static JavaGuiElement textfield2() {
        return new JavaGuiElement("textfield2_textfield", "Textfield2", JavaGuiElement.IdType.ELEMENT_NAME);
    }

    public static JavaGuiElement okbutton() {
        return new JavaGuiElement("okbutton_jbutton", "OkButton", JavaGuiElement.IdType.ELEMENT_NAME);
    }

    public static JavaGuiElement checkboxAwt() {
        return new JavaGuiElement("checkboxAwt_checkbox", "Checkbox awt", JavaGuiElement.IdType.ELEMENT_NAME);
    }

    public static JavaGuiElement checkboxSwing() {
        return new JavaGuiElement("checkboxSwing_jcheckbox", "Checkbox swing", JavaGuiElement.IdType.ELEMENT_NAME);
    }

    public static JavaGuiElement cancel() {
        return new JavaGuiElement("cancel_button", "Cancel", JavaGuiElement.IdType.ELEMENT_NAME);
    }

}