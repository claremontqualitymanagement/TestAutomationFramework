package se.claremont.taf.core.gui.createtesttab;

import se.claremont.taf.core.gui.Gui;
import se.claremont.taf.core.gui.guistyle.*;
import se.claremont.taf.core.gui.plugins.IGuiTab;
import se.claremont.taf.core.gui.teststructure.TestStep;
import se.claremont.taf.core.gui.teststructure.TestStepList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;

public class CreateTestTabPanel implements IGuiTab {

    private TafPanel tabPanel;
    private TafCloseButton closeButton;
    private TafLabel searchLabel = new TafLabel("Test step filter:");
    private TafTextField searchField = new TafTextField(" < Pattern > ");
    public static java.util.List<TestStep> testCaseTestSteps = new ArrayList<>();

    public CreateTestTabPanel() {
        TafHeadline headline = new TafHeadline("Create test");
        closeButton = new TafCloseButton(Gui.applicationWindow);
        closeButton.setMnemonic('c');
        tabPanel = new TafPanel("TestCreationPanel");
        GroupLayout groupLayout = new GroupLayout(tabPanel);
        tabPanel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        TafTextArea textArea = new TafTextArea("TabText");
        textArea.setText("Hey, you are aware recorded capturemanagers only work for demo purposes? They don't count for normal deviations in execution flow or data. Hence they only should be used for templates for proper test cases.");
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane testAreaScrollPane = new JScrollPane(textArea);

        TestStepListManager testStepListManager = new TestStepListManager(null, Gui.applicationWindow);

        searchLabel.setLabelFor(searchField);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (searchField.isChangedFromDefault() && searchField.getText().length() > 0) {
                    testStepListManager.updateByFilterUpdate(searchField.getText());
                } else {
                    testStepListManager.updateByFilterUpdate(null);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (searchField.isChangedFromDefault() && searchField.getText().length() > 0) {
                    testStepListManager.updateByFilterUpdate(searchField.getText());
                } else {
                    testStepListManager.updateByFilterUpdate(null);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (searchField.isChangedFromDefault() && searchField.getText().length() > 0) {
                    testStepListManager.updateByFilterUpdate(searchField.getText());
                } else {
                    testStepListManager.updateByFilterUpdate(null);
                }
            }
        });

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

        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(headline)
                                .addComponent(testAreaScrollPane)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(searchLabel)
                                        .addComponent(searchField)
                                )
                                .addComponent(testStepListManager)
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(headline)
                            .addComponent(testAreaScrollPane)
                            .addGroup(groupLayout.createParallelGroup()
                                    .addComponent(searchLabel)
                                    .addComponent(searchField)
                            )
                            .addComponent(testStepListManager)
                )
        );

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
