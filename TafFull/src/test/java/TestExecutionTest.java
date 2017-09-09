import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import se.claremont.autotest.common.logging.LogFolder;
import se.claremont.autotest.common.testrun.CliTestRunner;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testset.ParallelExecution;
import se.claremont.autotest.common.testset.UnitTestClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public class TestExecutionTest {
    String runName;

    @After
    public void teardown() throws IOException {
        for(File directory : new File(TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER)).listFiles(File::isDirectory)){
            if(directory.getName().contains(runName)){
                directory.delete();
                return;
            }
        }
    }

    @Test
    public void makeSureEachTestCaseIsBeingLoggedToCorrectlyWhenRunInParallelByThreadCount(){
        checkWriteAccess();
        TestRun.isInitialized = false;
        LogFolder.testRunLogFolder = null;
        runName = UUID.randomUUID().toString();
        String[] args = new String[]{ "runName=" + runName, "PARALLEL_TEST_EXECUTION_MODE=8", ParallelExecution.class.getName(), ParallelExecution2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertLogFiles();
    }

    @Test
    public void makeSureEachTestCaseIsBeingLoggedToCorrectlyWhenRunInParallelByClasses(){
        checkWriteAccess();
        TestRun.isInitialized = false;
        LogFolder.testRunLogFolder = null;
        runName = UUID.randomUUID().toString();
        String[] args = new String[]{ "runName=" + runName, "PARALLEL_TEST_EXECUTION_MODE=classes", ParallelExecution.class.getName(), ParallelExecution2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertLogFiles();
    }

    @Test
    public void makeSureEachTestCaseIsBeingLoggedToCorrectlyWhenRunInParallelByMethods(){
        checkWriteAccess();
        TestRun.isInitialized = false;
        LogFolder.testRunLogFolder = null;
        runName = UUID.randomUUID().toString();
        String[] args = new String[]{ "runName=" + runName, "PARALLEL_TEST_EXECUTION_MODE=methods", ParallelExecution.class.getName(), ParallelExecution2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertLogFiles();
    }


    private void assertLogFiles() {
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseStockholm.html"));
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseMalmoe.html"));
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseUppsala.html"));
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseGothenburg.html"));
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseStockholm.txt"));
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseMalmoe.txt"));
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseUppsala.txt"));
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseGothenburg.txt"));
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseArboga.html"));
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseFilipstad.html"));
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseKalmar.html"));
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseKarlstad.html"));
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseArboga.txt"));
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseFilipstad.txt"));
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseKalmar.txt"));
        Assert.assertTrue(testCaseLogFileFound("parallelExecutionTestCaseKarlstad.txt"));

        String logFile = testCaseLogFileContent("parallelExecutionTestCaseStockholm.html");
        Assert.assertNotNull(logFile);
        Assert.assertTrue(logFile.contains("Stockholm"));
        Assert.assertFalse(logFile.contains("Malmoe"));
        Assert.assertFalse(logFile.contains("Uppsala"));
        Assert.assertFalse(logFile.contains("Gothenburg"));
        Assert.assertFalse(logFile.contains("Arboga"));
        Assert.assertFalse(logFile.contains("Kalmar"));
        Assert.assertFalse(logFile.contains("Karlstad"));
        Assert.assertFalse(logFile.contains("Filipstad"));

        logFile = testCaseLogFileContent("parallelExecutionTestCaseStockholm.txt");
        Assert.assertNotNull(logFile);
        Assert.assertTrue(logFile.contains("Stockholm"));
        Assert.assertFalse(logFile.contains("Malmoe"));
        Assert.assertFalse(logFile.contains("Uppsala"));
        Assert.assertFalse(logFile.contains("Gothenburg"));
        Assert.assertFalse(logFile.contains("Arboga"));
        Assert.assertFalse(logFile.contains("Kalmar"));
        Assert.assertFalse(logFile.contains("Karlstad"));
        Assert.assertFalse(logFile.contains("Filipstad"));

        logFile = testCaseLogFileContent("parallelExecutionTestCaseMalmoe.html");
        Assert.assertNotNull(logFile);
        Assert.assertFalse(logFile.contains("Stockholm"));
        Assert.assertTrue(logFile.contains("Malmoe"));
        Assert.assertFalse(logFile.contains("Uppsala"));
        Assert.assertFalse(logFile.contains("Gothenburg"));
        Assert.assertFalse(logFile.contains("Arboga"));
        Assert.assertFalse(logFile.contains("Kalmar"));
        Assert.assertFalse(logFile.contains("Karlstad"));
        Assert.assertFalse(logFile.contains("Filipstad"));

        logFile = testCaseLogFileContent("parallelExecutionTestCaseMalmoe.txt");
        Assert.assertNotNull(logFile);
        Assert.assertFalse(logFile.contains("Stockholm"));
        Assert.assertTrue(logFile.contains("Malmoe"));
        Assert.assertFalse(logFile.contains("Uppsala"));
        Assert.assertFalse(logFile.contains("Gothenburg"));
        Assert.assertFalse(logFile.contains("Arboga"));
        Assert.assertFalse(logFile.contains("Kalmar"));
        Assert.assertFalse(logFile.contains("Karlstad"));
        Assert.assertFalse(logFile.contains("Filipstad"));

        logFile = testCaseLogFileContent("parallelExecutionTestCaseUppsala.html");
        Assert.assertNotNull(logFile);
        Assert.assertFalse(logFile.contains("Stockholm"));
        Assert.assertFalse(logFile.contains("Malmoe"));
        Assert.assertTrue(logFile.contains("Uppsala"));
        Assert.assertFalse(logFile.contains("Gothenburg"));
        Assert.assertFalse(logFile.contains("Arboga"));
        Assert.assertFalse(logFile.contains("Kalmar"));
        Assert.assertFalse(logFile.contains("Karlstad"));
        Assert.assertFalse(logFile.contains("Filipstad"));

        logFile = testCaseLogFileContent("parallelExecutionTestCaseUppsala.txt");
        Assert.assertNotNull(logFile);
        Assert.assertFalse(logFile.contains("Stockholm"));
        Assert.assertFalse(logFile.contains("Malmoe"));
        Assert.assertTrue(logFile.contains("Uppsala"));
        Assert.assertFalse(logFile.contains("Gothenburg"));
        Assert.assertFalse(logFile.contains("Arboga"));
        Assert.assertFalse(logFile.contains("Kalmar"));
        Assert.assertFalse(logFile.contains("Karlstad"));
        Assert.assertFalse(logFile.contains("Filipstad"));

        logFile = testCaseLogFileContent("parallelExecutionTestCaseGothenburg.html");
        Assert.assertNotNull(logFile);
        Assert.assertFalse(logFile.contains("Stockholm"));
        Assert.assertFalse(logFile.contains("Malmoe"));
        Assert.assertFalse(logFile.contains("Uppsala"));
        Assert.assertTrue(logFile.contains("Gothenburg"));
        Assert.assertFalse(logFile.contains("Arboga"));
        Assert.assertFalse(logFile.contains("Kalmar"));
        Assert.assertFalse(logFile.contains("Karlstad"));
        Assert.assertFalse(logFile.contains("Filipstad"));

        logFile = testCaseLogFileContent("parallelExecutionTestCaseArboga.html");
        Assert.assertNotNull(logFile);
        Assert.assertFalse(logFile.contains("Stockholm"));
        Assert.assertFalse(logFile.contains("Malmoe"));
        Assert.assertFalse(logFile.contains("Uppsala"));
        Assert.assertFalse(logFile.contains("Gothenburg"));
        Assert.assertTrue(logFile.contains("Arboga"));
        Assert.assertFalse(logFile.contains("Kalmar"));
        Assert.assertFalse(logFile.contains("Karlstad"));
        Assert.assertFalse(logFile.contains("Filipstad"));

        logFile = testCaseLogFileContent("parallelExecutionTestCaseFilipstad.html");
        Assert.assertNotNull(logFile);
        Assert.assertFalse(logFile.contains("Stockholm"));
        Assert.assertFalse(logFile.contains("Malmoe"));
        Assert.assertFalse(logFile.contains("Uppsala"));
        Assert.assertFalse(logFile.contains("Gothenburg"));
        Assert.assertFalse(logFile.contains("Arboga"));
        Assert.assertFalse(logFile.contains("Kalmar"));
        Assert.assertFalse(logFile.contains("Karlstad"));
        Assert.assertTrue(logFile.contains("Filipstad"));

        logFile = testCaseLogFileContent("parallelExecutionTestCaseKalmar.html");
        Assert.assertNotNull(logFile);
        Assert.assertFalse(logFile.contains("Stockholm"));
        Assert.assertFalse(logFile.contains("Malmoe"));
        Assert.assertFalse(logFile.contains("Uppsala"));
        Assert.assertFalse(logFile.contains("Gothenburg"));
        Assert.assertFalse(logFile.contains("Arboga"));
        Assert.assertTrue(logFile.contains("Kalmar"));
        Assert.assertFalse(logFile.contains("Karlstad"));
        Assert.assertFalse(logFile.contains("Filipstad"));

        logFile = testCaseLogFileContent("parallelExecutionTestCaseKarlstad.html");
        Assert.assertNotNull(logFile);
        Assert.assertFalse(logFile.contains("Stockholm"));
        Assert.assertFalse(logFile.contains("Malmoe"));
        Assert.assertFalse(logFile.contains("Uppsala"));
        Assert.assertFalse(logFile.contains("Gothenburg"));
        Assert.assertFalse(logFile.contains("Arboga"));
        Assert.assertFalse(logFile.contains("Kalmar"));
        Assert.assertTrue(logFile.contains("Karlstad"));
        Assert.assertFalse(logFile.contains("Filipstad"));

        logFile = testCaseLogFileContent("parallelExecutionTestCaseGothenburg.txt");
        Assert.assertNotNull(logFile);
        Assert.assertFalse(logFile.contains("Stockholm"));
        Assert.assertFalse(logFile.contains("Malmoe"));
        Assert.assertFalse(logFile.contains("Uppsala"));
        Assert.assertTrue(logFile.contains("Gothenburg"));
        Assert.assertFalse(logFile.contains("Arboga"));
        Assert.assertFalse(logFile.contains("Kalmar"));
        Assert.assertFalse(logFile.contains("Karlstad"));
        Assert.assertFalse(logFile.contains("Filipstad"));

        logFile = testCaseLogFileContent("parallelExecutionTestCaseKalmar.txt");
        Assert.assertNotNull(logFile);
        Assert.assertFalse(logFile.contains("Stockholm"));
        Assert.assertFalse(logFile.contains("Malmoe"));
        Assert.assertFalse(logFile.contains("Uppsala"));
        Assert.assertFalse(logFile.contains("Gothenburg"));
        Assert.assertFalse(logFile.contains("Arboga"));
        Assert.assertTrue(logFile.contains("Kalmar"));
        Assert.assertFalse(logFile.contains("Karlstad"));
        Assert.assertFalse(logFile.contains("Filipstad"));

        logFile = testCaseLogFileContent("parallelExecutionTestCaseKarlstad.txt");
        Assert.assertNotNull(logFile);
        Assert.assertFalse(logFile.contains("Stockholm"));
        Assert.assertFalse(logFile.contains("Malmoe"));
        Assert.assertFalse(logFile.contains("Uppsala"));
        Assert.assertFalse(logFile.contains("Gothenburg"));
        Assert.assertFalse(logFile.contains("Arboga"));
        Assert.assertFalse(logFile.contains("Kalmar"));
        Assert.assertTrue(logFile.contains("Karlstad"));
        Assert.assertFalse(logFile.contains("Filipstad"));

        logFile = testCaseLogFileContent("parallelExecutionTestCaseFilipstad.txt");
        Assert.assertNotNull(logFile);
        Assert.assertFalse(logFile.contains("Stockholm"));
        Assert.assertFalse(logFile.contains("Malmoe"));
        Assert.assertFalse(logFile.contains("Uppsala"));
        Assert.assertFalse(logFile.contains("Gothenburg"));
        Assert.assertFalse(logFile.contains("Arboga"));
        Assert.assertFalse(logFile.contains("Kalmar"));
        Assert.assertFalse(logFile.contains("Karlstad"));
        Assert.assertTrue(logFile.contains("Filipstad"));

        logFile = testCaseLogFileContent("parallelExecutionTestCaseArboga.txt");
        Assert.assertNotNull(logFile);
        Assert.assertFalse(logFile.contains("Stockholm"));
        Assert.assertFalse(logFile.contains("Malmoe"));
        Assert.assertFalse(logFile.contains("Uppsala"));
        Assert.assertFalse(logFile.contains("Gothenburg"));
        Assert.assertTrue(logFile.contains("Arboga"));
        Assert.assertFalse(logFile.contains("Kalmar"));
        Assert.assertFalse(logFile.contains("Karlstad"));
        Assert.assertFalse(logFile.contains("Filipstad"));
    }

    @Test
    public void makeSureTestRunReportIsProduced(){
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
         for(File directory : new File(TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER)).listFiles(File::isDirectory)){
             if(directory.getName().contains(runName)){
                 for(File file : directory.listFiles()){
                     if(file.getName().equals("_summary.html")){
                         return true;
                     }
                 }
             }
         }
         return false;
     }

    private boolean testCaseLogFileFound(String fileName){
        for(File directory : new File(TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER)).listFiles(File::isDirectory)){
            if(directory.getName().contains(runName)){
                for(File file : directory.listFiles()){
                    if(file.getName().equals(fileName)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String testCaseLogFileContent(String fileName){
        for(File directory : new File(TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER)).listFiles(File::isDirectory)){
            if(directory.getName().contains(runName)){
                for(File file : directory.listFiles()){
                    if(file.getName().equals(fileName)){
                        try {
                            return String.join(System.lineSeparator(), Files.readAllLines(file.toPath()));
                        } catch (IOException ignored) {
                        }
                    }
                }
            }
        }
        return null;
    }

     private void checkWriteAccess(){
         File f = new File(TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER));
         Assume.assumeTrue("Cannot write to base folder. Ignoring test.", f.canWrite());
     }
}
