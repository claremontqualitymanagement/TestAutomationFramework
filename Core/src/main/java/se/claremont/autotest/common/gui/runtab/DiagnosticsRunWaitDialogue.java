package se.claremont.autotest.common.gui.runtab;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import se.claremont.autotest.common.testrun.DiagnosticsRun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DiagnosticsRunWaitDialogue {
    private Font appFont;
    private JFrame applicationWindow;

    public DiagnosticsRunWaitDialogue(JFrame applicationWindow, Font appFont){
        this.appFont = appFont;
        this.applicationWindow = applicationWindow;
        DiagnosticsRun diagnosticsRun = new DiagnosticsRun(new JUnitCore());
        JFrame progressWindow = new JFrame("Test run progress");
        progressWindow.setName("DiagnosticsRunProgressWindow");
        progressWindow.setTitle("TAF - Diagnostics run progress");
        progressWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                diagnosticsRun.run();
                Result result = diagnosticsRun.getResult();
                progressWindow.dispose();
                new DiagnosticsRunResultsDialogue(applicationWindow, appFont, result);
            }
        });
        JLabel progressText = new JLabel("Running " + diagnosticsRun.getTestCount() + " identified tests...");
        progressText.setName("ProgressText");
        progressText.setFont(appFont);
        progressWindow.getContentPane().add(progressText);
        progressWindow.pack();
        progressWindow.setSize(400, 300);
        progressWindow.setVisible(true);
    }

}
