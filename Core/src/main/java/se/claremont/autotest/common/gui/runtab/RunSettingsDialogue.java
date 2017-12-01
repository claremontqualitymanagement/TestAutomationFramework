package se.claremont.autotest.common.gui.runtab;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;

public class RunSettingsDialogue {

    TafDialog window;
    RunTestTabPanel mainWindow;

    public RunSettingsDialogue(RunTestTabPanel mainWindow) {
        this.mainWindow = mainWindow;
        createWindow();
    }

    private void createWindow() {
        HashMap<String, String> runSettingsWhenOpened = new HashMap<>(Gui.defaultSettings);
        window = new TafDialog(mainWindow.applicationWindow, "RunSettingsWindow", false);
        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                mainWindow.updateCliCommandText("");
            }
        });
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setTitle("TAF - Run settings");

        int numberOfParameters = TestRun.getSettings().size();
        Container pane = window.getContentPane();
        pane.setName("RunSettingsContentPanel");
        GroupLayout groupLayout = new GroupLayout(pane);
        pane.setLayout(groupLayout);

        TafPanel runSettingsPanel = new TafPanel("SettingsValuesPanel");
        runSettingsPanel.setLayout(new GridLayout(numberOfParameters, 2, 20, 5));

        for (String key : TestRun.getSettings().keySet()) {
            runSettingsPanel.add(new TafLabel(key));
            JTextField parameterValue = new JTextField(TestRun.getSettings().get(key));
            parameterValue.setName(key.replace(" ", "") + "Value");
            parameterValue.setFont(AppFont.getInstance());
            parameterValue.setForeground(Gui.colorTheme.textColor);
            parameterValue.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    TestRun.setCustomSettingsValue(key, parameterValue.getText());
                    mainWindow.updateCliCommandText("");
                }

                public void removeUpdate(DocumentEvent e) {
                    TestRun.setCustomSettingsValue(key, parameterValue.getText());
                    mainWindow.updateCliCommandText("");
                }

                public void insertUpdate(DocumentEvent e) {
                    TestRun.setCustomSettingsValue(key, parameterValue.getText());
                    mainWindow.updateCliCommandText("");
                }
            });
            runSettingsPanel.add(parameterValue);
        }

        TafButton loadSettingsFromFile = new TafButton("Load settings");
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
                int returnVal = filePickerWindow.showOpenDialog(mainWindow.applicationWindow);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = filePickerWindow.getSelectedFile();
                    Settings settings = new Settings(file.getAbsolutePath());
                    TestRun.setSettings(settings);
                    window.setVisible(false);
                    createWindow();
                }

            }
        });

        TafButton saveSettingsToFile = new TafButton("Save settings");
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
                int returnVal = filePickerWindow.showSaveDialog(mainWindow.applicationWindow);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = filePickerWindow.getSelectedFile();
                    TestRun.getSettings().writeSettingsParametersToFile(file.getAbsolutePath());
                }

            }
        });

        TafButton addValueButton = new TafButton("Add parameter");
        addValueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNewValue();
            }
        });

        TafButton cancelButton = new TafButton("Cancel");
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

    private void setNewValue() {

        TafFrame newParameterDialogue = new TafFrame("TAF - Add run parameter");
        newParameterDialogue.setName("NewTestRunParameterWindow");
        newParameterDialogue.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container pane = newParameterDialogue.getContentPane();
        pane.setName("NewTestRunParameterContentPanel");

        TafLabel parameterNameLabel = new TafLabel("Parameter name");

        JTextField parameterNameText = new JTextField();
        parameterNameText.setName("ParameterNameTextField");
        parameterNameText.setForeground(Gui.colorTheme.textColor);
        parameterNameText.setFont(AppFont.getInstance());

        TafLabel parameterValueLabel = new TafLabel("Parameter value");

        JTextField parameterValueText = new JTextField();
        parameterValueText.setForeground(Gui.colorTheme.textColor);
        parameterValueText.setName("ParameterValueTextField");
        parameterValueText.setFont(AppFont.getInstance());

        TafButton saveButton = new TafButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestRun.setCustomSettingsValue(parameterNameText.getText(), parameterValueText.getText());
                window.getContentPane().removeAll();
                mainWindow.updateCliCommandText("");
                createWindow();
                newParameterDialogue.dispose();
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
