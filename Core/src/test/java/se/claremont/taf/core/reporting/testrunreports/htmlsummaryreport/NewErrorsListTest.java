package se.claremont.taf.core.reporting.testrunreports.htmlsummaryreport;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.support.SupportMethods;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testset.UnitTestClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2017-04-10.
 */
public class NewErrorsListTest extends UnitTestClass{


    @Test
    public void emptyNewErrorInfosShouldGenerateEmtpyString() throws Exception {
        List<NewError> newErrorInfos = new ArrayList<>();
        NewErrorsList newErrorsList = new NewErrorsList(newErrorInfos, new ArrayList<>());
        Assert.assertTrue(newErrorsList.toString().contentEquals(""));
    }

    @Test
    public void singleSharedNewErrorsShouldBePrinted(){
        List<NewError> newErrorInfos = new ArrayList<>();
        TestCase testCase1 = new TestCase();
        testCase1.log(LogLevel.VERIFICATION_FAILED, "No such data '123' in element Button1. Time duration 321 milliseconds.");
        newErrorInfos.add(new NewError(testCase1, testCase1.testCaseResult.testCaseLog.onlyErroneousLogPosts()));
        TestCase testCase2 = new TestCase();
        testCase2.log(LogLevel.VERIFICATION_FAILED, "No such data '456' in element Button1. Time duration 987 milliseconds.");
        newErrorInfos.add(new NewError(testCase2, testCase2.testCaseResult.testCaseLog.onlyErroneousLogPosts()));
        NewErrorsList newErrorsList = new NewErrorsList(newErrorInfos, new ArrayList<>());
        //String regexPattern = ".*Similar log records found in multiple test cases.*";
        String regexPattern = ".*Similar log records found in multiple test cases" +
                ".*Verification failed.*No such data .* in element Button1. Time duration .*milliseconds" +
                ".*Nameless test case" +
                ".*Log" +
                ".*Nameless test case" +
                ".*Log.*";
        Assert.assertTrue("Expected the output to be a match for the regular expression pattern:" + System.lineSeparator() + regexPattern + System.lineSeparator() + "But it was:" + System.lineSeparator() + newErrorsList.toString(), SupportMethods.isRegexMatch(newErrorsList.toString(), regexPattern));
    }

    @Test
    public void multipleSharedNewErrorsShouldBePrintedInOrder(){
        List<NewError> newErrorInfos = new ArrayList<>();
        TestCase testCase1 = new TestCase();
        testCase1.log(LogLevel.VERIFICATION_FAILED, "No such data '123' in element Button1. Time duration 321 milliseconds.");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Next error '123'");
        newErrorInfos.add(new NewError(testCase1, testCase1.testCaseResult.testCaseLog.onlyErroneousLogPosts()));
        TestCase testCase2 = new TestCase();
        testCase2.log(LogLevel.VERIFICATION_FAILED, "No such data '456' in element Button1. Time duration 987 milliseconds.");
        testCase2.log(LogLevel.VERIFICATION_FAILED, "Next error '123'");
        newErrorInfos.add(new NewError(testCase2, testCase2.testCaseResult.testCaseLog.onlyErroneousLogPosts()));
        NewErrorsList newErrorsList = new NewErrorsList(newErrorInfos, new ArrayList<>());
        String output = newErrorsList.toString();
        System.out.println(output);
        //String regexPattern = ".*Similar log records found in multiple test cases.*";
        String regexPattern = ".*Similar log records found in multiple test cases" +
                ".*Verification failed.*No such data .* in element Button1. Time duration .*milliseconds" +
                ".*Next" +
                ".*Nameless test case.*Log" +
                ".*Nameless test case.*Log.*";
        Assert.assertTrue("Expected the output to be a match for the regular expression pattern:" + System.lineSeparator() + regexPattern + System.lineSeparator() + "But it was:" + System.lineSeparator() + output, SupportMethods.isRegexMatch(output, regexPattern));
        Assert.assertFalse(output.contains("Log extracts for test cases with unique problems"));
    }

