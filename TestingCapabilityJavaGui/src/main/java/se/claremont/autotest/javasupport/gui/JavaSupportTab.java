package se.claremont.autotest.javasupport.gui;

import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.common.gui.plugins.IGuiTab;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.javasupport.applicationundertest.ApplicationUnderTest;
import se.claremont.autotest.javasupport.applicationundertest.applicationstarters.ApplicationStartMechanism;
import se.claremont.autotest.javasupport.gui.applicationdeclarationwindow.DeclareApplicationDialog;
import se.claremont.autotest.javasupport.gui.guirecordingwindow.RecordWindow;
import se.claremont.autotest.javasupport.gui.guispywindow.GuiSpyingWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JavaSupportTab implements IGuiTab{

    private Font appFont;
    JFrame parentWindow;
    TafPanel panel;
    private TafTextArea explanationText = new TafTextArea("ExplanationText");
    private TafButton declareApplicationButton = new TafButton("Declare application");
    private TafButton tryStartSutButton = new TafButton("Start application");
    private TafButton guiSpyButton = new TafButton("GUI Spy");
    private TafButton recordScriptButton = new TafButton("Record script");

    public static ApplicationUnderTest applicationUnderTest;

    public JavaSupportTab(JFrame parentWindow){
        this.parentWindow = parentWindow;
        TestCase testCase = new TestCase();
        applicationUnderTest = new ApplicationUnderTest(testCase, new ApplicationStartMechanism(testCase));
        panel = new TafPanel("JavaSupportTabPanal");
        GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);
        setFontSize();

        TafLabel text = new TafLabel("Java support tab");
        explanationText.setText("Testing a java application is a bit tricky since the JVM (Java Virtual Machine) " +
                "that runs the pre-compiled program is a closed box." + System.lineSeparator() + "In TAF we mitigate " +
                "that by starting the application from the test code, but in a child classloader. This makes the " +
                "application accessible for TAF, without compromising the context for the system under test.");
        explanationText.setLineWrap(true);
        explanationText.setSize(parentWindow.getWidth() * 9/10, parentWindow.getHeight() /2);
        JScrollPane explanationtextScrollPane = new JScrollPane(explanationText);
        explanationtextScrollPane.setName("ExplanationTextScrollBar");

        declareApplicationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeclareApplicationDialog();
            }
        });

        tryStartSutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applicationUnderTest.start();
            }
        });

        guiSpyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiSpyingWindow guiSpyingWindow = new GuiSpyingWindow();
            }
        });

        recordScriptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecordWindow recordWindow = new RecordWindow();
            }
        });

        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(text)
                                .addComponent(explanationtextScrollPane)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(declareApplicationButton)
                                        .addComponent(tryStartSutButton)
                                        .addComponent(guiSpyButton)
                                        .addComponent(recordScriptButton)
                                )
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(text)
                        .addComponent(explanationtextScrollPane)
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(declareApplicationButton)
                                .addComponent(tryStartSutButton)
                                .addComponent(guiSpyButton)
                                .addComponent(recordScriptButton)
                        )
        );
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
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
