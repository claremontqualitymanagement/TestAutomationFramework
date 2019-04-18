package se.claremont.taf.core.reporting.testrunreports;

import se.claremont.taf.core.support.SupportMethods;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testcase.TestCaseResult;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Produces a HTML file that updates it self on regular intervals - showing test results.
 *
 * Created by jordam on 2016-12-22.
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public class KioskModeReport {
    String html = null;
    String filePath = null;
    final ArrayList<String> tableRows = new ArrayList<>();

    @SuppressWarnings("SpellCheckingInspection")
    public void evaluateTestCase(TestCase testCase){
        if (testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.UNEVALUATED)){
            testCase.testCaseResult.evaluateResultStatus();
        }
        switch (testCase.testCaseResult.resultStatus){
            case UNEVALUATED:
                tableRows.add("      <tr class=\"unevaluated\"><td>Unevaluated</td><td>" + testCase.testSetName + "</td><td>" + testCase.testName + "</td></tr>" + System.lineSeparator());
                break;
            case PASSED:
                tableRows.add("      <tr class=\"passed\"><td>Passed</td><td>" + testCase.testSetName + "</td><td>" + testCase.testName + "</td></tr>" + System.lineSeparator());
                break;
            case FAILED_WITH_ONLY_KNOWN_ERRORS:
                tableRows.add("      <tr class=\"onlyknownerrors\"><td>Known errors</td><td>" + testCase.testSetName + "</td><td>" + testCase.testName + "</td></tr>" + System.lineSeparator());
                break;
            case FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS:
                tableRows.add("      <tr class=\"newandknownerrors\"><td>New and known errors</td><td>" + testCase.testSetName + "</td><td>" + testCase.testName + "</td></tr>" + System.lineSeparator());
                break;
            case FAILED_WITH_ONLY_NEW_ERRORS:
                tableRows.add("      <tr class=\"newerrors\"><td>New errors</td><td>" + testCase.testSetName + "</td><td>" + testCase.testName + "</td></tr>" + System.lineSeparator());
                break;
        }
    }

    public void create(String filePath, String title, int reloadIntervalInSeconds){
        this.filePath = filePath;
        html = "<!DOCTYPE html>" + System.lineSeparator();
        html += "<html>" + System.lineSeparator();
        html += "  <head>" + System.lineSeparator();
        html += "    <meta http-equiv=\"refresh\" content=\"" + reloadIntervalInSeconds + "\" >" + System.lineSeparator();
        html += "    <style>" + System.lineSeparator();
        html += styleInfo();
        html += "    </style>" + System.lineSeparator();
        html += "  </head>" + System.lineSeparator();
        html += "  <body>" + System.lineSeparator();
        html += "    <h1>" + title + "</h2>" + System.lineSeparator();
        html += "    <table>" + System.lineSeparator();
        for(String resultRow : tableRows){
            //noinspection StringConcatenationInLoop
            html += resultRow;
        }
        html += "    </table>" + System.lineSeparator();
        html += "    <div class=\"lastruntimestamp\">Last updated " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + "</div>" + System.lineSeparator();
        html += "  </body>" + System.lineSeparator();
        html += "</html>" + System.lineSeparator();
        save();
    }

    private void save(){
        SupportMethods.saveToFile(html, filePath);
    }

    public void openInDefaultBrowser(){
        try {
            Desktop.getDesktop().open(new File(filePath));
        } catch (IOException e) {
            System.out.println("Something went wrong trying to open the KioskModeReport in default browser. File path = '" + filePath + "'. Error: " + e.getMessage());
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private String styleInfo(){
        return
                "      body            { background-color: black; }" + System.lineSeparator() +
                "      h1              { color: lightgrey; }" + System.lineSeparator() +
                "      tr.passed       { background-color: lightgreen; }" + System.lineSeparator() +
                "      tr.newerrors    { background-color: red; color: white; }" + System.lineSeparator() +
                "      tr.newandknownerrors { background-color: orange; color: black; }" + System.lineSeparator() +
                "      tr.onlyknownerrors   { background-color: yellow; color: darkslategrey; }" + System.lineSeparator() +
                "      tr.unevaluated       { background-color: grey; color: black; }" + System.lineSeparator() +
                "      div.lastruntimestamp { color: lightgrey; }" + System.lineSeparator();
    }

}
