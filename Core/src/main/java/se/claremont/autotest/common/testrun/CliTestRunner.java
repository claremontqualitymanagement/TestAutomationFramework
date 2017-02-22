package se.claremont.autotest.common.testrun;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.common.reporting.testrunreports.TestRunReporterHtmlSummaryReportFile;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.support.Utils;
import se.claremont.autotest.common.support.api.Taf;

import java.util.ArrayList;
import java.util.List;

/**
 * CLI runner class for the test automation framework
 *
 * Created by jordam on 2016-08-27.
 */
public class CliTestRunner {

    private static List<String> remainingArguments ;
    private final static Logger logger = LoggerFactory.getLogger( CliTestRunner.class );
    private static boolean testMode = false;

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
                "If you want to initiate a test run it sometimes can be convenient to just point to a prepared runSettings file with properties. This could for example be very useful when running your tests in a CI/CD environment. You can do this by providing the argument 'settingsfile' or 'runsettingsfile'. E.g.:" + LF +
                LF + LF +
                " java -jar MyTestProject.jar runSettingsFile=C:\\temp\\runSettings.properties com.organization.testproject.MyTestClass1 com.organization.testproject.MyTestClass2" + LF + LF +
                "The line above will use the settings parameters in the C:\\Temp\\runSettings.properties file to execute the tests in the classes MyTestClass1 and MyTestClass2 found in the package 'com.organization.testproject'." +
                "If test classes are listed as arguments the output of those tests are displayed. This output can be quite extensive, and sometimes it is beneficial to make sure you can read it all." +
                LF + LF +
                "Test output from test classes extending the TestSet class is saved to the output log folder." + LF + LF +
                "Settings from file can be overwritten by stating them as arguments using equal sign in between parameter name and parameter value:" + LF +
                "emailRecipients=firstName.lastName@organization.com" + LF + LF;
    }

    private static void setRunSettingsFileIfGivenAsArgument(){
        String[] args = stringListToArray(remainingArguments);
        for(String arg : args){
            if(arg.trim().length() > 0 && arg.contains("=")){
                String[] parts = arg.split("=");
                if((parts[0].trim().toLowerCase().equals("settingsfile") || parts[0].trim().toLowerCase().equals("runsettingsfile")) && parts.length > 1) {
                    TestRun.settings = new Settings(arg.trim().substring(arg.indexOf("=") + 1).trim());
                    System.out.println("Run settings properties file = '" + parts[1].trim() + "'.");
                    remainingArguments.remove(arg);
                }
            }
        }
    }

    private static void setRunNameIfGivenAsArgument(){
        String[] args = stringListToArray(remainingArguments);
        for(String arg : args){
            if(arg.trim().length() > 0 && arg.contains("=")){
                String[] parts = arg.split("=");
                if(parts[0].trim().toLowerCase().equals("runname") && parts.length > 1) {
                    TestRun.testRunName = arg.trim().substring(arg.indexOf("=") + 1).trim();
                    System.out.println("Run name is set to '" + parts[1].trim() + "'.");
                    remainingArguments.remove(arg);
                }
            }
        }
    }


    private static void setRunSettingsParametersGivenAsArguments(){
        String[] args = stringListToArray(remainingArguments);
        for(String arg : args){
            if(arg.trim().length() > 0 && arg.contains("=")){
                String[] parts = arg.split("=");
                if(parts[0].trim().length() > 0 && parts.length > 1) {
                    String parameterName = parts[0].trim();
                    for(Settings.SettingParameters parameter : Settings.SettingParameters.values()){
                        if(parameterName.toUpperCase().equals(parameter.toString())){
                            parameterName = parameterName.toUpperCase();
                        }
                    }
                    TestRun.settings.setCustomValue(parameterName, arg.trim().substring(arg.trim().indexOf("=") + 1).trim());
                    System.out.println("Setting parameter '" + arg.trim().split("=")[0].trim() + "' to value '" + arg.trim().substring(arg.trim().indexOf("=") + 1).trim() + "'.");
                    remainingArguments.remove(arg);
                }
            }
        }
    }

    private static void printHelpTextIfApplicable(){
        String[] args = stringListToArray(remainingArguments);
        if (args.length == 0) {
            System.out.print(helpText());
            TestRun.exitCode = TestRun.ExitCodeTable.INIT_OK.getValue();
            exitWithExitCode();
            return;
        }
        for (String arg : args) {
            if (
                    arg.trim().toLowerCase().equals("help") ||
                            arg.trim().toLowerCase().equals("man") ||
                            arg.trim().toLowerCase().equals("-h") ||
                            arg.trim().toLowerCase().equals("-man") ||
                            arg.trim().toLowerCase().equals("--man") ||
                            arg.trim().toLowerCase().equals("-help") ||
                            arg.trim().toLowerCase().equals("--help")
                    ){
                System.out.print(helpText());
                remainingArguments.remove(arg);
                TestRun.exitCode = TestRun.ExitCodeTable.INIT_OK.getValue();
                exitWithExitCode();
                return;
            }
        }
    }

    private static void exitWithExitCode(){
        if (TestRun.exitCode == TestRun.ExitCodeTable.INIT_OK.getValue())
            System.out.println(System.lineSeparator() + "TAF RUNNING SUCCESSFULLY WITH exitCode= " + TestRun.exitCode);
        else
            System.out.println(System.lineSeparator() + "TAF RUNNING ERROR WITH exitCode= " + TestRun.exitCode);

        if(!testMode) System.exit(TestRun.exitCode);
    }

    private static void runDiagnosticTestsIfWanted(){
        String[] args = stringListToArray(remainingArguments);
        List<Class<?>> classes = new ArrayList<>();
        JUnitCore junit = new JUnitCore();
        TestRun.reporters.addTestRunReporterIfNotAlreadyRegistered(new TestRunReporterHtmlSummaryReportFile());
        junit.addListener(new TafRunListener());

        for (String arg : args) {
            if (arg.toLowerCase().equals("diagnostic") || arg.toLowerCase().equals("diagnostics")) {
                System.out.println("Running diagnostic tests. This might take a few minutes while strange things might occur on the screen. Don't be alarmed. Be patient.");
                List<Thread> threads = new ArrayList<>();

                //Running tests in separate thread to enable running a dot-printer to the command line to show progress. Not yet implemented.
                DiagnosticsRun diag = new DiagnosticsRun(junit);
                Thread diagnosticsRunThread = new Thread(diag);
                threads.add(diagnosticsRunThread);
                diagnosticsRunThread.start();

                //Waiting for threads to finish
                for (Thread thread : threads) {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {

                    }
                }

                if (!diag.getResult().getFailures().isEmpty()) {
                    TestRun.exitCode = TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue();
                }
                remainingArguments.remove(arg);
                exitWithExitCode();
                return;
            }
        }
    }

    private static Result runTestClasses(){
        String[] args = stringListToArray(remainingArguments);
        List<Class<?>> classes = new ArrayList<>();
        JUnitCore junit = new JUnitCore();
        TestRun.reporters.addTestRunReporterIfNotAlreadyRegistered(new TestRunReporterHtmlSummaryReportFile());
        junit.addListener(new TafRunListener());

        for (String arg : args) {
            try {
                classes.add(Class.forName(arg));
                System.out.println("Found test class '" + arg + "'.");
                remainingArguments.remove(arg);
            } catch (ClassNotFoundException e) {
                System.out.println(System.lineSeparator() + "WARNING: Expecting argument '" + arg +
                        "' to be a class containing tests, but no such class could be found. " +
                        "Are you sure you used a correct package path to the test class?");
                remainingArguments.remove(arg);
            }
        }

        if(classes.size() == 0) {
            System.out.println(System.lineSeparator() + "No test classes given for execution." + System.lineSeparator()+ System.lineSeparator() + "If in doubt of how to use this command line interface, please try the help switch or the Wiki.");
            return null;
        }
        Result result = junit.run(classes.toArray(new Class[0]));
        return result;
    }

    private static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printErrorMessageUponWrongJavaVersion(){
        if( Utils.getInstance().checkSupportedJavaVersionForTAF() ) return;
        System.out.println( "Running java version (" + Taf.tafUserInfon().getJavaVersion() + ") is not supported for TAF. Please use Java " + Utils.getInstance().SUPPORTED_TAF_JVM_VERSION + " or newer version!" );
        System.out.println(System.lineSeparator() + helpText());
        TestRun.exitCode = TestRun.ExitCodeTable.RUN_TAF_ERROR_FATAL.getValue();
        exitWithExitCode();
    }

    private static String[] stringListToArray(List<String> strings){
        String[] args = new String[strings.size()];
        for(int i = 0; i < strings.size(); i++){
            args[i] = strings.get(i);
        }
        return args;
    }

    private static List<String> stringArrayToList(String[] args){
        List<String> returnList = new ArrayList<>();
        for(String arg: args){
            returnList.add(arg);
        }
        return returnList;
    }

    static void runInTestMode(String[] args){
        testMode = true;
        executeRunSequence(args);
    }

    private static void executeRunSequence(String[] args){
        System.out.println(System.lineSeparator() + "Executing TAF (TestAutomationFramework) from CLI." + System.lineSeparator());
        remainingArguments = stringArrayToList(args);
        printErrorMessageUponWrongJavaVersion();// Exits at the end. No need to remove arguments from argument array for not being test classes
        printHelpTextIfApplicable();
        System.out.println("Argument(s) given:" + System.lineSeparator()  +
                " * " + String.join("" + System.lineSeparator() + " * ", args) + System.lineSeparator() + System.lineSeparator() +
                "Interpreting arguments.");
        TestRun.initializeIfNotInitialized(); // No need to remove arguments from argument array for not being test classes
        setRunSettingsFileIfGivenAsArgument();
        setRunNameIfGivenAsArgument();
        setRunSettingsParametersGivenAsArguments();
        runDiagnosticTestsIfWanted();
        runTestClasses();
        pause(1000);
        exitWithExitCode();
    }

    /**
     * Actual runner method
     * @param args arguments
     */
    public static void main(String [] args) {
        executeRunSequence(args);
    }
}
