package se.claremont.taf.core.testrun;

import org.junit.*;
import se.claremont.taf.core.testset.UnitTestClass;

import java.awt.*;
import java.io.File;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class EnvironmentSetupTests extends UnitTestClass{
    private File reportFolder ;
    private boolean reportFolderCreatedForTest = false;

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

    @SuppressWarnings("ConstantConditions")
    @Test
    public void checkFileWritePriviligeToBaseReportFolder(){
        if(!reportFolder.exists()) {
            boolean success = false;
            try{
                success = reportFolder.mkdirs();
                reportFolderCreatedForTest = true;
            }catch (Exception e){
                System.out.println("Could not create report folder. " + e);
            }
            if(!success) System.out.println("Report folder '"  + reportFolder.getPath() + "' did not exist. Could not create report folder.");
        }
        File subDirectory = null;
        File writeFile = null;
        try {
            subDirectory = new File(reportFolder.getPath() + File.separator + "tempFolder" + File.separator);
            if (!subDirectory.exists()) subDirectory.mkdirs();
            writeFile = new File(subDirectory.getPath() + File.separator + "tempFile.txt");
            if (writeFile.exists()) writeFile.delete();
            writeFile.createNewFile();
        } catch (Exception e){
            try {
                writeFile.delete();
                subDirectory.delete();
            }catch (Exception e2){
                System.out.println("Attempting cleanup from failed test but could not delete folder or file. " + e2.toString());
            }
            //noinspection ConstantConditions
            Assert.fail("Could not write '" + subDirectory.getPath() + File.separator + "tempFile.txt" + "' file." + System.lineSeparator() + testOutput() + System.lineSeparator() + "Error: " + e);
        } finally {
            try {
                writeFile.delete();
                subDirectory.delete();
            }catch (Exception e){
                System.out.println("Could not delete folder or file. " + e.toString());
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
