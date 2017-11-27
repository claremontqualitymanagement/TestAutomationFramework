package se.claremont.autotest.common.gui;

import javax.swing.*;

public class TafButton extends JButton {

    public TafButton(String label){
        this.setText(label);
        this.setFont(Style.AppFont.getInstance());
        this.setName(label.replace(" ", "") + "Button");
    }
}
