package se.claremont.taf.core.junitcustomization;

import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import se.claremont.taf.core.reporting.testrunreports.TestRunReporterHtmlSummaryReportFile;
import se.claremont.taf.core.testrun.Settings;
import se.claremont.taf.core.testrun.TestRun;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TafTestRunner {

    public TafResult run(List<Class<?>> classes) {
        JUnitCore junit = new JUnitCore();
        TafResult tafResult = new TafResult();
        TestRun.getReporterFactory().addTestRunReporterIfNotAlreadyRegistered(new TestRunReporterHtmlSummaryReportFile());
        TafRunListener runListener = new TafRunListener();

        if (classes.size() == 0) {
            System.out.println(System.lineSeparator() + "No test classes given for execution." + System.lineSeparator() + System.lineSeparator() + "If in doubt of how to use this command line interface, please try the help switch or the Wiki.");
            runListener.testRunFinished(tafResult);
            return tafResult;
        }
        String threadMode = TestRun.getSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE);
        if (threadMode.toLowerCase().equals("methods") || threadMode.toLowerCase().equals("true")) {
            System.out.println("Running test methods in parallel per method.");
            runListener.testRunStarted(null);
            tafResult.addTestResult(junit.run(new ParallelComputer(false, true), classes.toArray(new Class[0])));
        } else if (threadMode.toLowerCase().equals("classes")) {
            System.out.println("Running test methods in parallel per classes.");
            runListener.testRunStarted(null);
            tafResult.addTestResult(junit.run(new ParallelComputer(true, false), classes.toArray(new Class[0])));
        } else if (threadMode.toLowerCase().equals("false") || threadMode.toLowerCase().equals("none") ||threadMode.equals("1")) {
            System.out.println("Running test methods in sequence. No parallelism.");
            runListener.testRunStarted(null);
            tafResult.addTestResult(junit.run(classes.toArray(new Class[0])));
        } else {
            int threads = 1;
            try {
                threads = Integer.parseInt(threadMode);
            } catch (Exception ignored) {
            }
            //noinspection ConstantConditions
            if (threadMode != null && threads > 1) {
                System.out.println("Running test methods in parallel in " + threads + " concurrent execution threads.");
                TafParallelTestCaseRunner p = new TafParallelTestCaseRunner(threads);
                for (Class c : classes) {
                    p.addTestClasses(c);
                }
                try {
                    runListener.testRunStarted(null);
                    tafResult.addTestResult(p.run());
                } catch (ExecutionException | InterruptedException e) {
                    System.out.println("Could not execute tests by using parallel execution in thread pool. Try executing with PARALLEL_TEST_EXECUTION_MODE 'none', 'classes', 'methods', 'both'. Error: " + e.toString());
                }
                if(tafResult.getFailureCount() > 0){
                    TestRun.setExitCode(TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue());
                }
            } else {
                if(threads != 1) System.out.println("WARNING: '" + TestRun.getSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE) + "' is an unrecognized value for TestRun SettingParameter PARALLEL_TEST_EXECUTION_MODE. Managed values are 'methods', 'classes', 'both', 'none', 'true', 'false', or a numeric value indicating the number of concurrent execution threads to use for execution. Resorting to default by not running tests in parallel.");
                System.out.println("Running test methods in sequence. No parallelism.");
                tafResult.addTestResult(junit.run(classes.toArray(new Class[0])));
            }
        }
        TestRun.reportTestRun();
        runListener.testRunFinished(tafResult);
        return tafResult;
    }
}
