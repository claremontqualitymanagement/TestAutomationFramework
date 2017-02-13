package se.claremont.autotest.common.reporting.testrunreports.email;

import se.claremont.autotest.common.reporting.testrunreports.HtmlSummaryReport;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testrun.TestRunReporter;
import se.claremont.autotest.common.testset.TestSet;

import java.util.ArrayList;

/**
 * Created by jordam on 2016-09-19.
 */
public class TestRunReporterEmailReport implements TestRunReporter {
    private HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();

    @Override
    public void report() {
        String recipientsString = TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES);
        if(recipientsString == null || recipientsString.length() == 0) System.out.println("No recipients found for sending summary reportTestRun email. Add them to the runSettings.properties file in the base log folder.");
        if(htmlSummaryReport.numberOfTestCases() > 1 && recipientsString != null && recipientsString.length() > 0){
            ArrayList<String> recipients = new ArrayList<>();
            for(String recipient : recipientsString.split(",")){
                recipients.add(recipient.trim());
            }
            EmailSender emailSender = new EmailSender(
                    TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_SERVER_ADDRESS),
                    TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_SENDER_ADDRESS),
                    recipients.toArray(new String[0]),
                    "AutoTest results",
                    htmlSummaryReport.createReport(),
                    TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_SERVER_PORT),
                    TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_SMTP_OR_GMAIL));
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
