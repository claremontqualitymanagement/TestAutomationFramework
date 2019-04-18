package se.claremont.taf.core.gui.guistyle;

import se.claremont.taf.core.gui.Gui;
import se.claremont.taf.core.gui.abouttab.AboutTabPanel;
import se.claremont.taf.core.support.StringManagement;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class TafDialog extends JDialog {

    public TafDialog(JDialog parentDialog, String title, boolean modal) {
        super(parentDialog, title, modal);
        setIconImage(new ImageIcon(ClassLoader.getSystemResource(
                "icon.png")).getImage());
        if (!title.startsWith("TAF - "))
            this.setTitle("TAF - " + title);
        String usableName = StringManagement.methodNameWithOnlySafeCharacters(StringManagement.firstUpperLetterTrailingLowerLetter(title));
        this.getContentPane().setBackground(Gui.colorTheme.backgroundColor);
        this.getContentPane().setForeground(Gui.colorTheme.textColor);
        this.getRootPane().setBackground(Gui.colorTheme.backgroundColor);
        this.getRootPane().setForeground(Gui.colorTheme.textColor);
        if (!usableName.toLowerCase().endsWith("dialog"))
            usableName += "Dialog";
        this.setName(usableName);
        this.setBackground(Gui.colorTheme.backgroundColor);
        this.setForeground(Gui.colorTheme.textColor);
        AboutTabPanel.openDialogs.add(this);
        addListenerForWhenClosed();
    }

    public TafDialog(JFrame parentFrame, String title, boolean modal) {
        super(parentFrame, title, modal);
        setIconImage(new ImageIcon(ClassLoader.getSystemResource(
                "icon.png")).getImage());
        if (!title.startsWith("TAF - "))
            this.setTitle("TAF - " + title);
        String usableName = StringManagement.methodNameWithOnlySafeCharacters(StringManagement.firstUpperLetterTrailingLowerLetter(title));
        this.getContentPane().setBackground(Gui.colorTheme.backgroundColor);
        this.getContentPane().setForeground(Gui.colorTheme.textColor);
        this.getRootPane().setBackground(Gui.colorTheme.backgroundColor);
        this.getRootPane().setForeground(Gui.colorTheme.textColor);
        this.setLocationRelativeTo(parentFrame);
        if (!usableName.toLowerCase().endsWith("dialog"))
            usableName += "Dialog";
        this.setName(usableName);
        this.setBackground(Gui.colorTheme.backgroundColor);
        this.setForeground(Gui.colorTheme.textColor);
        AboutTabPanel.openDialogs.add(this);
        addListenerForWhenClosed();
    }

    public TafDialog() {
        this.setTitle("TAF");
        this.setName("TafFrame");
        this.getContentPane().setBackground(Gui.colorTheme.backgroundColor);
        this.getContentPane().setForeground(Gui.colorTheme.textColor);
        this.getRootPane().setBackground(Gui.colorTheme.backgroundColor);
        this.getRootPane().setForeground(Gui.colorTheme.textColor);
        this.setForeground(Gui.colorTheme.textColor);
        this.setBackground(Gui.colorTheme.backgroundColor);
        AboutTabPanel.openDialogs.add(this);
        addListenerForWhenClosed();
    }

    private TafDialog getThis(){
        return this;
    }

    private void addListenerForWhenClosed() {
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                AboutTabPanel.openDialogs.remove(this);
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
