package se.claremont.autotest.common;

import se.claremont.autotest.support.SupportMethods;

import javax.mail.*;
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
    String hostServerPort;
    EmailSendType emailSendType;

    enum EmailSendType{
        SMTP,
        GMAIL
    }

    EmailSender(String hostName, String senderAddress, String[] recipientAddresses, String subjectLine, String htmlContent, String hostServerPort, String smtpOrGmail){
        this.hostName = hostName;
        this.senderAddress = senderAddress;
        this.recipientAddresses = recipientAddresses;
        this.subjectLine = subjectLine;
        this.htmlContent = htmlContent;
        this.hostServerPort = hostServerPort;
        if(smtpOrGmail.toLowerCase().equals("smtp")){
            this.emailSendType = EmailSendType.SMTP;
        } else {
            this.emailSendType = EmailSendType.GMAIL;
        }

        if(this.emailSendType == EmailSendType.GMAIL){
            sendThroughGmail();
        } else if(this.emailSendType == EmailSendType.SMTP){
            sendThroughSmtp();
        } else {
            System.out.println("Cannot send email through un-implemented EmailSendType '" + this.emailSendType.toString() + "'.");
        }
    }

    void sendThroughSmtp() {
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
            System.out.println("Sent email message successfully.");
        }catch (MessagingException mex) {
            System.out.println("Something went terribly wrong sending the email. " + mex.toString());
            mex.printStackTrace();
        }
    }

    public String sendThroughGmail(){
        String returnMessage = "";
        String username = CliTestRunner.testRun.settings.getValueForProperty("emailUserName");
        String password = CliTestRunner.testRun.settings.getValueForHiddenProperty("emailPassword");
        this.hostName = "smtp.gmail.com";
        this.hostServerPort = "587";

        if(username == null || username.length() < 1) {
            return "Cannot send mail. No email account user name set in settings.";
        }
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", hostName);
        props.put("mail.smtp.port", hostServerPort);

        String finalUsername = username;
        String finalPassword = password;
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(finalUsername, finalPassword);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.senderAddress));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(String.join(",", recipientAddresses)));
            message.setSubject(subjectLine);
            message.setContent(htmlContent, "text/html");
            //message.setText(htmlContent);

            Transport.send(message);
            returnMessage = SupportMethods.LF +
                            "Email sent with subject '" + subjectLine +
                            "' using account '" + username + "' to mail host '" +
                            hostName + ":" + hostServerPort + "'." +
                            SupportMethods.LF;
            System.out.println(returnMessage);
        } catch (MessagingException e) {
            returnMessage =                     SupportMethods.LF + SupportMethods.LF +
                    "Could not send email with subject '" + subjectLine +
                    "' using account '" + username + "' to mail host '" +
                    hostName + ":" + hostServerPort + "'. Firewall issues or authentication problems could be the culprit." +
                    SupportMethods.LF + SupportMethods.LF + e.toString() + SupportMethods.LF;
            System.out.println(returnMessage);
        }
        return returnMessage;
    }

    public void sendThroughSmtpAdvanced() {
            Properties props = new Properties();
            props.put("mail.smtp.host", hostName);
            props.put("mail.smtp.socketFactory.port", hostServerPort);
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("username","password");
                        }
                    });

            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("jorgen.damberg@gmail.com"));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse("jorgen.damberg@claremont.se"));
                message.setSubject("Testing Subject");
                message.setText("Dear Mail Crawler," +
                        "\n\n No spam to my email, please!");

                Transport.send(message);

                System.out.println("Done");

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }
