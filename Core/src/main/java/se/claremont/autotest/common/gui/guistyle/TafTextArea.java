package se.claremont.autotest.common.gui.guistyle;

import se.claremont.autotest.common.support.StringManagement;

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
}
