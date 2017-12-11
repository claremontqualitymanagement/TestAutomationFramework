package se.claremont.autotest.common.gui.guistyle;

import javax.swing.*;

public class TafHtmlTextPane extends JTextPane {

    public TafHtmlTextPane(String name){
        this.setName(name);
        this.setFont(AppFont.getInstance());
        this.setForeground(TafGuiColor.textColor);
        this.setContentType("text/html");
        this.setText("<html><body></body></html>");
    }

    public void append(String htmlText){
        this.setText(this.getText().substring(0, this.getText().length() - "</body></html>".length()) + htmlText + "</body></html>");
    }

    public void appendLine(String htmlText){
        this.append(htmlText + "<br>" + System.lineSeparator());
    }
}
