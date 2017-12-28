package se.claremont.autotest.common.gui.guistyle;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.support.StringManagement;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
