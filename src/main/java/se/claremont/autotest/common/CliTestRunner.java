package se.claremont.autotest.common;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import se.claremont.autotest.support.SupportMethods;

/**
 * CLI runner class for the test automation framework
 *
 * Created by jordam on 2016-08-27.
 */
public class CliTestRunner {

    public static final TestRun testRun = new TestRun();

    private static final String LF = SupportMethods.LF;

    private static String helpText(){
        return "Usage instruction: " + LF + LF +
                "Command line options consists of " +
                "listed test classes to be run, or the keyword 'diagnostics' to run the unit tests and " +
                "the diagnostics tests to ensure the local installation is ok. A diagnostic run output results from failed tests as debug information." +
                LF + LF +
                "If test classes are listed as arguments the output of those tests are displayed. This output can be quite extensive, and sometimes it is beneficial to make sure you can read it all." +
                LF +
                "Test output from test classes extending the TestSet class is saved to the output log folder." + LF;
    }

    /**
     * Actual runner method
     * @param args arguments
     */
    public static void main(String [] args){

        if(args.length == 0){
            System.out.print(helpText());
        }
        for(String arg : args){
            if(arg.toLowerCase().contains("diagnostic")){
                Result result = JUnitCore.runClasses(
                        KnownError_Tests.class,
                        KnownErrorsList_Tests.class,
                        LogPost_Tests.class,
                        TestCaseLog_Tests.class,
                        TestCase_Tests.class,
                        TestSet_Tests.class,
                        ValuePair_Tests.class,
                        SummaryReport_Tests.class
                );

                if(result.getFailures().size() > 0) System.out.println();
                for (Failure failure : result.getFailures()) {
                    System.out.println("Identified failure: " + failure.toString());
                }

                System.out.println();
                System.out.println("Over all diagnostics result. Successful: " + result.wasSuccessful());
            }else if(arg.contains("help")||arg.contains("man")||arg.contains("-h")){
                System.out.print(helpText());
            } else {
                try {
                    Result result = JUnitCore.runClasses(Class.forName(arg));

                    for (Failure failure : result.getFailures()) {
                       System.out.println(failure.toString());
                    }

                    System.out.println(result.wasSuccessful());
            } catch (ClassNotFoundException e) {
                    System.out.println("Class with name '" + arg + "' not found.");
                    e.printStackTrace();

                }
            }
        }



    }
}
