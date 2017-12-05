package se.claremont.autotest.common.gui.runtab;

import se.claremont.autotest.common.gui.guistyle.TafFrame;
import se.claremont.autotest.common.gui.guistyle.TafLabel;
import se.claremont.autotest.common.testrun.CliTestRunner;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TestsRunningDialogue {
    @SuppressWarnings("WeakerAccess")
    static PrintStream originalOutputChannel;
    @SuppressWarnings("WeakerAccess")
    static ByteArrayOutputStream testOutputChannel;

    RunTestTabPanel parent;
    TafFrame progressWindow = new TafFrame("Test run progress");

    public TestsRunningDialogue(RunTestTabPanel parent){

        this.parent = parent;
        originalOutputChannel = System.out;
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
        testOutputChannel = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOutputChannel));
        int exitCode = CliTestRunner.runInTestMode(parent.cliArguments().trim().split(" "));
        String testOutput = testOutputChannel.toString();
        System.setOut(originalOutputChannel);
        progressWindow.dispose();
        new RunResultsDialogue(parent.applicationWindow, exitCode, parent.pathToHtmlTestRunSummaryReport, testOutput);
    }

}
