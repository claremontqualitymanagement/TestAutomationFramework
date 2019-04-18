package se.claremont.taf.core.junitcustomization;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import se.claremont.taf.core.testrun.TestRun;

/**
 * A custom JUnit RunListener, to enable reporting and fancy output.
 *
 * Created by jordam on 2017-01-04.
 */
@SuppressWarnings("WeakerAccess")
public class TafRunListener extends RunListener {

    @Override
    public void testRunStarted(Description description){
        System.out.println();
        System.out.println("Test run has started. This may take a while. Strange output may occur below from the tests being performed.");
        System.out.println();
    }

    //Only here to prevent standard output
    @Override
    public void testRunFinished(Result result) throws Exception {
    }

    public void testRunFinished(TafResult result) {
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
        if(result.getFailureCount() > 0){
            TestRun.setExitCode(TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue());
        }

        System.out.println("Success for classes run: " + result.wasSuccessful() + System.lineSeparator());
        if (result.getFailureCount() == 0) {
            System.out.println(celebration());
        } else{
            System.out.println((oups()));
        }
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

    private static String oups(){
        return System.lineSeparator() +
                "   ____                  _ " + System.lineSeparator() +
                "  / __ \\                | |" + System.lineSeparator() +
                " | |  | |_   _ _ __  ___| |" +System.lineSeparator() +
                " | |  | | | | | '_ \\/ __| |" + System.lineSeparator() +
                " | |__| | |_| | |_) \\__ \\_|" + System.lineSeparator() +
                "  \\____/ \\__,_| .__/|___(_)" + System.lineSeparator() +
                "              | |          " + System.lineSeparator() +
                "              |_|          " + System.lineSeparator();
    }


}
