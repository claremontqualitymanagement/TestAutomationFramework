package se.claremont.autotest.javasupport.gui;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.gui.guistyle.TafFrame;
import se.claremont.autotest.common.gui.userpreferences.Preferences;
import se.claremont.autotest.common.gui.userpreferences.SavePreferencesOnCloseWindowsListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class StandaloneJavaSupportGui {

    static TafFrame window = new TafFrame();

    public static void main(String... arg){
        Gui.preferences.loadFromFile(Preferences.getPreferencesFile());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addWindowListener(new SavePreferencesOnCloseWindowsListener());
        window.getContentPane().add(new JavaSupportTab(window).panel);
        window.setSize(Toolkit.getDefaultToolkit().getScreenSize().width /2, Toolkit.getDefaultToolkit().getScreenSize().height /2);
        window.pack();
        window.setVisible(true);
    }
}
