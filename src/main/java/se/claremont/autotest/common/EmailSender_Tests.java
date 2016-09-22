package se.claremont.autotest.common;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jordam on 2016-09-19.
 */
public class EmailSender_Tests {

    @Test
    public void send(){
        String[] recipients = new String[] {"jorgen.damberg@claremont.se"};
        EmailSender emailSender = new EmailSender("smtp.google.com", "jorgen.damberg@gmail.com", recipients, "Test email", "<html><body><h1>content string headline</h1></body></html>", "587");
        String returnMessage = emailSender.sendThroughGmail();
        Assert.assertFalse("Could not send email. " + returnMessage, returnMessage.contains("Exception"));
    }
}
