package se.claremont.autotest.common;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by jordam on 2016-09-18.
 */
class EmailSender {
    String hostName;
    String senderAddress;
    String[] recipientAddresses;
    String subjectLine;
    String htmlContent;

    EmailSender(String hostName, String senderAddress, String[] recipientAddresses, String subjectLine, String htmlContent){
        this.hostName = hostName;
        this.senderAddress = senderAddress;
        this.recipientAddresses = recipientAddresses;
        this.subjectLine = subjectLine;
        this.htmlContent = htmlContent;
        send();
    }

    private void send() {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", hostName);
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderAddress));
            for(String recipient :recipientAddresses){
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            }

            message.setSubject(subjectLine);
            message.setContent(htmlContent, "text/html");

            Transport.send(message);
            System.out.println("Sent message successfully....");
        }catch (MessagingException mex) {
            System.out.println("Something went terribly wrong sending the email. " + mex.toString());
            mex.printStackTrace();
        }
    }
}
