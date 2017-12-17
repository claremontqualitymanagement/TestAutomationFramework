package se.claremont.autotest.eyeautomatesupport.gui;

import se.claremont.autotest.common.gui.guistyle.TafFrame;

import javax.swing.*;

public class StandaloneSmartImageRecognitionSupportGui {

    public static void main(String[] args){
        TafFrame frame = new TafFrame("TAF - Smart image recognition by EyeAutomate");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new SmartImageRecognitionSupportTabPanel().panel);
        frame.pack();
        frame.setVisible(true);
    }
}
