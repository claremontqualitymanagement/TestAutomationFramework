package se.claremont.taf;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import se.claremont.taf.core.testrun.CliTestRunner;
import se.claremont.taf.core.testrun.Settings;
import se.claremont.taf.core.testrun.TestRun;
import se.claremont.taf.core.testset.UnitTestClass;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Settings_Tests extends UnitTestClass {

    @Before
    public void testSetup(){
        TestRun.setCustomSettingsValue("mytestparameter", "yes");
    }

    @Test
    public void settingRuntimeValuesForSettingsWithCLIShouldOverrideOtherSettings() {
        TestRun.setCustomSettingsValue("myparametername", "initialValue");
        String[] args = {"myparametername=cliValue"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue(TestRun.getCustomSettingsValue("myparametername").equals("cliValue"));
    }

    @Test
    public void htmlReportPrefixFromBaseFolderTest(){
        Settings s = new Settings();
        Assert.assertNotNull(s.getValue(Settings.SettingParameters.BASE_LOG_FOLDER));
        Assert.assertTrue(s.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).equals("file"));
        s.setValue(Settings.SettingParameters.BASE_LOG_FOLDER, "smb://myserver.mycompany.mycountry/mypath");
        Assert.assertTrue(s.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX), s.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).equals("smb"));
    }

    @Test
    public void htmlReportPrefixFromBaseFolderTest2(){
        Settings s = new Settings();
        Assert.assertNotNull(s.getValue(Settings.SettingParameters.BASE_LOG_FOLDER));
        Assert.assertTrue(s.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).equals("file"));
        s.setValue(Settings.SettingParameters.BASE_LOG_FOLDER, "smb://myserver.mycompany.mycountry/mypath");
        Assert.assertTrue(s.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX), s.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).equals("smb"));
        s.setValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX, "http");
        Assert.assertTrue(s.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX), s.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).equals("http"));
        s.setValue(Settings.SettingParameters.BASE_LOG_FOLDER, "file://myserver.mycompany.mycountry/mypath");
        Assert.assertTrue(s.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX), s.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).equals("http"));
    }

    private String createTemporaryRunSettingsFileWithCurrentSettings(){
        String tempFilePath = System.getProperty("java.io.tmpdir")
                + "runsettings.properties";
        try{
            if(Files.exists(Paths.get(tempFilePath)))
                Files.delete(Paths.get(tempFilePath));
        }catch (Exception e){
            Assume.assumeTrue("Could not delete temporary file '" + tempFilePath + "'. Error: " + e.toString(), false);
        }
        try {
            TestRun.getSettings().writeSettingsParametersToFile(tempFilePath);
        }catch (Exception e){
            Assume.assumeTrue("Could not write run settings to file", false);
        }
        if(!Files.exists(Paths.get(tempFilePath))){
            Assume.assumeTrue("The file '" + tempFilePath + "' do not exist. Aborting test case.", false);
        }
        return tempFilePath;
    }

    private void deleteTemporarySettingsFile(String tempFilePath){
        try{
            if(Files.exists(Paths.get(tempFilePath)))
                Files.delete(Paths.get(tempFilePath));
        }catch (Exception e){
            System.out.println("Could not delete temporary file '" + tempFilePath + "'. Error: " + e.toString());
        }
    }

    @Test
    public void settingsInRunSettingsFileShouldOverwriteDefaultValues(){
        TestRun.reloadSettings();
        Assert.assertTrue(TestRun.getSettingsValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).equals("file"));
        TestRun.setSettingsValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX, "http");
        String tempFilePath = createTemporaryRunSettingsFileWithCurrentSettings();
        TestRun.getSettings().clear();
        Settings settings = new Settings(tempFilePath);
        deleteTemporarySettingsFile(tempFilePath);
        Assert.assertTrue(settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).equals("http"));
    }

    @Test
    public void settingsInCliShouldOverwriteRunSettingsFileSettings(){
        TestRun.setSettingsValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX, "http");
        String tempFilePath = createTemporaryRunSettingsFileWithCurrentSettings();
        TestRun.reloadSettings();
        CliTestRunner.runInTestMode(new String[] {"runsettingsfile=" + tempFilePath, "HTML_REPORTS_LINK_PREFIX=ftp"});
        deleteTemporarySettingsFile(tempFilePath);
        Assert.assertTrue(TestRun.getSettingsValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).equals("ftp"));
    }

    @Test
    public void settingsInTestCaseSetupShouldOverwriteSettingsFromCli(){
        //Assert.assertTrue(TestRun.getCustomSettingsValue("myparametername").equals("testSetupValue"));

    }

    @Test
    public void settingsInTestCaseShouldOverwriteSettingsInTestSetup(){
        Assert.assertTrue(TestRun.getCustomSettingsValue("mytestparameter").equals("yes"));
        TestRun.setCustomSettingsValue("mytestparameter", "no");
        Assert.assertTrue(TestRun.getCustomSettingsValue("mytestparameter").equals("no"));

    }


}
