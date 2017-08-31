package se.claremont.autotest.common.testrun;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class TafTestRunnerTest {

    @Test
    public void parallelExecution(){
        TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, "both");
        long startTime = System.currentTimeMillis();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=both", "se.claremont.autotest.common.testrun.ParallelExecutionTest"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue("Expected more than 3000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime > 3000);
        Assert.assertTrue("Expected less than 6000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime < 6000);
    }

    @Test
    public void parallelExecutionSetToTrueShouldRunInParallel(){
        TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, "true");
        long startTime = System.currentTimeMillis();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=true", "se.claremont.autotest.common.testrun.ParallelExecutionTest"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue("Expected more than 3000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime > 3000);
        Assert.assertTrue("Expected less than 6000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime < 6000);
    }

    @Test
    public void parallelExecutionSetToFalseShouldRunSequential(){
        TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, "false");
        long startTime = System.currentTimeMillis();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=false", "se.claremont.autotest.common.testrun.ParallelExecutionTest"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue("Expected more than 6000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime > 6000);
        Assert.assertTrue("Expected less than 9000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime < 9000);
    }

    @Test
    @Ignore
    public void parallelExecutionExcessiveThreads() throws InitializationError {
        TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, "3");
        long startTime = System.currentTimeMillis();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=3", "se.claremont.autotest.common.testrun.ParallelExecutionTest"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue("Expected more than 3000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime > 3000);
        Assert.assertTrue("Expected less than 6000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime < 6000);
    }

    @Test
    @Ignore
    public void parallelExecutionSameAmountOfThreadsAsTests() throws InitializationError {
        TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, "2");
        long startTime = System.currentTimeMillis();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=2", "se.claremont.autotest.common.testrun.ParallelExecutionTest"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue("Expected more than 3000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime > 3000);
        Assert.assertTrue("Expected less than 6000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime < 6000);
    }

    @Test
    @Ignore
    public void parallelExecutionSingleThread(){
        TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, "1");
        long startTime = System.currentTimeMillis();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=1", "se.claremont.autotest.common.testrun.ParallelExecutionTest"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue("Expected more than 6000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime > 6000);
        Assert.assertTrue("Expected less than 8000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime < 8000);
    }

    @Test
    public void parallelExecutionBoth(){
        TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, "both");
        long startTime = System.currentTimeMillis();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=both", "se.claremont.autotest.common.testrun.ParallelExecutionTest"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue("Expected more than 3000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime > 3000);
        Assert.assertTrue("Expected less than 6000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime < 6000);
    }

    @Test
    public void parallelExecutionClasses(){
        TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, "classes");
        long startTime = System.currentTimeMillis();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=classes", "se.claremont.autotest.common.testrun.ParallelExecutionTest"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue("Expected more than 6000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime > 6000);
        Assert.assertTrue("Expected less than 9000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime < 9000);
    }

    @Test
    public void parallelExecutionMethods(){
        TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, "methods");
        long startTime = System.currentTimeMillis();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=methods", "se.claremont.autotest.common.testrun.ParallelExecutionTest"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue("Expected more than 3000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime > 3000);
        Assert.assertTrue("Expected less than 6000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime < 6000);
    }

    @Test
    public void parallelExecutionNone(){
        TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, "none");
        long startTime = System.currentTimeMillis();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=none", "se.claremont.autotest.common.testrun.ParallelExecutionTest"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue("Expected more than 6000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime > 6000);
        Assert.assertTrue("Expected less than 9000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime < 9000);
    }

    @Test
    public void parallelExecutionGibberish(){
        TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, "nosdfasdfne");
        long startTime = System.currentTimeMillis();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=sdgrsry", "se.claremont.autotest.common.testrun.ParallelExecutionTest"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue("Expected more than 6000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime > 6000);
        Assert.assertTrue("Expected less than 9000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime < 9000);
    }

    @Test
    @Ignore
    public void parallelExecutionZero(){
        TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, "0");
        long startTime = System.currentTimeMillis();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=0", "se.claremont.autotest.common.testrun.ParallelExecutionTest"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue("Expected more than 6000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime > 6000);
        Assert.assertTrue("Expected less than 9000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime < 9000);
    }

    @Test
    @Ignore
    public void parallelExecutionNegative(){
        TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, "-3");
        long startTime = System.currentTimeMillis();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=-3", "se.claremont.autotest.common.testrun.ParallelExecutionTest"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue("Expected more than 6000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime > 6000);
        Assert.assertTrue("Expected less than 9000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime < 9000);
    }

    @Test
    public void parallelExecutionDefault(){
        TestRun.isInitialized = false;
        TestRun.initializeIfNotInitialized();
        long startTime = System.currentTimeMillis();
        String[] args = new String[]{ "se.claremont.autotest.common.testrun.ParallelExecutionTest"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue("Expected more than 6000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime > 6000);
        Assert.assertTrue("Expected less than 9000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime < 9000);
    }

    @Test
    @Ignore
    public void parallelExecutionEmtpyString(){
        long startTime = System.currentTimeMillis();
        TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, "");
        System.out.println(TestRun.getSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE));
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=", "se.claremont.autotest.common.testrun.ParallelExecutionTest"};
        CliTestRunner.runInTestMode(args);
        System.out.println(TestRun.getSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE));
        Assert.assertTrue("Expected more than 6000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime > 6000);
        Assert.assertTrue("Expected less than 9000 ms. Took: " + (System.currentTimeMillis()-startTime), System.currentTimeMillis()-startTime < 9000);
    }
}
