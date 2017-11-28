package se.claremont.autotest.common.gui.guistyle;

import se.claremont.autotest.common.support.StringManagement;

import javax.swing.*;

public class TafLabel extends JLabel {
    public TafLabel(String label) {
        super(label);
        this.setText(label);
        this.setForeground(TafGuiColor.textColor);
        this.setFont(AppFont.getInstance());
        this.setName(StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(StringManagement.methodNameWithOnlySafeCharacters(label)) + "Label");
    }
}
