package se.claremont.taf.core.testrun;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

public class TafTestRunner_Tests {
    private long startTime;
    private FakeTestRunReporter fakeTestRunReporter;
    private final int bufferTimeAccepted = 4;
    private final int numberOfTestCases = 4;
    private final int timePerTestCase = 3;
    private final int getNumberOfTestClasses = 2;

    private void setup(){
        fakeTestRunReporter = new FakeTestRunReporter();
        TestRun.getReporterFactory().reporters.clear();
        TestRun.addTestRunReporterIfNotAlreadyRegistered(fakeTestRunReporter);
        startTime = System.currentTimeMillis();
    }

    private void assertReporting(){
        Assert.assertTrue("Expected one test run report, but found " + fakeTestRunReporter.numberOfReportsPerformed + "." + System.lineSeparator() + fakeTestRunReporter.toString(), fakeTestRunReporter.numberOfReportsPerformed == 1);
        Assert.assertTrue("Expected four test evaluations performed, but found " + fakeTestRunReporter.numberOfTestCaseEvaluationsPerformed + "." + System.lineSeparator() + fakeTestRunReporter.toString(), fakeTestRunReporter.testCaseNames.size() == 4);
        //Assert.assertTrue("Expected two test set evaluations performed, but found " + fakeTestRunReporter.numberOfTestSetEvaluationsPerformed + "." + System.lineSeparator() + fakeTestRunReporter.toString(), fakeTestRunReporter.testSetNames.size() == 2);
    }

    private void assertDuration(int lowestExpectedDurationInSeconds, int highestExpectedDurationInSeconds){
        Assert.assertTrue("Expected more than " + lowestExpectedDurationInSeconds + " seconds. Took: " + (System.currentTimeMillis()-startTime)/1000, System.currentTimeMillis()-startTime > lowestExpectedDurationInSeconds * 1000);
        Assert.assertTrue("Expected less than " + highestExpectedDurationInSeconds + " seconds. Took: " + (System.currentTimeMillis()-startTime)/1000, System.currentTimeMillis()-startTime < highestExpectedDurationInSeconds * 1000);
    }

    @Test
    public void parallelExecutionOneThread(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=1", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(timePerTestCase*numberOfTestCases, timePerTestCase*numberOfTestCases+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionTwoThreads(){
        setup();
        int numberOfThreads = 2;
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=" + numberOfThreads, ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(timePerTestCase*numberOfTestCases/numberOfThreads, timePerTestCase*numberOfTestCases/numberOfThreads+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void settingSystemPropertiesFromCLI(){
        setup();
        int numberOfThreads = 2;
        System.setProperty("testprop", "dummy");
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=" + numberOfThreads, ParallelExecution_TestClass1.class.getName(), "-Dtestprop=yay"};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue(System.getProperty("testprop").equals("yay"));
    }

    @Test
    public void parallelExecutionSetToTrueShouldRunInParallel(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=true", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase/getNumberOfTestClasses, numberOfTestCases*timePerTestCase/getNumberOfTestClasses + bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionSetToFalseShouldRunSequential(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=false", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionExcessiveThreads() throws InitializationError {
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=15", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(timePerTestCase, timePerTestCase + bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionSameAmountOfThreadsAsTests() throws InitializationError {
        setup();
        int numberOfThreads = 2;
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=" + numberOfThreads, ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase/numberOfThreads, numberOfTestCases*timePerTestCase/numberOfThreads+ bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionSingleThread(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=1", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionClasses(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=classes", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase/getNumberOfTestClasses, numberOfTestCases*timePerTestCase/getNumberOfTestClasses+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionMethods(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=methods", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(timePerTestCase*getNumberOfTestClasses, timePerTestCase*getNumberOfTestClasses+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionNone(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=none", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionGibberish(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=sdgrsry", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionZero(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=0", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertReporting();
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
    }

    @Test
    public void parallelExecutionNegative(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=-3", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionDefault(){
        setup();
        String[] args = new String[]{ ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    @Ignore
    public void parallelExecutionEmtpyString(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
        assertReporting();
    }

}
