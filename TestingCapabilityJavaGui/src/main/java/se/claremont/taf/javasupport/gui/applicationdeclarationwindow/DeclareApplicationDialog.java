package se.claremont.taf.javasupport.gui.applicationdeclarationwindow;

import se.claremont.taf.core.gui.Gui;
import se.claremont.taf.core.gui.guistyle.*;
import se.claremont.taf.core.gui.runtab.TestClassPickerDialogue;
import se.claremont.taf.javasupport.applicationundertest.ApplicationUnderTest;
import se.claremont.taf.javasupport.gui.JavaSupportTab;
import se.claremont.taf.javasupport.gui.teststeps.JavaStartApplicationTestStep;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DeclareApplicationDialog {

    TafFrame dialog = new TafFrame("TAF - Declare application");
    TafLabel headline = new TafLabel("Application declaration for testing");
    TafLabel blankSpace = new TafLabel(" ");
    TafPanel parametersPanel = new TafPanel("ParametersPanel");
    TafPanel startApplicationPanel = new TafPanel("StartApplicationPanel");
    TafLabel startApplicationLabel = new TafLabel("Application start parameters");
    TafLabel applicationFriendlyNameLabel = new TafLabel("Friendly name*");
    TafTextField applicationFriendlyNameText = new TafTextField(" < Friendly name > ");
    TafLabel pathToJarFileLabel = new TafLabel("Path to jar:");
    LocalTextField pathToJarFileTextField = new LocalTextField(" <Path to jar> ");
    TafButton selectJarFileButton = new TafButton("Select");
    TafLabel workingFolderLabel = new TafLabel("Working folder:");
    LocalTextField workingFolderTextField = new LocalTextField("<Working folder>");
    TafLabel mainClassLabel = new TafLabel("Main class:");
    JComboBox mainClassComboBox;
    DefaultComboBoxModel model;
    String comboBoxDefaultText = "<Main class>";
    LocalTextField mainClassTextField = new LocalTextField("<Main class>");
    TafLabel runtimeArgumentsLabel = new TafLabel("Runtime arguments:");
    LocalTextField runtimeArgumentsTextField = new LocalTextField("<Runtime arguments>");
    TafCheckbox showAdvancedCheckbox = new TafCheckbox("Show advanced options");
    TafPanel advancedParametersPanel = new TafPanel("AdvancedParametersPanel");
    TafLabel advancedParameterLabel = new TafLabel("Advanced");
    TafLabel loadedLibrariesLabel = new TafLabel("Loaded extra libraries");
    TafTextField loadedLibrariesTextField = new TafTextField(" < Loaded external libraries > ");
    TafButton loadedLibrariesAddButton = new TafButton("Add");
    TafLabel environmentVariablesLabel = new TafLabel("Environment variables");
    TafTextField environmentVariablesText = new TafTextField(" < modified environment variables > ");
    TafButton environmentVariablesAddButton = new TafButton("Add");
    TafLabel systemParametersLabel = new TafLabel("Modified system parameters");
    TafTextField systemParametersTextField = new TafTextField(" < System parameters > ");
    TafButton systemParametersAddButton = new TafButton("Add");
    TafLabel jvmArgumentLabel = new TafLabel("JVM arguments");
    TafTextField jvmArgumentTextField = new TafTextField(" < JVM arguments > ");
    TafButton jvmArgumentAddButton = new TafButton("Add");
    TafLabel cliLabel = new TafLabel("Corresponding CLI command:");
    JTextArea cliCommand = new JTextArea();
    TafButton saveButton = new TafButton("Save");
    TafButton cancelButton = new TafButton("Cancel");
    TafButton tryButton = new TafButton("Try");
    TafButton saveSutToFile = new TafButton("Save to file");
    TafButton loadSutFromFile = new TafButton("Load from file");

    GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    //Todo: List of classpaths needed

    public DeclareApplicationDialog(){
        ApplicationUnderTest unmodifiedAut = new ApplicationUnderTest(JavaSupportTab.applicationUnderTest);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setMaximumSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));

        headline.setFont(new Font(AppFont.getInstance().getName(), AppFont.getInstance().getStyle(), AppFont.getInstance().getSize() * 3/2));

        saveButton.setEnabled(false);

        applicationFriendlyNameText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(applicationFriendlyNameText.isChangedFromDefault() && applicationFriendlyNameText.getText().length() > 0){
                    saveButton.setEnabled(true);
                } else {
                    saveButton.setEnabled(false);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(applicationFriendlyNameText.isChangedFromDefault() && applicationFriendlyNameText.getText().length() > 0){
                    saveButton.setEnabled(true);
                } else {
                    saveButton.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if(applicationFriendlyNameText.isChangedFromDefault() && applicationFriendlyNameText.getText().length() > 0){
                    saveButton.setEnabled(true);
                } else {
                    saveButton.setEnabled(false);
                }
            }
        });

        selectJarFileButton.setMnemonic('e');
        selectJarFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setName("FilePickerWindow");
                fileChooser.setDialogTitle("TAF - File picker");
                fileChooser.setFont(AppFont.getInstance());
                try {
                    fileChooser.setCurrentDirectory(new File(TestClassPickerDialogue.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
                int returnVal = fileChooser.showOpenDialog(dialog);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    pathToJarFileTextField.setText(file.getAbsolutePath());
                    if(!file.isDirectory()){
                        if(workingFolderTextField.getText().equals(workingFolderTextField.disregardedDefaultRunNameString)){
                            workingFolderTextField.setText(file.getParent());
                        }
                    }
                }
                updateMainClassComboboxModel();
            }
        });

        applicationFriendlyNameLabel.setLabelFor(applicationFriendlyNameText);
        applicationFriendlyNameText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(applicationFriendlyNameText.isChangedFromDefault())
                    JavaSupportTab.applicationUnderTest.setName(applicationFriendlyNameText.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(applicationFriendlyNameText.isChangedFromDefault())
                    JavaSupportTab.applicationUnderTest.setName(applicationFriendlyNameText.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if(applicationFriendlyNameText.isChangedFromDefault())
                    JavaSupportTab.applicationUnderTest.setName(applicationFriendlyNameText.getText());
            }
        });

        model = new DefaultComboBoxModel(new String[] {comboBoxDefaultText});
        mainClassComboBox = new JComboBox(model);
        mainClassComboBox.setEditable(true);
        mainClassComboBox.getEditor().getEditorComponent().setForeground(Gui.colorTheme.disabledColor);
        mainClassComboBox.setFont(new Font(AppFont.getInstance().getFontName(), Font.ITALIC, AppFont.getInstance().getSize()));
        mainClassComboBox.addActionListener(new ActionListener() {
            private int selectedIndex = -1;

            @Override
            public void actionPerformed(ActionEvent e) {
                int index = mainClassComboBox.getSelectedIndex();
                if(index >= 0) {
                    selectedIndex = index;
                }
                else if("comboBoxEdited".equals(e.getActionCommand())) {
                    Object newValue = model.getSelectedItem();
                    model.removeElementAt(selectedIndex);
                    model.addElement(newValue);
                    mainClassComboBox.setSelectedItem(newValue);
                    selectedIndex = model.getIndexOf(newValue);
                }
                if(mainClassComboBox.getItemAt(selectedIndex) != null && !mainClassComboBox.getItemAt(selectedIndex).equals(comboBoxDefaultText)){
                    //JavaSupportTab.applicationStartMechanism.mainClass = mainClassComboBox.getItemAt(selectedIndex).toString();
                    updateCliSuggestionAndSaveToFileButtonStatus();
                    mainClassComboBox.getEditor().getEditorComponent().setForeground(Gui.colorTheme.textColor);
                    mainClassComboBox.setFont(AppFont.getInstance());
                } else {
                    mainClassComboBox.getEditor().getEditorComponent().setForeground(Gui.colorTheme.disabledColor);
                    mainClassComboBox.setFont(new Font(AppFont.getInstance().getFontName(), Font.ITALIC, AppFont.getInstance().getSize()));
                }
            }
        });
        mainClassComboBox.setSelectedIndex(0);

        if(JavaSupportTab.applicationUnderTest.startMechanism.startUrlOrPathToJarFile != null &&
                JavaSupportTab.applicationUnderTest.startMechanism.startUrlOrPathToJarFile.length() > 0)
            pathToJarFileTextField.setText(JavaSupportTab.applicationUnderTest.startMechanism.startUrlOrPathToJarFile);

        if(JavaSupportTab.applicationUnderTest.startMechanism.mainClass != null && JavaSupportTab.applicationUnderTest.startMechanism.mainClass.length() > 0)
            mainClassTextField.setText(JavaSupportTab.applicationUnderTest.startMechanism.mainClass);

        if(JavaSupportTab.applicationUnderTest.startMechanism.arguments != null && JavaSupportTab.applicationUnderTest.startMechanism.arguments.size() > 0)
            runtimeArgumentsTextField.setText(String.join(" ", JavaSupportTab.applicationUnderTest.startMechanism.arguments));

        showAdvancedCheckbox.setSelected(false);
        showAdvancedCheckbox.setMnemonic('h');
        showAdvancedCheckbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if(showAdvancedCheckbox.isSelected()){
                    advancedParametersPanel.setVisible(true);
                    dialog.pack();
                    dialog.revalidate();
                    dialog.repaint();
                } else {
                    advancedParametersPanel.setVisible(false);
                    dialog.pack();
                    dialog.revalidate();
                    dialog.repaint();
                }
            }
        });
        advancedParametersPanel.setVisible(false);

        loadedLibrariesTextField.setEditable(false);
        loadedLibrariesAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser window = new JFileChooser();
                window.setName("FilePickerWindow");
                window.setDialogTitle("TAF - File picker");
                window.setFont(AppFont.getInstance());
                try {
                    window.setCurrentDirectory(new File(TestClassPickerDialogue.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
                int returnVal = window.showOpenDialog(dialog);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = window.getSelectedFile();
                    if(file.isDirectory()){
                        JavaSupportTab.applicationUnderTest.loadAllLibrariesInFolder(file.getPath());
                        ArrayList<String> subFiles = new ArrayList<>();
                        for(File subFile : file.listFiles()){
                            if(!subFile.isDirectory()) subFiles.add(subFile.getAbsolutePath());
                        }
                        if(loadedLibrariesTextField.getText().equals(loadedLibrariesTextField.disregardedDefaultRunNameString)){
                            loadedLibrariesTextField.setText(String.join(", ", subFiles));
                        }else {
                            loadedLibrariesTextField.setText(loadedLibrariesTextField.getText() + ", " + String.join(", ", subFiles));
                        }
                    } else {
                        JavaSupportTab.applicationUnderTest.loadLibrary(file.getPath());
                        if(loadedLibrariesTextField.getText().equals(loadedLibrariesTextField.disregardedDefaultRunNameString)){
                            loadedLibrariesTextField.setText(String.join(", ", loadedLibrariesTextField.getText().split(", ")) + file.getAbsolutePath());
                        }else {
                            loadedLibrariesTextField.setText(loadedLibrariesTextField.getText() + ", " + file.getAbsolutePath());
                        }
                    }
                    updateCliSuggestionAndSaveToFileButtonStatus();
                }

            }
        });
        systemParametersTextField.setEditable(false);
        systemParametersAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSystemPropertyWindow propertyAddingWindow = new AddSystemPropertyWindow(systemParametersTextField, dialog);
                updateCliSuggestionAndSaveToFileButtonStatus();
            }
        });

        environmentVariablesText.setEditable(false);
        environmentVariablesAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddEnvironmentVariableWindow addEnvironmentVariableWindow = new AddEnvironmentVariableWindow(environmentVariablesText, dialog);
                updateCliSuggestionAndSaveToFileButtonStatus();
            }
        });

        jvmArgumentTextField.setEditable(false);
        jvmArgumentAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddJVMSettingWindow addJVMSettingWindow = new AddJVMSettingWindow(jvmArgumentTextField, dialog);
                if(jvmArgumentTextField.isChangedFromDefault()){
                    if(jvmArgumentTextField.getText().contains(", ")){
                        for(String pair : jvmArgumentTextField.getText().split(", ")){
                            if(!pair.contains("="))continue;
                            JavaSupportTab.applicationUnderTest.context.jvmSettings.setVMOption(
                                    pair.split("=")[0],
                                    pair.substring(pair.indexOf("=")));
                        }
                    }
                }
                updateCliSuggestionAndSaveToFileButtonStatus();
            }
        });

        loadSutFromFile.setMnemonic('L');
        loadSutFromFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser window = new JFileChooser();
                window.setName("FilePickerWindow");
                window.setDialogTitle("TAF - File picker");
                window.setFont(AppFont.getInstance());
                try {
                    window.setCurrentDirectory(new File(TestClassPickerDialogue.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
                int returnVal = window.showOpenDialog(dialog);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = window.getSelectedFile();
                    JavaSupportTab.applicationUnderTest = new ApplicationUnderTest(ApplicationUnderTest.readFromJsonFile(file.getPath()));
                    pathToJarFileTextField.setText(JavaSupportTab.applicationUnderTest.startMechanism.startUrlOrPathToJarFile.substring(7));
                    mainClassComboBox.setModel(new DefaultComboBoxModel(new String[]{JavaSupportTab.applicationUnderTest.startMechanism.mainClass}));
                    runtimeArgumentsTextField.setText(String.join(", ", JavaSupportTab.applicationUnderTest.startMechanism.arguments));
                    if(JavaSupportTab.applicationUnderTest.context.jvmSettings.appliedSetting.size() > 0){
                        jvmArgumentTextField.setText(String.join(", ", JavaSupportTab.applicationUnderTest.context.jvmSettings.appliedSetting));
                    }
                    environmentVariablesText.setText(String.join(" ", JavaSupportTab.applicationUnderTest.context.environmentVariables.appliedVariableChanges));
                    if(JavaSupportTab.applicationUnderTest.context.properties.appliedProperties.size() > 0){
                        systemParametersTextField.setText(String.join(", ", JavaSupportTab.applicationUnderTest.context.properties.appliedProperties));
                    }
                    loadedLibrariesTextField.setText(String.join(", ", JavaSupportTab.applicationUnderTest.context.loadedLibraries.appliedFiles));
                    updateCliSuggestionAndSaveToFileButtonStatus();
                }
            }
        });

        saveSutToFile.setEnabled(false);
        saveSutToFile.setMnemonic('f');
        saveSutToFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser window = new JFileChooser();
                window.setName("FileSaveWindow");
                window.setDialogTitle("TAF - File save dialog");
                window.setFont(AppFont.getInstance());
                try {
                    window.setCurrentDirectory(new File(TestClassPickerDialogue.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
                int returnVal = window.showSaveDialog(dialog);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = window.getSelectedFile();
                    JavaSupportTab.applicationUnderTest.saveToJsonFile(file.getPath());
                }
            }
        });

        cancelButton.setMnemonic('c');
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JavaSupportTab.applicationUnderTest = new ApplicationUnderTest(unmodifiedAut);
                dialog.setVisible(false);
                dialog.dispose();
            }
        });

        saveButton.setMnemonic('S');
        saveButton.setToolTipText("Needs a set friendly name for saving.");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.addTestStepToListOfAvailableTestSteps(new JavaStartApplicationTestStep(JavaSupportTab.applicationUnderTest));
                dialog.setVisible(false);
                dialog.dispose();
            }
        });

        tryButton.setMnemonic('T');
        tryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JavaSupportTab.applicationUnderTest.start();
            }
        });


        cliCommand.setForeground(TafGuiColor.disabledColor);
        cliCommand.setFont(new Font(AppFont.getInstance().getFontName(), Font.ITALIC, AppFont.getInstance().getSize()));
        cliCommand.setEnabled(false);
        cliCommand.setLineWrap(true);
        cliCommand.setBackground(Gui.colorTheme.backgroundColor);

        updateCliSuggestionAndSaveToFileButtonStatus();

        setParameterPanelLayout();
        setWindowLayout();

        dialog.pack();
        //dialog.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width * 3/5, dialog.getHeight()));
        dialog.setVisible(true);
    }

    private void setParameterPanelLayout() {
        startApplicationPanel.setLayout(gridBagLayout);
        constraints.ipadx = AppFont.getInstance().getSize();

        startApplicationLabel.setFont(new Font(AppFont.getInstance().getName(), Font.BOLD, AppFont.getInstance().getSize()));
        startApplicationPanel.setBorder(BorderFactory.createLineBorder(TafGuiColor.textColor));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.weightx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_START;
        startApplicationPanel.add(startApplicationLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_START;
        startApplicationPanel.add(pathToJarFileLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 0.5;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        startApplicationPanel.add(pathToJarFileTextField, constraints);

        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_END;
        startApplicationPanel.add(selectJarFileButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        startApplicationPanel.add(mainClassLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.weightx = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_END;
        startApplicationPanel.add(mainClassComboBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        startApplicationPanel.add(runtimeArgumentsLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.weightx = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_END;
        startApplicationPanel.add(runtimeArgumentsTextField, constraints);

        advancedParametersPanel.setLayout(new GridBagLayout());
        advancedParametersPanel.setBorder(BorderFactory.createLineBorder(TafGuiColor.textColor));
        advancedParameterLabel.setFont(new Font(AppFont.getInstance().getName(), Font.BOLD, AppFont.getInstance().getSize()));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_START;
        advancedParametersPanel.add(advancedParameterLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_START;
        advancedParametersPanel.add(workingFolderLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_END;
        advancedParametersPanel.add(workingFolderTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        advancedParametersPanel.add(loadedLibrariesLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        advancedParametersPanel.add(loadedLibrariesTextField, constraints);

        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_END;
        advancedParametersPanel.add(loadedLibrariesAddButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        advancedParametersPanel.add(systemParametersLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        advancedParametersPanel.add(systemParametersTextField, constraints);

        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_END;
        advancedParametersPanel.add(systemParametersAddButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        advancedParametersPanel.add(environmentVariablesLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        advancedParametersPanel.add(environmentVariablesText, constraints);

        constraints.gridx = 2;
        constraints.gridy = 4;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_END;
        advancedParametersPanel.add(environmentVariablesAddButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        advancedParametersPanel.add(jvmArgumentLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        advancedParametersPanel.add(jvmArgumentTextField, constraints);

        constraints.gridx = 2;
        constraints.gridy = 5;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_END;
        advancedParametersPanel.add(jvmArgumentAddButton, constraints);

    }

    private void setWindowLayout() {
        TafPanel contentPanelSubstitute = new TafPanel("DeclareSutMainPanel");
        //JScrollPane scrollPane = new JScrollPane(contentPanelSubstitute);
        GroupLayout groupLayout = new GroupLayout(contentPanelSubstitute);
        contentPanelSubstitute.setLayout(groupLayout);
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(headline)
                                .addComponent(blankSpace)
                                .addGroup(groupLayout.createSequentialGroup()
                                    .addComponent(applicationFriendlyNameLabel)
                                    .addComponent(applicationFriendlyNameText)
                                )
                                .addComponent(startApplicationPanel)
                                .addComponent(showAdvancedCheckbox)
                                .addComponent(advancedParametersPanel)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(loadSutFromFile)
                                        .addComponent(saveSutToFile)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(cliLabel)
                                        .addComponent(cliCommand)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(tryButton)
                                        .addComponent(cancelButton)
                                        .addComponent(saveButton)
                                )
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addComponent(blankSpace)
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(applicationFriendlyNameLabel)
                                .addComponent(applicationFriendlyNameText)
                        )
                        .addComponent(startApplicationPanel)
                        .addComponent(showAdvancedCheckbox)
                        .addComponent(advancedParametersPanel)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(loadSutFromFile)
                                .addComponent(saveSutToFile)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(cliLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cliCommand)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(tryButton)
                                .addComponent(cancelButton)
                                .addComponent(saveButton)
                        )
        );

        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        dialog.getContentPane().add(contentPanelSubstitute);
    }

    private void updateMainClassComboboxModel() {
        model.removeAllElements();
        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream(pathToJarFileTextField.getText()));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    try {
                        String className = entry.getName().replace("/", ".").replace("\\", ".");
                        className = className.substring(0, className.length() - ".class".length() );
                        Class klass = Class.forName (className, false, Thread.currentThread().getContextClassLoader());
                        if(!Modifier.isPublic(klass.getModifiers()))continue;
                        //Class<?> klass = ClassLoader.getSystemClassLoader().loadClass(className);
                        //Class<?> klass = Class.forName(className);
                        for(Method method : klass.getDeclaredMethods()){
                            if(!method.getName().equals("main"))continue;
                            method.setAccessible(true);
                            if(Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())){
                                model.addElement(klass.getName());
                            }
                        }
                    }catch (NoClassDefFoundError ignored ){
                        System.out.println(ignored.toString());
                    }catch (Exception ignored) {
                        System.out.println(ignored.toString());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if(model.getSize() == 0) model.addElement(comboBoxDefaultText);
        mainClassComboBox.setModel(model);
        mainClassComboBox.setSelectedIndex(0);
    }

    private void updateCliSuggestionAndSaveToFileButtonStatus(){
        String cli = "";
        String pathToJar = pathToJarFileTextField.getText();
        if(pathToJarFileTextField.isChangedFromDefault() ||
                mainClassTextField.isChangedFromDefault() ||
                runtimeArgumentsTextField.isChangedFromDefault() ||
                systemParametersTextField.isChangedFromDefault() ||
                environmentVariablesText.isChangedFromDefault() ||
                jvmArgumentTextField.isChangedFromDefault() ||
                loadedLibrariesTextField.isChangedFromDefault())
            cli = "java ";


        if(jvmArgumentTextField.isChangedFromDefault()){
            if(jvmArgumentTextField.getText().contains(", ")){
                for(String pair : jvmArgumentTextField.getText().split(", ")){
                    cli += "-X" + pair;
                }
            } else {
                cli += "-X" + jvmArgumentTextField.getText();
            }
        }

        if(pathToJarFileTextField.isChangedFromDefault()){
            JavaSupportTab.applicationUnderTest.startMechanism.startUrlOrPathToJarFile = "file://" + pathToJarFileTextField.getText();
            cli += "-jar " + pathToJar + " ";
        }


        if(JavaSupportTab.applicationUnderTest.context.properties.appliedProperties.size() > 0){
            cli += "-D" + String.join(" -D", JavaSupportTab.applicationUnderTest.context.properties.appliedProperties) + " ";
        }

        String comboboxChoice;
        if(mainClassComboBox.getItemAt(mainClassComboBox.getSelectedIndex()) != null){
            comboboxChoice = mainClassComboBox.getItemAt(mainClassComboBox.getSelectedIndex()).toString();
            if(!comboboxChoice.equals(mainClassTextField.disregardedDefaultRunNameString) && comboboxChoice.length() != 0){
                JavaSupportTab.applicationUnderTest.startMechanism.mainClass = comboboxChoice;
                cli += " " + comboboxChoice;
            }
        }

        if(environmentVariablesText.isChangedFromDefault()){
            if(environmentVariablesText.getText().contains(", ")){
                for(String pair : environmentVariablesText.getText().split(", ")){
                    if(!pair.contains("="))continue;
                    JavaSupportTab.applicationUnderTest.context.environmentVariables.setEnvironmentVariable(pair.split("=")[0], pair.substring(pair.indexOf("=")));
                }
            } else {
                if(environmentVariablesText.getText().contains("="))
                    JavaSupportTab.applicationUnderTest.context.environmentVariables.setEnvironmentVariable(
                            environmentVariablesText.getText().split("=")[0],
                            environmentVariablesText.getText().substring(environmentVariablesText.getText().indexOf("=")));
            }
        }

        if(systemParametersTextField.isChangedFromDefault()){
            if(systemParametersTextField.getText().contains(", ")){
                cli += " -D" + String.join(" -D" + systemParametersTextField.getText().split(", "));
                for(String pair : systemParametersTextField.getText().split(", ")){
                    if(!pair.contains("="))continue;
                    JavaSupportTab.applicationUnderTest.context.properties.setProperty(pair.split("=")[0], pair.substring(pair.indexOf("=")));
                }
            } else {
                cli += " -D" + systemParametersTextField.getText();
                if(systemParametersTextField.getText().contains("="))
                    JavaSupportTab.applicationUnderTest.context.properties.setProperty(
                            systemParametersTextField.getText().split("=")[0],
                            systemParametersTextField.getText().substring(systemParametersTextField.getText().indexOf("=")));
            }
        }

        if(!workingFolderTextField.getText().equals(workingFolderTextField.disregardedDefaultRunNameString) && workingFolderTextField.getText().length() != 0){
            cli += " -cp " + workingFolderTextField.getText() + File.pathSeparator + "*";
            if(!loadedLibrariesTextField.getText().equals(loadedLibrariesTextField.disregardedDefaultRunNameString)){
                cli += "/" + loadedLibrariesTextField.getText().replace(", ", "/");
            }
        }

        if(!runtimeArgumentsTextField.getText().equals(runtimeArgumentsTextField.disregardedDefaultRunNameString) && runtimeArgumentsTextField.getText().length() != 0) {
            JavaSupportTab.applicationUnderTest.startMechanism.arguments.clear();
            for(String arg : runtimeArgumentsTextField.getText().split(" ")){
                JavaSupportTab.applicationUnderTest.startMechanism.arguments.add(arg);
            }
            cli += " " + runtimeArgumentsTextField.getText();
        }

        if(pathToJarFileTextField.getText() != null &&
                pathToJarFileTextField.getText().length() > 0 &&
                mainClassComboBox.getItemAt(mainClassComboBox.getSelectedIndex()).toString().length() > 0 &&
                !mainClassComboBox.getItemAt(mainClassComboBox.getSelectedIndex()).toString().equals(comboBoxDefaultText))
            saveSutToFile.setEnabled(true);

        cliCommand.setText(cli);
        dialog.pack();
        dialog.revalidate();
        dialog.repaint();
    }

    private class LocalTextField extends TafTextField{

        public LocalTextField(String initialText){
            super(initialText);
            getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) { updateCliSuggestionAndSaveToFileButtonStatus(); }

                public void removeUpdate(DocumentEvent e) { updateCliSuggestionAndSaveToFileButtonStatus(); }

                public void insertUpdate(DocumentEvent e) {
                    updateCliSuggestionAndSaveToFileButtonStatus();
                }
            });

        }
    }
}
