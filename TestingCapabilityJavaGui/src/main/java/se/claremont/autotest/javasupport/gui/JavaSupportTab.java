package se.claremont.autotest.javasupport.gui;

import javax.swing.*;
import java.awt.*;

public class JavaSupportTab extends JPanel {

    private Font appFont;

    public JavaSupportTab(){
        setFontSize();
        JLabel text = new JLabel("Hej");
        this.add(text);
        this.setVisible(true);
    }

    private void setFontSize() {
        appFont = new Font("serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().height / 50);
    }

}
