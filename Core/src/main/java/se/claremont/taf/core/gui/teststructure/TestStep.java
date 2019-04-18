package se.claremont.taf.core.gui.teststructure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.claremont.taf.core.gui.guistyle.TafLabel;
import se.claremont.taf.core.gui.guistyle.TafPanel;
import se.claremont.taf.core.gui.guistyle.TafTextField;
import se.claremont.taf.core.testcase.TestCase;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.Serializable;

public abstract class TestStep implements Serializable {
    @JsonIgnore private TafPanel panel;
    @JsonProperty private String description;
    @JsonProperty private String name;
    private Object runTimeElement;
    @JsonIgnore private TafTextField component = new TafTextField(" < initial default name > ");
    @JsonProperty
    public String actionName;
    @JsonProperty
    public String elementName;
    @JsonProperty
    public Object data;
    @JsonIgnore TestCase testCase;

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

    public abstract String asCode();

    public abstract TestStep clone();

    public void assignTestCase(TestCase testCase){
        this.testCase = testCase;
    }

    public TestCase getTestCase() {
        return testCase;
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

    public Object getAssociatedData(){
        return data;
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

    @Override
    public String toString(){
        return getTestStepTypeShortName() + " " + name;
    }

    public Object getRunTimeElement() {
        return runTimeElement;
    }

    public void setRunTimeElement(Object runTimeElement) {
        this.runTimeElement = runTimeElement;
    }
}
