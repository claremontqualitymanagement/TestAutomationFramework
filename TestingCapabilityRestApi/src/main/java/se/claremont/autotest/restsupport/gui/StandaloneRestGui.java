package se.claremont.autotest.restsupport.gui;

import se.claremont.autotest.common.gui.guistyle.TafFrame;

import javax.swing.*;

public class StandaloneRestGui {

    public static void main (String[] args){
        TafFrame frame = new TafFrame("RestWindow");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new RestSupportTabPanel().tafPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