    @Test
    public void testCasesWithExtraProblemsShouldBePrintedSeparately(){
        List<NewError> newErrorInfos = new ArrayList<>();
        TestCase testCase1 = new TestCase(null, "Test1");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "No such data '123' in element Button1. Time duration 321 milliseconds.");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Next error '123'");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "My own error");
        newErrorInfos.add(new NewError(testCase1, testCase1.testCaseResult.testCaseLog.onlyErroneousLogPosts()));
        TestCase testCase2 = new TestCase(null, "Test2");
        testCase2.log(LogLevel.VERIFICATION_FAILED, "No such data '456' in element Button1. Time duration 987 milliseconds.");
        testCase2.log(LogLevel.VERIFICATION_FAILED, "Next error '123'");
        newErrorInfos.add(new NewError(testCase2, testCase2.testCaseResult.testCaseLog.onlyErroneousLogPosts()));
        NewErrorsList newErrorsList = new NewErrorsList(newErrorInfos, new ArrayList<>());
        String output = newErrorsList.toString();
        System.out.println(output);
        //String regexPattern = ".*Similar log records found in multiple test cases.*";
        String regexPattern = ".*Similar log records found in multiple test cases.*No such data .* in element Button1.*Time duration .*milliseconds.*Next error.*Test1.*Test2.*Test case also has problematic log records not part of shared log row.*Log extracts for failed test cases with unique problems.*My own error.*Test1.*";
        Assert.assertTrue("Expected the output to be a match for the regular expression pattern:" + System.lineSeparator() + regexPattern + System.lineSeparator() + "But it was:" + System.lineSeparator() + output, SupportMethods.isRegexMatch(output, regexPattern));
    }

    @Test
    public void testCasesWithExtraProblemsShouldBePrintedSeparatelyWithTotallyUnSharedTestCaseInfo(){
        List<NewError> newErrorInfos = new ArrayList<>();
        TestCase testCase1 = new TestCase(null, "Test1");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "No such data '123' in element Button1. Time duration 321 milliseconds.");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Next error '123'");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "My own error");
        newErrorInfos.add(new NewError(testCase1, testCase1.testCaseResult.testCaseLog.onlyErroneousLogPosts()));
        TestCase testCase3 = new TestCase(null, "Test3");
        testCase3.log(LogLevel.VERIFICATION_FAILED, "Totally unique test case error1");
        testCase3.log(LogLevel.VERIFICATION_FAILED, "Totally unique test case error2");
        testCase3.log(LogLevel.VERIFICATION_FAILED, "Totally unique test case error3");
        newErrorInfos.add(new NewError(testCase3, testCase3.testCaseResult.testCaseLog.onlyErroneousLogPosts()));
        TestCase testCase2 = new TestCase(null, "Test2");
        testCase2.log(LogLevel.VERIFICATION_FAILED, "No such data '456' in element Button1. Time duration 987 milliseconds.");
        testCase2.log(LogLevel.VERIFICATION_FAILED, "Next error '123'");
        newErrorInfos.add(new NewError(testCase2, testCase2.testCaseResult.testCaseLog.onlyErroneousLogPosts()));
        NewErrorsList newErrorsList = new NewErrorsList(newErrorInfos, new ArrayList<>());
        String output = newErrorsList.toString();
        System.out.println(output);
        //String regexPattern = ".*Similar log records found in multiple test cases.*";
        String regexPattern = ".*Similar log records found in multiple test cases" +
                ".*No such data .* in element Button1.*Time duration .*milliseconds" +
                ".*Next error " +
                ".*Test1" +
                ".*Test2" +
                ".*Test case also has problematic log records not part of shared log row" +
                ".*Log extracts for failed test cases with unique problems" +
                ".*My own error" +
                ".*Test1.*";
        Assert.assertTrue("Expected the output to be a match for the regular expression pattern:" + System.lineSeparator() + regexPattern + System.lineSeparator() + "But it was:" + System.lineSeparator() + output, SupportMethods.isRegexMatch(output, regexPattern));
    }
}