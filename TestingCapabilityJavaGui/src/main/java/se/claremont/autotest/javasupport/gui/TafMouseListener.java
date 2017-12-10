package se.claremont.autotest.javasupport.gui;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TafMouseListener implements MouseListener {
    String returnText = null;
    JTextComponent textComponent;

    public TafMouseListener(JTextComponent textComponent){
        this.textComponent = textComponent;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getComponent() == null)return;
        String text = "java.click(new JavaGuiElement(By.byName(\"" + e.getComponent().getName() + "\");";
        returnText = text;
        System.out.println(text);
        textComponent.setText(textComponent.getText() + text);
        textComponent.revalidate();
        textComponent.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public static boolean isApplied(Component component){
        for(MouseListener mouseListener : component.getMouseListeners()){
            if(mouseListener.getClass().equals(TafMouseListener.class)) return true;
        }
        return false;
    }

    public String getReturnText(){
        return returnText;
    }
}
