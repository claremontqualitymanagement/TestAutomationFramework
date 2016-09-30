package se.claremont.autotest.common;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by jordam on 2016-09-19.
 */
public class EmailSender_Tests {

    @Ignore
    @Test
    public void sendGmail(){
        String[] recipients = new String[] {"jorgen.damberg@claremont.se"};
        CliTestRunner.testRun.settings.setValue(Settings.SettingParameters.EMAIL_ACCOUNT_USER_PASSWORD, "Claremont!16");
        CliTestRunner.testRun.settings.setValue(Settings.SettingParameters.EMAIL_ACCOUNT_USER_NAME, "autotestcqm@gmail.com");
        CliTestRunner.testRun.settings.setValue(Settings.SettingParameters.EMAIL_SMTP_OR_GMAIL, "gmail");
        CliTestRunner.testRun.settings.setValue(Settings.SettingParameters.EMAIL_SERVER_PORT, "587");
        CliTestRunner.testRun.settings.setValue(Settings.SettingParameters.EMAIL_SERVER_ADDRESS, "smtp.gmail.com");
        CliTestRunner.testRun.settings.setValue(Settings.SettingParameters.EMAIL_SENDER_ADDRESS, "jorgen.damberg@gmail.com");
        CliTestRunner.testRun.settings.setValue(Settings.SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES, "jorgen.damberg@claremont.se");
        EmailSender emailSender = new EmailSender("smtp.google.com", "jorgen.damberg@gmail.com", recipients, "Test email", "<html><body><h1>content string headline</h1></body></html>", "587", "gmail");
        String returnMessage = emailSender.send();
        System.out.println(returnMessage);
        Assert.assertFalse("No user name set: '" + returnMessage, returnMessage.contains("Cannot send mail"));
        Assert.assertFalse("Could not send gmail. " + returnMessage, returnMessage.toLowerCase().contains("exception"));
        Assert.assertFalse("Impossible state", returnMessage.contains("This is not supposed to be possible"));
    }

    @Ignore
    @Test
    public void SendSmtp(){
        Settings settings = new Settings();
        String[] recipients = new String[] {"jorgen.damberg@claremont.se"};
        EmailSender emailSender = new EmailSender("mailrelay.prv.se", "jorgen.damberg@gmail.com", recipients, "Test email", "<html><body><h1>content string headline</h1></body></html>", "25", "smtp");
        String returnMessage = emailSender.send();
        System.out.println(returnMessage);
        Assert.assertFalse("Could not send smtp email. " + returnMessage, returnMessage.toLowerCase().contains("exception"));
        Assert.assertFalse("Impossible state", returnMessage.contains("This is not supposed to be possible"));
        Assert.assertFalse("Bad sending of email", returnMessage.contains("Something went terribly wrong sending the email"));
    }
}
