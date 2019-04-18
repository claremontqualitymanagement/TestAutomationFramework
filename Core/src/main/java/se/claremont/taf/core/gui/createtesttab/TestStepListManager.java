package se.claremont.taf.core.gui.createtesttab;

import se.claremont.taf.core.gui.Gui;
import se.claremont.taf.core.gui.guistyle.*;
import se.claremont.taf.core.gui.teststructure.SubProcedureTestStep;
import se.claremont.taf.core.gui.teststructure.TestCaseManager;
import se.claremont.taf.core.gui.teststructure.TestStep;
import se.claremont.taf.core.gui.teststructure.TestStepResult;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public final class TestStepListManager extends JPanel {

    JPanel p = new JPanel(new GridLayout(2, 2, 10, 0));
    TafLabel testCaseTestStepsLabel = new TafLabel("Test case test steps");
    TafLabel availableTestStepsLabel = new TafLabel("Available test steps");
    JScrollPane testCaseTestStepListScrollPanel;
    JScrollPane avalableTestStepsListScrollPanel;
    TafButton mergeTestStepsButton = new TafButton("Merge");
    TafButton trialRunButton = new TafButton("Try run");
    TafButton generateCodeButton = new TafButton("Generate code");
    TafButton removeSelectedTestStepsButton = new TafButton("Remove");
    TafButton splitSubTestStepButton = new TafButton("Split");
    TafButton cloneTestStepButton = new TafButton("Clone");
    TransferHandler h = new ListItemTransferHandler();
    DefaultListModel<TestStep> testCaseTestStepListModel = new DefaultListModel<>();
    TestStepList testCaseTestStepList = new TestStepList(testCaseTestStepListModel, h, true);
    DefaultListModel<TestStep> availableTestStepsListModel = new DefaultListModel<>();
    TestStepList availableTestStepsList = new TestStepList(availableTestStepsListModel, h, false);
    String testStepTypeFilterAsRegex;
    TafFrame applicationFrame;

    public TestStepListManager(String testStepTypeFilterAsRegex, TafFrame applicationFrame) {
        super(new BorderLayout());
        this.applicationFrame = applicationFrame;
        this.testStepTypeFilterAsRegex = testStepTypeFilterAsRegex;
        this.setBackground(TafGuiColor.backgroundColor);
        p.setBackground(TafGuiColor.backgroundColor);
        GroupLayout groupLayout = new GroupLayout(p);
        p.setLayout(groupLayout);
        testCaseTestStepListScrollPanel = new JScrollPane(createTestCaseTestStepsList(h));
        avalableTestStepsListScrollPanel = new JScrollPane(createAvailableTestStepsList(h));

        TafPanel chosenTestStepsButtonPanel = new TafPanel("ChosenTestStepsButtonPanel");

        trialRunButton.setEnabled(false);
        trialRunButton.setToolTipText("Attempts to run the defined test steps in the left panel");
        trialRunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SubProcedureTestStep trialRunTestAction = new SubProcedureTestStep("Test run", "Test run from TAF GUI");
                for (int i = 0; i < testCaseTestStepList.getModel().getSize(); i++) {
                    TestStep testStep = testCaseTestStepList.getModel().getElementAt(i);
                    trialRunTestAction.addTestStep(testStep);
                }
                TestStepResult trialRunResult = trialRunTestAction.execute();
                System.out.println(trialRunResult.getResult().getFriendlyName());
            }
        });

        generateCodeButton.setEnabled(false);
        generateCodeButton.setToolTipText("Generates the initial code corresponding to the test steps in the left panel.");
        generateCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SubProcedureTestStep trialRunTestAction = new SubProcedureTestStep("Test run", "Test run from TAF GUI");
                for (int i = 0; i < testCaseTestStepList.getModel().getSize(); i++) {
                    TestStep testStep = testCaseTestStepList.getModel().getElementAt(i);
                    trialRunTestAction.addTestStep(testStep);
                }
                TestCaseManager.testSetCode.addTestCodeFromTestSteps(trialRunTestAction);
                System.out.println(TestCaseManager.testSetCode.asCode());
                TestCaseManager.testSetCode.displayInFrame();
            }
        });
        chosenTestStepsButtonPanel.add(trialRunButton);
        chosenTestStepsButtonPanel.add(generateCodeButton);

        TafPanel avaliableTestStepsButtonPanel = new TafPanel("AvaliableTestStepsButtonPanel");
        avaliableTestStepsButtonPanel.add(mergeTestStepsButton);
        avaliableTestStepsButtonPanel.add(splitSubTestStepButton);
        avaliableTestStepsButtonPanel.add(cloneTestStepButton);
        avaliableTestStepsButtonPanel.add(removeSelectedTestStepsButton);

        mergeTestStepsButton.setEnabled(false);
        mergeTestStepsButton.setToolTipText("Merge test steps to reusable test action.");
        mergeTestStepsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mergeSelectedSteps();
                update();
            }
        });

        splitSubTestStepButton.setEnabled(false);
        splitSubTestStepButton.setToolTipText("Splits a test action (merged test steps) to their initial test steps.");
        splitSubTestStepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestStep testStep = (TestStep)availableTestStepsList.getSelectedValuesList().get(0);
                SubProcedureTestStep sub = (SubProcedureTestStep)testStep;
                for(TestStep step : sub.testSteps){
                    Gui.addTestStepToListOfAvailableTestSteps(step);
                }
                Gui.removeTestStepFromListOfAvailableTestSteps(availableTestStepsList.getSelectedValue());
            }
        });

        cloneTestStepButton.setEnabled(false);
        cloneTestStepButton.setToolTipText("Makes a copy of selected test step.");
        cloneTestStepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(TestStep testStep : Gui.getAvailableTestSteps()){
                    System.out.println(testStep);
                }
                TestStep testStep = availableTestStepsList.getSelectedValue();
                if(testStep != null) Gui.addTestStepToListOfAvailableTestSteps(testStep.clone());
            }
        });

        removeSelectedTestStepsButton.setEnabled(false);
        removeSelectedTestStepsButton.setToolTipText("Removes selected test step(s). Only applicable in the right panel.");
        removeSelectedTestStepsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(TestStep testStep : availableTestStepsList.getSelectedValuesList()){
                    Gui.removeTestStepFromListOfAvailableTestSteps(testStep);
                }
                update();
            }
        });


        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                //                        .addComponent(headline)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addGroup(groupLayout.createParallelGroup()
                                                .addComponent(testCaseTestStepsLabel)
                                                .addComponent(testCaseTestStepListScrollPanel)
                                        )
                                        .addGroup(groupLayout.createParallelGroup()
                                                .addComponent(availableTestStepsLabel)
                                                .addComponent(avalableTestStepsListScrollPanel)
                                        )
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(chosenTestStepsButtonPanel)
                                        .addComponent(avaliableTestStepsButtonPanel)
                                )
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        //                .addComponent(headline)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(testCaseTestStepsLabel)
                                .addComponent(availableTestStepsLabel)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(testCaseTestStepListScrollPanel)
                                .addComponent(avalableTestStepsListScrollPanel)
                        )
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(chosenTestStepsButtonPanel)
                                .addComponent(avaliableTestStepsButtonPanel)
                        )
        );

        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        add(p);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private synchronized void mergeSelectedSteps() {
        TafDialog mergeDialog = new TafDialog(applicationFrame, "TAF - Merge test steps to test action", true);

        TafLabel testStepNameLabel = new TafLabel("Test step name:");
        TafTextField testStepNameTextField = new TafTextField(" < Test step name >");

        TafCheckbox keepOriginalTestStepsInList = new TafCheckbox("Keep original test steps in list");
        keepOriginalTestStepsInList.setSelected(true);

        TafCloseButton closeButton = new TafCloseButton(mergeDialog);
        closeButton.setText("Cancel");

        TafButton mergeButton = new TafButton("Merge");
        mergeButton.setEnabled(false);
        mergeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String description = "<html><body>: Test action containing steps:<ol>";
                for (TestStep testStep : availableTestStepsList.getSelectedValuesList()) {
                    description += "<li>" + testStep.getName() + "</li>";
                }
                description += "</ol></body></html>";
                SubProcedureTestStep newTestStep = new SubProcedureTestStep(testStepNameTextField.getText(), description);
                for (TestStep testStep : availableTestStepsList.getSelectedValuesList()) {
                    newTestStep.addTestStep(testStep);
                }
                if (!keepOriginalTestStepsInList.isSelected()) {
                    for (TestStep testStep : availableTestStepsList.getSelectedValuesList()) {
                        Gui.removeTestStepFromListOfAvailableTestSteps(testStep);
                    }
                }
                Gui.addTestStepToListOfAvailableTestSteps(newTestStep);
                mergeDialog.setVisible(false);
                mergeDialog.dispose();
            }
        });

        testStepNameTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (testStepNameTextField.isChangedFromDefault() && testStepNameTextField.getText().length() > 0) {
                    mergeButton.setEnabled(true);
                } else {
                    mergeButton.setEnabled(false);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (testStepNameTextField.isChangedFromDefault() && testStepNameTextField.getText().length() > 0) {
                    mergeButton.setEnabled(true);
                } else {
                    mergeButton.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (testStepNameTextField.isChangedFromDefault() && testStepNameTextField.getText().length() > 0) {
                    mergeButton.setEnabled(true);
                } else {
                    mergeButton.setEnabled(false);
                }
            }
        });

        mergeDialog.setLayout(new BoxLayout(mergeDialog.getContentPane(), BoxLayout.Y_AXIS));
        mergeDialog.getContentPane().add(testStepNameLabel);
        mergeDialog.getContentPane().add(testStepNameTextField);
        mergeDialog.getContentPane().add(keepOriginalTestStepsInList);
        mergeDialog.getContentPane().add(mergeButton);
        mergeDialog.getContentPane().add(closeButton);
        mergeDialog.setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 4, Toolkit.getDefaultToolkit().getScreenSize().height / 3);
        mergeDialog.setVisible(true);
    }

    public synchronized void update() {
        testCaseTestStepListScrollPanel = new JScrollPane(createTestCaseTestStepsList(h));
        avalableTestStepsListScrollPanel = new JScrollPane(createAvailableTestStepsList(h));
        this.revalidate();
        this.repaint();
    }

    public synchronized void updateByFilterUpdate(String pattern) {
        if (pattern == null || pattern.length() == 0) {
            testStepTypeFilterAsRegex = null;
        } else {
            testStepTypeFilterAsRegex = ".*" + pattern + ".*";
        }
        update();
    }

    class TestStepList extends JList<TestStep> {
        private TransferHandler handler;
        private boolean isRunnable;

        TestStepList(ListModel listModel, TransferHandler handler, boolean isRunnable) {
            super(listModel);
            this.isRunnable = isRunnable;
            this.handler = handler;
            this.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    ((JLabel) c).setForeground(TafGuiColor.textColor);
                    ((JLabel) c).setFont(AppFont.getInstance());
                    return c;
                }
            });
            this.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            this.setDropMode(DropMode.INSERT);
            this.setDragEnabled(true);
            this.setTransferHandler(handler);
            ToolTipManager.sharedInstance().registerComponent(this);
            this.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (getThis().getSelectedValuesList().size() > 1) {
                        mergeTestStepsButton.setEnabled(true);
                    } else {
                        mergeTestStepsButton.setEnabled(false);
                    }
                    if (isRunnable) {
                        if (getThis().getSelectedValuesList().size() > 0) {
                            generateCodeButton.setEnabled(true);
                            trialRunButton.setEnabled(true);
                        } else {
                            generateCodeButton.setEnabled(false);
                            trialRunButton.setEnabled(false);
                        }
                    } else {
                        if (getThis().getSelectedValuesList().size() > 0) {
                            removeSelectedTestStepsButton.setEnabled(true);
                        } else {
                            removeSelectedTestStepsButton.setEnabled(false);
                        }
                        if (getThis().getSelectedValuesList().size() == 1 && getThis().getSelectedValue().getTestStepTypeShortName().equals("Sub")) {
                            splitSubTestStepButton.setEnabled(true);
                        } else {
                            splitSubTestStepButton.setEnabled(false);
                        }
                        if (getThis().getSelectedValuesList().size() == 1) {
                            cloneTestStepButton.setEnabled(true);
                        } else {
                            cloneTestStepButton.setEnabled(false);
                        }
                    }
                }
            });
        }

        private TestStepList getThis() {
            return this;
        }

        public String getToolTipText(MouseEvent event) {
            try {
                Point p = event.getPoint();
                int location = locationToIndex(p);
                TestStep testStep = (TestStep) getModel().getElementAt(location);
                return "<html><div style=\"font-size: " + AppFont.getInstance().getSize() * 2 / 3 + "\">" + testStep.getTestStepTypeShortName() + " " + testStep.getDescription() + "</div></html>";
            } catch (Exception ignored) {
                //Empty lists genereate errors
            }
            return "";
        }
    }

    private synchronized JList<TestStep> createAvailableTestStepsList(TransferHandler handler) {
        availableTestStepsListModel.clear();
        int iterator = 0;
        for (int i = 0; i < Gui.getAvailableTestSteps().size(); i++) {
            TestStep testStep = Gui.getAvailableTestSteps().get(i);
            String testStepInfoString = testStep.toString();
            if (testStepTypeFilterAsRegex == null || testStepInfoString.matches(testStepTypeFilterAsRegex)) {
                availableTestStepsListModel.add(iterator, testStep);
                iterator++;
            }
            if (testStep.getClass().equals(SubProcedureTestStep.class)) {
                boolean allSubTestStepsPassesFilter = true;
                if (testStepTypeFilterAsRegex != null) {
                    for (TestStep subStep : ((SubProcedureTestStep) testStep).testSteps) {
                        if (testStep.getDescription() == null) testStep.setDescription("");
                        if (!subStep.getDescription().matches(testStepTypeFilterAsRegex)) {
                            allSubTestStepsPassesFilter = false;
                            break;
                        }
                    }
                }
                if (allSubTestStepsPassesFilter) {
                    availableTestStepsListModel.add(i, testStep);
                    iterator++;
                }
            }
        }

        availableTestStepsList = new TestStepList(availableTestStepsListModel, h, false);
        // Disable row Cut, Copy, Paste
        ActionMap map = availableTestStepsList.getActionMap();
        Action dummy = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { /* Dummy action */ }
        };
        map.put(TransferHandler.getCutAction().getValue(Action.NAME), dummy);
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME), dummy);
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME), dummy);

        return availableTestStepsList;
    }

    private synchronized JList<TestStep> createTestCaseTestStepsList(TransferHandler handler) {
        testCaseTestStepListModel.clear();
        for (int i = 0; i < CreateTestTabPanel.testCaseTestSteps.size(); i++) {
            TestStep testStep = CreateTestTabPanel.testCaseTestSteps.get(i);
            testCaseTestStepListModel.add(i, testStep);
        }
        testCaseTestStepList = new TestStepList(testCaseTestStepListModel, h, true);

        testCaseTestStepList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                ((JLabel) c).setForeground(TafGuiColor.textColor);
                ((JLabel) c).setFont(AppFont.getInstance());
                return c;
            }
        });
        testCaseTestStepList.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        testCaseTestStepList.setDropMode(DropMode.INSERT);
        testCaseTestStepList.setDragEnabled(true);
        testCaseTestStepList.setTransferHandler(handler);

        // Disable row Cut, Copy, Paste
        ActionMap map = testCaseTestStepList.getActionMap();
        Action dummy = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { /* Dummy action */ }
        };
        map.put(TransferHandler.getCutAction().getValue(Action.NAME), dummy);
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME), dummy);
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME), dummy);

        return testCaseTestStepList;
    }
}

