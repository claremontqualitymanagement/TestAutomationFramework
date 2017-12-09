package se.claremont.autotest.javasupport.gui;

import se.claremont.autotest.common.gui.guistyle.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParameterAddingWindow{
    JFrame parentWindow;
    TafDialog thisDialog;
    TafLabel parameterNameLabel = new TafLabel("Parameter name");
    TafTextField parameterNameTextField = new TafTextField(" < Parameter name > ");
    TafLabel parameterValueLabel = new TafLabel("Parameter value");
    TafTextField parameterValueTextField = new TafTextField(" < Parameter value > ");
    TafButton saveButton = new TafButton("Save");
    TafCloseButton cancelButton;

    public ParameterAddingWindow(JFrame parentWindow, String title){
        thisDialog = new TafDialog(parentWindow, title, true);
        cancelButton = new TafCloseButton(thisDialog);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



            }
        });

        thisDialog.setLayout(new GridLayout(3, 2));
        thisDialog.getContentPane().add(parameterNameLabel);
        thisDialog.getContentPane().add(parameterNameTextField);
        thisDialog.getContentPane().add(parameterValueLabel);
        thisDialog.getContentPane().add(parameterValueTextField);
        thisDialog.getContentPane().add(cancelButton);
        thisDialog.getContentPane().add(saveButton);
        thisDialog.pack();
        thisDialog.setVisible(true);

    }
}
