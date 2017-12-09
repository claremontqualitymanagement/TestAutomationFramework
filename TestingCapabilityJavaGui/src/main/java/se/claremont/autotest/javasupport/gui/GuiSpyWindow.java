package se.claremont.autotest.javasupport.gui;

import se.claremont.autotest.common.gui.guistyle.TafCloseButton;
import se.claremont.autotest.common.gui.guistyle.TafDialog;
import se.claremont.autotest.common.gui.guistyle.TafLabel;

import javax.swing.*;
import java.awt.*;

public class GuiSpyWindow {
    TafDialog dialog;
    TafCloseButton closeButton = new TafCloseButton(dialog);
    TafLabel headline = new TafLabel("Rich Java GUI recording");

    public GuiSpyWindow(JFrame parentWindow){
        this.dialog = new TafDialog(parentWindow, "TAF - GUI Spy", true);


    }
}
