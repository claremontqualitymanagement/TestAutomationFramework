package se.claremont.taf.javasupport.gui;

import se.claremont.taf.gui.Gui;
import se.claremont.taf.gui.guistyle.TafFrame;
import se.claremont.taf.gui.userpreferences.Preferences;
import se.claremont.taf.gui.userpreferences.SavePreferencesOnCloseWindowsListener;

import javax.swing.*;
import java.awt.*;

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
