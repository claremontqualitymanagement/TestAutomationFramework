package se.claremont.autotest.common.testrun.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CreateTestTabPanel extends JPanel{

    private Font appFont;

    public CreateTestTabPanel(){
        GroupLayout groupLayout = new GroupLayout(this);
        this.setLayout(new GridLayout(2, 1));
        groupLayout.setAutoCreateGaps(true);

        setFontSize();

        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResource("logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ImagePanel imagePanel = new ImagePanel(image);
        JLabel logoImage = new JLabel(new ImageIcon(image));
        this.add(logoImage);
        JLabel headline = new JLabel("Hey, you are aware recorded scripts only work for demo purposes? They don't count for normal deviations in execution flow or data. Hence they only should be used for templates for proper test cases.");
        headline.setFont(appFont);
        this.add(headline);
        this.setVisible(true);
    }

    private void setFontSize() {
        appFont = new Font("serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().height / 50);
    }

}
