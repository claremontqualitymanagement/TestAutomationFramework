package se.claremont.autotest.restsupport.gui;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.gui.createtesttab.TestStepListManager;
import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.common.gui.plugins.IGuiTab;
import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepList;
import se.claremont.autotest.common.gui.teststructure.TestStepListPanel;
import se.claremont.autotest.common.testrun.CliTestRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class RestSupportTabPanel implements IGuiTab {

    TafPanel tafPanel = null;
    TafHeadline headline = new TafHeadline("REST");
    TafLabel text = new TafLabel("REST testing should not be done through the TAF GUI. Use the IDE and create a RestSupport object. Among other benefits this will unlock the verification mechanisms.");
    TafButton createRequestButton = new TafButton("Create request");

    public RestSupportTabPanel(){
        tafPanel = new TafPanel("RestSupportPanel");

        createRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateRequestWindow(getCurrentApplicationFrame());
            }
        });

        TestStepListManager restTestSteps = new TestStepListManager("^REST.*", getCurrentApplicationFrame());
        Gui.addChangeListenerToListOfAvailableTestSteps(new TestStepList.TestStepListChangeListener() {
            @Override
            public void isAdded(TestStep testStep) {
                restTestSteps.update();
            }

            @Override
            public void isRemoved(TestStep testStep) {
                restTestSteps.update();
            }
        });

        GroupLayout groupLayout = new GroupLayout(tafPanel);
        tafPanel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(headline)
                                .addComponent(text)
                                .addComponent(restTestSteps)
                                .addComponent(createRequestButton)
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addComponent(text)
                        .addComponent(restTestSteps)
                        .addComponent(createRequestButton)
        );

    }

    @Override
    public JPanel getPanel() {
        if(tafPanel == null) return new RestSupportTabPanel().tafPanel;
        return tafPanel;
    }

    private TafFrame getCurrentApplicationFrame(){
        TafFrame applicationFrame = null;
        if(StandaloneRestSupportGui.frame != null){
            applicationFrame = StandaloneRestSupportGui.frame;
        } else {
            applicationFrame = Gui.applicationWindow;
        }
        return applicationFrame;
    }

    @Override
    public String getName() {
        return "REST";
    }
}
