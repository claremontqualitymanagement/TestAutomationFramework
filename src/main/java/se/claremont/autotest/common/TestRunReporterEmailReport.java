package se.claremont.autotest.common;

import java.util.ArrayList;

/**
 * Created by jordam on 2016-09-19.
 */
public class TestRunReporterEmailReport implements TestRunReporter{
    HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();

    @Override
    public void report() {
        String recipientsString = TestRun.settings.getValueForProperty("emailRecipients");
        if(htmlSummaryReport.numberOfTestCases() > 1 && recipientsString.length() > 0){
            ArrayList<String> recipients = new ArrayList<>();
            for(String recipient : recipientsString.split(",")){
                recipients.add(recipient.trim());
            }
            EmailSender emailSender = new EmailSender("127.0.0.1", "jorgen.damberg@gmail.com", recipients.toArray(new String[0]), "AutoTest results", htmlSummaryReport.createReport(), "587");
            emailSender.sendThroughGmail();
        }
    }

    public void evaluateTestCase(TestCase testCase) {
        htmlSummaryReport.evaluateTestCase(testCase);
    }

    public void evaluateTestSet(TestSet testSet){
        htmlSummaryReport.evaluateTestSet(testSet);
    }
}
