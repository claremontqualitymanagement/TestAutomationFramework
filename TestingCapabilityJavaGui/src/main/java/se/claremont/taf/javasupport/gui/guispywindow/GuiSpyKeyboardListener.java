package se.claremont.taf.javasupport.gui.guispywindow;

import se.claremont.taf.core.support.SupportMethods;
import se.claremont.taf.javasupport.gui.ObjectToHtmlHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class GuiSpyKeyboardListener implements KeyListener {

    JFrame parentWindow;

    GuiSpyKeyboardListener(JFrame parentWindow){
        this.parentWindow = parentWindow;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_I) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

            StringSelection stringSelection = new StringSelection(GuiSpyingWindow.elementProgramaticDescriptionFormattedForClipboard);
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);
            System.out.println("Copied text to clipboard.");

        } else if((e.getKeyCode() == KeyEvent.VK_D) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){

            StringSelection stringSelection = new StringSelection(GuiSpyingWindow.currentElementParametersForClipboard);
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);
            System.out.println("Copied text to clipboard.");

        } else if((e.getKeyCode() == KeyEvent.VK_K) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){

            if(GuiSpyingWindow.executionIsPaused){
                GuiSpyingWindow.executionIsPaused = false;
            } else {
                GuiSpyingWindow.executionIsPaused = true;
            }

        } else if((e.getKeyCode() == KeyEvent.VK_J) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
            Component component = GuiSpyMouseListener.currentComponent;
            if(component == null) return;
            String html = ObjectToHtmlHelper.toHtmlPage(component);
            SupportMethods.saveToFile(html, "C:\\temp\\object.html");
            try {
                Desktop.getDesktop().browse(new File("C:\\temp\\object.html").toURI());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiers() & KeyEvent.ALT_MASK) != 0)){
            parentWindow.dispose();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
