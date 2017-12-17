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
    static Component currentComponent;

    GuiSpyMouseListener(JTextComponent updateComponent, JPanel elementPropertiesPanel){
        this.textComponent = updateComponent;
        this.elementPropertiesPanel = elementPropertiesPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //currentComponent = e.getComponent();
        //System.out.println("java.click(new JavaGuiElement(By.Name(\"" + currentComponent.getName() + "\"), \"" + currentComponent.getName() + e.getComponent().getClass().getSimpleName() + "\");");
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(GuiSpyingWindow.executionIsPaused) return;
        currentComponent = e.getComponent();
        textComponent.setText(componentDeclarationString(currentComponent));
        textComponent.revalidate();
        textComponent.repaint();
        updatePropertiesPanel(currentComponent);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(GuiSpyingWindow.executionIsPaused) return;
        currentComponent = null;
        textComponent.setText("");
    }

    static boolean guiSpyMouseListenerIsAdded(Component component){
        for(MouseListener mouseListener : component.getMouseListeners()){
            if(mouseListener.getClass().equals(GuiSpyMouseListener.class)) return true;
        }
        return false;
    }

    GridBagConstraints constraints = new GridBagConstraints();

    private void updatePropertiesPanel(Component c){
        int propertiesCount = 4;
        StringBuilder parameterTextForClipbard = new StringBuilder();

        GridBagLayout gridBag = new GridBagLayout();
        elementPropertiesPanel.setLayout(gridBag);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        elementPropertiesPanel.removeAll();

        addPropertyName("Class");
        parameterTextForClipbard.append("Class=").append(c.getClass().getName()).append(System.lineSeparator());
        addPropertyValueAsLabel(c.getClass().getName());
        propertiesPanelRowCount++;

        String text = null;
        MethodInvoker m = new MethodInvoker();
        try{
            text = (String)m.invokeTheFirstEncounteredMethod(c, MethodDeclarations.textGettingMethodsInAttemptOrder);
        }catch (Exception ignored){}
        addPropertyName("Text");
        TafTextField textProperty = new TafTextField(" < No text identified > ");
        if(text != null){
            textProperty.setText(text);
        }
        setGridBagConstraintsForPropertyValue();
        parameterTextForClipbard.append("Text=").append(text).append(System.lineSeparator());
        elementPropertiesPanel.add(textProperty, constraints);
        propertiesPanelRowCount++;

        addPropertyName("Enabled");
        addPropertyValueAsLabel(String.valueOf(c.isEnabled()));
        parameterTextForClipbard.append("Enabled=").append(String.valueOf(c.isEnabled())).append(System.lineSeparator());
        propertiesPanelRowCount++;

        TafTextField name = new TafTextField(" < No element name set > ");
        if(c.getName() != null && c.getName().length() > 0) name.setText(c.getName());
        addPropertyName("Name");
        setGridBagConstraintsForPropertyValue();
        parameterTextForClipbard.append("Name=").append(c.getName()).append(System.lineSeparator());
        elementPropertiesPanel.add(name, constraints);
        propertiesPanelRowCount++;

        GuiSpyingWindow.currentElementParametersForClipboard = parameterTextForClipbard.toString();
        elementPropertiesPanel.revalidate();
        elementPropertiesPanel.repaint();

        //elementPropertiesPanel.setLayout(new GridLayout(propertiesCount, 2));
    }

    private int propertiesPanelRowCount = 0;

    private void setGridBagConstraintsForPropertyValue(){
        constraints.gridx = 1;
        constraints.gridy = propertiesPanelRowCount;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
    }

    private void addPropertyName(String name){
        constraints.gridx = 0;
        constraints.gridy = propertiesPanelRowCount;
        constraints.weightx = 0;
        constraints.ipadx = 50;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        elementPropertiesPanel.add(new TafLabel(name), constraints);
    }

    private void addPropertyValueAsLabel(String value){
        constraints.gridx = 1;
        constraints.gridy = propertiesPanelRowCount;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        elementPropertiesPanel.add(new TafLabel(value), constraints);
    }

    private String componentDeclarationString(Component c) {
        if (c == null) return null;
        String elementName = "Noname";
        StringBuilder htmlDescriptionForGui = new StringBuilder();

        htmlDescriptionForGui.append("      ").append(".byClassName(\"").append(c.getClass().getSimpleName()).append("\")<br>");
        if (c.getName() != null && c.getName().length() > 0) {
            htmlDescriptionForGui.append("      .andByName(\"").append(c.getName()).append("\")<br>");
            elementName = c.getName();
        }
        String text = null;
        try {
            text = (String) se.claremont.autotest.javasupport.interaction.MethodInvoker.invokeMethod(null, c, "getText", null);
        } catch (Exception ignored) {
        }
        if (text != null && text.length() > 0) {
            htmlDescriptionForGui.append("      .andByExactText(\"").append(text).append("\")<br>");
        }
        GuiSpyingWindow.elementProgramaticDescriptionFormattedForClipboard = "   public static JavaGuiElement " + elementName + " = new JavaGuiElement(By" + System.lineSeparator() + htmlDescriptionForGui.toString().replace("<br>", System.lineSeparator()) + "   );" + System.lineSeparator();
        return ("<html><body><div style=\"white-space: pre; font-size: " + (AppFont.getInstance().getSize() * 2 / 3) + "; color:darkgrey; \">   public static JavaGuiElement " + elementName + " = new JavaGuiElement(By<br>" + htmlDescriptionForGui.toString() + "   );</div></body></html>").replace(" ", "&nbsp;");
    }

}
