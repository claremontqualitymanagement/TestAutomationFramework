package se.claremont.taf.javasupport.gui;

import se.claremont.taf.gui.Gui;
import se.claremont.taf.gui.createtesttab.TestStepListManager;
import se.claremont.taf.gui.guistyle.*;
import se.claremont.taf.gui.plugins.IGuiTab;
import se.claremont.taf.gui.teststructure.TestStep;
import se.claremont.taf.gui.teststructure.TestStepList;
import se.claremont.taf.testcase.TestCase;
import se.claremont.taf.javasupport.applicationundertest.ApplicationUnderTest;
import se.claremont.taf.javasupport.applicationundertest.applicationstarters.ApplicationStartMechanism;
import se.claremont.taf.javasupport.gui.applicationdeclarationwindow.DeclareApplicationDialog;
import se.claremont.taf.javasupport.gui.guirecordingwindow.RecordWindow;
import se.claremont.taf.javasupport.gui.guispywindow.GuiSpyingWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JavaSupportTab implements IGuiTab{

    private Font appFont;
    JFrame parentWindow;
    TafPanel panel;
    private TafTextArea explanationText = new TafTextArea("ExplanationText");
    private TestStepListManager testStepListManager = new TestStepListManager("^Java.*", getCurrentApplicationFrame());
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

        Gui.addChangeListenerToListOfAvailableTestSteps(new TestStepList.TestStepListChangeListener() {
            @Override
            public void isAdded(TestStep testStep) {
                testStepListManager.update();
            }

            @Override
            public void isRemoved(TestStep testStep) {
                testStepListManager.update();
            }
        });


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
                                .addComponent(testStepListManager)
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
                        .addComponent(testStepListManager)
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

    private TafFrame getCurrentApplicationFrame(){
        TafFrame applicationFrame = null;
        if(StandaloneJavaSupportGui.window != null){
            applicationFrame = StandaloneJavaSupportGui.window;
        } else {
            applicationFrame = Gui.applicationWindow;
        }
        return applicationFrame;
    }

    public String getName(){
        return "Java";
    }

    private void setFontSize() {
        appFont = new Font("serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().height / 50);
    }

}
