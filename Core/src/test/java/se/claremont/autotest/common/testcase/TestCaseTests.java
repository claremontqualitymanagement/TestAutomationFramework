package se.claremont.autotest.common.testcase;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.logging.LogLevel;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TestCaseTests {

    @Test
    public void debugModeShouldUseStreamingOutput() {
        PrintStream original = System.out;
        ByteArrayOutputStream temp = new ByteArrayOutputStream();
        PrintStream tempOut = new PrintStream(temp);
        System.setOut(tempOut);
        TestCase testCase = new TestCase();
        testCase.setDebugMode();
        testCase.log(LogLevel.DEBUG, "RelevantText");
        System.out.flush();
        System.setOut(original);
        Assert.assertTrue(temp.toString(), temp.toString().contains("RelevantText"));
    }
}
