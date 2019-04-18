package se.claremont.taf.javasupport.gui.guirecordingwindow.listeners;

import se.claremont.taf.core.gui.Gui;
import se.claremont.taf.core.gui.guistyle.TafFrame;
import se.claremont.taf.core.gui.guistyle.TafHtmlTextPane;
import se.claremont.taf.javasupport.gui.guirecordingwindow.JavaGuiElementDeclarationManager;
import se.claremont.taf.javasupport.gui.teststeps.JavaClictTestStep;
import se.claremont.taf.javasupport.objectstructure.JavaGuiElement;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RecordingMouseListener implements MouseListener {
    String returnText = null;
    TafHtmlTextPane scriptArea;

    public RecordingMouseListener(TafHtmlTextPane scriptArea) {
        this.scriptArea = scriptArea;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getComponent() == null) return;
        Component actualComponent = e.getComponent();
        if(actualComponent == null)return;
        String alternativeText = JavaGuiElementDeclarationManager.javaGuiElementAsCodeHtml(JavaGuiElementDeclarationManager.createJavaGuiElement(e.getComponent()));
        String text = "<pre>java.click(new JavaGuiElement(By.byName(\"" + actualComponent.getName() + "\")));</pre><br>" + System.lineSeparator();
        scriptArea.append(text);
        scriptArea.revalidate();
        scriptArea.repaint();
        Gui.addTestStepToListOfAvailableTestSteps(new JavaClictTestStep(new JavaGuiElement(actualComponent)));
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

    public static boolean isApplied(Component component) {
        for (MouseListener mouseListener : component.getMouseListeners()) {
            if (mouseListener.getClass().equals(RecordingMouseListener.class)) return true;
        }
        return false;
    }

    public String getReturnText() {
        return returnText;
    }

    private Component identifyComponent(Component w) {
        while (!w.getClass().getName().equals(TafFrame.class.getName())){
            w = w.getParent();
        }
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        int x = w.getLocationOnScreen().x;
        int y = w.getLocationOnScreen().y;
        Component c = w.getComponentAt(mousePosition.x - x, mousePosition.y - y);
        Component returnElement = c;
        while (c != null) {
            try {
                c = c.getComponentAt(mousePosition.x - c.getLocationOnScreen().x, mousePosition.y - c.getLocationOnScreen().y);
                if (c != null) {
                    if (c.equals(returnElement)) {
                        c = null;
                    } else {
                        returnElement = c;
                    }
                }
            }catch (IllegalStateException e){
                c = null;
            }
        }
        System.out.println(returnElement.toString());
        return returnElement;
    }

}
