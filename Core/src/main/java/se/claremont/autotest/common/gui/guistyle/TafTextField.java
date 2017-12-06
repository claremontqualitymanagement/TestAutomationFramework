package se.claremont.autotest.common.gui.guistyle;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.support.StringManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TafTextField extends JTextField {
    public String disregardedDefaultRunNameString;

    public TafTextField(String initialDefaultText){
        disregardedDefaultRunNameString = initialDefaultText;
        if(initialDefaultText != null && initialDefaultText.length() > 0) {
            setName(StringManagement.methodNameWithOnlySafeCharacters(initialDefaultText) + "TextField");
        } else {
            setName("TextField");
        }
        prepareField();
    }

    private void initiateTextFieldWhenActivated(){
        if(getText().equals(disregardedDefaultRunNameString)) setText("");
        setFont(AppFont.getInstance());
        setForeground(Gui.colorTheme.textColor);
    }

    private class TafFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            initiateTextFieldWhenActivated();
        }

        @Override
        public void focusLost(FocusEvent e) {
            if(getText().equals("")){
                initiateTextFieldToDefault();
            }
        }
    }

    private void initiateTextFieldToDefault(){
        setFont(new Font(AppFont.getInstance().getFontName(), Font.ITALIC, AppFont.getInstance().getSize()));
        setForeground(Gui.colorTheme.disabledColor);
        setText(disregardedDefaultRunNameString);
    }

    private void prepareField() {
        initiateTextFieldToDefault();
        addFocusListener(new TafFocusListener());
    }

}
