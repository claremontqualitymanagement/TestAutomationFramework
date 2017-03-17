package se.claremont.autotest.common.testrun;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.common.testset.TestSet;

/**
 * Created by jordam on 2017-03-17.
 */
public class MailSendingTest {

    @Test
    @Ignore
    public void singleMailFromMultipleTestSetExecutions() {
        //Need to be configured for each test run
        TestRun.initializeIfNotInitialized();
        TestRun.setSettingsValue(Settings.SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES, "");
        TestRun.setSettingsValue(Settings.SettingParameters.EMAIL_SERVER_ADDRESS, "smtp.gmail.com");
        TestRun.setSettingsValue(Settings.SettingParameters.EMAIL_SENDER_ADDRESS, "");
        TestRun.setSettingsValue(Settings.SettingParameters.EMAIL_SERVER_PORT, "465");
        TestRun.setSettingsValue(Settings.SettingParameters.EMAIL_ACCOUNT_USER_NAME, "");
        TestRun.setSettingsValue(Settings.SettingParameters.EMAIL_ACCOUNT_USER_PASSWORD, "");
        TestRun.setSettingsValue(Settings.SettingParameters.EMAIL_SMTP_OR_GMAIL, "SMTP");
        String[] args = {"runname=HappyTest", TestSet1.class.getName(), TestSet2.class.getName()};
        CliTestRunner.runInTestMode(args);
    }

}
