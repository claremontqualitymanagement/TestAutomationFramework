package se.claremont.autotest.common.testrun;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import se.claremont.autotest.common.logging.KnownError_Test;
import se.claremont.autotest.common.logging.KnownErrorsList_Test;
import se.claremont.autotest.common.logging.LogPost_Tests;
import se.claremont.autotest.common.reporting.testrunreports.htmlsummaryreport.HtmlSummaryReport_Test;
import se.claremont.autotest.common.support.ValuePair_Tests;
import se.claremont.autotest.common.testcase.TestCaseLog_Tests;
import se.claremont.autotest.common.testcase.TestCase_Tests;
import se.claremont.autotest.common.testset.TestSet_Tests;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Performs a run of selected tests, to assess if a TAF installation is ready to run tests.
 *
 * Created by jordam on 2016-12-23.
 */
public class DiagnosticsRun implements Runnable {
    private JUnitCore junit;
    private Result result;

    public DiagnosticsRun(JUnitCore jUnitCore){
        junit = jUnitCore;
    }

    @Override
    public void run() {
        PrintStream originalStream = System.out;

        PrintStream dummyStream    = new PrintStream(new OutputStream(){
            public void write(int b) {
            }
        });

        System.setOut(dummyStream);

        //noinspection AccessStaticViaInstance
        result = junit.runClasses(KnownError_Test.class,
                KnownErrorsList_Test.class,
                LogPost_Tests.class,
                TestCaseLog_Tests.class,
                TestCase_Tests.class,
                TestSet_Tests.class,
                ValuePair_Tests.class,
                HtmlSummaryReport_Test.class,
                Settings_Tests.class);

        System.setOut(originalStream);

        if (result.getFailures().size() > 0) System.out.println();
        for (Failure failure : result.getFailures()) {
            System.out.println("Identified failure: " + failure.toString());
        }

        System.out.println();
        System.out.println("#Diagnostic tests run: " + result.getRunCount());
        System.out.println("#Failed tests: " + result.getFailureCount());
        System.out.println(System.lineSeparator() + "Over all diagnostics result. Successful: " + result.wasSuccessful());
        System.out.println();
    }

    public Result getResult() {
        return result;
    }
}
