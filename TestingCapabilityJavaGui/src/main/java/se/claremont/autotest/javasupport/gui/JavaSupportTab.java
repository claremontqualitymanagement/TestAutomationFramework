package se.claremont.autotest.javasupport.gui;

import se.claremont.autotest.common.gui.Gui;
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

    public JavaSupportTab(){
        this(Gui.applicationWindow);
    }

    public JavaSupportTab(JFrame parentWindow){

        TestCase testCase = new TestCase();
        applicationUnderTest = new ApplicationUnderTest(testCase, new ApplicationStartMechanism(testCase));
        if(Gui.preferences.get("LastJavaSutApplicationDescription") != null)
            applicationUnderTest = (ApplicationUnderTest)Gui.preferences.get("LastJavaSutApplicationDescription");

        this.parentWindow = parentWindow;
        panel = new TafPanel("JavaSupportTabPanal");
        GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);
        setFontSize();

        TafHeadline text = new TafHeadline("Java support tab");
        explanationText.setText("Testing a java application is a bit tricky since the JVM (Java Virtual Machine) " +
                "that runs the pre-compiled program is a closed box." + System.lineSeparator() + "In TAF we mitigate " +
                "that by starting the application from the test code, but in a child classloader. This makes the " +
                "application accessible for TAF, without compromising the context for the system under test.");
        explanationText.setLineWrap(true);
        if(parentWindow != null)
            explanationText.setSize(parentWindow.getWidth() * 9/10, parentWindow.getHeight() /2);
        JScrollPane explanationtextScrollPane = new JScrollPane(explanationText);
        explanationtextScrollPane.setName("ExplanationTextScrollBar");

        declareApplicationButton.setMnemonic('d');
        declareApplicationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeclareApplicationDialog();
            }
        });

        tryStartSutButton.setMnemonic('s');
        tryStartSutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applicationUnderTest.start();
            }
        });

        guiSpyButton.setMnemonic('g');
        guiSpyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiSpyingWindow guiSpyingWindow = new GuiSpyingWindow();
            }
        });

        recordScriptButton.setMnemonic('r');
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
        if(panel == null) return new JavaSupportTab().panel;
        return panel;
    }


    public String getName(){
        return "Java";
    }

    private void setFontSize() {
        appFont = new Font("serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().height / 50);
    }

}
