package se.claremont.autotest.common.gui.createtesttab;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.common.gui.teststructure.SubProcedureTestStep;
import se.claremont.autotest.common.gui.teststructure.TestCaseManager;
import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public final class TestStepListManager extends JPanel{

    JPanel p = new JPanel(new GridLayout(2, 2, 10, 0));
    TafLabel testCaseTestStepsLabel = new TafLabel("Test case test steps");
    TafLabel availableTestStepsLabel = new TafLabel("Available test steps");
    JScrollPane testCaseTestStepListScrollPanel;
    JScrollPane avalableTestStepsListScrollPanel;
    TafButton mergeTestStepsButton = new TafButton("Merge selected test steps to a test action");
    TafButton trialRunButton = new TafButton("Try run");
    TafButton generateCodeButton = new TafButton("Generate code");
    TransferHandler h = new ListItemTransferHandler();
    DefaultListModel<TestStep> testCaseTestStepListModel = new DefaultListModel<>();
    TestStepList testCaseTestStepList = new TestStepList(testCaseTestStepListModel, h, true);
    DefaultListModel<TestStep> availableTestStepsListModel = new DefaultListModel<>();
    TestStepList availableTestStepsList = new TestStepList(availableTestStepsListModel, h, false);
    String testStepTypeFilterAsRegex;

    public TestStepListManager(String testStepTypeFilterAsRegex) {
        super(new BorderLayout());
        this.testStepTypeFilterAsRegex = testStepTypeFilterAsRegex;
        this.setBackground(TafGuiColor.backgroundColor);
        p.setBackground(TafGuiColor.backgroundColor);
        GroupLayout groupLayout = new GroupLayout(p);
        p.setLayout(groupLayout);
        testCaseTestStepListScrollPanel = new JScrollPane(createTestCaseTestStepsList(h));
        avalableTestStepsListScrollPanel = new JScrollPane(createAvailableTestStepsList(h));

        mergeTestStepsButton.setEnabled(false);
        trialRunButton.setEnabled(false);
        trialRunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SubProcedureTestStep trialRunTestAction = new SubProcedureTestStep("Test run", "Test run from TAF GUI");
                for(int i = 0; i < testCaseTestStepList.getModel().getSize(); i++){
                    TestStep testStep = testCaseTestStepList.getModel().getElementAt(i);
                    trialRunTestAction.addTestStep(testStep);
                }
                TestStepResult trialRunResult = trialRunTestAction.execute();
                System.out.println(trialRunResult.getResult().getFriendlyName());
            }
        });
        generateCodeButton.setEnabled(false);
        generateCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SubProcedureTestStep trialRunTestAction = new SubProcedureTestStep("Test run", "Test run from TAF GUI");
                for(int i = 0; i < testCaseTestStepList.getModel().getSize(); i++){
                    TestStep testStep = testCaseTestStepList.getModel().getElementAt(i);
                    trialRunTestAction.addTestStep(testStep);
                }
                TestCaseManager.testSetCode.addTestCodeFromTestSteps(trialRunTestAction);
                System.out.println(TestCaseManager.testSetCode.asCode());
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
                                    .addComponent(mergeTestStepsButton)
                                    .addComponent(trialRunButton)
                                    .addComponent(generateCodeButton)
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
                            .addComponent(mergeTestStepsButton)
                            .addComponent(trialRunButton)
                            .addComponent(generateCodeButton)
                        )
        );

        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        add(p);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        //setPreferredSize(new Dimension(320, 240));
    }

    public void update(){
        testCaseTestStepListScrollPanel = new JScrollPane(createTestCaseTestStepsList(h));
        avalableTestStepsListScrollPanel = new JScrollPane(createAvailableTestStepsList(h));
        this.revalidate();
        this.repaint();
    }

    class TestStepList extends JList<TestStep>{
        private TransferHandler handler;
        private boolean isRunnable;

        TestStepList(ListModel listModel, TransferHandler handler, boolean isRunnable){
            super(listModel);
            this.isRunnable = isRunnable;
            this.handler = handler;
            this.setCellRenderer(new DefaultListCellRenderer() {
                @Override public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
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
                    if(getThis().getSelectedValuesList().size() > 1){
                        mergeTestStepsButton.setEnabled(true);
                    }else {
                        mergeTestStepsButton.setEnabled(false);
                    }
                    if(!isRunnable)return;
                    if(getThis().getSelectedValuesList().size() > 0){
                        generateCodeButton.setEnabled(true);
                        trialRunButton.setEnabled(true);
                    }else {
                        generateCodeButton.setEnabled(false);
                        trialRunButton.setEnabled(false);
                    }
                }
            });
        }

        private TestStepList getThis(){
            return this;
        }

        public String getToolTipText(MouseEvent event){
            try{
                Point p = event.getPoint();
                int location = locationToIndex(p);
                TestStep testStep = (TestStep) getModel().getElementAt(location);
                return "<html><div style=\"font-size: " + AppFont.getInstance().getSize() * 2/3 + "\">" + testStep.getTestStepTypeShortName() + " " + testStep.getDescription() + "</div></html>";
            }catch (Exception ignored){
                //Empty lists genereate errors
            }
            return "";
        }
    }

    private JList<TestStep> createAvailableTestStepsList(TransferHandler handler) {
        availableTestStepsListModel.clear();
        int iterator = 0;
        for(int i = 0; i < Gui.availableTestSteps.getTestSteps().size(); i++){
            TestStep testStep = Gui.availableTestSteps.getTestSteps().get(i);
            if(testStepTypeFilterAsRegex == null || testStep.getTestStepTypeShortName().matches(testStepTypeFilterAsRegex)){
                availableTestStepsListModel.add(iterator, testStep);
                iterator++;
            }
            if(testStep.getClass().equals(SubProcedureTestStep.class)){
                boolean allSubTestStepsPassesFilter = true;
                for(TestStep subStep : ((SubProcedureTestStep)testStep).testSteps){
                    if(!subStep.getDescription().matches(testStepTypeFilterAsRegex)){
                        allSubTestStepsPassesFilter = false;
                        break;
                    }
                }
                if(allSubTestStepsPassesFilter){
                    availableTestStepsListModel.add(i, testStep);
                    iterator++;
                }
            }
        }

        availableTestStepsList = new TestStepList(availableTestStepsListModel, h, false);
        // Disable row Cut, Copy, Paste
        ActionMap map = availableTestStepsList.getActionMap();
        Action dummy = new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { /* Dummy action */ }
        };
        map.put(TransferHandler.getCutAction().getValue(Action.NAME), dummy);
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME), dummy);
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME), dummy);

        return availableTestStepsList;
    }

    private JList<TestStep> createTestCaseTestStepsList(TransferHandler handler) {
        testCaseTestStepListModel.clear();
        for(int i = 0; i < CreateTestTabPanel.testCaseTestSteps.size(); i++){
            TestStep testStep = CreateTestTabPanel.testCaseTestSteps.get(i);
            testCaseTestStepListModel.add(i, testStep);
        }
        testCaseTestStepList = new TestStepList(testCaseTestStepListModel, h, true);

        testCaseTestStepList.setCellRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
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
            @Override public void actionPerformed(ActionEvent e) { /* Dummy action */ }
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
    @Override protected Transferable createTransferable(JComponent c) {
        source = (JList<?>) c;
        indices = source.getSelectedIndices();
        Object[] transferedObjects = source.getSelectedValuesList().toArray(new Object[0]);
        // return new DataHandler(transferedObjects, localObjectFlavor.getMimeType());
        return new Transferable() {
            @Override public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] {localObjectFlavor};
            }
            @Override public boolean isDataFlavorSupported(DataFlavor flavor) {
                return Objects.equals(localObjectFlavor, flavor);
            }
            @Override public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                if (isDataFlavorSupported(flavor)) {
                    return transferedObjects;
                } else {
                    throw new UnsupportedFlavorException(flavor);
                }
            }
        };
    }
    @Override public boolean canImport(TransferHandler.TransferSupport info) {
        return info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
    }
    @Override public int getSourceActions(JComponent c) {
        return TransferHandler.MOVE; // TransferHandler.COPY_OR_MOVE;
    }
    @SuppressWarnings("unchecked")
    @Override public boolean importData(TransferHandler.TransferSupport info) {
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
            for (Object o: values) {
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
    @Override protected void exportDone(JComponent c, Transferable data, int action) {
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
        indices  = null;
        addCount = 0;
        addIndex = -1;
    }
}