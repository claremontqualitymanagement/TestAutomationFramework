package se.claremont.autotest.common.reporting.testrunreports.email;

import se.claremont.autotest.common.reporting.testrunreports.htmlsummaryreport.HtmlSummaryReport;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.SettingParameters;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testrun.reportingengine.TestRunReporter;
import se.claremont.autotest.common.testset.TestSet;

import java.util.ArrayList;

/**
 * Enables sending test run results via Email
 *
 * Created by jordam on 2016-09-19.
 */
public class TestRunReporterEmailReport implements TestRunReporter {
    private HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();

    @Override
    public void report() {
        if(TestRun.getSettingsValue(SettingParameters.EMAIL_SERVER_ADDRESS) == null
                || TestRun.getSettingsValue(SettingParameters.EMAIL_SERVER_ADDRESS).length() == 0 ||
                TestRun.getSettingsValue(SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES) == null ||
                TestRun.getSettingsValue(SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES).length() == 0){
            return;
        }
        String recipientsString = TestRun.getSettingsValue(SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES);
        if(recipientsString == null || recipientsString.length() == 0) System.out.println("No recipients found for sending summary reportTestRun email. Add them to the runSettings.properties file in the base log folder.");
        if(htmlSummaryReport.numberOfTestCases() > 1 && recipientsString != null && recipientsString.length() > 0){
            ArrayList<String> recipients = new ArrayList<>();
            for(String recipient : recipientsString.split(",")){
                recipients.add(recipient.trim());
            }
            EmailSenderImplementation emailSenderImplementation = new EmailSenderImplementation(
                    TestRun.getSettingsValue(SettingParameters.EMAIL_SERVER_ADDRESS),
                    TestRun.getSettingsValue(SettingParameters.EMAIL_SENDER_ADDRESS),
                    recipients.toArray(new String[0]),
                    "AutoTest results",
                    htmlSummaryReport.createReport(),
                    TestRun.getSettingsValue(SettingParameters.EMAIL_SERVER_PORT),
                    TestRun.getSettingsValue(SettingParameters.EMAIL_SMTP_OR_GMAIL));
            System.out.println(emailSenderImplementation.send());
        }
    }

    public void evaluateTestCase(TestCase testCase) {
        htmlSummaryReport.evaluateTestCase(testCase);
    }

    public void evaluateTestSet(TestSet testSet){
        htmlSummaryReport.evaluateTestSet(testSet);
    }
}
