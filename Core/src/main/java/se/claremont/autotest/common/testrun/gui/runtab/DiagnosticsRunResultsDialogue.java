package se.claremont.autotest.common.testrun.gui.runtab;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DiagnosticsRunResultsDialogue {

    public DiagnosticsRunResultsDialogue(JFrame applicationWindow, Font appFont, Result result) {
        JDialog resultFrame = new JDialog(applicationWindow, "Test results", true);
        resultFrame.setName("DiagnosticsRunResultsWindow");
        resultFrame.setTitle("TAF - Diagnostic run results");
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel resultPanel = new JPanel();
        resultPanel.setName("DiagnosticsRunResultsPanel");
        resultPanel.setFont(appFont);
        resultPanel.setLayout(new GridLayout(result.getFailureCount() + 2, 2));
        JLabel timeLabel = new JLabel("Execution time: ");
        timeLabel.setName("ExecutionDurationLabel");
        timeLabel.setFont(appFont);
        JLabel timeText = new JLabel(String.valueOf(result.getRunTime()) + " milliseconds");
        timeText.setName("ExecutionDurationText");
        timeText.setFont(appFont);
        resultPanel.add(timeLabel);
        resultPanel.add(timeText);
        JLabel testCaseCountLabel = new JLabel("Test case count: ");
        testCaseCountLabel.setName("TestCaseCountLabel");
        testCaseCountLabel.setFont(appFont);
        resultPanel.add(testCaseCountLabel);
        JLabel testCaseCountText = new JLabel(String.valueOf(result.getRunCount()));
        testCaseCountText.setName("TestCaseCountTextField");
        testCaseCountText.setFont(appFont);
        resultPanel.add(testCaseCountText);
        JLabel failedCountLabel = new JLabel("Failed test case count: ");
        failedCountLabel.setName("FailedTestCaseCountLabel");
        failedCountLabel.setFont(appFont);
        resultPanel.add(failedCountLabel);
        JLabel failedTestCountText = new JLabel(String.valueOf(result.getFailureCount()));
        failedTestCountText.setName("FailedTestCaseCountText");
        failedTestCountText.setFont(appFont);
        resultPanel.add(failedTestCountText);
        int failureCounter = 0;
        for (Failure failure : result.getFailures()) {
            failureCounter++;
            JLabel failureLabel = new JLabel("Failure" + failureCounter);
            failedCountLabel.setName("FailureLabel" + failureCounter);
            resultPanel.add(failedCountLabel);
            JLabel failureText = new JLabel(failure.getMessage());
            failureText.setName("FailureText" + failureCounter);
            resultPanel.add(failureText);
        }
        JButton closeButton = new JButton("Close");
        closeButton.setName("CloseButton");
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
}
