package se.claremont.autotest.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Gui extends JFrame{

    private Font appFont;

    Gui() {
        Container pane = this.getContentPane();
        setFontSize();
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(appFont);
        tabs.addTab("Run tests", new RunTestTabPanel(this));
        tabs.addTab("Create tests", new CreateTestTabPanel());

        pane.add(tabs);
        this.setSize(3 * Toolkit.getDefaultToolkit().getScreenSize().width / 4, Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        this.setTitle("TAF - Test Automation Framework");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public void activate() {
        this.setVisible(true);
    }

    private void setFontSize() {
        appFont = new Font("serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().height / 50);
    }

}
