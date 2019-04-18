package se.claremont.taf.core.reporting.testrunreports.email;

import se.claremont.taf.core.reporting.testrunreports.htmlsummaryreport.HtmlSummaryReport;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testrun.Settings;
import se.claremont.taf.core.testrun.TestRun;
import se.claremont.taf.core.testrun.reportingengine.TestRunReporter;
import se.claremont.taf.core.testset.TestSet;

import java.util.ArrayList;

/**
 * Enables sending test run results via Email
 *
 * Created by jordam on 2016-09-19.
 */
public class TestRunReporterEmailReport implements TestRunReporter {
    private final HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();

    @Override
    public void report() {
        if(TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_SERVER_ADDRESS) == null
                || TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_SERVER_ADDRESS).length() == 0 ||
                TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES) == null ||
                TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES).length() == 0){
            return;
        }
        String recipientsString = TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES);
        if(recipientsString == null || recipientsString.length() == 0) System.out.println("No recipients found for sending summary reportTestRun email. Add them to the runSettings.properties file in the base log folder.");
        if(htmlSummaryReport.numberOfTestCases() > 1 && recipientsString != null && recipientsString.length() > 0){
            ArrayList<String> recipients = new ArrayList<>();
            for(String recipient : recipientsString.split(",")){
                recipients.add(recipient.trim());
            }
            EmailSenderImplementation emailSenderImplementation = new EmailSenderImplementation(
                    TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_SERVER_ADDRESS),
                    TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_SENDER_ADDRESS),
                    recipients.toArray(new String[0]),
                    "AutoTest results",
                    htmlSummaryReport.createReport(),
                    TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_SERVER_PORT),
                    TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_SMTP_OR_GMAIL));
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
