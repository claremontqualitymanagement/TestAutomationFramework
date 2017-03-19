package se.claremont.autotest.common.testrun;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Created by jordam on 2017-01-04.
 */
@SuppressWarnings("WeakerAccess")
public class TafRunListener extends RunListener {

    public void testRunStarted(Description description){
        System.out.println();
        System.out.println("Test run has started. This may take a while. Strange output may occur below from the tests being performed.");
        System.out.println();
    }

    /*
    public void testFinished(Description description) throws Exception {
        TestRun.testRunResult.addTestCaseResult(description.getMethodName(), description.getClassName(), "Passed");
    }

    public void testFailure(Failure failure) throws Exception {
        TestRun.testRunResult.addTestCaseResult(failure.getDescription().getMethodName(), failure.getDescription().getClassName(), "Failed");
    }

    public void testAssumptionFailure(Failure failure) {
        TestRun.testRunResult.addTestCaseResult(failure.getDescription().getMethodName(), failure.getDescription().getClassName(), "Blocked");
    }

    public void testIgnored(Description description) throws Exception {
        TestRun.testRunResult.addTestCaseResult(description.getMethodName(), description.getClassName(), "Ignored");
    }
    */

    public void testRunFinished(Result result) throws Exception {
        TestRun.reportTestRun();
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
        sendRunResultToRESTServerIfApplicable();
        if (result.getFailureCount() == 0) System.out.println(celebration());
    }

    private void sendRunResultToRESTServerIfApplicable(){
        //System.out.println("Test run results to JSON: " + TestRun.testRunResult.toJson());
    }

    private static String celebration(){
        return System.lineSeparator() +
                "    __     ___             _      _ " + System.lineSeparator() +
                "    \\ \\   / (_)           (_)    | |" + System.lineSeparator() +
                "     \\ \\_/ / _ _ __  _ __  _  ___| |" + System.lineSeparator() +
                "      \\   / | | '_ \\| '_ \\| |/ _ \\ |" + System.lineSeparator() +
                "       | |  | | |_) | |_) | |  __/_|" + System.lineSeparator() +
                "       |_|  |_| .__/| .__/|_|\\___(_)" + System.lineSeparator() +
                "              | |   | |             " + System.lineSeparator() +
                "              |_|   |_|             " + System.lineSeparator();
    }


}
