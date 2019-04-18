package se.claremont.taf.gui.guistyle;

import javax.swing.*;

public class TafTextArea extends JTextArea {

    public TafTextArea(String name){
        super();
        this.setName(name);
        this.setForeground(TafGuiColor.textColor);
        this.setBackground(TafGuiColor.backgroundColor);
        this.setLineWrap(true);
        this.setFont(AppFont.getInstance());
    }

    @Override
    public void setToolTipText(String tooltip){
        super.setToolTipText(Helper.tooltipStypeInfoHead + tooltip + Helper.tooltipStypeInfoTail);
    }

}
