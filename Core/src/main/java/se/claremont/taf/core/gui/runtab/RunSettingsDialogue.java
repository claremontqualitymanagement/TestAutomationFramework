package se.claremont.taf.core.gui.runtab;

import se.claremont.taf.core.gui.Gui;
import se.claremont.taf.core.gui.guistyle.*;
import se.claremont.taf.core.testrun.Settings;
import se.claremont.taf.core.testrun.TestRun;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;

public class RunSettingsDialogue {

    final TafDialog window;
    RunTestTabPanel mainWindow;
    HashMap<String, String> runSettingsWhenOpened = new HashMap<>(Gui.defaultSettings);
    Container pane;
    TafPanel runSettingsPanel = new TafPanel("SettingsValuesPanel");

    public RunSettingsDialogue(RunTestTabPanel mainWindow) {
        this.mainWindow = mainWindow;
        window = new TafDialog(Gui.applicationWindow, "RunSettingsWindow", false);
        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                mainWindow.updateCliCommandText("");
            }
        });
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setTitle("TAF - Run settings");
        pane = window.getContentPane();
        pane.setName("RunSettingsContentPanel");
        GroupLayout groupLayout = new GroupLayout(pane);
        pane.setLayout(groupLayout);
        createParametersPanel();
        TafButton loadSettingsFromFile = new TafButton("Load settings");
        loadSettingsFromFile.setMnemonic('l');
        loadSettingsFromFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser filePickerWindow = new JFileChooser();
                filePickerWindow.setName("FilePickerWindow");
                filePickerWindow.setDialogTitle("TAF - Run settings file picker");
                filePickerWindow.setFont(AppFont.getInstance());
                try {
                    filePickerWindow.setCurrentDirectory(new File(TestClassPickerDialogue.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
                int returnVal = filePickerWindow.showOpenDialog(Gui.applicationWindow);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = filePickerWindow.getSelectedFile();
                    Settings settings = new Settings(file.getAbsolutePath());
                    TestRun.setSettings(settings);
                    runSettingsPanel.removeAll();
                    createParametersPanel();
                    window.revalidate();
                    window.repaint();
                }

            }
        });

        TafButton saveSettingsToFile = new TafButton("Save settings");
        saveSettingsToFile.setMnemonic('v');
        saveSettingsToFile .addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser filePickerWindow = new JFileChooser();
                filePickerWindow.setName("FilePickerWindow");
                filePickerWindow.setDialogTitle("TAF - Save run settings file");
                filePickerWindow.setFont(AppFont.getInstance());
                try {
                    filePickerWindow.setCurrentDirectory(new File(TestClassPickerDialogue.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
                int returnVal = filePickerWindow.showSaveDialog(Gui.applicationWindow);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = filePickerWindow.getSelectedFile();
                    TestRun.getSettings().writeSettingsParametersToFile(file.getAbsolutePath());
                }

            }
        });

        TafButton addValueButton = new TafButton("Add parameter");
        addValueButton.setMnemonic('a');
        addValueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewValue();
            }
        });

        TafButton cancelButton = new TafButton("Cancel");
        cancelButton.setMnemonic('c');
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestRun.getSettings().clear();
                TestRun.getSettings().putAll(runSettingsWhenOpened);
                mainWindow.updateCliCommandText("");
                window.dispose();
            }
        });

        TafButton saveButton = new TafButton("Save");
        saveButton.setMnemonic('s');
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.updateCliCommandText("");
                window.dispose();
            }
        });

        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(runSettingsPanel)
                        .addComponent(addValueButton)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(loadSettingsFromFile)
                                .addComponent(saveSettingsToFile)
                        )
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(cancelButton)
                                .addComponent(saveButton)
                        )
        );

        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(runSettingsPanel)
                        .addComponent(addValueButton)
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(loadSettingsFromFile)
                                .addComponent(saveSettingsToFile)
                        )
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(cancelButton)
                                .addComponent(saveButton)
                        )
        );


        window.pack();
        window.setVisible(true);
    }

    private void createParametersPanel() {
        int numberOfParameters = TestRun.getSettings().size();
        runSettingsPanel.setLayout(new GridLayout(numberOfParameters, 2, 20, 5));

        for (String key : TestRun.getSettings().keySet()) {
            runSettingsPanel.add(new TafLabel(key));
            TafTextField parameterValue = new TafTextField(" <" + key + "> ");
            parameterValue.setName(key.replace(" ", "") + "Value");
            //parameterValue.setFont(AppFont.getInstance());
            //parameterValue.setForeground(Gui.colorTheme.textColor);
            if(TestRun.getSettings().get(key) != null && TestRun.getSettings().get(key).length() != 0){
                parameterValue.setText(TestRun.getSettings().get(key));
            }
            parameterValue.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    if(!parameterValue.isChangedFromDefault()) return;
                    TestRun.setCustomSettingsValue(key, parameterValue.getText());
                    mainWindow.updateCliCommandText("");
                }

                public void removeUpdate(DocumentEvent e) {
                    if(!parameterValue.isChangedFromDefault()) return;
                    TestRun.setCustomSettingsValue(key, parameterValue.getText());
                    mainWindow.updateCliCommandText("");
                }

                public void insertUpdate(DocumentEvent e) {
                    if(!parameterValue.isChangedFromDefault()) return;
                    TestRun.setCustomSettingsValue(key, parameterValue.getText());
                    mainWindow.updateCliCommandText("");
                }
            });

            runSettingsPanel.add(parameterValue);
        }

    }

    private void setNewValue() {

        final TafFrame newParameterDialogue = new TafFrame("TAF - Add run parameter");
        newParameterDialogue.setName("NewTestRunParameterWindow");
        newParameterDialogue.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container pane = newParameterDialogue.getContentPane();
        pane.setName("NewTestRunParameterContentPanel");

        TafLabel parameterNameLabel = new TafLabel("Parameter name");

        TafButton saveButton = new TafButton("Save");
        saveButton.setEnabled(false);

        TafTextField parameterNameText = new TafTextField(" < parameter name >");
        parameterNameText.setName("ParameterNameField");
        parameterNameText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(parameterNameText.isChangedFromDefault() && parameterNameText.getText().length() > 0){
                    saveButton.setEnabled(true);
                } else {
                    saveButton.setEnabled(false);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(parameterNameText.isChangedFromDefault() && parameterNameText.getText().length() > 0){
                    saveButton.setEnabled(true);
                } else {
                    saveButton.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if(parameterNameText.isChangedFromDefault() && parameterNameText.getText().length() > 0){
                    saveButton.setEnabled(true);
                } else {
                    saveButton.setEnabled(false);
                }
            }
        });

        TafLabel parameterValueLabel = new TafLabel("Parameter value");

        TafTextField parameterValueText = new TafTextField(" < parameter value >");
        parameterValueText.setName("ParameterValueField");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestRun.setCustomSettingsValue(parameterNameText.getText(), parameterValueText.getText());
                runSettingsPanel.removeAll();
                mainWindow.updateCliCommandText("");
                createParametersPanel();
                newParameterDialogue.dispatchEvent(new WindowEvent(newParameterDialogue, WindowEvent.WINDOW_CLOSING));
                newParameterDialogue.setVisible(false);
                newParameterDialogue.dispose();
                window.revalidate();
                window.repaint();
            }
        });

        TafButton cancelButton = new TafButton("Cancel");
        cancelButton.setName("CancelButton");
        cancelButton.setFont(AppFont.getInstance());
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newParameterDialogue.dispose();
            }
        });

        GroupLayout groupLayout = new GroupLayout(pane);
        pane.setLayout(groupLayout);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup()
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(parameterNameLabel)
                                .addComponent(parameterValueLabel)
                        )
                        .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(parameterNameText)
                            .addComponent(parameterValueText)
                        )
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(cancelButton)
                                .addComponent(saveButton)
                        )
        );

        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(parameterNameLabel)
                                .addComponent(parameterValueLabel)
                        )
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(parameterNameText)
                                .addComponent(parameterValueText)
                        )
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(cancelButton)
                                .addComponent(saveButton)
                        )
        );


        //pane.setLayout(new GridLayout(3, 2));
        newParameterDialogue.pack();
        newParameterDialogue.setVisible(true);
    }

}
