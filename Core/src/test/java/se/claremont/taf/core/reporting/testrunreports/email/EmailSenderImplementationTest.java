package se.claremont.taf.core.reporting.testrunreports.email;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.subethamail.wiser.Wiser;
import se.claremont.taf.core.testrun.Settings;
import se.claremont.taf.core.testrun.TestRun;
import se.claremont.taf.core.testset.UnitTestClass;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Email sending tests
 *
 * Created by jordam on 2016-09-19.
 */
public class EmailSenderImplementationTest extends UnitTestClass{
    private Wiser wiser;

    /*
    @Before
    public void setup() {
        wiser = new Wiser(2500);
        wiser.start();
    }

    @After
    public void tearDown() {
        wiser.stop();
    }
    */

    @Test
    @Ignore
    public void testGmail2() {
        //TestRun.settings.setValue(Settings.SettingParameters.EMAIL_ACCOUNT_USER_PASSWORD, "password");
        TestRun.setSettingsValue(Settings.SettingParameters.EMAIL_ACCOUNT_USER_NAME, "account@yahoo.rocks");
        //TestRun.settings.setValue(Settings.SettingParameters.EMAIL_SMTP_OR_GMAIL, "gmail");
        TestRun.setSettingsValue(Settings.SettingParameters.EMAIL_SERVER_PORT, "2500");
        TestRun.setSettingsValue(Settings.SettingParameters.EMAIL_SERVER_ADDRESS, "localhost");
        TestRun.setSettingsValue(Settings.SettingParameters.EMAIL_SENDER_ADDRESS, "sender@gmail.com");
        //TestRun.settings.setValue(Settings.SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES, "recipient@mail.se");

        String[] recipients = new String[] {"recipient@mail.com"};
        EmailSenderImplementation emailSenderImplementation = new EmailSenderImplementation(
                null,
                null,
                recipients,
                "Test email",
                "<html><body><h1>content string headline</h1></body></html>",
                null,
                "gmail");
        String result = emailSenderImplementation.send();

        assertTrue(result, result.contains("Email sent with subject 'Test email' using account 'account@yahoo.rocks' to mail host 'localhost"));

        Assert.assertFalse(wiser.getMessages().isEmpty());

        try {
            MimeMessage msg = wiser.getMessages().get(0).getMimeMessage();

            assertEquals("sender@gmail.com", msg.getFrom()[0].toString());
            assertEquals("recipient@mail.com", msg.getAllRecipients()[0].toString());
            assertEquals("Test email", msg.getSubject());
            assertTrue(msg.getContent().toString(),
                    msg.getContent().toString().contains("<html><body><h1>content string headline</h1></body></html>"));
        } catch (MessagingException | IOException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    @Ignore
    public void testSmtp() {
        //TestRun.settings.setValue(Settings.SettingParameters.EMAIL_ACCOUNT_USER_PASSWORD, "password");
        TestRun.setSettingsValue(Settings.SettingParameters.EMAIL_ACCOUNT_USER_NAME, "account@yahoo.rocks");
        //TestRun.settings.setValue(Settings.SettingParameters.EMAIL_SMTP_OR_GMAIL, "gmail");
        TestRun.setSettingsValue(Settings.SettingParameters.EMAIL_SERVER_PORT, "2500");
        TestRun.setSettingsValue(Settings.SettingParameters.EMAIL_SERVER_ADDRESS, "localhost");
        TestRun.setSettingsValue(Settings.SettingParameters.EMAIL_SENDER_ADDRESS, "sender@gmail.com");
        //TestRun.settings.setValue(Settings.SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES, "recipient@mail.se");

        String[] recipients = new String[] {"recipient@mail.com"};
        EmailSenderImplementation emailSenderImplementation = new EmailSenderImplementation(
                "localhost",
                "sender@gmail.com",
                recipients,
                "Test email",
                "<html><body><h1>content string headline</h1></body></html>",
                "2500",
                "smtp");
        String result = emailSenderImplementation.send();

        assertTrue(result, result.contains("Sent email message successfully"));

        assertFalse(wiser.getMessages().isEmpty());

        try {
            MimeMessage msg = wiser.getMessages().get(0).getMimeMessage();

            assertEquals("sender@gmail.com", msg.getFrom()[0].toString());
            assertEquals("recipient@mail.com", msg.getAllRecipients()[0].toString());
            assertEquals("Test email", msg.getSubject());
            assertTrue(msg.getContent().toString(),
                    msg.getContent().toString().contains("<html><body><h1>content string headline</h1></body></html>"));
        } catch (MessagingException | IOException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
}
