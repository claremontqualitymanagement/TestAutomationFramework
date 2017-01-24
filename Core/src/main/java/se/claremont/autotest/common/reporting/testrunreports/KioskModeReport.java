package se.claremont.autotest.common.reporting.testrunreports;

import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testcase.TestCase;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jordam on 2016-12-22.
 */
public class KioskModeReport {
    String html = null;
    String filePath = null;
    ArrayList<String> tableRows = new ArrayList<>();

    public void evaluateTestCase(TestCase testCase){
        if (testCase.resultStatus.equals(TestCase.ResultStatus.UNEVALUATED)){
            testCase.evaluateResultStatus();
        }
        switch (testCase.resultStatus){
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
        html += "<hmtl>" + System.lineSeparator();
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
            html += resultRow;
        }
        html += "    </table>" + System.lineSeparator();
        html += "    <div class=\"lastruntimestamp\">Last updated " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + "</div>" + System.lineSeparator();
        html += "  </body>" + System.lineSeparator();
        html += "</hmtl>" + System.lineSeparator();
        save();
    }

    private void save(){
        SupportMethods.saveToFile(html, filePath);
    }

    public void openInDefaultBrowser(){
        try {
            Desktop.getDesktop().open(new File(filePath));
        } catch (IOException e) {
            System.out.println("Something went wrong trying to open the KioskModeReport in default browser. Filepath = '" + filePath + "'. Error: " + e.getMessage());
        }
    }

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
