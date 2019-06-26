package se.claremont.taf.core.testrun;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import se.claremont.taf.core.gui.Gui;
import se.claremont.taf.core.junitcustomization.TafResult;
import se.claremont.taf.core.junitcustomization.TafTestRunner;
import se.claremont.taf.core.reporting.testrunreports.TestRunReporterHtmlSummaryReportFile;
import se.claremont.taf.core.support.SupportMethods;
import se.claremont.taf.core.support.Utils;
import se.claremont.taf.core.support.api.Taf;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * CLI runner class for the test automation framework.
 * Used to set up execution environment and start selected
 * tests from the command prompt.
 */
@SuppressWarnings("WeakerAccess")
public class CliTestRunner {

    private static List<String> remainingArguments ;
    private static boolean testMode = false;

    /**
     * Actual runner method setting up test settings and executing tests.
     * @param args arguments
     */
    public static void main(String [] args) {
        if(shouldShowGui(args)){
            Gui gui = new Gui();
            gui.activate();
        } else {
            executeRunSequence(args);
        }
    }

    /**
     * Used to enable unit testing and system testing ot the TAF framework - essentially
     * preventing the use of ExitCode at the end of the test run, thus making it possible
     * to evaluate the state at the end of a test run.
     *
     * @param args CLI run arguments to be passed to normal runner.
     * @param testClasses Classes to run during the test.
     * @return Returns the exit code that the test run would exit with.
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static int runInTestMode(String[] args, Class<?>[] testClasses){
        testMode = true;
        List<String> arguments = new ArrayList<>();
        Collections.addAll(arguments, args);
        if(testClasses != null){
            logLoadedClasses();
            for(Class<?> klass : testClasses){
                arguments.add((klass.getName()));
                try {
                    Class.forName(klass.getName());
                } catch (ClassNotFoundException e) {
                    System.out.println(e.toString());
                }
            }
        }
        executeRunSequence(arguments.toArray(new String[0]));
        return TestRun.getExitCode();
    }

    /**
     * Used to enable unit testing and system testing ot the TAF framework - essentially
     * preventing the use of ExitCode at the end of the test run, thus making it possible
     * to evaluate the state at the end of a test run.
     *
     * @param args CLI run arguments to be passed to normal runner.
     * @return Returns the exit code that the test run would exit with.
     */
    public static int runInTestMode(String[] args){
        return runInTestMode(args, (Class<?>[])null);
    }

    /**
     * Used to enable unit testing and system testing ot the TAF framework - essentially
     * preventing the use of ExitCode at the end of the test run, thus making it possible
     * to evaluate the state at the end of a test run.
     *
     * @param args CLI run arguments to be passed to normal runner.
     * @param testClass Class to run during the test.
     * @return Returns the exit code that the test run would exit with.
     */
    public static int runInTestMode(String[] args, Class<?> testClass)  {
        return runInTestMode(args, new Class<?>[]{testClass});
    }

    public static void executeRunSequence(String[] args){
        System.out.println(System.lineSeparator() + "Executing TAF (TestAutomationFramework) from CLI." + System.lineSeparator());
        remainingArguments = stringArrayToList(args);
        printErrorMessageUponWrongJavaVersion();// Exits at the end. No need to remove arguments from argument array for not being test classes
        setSystemPropertiesIfStatedWithMinusD();
        printCurrentVersionUponVersionArgument();
        printHelpTextIfApplicable();
        System.out.println("Argument(s) given:" + System.lineSeparator()  +
                " * " + String.join("" + System.lineSeparator() + " * ", args) + System.lineSeparator() + System.lineSeparator() +
                "Interpreting arguments.");
        setRunSettingsFileIfGivenAsArgument();
        setRunNameIfGivenAsArgument();
        setRunSettingsParametersGivenAsArguments();
        runDiagnosticTestsIfWanted();
        runTestClasses();
        pause(1000);
        exitWithExitCode();
    }

    private static void printCurrentVersionUponVersionArgument() {
        String[] args = stringListToArray(remainingArguments);
        for(String arg : args){
            if(arg.trim().length() > 0 && arg.trim().length() < "version".length() + 2 && arg.trim().toLowerCase().endsWith("version")){
                    CliTestRunner runner = new CliTestRunner();
                    System.out.println("Current version of TAF: " + runner.getClass().getPackage().getImplementationVersion());
                    remainingArguments.remove(arg);
            }
        }
    }

    private static boolean shouldShowGui(String[] args){
        if(!Desktop.isDesktopSupported()) return false;
        if(args == null || args.length == 0) return true;
        return false;
    }

