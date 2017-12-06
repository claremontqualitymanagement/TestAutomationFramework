package se.claremont.autotest.common.gui.createtesttab;

import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.common.gui.plugins.IGuiTab;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CreateTestTabPanel implements IGuiTab{

    private TafPanel tabPanel;
    private TafCloseButton closeButton;

    public CreateTestTabPanel(TafFrame parentFrame){
        closeButton = new TafCloseButton(parentFrame);
        tabPanel = new TafPanel("CreateTestPanel");
        GroupLayout groupLayout = new GroupLayout(tabPanel);
        tabPanel.setLayout(new GridLayout(2, 1));
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        JTextArea headline = new JTextArea("Hey, you are aware recorded scripts only work for demo purposes? They don't count for normal deviations in execution flow or data. Hence they only should be used for templates for proper test cases.");
        headline.setLineWrap(true);
        headline.setName("TabText");

        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResource("logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ImagePanel imagePanel = new ImagePanel(image);
        JLabel logoImage = new JLabel(new ImageIcon(image));
        tabPanel.add(logoImage);
        tabPanel.add(headline);
        tabPanel.add(closeButton);
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
