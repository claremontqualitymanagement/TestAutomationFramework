package se.claremont.autotest.common;

import java.util.ArrayList;

/**
 * Created by jordam on 2016-09-19.
 */
public class TestRunReporterEmailReport implements TestRunReporter{
    private HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();

    @Override
    public void report() {
        String recipientsString = TestRun.settings.getValue(Settings.SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES);
        if(htmlSummaryReport.numberOfTestCases() > 1 && recipientsString.length() > 0){
            ArrayList<String> recipients = new ArrayList<>();
            for(String recipient : recipientsString.split(",")){
                recipients.add(recipient.trim());
            }
            EmailSender emailSender = new EmailSender(
                    TestRun.settings.getValue(Settings.SettingParameters.EMAIL_SERVER_ADDRESS),
                    TestRun.settings.getValue(Settings.SettingParameters.EMAIL_SENDER_ADDRESS),
                    recipients.toArray(new String[0]),
                    "AutoTest results",
                    htmlSummaryReport.createReport(),
                    TestRun.settings.getValue(Settings.SettingParameters.EMAIL_SERVER_PORT),
                    TestRun.settings.getValue(Settings.SettingParameters.EMAIL_SMTP_OR_GMAIL));
            System.out.println(emailSender.send());
        }
    }

    public void evaluateTestCase(TestCase testCase) {
        htmlSummaryReport.evaluateTestCase(testCase);
    }

    public void evaluateTestSet(TestSet testSet){
        htmlSummaryReport.evaluateTestSet(testSet);
    }
}
