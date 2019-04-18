package se.claremont.taf.core.gui.runtab;

import se.claremont.taf.core.gui.Gui;
import se.claremont.taf.core.gui.guistyle.*;
import se.claremont.taf.core.gui.plugins.IGuiTab;
import se.claremont.taf.core.logging.LogFolder;
import se.claremont.taf.core.testrun.Settings;
import se.claremont.taf.core.testrun.TestRun;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class RunTestTabPanel implements IGuiTab {

    private TafLabel runNameLabel = new TafLabel("Test run name");
    private TafTextField runNameText = new TafTextField(" < Optional test run name > ");
    //private String disregardedDefaultRunNameString = " <optional specific test run name> ";
    static List<String> chosenTestClasses = new ArrayList<>();

    private TafLabel executionModeLabel = new TafLabel("Execution mode");
    private JSpinner executionModeSpinner = new JSpinner();
    String[] spinnerOptions = {
            "Sequential execution",
            "Test classes in parallel",
            "Test methods in parallel",
            "2 parallel threads",
            "3 parallel threads",
            "4 parallel threads",
            "5 parallel threads",
            "6 parallel threads",
            "7 parallel threads",
            "8 parallel threads",
            "9 parallel threads",
            "10 parallel threads"
    };

    private TafButton showHelpTextButton = new TafButton("Help");
    private TafButton setRunParametersButton = new TafButton("Set run parameters...");
    private TafButton pickTestsButton = new TafButton("Pick test classes...");
    private TafButton startButton = new TafButton("Start test run");
    private TafButton closeButton = new TafButton("Exit");
    private TafButton resetSettings = new TafButton("Reset");

    private TafLabel cliCommandLabel = new TafLabel("Corresponding CLI command:");
    private JTextArea cliCommandText = new JTextArea();
    private TafButton cliToClipboardButton = new TafButton("CLI to clipboard");
    private JLabel logoImage;

    //TafFrame applicationWindow;
    private TafPanel tabPanel;
    String pathToHtmlTestRunSummaryReport;


    public RunTestTabPanel()  {
        int minimumLabelWidth = Gui.applicationWindow.getWidth() / 4;
        int minimumLabelHeight = Gui.applicationWindow.getHeight() / 20;
        tabPanel = new TafPanel("UseRunTestTab");
        GroupLayout groupLayout = new GroupLayout(tabPanel);
        tabPanel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);

        prepareLogoImage();
        prepareRunName();
        prepareExecutionMode();
        prepareCliCommand();
        prepareHelpButton();
        prepareCliToClipboardButton();
        prepareRunSettingsButton();
        preparePickTestClassesButton();
        prepareStartButton();
        prepareCloseButton();
        prepareResetButton();

        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(logoImage)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(runNameLabel)
                                        .addComponent(runNameText, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(executionModeLabel)
                                        .addComponent(executionModeSpinner, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(cliCommandLabel)
                                        .addComponent(cliCommandText, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(showHelpTextButton)
                                        .addComponent(cliToClipboardButton)
                                        .addComponent(setRunParametersButton)
                                        .addComponent(pickTestsButton)
                                        .addComponent(resetSettings)
                                        .addComponent(startButton)
                                        .addComponent(closeButton)
                                )
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(logoImage)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(runNameLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(runNameText, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(executionModeLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(executionModeSpinner, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(cliCommandLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cliCommandText, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(showHelpTextButton)
                                .addComponent(cliToClipboardButton)
                                .addComponent(setRunParametersButton)
                                .addComponent(pickTestsButton)
                                .addComponent(resetSettings)
                                .addComponent(startButton)
                                .addComponent(closeButton)
                        )
        );

        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        tabPanel.setVisible(true);
    }

    private void prepareResetButton() {
        resetSettings.setMnemonic('r');
        resetSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenTestClasses.clear();
                executionModeSpinner.setValue(spinnerOptions[0]);
                runNameText.setText(runNameText.disregardedDefaultRunNameString);
                TestRun.getSettings().clear();
                TestRun.getSettings().putAll(Gui.defaultSettings);
                updateCliCommandText("");
            }
        });
    }

    private void prepareCloseButton() {
        closeButton.setMnemonic('e');
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.applicationWindow.dispose();
            }
        });
    }



    private void prepareStartButton() {
        startButton.setMnemonic('s');
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pathToHtmlTestRunSummaryReport = LogFolder.testRunLogFolder + "_summary.html";
                new TestsRunningDialogue(getThis());
            }
        });
    }

    private void preparePickTestClassesButton() {
        pickTestsButton.setMnemonic('t');
        pickTestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestClassPickerDialogue window = new TestClassPickerDialogue(getThis());
            }
        });
    }

    private void prepareRunSettingsButton() {
        setRunParametersButton.setMnemonic('p');
        setRunParametersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RunSettingsDialogue(getThis());
                updateCliCommandText("");
            }
        });
    }

    private void prepareCliToClipboardButton() {
        cliToClipboardButton.setMnemonic('c');
        cliToClipboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(cliCommandText.getText());
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
            }
        });
    }

    private void prepareHelpButton() {
        showHelpTextButton.setMnemonic('h');
        showHelpTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Event: Action command: '" + e.getActionCommand() + "', modifiers " + e.getModifiers() + ", param string: '" + e.paramString() + "'");
                new HelpDialogue();
            }
        });
    }

    private void prepareCliCommand() {
        cliCommandLabel.setForeground(Color.gray);
        cliCommandLabel.setLabelFor(cliCommandText);
        cliCommandText.setFont(AppFont.getInstance());
        cliCommandText.setName("CliCommandText");
        cliCommandText.setForeground(Color.gray);
        cliCommandText.setEditable(false);
        cliCommandText.setLineWrap(true);
        cliCommandText.setBackground(Gui.colorTheme.backgroundColor);
    }

    private void prepareExecutionMode() {

        executionModeLabel.setFont(AppFont.getInstance());
        executionModeLabel.setLabelFor(executionModeSpinner);

        executionModeSpinner.setFont(AppFont.getInstance());
        executionModeSpinner.setForeground(Gui.colorTheme.textColor);
        executionModeSpinner.setName("ExecutionModeSpinner");
        SpinnerListModel spinnerListModel = new SpinnerListModel(spinnerOptions);
        executionModeSpinner.setModel(spinnerListModel);
        JFormattedTextField spinnerTextArea = ((JSpinner.DefaultEditor) executionModeSpinner.getEditor()).getTextField();
        spinnerTextArea.setName("ExecutionModeSpinnerTextArea");
        spinnerTextArea.setBackground(Color.white);
        spinnerTextArea.setForeground(Gui.colorTheme.textColor);
        spinnerTextArea.setEditable(false);
        executionModeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, getExecutionModePart());
                updateCliCommandText("");
            }
        });
    }

    private void initiateRunNameFieldWhenActivated(){
        runNameText.setText("");
        runNameText.setFont(AppFont.getInstance());
        runNameText.setForeground(Gui.colorTheme.textColor);
    }

    private class TafFocusListener implements FocusListener{

        @Override
        public void focusGained(FocusEvent e) {
            initiateRunNameFieldWhenActivated();
        }

        @Override
        public void focusLost(FocusEvent e) {
            if(runNameText.getText().equals("")){
                initiateRunNameTextFieldToDefault();
            }
        }
    }

    private void initiateRunNameTextFieldToDefault(){
        runNameLabel.setLabelFor(runNameText);
        runNameText.setName("RunNameTextField");
        runNameText.setForeground(Gui.colorTheme.disabledColor);
    }

    private void prepareRunName() {
        initiateRunNameTextFieldToDefault();
        runNameLabel.setLabelFor(runNameText);
        runNameText.addFocusListener(new TafFocusListener());
        runNameText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updateCliCommandText(""); }

            public void removeUpdate(DocumentEvent e) {
                updateCliCommandText("");
            }

            public void insertUpdate(DocumentEvent e) {
                updateCliCommandText("");
            }
        });

        updateCliCommandText("");
    }

    private void prepareLogoImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResource("logo.png"));
        } catch (IOException ignored) {
        }

        logoImage = new JLabel(new ImageIcon(image));
        logoImage.setName("LogoImage");
    }

    private RunTestTabPanel getThis() {
        return this;
    }

    void updateCliCommandText(String additions) {
        cliCommandText.setText((javaJarPath() +
                cliArguments() +
                additions
        ).trim());
    }

    String cliArguments(){
        String args = " " + runSettingsChangesFromDefault() + " " + String.join(" ", chosenTestClasses);
        if(runNameText.isChangedFromDefault()){
            args += " runName=" + runNameText.getText();
        }
        return args;
    }

    private String javaJarPath(){
        String path = Gui.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        String decodedPath = "TafFull.jar";
        try {
            decodedPath = URLDecoder.decode(path, "UTF-8");
            if(!decodedPath.startsWith("//") && decodedPath.startsWith("/")) decodedPath = decodedPath.substring(1);
            if(decodedPath.endsWith("/classes/")) decodedPath = "TafFull.jar"; //Being run without Jar-file. Printing for prettyness.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "java -jar " + decodedPath;
    }

    private String runSettingsChangesFromDefault() {
        java.util.List<String> cliAdditions = new ArrayList<>();
        for (String parameterName : TestRun.getSettings().keySet()) {
            if (!Gui.defaultSettings.containsKey(parameterName) || !Gui.defaultSettings.get(parameterName).equals(TestRun.getCustomSettingsValue(parameterName)))
                cliAdditions.add(parameterEnumNameFromParameterFriendlyName(parameterName) + "=" + TestRun.getCustomSettingsValue(parameterName));
        }
        if (cliAdditions.size() == 0) return "";
        return " " + String.join(" ", cliAdditions);
    }

    private String parameterEnumNameFromParameterFriendlyName(String friendlyName) {
        String returnString = friendlyName;
        for (Settings.SettingParameters parameterName : Settings.SettingParameters.values()) {
            if (parameterName.friendlyName().equals(friendlyName))
                return parameterName.toString();
        }
        return returnString;
    }

    private String getExecutionModePart() {
        switch (executionModeSpinner.getValue().toString()) {
            case "Sequential execution":
                return "";
            case "Test classes in parallel":
                return "classes";
            case "Test methods in parallel":
                return "methods";
            case "2 parallel threads":
                return "2";
            case "3 parallel threads":
                return "3";
            case "4 parallel threads":
                return "4";
            case "5 parallel threads":
                return "5";
            case "6 parallel threads":
                return "6";
            case "7 parallel threads":
                return "7";
            case "8 parallel threads":
                return "8";
            case "9 parallel threads":
                return "9";
            case "10 parallel threads":
                return "10";
            default:
                return "";
        }
    }

    @Override
    public JPanel getPanel() {
        return tabPanel;
    }

    @Override
    public String getName() {
        return "Run tests";
    }

}
