package se.claremont.taf.core.gui.guistyle;

import se.claremont.taf.core.gui.Gui;
import se.claremont.taf.core.support.StringManagement;

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
