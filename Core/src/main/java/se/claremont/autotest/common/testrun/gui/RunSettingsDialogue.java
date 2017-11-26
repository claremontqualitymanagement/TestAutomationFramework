package se.claremont.autotest.common.testrun.gui;

import se.claremont.autotest.common.testrun.TestRun;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunSettingsDialogue {

    JDialog window = new JDialog();
    private Font appFont;
    RunTestTabPanel mainWindow;

    public RunSettingsDialogue(RunTestTabPanel mainWindow){
        this.mainWindow = mainWindow;
        createWindow();
    }

    private void createWindow(){
        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                mainWindow.updateCliCommandText("");
            }
        });
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setFontSize();
        Container pane = window.getContentPane();
        int numberOfParameters = TestRun.getSettings().size();
        pane.setLayout(new GridLayout(numberOfParameters + 2, 2));
        for(String key : TestRun.getSettings().keySet()){
            JLabel parameterName = new JLabel(key);
            parameterName.setFont(appFont);
            pane.add(parameterName);
            JTextField parameterValue = new JTextField(TestRun.getSettings().get(key));
            parameterValue.setFont(appFont);
            parameterValue.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    TestRun.setCustomSettingsValue(key, parameterValue.getText());
                }

                public void removeUpdate(DocumentEvent e) {
                    TestRun.setCustomSettingsValue(key, parameterValue.getText());
                }

                public void insertUpdate(DocumentEvent e) {
                    TestRun.setCustomSettingsValue(key, parameterValue.getText());
                }
            });
            pane.add(parameterValue);
        }
        JButton addValueButton = new JButton("Add parameter");
        addValueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewValue();
            }
        });
        addValueButton.setFont(appFont);
        /*
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose();
            }
        });
        closeButton.setFont(appFont);
        */
        pane.add(addValueButton);
        //pane.add(closeButton);
        window.pack();

        window.setVisible(true);
    }

    private void setNewValue(){
        JFrame newParameterDialogue = new JFrame();
        newParameterDialogue.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container pane = newParameterDialogue.getContentPane();
        JLabel parameterNameLabel = new JLabel("Parameter name");
        parameterNameLabel.setFont(appFont);
        JTextField parameterNameText = new JTextField();
        parameterNameText.setFont(appFont);
        JLabel parameterValueLabel = new JLabel("Parameter value");
        parameterValueLabel.setFont(appFont);
        JTextField parameterValueText = new JTextField();
        parameterValueText.setFont(appFont);
        JButton saveButton = new JButton("Save");
        saveButton.setFont(appFont);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestRun.setCustomSettingsValue(parameterNameText.getText(), parameterValueText.getText());
                window.getContentPane().removeAll();
                createWindow();
                newParameterDialogue.dispose();
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(appFont);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newParameterDialogue.dispose();
            }
        });
        pane.setLayout(new GridLayout(3,2 ));
        pane.add(parameterNameLabel);
        pane.add(parameterValueLabel);
        pane.add(parameterNameText);
        pane.add(parameterValueText);
        pane.add(cancelButton);
        pane.add(saveButton);
        newParameterDialogue.pack();
        newParameterDialogue.setVisible(true);
    }

    private void setFontSize() {
        appFont = new Font("serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().height / 50);
    }

}
