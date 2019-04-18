package se.claremont.taf.core.gui.guistyle.lookandfeel;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

public class TafLookAndFeel extends LookAndFeel {

    //public static Color backgroundColor = new Color(150, 192, 230); //Light blue
    public static final ColorUIResource backgroundColor = new ColorUIResource(Color.white);

    //public static Color textColor = new Color(104, 102, 99); //Dark grey
    public static final ColorUIResource textColor = new ColorUIResource(119, 150, 178); //Dark blue


    //public static Color disabledColor = new Color(119, 150, 178); //Dark blue
    public static final ColorUIResource disabledColor = new ColorUIResource(150, 192, 230); //Light blue

    public TafLookAndFeel(){
    }

    @Override
    public String getName() {
        return "Taf";
    }

    @Override
    public String getID() {
        return "Taf";
    }

    @Override
    public String getDescription() {
        return "TAF GUI Swing Look and Feel";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return true;
    }
}
