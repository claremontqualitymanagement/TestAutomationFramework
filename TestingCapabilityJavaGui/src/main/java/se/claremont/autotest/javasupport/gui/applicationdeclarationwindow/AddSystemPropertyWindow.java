package se.claremont.autotest.javasupport.gui.applicationdeclarationwindow;

import se.claremont.autotest.common.gui.guistyle.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddSystemPropertyWindow {
    TafDialog frame;
    TafTextField textComponentToUpdate;
    TafTextField propertyNameText = new TafTextField(" < property name > ");
    TafTextField propertyValueText = new TafTextField(" < property value > ");
    TafButton saveButton = new TafButton("Save");

    public AddSystemPropertyWindow(TafTextField component, JFrame parentWindow){

        frame = new TafDialog(parentWindow, "TAF - Add system property", true);

        this.textComponentToUpdate = component;

        frame.getContentPane().setLayout(new BorderLayout());

        TafHeadline headline = new TafHeadline("Add system property");

        TafLabel warningText = new TafLabel("Warning: System properties are global. Once applied it affects all java applications in JVM - including TAF.");

        TafPanel propertiesPanel = new TafPanel("PropertiesPanel");
        propertiesPanel.setLayout(new GridLayout(3, 2));

        TafLabel propertyNameLabel = new TafLabel("Property name");
        propertyNameLabel.setLabelFor(propertyNameText);
        propertyNameText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                enableSaveButtonIfBothNameAndValueIsChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                enableSaveButtonIfBothNameAndValueIsChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                enableSaveButtonIfBothNameAndValueIsChanged();
            }
        });
        propertiesPanel.add(propertyNameLabel);
        propertiesPanel.add(propertyNameText);

        TafLabel propertyValueLabel = new TafLabel("Property value");
        propertyValueText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                enableSaveButtonIfBothNameAndValueIsChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                enableSaveButtonIfBothNameAndValueIsChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                enableSaveButtonIfBothNameAndValueIsChanged();
            }
        });
        propertyValueLabel.setLabelFor(propertyValueText);
        propertiesPanel.add(propertyValueLabel);
        propertiesPanel.add(propertyValueText);

        TafCloseButton cancelButton = new TafCloseButton(frame);
        cancelButton.setText("Cancel");
        propertiesPanel.add(cancelButton);

        saveButton.setEnabled(false);
        propertiesPanel.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!textComponentToUpdate.isChangedFromDefault()){
                    textComponentToUpdate.setText(propertyNameText.getText() + "=" + propertyValueText.getText());
                } else {
                    textComponentToUpdate.setText(textComponentToUpdate.getText() + ", " + propertyNameText.getText() + "=" + propertyValueText.getText());
                }
                textComponentToUpdate.revalidate();
                textComponentToUpdate.repaint();
                frame.dispose();
            }
        });

        frame.getContentPane().add(headline, BorderLayout.PAGE_START);
        frame.getContentPane().add(warningText, BorderLayout.CENTER);
        frame.getContentPane().add(propertiesPanel, BorderLayout.PAGE_END);

        frame.pack();
        frame.setVisible(true);
    }

    private void enableSaveButtonIfBothNameAndValueIsChanged() {
        if(propertyNameText.isChangedFromDefault() && propertyValueText.isChangedFromDefault()){
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }
    }
}
