package se.claremont.autotest.websupport.gui;

import se.claremont.autotest.common.gui.guistyle.TafFrame;

import javax.swing.*;

public class WebTempFrame {

    public static void main(String[] args){
        TafFrame frame = new TafFrame("WebFrame");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new WebSupportTabPanel().panel);
        frame.pack();
        frame.setVisible(true);
    }
}