// Demo - BasicDnD (The Java™ Tutorials > Creating a GUI With JFC/Swing > Drag and Drop and Data Transfer)
// https://docs.oracle.com/javase/tutorial/uiswing/dnd/basicdemo.html
class ListItemTransferHandler extends TransferHandler {
    protected final DataFlavor localObjectFlavor;
    protected JList<?> source;
    protected int[] indices;
    protected int addIndex = -1; // Location where items were added
    protected int addCount; // Number of items added.

    protected ListItemTransferHandler() {
        super();
        // localObjectFlavor = new ActivationDataFlavor(Object[].class, DataFlavor.javaJVMLocalObjectMimeType, "Array of items");
        localObjectFlavor = new DataFlavor(Object[].class, "Array of items");
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        source = (JList<?>) c;
        indices = source.getSelectedIndices();
        Object[] transferedObjects = source.getSelectedValuesList().toArray(new Object[0]);
        // return new DataHandler(transferedObjects, localObjectFlavor.getMimeType());
        return new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{localObjectFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return Objects.equals(localObjectFlavor, flavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                if (isDataFlavorSupported(flavor)) {
                    return transferedObjects;
                } else {
                    throw new UnsupportedFlavorException(flavor);
                }
            }
        };
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        return info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
    }

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.MOVE; // TransferHandler.COPY_OR_MOVE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        TransferHandler.DropLocation tdl = info.getDropLocation();
        if (!canImport(info) || !(tdl instanceof JList.DropLocation)) {
            return false;
        }
        JList.DropLocation dl = (JList.DropLocation) tdl;
        JList target = (JList) info.getComponent();
        DefaultListModel listModel = (DefaultListModel) target.getModel();
        // boolean insert = dl.isInsert();
        int max = listModel.getSize();
        int index = dl.getIndex();
        index = index < 0 ? max : index; // If it is out of range, it is appended to the end
        index = Math.min(index, max);
        addIndex = index;
        try {
            Object[] values = (Object[]) info.getTransferable().getTransferData(localObjectFlavor);
            for (Object o : values) {
                int i = index++;
                listModel.add(i, o);
                target.addSelectionInterval(i, i);
            }
            addCount = target.equals(source) ? values.length : 0;
            return true;
        } catch (UnsupportedFlavorException | IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        cleanup(c, action == TransferHandler.MOVE);
    }

    private void cleanup(JComponent c, boolean remove) {
        if (remove && Objects.nonNull(indices)) {
            // If we are moving items around in the same list, we
            // need to adjust the indices accordingly, since those
            // after the insertion point have moved.
            if (addCount > 0) {
                for (int i = 0; i < indices.length; i++) {
                    if (indices[i] >= addIndex) {
                        indices[i] += addCount;
                    }
                }
            }
            JList source = (JList) c;
            DefaultListModel model = (DefaultListModel) source.getModel();
            for (int i = indices.length - 1; i >= 0; i--) {
                model.remove(indices[i]);
            }
        }
        indices = null;
        addCount = 0;
        addIndex = -1;
    }
}