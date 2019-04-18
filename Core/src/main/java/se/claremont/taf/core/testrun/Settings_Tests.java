package se.claremont.taf.core.testrun;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Tests for the Settings class
 *
 * Created by jordam on 2016-09-18.
 */
@SuppressWarnings("SpellCheckingInspection")
public class Settings_Tests extends UnitTestClass{

    @BeforeClass
    public static void classsetup(){
        TestRun.setCustomSettingsValue("myparametername", "classSetupValue");
        Assert.assertTrue(TestRun.getCustomSettingsValue("myparametername").equals("classSetupValue"));
    }

    @Before
    public void testCaseSetup(){
        TestRun.setCustomSettingsValue("myparametername", "testSetupValue");
        Assert.assertTrue(TestRun.getCustomSettingsValue("myparametername").equals("testSetupValue"));
    }

     @Test
    public void loadDefaults(){
         @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") Settings settings = new Settings();
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
    public void updateFixParameter(){
        Settings settings = new Settings();
        settings.setValue(Settings.SettingParameters.BASE_LOG_FOLDER, "dummyfolder");
        Assert.assertTrue(settings.getValue(Settings.SettingParameters.BASE_LOG_FOLDER).equals("dummyfolder"));
        settings.setValue(Settings.SettingParameters.BASE_LOG_FOLDER, "dummyfolder2");
        Assert.assertTrue(settings.getValue(Settings.SettingParameters.BASE_LOG_FOLDER).equals("dummyfolder2"));
    }

    @Test
    public void print(){
        Settings settings = new Settings();
        settings.setCustomValue("dummy", "dummyvalue");
        Assert.assertTrue(settings.toString().contains("dummyvalue"));
    }

    @Test
    public void customParameterWithSameNameAsEnumParameterShouldOverwriteTheEnumValue(){
        Settings settings = new Settings();
        settings.setValue(Settings.SettingParameters.PATH_TO_LOGO, "this value");
        Assert.assertTrue(settings.getValue(Settings.SettingParameters.PATH_TO_LOGO).equals("this value"));
        settings.setCustomValue(Settings.SettingParameters.PATH_TO_LOGO.friendlyName(), "that value");
        System.out.println(Settings.SettingParameters.PATH_TO_LOGO.friendlyName());
        Assert.assertTrue("Expected value to be 'that value', but it was '" + settings.getValue(Settings.SettingParameters.PATH_TO_LOGO) + "'.", settings.getValue(Settings.SettingParameters.PATH_TO_LOGO).equals("that value"));
    }

    @Test
    public void enumParameterValuesShouldBeSetAndRetrievableWithBothEnumNameAndFriendlyName(){
        Settings settings = new Settings();
        settings.setCustomValue(Settings.SettingParameters.PATH_TO_LOGO.friendlyName(), "MyPath");
        Assert.assertTrue(settings.getValue(Settings.SettingParameters.PATH_TO_LOGO).equals("MyPath"));
        Assert.assertTrue(settings.getCustomValue(Settings.SettingParameters.PATH_TO_LOGO.friendlyName()).equals("MyPath"));
        Assert.assertTrue(settings.getCustomValue(Settings.SettingParameters.PATH_TO_LOGO.friendlyName()).equals(settings.getValue(Settings.SettingParameters.PATH_TO_LOGO)));

        settings.setCustomValue(Settings.SettingParameters.PATH_TO_LOGO.toString(), "MyNewPath");
        Assert.assertTrue(settings.getValue(Settings.SettingParameters.PATH_TO_LOGO).equals("MyNewPath"));
    }

}
