package se.claremont.autotest.javasupport.gui.guispywindow;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DescriptionToClipboardManager implements KeyListener {

    JLabel hintText;
    JTextComponent currentElementTextcomponent;

    DescriptionToClipboardManager(JLabel hintText, JTextComponent currentElementText){
        this.hintText = hintText;
        this.currentElementTextcomponent = currentElementText;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_I) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            Color initialColor = hintText.getForeground();
            hintText.setForeground(Color.YELLOW);
            hintText.revalidate();
            hintText.repaint();
            StringSelection stringSelection = new StringSelection(currentElementTextcomponent.getText());
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);
            System.out.println("Copied text to clipboard.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            hintText.setForeground(initialColor);
            hintText.revalidate();
            hintText.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
