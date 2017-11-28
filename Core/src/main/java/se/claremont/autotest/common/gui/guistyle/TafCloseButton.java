package se.claremont.autotest.common.gui.guistyle;

import se.claremont.autotest.common.support.StringManagement;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TafCloseButton extends TafButtonType {

    public TafCloseButton(JFrame frame){
        this.setText("Close");
        String frameName = frame.getName();
        if(frameName == null || frameName.length() == 0)
            frameName = frame.getTitle();
        if(frameName == null || frameName.length() == 0)
            frameName = frame.getClass().getSimpleName();
        this.setName(StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(StringManagement.methodNameWithOnlySafeCharacters(frameName)) + "CloseButton");
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    public TafCloseButton(JDialog frame){
        this.setText("Close");
        String frameName = frame.getName();
        if(frameName == null || frameName.length() == 0)
            frameName = frame.getTitle();
        if(frameName == null || frameName.length() == 0)
            frameName = frame.getClass().getSimpleName();
        this.setName(StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(StringManagement.methodNameWithOnlySafeCharacters(frameName)) + "CloseButton");
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }
}
