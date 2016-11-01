package se.claremont.autotest.common;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.support.SupportMethods;

/**
 * Tests for the Settings class
 *
 * Created by jordam on 2016-09-18.
 */
public class Settings_Tests {

     @Test
    public void loadDefaults(){
         Settings settings = new Settings();
         Assert.assertTrue( !settings.getValue(Settings.SettingParameters.BASE_LOG_FOLDER).isEmpty() );
     }

    @Test
    public void add(){
        Settings settings = new Settings();
        settings.setCustomValue("dummy", "dummyvalue");
        Assert.assertTrue(settings.getCustomValue("dummy").equals("dummyvalue"));
    }

    @Test
    public void update(){
        Settings settings = new Settings();
        settings.setCustomValue("dummy", "dummyvalue");
        settings.setCustomValue("dummy", "dummyvalue2");
        Assert.assertTrue(settings.getCustomValue("dummy").equals("dummyvalue2"));
    }

    @Test
    public void print(){
        Settings settings = new Settings();
        settings.setCustomValue("dummy", "dummyvalue");
        Assert.assertTrue(settings.toString().contains("dummyvalue"));
    }

    @Test
    public void customParameterOnEnumParameter(){
        Settings settings = new Settings();
        settings.setValue(Settings.SettingParameters.PATH_TO_LOGO, "this value");
        Assert.assertTrue(settings.getValue(Settings.SettingParameters.PATH_TO_LOGO).equals("this value"));
        settings.setCustomValue(SupportMethods.stringToCapitalInitialCharacterForEachWordAndNoSpaces(Settings.SettingParameters.PATH_TO_LOGO.toString().replace("_", " ")), "that value");
        Assert.assertTrue(settings.getValue(Settings.SettingParameters.PATH_TO_LOGO).equals("that value"));
    }

}
