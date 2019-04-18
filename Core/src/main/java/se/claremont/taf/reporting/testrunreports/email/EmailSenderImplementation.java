package se.claremont.taf.reporting.testrunreports.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.taf.support.SupportMethods;
import se.claremont.taf.testrun.Settings;
import se.claremont.taf.testrun.TestRun;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Sends emails
 *
 * Created by jordam on 2016-09-18.
 */
@SuppressWarnings({"SpellCheckingInspection", "CanBeFinal"})
public class EmailSenderImplementation implements EmailSender{

    private final static Logger logger = LoggerFactory.getLogger( EmailSenderImplementation.class );

    private String hostName;
    private String senderAddress;
    private String[] recipientAddresses;
    private String subjectLine;
    private String htmlContent;
    private String hostServerPort;
    private EmailSendType emailSendType;

    @SuppressWarnings("SpellCheckingInspection")
    enum EmailSendType{
        SMTP,
        GMAIL,
        UNMANAGED
    }

    public EmailSenderImplementation(String hostName, String senderAddress, String[] recipientAddresses, String subjectLine, String htmlContent, String hostServerPort, String smtpOrGmail){
        this.hostName = hostName;
        this.senderAddress = senderAddress;
        this.recipientAddresses = recipientAddresses;
        this.subjectLine = subjectLine;
        this.htmlContent = htmlContent;
        this.hostServerPort = hostServerPort;
        if("smtp".equalsIgnoreCase(smtpOrGmail)){
            this.emailSendType = EmailSendType.SMTP;
        } else if("gmail".equalsIgnoreCase(smtpOrGmail)){
            this.emailSendType = EmailSendType.GMAIL;
        } else {
            this.emailSendType = EmailSendType.UNMANAGED;
        }
    }

    public String send(){
        if(this.emailSendType == EmailSendType.GMAIL){
            return sendThroughGmail();
        } else if(this.emailSendType == EmailSendType.SMTP){
            return sendThroughSmtp();
        } else if(this.emailSendType == EmailSendType.UNMANAGED){
            return "Cannot send email through un-implemented EmailSendType '" + this.emailSendType.toString() + "'.";
        } else {
            return "This is not supposed to be possible";
        }

    }

    private String sendThroughSmtp() {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", hostName);
        properties.setProperty("mail.smtp.port", hostServerPort);
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
            logger.debug( "Sent email message successfully." );
            return "Sent email message successfully";
        }catch (MessagingException mex) {
            return "Something went terribly wrong sending the email. " + mex.toString();
        }
    }

    private String sendThroughGmail(){
        String returnMessage;
        String username = TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_ACCOUNT_USER_NAME);
        String password = TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_ACCOUNT_USER_PASSWORD);
        this.hostName = TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_SERVER_ADDRESS);
        this.hostServerPort = TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_SERVER_PORT);

        if(username == null || username.length() < 1) {
            return "Cannot send mail. No email account user name set in settings.";
        }
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", hostName);
        props.put("mail.smtp.port", hostServerPort);

        final String finalUsername = username;
        final String finalPassword = password;
        logger.debug( "finalUsername= " + finalUsername );
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(finalUsername, finalPassword);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(TestRun.getSettingsValue(Settings.SettingParameters.EMAIL_SENDER_ADDRESS)));
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
            return returnMessage;
        } catch (MessagingException e) {
            returnMessage =                     SupportMethods.LF + SupportMethods.LF +
                    "Could not send email with subject '" + subjectLine +
                    "' using account '" + username + "' to mail host '" +
                    hostName + ":" + hostServerPort + "'. Firewall issues or authentication problems could be the culprit." +
                    SupportMethods.LF + SupportMethods.LF + e.toString() + SupportMethods.LF;
            return returnMessage;
        }
    }

    private void sendThroughSmtpAdvanced() {
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

                logger.debug( "Done" );

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }
