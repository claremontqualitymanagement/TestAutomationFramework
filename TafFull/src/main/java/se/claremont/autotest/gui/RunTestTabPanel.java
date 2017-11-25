package se.claremont.autotest.gui;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import se.claremont.autotest.common.testrun.DiagnosticsRun;

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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RunTestTabPanel extends JPanel {

    private JLabel runNameLabel = new JLabel("Test run name");
    private JTextField runNameText = new JTextField();

    private JLabel executionModeLabel = new JLabel("Execution mode");
    private JSpinner executionModeSpinner = new JSpinner();
    String[] spinnerOptions = {"Sequential execution", "Test classes in parallel", "Test methods in parallel", "2 parallel threads", "3 parallel threads"};

    private JButton showHelpTextButton = new JButton("Help");
    private JButton runDiagnosticsButton = new JButton("Run diagnostics tests");
    private JButton setRunParametersButton = new JButton("Set run parameters...");
    private JButton startButton = new JButton("Start test run");
    private JButton closeButton = new JButton("Exit");

    private JLabel cliCommandLabel = new JLabel("Corresponding CLI command:");
    private JTextArea cliCommandText = new JTextArea();
    private JButton cliToClipboardButton = new JButton("CLI to clipboard");

    private JFrame applicationWindow;
    private Font appFont;


    public RunTestTabPanel(JFrame parentFrame) {
        applicationWindow = parentFrame;

        GroupLayout groupLayout = new GroupLayout(this);
        this.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);

        setFontSize();

        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResource("logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ImagePanel imagePanel = new ImagePanel(image);
        JLabel logoImage = new JLabel(new ImageIcon(image));

        runNameLabel.setFont(appFont);
        runNameLabel.setSize(cliCommandLabel.getSize().width, cliCommandLabel.getHeight());
        runNameText.setFont(appFont);
        runNameText.setText(new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()));
        updateCliCommandText();
        runNameText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                updateCliCommandText();
            }

            public void removeUpdate(DocumentEvent e) {
                updateCliCommandText();
            }

            public void insertUpdate(DocumentEvent e) {
                updateCliCommandText();
            }
        });

        executionModeLabel.setFont(appFont);
        executionModeSpinner.setFont(appFont);
        SpinnerListModel spinnerListModel = new SpinnerListModel(spinnerOptions);
        executionModeSpinner.setModel(spinnerListModel);
        ((JSpinner.DefaultEditor) executionModeSpinner.getEditor()).getTextField().setEditable(false);
        executionModeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateCliCommandText();
            }
        });

        cliCommandLabel.setFont(appFont);
        cliCommandText.setFont(appFont);
        cliCommandLabel.setForeground(Color.gray);
        cliCommandText.setForeground(Color.gray);
        cliCommandText.setLineWrap(true);
        cliCommandText.setBackground(cliCommandLabel.getBackground());

        showHelpTextButton.setFont(appFont);
        showHelpTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHelp();
            }
        });

        cliToClipboardButton.setFont(appFont);
        cliToClipboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(cliCommandText.getText());
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
            }
        });
        runDiagnosticsButton.setFont(appFont);
        runDiagnosticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DiagnosticsRun diagnosticsRun = new DiagnosticsRun(new JUnitCore());
                JFrame progressWindow = new JFrame("Test run progress");
                JPanel progressPanel = new JPanel();
                JLabel progressText = new JLabel("Running tests...");
                progressText.setFont(appFont);
                progressPanel.add(progressText);
                progressWindow.add(progressPanel);
                progressWindow.pack();
                progressWindow.setSize(400, 300);
                progressWindow.show();
                diagnosticsRun.run();
                Result result = diagnosticsRun.getResult();
                progressWindow.dispose();
                showResult(result);
            }
        });

        setRunParametersButton.setFont(appFont);
        setRunParametersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RunSettingsDialogue();
            }
        });

        startButton.setFont(appFont);
        closeButton.setFont(appFont);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        int minimumLabelWidth = applicationWindow.getWidth()/4;
        int minimumLabelHeight = applicationWindow.getHeight()/20;

        Dimension labelSize = new Dimension(minimumLabelWidth, minimumLabelHeight);

        runNameLabel.setSize(labelSize);
        executionModeLabel.setSize(labelSize);
        cliCommandLabel.setSize(labelSize);

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
                                .addComponent(startButton)
                                .addComponent(closeButton)
                        )
        );
    }

    public JFormattedTextField getTextField(JSpinner spinner) {
        return ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
    }

    private void setFontSize() {
        appFont = new Font("serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().height / 50);
    }


    private void showResult(Result result) {
        JDialog resultFrame = new JDialog(applicationWindow, "Test results", true);
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel resultPanel = new JPanel();
        resultPanel.setFont(appFont);
        resultPanel.setLayout(new GridLayout(result.getFailureCount() + 2, 2));
        JLabel timeLabel = new JLabel("Execution time: ");
        timeLabel.setFont(appFont);
        JLabel timeText = new JLabel(String.valueOf(result.getRunTime()) + " milliseconds");
        timeText.setFont(appFont);
        resultPanel.add(timeLabel);
        resultPanel.add(timeText);
        JLabel testCaseCountLabel = new JLabel("Test case count: ");
        testCaseCountLabel.setFont(appFont);
        resultPanel.add(testCaseCountLabel);
        JLabel testCaseCountText = new JLabel(String.valueOf(result.getRunCount()));
        testCaseCountText.setFont(appFont);
        resultPanel.add(testCaseCountText);
        JLabel failedCountLabel = new JLabel("Failed test case count: ");
        failedCountLabel.setFont(appFont);
        resultPanel.add(failedCountLabel);
        JLabel failedTestCountText = new JLabel(String.valueOf(result.getFailureCount()));
        failedTestCountText.setFont(appFont);
        resultPanel.add(failedTestCountText);
        for (Failure failure : result.getFailures()) {
            resultPanel.add(new JLabel("Failure"));
            resultPanel.add(new JLabel(failure.getMessage()));
        }
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultFrame.dispose();
            }
        });
        resultPanel.add(closeButton);
        resultFrame.add(resultPanel);
        resultFrame.pack();
        resultFrame.show();
    }


    private void updateCliCommandText() {
        String path = Gui.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = "TafFull.jar";
        try {
            decodedPath = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cliCommandText.setText("java -jar " + decodedPath + " runName=" + runNameText.getText() + " " + getExecutionModePart());
    }

    private void showHelp() {
        JFrame helpFrame = new JFrame();
        JTextArea textArea = new JTextArea();
        String helpText = "This is the current version of the help text.";
        textArea.setText(helpText);
        textArea.setLineWrap(true);
        JPanel helpPanel = new JPanel();
        helpPanel.add(textArea);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                helpFrame.dispose();
            }
        });
        helpPanel.add(closeButton);
        helpFrame.add(helpPanel);
        helpFrame.pack();
        helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        helpFrame.show();
    }

    private String getExecutionModePart() {
        switch (executionModeSpinner.getValue().toString()) {
            case "Sequential execution":
                return "";
            case "Test classes in parallel":
                return "PARALLEL_TEST_EXECUTION_MODE=classes ";
            case "Test methods in parallel":
                return "PARALLEL_TEST_EXECUTION_MODE=methods ";
            case "2 parallel threads":
                return "PARALLEL_TEST_EXECUTION_MODE=2 ";
            case "3 parallel threads":
                return "PARALLEL_TEST_EXECUTION_MODE=3 ";
            default:
                return "";
        }
    }
}
