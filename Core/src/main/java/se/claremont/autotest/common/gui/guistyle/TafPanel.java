package se.claremont.autotest.common.gui.guistyle;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.support.StringManagement;

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
