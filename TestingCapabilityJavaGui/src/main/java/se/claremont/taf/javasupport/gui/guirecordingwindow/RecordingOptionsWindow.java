package se.claremont.taf.javasupport.gui.guirecordingwindow;

import se.claremont.taf.core.gui.guistyle.*;
import se.claremont.taf.javasupport.applicationundertest.ApplicationUnderTest;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class RecordingOptionsWindow {

    TafDialog dialog;
    public static boolean recordTafWindows = false;
    private static String initialPath = new File(".").getAbsolutePath().substring(0, new File(".").getAbsolutePath().length() -1) + "capturemanagers";
    public static boolean recordKeyStrokesOutsideOfWindows = true;
    public static String pathToScriptsRoot = initialPath;

    public RecordingOptionsWindow(TafFrame parent) {
        dialog = new TafDialog(parent, "TAF - Recording options", true);
        GroupLayout groupLayout = new GroupLayout(dialog.getContentPane());
        dialog.getContentPane().setLayout(groupLayout);

        TafHeadline headline = new TafHeadline("Recording options");

        TafLabel scriptRootPathLabel = new TafLabel("Script root path");
        TafTextField scriptRootPathTextField = new TafTextField(" < Script root path > ");
        scriptRootPathTextField.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(scriptRootPathTextField.isChangedFromDefault())
                    pathToScriptsRoot = scriptRootPathTextField.getText();
            }
        });
        scriptRootPathTextField.setText(initialPath);
        scriptRootPathLabel.setLabelFor(scriptRootPathTextField);

        TafButton selectScriptRootPathButton = new TafButton("Select");
        selectScriptRootPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
            }
        });

        TafCheckbox recordAllKeyStrolesCheckbox = new TafCheckbox("Record keystrokes outside windows");
        recordAllKeyStrolesCheckbox.setSelected(true);
        recordAllKeyStrolesCheckbox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                recordKeyStrokesOutsideOfWindows = recordAllKeyStrolesCheckbox.isSelected();
            }
        });

        TafCheckbox recordTafWindowsAlsoCheckbox = new TafCheckbox("Also record TAF windows");
        recordTafWindowsAlsoCheckbox.setSelected(false);

        TafCloseButton closeButton = new TafCloseButton(dialog);
        closeButton.setText("Cancel");

        TafButton saveButton = new TafButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordKeyStrokesOutsideOfWindows = recordAllKeyStrolesCheckbox.isSelected();
                for(Window window : ApplicationUnderTest.getWindows()){
                    if(window.getClass().equals(TafFrame.class) || window.getClass().equals(TafDialog.class)){
                        if(!recordTafWindowsAlsoCheckbox.isSelected()){
                            RecordingListenersManager.removeAllTafRecordingListenersFromWindow(window);
                        } else {
                            RecordingListenersManager.makeSureAllComponentsHasRegisteredListeners(window);
                        }
                    }
                }
            }
        });

        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup()
                        .addComponent(headline)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(scriptRootPathLabel)
                                .addComponent(scriptRootPathTextField)
                                .addComponent(selectScriptRootPathButton)
                        )
                        .addComponent(recordAllKeyStrolesCheckbox)
                        .addComponent(recordTafWindowsAlsoCheckbox)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(closeButton)
                                .addComponent(saveButton)
                        )
        );

        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(scriptRootPathLabel)
                                .addComponent(scriptRootPathTextField)
                                .addComponent(selectScriptRootPathButton)
                        )
                        .addComponent(recordAllKeyStrolesCheckbox)
                        .addComponent(recordTafWindowsAlsoCheckbox)
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(closeButton)
                                .addComponent(saveButton)
                        )
        );

        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        dialog.pack();
        dialog.setVisible(true);
    }

}
