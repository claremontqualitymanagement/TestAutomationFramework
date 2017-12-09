package se.claremont.autotest.javasupport.gui;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.common.gui.runtab.TestClassPickerDialogue;
import se.claremont.autotest.javasupport.applicationundertest.applicationstarters.ApplicationStartMechanism;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DeclareApplicationDialog {

    TafFrame dialog = new TafFrame();
    TafLabel headline = new TafLabel("Application declaration for testing");
    TafLabel pathToJarFileLabel = new TafLabel("Path to jar:");
    LocalTextField pathToJarFileTextField = new LocalTextField(" <Path to jar> ");
    TafButton selectExeFileButton = new TafButton("Select");
    TafLabel workingFolderLabel = new TafLabel("Working folder:");
    LocalTextField workingFolderTextField = new LocalTextField("<Working folder>");
    TafLabel mainClassLabel = new TafLabel("Main class:");
    JComboBox mainClassComboBox;
    DefaultComboBoxModel model;
    String comboBoxDefaultText = "<Main class>";
    LocalTextField mainClassTextField = new LocalTextField("<Main class>");
    TafLabel runtimeArgumentsLabel = new TafLabel("Runtime arguments:");
    LocalTextField runtimeArgumentsTextField = new LocalTextField("<Runtime arguments>");
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
    //Todo: List of classpaths needed

    public DeclareApplicationDialog(){
        String originalPath = JavaSupportTab.applicationUnderTest.startMechanism.startUrlOrPathToJarFile;
        java.util.List<String> originalArgs = JavaSupportTab.applicationUnderTest.startMechanism.arguments;
        String originalMain = JavaSupportTab.applicationUnderTest.startMechanism.mainClass;

        GroupLayout groupLayout = new GroupLayout(dialog.getContentPane());
        dialog.getContentPane().setLayout(groupLayout);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
                            loadedLibrariesTextField.setText(String.join(", ", file.getName()));
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
                ParameterAddingWindow parameterAddingWindow = new ParameterAddingWindow(dialog, "TAF - Adding parameter");
            }
        });

        environmentVariablesText.setEditable(false);
        environmentVariablesAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ParameterAddingWindow parameterAddingWindow = new ParameterAddingWindow(dialog, "TAF - Adding parameter");

            }
        });
        jvmArgumentTextField.setEditable(false);
        jvmArgumentAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ParameterAddingWindow parameterAddingWindow = new ParameterAddingWindow(dialog, "TAF - Adding parameter");
            }
        });

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
                    JavaSupportTab.applicationUnderTest.startMechanism = ApplicationStartMechanism.readFromJsonFile(file.getPath());
                    pathToJarFileTextField.setText(JavaSupportTab.applicationUnderTest.startMechanism.startUrlOrPathToJarFile.substring(7));
                    mainClassComboBox.setModel(new DefaultComboBoxModel(new String[]{JavaSupportTab.applicationUnderTest.startMechanism.mainClass}));
                    runtimeArgumentsTextField.setText(String.join(" ",JavaSupportTab.applicationUnderTest.startMechanism.arguments));
                    updateCliSuggestionAndSaveToFileButtonStatus();
                }
            }
        });

        saveSutToFile.setEnabled(false);
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
                    JavaSupportTab.applicationUnderTest.startMechanism.saveToJsonFile(file.getPath());
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JavaSupportTab.applicationUnderTest.startMechanism.mainClass = originalMain;
                JavaSupportTab.applicationUnderTest.startMechanism.arguments = originalArgs;
                JavaSupportTab.applicationUnderTest.startMechanism.startUrlOrPathToJarFile = originalPath;
                dialog.setVisible(false);
                dialog.dispose();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });

        tryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JavaSupportTab.applicationUnderTest.start();
            }
        });

        selectExeFileButton.addActionListener(new ActionListener() {
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

        cliCommand.setForeground(TafGuiColor.disabledColor);
        cliCommand.setFont(new Font(AppFont.getInstance().getFontName(), Font.ITALIC, AppFont.getInstance().getSize()));
        cliCommand.setEnabled(false);
        cliCommand.setLineWrap(true);
        cliCommand.setBackground(Gui.colorTheme.backgroundColor);

        updateCliSuggestionAndSaveToFileButtonStatus();

        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(headline)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(pathToJarFileLabel)
                                        .addComponent(pathToJarFileTextField)
                                        .addComponent(selectExeFileButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(workingFolderLabel)
                                        .addComponent(workingFolderTextField, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(mainClassLabel)
                                        .addComponent(mainClassComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(runtimeArgumentsLabel)
                                        .addComponent(runtimeArgumentsTextField, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(loadedLibrariesLabel)
                                        .addComponent(loadedLibrariesTextField)
                                        .addComponent(loadedLibrariesAddButton)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(systemParametersLabel)
                                        .addComponent(systemParametersTextField)
                                        .addComponent(systemParametersAddButton)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(environmentVariablesLabel)
                                        .addComponent(environmentVariablesText)
                                        .addComponent(environmentVariablesAddButton)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(jvmArgumentLabel)
                                        .addComponent(jvmArgumentTextField)
                                        .addComponent(jvmArgumentAddButton)
                                )
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
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(pathToJarFileLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pathToJarFileTextField)
                                .addComponent(selectExeFileButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(workingFolderLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(workingFolderTextField, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(mainClassLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(mainClassComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(runtimeArgumentsLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(runtimeArgumentsTextField, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(loadedLibrariesLabel)
                                .addComponent(loadedLibrariesTextField)
                                .addComponent(loadedLibrariesAddButton)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(systemParametersLabel)
                                .addComponent(systemParametersTextField)
                                .addComponent(systemParametersAddButton)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(environmentVariablesLabel)
                                .addComponent(environmentVariablesText)
                                .addComponent(environmentVariablesAddButton)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jvmArgumentLabel)
                                .addComponent(jvmArgumentTextField)
                                .addComponent(jvmArgumentAddButton)
                        )
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
        dialog.pack();
        dialog.setSize(Toolkit.getDefaultToolkit().getScreenSize().width * 3/5, Toolkit.getDefaultToolkit().getScreenSize().height * 3/5);
        dialog.setVisible(true);
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
        String pathToExe = pathToJarFileTextField.getText();
        if(!pathToExe.equals(pathToJarFileTextField.disregardedDefaultRunNameString) && pathToExe.length() != 0){
            JavaSupportTab.applicationUnderTest.startMechanism.startUrlOrPathToJarFile = "file://" + pathToJarFileTextField.getText();
            cli += "java -jar " + pathToExe;
        }

        String comboboxChoice;
        if(mainClassComboBox.getItemAt(mainClassComboBox.getSelectedIndex()) != null){
            comboboxChoice = mainClassComboBox.getItemAt(mainClassComboBox.getSelectedIndex()).toString();
            if(!comboboxChoice.equals(mainClassTextField.disregardedDefaultRunNameString) && comboboxChoice.length() != 0){
                JavaSupportTab.applicationUnderTest.startMechanism.mainClass = comboboxChoice;
                cli += " " + comboboxChoice;
            }
        }

        if(!workingFolderTextField.getText().equals(workingFolderTextField.disregardedDefaultRunNameString) && workingFolderTextField.getText().length() != 0){
            cli += " -cp " + workingFolderTextField.getText() + File.separator + "*";
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
