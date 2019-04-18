package se.claremont.taf.core.testrun;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.junitcustomization.TafTestRunner;
import se.claremont.taf.core.testset.UnitTestClass;
import se.claremont.taf.testhelpers.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public class TestRunExitCodeTest extends UnitTestClass{

    private void setup(){
        TestRun.setExitCode(TestRun.ExitCodeTable.INIT_OK.getValue());
        System.setProperty("TAF latest test run exit code", "0");
        TestRun.getReporterFactory().reporters.clear();
    }

    @Test
    public void initialValue(){
        setup();
        Assert.assertTrue(TestRun.getExitCode() == 0);
    }

    @Test
    public void cliTestRunnerSettingExitCodeOnOk(){
        setup();
        String[] args = new String[]{ ParallelExecution_TestClass1.class.getName()};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue(System.getProperty("TAF latest test run exit code"), System.getProperty("TAF latest test run exit code").equals("0"));
    }

    @Test
    public void tafTestRunnerSettingExitCodeOnOk(){
        setup();
        List<Class<?>> classes = new ArrayList<>();
        classes.add(ResourceManager.extractFileFromResourcesAndCompileAndLoadIt("PassingTestClass.java"));
        Assert.assertTrue(new TafTestRunner().run(classes).wasSuccessful());
        Assert.assertTrue("Exit code was not " + TestRun.ExitCodeTable.INIT_OK.getValue() + ". It was " + TestRun.getExitCode(), TestRun.getExitCode()== TestRun.ExitCodeTable.INIT_OK.getValue());
    }

    @Test
    public void tafTestRunnerSettingExitCodeOnFrameworkError() throws Exception {
        setup();
        List<Class<?>> classes = new ArrayList<>();
        classes.add(ResourceManager.extractFileFromResourcesAndCompileAndLoadIt("ExceptionThrowingTestClass.java"));
        Assert.assertFalse(new TafTestRunner().run(classes).wasSuccessful());
        Assert.assertTrue("Exit code was not " + TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue() + ". It was " + TestRun.getExitCode(), TestRun.getExitCode() == TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue());
    }

    @Test
    public void tafTestRunnerSettingExitCodeOnVerificationFail()throws Exception{
        setup();
        List<Class<?>> classes = new ArrayList<>();
        classes.add(ResourceManager.extractFileFromResourcesAndCompileAndLoadIt("FailingTestClass.java"));
        Assert.assertFalse(new TafTestRunner().run(classes).wasSuccessful());
        Assert.assertTrue("Exit code was not " + TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue() + ". It was " + TestRun.getExitCode(), TestRun.getExitCode() == TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue());
    }


}
