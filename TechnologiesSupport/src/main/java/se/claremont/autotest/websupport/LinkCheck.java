package se.claremont.autotest.websupport;

import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.restsupport.RestResponse;
import se.claremont.autotest.restsupport.RestSupport;

/**
 * Checks a link to see if it is broken.
 *
 * Created by jordam on 2016-12-04.
 */
public class LinkCheck implements Runnable{
    private String link;
    private TestCase testCase;

    public LinkCheck(TestCase testCase, String link){
        this.link = link;
        this.testCase = testCase;
    }

    private void log(LogLevel logLevel, String message){
        testCase.testCaseLog.log(logLevel, message, message, testCase.testName, "reportBrokenLinks", "WebInteractionMethods/" + testCase.testSetName);
    }

    @Override
    public void run() {
        String responseCode = null;
        long startTime = System.currentTimeMillis();
        try {
            if(link.toLowerCase().startsWith("mailto:") && link.contains("@") && link.contains(".")){
                log(LogLevel.INFO, "Reference to mail address (MailTo:) not checked for validity. Reference: '" + link + "'.");
                return;
            }
            RestResponse restResponse = new RestSupport(testCase).responseFromGetRequest(link);
            responseCode = restResponse.responseCode;
        } catch (Exception e) {
            try {
                log(LogLevel.VERIFICATION_PROBLEM, "Could not verify link '" + link + "' (response took " + (System.currentTimeMillis() - startTime)  + " milliseconds). Error: " + e.getMessage());
            } catch (Exception e1){
                log(LogLevel.FRAMEWORK_ERROR, "Could not verify one link '" + link + "'. Error: " + e.getMessage());
            }
        }
        if(responseCode != null && responseCode.equals("200")){
            log(LogLevel.VERIFICATION_PASSED, "Link '" + link + "' is ok (response took " + (System.currentTimeMillis() - startTime) + " milliseconds). Response code '" + responseCode + "'");
        } else {
            log(LogLevel.VERIFICATION_FAILED, "Link '" + link + "' was broken (response took " + (System.currentTimeMillis() - startTime) + " milliseconds). Response code '" + responseCode + "'.");
        }

    }

}
