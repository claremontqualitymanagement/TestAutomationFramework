package se.claremont.autotest.common;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by jordam on 2016-12-23.
 */
public class DiagnosticsRun implements Runnable {
    JUnitCore junit;

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

        Result result = junit.runClasses(
                KnownError_Test.class,
                KnownErrorsList_Test.class,
                LogPost_Tests.class,
                TestCaseLog_Tests.class,
                TestCase_Tests.class,
                TestSet_Tests.class,
                ValuePair_Tests.class,
                HtmlSummaryReport_Test.class,
                Settings_Tests.class,
                SummaryReport_Tests.class
        );

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
}
