package se.claremont.autotest.common.testrun.gui.runtab;

import se.claremont.autotest.common.logging.LogFolder;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testrun.gui.Gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class RunTestTabPanel extends JPanel {

    private JLabel runNameLabel = new JLabel("Test run name");
    private JTextField runNameText = new JTextField();
    static List<String> chosenTestClasses = new ArrayList<>();

    private JLabel executionModeLabel = new JLabel("Execution mode");
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

    private JButton showHelpTextButton = new JButton("Help");
    private JButton runDiagnosticsButton = new JButton("Run diagnostics tests");
    private JButton setRunParametersButton = new JButton("Set run parameters...");
    private JButton pickTestsButton = new JButton("Pick test classes...");
    private JButton startButton = new JButton("Start test run");
    private JButton closeButton = new JButton("Exit");
    private JButton resetSettings = new JButton("Reset");

    private JLabel cliCommandLabel = new JLabel("Corresponding CLI command:");
    private JTextArea cliCommandText = new JTextArea();
    private JButton cliToClipboardButton = new JButton("CLI to clipboard");
    private JLabel logoImage;

    JFrame applicationWindow;
    private Font appFont;
    private Dimension labelSize;

    String pathToHtmlTestRunSummaryReport;

    public RunTestTabPanel(JFrame parentFrame)  {

        applicationWindow = parentFrame;
        applicationWindow.setName("TafGuiMainWindow");
        this.setName("RunTestTabPanel");
        int minimumLabelWidth = applicationWindow.getWidth() / 4;
        int minimumLabelHeight = applicationWindow.getHeight() / 20;
        labelSize = new Dimension(minimumLabelWidth, minimumLabelHeight);

        GroupLayout groupLayout = new GroupLayout(this);
        this.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);

        setFontSize();
        prepareLogoImage();
        prepareRunName();
        prepareExecutionMode();
        prepareCliCommand();
        prepareHelpButton();
        prepareCliToClipboardButton();
        prepareDiagnosticsRunButton();
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
                                        .addComponent(runDiagnosticsButton)
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
                                .addComponent(runDiagnosticsButton)
                                .addComponent(setRunParametersButton)
                                .addComponent(pickTestsButton)
                                .addComponent(resetSettings)
                                .addComponent(startButton)
                                .addComponent(closeButton)
                        )
        );
    }

    private void prepareResetButton() {
        resetSettings.setFont(appFont);
        resetSettings.setName("ResetSettingsButton");
        resetSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenTestClasses.clear();
                executionModeSpinner.setValue(spinnerOptions[0]);
                runNameText.setText(new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()));
                TestRun.getSettings().clear();
                TestRun.getSettings().putAll(Gui.defaultSettings);
                updateCliCommandText("");
            }
        });
    }

    private void prepareCloseButton() {
        closeButton.setFont(appFont);
        closeButton.setName("ExitTafButton");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }



    private void prepareStartButton() {
        startButton.setFont(appFont);
        startButton.setName("StartTestsButton");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pathToHtmlTestRunSummaryReport = LogFolder.testRunLogFolder + "_summary.html";
                new TestsRunningDialogue(getThis(), appFont);
            }
        });
    }

    private void preparePickTestClassesButton() {
        pickTestsButton.setFont(appFont);
        pickTestsButton.setName("PickTestsButton");
        pickTestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestClassPickerDialogue window = new TestClassPickerDialogue(appFont, getThis());
            }
        });
    }

    private void prepareRunSettingsButton() {
        setRunParametersButton.setFont(appFont);
        setRunParametersButton.setName("SetRunParametersButton");
        setRunParametersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RunSettingsDialogue(getThis());
                updateCliCommandText("");
            }
        });
    }

    private void prepareDiagnosticsRunButton() {
        runDiagnosticsButton.setFont(appFont);
        runDiagnosticsButton.setName("RunDiagnosticsButton");
        runDiagnosticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DiagnosticsRunWaitDialogue(applicationWindow, appFont);
            }
        });
    }

    private void prepareCliToClipboardButton() {
        cliToClipboardButton.setFont(appFont);
        cliToClipboardButton.setName("CliToClipBoardButton");
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
        showHelpTextButton.setFont(appFont);
        showHelpTextButton.setName("HelpButton");
        showHelpTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HelpDialogue(appFont);
            }
        });
    }

    private void prepareCliCommand() {
        cliCommandLabel.setFont(appFont);
        cliCommandLabel.setSize(labelSize);
        cliCommandLabel.setName("CliCommandLabel");
        cliCommandLabel.setForeground(Color.gray);
        cliCommandText.setFont(appFont);
        cliCommandText.setName("CliCommandText");
        cliCommandText.setForeground(Color.gray);
        cliCommandText.setLineWrap(true);
        cliCommandText.setBackground(cliCommandLabel.getBackground());
    }

    private void prepareExecutionMode() {
        executionModeLabel.setFont(appFont);
        executionModeLabel.setSize(labelSize);
        executionModeLabel.setName("ExecutionModeLabel");
        executionModeSpinner.setFont(appFont);
        executionModeSpinner.setName("ExecutionModeSpinner");
        SpinnerListModel spinnerListModel = new SpinnerListModel(spinnerOptions);
        executionModeSpinner.setModel(spinnerListModel);
        JFormattedTextField spinnerTextArea = ((JSpinner.DefaultEditor) executionModeSpinner.getEditor()).getTextField();
        spinnerTextArea.setName("ExecutionModeSpinnerTextArea");
        spinnerTextArea.setBackground(Color.white);
        spinnerTextArea.setEditable(false);
        executionModeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateCliCommandText("");
            }
        });
    }

    private void prepareRunName() {
        runNameLabel.setFont(appFont);
        runNameLabel.setName("RunNameLabel");
        runNameLabel.setSize(labelSize);
        //runNameLabel.setSize(cliCommandLabel.getSize().width, cliCommandLabel.getHeight());
        runNameText.setFont(appFont);
        runNameText.setName("RunNameTextField");
        runNameText.setText(new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()));
        updateCliCommandText("");
        runNameText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                updateCliCommandText("");
            }

            public void removeUpdate(DocumentEvent e) {
                updateCliCommandText("");
            }

            public void insertUpdate(DocumentEvent e) {
                updateCliCommandText("");
            }
        });
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

    private void setFontSize() {
        appFont = new Font("serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().height / 50);
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
        return " runName=" + runNameText.getText() +
                getExecutionModePart() +
                runSettingsChangesFromDefault() + " " + String.join(" ", chosenTestClasses);
    }

    private String javaJarPath(){
        String path = Gui.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = "TafFull.jar";
        try {
            decodedPath = URLDecoder.decode(path, "UTF-8");
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
                return " PARALLEL_TEST_EXECUTION_MODE=classes";
            case "Test methods in parallel":
                return " PARALLEL_TEST_EXECUTION_MODE=methods";
            case "2 parallel threads":
                return " PARALLEL_TEST_EXECUTION_MODE=2";
            case "3 parallel threads":
                return " PARALLEL_TEST_EXECUTION_MODE=3";
            case "4 parallel threads":
                return " PARALLEL_TEST_EXECUTION_MODE=4";
            case "5 parallel threads":
                return " PARALLEL_TEST_EXECUTION_MODE=5";
            case "6 parallel threads":
                return " PARALLEL_TEST_EXECUTION_MODE=6";
            case "7 parallel threads":
                return " PARALLEL_TEST_EXECUTION_MODE=7";
            case "8 parallel threads":
                return " PARALLEL_TEST_EXECUTION_MODE=8";
            case "9 parallel threads":
                return " PARALLEL_TEST_EXECUTION_MODE=9";
            case "10 parallel threads":
                return " PARALLEL_TEST_EXECUTION_MODE=10";
            default:
                return "";
        }
    }
}
