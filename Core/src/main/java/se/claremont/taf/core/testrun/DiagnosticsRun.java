package se.claremont.taf.core.testrun;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import se.claremont.taf.core.logging.KnownError_Test;
import se.claremont.taf.core.logging.KnownErrorsList_Test;
import se.claremont.taf.core.logging.LogPost_Tests;
import se.claremont.taf.core.reporting.testrunreports.htmlsummaryreport.HtmlSummaryReport_Test;
import se.claremont.taf.core.support.ValuePair_Tests;
import se.claremont.taf.core.testcase.TestCaseLog_Tests;
import se.claremont.taf.core.testcase.TestCase_Tests;
import se.claremont.taf.core.testset.TestSet_Tests;

import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Performs a run of selected tests, to assess if a TAF installation is ready to run tests.
 *
 */
public class DiagnosticsRun implements Runnable {
    private final JUnitCore junit;
    private Result result;
    Class<?>[] testClasses = {KnownError_Test.class,
            KnownErrorsList_Test.class,
            LogPost_Tests.class,
            TestCaseLog_Tests.class,
            TestCase_Tests.class,
            TestSet_Tests.class,
            ValuePair_Tests.class,
            HtmlSummaryReport_Test.class,
            EnvironmentSetupTests.class,
            Settings_Tests.class};

    public DiagnosticsRun(JUnitCore jUnitCore){
        junit = jUnitCore;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        PrintStream originalStream = System.out;

        PrintStream dummyStream    = new PrintStream(new OutputStream(){
            public void write(int b) {
            }
        });

        System.setOut(dummyStream);

        //noinspection AccessStaticViaInstance
        result = junit.runClasses(testClasses);

        System.setOut(originalStream);

        if (result.getFailures().size() > 0) {
            System.out.println("Diagnostics test encountered problems. Full diagnostic test output below:" + System.lineSeparator());
            System.out.println("==========================================================================");
            System.out.println(dummyStream.toString());
            TestRun.setExitCode(TestRun.ExitCodeTable.RUN_TEST_ERROR_FATAL.getValue());
            System.out.println();
            for (Failure failure : result.getFailures()) {
                System.out.println("Identified failure: " + failure.toString());
            }
        } else {
            TestRun.setExitCode(TestRun.ExitCodeTable.INIT_OK.getValue());
        }

        System.out.println();
        System.out.println("Diagnostic test run took " + (System.currentTimeMillis() - startTime)/1000 + " seconds." + System.lineSeparator());
        System.out.println("#Diagnostic tests run: " + result.getRunCount());
        System.out.println("#Failed tests: " + result.getFailureCount());
        System.out.println(System.lineSeparator() + "Over all diagnostics result. Successful: " + result.wasSuccessful());
        System.out.println();
    }

    public int getTestCount(){
        int counter = 0;
        for(Class<?> klass : testClasses){
            final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(klass.getDeclaredMethods()));
            for (final Method method : allMethods) {
                if (method.isAnnotationPresent(Test.class)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public Result getResult() {
        return result;
    }
}
