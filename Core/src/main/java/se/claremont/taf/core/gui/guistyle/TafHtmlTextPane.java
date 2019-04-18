package se.claremont.taf.core.gui.guistyle;

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
        String originalText = getText().substring(getText().indexOf("<body>") + 6);
        originalText = originalText.substring(0, originalText.lastIndexOf("</body>"));
        this.setText("<html><body>" + originalText + htmlText + "</body></html>");
        this.revalidate();
        this.repaint();
    }

    public void appendLine(String htmlText){
        this.append(htmlText + "<br>" + System.lineSeparator());
    }

    @Override
    public void setToolTipText(String tooltip){
        super.setToolTipText(Helper.tooltipStypeInfoHead + tooltip + Helper.tooltipStypeInfoTail);
    }

}
