package se.claremont.autotest.websupport;

import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.restsupport.RestSupport;

import java.util.ArrayList;

/**
 * Checks a link to see if it is broken.
 *
 * Created by jordam on 2016-12-04.
 */
@SuppressWarnings("unused")
public class LinkCheck implements Runnable{
    private final String link;
    private final TestCase testCase;
    private final String testStepName;
    private final ArrayList<String[]> tableRows;

    public LinkCheck(TestCase testCase, String link){
        this.link = link;
        this.testCase = testCase;
        this.testStepName = testCase.getCurrentTestStepName();
        System.out.println(this.testStepName);
        this.tableRows = null;
    }

    public LinkCheck(ArrayList<String[]> tableRows, String link){
        this.link = link;
        this.testCase = null;
        this.testStepName = null;
        this.tableRows = tableRows;
    }

    private void log(LogLevel logLevel, String message){
        //testCase.testCaseLog.log(logLevel, message);
        testCase.testCaseLog.log(logLevel, message, message, testCase.testName, testStepName, "WebInteractionMethods/" + testCase.testSetName);
    }

    @Override
    public void run() {
        String responseCode = null;
        String[] results = new String[4];
        results[0] = link; // 0 is link
        long startTime = System.currentTimeMillis();
        try {
            if(link.toLowerCase().startsWith("mailto:") && link.contains("@") && link.contains(".")){
                results[2] = String.valueOf(System.currentTimeMillis() - startTime); //2 is execution time
                results[3] = "Mail address. Skipped."; //3 is comment
                return;
            }
            responseCode = new RestSupport(testCase).responseCodeFromGetRequestWithoutLogging(link);
        } catch (Exception e) {
            results[3] = "Error: " + e.getMessage(); //3 is comment
        }
        results[1] = String.valueOf(responseCode); //1 is response code
        results[2] = String.valueOf(System.currentTimeMillis() - startTime); //2 is execution time
        tableRows.add(results);
    }

}
