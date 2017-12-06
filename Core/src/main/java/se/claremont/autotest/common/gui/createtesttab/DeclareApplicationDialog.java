package se.claremont.autotest.common.gui.createtesttab;

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
import java.net.URISyntaxException;

public class DeclareApplicationDialog {

    TafFrame window = new TafFrame();
    TafLabel headline = new TafLabel("Application declaration for testing");
    TafLabel pathToJarFileLabel = new TafLabel("Path to jar:");
    LocalTextField pathToJarFileTextField = new LocalTextField(" <Path to jar> ");
    TafButton selectExeFileButton = new TafButton("Select");
    TafLabel runtimeArgumentsLabel = new TafLabel("Runtime arguments:");
    LocalTextField runtimeArgumentsTextField = new LocalTextField("<Runtime arguments>");
    TafLabel workingFolderLabel = new TafLabel("Working folder:");
    LocalTextField workingFolderTextField = new LocalTextField("<Working folder>");
    TafLabel cliLabel = new TafLabel("Corresponding CLI command:");
    JTextArea cliCommand = new JTextArea();
    TafButton saveButton = new TafButton("Save");
    TafButton cancelButton = new TafButton("Cancel");
    TafButton tryButton = new TafButton("Try");
    //Todo: List of classpaths needed

    public DeclareApplicationDialog(){
        GroupLayout groupLayout = new GroupLayout(window.getContentPane());
        window.getContentPane().setLayout(groupLayout);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.setVisible(false);
                window.dispose();
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
            }
        });

        cliCommand.setForeground(TafGuiColor.disabledColor);
        cliCommand.setFont(new Font(AppFont.getInstance().getFontName(), Font.ITALIC, AppFont.getInstance().getSize()));
        cliCommand.setEnabled(false);
        cliCommand.setLineWrap(true);
        cliCommand.setBackground(Gui.colorTheme.backgroundColor);

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
        window.setSize(Toolkit.getDefaultToolkit().getScreenSize().width * 3/5, Toolkit.getDefaultToolkit().getScreenSize().height * 3/5);
        window.setVisible(true);
    }

    private void updateCli(){
        String cli = "";
        String pathToExe = pathToJarFileTextField.getText();
        if(!pathToExe.equals(pathToJarFileTextField.disregardedDefaultRunNameString) && pathToExe.length() != 0)
            cli += "java -jar " + pathToExe;

        if(!workingFolderTextField.getText().equals(workingFolderTextField.disregardedDefaultRunNameString) && workingFolderTextField.getText().length() != 0)
            cli += " -cp " + workingFolderTextField.getText() + File.separator + "*";

        if(!runtimeArgumentsTextField.getText().equals(runtimeArgumentsTextField.disregardedDefaultRunNameString) && runtimeArgumentsTextField.getText().length() != 0)
            cli += " " + runtimeArgumentsTextField.getText();

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
