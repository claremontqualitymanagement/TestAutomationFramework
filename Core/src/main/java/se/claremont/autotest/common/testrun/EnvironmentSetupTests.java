package se.claremont.autotest.common.testrun;

import org.junit.*;
import se.claremont.autotest.common.testset.UnitTestClass;

import java.awt.*;
import java.io.File;

public class EnvironmentSetupTests extends UnitTestClass{
    File reportFolder ;
    boolean reportFolderCreatedForTest = false;

    @Before
    public void before(){
        reportFolder = new File(TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER));
        reportFolderCreatedForTest = false;
    }

    @After
    public void after(){
        if(reportFolderCreatedForTest) {
            reportFolder.delete();
            if(reportFolder.exists())
                System.out.println("Could not delete the log folder '" + TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER) + "' created for the test.");
        }
    }

    @Test
    public void checkThatStatedLogBaseFolderIsADirectory(){
        if(!reportFolder.exists()) {
            reportFolder.mkdirs();
            reportFolderCreatedForTest = true;
        }
        Assert.assertTrue("The base log folder is set to '" + TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER) + "' but this is not a directory.", reportFolder.isDirectory());
    }

    @Test
    public void checkFileWritePriviligeToBaseReportFolder(){
        if(!reportFolder.exists()) {
            reportFolder.mkdirs();
            reportFolderCreatedForTest = true;
        }
        File subDirectory = null;
        File writeFile = null;
        try {
            subDirectory = new File(reportFolder.getPath() + "checkFileWritePriviligeToBaseReportFolderTestFolder");
            subDirectory.mkdirs();
            writeFile = new File(subDirectory.getPath() + "tempFile.txt");
            writeFile.createNewFile();
        } catch (Exception e){
            Assert.assertTrue("Could not write file.", false);
        } finally {
            try {
                writeFile.delete();
                subDirectory.delete();
            }catch (Exception e){
                System.out.println("Could not delete folder or file.");
            }
        }
        //Assert.assertTrue("Report folder '" + TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER) + "' could not be found or created.", reportFolder.exists());
    }

    @Test
    public void checkDesktopPresence(){
        Assume.assumeTrue("Running tests on a console machine. No desktop supported. GUI based testing might be at risk, depending on the features of the driver used.", Desktop.isDesktopSupported());
    }

    /* Test ideas for further tests

    @Test
    public void checkRegionalTimeFormatSettings(){

    }

    @Test
    public  void checkStandardTextEncoding(){

    }

    @Test
    public void checkInternetPresence(){

    }

    @Test
    public void checkDesktopScreenLock(){

    }
    */
}
