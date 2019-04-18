package se.claremont.taf.websupport.gui;

import se.claremont.taf.gui.Gui;
import se.claremont.taf.gui.createtesttab.TestStepListManager;
import se.claremont.taf.gui.guistyle.*;
import se.claremont.taf.gui.plugins.IGuiTab;
import se.claremont.taf.gui.teststructure.TestStep;
import se.claremont.taf.gui.teststructure.TestStepList;
import se.claremont.taf.websupport.gui.recorder.RecorderWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WebSupportTabPanel implements IGuiTab{
    TafPanel panel = null;
    TafHeadline headline = new TafHeadline("Web support");
    TafLabel text = new TafLabel("Web is supported through code, for now.");
    TafButton recordButton = new TafButton("Record");
    TafFrame parentWindow;

    public WebSupportTabPanel(){
        if(StandaloneWebSupportGui.frame == null){
            parentWindow = Gui.applicationWindow;
        } else {
            parentWindow = StandaloneWebSupportGui.frame;
        }

        panel = new TafPanel("WebSupportTabPanel");

        recordButton.setMnemonic('r');
        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecorderWindow recorderWindow = new RecorderWindow(parentWindow);
            }
        });
        TafFrame application;
        if(StandaloneWebSupportGui.frame == null){
            application = Gui.applicationWindow;
        } else{
            application = StandaloneWebSupportGui.frame;
        }

        TestStepListManager testStepListManager = new TestStepListManager("^Web.*", application);
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

        GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);

        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(headline)
                                .addComponent(text)
                                .addComponent(testStepListManager)
                                .addComponent(recordButton)
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addComponent(text)
                        .addComponent(testStepListManager)
                        .addComponent(recordButton)
        );

    }

    @Override
    public JPanel getPanel() {
        if(panel == null) return new WebSupportTabPanel().panel;
        return panel;
    }

    @Override
    public String getName() {
        return "Web";
    }
}
