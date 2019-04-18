package se.claremont.taf.core.gui.userpreferences;

import se.claremont.taf.core.gui.Gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class SavePreferencesOnCloseWindowsListener implements WindowListener {
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("Saving user preferences.");
        Gui.preferences.saveToFile(Preferences.getPreferencesFile());
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
