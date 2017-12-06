package se.claremont.autotest.common.gui.createtesttab;

import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.common.gui.runtab.TestClassPickerDialogue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;

public class DeclareApplicationDialog {

    TafFrame window = new TafFrame();
    TafLabel headline = new TafLabel("Application declaration for testing");
    TafLabel pathToExeLabel = new TafLabel("Path to exe:");
    TafTextField pathToExeTextField = new TafTextField(" <Path to exe> ");
    TafButton selectExeFileButton = new TafButton("Select");
    TafLabel runtimeArgumentsLabel = new TafLabel("Runtime arguments:");
    TafTextField runtimeArgumentsTextField = new TafTextField("<Runtime arguments>");
    TafLabel workingFolderLabel = new TafLabel("Working folder:");
    TafTextField workingFolderTextField = new TafTextField("<Working folder>");
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
                    pathToExeTextField.setText(file.getAbsolutePath());
                    if(workingFolderTextField.getText().equals(workingFolderTextField.disregardedDefaultRunNameString)){
                        workingFolderTextField.setText(file.getPath());
                    }
                }
            }
        });

        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(headline)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(pathToExeLabel)
                                        .addComponent(pathToExeTextField)
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
                                .addComponent(pathToExeLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pathToExeTextField)
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
}
