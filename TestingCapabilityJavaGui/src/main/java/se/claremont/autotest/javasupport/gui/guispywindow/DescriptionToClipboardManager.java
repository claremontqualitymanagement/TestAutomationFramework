package se.claremont.autotest.javasupport.gui.guispywindow;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DescriptionToClipboardManager implements KeyListener {


    DescriptionToClipboardManager(){
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
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
