package se.claremont.autotest.common.gui.runtab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class RunResultsDialogue {

    private String pathToSummaryReport;

    public RunResultsDialogue(JFrame parent, int exitCode, String pathToSummaryReport, Font appFont){
        this.pathToSummaryReport = pathToSummaryReport;
        final URI link = getLink();

        JDialog resultWindow = new JDialog(parent, "TAF - Run results", true);
        resultWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultWindow.setName("RunResultWindow");

        Container pane = resultWindow.getContentPane();
        pane.setName("RunResultPanel");
        pane.setLayout(new GridLayout(5,1));

        JLabel headline = new JLabel("Test run results overview");
        headline.setFont(appFont);
        headline.setName("Headline");

        JLabel resultsLabel = new JLabel("Test run exit code: ");
        resultsLabel.setFont(appFont);
        resultsLabel.setName("ExitCodeLabel");

        JLabel resultsText = new JLabel(String.valueOf(exitCode));
        resultsText.setFont(appFont);
        resultsText.setName("TestRunExitCode");

        JButton linkButton = new JButton("Summary report");
        linkButton.setName("LinkButton");
        linkButton.setFont(appFont);
        linkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(link == null){
                    try {
                        Desktop.getDesktop().browse(link);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        JButton closeButton = new JButton("Close");
        closeButton.setName("CloseButton");
        closeButton.setFont(appFont);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultWindow.dispose();
            }
        });
        pane.add(headline);
        pane.add(resultsLabel);
        pane.add(resultsText);
        pane.add(linkButton);
        pane.add(closeButton);
        resultWindow.pack();
        resultWindow.setVisible(true);
    }

    private URI getLink(){
        URI link = null;
        try {
            link = new URI(pathToSummaryReport);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return link;
    }
}
