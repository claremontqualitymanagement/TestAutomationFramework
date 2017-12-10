package se.claremont.autotest.common.gui.guistyle;

import javax.swing.*;

public class TafTextPane extends JTextPane {

    public TafTextPane(String name){
        this.setName(name);
        this.setFont(AppFont.getInstance());
        this.setForeground(TafGuiColor.textColor);
        this.setContentType("text/html");
    }

    public void append(String htmlText){
        this.setText(this.getText() + htmlText);
    }

    public void appendLine(String htmlText){
        this.append(htmlText + "<br>" + System.lineSeparator());
    }
}
