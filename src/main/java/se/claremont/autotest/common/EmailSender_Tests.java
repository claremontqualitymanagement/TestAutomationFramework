package se.claremont.autotest.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 * Created by jordam on 2016-09-19.
 */
public class EmailSender_Tests {

    @Test
    public void sendGmail(){
        String[] recipients = new String[] {"jorgen.damberg@claremont.se"};
        EmailSender emailSender = new EmailSender("smtp.google.com", "jorgen.damberg@gmail.com", recipients, "Test email", "<html><body><h1>content string headline</h1></body></html>", "587", "gmail");
        String returnMessage = emailSender.sendThroughGmail();
        Assert.assertFalse("Could not send email. " + returnMessage, returnMessage.contains("Exception"));
    }

    @Test
    public void SendSmtp(){
        Settings settings = new Settings();
        String[] recipients = new String[] {"jorgen.damberg@claremont.se"};
        EmailSender emailSender = new EmailSender("mailrelay.prv.se", "jorgen.damberg@gmail.com", recipients, "Test email", "<html><body><h1>content string headline</h1></body></html>", "25", "smptp");
        String returnMessage = emailSender.sendThroughGmail();
        Assert.assertFalse("Could not send email. " + returnMessage, returnMessage.contains("Exception"));
    }
}
