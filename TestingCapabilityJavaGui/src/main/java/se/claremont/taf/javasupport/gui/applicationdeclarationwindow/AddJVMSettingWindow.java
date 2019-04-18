package se.claremont.taf.javasupport.gui.applicationdeclarationwindow;

import se.claremont.taf.gui.guistyle.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddJVMSettingWindow {
    TafDialog frame;
    TafTextField textComponentToUpdate;
    TafTextField propertyNameText = new TafTextField(" < JVM property name > ");
    TafTextField propertyValueText = new TafTextField(" < JVM property value > ");
    TafButton saveButton = new TafButton("Apply");

    public AddJVMSettingWindow(TafTextField textComponentToUpdate, JFrame parentWindow){

        frame = new TafDialog(parentWindow, "TAF - Add JVM property", true);

        this.textComponentToUpdate = textComponentToUpdate;

        frame.getContentPane().setLayout(new BorderLayout());

        TafHeadline headline = new TafHeadline("Add JVM property");

        TafLabel warningText = new TafLabel("Warning: JVM properties are global. Only a few actually can be edited at runtime, but once applied it affects all java applications in JVM - including TAF.");

        TafPanel propertiesPanel = new TafPanel("PropertiesPanel");
        propertiesPanel.setLayout(new GridLayout(3, 2));

        TafLabel propertyNameLabel = new TafLabel("JVM property name");
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

        TafLabel propertyValueLabel = new TafLabel("JVM property value");
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
