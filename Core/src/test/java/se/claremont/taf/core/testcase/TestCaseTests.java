package se.claremont.taf.core.testcase;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.logging.LogLevel;

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
