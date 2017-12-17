package se.claremont.autotest.javasupport.gui.applicationdeclarationwindow;

import se.claremont.autotest.common.gui.guistyle.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddEnvironmentVariableWindow {
    TafFrame frame = new TafFrame("AddEnvironmentVariableFrame");
    TafTextField textComponentToUpdate;
    TafTextField variableNameText = new TafTextField(" < environment variable name > ");
    TafTextField variableValueText = new TafTextField(" < environment variable value > ");
    TafButton saveButton = new TafButton("Save");

    public AddEnvironmentVariableWindow(TafTextField component){

        this.textComponentToUpdate = component;

        frame.getContentPane().setLayout(new BorderLayout());

        TafHeadline headline = new TafHeadline("Add or update environment variable");

        TafLabel warningText = new TafLabel("Warning: Environment variables are global to the JVM process - including TAF.");

        TafPanel variablesPanel = new TafPanel("VariablesPanel");
        variablesPanel.setLayout(new GridLayout(3, 2));

        TafLabel variableNameLabel = new TafLabel("Environment variable name");
        variableNameLabel.setLabelFor(variableNameText);
        variableNameText.getDocument().addDocumentListener(new DocumentListener() {
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
        variablesPanel.add(variableNameLabel);
        variablesPanel.add(variableNameText);

        TafLabel variableValueLabel = new TafLabel("Variable value");
        variableValueText.getDocument().addDocumentListener(new DocumentListener() {
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
        variableValueLabel.setLabelFor(variableValueText);
        variablesPanel.add(variableValueLabel);
        variablesPanel.add(variableValueText);

        TafCloseButton cancelButton = new TafCloseButton(frame);
        cancelButton.setText("Cancel");
        variablesPanel.add(cancelButton);

        saveButton.setEnabled(false);
        variablesPanel.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!textComponentToUpdate.isChangedFromDefault()){
                    textComponentToUpdate.setText(variableNameText.getText() + "=" + variableValueText.getText());
                } else {
                    textComponentToUpdate.setText(textComponentToUpdate.getText() + ", " + variableNameText.getText() + "=" + variableValueText.getText());
                }
                textComponentToUpdate.revalidate();
                textComponentToUpdate.repaint();
                frame.dispose();
            }
        });

        frame.getContentPane().add(headline, BorderLayout.PAGE_START);
        frame.getContentPane().add(warningText, BorderLayout.CENTER);
        frame.getContentPane().add(variablesPanel, BorderLayout.PAGE_END);

        frame.pack();
        frame.setVisible(true);
    }

    private void enableSaveButtonIfBothNameAndValueIsChanged() {
        if(variableNameText.isChangedFromDefault() && variableValueText.isChangedFromDefault()){
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }
    }
}
