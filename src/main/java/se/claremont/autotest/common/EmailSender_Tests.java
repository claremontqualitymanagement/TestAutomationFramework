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
        String returnMessage = emailSender.send();
        System.out.println(returnMessage);
        Assert.assertFalse("No user name set: '" + returnMessage, returnMessage.contains("Cannot send mail"));
        Assert.assertFalse("Could not send gmail. " + returnMessage, returnMessage.toLowerCase().contains("exception"));
        Assert.assertFalse("Impossible state", returnMessage.contains("This is not supposed to be possible"));
    }

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
