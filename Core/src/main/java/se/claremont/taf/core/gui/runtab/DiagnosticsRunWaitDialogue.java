package se.claremont.taf.core.gui.runtab;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import se.claremont.taf.core.gui.guistyle.TafFrame;
import se.claremont.taf.core.gui.guistyle.TafLabel;
import se.claremont.taf.core.testrun.DiagnosticsRun;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DiagnosticsRunWaitDialogue {
    private TafFrame applicationWindow;

    public DiagnosticsRunWaitDialogue(TafFrame applicationWindow){
        this.applicationWindow = applicationWindow;

        DiagnosticsRun diagnosticsRun = new DiagnosticsRun(new JUnitCore());

        TafFrame progressWindow = new TafFrame("Test run progress");
        progressWindow.setName("DiagnosticsRunProgressWindow");
        progressWindow.setTitle("TAF - Diagnostics run progress");
        progressWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                diagnosticsRun.run();
                Result result = diagnosticsRun.getResult();
                progressWindow.dispose();
                new DiagnosticsRunResultsDialogue(applicationWindow, result);
            }
        });

        TafLabel progressText = new TafLabel("Running " + diagnosticsRun.getTestCount() + " identified tests...");
        progressText.setName("ProgressText");

        progressWindow.getContentPane().add(progressText);
        progressWindow.pack();
        progressWindow.setSize(400, 300);
        progressWindow.setVisible(true);
    }

}
