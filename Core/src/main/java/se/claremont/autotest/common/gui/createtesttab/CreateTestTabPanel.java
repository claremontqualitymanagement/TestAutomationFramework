package se.claremont.autotest.common.gui.createtesttab;

import se.claremont.autotest.common.gui.guistyle.AppFont;
import se.claremont.autotest.common.gui.guistyle.TafButton;
import se.claremont.autotest.common.gui.guistyle.TafLabel;
import se.claremont.autotest.common.gui.guistyle.TafPanel;
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
    private TafButton declareApplicationButton = new TafButton("Declare application");

    public CreateTestTabPanel(){

        tabPanel = new TafPanel("CreateTestPanel");
        GroupLayout groupLayout = new GroupLayout(tabPanel);
        tabPanel.setLayout(new GridLayout(2, 1));
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        TafLabel headline = new TafLabel("Hey, you are aware recorded scripts only work for demo purposes? They don't count for normal deviations in execution flow or data. Hence they only should be used for templates for proper test cases.");
        headline.setName("TabText");

        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResource("logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ImagePanel imagePanel = new ImagePanel(image);
        JLabel logoImage = new JLabel(new ImageIcon(image));
        declareApplicationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeclareApplicationDialog declareApplicationDialog = new DeclareApplicationDialog();
            }
        });
        tabPanel.add(logoImage);
        tabPanel.add(headline);
        tabPanel.add(declareApplicationButton);
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
