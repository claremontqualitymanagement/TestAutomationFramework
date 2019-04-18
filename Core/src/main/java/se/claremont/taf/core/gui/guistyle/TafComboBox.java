package se.claremont.taf.core.gui.guistyle;

import javax.swing.*;

public class TafComboBox extends JComboBox {

    public TafComboBox(String name, String[] options){
        super(options);
        setName(name);
        setFont(AppFont.getInstance());
        getEditor().getEditorComponent().setForeground(TafGuiColor.textColor);
        getEditor().getEditorComponent().setBackground(new TafTextField("dummy").getBackground());
    }

    @Override
    public void setToolTipText(String tooltip){
        super.setToolTipText(Helper.tooltipStypeInfoHead + tooltip + Helper.tooltipStypeInfoTail);
    }

}
