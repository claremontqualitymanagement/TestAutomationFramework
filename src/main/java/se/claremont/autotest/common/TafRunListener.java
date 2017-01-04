package se.claremont.autotest.common;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Created by jordam on 2017-01-04.
 */
public class TafRunListener extends RunListener {

    public void testRunStarted(Description description){
        System.out.println();
        System.out.println("Test run has started. This may take a while. Strange output may occur below from the tests being performed.");
        System.out.println();
    }

    public void testRunFinished(Result result) throws Exception {
        TestRun.reporters.report();
        System.out.println();
        System.out.println("Test run finished.");
        System.out.println("Test cases run:     " + result.getRunCount());
        System.out.println("Test cases ignored: " + result.getIgnoreCount());
        System.out.println("Test cases failed:  " + result.getFailureCount());
        System.out.println();
        for (Failure failure : result.getFailures()) {
            System.out.println("Failure: " + failure.toString());
            System.out.println();
        }

        System.out.println("Success for classes run: " + result.wasSuccessful() + System.lineSeparator());

    }

}
