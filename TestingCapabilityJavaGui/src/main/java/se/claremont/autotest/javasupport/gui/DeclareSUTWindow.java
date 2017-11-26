package se.claremont.autotest.javasupport.gui;

import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.javasupport.applicationundertest.ApplicationUnderTest;
import se.claremont.autotest.javasupport.applicationundertest.applicationstarters.ApplicationStartMechanism;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeclareSUTWindow {
    ApplicationStartMechanism asm;
    ApplicationUnderTest aut;

    public DeclareSUTWindow(Font appFont) {

        TestCase testCase = new TestCase();
        asm  = new ApplicationStartMechanism(testCase);
        aut = new ApplicationUnderTest(testCase, asm);

        JFrame sutDeclarationWindow = new JFrame();
        sutDeclarationWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        sutDeclarationWindow.setTitle("TAF - Declare system under test (Java)");
        sutDeclarationWindow.setName("SutDeclarationWindow");

        Container pane = sutDeclarationWindow.getContentPane();
        pane.setName("ContentPane");
        pane.setLayout(new FlowLayout());

        JLabel headline = new JLabel("Declaring the system under test");
        headline.setFont(appFont);
        headline.setName("HeadlineLabel");

        JLabel cliLabel = new JLabel("Command line:");
        cliLabel.setName("CliLabel");
        cliLabel.setFont(appFont);

        JTextField cli = new JTextField();
        cli.setName("CliCommandTextField");
        cli.setFont(appFont);

        JButton closeButton = new JButton("Close");
        closeButton.setName("CloseButton");
        closeButton.setFont(appFont);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sutDeclarationWindow.dispose();
            }
        });

        JButton systemPropertiesButton = new JButton("System properties");
        systemPropertiesButton.setName("SystemPropertiesButton");
        systemPropertiesButton.setFont(appFont);

        pane.add(headline);
        pane.add(cliLabel);
        pane.add(cli);
        pane.add(closeButton);
        pane.add(systemPropertiesButton);

        pane.setVisible(true);
        sutDeclarationWindow.pack();
        sutDeclarationWindow.setVisible(true);

    }
}
