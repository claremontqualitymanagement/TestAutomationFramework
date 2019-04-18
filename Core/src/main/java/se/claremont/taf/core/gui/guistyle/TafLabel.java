package se.claremont.taf.core.gui.guistyle;

import se.claremont.taf.core.support.StringManagement;

import javax.swing.*;

public class TafLabel extends JLabel {

    public TafLabel(String label) {
        super(label);
        this.setText(label);
        this.setForeground(TafGuiColor.textColor);
        this.setFont(AppFont.getInstance());
        this.setName(StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(StringManagement.methodNameWithOnlySafeCharacters(label)) + "Label");
    }

    @Override
    public void setToolTipText(String tooltip){
        super.setToolTipText(Helper.tooltipStypeInfoHead + tooltip + Helper.tooltipStypeInfoTail);
    }

}

