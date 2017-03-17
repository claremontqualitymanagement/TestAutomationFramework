package se.claremont.autotest.common.testset;

import org.junit.*;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Used to get limited output from unit test runs.
 *
 * Created by jordam on 2017-03-17.
 */
public class UnitTestClass {
    static PrintStream originalOutputChannel;
    static ByteArrayOutputStream testOutputChannel;

    @Rule
    public TestWatcher watchman = new TestWatcher() {

        @Override
        protected void failed(Throwable e, Description description) {
            restoreOutputChannel();
            System.out.print("Result: Not OK. Output:");
            System.out.print(testOutputChannel.toString() + System.lineSeparator());
        }

        @Override
        protected void succeeded(Description description) {
            restoreOutputChannel();
            System.out.print("Result: OK.");
        }

        @Override
        protected void skipped(org.junit.AssumptionViolatedException e, Description description) {
            if(e.toString().length() > 0){
                System.out.print("Result: Test ignored. Assumption validation: " + e.toString());
            } else {
                System.out.print("Result: Test ignored.");
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

    private static void restoreOutputChannel(){
        System.setOut(originalOutputChannel);
    }

    private void redirectOutputToNewTestStream(){
        testOutputChannel = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOutputChannel));
    }

}
