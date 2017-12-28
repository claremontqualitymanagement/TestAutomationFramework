package se.claremont.autotest.common.gui.guistyle;

import se.claremont.autotest.common.support.StringManagement;

import javax.swing.*;

public class TafCheckbox extends JCheckBox {

    public TafCheckbox(String text){
        super(text);
        setName(StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(text));
        setFont(AppFont.getInstance());
        setForeground(TafGuiColor.textColor);
        setBackground(TafGuiColor.backgroundColor);
    }

    @Override
    public void setToolTipText(String tooltip){
        super.setToolTipText(Helper.tooltipStypeInfoHead + tooltip + Helper.tooltipStypeInfoTail);
    }

}