    private static final String LF = SupportMethods.LF;

    @SuppressWarnings("SpellCheckingInspection")
    private static String helpText() {
        return "Usage instructions: " +
                LF + LF +
                "java.exe -jar TafFull.jar [help] git[version] [diagnostic] [parallel_test_execution_mode=none] [run_settings_alterations...] [RunName=name] [System_property_alterations...] [com.company.packagepath.TestClassName...]" +
                LF + LF +
                "This syntax is explained below. If no arguments are given a short help text pointing to this help text is displayed." +
                LF + LF +
                "Tell what test classes to run" + LF +
                "-----------------------------" + LF +
                "Any program argument not falling into the categories below will be interpreted as a name of a class containing JUnit tests." + LF +
                "Test classes must be stated with their full package path, but without the .class file extension." +
                LF + LF +
                "If no classes containing tests are stated no tests will run." + LF + LF +
                "Diagnostic run" + LF +
                "--------------" + LF +
                "Command line options consists of " +
                "listed test classes to be run, or the keyword 'diagnostics' to run the unit tests and " +
                "the diagnostics tests to ensure the local installation is ok. A diagnostic run output results from failed tests as debug information." +
                LF + LF +
                "Setting a test run name" + LF +
                "-----------------------" + LF +
                "A test run name can be set using the argument runName" + LF +
                "This test run name will be used for log folder name creation." + LF +
                "Example: RunName=MyTestRun1stOfSeptember" + LF +
                LF + LF +
                "Default run name is based on class names and a time stamp." +
                LF + LF +
                "Running tests in parallel" + LF +
                "-------------------------" + LF +
                "Tests are normally run i sequence, but sometimes you want them to run in parallel. TAF allows a few " +
                "different methods for doing this by using the argument PARALLEL_TEST_EXECUTION_MODE. Examples:" + LF +
                "PARALLEL_TEST_EXECUTION_MODE=methods will run test methods in classes in parallel." + LF +
                "PARALLEL_TEST_EXECUTION_MODE=classes will run test classes in parallel." + LF +
                "PARALLEL_TEST_EXECUTION_MODE=none will run test methods in classes in sequence." + LF +
                "PARALLEL_TEST_EXECUTION_MODE=false is same as PARALLEL_TEST_EXECUTION_MODE=none." + LF +
                "PARALLEL_TEST_EXECUTION_MODE=true is same as PARALLEL_TEST_EXECUTION_MODE=methods." + LF +
                "PARALLEL_TEST_EXECUTION_MODE=4 is executing tests in a thread pool with 4 threads." +
                LF + LF +
                "Using a test run properties file" + LF +
                "--------------------------------" + LF +
                "If you want to initiate a test run it sometimes can be convenient to just point to a prepared runSettings file with properties. This could for example be very useful when running your tests in a CI/CD environment. You can do this by providing the argument 'settingsfile' or 'runsettingsfile'. E.g.:" + LF +
                LF + LF +
                " java -jar MyTestProject.jar runSettingsFile=C:\\temp\\runSettings.properties com.organization.testproject.MyTestClass1 com.organization.testproject.MyTestClass2" +
                LF + LF +
                "The line above will use the settings parameters in the C:\\Temp\\runSettings.properties file to execute the tests in the classes MyTestClass1 and MyTestClass2 found in the package 'com.organization.testproject'." +
                "If test classes are listed as arguments the output of those tests are displayed. This output can be quite extensive, and sometimes it is beneficial to make sure you can read it all." +
                LF + LF +
                "If no test run properties file is given, one will be created in the test report base folder if no file already exist there." + LF + LF +
                "Test report folder" + LF +
                "------------------" + LF +
                "Test output from test classes extending the TestSet class is saved to the output log folder." + LF + LF +
                "Test output folder is controlled via a run settings parameter called BASE_LOG_FOLDER. You can set this as any other run setting. See below." +
                LF + LF +
                "Default folder is a folder called TAF in the active user's user folder." +
                LF + LF +
                "Setting test run settings" + LF +
                "-------------------------" + LF +
                "Settings from file can be overwritten by stating them as arguments using equal sign in between parameter name and parameter value:" + LF +
                "emailRecipients=firstName.lastName@organization.com" + LF + LF +
                "If the parameter name corresponds to a test run setting variable name this variable will be given the value stated. If not a custom run settings variable will be created with this value." +
                LF + LF +
                "Setting system properties" + LF +
                "-------------------------" + LF +
                "By using regular -D java notation you can set system properties as arguments to execution. Example:" +
                LF + LF +
                "java -jar Tests.jar -Dtestenvironment=dev" +
                LF + LF +
                "The line above will set the system property 'testenvironment' to 'dev'." +
                LF + LF +
                "Test run exit value" + LF +
                "-------------------" + LF +
                "To be useful with build engines, like for example Jenkins or Team City, a successful test run will return an exit code of '0' (zero). The exact exit code for failed test runs will depend on the run result status." +
                LF + LF +
                "Viewing this help text" + LF +
                "----------------------" + LF +
                "This help text is displayed by giving any of the arguments:" + LF +
                "   help" + LF +
                "   man" + LF +
                "   -h" + LF +
                "   -man" + LF +
                "   --man" + LF +
                "   -help" + LF +
                "   --help" +
                LF + LF;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static void setRunSettingsFileIfGivenAsArgument(){
        String[] args = stringListToArray(remainingArguments);
        for(String arg : args){
            if(arg.trim().length() > 0 && arg.contains("=")){
                String[] parts = arg.split("=");
                if((parts[0].trim().equalsIgnoreCase("settingsfile") || parts[0].trim().equalsIgnoreCase("runsettingsfile")) && parts.length > 1) {
                    TestRun.getInstance().settings = new Settings(arg.trim().substring(arg.indexOf("=") + 1).trim());
                    System.out.println("Run settings properties file = '" + parts[1].trim() + "'.");
                    remainingArguments.remove(arg);
                }
            }
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static void setRunNameIfGivenAsArgument(){
        String[] args = stringListToArray(remainingArguments);
        for(String arg : args){
            if(arg.trim().length() > 0 && arg.contains("=")){
                String[] parts = arg.split("=");
                if(parts[0].trim().toLowerCase().equals("runname") && parts.length > 1) {
                    TestRun.getInstance().testRunName = arg.trim().substring(arg.indexOf("=") + 1).trim();
                    System.out.println("Run name is set to '" + parts[1].trim() + "'.");
                    remainingArguments.remove(arg);
                }
            }
        }
    }

    private static void setSystemPropertiesIfStatedWithMinusD() {
        String[] args = stringListToArray(remainingArguments);
        for(String arg : args){
            if(arg.trim().length() > 0 && arg.startsWith("-D") && arg.contains("=")){
                String[] parts = arg.split("=");
                if(parts[0].trim().length() > 0 && parts.length > 1) {
                    String parameterName = parts[0].trim().substring(2);
                    System.setProperty(parameterName, arg.trim().substring(arg.trim().indexOf("=") + 1).trim());
                    System.out.println("Setting system property '" + arg.trim().split("=")[0].trim().substring(2) + "' to value '" + arg.trim().substring(arg.trim().indexOf("=") + 1).trim() + "'.");
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
                    if(TestRun.getCustomSettingsValue(parameterName) != null
                            && TestRun.getCustomSettingsValue(parameterName).trim().length() > 0){
                        System.out.println("Updating test run settings parameter '" +
                                parameterName + "' from '" + TestRun.getCustomSettingsValue(parameterName) +
                                "' to '" + arg.trim().substring(arg.trim().indexOf("=") + 1).trim() + "'.");
                    } else {
                        System.out.println("Setting parameter '" + arg.trim().split("=")[0].trim() +
                                "' to value '" + arg.trim().substring(arg.trim().indexOf("=") + 1).trim() + "'.");
                    }
                    TestRun.setCustomSettingsValue(parameterName, arg.trim().substring(arg.trim().indexOf("=") + 1).trim());
                    remainingArguments.remove(arg);
                }
            }
        }
    }

    private static void printHelpTextIfApplicable(){
        String[] args = stringListToArray(remainingArguments);
        if (args.length == 0) {
            System.out.print(helpText());
            TestRun.setExitCode(TestRun.ExitCodeTable.INIT_OK.getValue());
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
                TestRun.setExitCode(TestRun.ExitCodeTable.INIT_OK.getValue());
                exitWithExitCode();
                return;
            }
        }
    }

    private static void exitWithExitCode(){
        if (TestRun.getExitCode() == TestRun.ExitCodeTable.INIT_OK.getValue())
            System.out.println(System.lineSeparator() + "TAF RUNNING SUCCESSFULLY WITH exitCode = " + TestRun.getExitCode());
        else
            System.out.println(System.lineSeparator() + "TAF RUNNING ERROR WITH exitCode = " + TestRun.getExitCode());

        if(testMode) {
            System.setProperty("TAF latest test run exit code", String.valueOf(TestRun.getExitCode()));
        } else {
            System.exit(TestRun.getExitCode());
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static void runDiagnosticTestsIfWanted(){
        String[] args = stringListToArray(remainingArguments);
        List<Class<?>> classes = new ArrayList<>();
        JUnitCore junit = new JUnitCore();
        //TestRun.reporters.addTestRunReporterIfNotAlreadyRegistered(new TestRunReporterHtmlSummaryReportFile());

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
                        System.out.println(e.toString());
                    }
                }

                if (!diag.getResult().getFailures().isEmpty()) {
                    TestRun.setExitCode(TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue());
                }
                remainingArguments.remove(arg);
                exitWithExitCode();
                return;
            }
        }
    }

