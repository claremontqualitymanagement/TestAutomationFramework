package se.claremont.autotest.common.testrun.gui;

import jdk.nashorn.internal.scripts.JD;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import se.claremont.autotest.common.testrun.CliTestRunner;
import se.claremont.autotest.common.testrun.DiagnosticsRun;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;

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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class RunTestTabPanel extends JPanel {

    private JLabel runNameLabel = new JLabel("Test run name");
    private JTextField runNameText = new JTextField();
    private List<String> chosenTestClasses = new ArrayList<>();

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

    private JLabel cliCommandLabel = new JLabel("Corresponding CLI command:");
    private JTextArea cliCommandText = new JTextArea();
    private JButton cliToClipboardButton = new JButton("CLI to clipboard");

    private JFrame applicationWindow;
    private Font appFont;


    public RunTestTabPanel(JFrame parentFrame)  {
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

        executionModeLabel.setFont(appFont);
        executionModeSpinner.setFont(appFont);
        SpinnerListModel spinnerListModel = new SpinnerListModel(spinnerOptions);
        executionModeSpinner.setModel(spinnerListModel);
        ((JSpinner.DefaultEditor) executionModeSpinner.getEditor()).getTextField().setBackground(Color.white);
        ((JSpinner.DefaultEditor) executionModeSpinner.getEditor()).getTextField().setEditable(false);
        executionModeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateCliCommandText("");
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
                progressWindow.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(WindowEvent e) {
                        super.windowOpened(e);
                        diagnosticsRun.run();
                        Result result = diagnosticsRun.getResult();
                        progressWindow.dispose();
                        showResult(result);
                    }
                });
                JLabel progressText = new JLabel("Running " + diagnosticsRun.getTestCount() + " identified tests...");
                progressText.setFont(appFont);
                progressWindow.getContentPane().add(progressText);
                progressWindow.pack();
                progressWindow.setSize(400, 300);
                progressWindow.setVisible(true);
            }
        });

        setRunParametersButton.setFont(appFont);
        setRunParametersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RunSettingsDialogue(getThis());
                updateCliCommandText("");
            }
        });

        pickTestsButton.setFont(appFont);
        pickTestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pickTestClasses();
            }
        });

        startButton.setFont(appFont);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame progressWindow = new JFrame("Test run progress");
                progressWindow.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(WindowEvent e) {
                        super.windowOpened(e);
                        runTests();
                        progressWindow.dispose();
                    }
                });
                JLabel progressText = new JLabel("Running tests...");
                progressText.setFont(appFont);
                progressWindow.getContentPane().add(progressText);
                progressWindow.pack();
                progressWindow.setSize(400, 300);
                progressWindow.setVisible(true);
            }
        });
        closeButton.setFont(appFont);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        int minimumLabelWidth = applicationWindow.getWidth() / 4;
        int minimumLabelHeight = applicationWindow.getHeight() / 20;

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
                                        .addComponent(pickTestsButton)
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
                                .addComponent(startButton)
                                .addComponent(closeButton)
                        )
        );
    }

    private void pickTestClasses() {
        JFrame classPickerWindow = new JFrame();
        JLabel headline = new JLabel("Pick your test classes");
        headline.setFont(appFont);
        classPickerWindow.setLayout(new GridLayout(4, 1));
        classPickerWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        classPickerWindow.setTitle("TAF - Test class picker dialogue");
        Container pane = classPickerWindow.getContentPane();
        DefaultListModel listModel = new DefaultListModel();
        try {
            Object[] classes = getLoadedClassesAndClassesInClassPath().toArray();
            for(Object o : classes){
                listModel.addElement(o);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JList testClasses = new JList(listModel);
        testClasses.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        testClasses.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        testClasses.setVisibleRowCount(-1);
        testClasses.setFont(appFont);
        JScrollPane listScroller = new JScrollPane(testClasses);
        //listScroller.setPreferredSize(new Dimension(250, 80));
        JButton closeButton = new JButton("Cancel");
        closeButton.setFont(appFont);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classPickerWindow.dispose();
            }
        });
        JButton saveButton = new JButton("Save");
        saveButton.setFont(appFont);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> selectedOptions = new ArrayList<>();;
                List<Object> selections = testClasses.getSelectedValuesList();
                for(Object o : selections){
                    chosenTestClasses.add((String)o);
                }
                updateCliCommandText("");
                classPickerWindow.dispose();
            }
        });
        pane.add(headline);
        pane.add(listScroller);
        pane.add(closeButton);
        pane.add(saveButton);
        int width = listScroller.getWidth();
        int height = listScroller.getHeight();
        if(height > Toolkit.getDefaultToolkit().getScreenSize().height){
            height = 9* Toolkit.getDefaultToolkit().getScreenSize().height/10;
            listScroller.createVerticalScrollBar();
        }
        if(width > Toolkit.getDefaultToolkit().getScreenSize().width){
            width = 9* Toolkit.getDefaultToolkit().getScreenSize().width /10;
            listScroller.createHorizontalScrollBar();
        }
        listScroller.setSize(width, height);
        classPickerWindow.pack();
        classPickerWindow.setVisible(true);
    }

    private void runTests() {
        CliTestRunner.executeRunSequence(cliArguments().trim().split(" "));
    }

    public JFormattedTextField getTextField(JSpinner spinner) {
        return ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
    }

    private void setFontSize() {
        appFont = new Font("serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().height / 50);
    }

    private RunTestTabPanel getThis() {
        return this;
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
        closeButton.setFont(appFont);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultFrame.dispose();
            }
        });
        resultPanel.add(closeButton);
        resultFrame.add(resultPanel);
        resultFrame.pack();
        resultFrame.setVisible(true);
    }


    void updateCliCommandText(String additions) {
        cliCommandText.setText((javaJarPath() +
                cliArguments() +
                additions +
                String.join(" ", chosenTestClasses)
        ).trim());
    }

    private String cliArguments(){
        return " runName=" + runNameText.getText() +
                getExecutionModePart() +
                runSettingsChangesFromDefault();
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

    private void showHelp() {
        JFrame helpFrame = new JFrame();
        JTextArea textArea = new JTextArea();
        textArea.setFont(appFont);
        String helpText = null;
        helpText = "This is the current version of the help text. " + System.lineSeparator() +
                System.lineSeparator() +
                "To use TAF without gui, use the switch 'nogui'.";
        textArea.setText(helpText);
        textArea.setLineWrap(true);
        JPanel helpPanel = new JPanel();
        helpPanel.add(textArea);
        JButton closeButton = new JButton("Close");
        closeButton.setFont(appFont);
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
        helpFrame.setVisible(true);
    }


    private List<Class> getClassesFromClassLoader(){
        Field f = null;
        try {
            f = ClassLoader.class.getDeclaredField("classes");
            f.setAccessible(true);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            return (Vector<Class>) f.get(classLoader);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ArrayList<Class>();
    }

    private Set<String> getLoadedClassesAndClassesInClassPath() throws IOException {
        java.util.List<String> jarFilesInClassPath = new ArrayList<>();
        jarFilesInClassPath.add(theFileNameOfCurrentExecutingFile());
        Set<String> classNamesForLoadedClasses = new HashSet<String>();
        for(Class c : getClassesFromClassLoader()){
            classNamesForLoadedClasses.add(c.getName());
        }
        try {
            List<URL> roots = Collections.list(ClassLoader.getSystemClassLoader().getResources(""));
            for (URL url : roots) {
                File root = new File(url.getPath());
                for (File file : root.listFiles()) {
                    if (file.isDirectory())continue;
                    if(file.getName().endsWith(".class") || file.getName().endsWith(".java")){
                        classNamesForLoadedClasses.add(file.getName());
                    }
                    if(file.getName().endsWith(".jar")){
                        jarFilesInClassPath.add(file.getName());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(String fileName : jarFilesInClassPath){
            if(fileName.endsWith("jar")){
                ZipInputStream zip = new ZipInputStream(new FileInputStream(fileName));
                for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                    if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                        String className = entry.getName().replace('/', '.'); // including ".class"
                        classNamesForLoadedClasses.add(className.substring(0, className.length() - ".class".length()));
                    }
                }
            }
        }
        return classNamesForLoadedClasses;
    }

    private String theFileNameOfCurrentExecutingFile() {
        return new java.io.File(RunTestTabPanel.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getName();
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
