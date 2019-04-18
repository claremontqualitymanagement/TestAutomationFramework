package se.claremont.taf.core.gui.runtab;

import se.claremont.taf.core.gui.guistyle.*;
import se.claremont.taf.core.support.ColoredConsolePrinter;

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

    public RunResultsDialogue(JFrame parent, int exitCode, String pathToSummaryReport, String testOutput){
        this.pathToSummaryReport = pathToSummaryReport;

        final URI link = getLink();

        TafDialog resultWindow = new TafDialog(parent, "TAF - Run results", true);
        resultWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultWindow.setName("RunResultWindow");

        Container pane = resultWindow.getContentPane();
        pane.setName("RunResultPanel");
        GroupLayout groupLayout = new GroupLayout(pane);
        pane.setLayout(groupLayout);
        //pane.setLayout(new GridLayout(5,1, 50, 50));

        TafLabel headline = new TafLabel("Test run results overview");

        TafLabel exitCodeLabel = new TafLabel("Test run exit code: ");
        TafLabel resultsText = new TafLabel(String.valueOf(exitCode));
        resultsText.setName("TestRunExitCode");

        TafTextArea testOutputTextArea = new TafTextArea("TestOutputTextArea");
        testOutputTextArea.setText(ColoredConsolePrinter.removeFormattingFromString(testOutput));
        testOutputTextArea.setFont(new Font("monospaced", Font.PLAIN, AppFont.getInstance().getSize() * 2/3));
        testOutputTextArea.setForeground(TafGuiColor.textColor);
        testOutputTextArea.setEditable(false);
        testOutputTextArea.setLineWrap(false);
        JScrollPane testOutputScrollPane = new JScrollPane(testOutputTextArea);
        testOutputScrollPane.createHorizontalScrollBar();
        testOutputScrollPane.createVerticalScrollBar();
        testOutputScrollPane.setName("TestOutputScrollPane");

        TafButton linkButton = new TafButton("Summary report");
        linkButton.setMnemonic('s');
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

        TafCloseButton closeButton = new TafCloseButton(resultWindow);
        closeButton.setMnemonic('c');

        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(headline)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(exitCodeLabel)
                                        .addComponent(resultsText, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                                .addComponent(testOutputScrollPane)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(linkButton)
                                        .addComponent(closeButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(exitCodeLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(resultsText, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
                        .addComponent(testOutputScrollPane)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(linkButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(closeButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
        );

        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        resultWindow.pack();
        if(resultWindow.getHeight() >= Toolkit.getDefaultToolkit().getScreenSize().height){
            resultWindow.setSize(resultWindow.getWidth(), Toolkit.getDefaultToolkit().getScreenSize().height * 4/5);
        }
        if(resultWindow.getWidth() >= Toolkit.getDefaultToolkit().getScreenSize().width){
            resultWindow.setSize(Toolkit.getDefaultToolkit().getScreenSize().width * 4/5, resultWindow.getHeight() * 4/5);
        }
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