    private static void runTestClasses(){
        String[] args = stringListToArray(remainingArguments);
        List<Class<?>> classes = new ArrayList<>();
        TestRun.getReporterFactory().addTestRunReporterIfNotAlreadyRegistered(new TestRunReporterHtmlSummaryReportFile());

        int testCount = 0;
        for (String arg : args) {
            try {
                Request r = Request.aClass(Class.forName(arg));
                if(r == null || r.getRunner() == null) {
                    System.out.println("No tests in class '" + arg + "' identified.");
                    return;
                }
                testCount += r.getRunner().testCount();
                if(r.getRunner().testCount() > 0){
                    System.out.println("Found test class '" + arg + "' containing " + r.getRunner().testCount() + " tests.");
                    classes.add(Class.forName(arg));
                    remainingArguments.remove(arg);
                } else {
                    System.out.println("WARNING: Class '" + Class.forName(arg) + "' does not seem to contain any tests. Not adding it.");
                    TestRun.setExitCode(TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue());
                }
            } catch (ClassNotFoundException e) {
                System.out.println(System.lineSeparator() + "WARNING: Expecting argument '" + arg +
                        "' to be a class containing tests, but no such class could be found. " +
                        "Are you sure you used a correct package path to the test class?");
                TestRun.setExitCode(TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue());
                remainingArguments.remove(arg);
            }
        }
        TafTestRunner tafTestRunner = new TafTestRunner();
        TafResult tafResult = tafTestRunner.run(classes);
        if(tafResult.getFailureCount() > 0){
            TestRun.setExitCode(TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue());
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printResults(TafResult result){
        if(result == null){
            System.out.println("Strange. No test result result found from test run.");
            return;
        }
        System.out.println("Ran " + result.getRunCount() + " test cases. Success: " + result.wasSuccessful());
        System.out.println("Successful: " + (result.getRunCount() - result.getFailureCount()));
        System.out.println("Ignored:    " + result.getIgnoreCount());
        System.out.println("Failed:     " + result.getFailureCount());
    }

    private static void printErrorMessageUponWrongJavaVersion(){
        if( Utils.getInstance().checkSupportedJavaVersionForTAF() ) return;
        System.out.println( "Running java version (" + Taf.tafUserInfon().getJavaVersion() + ") is not supported for TAF. Please use Java " + Utils.getInstance().SUPPORTED_TAF_JVM_VERSION + " or newer version!" );
        System.out.println(System.lineSeparator() + helpText());
        TestRun.setExitCode(TestRun.ExitCodeTable.RUN_TAF_ERROR_FATAL.getValue());
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
        Collections.addAll(returnList, args);
        int position = -1;
        for(int i = 0 ; i < returnList.size(); i++){
            if(returnList.get(i).equalsIgnoreCase("nogui")){
                position = i;
            }
        }
        if(position != -1){
            returnList.remove(position);
        }
        return returnList;
    }

    private static void logLoadedClasses() {
        ClassLoader myCL = Thread.currentThread().getContextClassLoader();
        while (myCL != null) {
            System.out.println("ClassLoader: " + myCL);
            try {
                for (Iterator iterator = list(myCL); iterator.hasNext();) {
                    System.out.println("\t" + iterator.next());
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            myCL = myCL.getParent();
        }
    }

    private static Object runInTestMode(String[] args, Object runAfter){
        testMode = true;
        executeRunSequence(args);
        return runAfter;
    }

    private static Iterator list(ClassLoader CL)
            throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        Class CL_class = CL.getClass();
        while (CL_class != java.lang.ClassLoader.class) {
            CL_class = CL_class.getSuperclass();
        }
        java.lang.reflect.Field ClassLoader_classes_field = CL_class
                .getDeclaredField("classes");
        ClassLoader_classes_field.setAccessible(true);
        Vector classes = (Vector) ClassLoader_classes_field.get(CL);
        return classes.iterator();
    }


}
