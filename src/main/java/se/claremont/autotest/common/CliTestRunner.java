package se.claremont.autotest.common;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.support.SupportMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * CLI runner class for the test automation framework
 *
 * Created by jordam on 2016-08-27.
 */
public class CliTestRunner {

    private final static Logger logger = LoggerFactory.getLogger( CliTestRunner.class );

//    public static final TestRun testRun = new TestRun();
    private static final String LF = SupportMethods.LF;

    private static String helpText() {
        return "Usage instruction: " + LF + LF +
                "Command line options consists of " +
                "listed test classes to be run, or the keyword 'diagnostics' to run the unit tests and " +
                "the diagnostics tests to ensure the local installation is ok. A diagnostic run output results from failed tests as debug information." +
                LF + LF +
                "A test run name can be set using the argument runName" + LF +
                "Example: RunName=MyTestRun1stOfApril" + LF +
                "This test run name will be used for log folder name creation." + LF +
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
    public static void main(String [] args) {
        System.out.println();
        List<Class<?>> classes = new ArrayList<Class<?>>();

        TestRun.initializeIfNotInitialized();

        if (args.length == 0) {
            System.out.print(helpText());
            return;
        }
        JUnitCore junit = new JUnitCore();
        TestRun.reporters.addTestRunReporterIfNotAlreadyRegistered(new TestRunReporterHtmlSummaryReportFile());
        junit.addListener(new TafRunListener());

        for (String arg : args) {
            if (
                    arg.equals("help") ||
                    arg.equals("man") ||
                    arg.equals("-h") ||
                    arg.equals("-man") ||
                    arg.equals("--man") ||
                    arg.equals("-help") ||
                    arg.equals("--help")
                    ){
                System.out.print(helpText());
                return;
            } else if (arg.contains("=")) { //Setting (overriding) run time settings
                String[] parts = arg.split("=");
                if(parts[0].toLowerCase().equals("runname")){
                    TestRun.testRunName = parts[1].trim();
                    System.out.println("Setting test run name to '" + TestRun.testRunName + "'.");
                } else {
                    TestRun.settings.setCustomValue(arg.split("=")[0].trim(), arg.split("=")[1].trim());
                    System.out.println("Setting value '" + arg.split("=")[1].trim() + "' for parameter name '" + arg.split("=")[0].trim() + "'.");
                }
            } else if (arg.toLowerCase().equals("diagnostic") || arg.toLowerCase().equals("diagnostics")) {
                System.out.println("Running diagnostic tests. This might take a few minutes while strange things might occur on the screen. Don't be alarmed. Be patient.");
                List<Thread> threads = new ArrayList<>();

                //Running tests in separate thread to enable running a dot-printer to the command line to show progress. Not yet implemented.
                DiagnosticsRun diag = new DiagnosticsRun(junit);
                Thread diagnosticsRunThread = new Thread(diag);
                threads.add(diagnosticsRunThread);
                diagnosticsRunThread.start();

                //Waiting for threads to finish
                for (int i = 0; i < threads.size(); i++)
                    try {
                        threads.get(i).join();
                    } catch (InterruptedException e) {
                    }

                return;
            } else {
                try {
                    classes.add(Class.forName(arg));
                } catch (ClassNotFoundException e) {
                    System.out.println("Warning: Class '" + arg + "' not found.");
                }
            }
        }

        Result result = junit.run(classes.toArray(new Class[0]));

        if (TestRun.exitCode == 0)
            logger.debug(System.lineSeparator() + "TAF RUNNING SUCCESSFULLY WITH exitCode= " + TestRun.exitCode);
        else
            logger.debug(System.lineSeparator() + "TAF RUNNING ERROR WITH exitCode= " + TestRun.exitCode);

        System.exit(TestRun.exitCode);

    }
}
