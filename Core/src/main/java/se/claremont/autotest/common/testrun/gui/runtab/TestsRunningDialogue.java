package se.claremont.autotest.common.testrun.gui.runtab;

import se.claremont.autotest.common.testrun.CliTestRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TestsRunningDialogue {

    RunTestTabPanel parent;
    private Font appFont;
    JFrame progressWindow = new JFrame("Test run progress");

    public TestsRunningDialogue(RunTestTabPanel parent, Font appFont){
        this.parent = parent;
        this.appFont = appFont;
        progressWindow.setName("DiagnosticTestProgressWindow");
        progressWindow.setTitle("TAF - Test run progress");
        progressWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                runTests();
            }
        });
        JLabel progressText = new JLabel("Running tests...");
        progressText.setName("ProgressText");
        progressText.setFont(appFont);
        progressWindow.getContentPane().add(progressText);
        progressWindow.pack();
        progressWindow.setSize(400, 300);
        progressWindow.setVisible(true);

    }

    private void runTests() {
        int exitCode = CliTestRunner.runInTestMode(parent.cliArguments().trim().split(" "));
        progressWindow.dispose();
        new RunResultsDialogue(parent.applicationWindow, exitCode, parent.pathToHtmlTestRunSummaryReport, appFont);
    }

}
