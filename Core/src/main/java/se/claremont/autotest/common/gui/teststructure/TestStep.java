package se.claremont.autotest.common.gui.teststructure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.claremont.autotest.common.gui.guistyle.TafLabel;
import se.claremont.autotest.common.gui.guistyle.TafPanel;
import se.claremont.autotest.common.gui.guistyle.TafTextField;

import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public abstract class TestStep {
    @JsonIgnore private TafPanel panel;
    @JsonProperty private String description;
    @JsonProperty private String name;
    @JsonIgnore private TafTextField component = new TafTextField(" < initial default name > ");
    @JsonProperty
    public String actionName;
    @JsonProperty
    public String elementName;
    @JsonProperty
    public Object data;

    public TestStep(){}

    public TestStep(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setDescription(String description){
        this.description = description;
        this.component.setToolTipText(description);
    }

    public void setName(String name){
        component.setText(name);
        this.name = name;
    }

    public void setActionName(String actionName){
        this.actionName = actionName;
    }

    public void setElementName(String elementName){
        this.elementName = elementName;
    }

    public void setAssociatedData(Object data){
        this.data = data;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public abstract String getTestStepTypeShortName();

    public abstract TestStepResult execute();

    public Component guiComponent() {
        panel = new TafPanel(name + "Panel");
        panel.add(new TafLabel(getTestStepTypeShortName()));
        component.setText(name);
        component.setToolTipText(description);
        component.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateName();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateName();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateName();
            }
        });
        panel.add(component);
        return panel;
    }

    private void updateName() {
        this.name = component.getText();
    }
}
