package se.claremont.taf.websupport.gui;

import se.claremont.taf.gui.guistyle.TafFrame;

import javax.swing.*;

public class StandaloneWebSupportGui {

    static TafFrame frame = null;

    public static void main(String[] args){
        frame = new TafFrame("WebFrame");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new WebSupportTabPanel().panel);
        frame.pack();
        frame.setVisible(true);
    }
}
