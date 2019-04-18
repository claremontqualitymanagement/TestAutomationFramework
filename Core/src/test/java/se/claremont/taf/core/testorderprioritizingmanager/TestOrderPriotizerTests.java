package se.claremont.taf.core.testorderprioritizingmanager;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.testset.UnitTestClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TestOrderPriotizerTests extends UnitTestClass {

    @Test
    public void testsExecutedLaterShouldBeSortedLater() throws ParseException {
        TestCaseResults testCaseResults = new TestCaseResults();

        TestCaseResult testCaseResult = new TestCaseResult();
        testCaseResult.executionTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-04-20 00:01:00");
        testCaseResult.testOutcome = TestOutcome.PASS;
        testCaseResult.testName = "Test1";
        testCaseResults.add(testCaseResult);

        TestCaseResult testCaseResult2 = new TestCaseResult();
        testCaseResult2.executionTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-04-20 00:00:00");
        testCaseResult2.testOutcome = TestOutcome.PASS;
        testCaseResult2.testName = "Test2";
        testCaseResults.add(testCaseResult2);

        TestOrderPrioritizer testOrderPrioritizer = new TestOrderPrioritizer();
        testOrderPrioritizer.testCaseResultsList = testCaseResults;
        Assert.assertTrue(String.join("", testOrderPrioritizer.getRecomendedExecutionOrder()).equals("Test2Test1"));
    }

    @Test
    public void sameTestNameShouldWork() throws ParseException {
        TestCaseResults testCaseResults = new TestCaseResults();

        TestCaseResult testCaseResult = new TestCaseResult();
        testCaseResult.executionTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-04-20 00:01:00");
        testCaseResult.testOutcome = TestOutcome.PASS;
        testCaseResult.testName = "Test1";
        testCaseResults.add(testCaseResult);

        TestCaseResult testCaseResult2 = new TestCaseResult();
        testCaseResult2.executionTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-04-20 00:00:00");
        testCaseResult2.testOutcome = TestOutcome.PASS;
        testCaseResult2.testName = "Test2";
        testCaseResults.add(testCaseResult2);

        TestCaseResult testCaseResult3 = new TestCaseResult();
        testCaseResult3.executionTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-04-20 00:02:00");
        testCaseResult3.testOutcome = TestOutcome.PASS;
        testCaseResult3.testName = "Test2";
        testCaseResults.add(testCaseResult3);

        TestOrderPrioritizer testOrderPrioritizer = new TestOrderPrioritizer();
        testOrderPrioritizer.testCaseResultsList = testCaseResults;
        Assert.assertTrue(String.join("", testOrderPrioritizer.getRecomendedExecutionOrder()).equals("Test1Test2"));
    }

    @Test
    public void erroredTestNameShouldBeFirst() throws ParseException {
        TestCaseResults testCaseResults = new TestCaseResults();

        TestCaseResult testCaseResult = new TestCaseResult();
        testCaseResult.executionTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-04-20 00:01:00");
        testCaseResult.testOutcome = TestOutcome.PASS;
        testCaseResult.testName = "Test1";
        testCaseResults.add(testCaseResult);

        TestCaseResult testCaseResult2 = new TestCaseResult();
        testCaseResult2.executionTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-04-20 00:00:00");
        testCaseResult2.testOutcome = TestOutcome.FAIL;
        testCaseResult2.testName = "Test2";
        testCaseResults.add(testCaseResult2);

        TestCaseResult testCaseResult3 = new TestCaseResult();
        testCaseResult3.executionTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2017-04-20 00:02:00");
        testCaseResult3.testOutcome = TestOutcome.PASS;
        testCaseResult3.testName = "Test1";
        testCaseResults.add(testCaseResult3);

        TestOrderPrioritizer testOrderPrioritizer = new TestOrderPrioritizer();
        testOrderPrioritizer.testCaseResultsList = testCaseResults;
        Assert.assertTrue(String.join("", testOrderPrioritizer.getRecomendedExecutionOrder()).equals("Test2Test1"));
    }

}
