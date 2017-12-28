package se.claremont.autotest.restsupport.gui;

import se.claremont.autotest.common.gui.guistyle.TafFrame;

import javax.swing.*;

public class StandaloneRestSupportGui {
    static TafFrame frame;

    public static void main (String[] args){
        frame = new TafFrame("RestWindow");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new RestSupportTabPanel().tafPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
