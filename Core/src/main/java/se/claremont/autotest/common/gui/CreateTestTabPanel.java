package se.claremont.autotest.common.gui;

import se.claremont.autotest.common.gui.guistyle.AppFont;
import se.claremont.autotest.common.gui.guistyle.TafLabel;
import se.claremont.autotest.common.gui.guistyle.TafPanel;
import se.claremont.autotest.common.gui.plugins.IGuiTab;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CreateTestTabPanel implements IGuiTab{

    private TafPanel tabPanel;

    public CreateTestTabPanel(){

        tabPanel = new TafPanel("CreateTestPanel");
        GroupLayout groupLayout = new GroupLayout(tabPanel);
        tabPanel.setLayout(new GridLayout(2, 1));
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResource("logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ImagePanel imagePanel = new ImagePanel(image);
        JLabel logoImage = new JLabel(new ImageIcon(image));

        tabPanel.add(logoImage);
        TafLabel headline = new TafLabel("Hey, you are aware recorded scripts only work for demo purposes? They don't count for normal deviations in execution flow or data. Hence they only should be used for templates for proper test cases.");
        headline.setName("TabText");
        tabPanel.add(headline);
        tabPanel.setVisible(true);
    }

    @Override
    public JPanel getPanel() {
        return tabPanel;
    }

    @Override
    public String getName() {
        return "Create test";
    }
}
