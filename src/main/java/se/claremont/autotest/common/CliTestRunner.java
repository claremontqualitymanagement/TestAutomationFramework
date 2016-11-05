package se.claremont.autotest.common;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.support.SupportMethods;
import se.claremont.tools.Utils;

/**
 * CLI runner class for the test automation framework
 *
 * Created by jordam on 2016-08-27.
 */
public class CliTestRunner {

    private final static Logger logger = LoggerFactory.getLogger( CliTestRunner.class );

//    public static final TestRun testRun = new TestRun();
    private static final String LF = SupportMethods.LF;

    private static String helpText(){
        return  "Usage instruction: " + LF + LF +
                "Command line options consists of " +
                "listed test classes to be run, or the keyword 'diagnostics' to run the unit tests and " +
                "the diagnostics tests to ensure the local installation is ok. A diagnostic run output results from failed tests as debug information." +
                LF + LF +
                "If test classes are listed as arguments the output of those tests are displayed. This output can be quite extensive, and sometimes it is beneficial to make sure you can read it all." +
                LF +
                "Test output from test classes extending the TestSet class is saved to the output log folder." + LF + LF +
                "Settings from file can be overwritten by stating them as arguments using equal sign in between parameter name and parameter value:" + LF +
                "emailRecipients=firstName.lastName@organization.com" + LF + LF;
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
                        HtmlSummaryReport_Tests.class
                );

                if(result.getFailures().size() > 0) System.out.println();
                for (Failure failure : result.getFailures()) {
                    System.out.println("Identified failure: " + failure.toString());
                }

                System.out.println();
                System.out.println("Over all diagnostics result. Successful: " + result.wasSuccessful());
            }else if(arg.contains("help")||arg.contains("man")||arg.contains("-h")){
                System.out.print(helpText());
            } else if(arg.contains("=")){
                TestRun.settings.setCustomValue(arg.split("=")[0].trim(), arg.split("=")[1].trim());
                System.out.println("Setting value '" + arg.split("=")[1].trim() + "' for parameter name '" + arg.split("=")[0].trim() + "'.");
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
                    logger.error( "Class with name '" + arg + "' not found.", e );
                }
            }
        }

        if( TestRun.exitCode == 0 )
            logger.debug( "TAF RUNNING SUCCESSFULLY WITH exitCode= " + TestRun.exitCode );
        else
            logger.debug( "TAF RUNNING ERROR WITH exitCode= " + TestRun.exitCode );

        System.out.println( "TAF exits with exitCode= " + TestRun.exitCode );
        System.exit(TestRun.exitCode);
    }
}
