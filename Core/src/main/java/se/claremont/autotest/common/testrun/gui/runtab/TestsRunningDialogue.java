package se.claremont.autotest.common.testrun.gui.runtab;

import se.claremont.autotest.common.testrun.CliTestRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TestsRunningDialogue {

    RunTestTabPanel parent;

    public TestsRunningDialogue(RunTestTabPanel parent, Font appFont){
        this.parent = parent;
        JFrame progressWindow = new JFrame("Test run progress");
        progressWindow.setName("DiagnosticTestProgressWindow");
        progressWindow.setTitle("TAF - Test run progress");
        progressWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                runTests();
                progressWindow.dispose();
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
        CliTestRunner.executeRunSequence(parent.cliArguments().trim().split(" "));
    }

}
