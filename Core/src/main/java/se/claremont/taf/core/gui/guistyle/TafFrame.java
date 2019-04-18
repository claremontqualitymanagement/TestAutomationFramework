package se.claremont.taf.core.gui.guistyle;

import se.claremont.taf.core.gui.Gui;
import se.claremont.taf.core.gui.abouttab.AboutTabPanel;
import se.claremont.taf.core.support.StringManagement;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class TafFrame extends JFrame {

    public TafFrame(String title){
        this.setTitle(title);
        setIconImage(new ImageIcon(ClassLoader.getSystemResource(
                "icon.png")).getImage());
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
        setIconImage(new ImageIcon(ClassLoader.getSystemResource(
                "icon.png")).getImage());
        this.setForeground(Gui.colorTheme.textColor);
        this.setBackground(Gui.colorTheme.backgroundColor);
        this.getContentPane().setBackground(Gui.colorTheme.backgroundColor);
        this.getRootPane().setBackground(Gui.colorTheme.backgroundColor);
        AboutTabPanel.openFrames.add(this);
        addListenerForWhenClosed();
    }

    private TafFrame getThis(){
        return this;
    }

    private void addListenerForWhenClosed(){
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                AboutTabPanel.openFrames.remove(this);
                getThis().setVisible(true);
                getThis().dispose();
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
