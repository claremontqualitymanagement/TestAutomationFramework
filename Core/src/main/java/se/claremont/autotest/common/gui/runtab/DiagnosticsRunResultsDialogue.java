package se.claremont.autotest.common.gui.runtab;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import se.claremont.autotest.common.gui.guistyle.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DiagnosticsRunResultsDialogue {

    public DiagnosticsRunResultsDialogue(JFrame applicationWindow, Result result) {

        TafDialog resultFrame = new TafDialog(applicationWindow, "Test results", true);
        resultFrame.setName("DiagnosticsRunResultsWindow");
        resultFrame.setTitle("TAF - Diagnostic run results");
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        TafPanel resultPanel = new TafPanel("DiagnosticsRunResultsPanel");
        resultPanel.setLayout(new GridLayout(result.getFailureCount() + 2, 2, 50, 50));

        TafLabel timeLabel = new TafLabel("Execution time: ");
        resultPanel.add(timeLabel);

        TafLabel timeText = new TafLabel(String.valueOf(result.getRunTime()) + " milliseconds");
        timeText.setName("ExecutionDurationText");
        resultPanel.add(timeText);

        TafLabel testCaseCountLabel = new TafLabel("Test case count: ");
        resultPanel.add(testCaseCountLabel);

        TafLabel testCaseCountText = new TafLabel(String.valueOf(result.getRunCount()));
        testCaseCountText.setName("TestCaseCountTextField");
        resultPanel.add(testCaseCountText);

        TafLabel failedCountLabel = new TafLabel("Failed test case count: ");
        resultPanel.add(failedCountLabel);

        TafLabel failedTestCountText = new TafLabel(String.valueOf(result.getFailureCount()));
        failedTestCountText.setName("FailedTestCaseCountText");
        resultPanel.add(failedTestCountText);

        int failureCounter = 0;
        for (Failure failure : result.getFailures()) {
            failureCounter++;
            TafLabel failureLabel = new TafLabel("Failure" + failureCounter);
            failedCountLabel.setName("FailureLabel" + failureCounter);
            resultPanel.add(failedCountLabel);
            TafLabel failureText = new TafLabel(failure.getMessage());
            failureText.setName("FailureText" + failureCounter);
            resultPanel.add(failureText);
        }

        TafCloseButton closeButton = new TafCloseButton(resultFrame);
        resultPanel.add(closeButton);

        resultFrame.getRootPane().setDefaultButton(closeButton);
        closeButton.requestFocus();
        resultFrame.add(resultPanel);
        resultFrame.pack();
        resultFrame.setVisible(true);
    }
}
