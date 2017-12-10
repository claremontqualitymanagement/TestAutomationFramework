package se.claremont.autotest.javasupport.gui.guispywindow;

import com.sun.jndi.toolkit.url.Uri;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.javasupport.gui.Helper;
import se.claremont.autotest.javasupport.gui.JavaSupportTab;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.URI;

public class GuiSpyKeyboardListener implements KeyListener {


    GuiSpyKeyboardListener(){
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
            String html = Helper.toHtmlPage(component);
            SupportMethods.saveToFile(html, "C:\\temp\\object.html");
            try {
                Desktop.getDesktop().browse(new File("C:\\temp\\object.html").toURI());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
