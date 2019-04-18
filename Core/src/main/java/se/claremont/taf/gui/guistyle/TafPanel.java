package se.claremont.taf.gui.guistyle;

import se.claremont.taf.gui.Gui;
import se.claremont.taf.support.StringManagement;

import javax.swing.*;

public class TafPanel extends JPanel {

    public TafPanel(String name){
        String usableName = StringManagement.methodNameWithOnlySafeCharacters(StringManagement.firstUpperLetterTrailingLowerLetter(name));
        if(!usableName.toLowerCase().endsWith("panel"))
            usableName += "Panel";
        this.setName(usableName);
        this.setBackground(Gui.colorTheme.backgroundColor);
        this.setForeground(Gui.colorTheme.textColor);
    }
}
