package se.claremont.autotest.guidriverpluginstructure.websupport;

import okhttp3.OkHttpClient;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.restsupport.RestGetRequest;
import se.claremont.autotest.restsupport.RestResponse;

/**
 * Checks a link to see if it is broken.
 *
 * Created by jordam on 2016-12-04.
 */
public class LinkCheck implements Runnable{
    String link;
    TestCase testCase;

    public LinkCheck(TestCase testCase, String link){
        this.link = link;
        this.testCase = testCase;
    }

    private void log(LogLevel logLevel, String message){
        testCase.testCaseLog.log(logLevel, message, message, testCase.testName, "reportBrokenLinks", "WebInteractionMethods/" + testCase.testSetName);
    }

    @Override
    public void run() {
        OkHttpClient client = new OkHttpClient();
        String responseCode = null;
        long startTime = System.currentTimeMillis();
        try {
            if(link.toLowerCase().startsWith("mailto:") && link.contains("@") && link.contains(".")){
                log(LogLevel.INFO, "Reference to mail address (MailTo:) not checked for validity. Reference: '" + link + "'.");
                return;
            }
            RestGetRequest restGetRequest = new RestGetRequest(link);
            RestResponse restResponse = restGetRequest.execute(client);
            responseCode = restResponse.responseCode;
            //responseCode = rest.responseCodeFromGetRequest(link.getAttribute("href"));
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
