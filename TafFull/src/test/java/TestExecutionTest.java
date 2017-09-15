import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import se.claremont.autotest.common.logging.LogFolder;
import se.claremont.autotest.common.testrun.CliTestRunner;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testset.UnitTestClass;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class TestExecutionTest extends UnitTestClass{
    String runName;

    @After
    public void teardown() throws IOException {
        File baseFolder = new File(TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER));
        for(File directory : baseFolder.listFiles()){
            if(directory.isDirectory() && directory.getName().contains(runName)){
                directory.delete();
                return;
            }
        }
    }

    @Test
    public void testRunReportProduced(){
        checkWriteAccess();
         runName = UUID.randomUUID().toString();
         TestRun.isInitialized = false;
         LogFolder.testRunLogFolder = null;
         String[] args = new String[] { "runName=" + runName, TafSystemTests.class.getName()};
         CliTestRunner.runInTestMode(args);
         Assert.assertTrue(summaryFileFound());
         //C:\Users\jordam\TAF\20170907_134612_se_claremont_autotest_common_testset_TestSet\
     }

     private boolean summaryFileFound(){
        File baseLogFolder = new File(TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER));
         for(File directory : baseLogFolder.listFiles()){
             if(directory.isDirectory() && directory.getName().contains(runName)){
                 for(File file : directory.listFiles()){
                     if(file.getName().equals("_summary.html")){
                         return true;
                     }
                 }
             }
         }
         return false;
     }

     private void checkWriteAccess(){
         File f = new File(TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER));
         Assume.assumeTrue("Cannot write to base folder. Ignoring test.", f.canWrite());
     }
}
