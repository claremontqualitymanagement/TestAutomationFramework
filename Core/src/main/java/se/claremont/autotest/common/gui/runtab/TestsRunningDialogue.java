package se.claremont.autotest.common.gui.runtab;

import se.claremont.autotest.common.gui.guistyle.AppFont;
import se.claremont.autotest.common.gui.guistyle.TafFrame;
import se.claremont.autotest.common.gui.guistyle.TafLabel;
import se.claremont.autotest.common.testrun.CliTestRunner;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TestsRunningDialogue {

    RunTestTabPanel parent;
    TafFrame progressWindow = new TafFrame("Test run progress");

    public TestsRunningDialogue(RunTestTabPanel parent){

        this.parent = parent;

        progressWindow.setName("DiagnosticTestProgressWindow");
        progressWindow.setTitle("TAF - Test run progress");
        progressWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                runTests();
            }
        });

        TafLabel progressText = new TafLabel("Running tests...");

        progressWindow.getContentPane().add(progressText);
        progressWindow.pack();
        progressWindow.setSize(400, 300);
        progressWindow.setVisible(true);

    }

    private void runTests() {
        int exitCode = CliTestRunner.runInTestMode(parent.cliArguments().trim().split(" "));
        progressWindow.dispose();
        new RunResultsDialogue(parent.applicationWindow, exitCode, parent.pathToHtmlTestRunSummaryReport);
    }

}
