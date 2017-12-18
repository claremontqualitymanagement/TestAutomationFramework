package se.claremont.autotest.common.gui.createtesttab;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.common.gui.plugins.IGuiTab;
import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepList;
import se.claremont.autotest.common.gui.teststructure.TestStepListPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

public class CreateTestTabPanel implements IGuiTab{

    private TafPanel tabPanel;
    private TafCloseButton closeButton;

    public CreateTestTabPanel(){
        TafHeadline headline = new TafHeadline("Create test");
        closeButton = new TafCloseButton(Gui.applicationWindow);
        closeButton.setMnemonic('c');
        tabPanel = new TafPanel("TestCreationPanel");
        GroupLayout groupLayout = new GroupLayout(tabPanel);
        tabPanel.setLayout(new GridLayout(2, 1));
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        JTextArea textArea = new JTextArea("Hey, you are aware recorded scripts only work for demo purposes? They don't count for normal deviations in execution flow or data. Hence they only should be used for templates for proper test cases.");
        textArea.setLineWrap(true);
        textArea.setFont(AppFont.getInstance());
        textArea.setName("TabText");

        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResource("logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ImagePanel imagePanel = new ImagePanel(image);
        JLabel logoImage = new JLabel(new ImageIcon(image));

        //TestStepListPanel testStepListPanel = new TestStepListPanel(new LinkedList<>(), Gui.availableTestSteps);
        TafTextArea availableTestStepListTextArea = new TafTextArea("AvailableTestStepListTextArea");
        Gui.availableTestSteps.addChangeListener(new TestStepList.TestStepListChangeListener() {
            @Override
            public void isAdded(TestStep testStep) {
                StringBuilder text = new StringBuilder();
                for(TestStep ts : Gui.availableTestSteps.getTestSteps()){
                    text.append(ts.getName()).append(System.lineSeparator());
                }
                availableTestStepListTextArea.setText(text.toString());
                availableTestStepListTextArea.revalidate();
                availableTestStepListTextArea.repaint();
                tabPanel.revalidate();
                tabPanel.repaint();
            }

            @Override
            public void isRemoved(TestStep testStep) {
                StringBuilder text = new StringBuilder();
                for(TestStep ts : Gui.availableTestSteps.getTestSteps()){
                    text.append(ts.getName()).append(System.lineSeparator());
                }
                availableTestStepListTextArea.setText(text.toString());
                availableTestStepListTextArea.revalidate();
                availableTestStepListTextArea.repaint();
                tabPanel.revalidate();
                tabPanel.repaint();
            }
        });
        JScrollPane testStepScrollPane = new JScrollPane(availableTestStepListTextArea);
        tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));
        //tabPanel.add(logoImage);
        tabPanel.add(headline);
        tabPanel.add(textArea);
        tabPanel.add(testStepScrollPane);
        //tabPanel.add(testStepListPanel);
        //tabPanel.add(closeButton);
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
