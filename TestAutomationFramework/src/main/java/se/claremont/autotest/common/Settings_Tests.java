package se.claremont.autotest.common;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.support.StringManagement;

import static org.junit.Assert.fail;

/**
 * Tests for the Settings class
 *
 * Created by jordam on 2016-09-18.
 */
public class Settings_Tests {

     @Test
    public void loadDefaults(){
         Settings settings = new Settings();
         Assert.assertEquals("http://46.101.193.212/TAF/images/claremontlogo.gif", settings.getValue(Settings.SettingParameters.PATH_TO_LOGO));
         Assert.assertEquals("EmailSenderAddress", settings.getValue(Settings.SettingParameters.EMAIL_SENDER_ADDRESS));
         Assert.assertEquals("EmailAccountUserName", settings.getValue(Settings.SettingParameters.EMAIL_ACCOUNT_USER_NAME));
         Assert.assertEquals("EmailAccountUserPassword", settings.getValue(Settings.SettingParameters.EMAIL_ACCOUNT_USER_PASSWORD));
         Assert.assertEquals("EmailServerAddress", settings.getValue(Settings.SettingParameters.EMAIL_SERVER_ADDRESS));
         Assert.assertEquals("EmailServerPort", settings.getValue(Settings.SettingParameters.EMAIL_SERVER_PORT));
         Assert.assertEquals("EmailSmtpOrGmail", settings.getValue(Settings.SettingParameters.EMAIL_SMTP_OR_GMAIL));
         Assert.assertEquals("EmailReportRecipientsCommaSeparatedListOfAddresses", settings.getValue(Settings.SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES));
         Assert.assertFalse( settings.getValue(Settings.SettingParameters.BASE_LOG_FOLDER).isEmpty() );
         Assert.assertFalse(settings.getValue(Settings.SettingParameters.PHANTOMJS_PATH_TO_EXE).isEmpty());
         Assert.assertFalse(settings.getValue(Settings.SettingParameters.FIREFOX_PATH_TO_BROWSER_EXE).isEmpty());
         Assert.assertFalse(settings.getValue(Settings.SettingParameters.TEST_RUN_LOG_FOLDER).isEmpty());
     }

    @Test
    public void add(){
        Settings settings = new Settings();
        settings.setCustomValue("dummy", "dummyvalue");
        Assert.assertEquals("dummyvalue", settings.getCustomValue("dummy"));
    }

    @Test
    public void update(){
        Settings settings = new Settings();
        settings.setCustomValue("dummy", "dummyvalue");
        settings.setCustomValue("dummy", "dummyvalue2");
        Assert.assertEquals("dummyvalue2", settings.getCustomValue("dummy"));
    }

    @Test
    public void print(){
        Settings settings = new Settings();
        settings.setCustomValue("dummy", "dummyvalue");
        Assert.assertTrue(settings.toString().contains("dummy=dummyvalue"));
    }

    @Test
    public void customParameterOnEnumParameter(){
        Settings settings = new Settings();
        settings.setValue(Settings.SettingParameters.PATH_TO_LOGO, "this value");
        Assert.assertEquals("this value", settings.getValue(Settings.SettingParameters.PATH_TO_LOGO));
        settings.setCustomValue(StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(Settings.SettingParameters.PATH_TO_LOGO.toString().replace("_", " ")), "that value");
        Assert.assertEquals("that value", settings.getValue(Settings.SettingParameters.PATH_TO_LOGO));
    }

}
