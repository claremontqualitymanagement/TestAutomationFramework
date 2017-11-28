package se.claremont.autotest.common.gui.runtab;

import se.claremont.autotest.common.gui.guistyle.TafButton;
import se.claremont.autotest.common.gui.guistyle.TafCloseButton;
import se.claremont.autotest.common.gui.guistyle.TafDialog;
import se.claremont.autotest.common.gui.guistyle.TafLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RunResultsDialogue {

    private String pathToSummaryReport;

    public RunResultsDialogue(JFrame parent, int exitCode, String pathToSummaryReport){
        this.pathToSummaryReport = pathToSummaryReport;

        final URI link = getLink();

        TafDialog resultWindow = new TafDialog(parent, "TAF - Run results", true);
        resultWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultWindow.setName("RunResultWindow");

        Container pane = resultWindow.getContentPane();
        pane.setName("RunResultPanel");
        pane.setLayout(new GridLayout(5,1, 50, 50));

        TafLabel resultsText = new TafLabel(String.valueOf(exitCode));
        resultsText.setName("TestRunExitCode");

        TafButton linkButton = new TafButton("Summary report");
        linkButton.setName("LinkButton");
        if(pathToSummaryReport.equals("null_summary.html") || !Files.exists(Paths.get(pathToSummaryReport))){
            linkButton.setEnabled(false);
        }
        linkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(link != null){
                    try {
                        Desktop.getDesktop().browse(link);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });


        pane.add(new TafLabel("Test run results overview"));
        pane.add(new TafLabel("Test run exit code: "));
        pane.add(resultsText);
        pane.add(linkButton);
        pane.add(new TafCloseButton(resultWindow));
        resultWindow.pack();
        resultWindow.setVisible(true);
    }

    private URI getLink(){
        URI link = null;
        try {
            link = new URI("file://"  + pathToSummaryReport.replace("\\", "/"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return link;
    }
}
