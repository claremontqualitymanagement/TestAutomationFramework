package se.claremont.autotest.javasupport.gui.guispywindow;

import se.claremont.autotest.common.gui.guistyle.AppFont;
import se.claremont.autotest.common.gui.guistyle.TafLabel;
import se.claremont.autotest.common.gui.guistyle.TafTextField;
import se.claremont.autotest.javasupport.interaction.MethodDeclarations;
import se.claremont.autotest.javasupport.interaction.MethodInvoker;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GuiSpyMouseListener implements MouseListener {

    private final JTextComponent textComponent;
    private final JPanel elementPropertiesPanel;

    GuiSpyMouseListener(JTextComponent updateComponent, JPanel elementPropertiesPanel){
        this.textComponent = updateComponent;
        this.elementPropertiesPanel = elementPropertiesPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        textComponent.setText(componentDeclarationString(e.getComponent()));
        updatePropertiesPanel(e.getComponent());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        textComponent.setText("");
    }

    static boolean guiSpyMouseListenerIsAdded(Component component){
        for(MouseListener mouseListener : component.getMouseListeners()){
            if(mouseListener.getClass().equals(GuiSpyMouseListener.class)) return true;
        }
        return false;
    }

    private void updatePropertiesPanel(Component c){
        int propertiesCount = 4;
        elementPropertiesPanel.removeAll();

        elementPropertiesPanel.add(new TafLabel("Class"));
        elementPropertiesPanel.add(new TafLabel(c.getClass().getName()));

        String text = null;
        MethodInvoker m = new MethodInvoker();
        try{
            text = (String)m.invokeTheFirstEncounteredMethod(c, MethodDeclarations.textGettingMethodsInAttemptOrder);
        }catch (Exception ignored){}
        elementPropertiesPanel.add(new TafLabel("Text"));
        TafTextField textProperty = new TafTextField(" < No text identified > ");
        if(text != null){
            textProperty.setText(text);
        }
        elementPropertiesPanel.add(textProperty);

        elementPropertiesPanel.add(new TafLabel("Enabled"));
        elementPropertiesPanel.add(new TafLabel(String.valueOf(c.isEnabled())));

        elementPropertiesPanel.add(new TafLabel("Name"));
        TafTextField name = new TafTextField(" < No element name set > ");
        if(c.getName() != null && c.getName().length() > 0) name.setText(c.getName());
        elementPropertiesPanel.add(name);

        elementPropertiesPanel.setLayout(new GridLayout(propertiesCount, 2));
        elementPropertiesPanel.revalidate();
        elementPropertiesPanel.repaint();
    }

    private String componentDeclarationString(Component c) {
        if (c == null) return null;
        String elementName = "Noname";
        StringBuilder sb = new StringBuilder();

        sb.append("      ").append(".byClassName(\"").append(c.getClass().getSimpleName()).append("\")<br>");
        if (c.getName() != null && c.getName().length() > 0) {
            sb.append("      .andByName(\"").append(c.getName()).append("\")<br>");
            elementName = c.getName();
        }
        String text = null;
        try {
            text = (String) se.claremont.autotest.javasupport.interaction.MethodInvoker.invokeMethod(null, c, "getText", null);
        } catch (Exception ignored) {
        }
        if (text != null && text.length() > 0) {
            sb.append("      .andByExactText(\"").append(text).append("\")<br>");
        }
        return "<html><body><div style=\"white-space: pre; font-size: " + (AppFont.getInstance().getSize() * 2 / 3) + "; color:darkgrey; \">   public static JavaGuiElement " + elementName + " = new JavaGuiElement(By<br>" + sb.toString() + "   );</div></body></html>";
    }

}
