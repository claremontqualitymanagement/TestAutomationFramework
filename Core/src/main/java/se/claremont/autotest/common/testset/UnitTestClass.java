package se.claremont.autotest.common.testset;

import org.junit.*;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Used to get limited output from unit test runs.
 *
 * Created by jordam on 2017-03-17.
 */
@RunWith(se.claremont.autotest.common.testrun.TafTestRunner.class)
public class UnitTestClass {
    @SuppressWarnings("WeakerAccess")
    static PrintStream originalOutputChannel;
    @SuppressWarnings("WeakerAccess")
    static ByteArrayOutputStream testOutputChannel;

    @Rule
    public TestWatcher watchman = new TestWatcher() {

        @Override
        protected void failed(Throwable e, Description description) {
            restoreOutputChannel();
            System.out.println("Result: Failed.");
            System.out.println("  > Failed test '" + description.getTestClass().getName() + "." + description.getMethodName() + "'.");
            if(testOutputChannel != null && testOutputChannel.toString().length() > 0){
                System.out.print("  > Output from test run:" + System.lineSeparator() + testOutputFormattedForDisplay() + System.lineSeparator());
            }
            if(e != null && e.toString().length() > 0){
                System.out.print("  > Error: " + e.toString() + System.lineSeparator());
            }
        }

        @Override
        protected void succeeded(Description description) {
            restoreOutputChannel();
            System.out.print("Result: OK.");
        }

        @Override
        protected void skipped(org.junit.AssumptionViolatedException e, Description description) {
            if(e.getMessage().length() > 0){
                System.out.print("Result: Test ignored. Assumption validation: " + e.getMessage());
            } else {
                System.out.print("Result: Test ignored. Assumptions on pre-requisites for execution not met.");
            }
        }

        @Override
        protected void finished(Description description) {
            System.out.print(System.lineSeparator());
        }

    };

    @Rule
    public TestName currentTestNameInternal = new TestName();

    @BeforeClass
    public static void rememberOriginalOutputChannel(){
        originalOutputChannel = System.out;
    }

    @Before
    public void startUp(){
        restoreOutputChannel();
        System.out.print("Execution of '" + currentTestNameInternal.getMethodName() + "'. ");
        //System.out.print("Running test '" + this.getClass().getName() + "." + currentTestNameInternal.getMethodName() + "'. ");
        redirectOutputToNewTestStream();
    }

    @After
    public void restoreOutPutChannelAfterTest(){
        restoreOutputChannel();
    }

    @AfterClass
    public static void restore(){
        restoreOutputChannel();
    }

    /**
     * Test output from unit tests are generally suppressed, but for verification purposes the test output can be retrieved by this method.
     *
     * @return Returns the suppressed console output from the test.
     */
    public String testOutput(){
        if(testOutputChannel == null) return null;
        return testOutputChannel.toString();
    }

    /**
     * Returns true if the given phrase is found in the console output from the test.
     *
     * @param stringToFind The string to find in the console output from the test.
     * @return Returns true if the given string is found in the console output from the test.
     */
    public boolean testOutputContains(String stringToFind) {
        return testOutputChannel != null && testOutputChannel.toString().contains(stringToFind);
    }

    private static void restoreOutputChannel(){
        System.setOut(originalOutputChannel);
    }

    private void redirectOutputToNewTestStream(){
        testOutputChannel = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOutputChannel));
    }

    private String testOutputFormattedForDisplay(){
        String[] rows = testOutputChannel.toString().split(System.lineSeparator());
        String indentation = "  > ";
        return indentation + String.join(System.lineSeparator() + indentation, rows) + System.lineSeparator();
    }


}
