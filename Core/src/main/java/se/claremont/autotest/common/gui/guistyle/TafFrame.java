package se.claremont.autotest.common.gui.guistyle;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.gui.abouttab.AboutTabPanel;
import se.claremont.autotest.common.support.StringManagement;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class TafFrame extends JFrame {

    public TafFrame(String title){
        this.setTitle(title);
        String usableName = StringManagement.methodNameWithOnlySafeCharacters(StringManagement.firstUpperLetterTrailingLowerLetter(title));
        if(!usableName.toLowerCase().endsWith("frame"))
            usableName += "Frame";
        this.setName(usableName);
        this.setBackground(Gui.colorTheme.backgroundColor);
        this.setForeground(Gui.colorTheme.textColor);
        this.getContentPane().setBackground(Gui.colorTheme.backgroundColor);
        this.getRootPane().setBackground(Gui.colorTheme.backgroundColor);
        AboutTabPanel.openFrames.add(this);
        addListenerForWhenClosed();
    }

    public TafFrame(){
        this.setTitle("TAF");
        this.setName("TafFrame");
        this.setForeground(Gui.colorTheme.textColor);
        this.setBackground(Gui.colorTheme.backgroundColor);
        this.getContentPane().setBackground(Gui.colorTheme.backgroundColor);
        this.getRootPane().setBackground(Gui.colorTheme.backgroundColor);
        AboutTabPanel.openFrames.add(this);
        addListenerForWhenClosed();
    }

    private void addListenerForWhenClosed(){
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                AboutTabPanel.openFrames.remove(this);
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
        });

    }
}
