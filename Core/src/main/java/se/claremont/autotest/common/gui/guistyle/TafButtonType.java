package se.claremont.autotest.common.gui.guistyle;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.support.StringManagement;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class TafButtonType extends JButton{

    public TafButtonType(){
        this.setFont(AppFont.getInstance());
        this.setForeground(Gui.colorTheme.textColor);
        this.setName("TafButton");
        /*
        JButton b = this;
        this.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel)e.getSource();
                if(model.isEnabled()){
                    b.setForeground(TafGuiColor.textColor);
                }
                if(!model.isEnabled()){
                    b.setForeground(TafGuiColor.disabledColor);
                    b.setBackground(TafGuiColor.backgroundColor);
                }
            }
        });
        */
    }

    public TafButtonType(String label){
        this.setText(label);
        this.setFont(AppFont.getInstance());
        this.setForeground(TafGuiColor.textColor);
        this.setName(StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(StringManagement.methodNameWithOnlySafeCharacters(label)) + "Button");
        /*
        JButton b = this;
        this.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel)e.getSource();
                if(model.isEnabled()){
                    b.setForeground(TafGuiColor.textColor);
                }
                if(!model.isEnabled()){
                    b.setForeground(TafGuiColor.disabledColor);
                }
            }
        });
        */
    }

}
