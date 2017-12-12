package se.claremont.autotest.restsupport.gui;

import se.claremont.autotest.common.gui.guistyle.TafButton;
import se.claremont.autotest.common.gui.guistyle.TafHeadline;
import se.claremont.autotest.common.gui.guistyle.TafLabel;
import se.claremont.autotest.common.gui.guistyle.TafPanel;
import se.claremont.autotest.common.gui.plugins.IGuiTab;
import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepListPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class RestSupportTabPanel implements IGuiTab {

    TafPanel tafPanel = null;
    TafHeadline headline = new TafHeadline("REST");
    TafLabel text = new TafLabel("REST support is yet code based. Use the IDE and create a RestSupport object.");
    TafButton createRequestButton = new TafButton("Create request");

    public RestSupportTabPanel(){
        tafPanel = new TafPanel("RestSupportPanel");

        createRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateRequestWindow();
            }
        });

        List<TestStep> selectedTestSteps = new LinkedList<>();
        selectedTestSteps.add(new RestTestStep("PUT request", "PUT request to http://testserver.se/endpoint"));
        selectedTestSteps.add(new RestTestStep("POST request", "POST request to http://testserver.se/endpoint with data 'khdsfksdhf'"));
        List<TestStep> additionalAvaliableTestSteps = new LinkedList<>();
        additionalAvaliableTestSteps.add(new RestTestStep("GET request", "GET request to http://testserver.se/endpoint"));
        TestStepListPanel testStepListPanel = new TestStepListPanel(selectedTestSteps, additionalAvaliableTestSteps);

        GroupLayout groupLayout = new GroupLayout(tafPanel);
        tafPanel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(headline)
                                .addComponent(text)
                                .addComponent(testStepListPanel)
                                .addComponent(createRequestButton)
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addComponent(text)
                        .addComponent(testStepListPanel)
                        .addComponent(createRequestButton)
        );

    }

    @Override
    public JPanel getPanel() {
        if(tafPanel == null) return new RestSupportTabPanel().tafPanel;
        return tafPanel;
    }

    @Override
    public String getName() {
        return "REST";
    }
}
