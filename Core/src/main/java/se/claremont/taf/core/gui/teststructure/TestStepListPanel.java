package se.claremont.taf.core.gui.teststructure;

import se.claremont.taf.core.gui.guistyle.TafButton;
import se.claremont.taf.core.gui.guistyle.TafCloseButton;
import se.claremont.taf.core.gui.guistyle.TafFrame;
import se.claremont.taf.core.gui.guistyle.TafPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class TestStepListPanel extends TafPanel {
    TafPanel listingsPanel = new TafPanel("Listingspanel");
    JScrollPane scrollPane = new JScrollPane(listingsPanel);
    TafPanel thisPanel;

    public TestStepListPanel(List<TestStep> selectedTestSteps, List<TestStep> additionalAvailableTestSteps){
        super("TestStepListPanel");
        scrollPane.setName("TestStepListingsScrollPane");
        populateListingsPanel(selectedTestSteps, additionalAvailableTestSteps);
        add(scrollPane);
    }

    private void populateListingsPanel(List<TestStep> selectedTestSteps, List<TestStep> additionalAvaliableTestSteps){

        listingsPanel.removeAll();
        //listingsPanel.setLayout(new GridLayout(selectedTestSteps.size(), 3));
        GridBagLayout gridBagLayout = new GridBagLayout();
        listingsPanel.setLayout(gridBagLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;


        List<TestStep> listForRemoval = new LinkedList<>(selectedTestSteps);
        List<TestStep> listForAddition = new LinkedList<>(additionalAvaliableTestSteps);

        if(selectedTestSteps.size() == 0){
            TafButton addTestStepButton = new TafButton("Select first test step");
            addTestStepButton.setMnemonic('S');
            addTestStepButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new SelectWindow(additionalAvaliableTestSteps);
                }
            });
            listingsPanel.add(addTestStepButton, constraints);
        }

        int i = 0;
        for(TestStep testStep : selectedTestSteps){
            constraints.anchor = GridBagConstraints.LINE_START;
            constraints.weightx = 0;
            constraints.gridx = 0;
            constraints.gridy = i;
            listingsPanel.add(testStep.guiComponent(), constraints);

            TafButton addButton = new TafButton("Add below");
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new SelectWindow(additionalAvaliableTestSteps);
                }
            });
            if(additionalAvaliableTestSteps.size() == 0) addButton.setEnabled(false);
            constraints.weightx = 0.5;
            constraints.gridx = 1;
            listingsPanel.add(addButton, constraints);

            TafButton removeButton = new TafButton("Remove");
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    listForAddition.add(testStep);
                    listForRemoval.remove(testStep);
                    populateListingsPanel(listForRemoval, listForAddition);
                }
            });
            constraints.gridx = 2;
            constraints.weightx = 1;
            listingsPanel.add(removeButton, constraints);
            i++;
        }
    }

    private class SelectWindow extends TafFrame{

        public SelectWindow(List<TestStep> additionalAvaliableTestSteps) {

            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            java.awt.List testStepListing = new java.awt.List();
            for (TestStep step : additionalAvaliableTestSteps) {
                testStepListing.add(step.getTestStepTypeShortName() + " " + step.getName());
            }
            this.getContentPane().add(testStepListing);

            this.getContentPane().add(new TafCloseButton(this));

            this.pack();
            this.setVisible(true);

        }
    }

}
