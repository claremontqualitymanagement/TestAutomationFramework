package se.claremont.taf.core.gui.guistyle;

import se.claremont.taf.core.gui.Gui;
import se.claremont.taf.core.support.StringManagement;

import javax.swing.*;

class TafButtonType extends JButton{

    public TafButtonType(){
        this.setFont(AppFont.getInstance());
        this.setForeground(Gui.colorTheme.textColor);
        this.setName("TafButton");
    }

    public TafButtonType(String label){
        this.setText(label);
        this.setFont(AppFont.getInstance());
        this.setForeground(TafGuiColor.textColor);
        this.setName(StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(StringManagement.methodNameWithOnlySafeCharacters(label)) + "Button");
    }

    public TafButtonType(String label, Icon icon){
        super(label, icon);
        this.setText(label);
        this.setIcon(icon);
        this.setFont(AppFont.getInstance());
        this.setForeground(TafGuiColor.textColor);
        this.setName(StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(StringManagement.methodNameWithOnlySafeCharacters(label)) + "Button");
    }

    @Override
    public void setToolTipText(String tooltip){
        super.setToolTipText(Helper.tooltipStypeInfoHead + tooltip + Helper.tooltipStypeInfoTail);
    }

}
