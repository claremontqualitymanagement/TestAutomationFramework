package se.claremont.autotest.javasupport.gui;

import se.claremont.autotest.common.gui.guistyle.TafButton;
import se.claremont.autotest.common.gui.guistyle.TafCloseButton;
import se.claremont.autotest.common.gui.plugins.IGuiTab;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.javasupport.applicationundertest.ApplicationUnderTest;
import se.claremont.autotest.javasupport.applicationundertest.applicationstarters.ApplicationStartMechanism;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JavaSupportTab implements IGuiTab{

    private Font appFont;
    JPanel panel;
    private TafButton declareApplicationButton = new TafButton("Declare application");
    private TafButton tryStartSutButton = new TafButton("Try start");

    static ApplicationStartMechanism applicationStartMechanism;

    public JavaSupportTab(){
        TestCase testCase = new TestCase();
        applicationStartMechanism = new ApplicationStartMechanism(testCase);
        panel = new JPanel();
        setFontSize();

        JLabel text = new JLabel("Java support tab");
        text.setFont(appFont);
        text.setName("MainText");

        declareApplicationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeclareApplicationDialog();
            }
        });

        tryStartSutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applicationStartMechanism.run();
            }
        });

        panel.add(text);
        panel.add(declareApplicationButton);
        panel.add(tryStartSutButton);

        panel.setVisible(true);
    }

    public JPanel getPanel(){
        return panel;
    }

    public String getName(){
        return "Java";
    }

    private void setFontSize() {
        appFont = new Font("serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().height / 50);
    }

}
