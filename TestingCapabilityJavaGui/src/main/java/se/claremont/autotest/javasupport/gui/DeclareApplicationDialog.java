package se.claremont.autotest.javasupport.gui;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.common.gui.runtab.TestClassPickerDialogue;

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
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DeclareApplicationDialog {

    TafFrame window = new TafFrame();
    TafLabel headline = new TafLabel("Application declaration for testing");
    TafLabel pathToJarFileLabel = new TafLabel("Path to jar:");
    LocalTextField pathToJarFileTextField = new LocalTextField(" <Path to jar> ");
    TafButton selectExeFileButton = new TafButton("Select");
    TafLabel workingFolderLabel = new TafLabel("Working folder:");
    LocalTextField workingFolderTextField = new LocalTextField("<Working folder>");
    TafLabel mainClassLabel = new TafLabel("Main class:");
    JComboBox comboBox;
    DefaultComboBoxModel model;
    String comboBoxDefaultText = "<Main class>";
    LocalTextField mainClassTextField = new LocalTextField("<Main class>");
    TafLabel runtimeArgumentsLabel = new TafLabel("Runtime arguments:");
    LocalTextField runtimeArgumentsTextField = new LocalTextField("<Runtime arguments>");
    TafLabel cliLabel = new TafLabel("Corresponding CLI command:");
    JTextArea cliCommand = new JTextArea();
    TafButton saveButton = new TafButton("Save");
    TafButton cancelButton = new TafButton("Cancel");
    TafButton tryButton = new TafButton("Try");
    //Todo: List of classpaths needed

    public DeclareApplicationDialog(){
        String originalPath = JavaSupportTab.applicationStartMechanism.startUrlOrPathToJarFile;
        java.util.List<String> originalArgs = JavaSupportTab.applicationStartMechanism.arguments;
        String originalMain = JavaSupportTab.applicationStartMechanism.mainClass;

        GroupLayout groupLayout = new GroupLayout(window.getContentPane());
        window.getContentPane().setLayout(groupLayout);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultComboBoxModel(new String[] {comboBoxDefaultText});

        comboBox = new JComboBox(model);
        comboBox.setEditable(true);
        comboBox.getEditor().getEditorComponent().setForeground(Gui.colorTheme.disabledColor);
        comboBox.setFont(new Font(AppFont.getInstance().getFontName(), Font.ITALIC, AppFont.getInstance().getSize()));
        comboBox.addActionListener(new ActionListener() {
            private int selectedIndex = -1;

            @Override
            public void actionPerformed(ActionEvent e) {
                int index = comboBox.getSelectedIndex();
                if(index >= 0) {
                    selectedIndex = index;
                }
                else if("comboBoxEdited".equals(e.getActionCommand())) {
                    Object newValue = model.getSelectedItem();
                    model.removeElementAt(selectedIndex);
                    model.addElement(newValue);
                    comboBox.setSelectedItem(newValue);
                    selectedIndex = model.getIndexOf(newValue);
                }
                if(comboBox.getItemAt(selectedIndex) != null && !comboBox.getItemAt(selectedIndex).equals(comboBoxDefaultText)){
                    comboBox.getEditor().getEditorComponent().setForeground(Gui.colorTheme.textColor);
                    comboBox.setFont(AppFont.getInstance());
                } else {
                    comboBox.getEditor().getEditorComponent().setForeground(Gui.colorTheme.disabledColor);
                    comboBox.setFont(new Font(AppFont.getInstance().getFontName(), Font.ITALIC, AppFont.getInstance().getSize()));
                }
            }
        });
        comboBox.setSelectedIndex(0);

        if(JavaSupportTab.applicationStartMechanism.startUrlOrPathToJarFile != null && JavaSupportTab.applicationStartMechanism.startUrlOrPathToJarFile.length() > 0)
            pathToJarFileTextField.setText(JavaSupportTab.applicationStartMechanism.startUrlOrPathToJarFile);

        if(JavaSupportTab.applicationStartMechanism.mainClass != null && JavaSupportTab.applicationStartMechanism.mainClass.length() > 0)
            mainClassTextField.setText(JavaSupportTab.applicationStartMechanism.mainClass);

        if(JavaSupportTab.applicationStartMechanism.arguments != null && JavaSupportTab.applicationStartMechanism.arguments.size() > 0)
            runtimeArgumentsTextField.setText(String.join(" ", JavaSupportTab.applicationStartMechanism.arguments));

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JavaSupportTab.applicationStartMechanism.mainClass = originalMain;
                JavaSupportTab.applicationStartMechanism.arguments = originalArgs;
                JavaSupportTab.applicationStartMechanism.startUrlOrPathToJarFile = originalPath;
                window.setVisible(false);
                window.dispose();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.setVisible(false);
                window.dispose();
            }
        });

        tryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JavaSupportTab.applicationStartMechanism.run();
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
                int returnVal = fileChooser.showOpenDialog(window);
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

        updateCli();

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
                                        .addComponent(comboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(runtimeArgumentsLabel)
                                        .addComponent(runtimeArgumentsTextField, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                                .addComponent(comboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(runtimeArgumentsLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(runtimeArgumentsTextField, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(cliLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cliCommand)
                        )
                        .addComponent(cliCommand)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(tryButton)
                                .addComponent(cancelButton)
                                .addComponent(saveButton)
                        )
        );

        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        window.pack();
        window.setSize(Toolkit.getDefaultToolkit().getScreenSize().width * 3/5, Toolkit.getDefaultToolkit().getScreenSize().height * 3/5);
        window.setVisible(true);
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
        comboBox.setModel(model);
        comboBox.setSelectedIndex(0);
    }

    private void updateCli(){
        String cli = "";
        String pathToExe = pathToJarFileTextField.getText();
        if(!pathToExe.equals(pathToJarFileTextField.disregardedDefaultRunNameString) && pathToExe.length() != 0){
            JavaSupportTab.applicationStartMechanism.startUrlOrPathToJarFile = "file://" + pathToJarFileTextField.getText();
            cli += "java -jar " + pathToExe;
        }

        String comboboxChoice = comboBox.getItemAt(comboBox.getSelectedIndex()).toString();
        if(!comboboxChoice.equals(mainClassTextField.disregardedDefaultRunNameString) && comboboxChoice.length() != 0){
            JavaSupportTab.applicationStartMechanism.mainClass = comboboxChoice;
            cli += " " + comboboxChoice;
        }

        if(!workingFolderTextField.getText().equals(workingFolderTextField.disregardedDefaultRunNameString) && workingFolderTextField.getText().length() != 0){
            cli += " -cp " + workingFolderTextField.getText() + File.separator + "*";
        }

        if(!runtimeArgumentsTextField.getText().equals(runtimeArgumentsTextField.disregardedDefaultRunNameString) && runtimeArgumentsTextField.getText().length() != 0) {
            JavaSupportTab.applicationStartMechanism.arguments.clear();
            for(String arg : runtimeArgumentsTextField.getText().split(" ")){
                JavaSupportTab.applicationStartMechanism.arguments.add(arg);
            }
            cli += " " + runtimeArgumentsTextField.getText();
        }
        cliCommand.setText(cli);
    }

    private class LocalTextField extends TafTextField{

        public LocalTextField(String initialText){
            super(initialText);
            getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) { updateCli(); }

                public void removeUpdate(DocumentEvent e) { updateCli(); }

                public void insertUpdate(DocumentEvent e) {
                    updateCli();
                }
            });

        }
    }
}
