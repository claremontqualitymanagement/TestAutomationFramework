package se.claremont.taf.core.testcase;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.logging.LogPost;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Tests for the TestCaseLog class
 *
 * Created by jordam on 2016-08-30.
 */
public class TestCaseLog_Tests extends UnitTestClass{

    @Test
    public void logInstantiationNotNull(){
        TestCaseLog testCaseLog = new TestCaseLog("logInstantiationNotNull");
        Assert.assertNotNull("New TestCaseLog() was null", testCaseLog.logPosts);
    }

    @Test
    public void logInstantiationLogEmpty(){
        TestCaseLog testCaseLog = new TestCaseLog("logInstantiationLogEmpty");
        Assert.assertTrue("New TestCaseLog() has no attached testCaseLog list.", testCaseLog.logPosts.size() == 0);
    }

    @Test
    public void logging(){
        TestCaseLog testCaseLog = new TestCaseLog("logging");
        testCaseLog.log(LogLevel.DEBUG, "Testing");
        Assert.assertTrue("New TestCaseLog() didn't have logged item.", testCaseLog.logPosts.size() == 1);
        for(LogPost logPost : testCaseLog.logPosts){
            Assert.assertTrue("Correct testCaseLog post couldn't be matched.", logPost.toString().contains("Testing"));
        }
    }

    @Test
    public void loggingAllLogLevels(){
        TestCaseLog testCaseLog = new TestCaseLog("loggingAllLogLevels");
        try {
            testCaseLog.log(LogLevel.DEBUG, "Testing DEBUG");
            testCaseLog.log(LogLevel.VERIFICATION_PASSED, "Testing DEBUG");
            testCaseLog.log(LogLevel.VERIFICATION_FAILED, "Testing DEBUG");
            testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Testing DEBUG");
            testCaseLog.log(LogLevel.DEVIATION_EXTRA_INFO, "Testing DEBUG");
            testCaseLog.log(LogLevel.EXECUTED, "Testing DEBUG");
            testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "Testing DEBUG");
            testCaseLog.log(LogLevel.INFO, "Testing DEBUG");
            testCaseLog.log(LogLevel.FRAMEWORK_ERROR, "Testing DEBUG");

        } catch (Exception e){
            //noinspection ConstantConditions
            Assert.assertTrue("Couldn't register some LogLevel testCaseLog post.", false);
        }
        Assert.assertTrue("New TestCaseLog() didn't have all logged items. Found " + testCaseLog.logPosts.size(), testCaseLog.logPosts.size() == 13);
    }

    @Test
    public void logHasEncounteredErrorTest(){
        TestCaseLog testCaseLog = new TestCaseLog("logHasEncounteredErrorTest");
        try {
            testCaseLog.log(LogLevel.DEBUG, "Testing DEBUG");
            testCaseLog.log(LogLevel.VERIFICATION_PASSED, "Testing DEBUG");
            testCaseLog.log(LogLevel.VERIFICATION_FAILED, "Testing DEBUG");
            testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Testing DEBUG");
            testCaseLog.log(LogLevel.DEVIATION_EXTRA_INFO, "Testing DEBUG");
            testCaseLog.log(LogLevel.EXECUTED, "Testing DEBUG");
            testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "Testing DEBUG");
            testCaseLog.log(LogLevel.INFO, "Testing DEBUG");
            testCaseLog.log(LogLevel.FRAMEWORK_ERROR, "Testing DEBUG");

        } catch (Exception e){
            //noinspection ConstantConditions
            Assert.assertTrue("Couldn't register some LogLevel testCaseLog post.", false);
        }
        Assert.assertTrue("New TestCaseLog() didn't reportTestRun that it had encountered errors when it actually had errors.", testCaseLog.hasEncounteredErrors());
    }

    @Test
    public void logOnlyErroneousLogPosts(){
        TestCaseLog testCaseLog = new TestCaseLog("logOnlyErroneousLogPosts");
        try {
            testCaseLog.log(LogLevel.DEBUG, "Testing DEBUG");
            testCaseLog.log(LogLevel.VERIFICATION_PASSED, "Testing DEBUG");
            testCaseLog.log(LogLevel.VERIFICATION_FAILED, "Testing DEBUG");
            testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Testing DEBUG");
            testCaseLog.log(LogLevel.DEVIATION_EXTRA_INFO, "Testing DEBUG");
            testCaseLog.log(LogLevel.EXECUTED, "Testing DEBUG");
            testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "Testing DEBUG");
            testCaseLog.log(LogLevel.INFO, "Testing DEBUG");
            testCaseLog.log(LogLevel.FRAMEWORK_ERROR, "Testing DEBUG");

        } catch (Exception e){
            //noinspection ConstantConditions
            Assert.assertTrue("Couldn't register some LogLevel testCaseLog post.", false);
        }
        Assert.assertTrue("New TestCaseLog() didn't have all logged items.", testCaseLog.onlyErroneousLogPosts().size() == 4);
    }

    @Test
    public void logDifferentlyToTextAndHtmlTestLogging(){
        TestCaseLog testCaseLog = new TestCaseLog("logDifferentlyToTextAndHtmlTestLogging");
        testCaseLog.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Testing", "<p><b>TestingHtml</b></p>");
        Assert.assertTrue("Logging html and text at the same time resulted in more than one testCaseLog post.", testCaseLog.logPosts.size() == 1);
    }

    @Test
    public void logDifferentlyToTextAndHtmlHtmlRepresentation(){
        TestCaseLog testCaseLog = new TestCaseLog("logDifferentlyToTextAndHtmlHtmlRepresentation");
        testCaseLog.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Testing", "<p><b>TestingHtml</b></p>");
        Assert.assertTrue("Wrong text reported as html. Found to be '" + testCaseLog.logPosts.get(0).toHtmlTableRow() + "'.", testCaseLog.logPosts.get(0).toHtmlTableRow().contains("<p><b>TestingHtml</b></p>"));
    }

    @Test
    public void logDifferentlyToTextAndHtmlTextRepresentation(){
        TestCaseLog testCaseLog = new TestCaseLog("logDifferentlyToTextAndHtmlTextRepresentation");
        testCaseLog.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "ABCD", "<p><b>TestingHtml</b></p>");
        Assert.assertFalse("Wrong text reported as text.", testCaseLog.logPosts.get(0).toString().contains("<p><b>TestingHtml</b></p>"));
        Assert.assertTrue("Wrong", testCaseLog.logPosts.get(0).toString().contains("ABCD"));
    }

    @Test
    public void logToStringBothHtmlAndString(){
        TestCaseLog testCaseLog = new TestCaseLog("logToStringBothHtmlAndString");
        testCaseLog.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "ABCD", "<p><b>TestingHtml</b></p>");
        Assert.assertTrue("Wrong", testCaseLog.logPosts.get(0).toString().contains("ABCD"));
        Assert.assertTrue("Wrong", testCaseLog.logPosts.get(0).toString().toLowerCase().contains("executed"));
    }

    @Test
    public void logToString(){
        TestCaseLog testCaseLog = new TestCaseLog("logToString");
        testCaseLog.log(LogLevel.DEVIATION_EXTRA_INFO, "EFG");
        Assert.assertTrue("Wrong.", testCaseLog.logPosts.get(0).toString().contains("EFG"));
        Assert.assertTrue("Wrong", testCaseLog.logPosts.get(0).toString().toLowerCase().contains("deviation extra info"));
    }

    @Test
    public void toJson(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        System.out.print(testCaseLog.toJson());
        Assert.assertTrue(testCaseLog.toJson(), testCaseLog.toJson().contains("\"logPosts\""));
    }

    @Test
    public void methodFirstErroneousLogPostShouldReturnFirstError(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        testCaseLog.log(LogLevel.DEBUG, "Debug");
        testCaseLog.log(LogLevel.EXECUTED, "Executed");
        testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "Execution problem");
        testCaseLog.log(LogLevel.DEVIATION_EXTRA_INFO, "Deviation extra info");
        testCaseLog.log(LogLevel.FRAMEWORK_ERROR, "Framework error");
        testCaseLog.log(LogLevel.INFO, "Info");
        Assert.assertTrue(testCaseLog.firstNonSuccessfulLogPost().message.equals("Execution problem"));
    }

    @Test
    public void methodFirstErroneousLogPostShouldReturnNullOnEmpty(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        Assert.assertTrue(testCaseLog.firstNonSuccessfulLogPost() == null);
    }

    @Test
    public void methodFirstErroneousLogPostShouldReturnNullOnNoErrors(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        testCaseLog.log(LogLevel.DEBUG, "Debug");
        Assert.assertTrue(testCaseLog.firstNonSuccessfulLogPost() == null);
    }

    @Test
    public void methodHasEncounteredErrorShouldReturnFalseOnEmpty(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        Assert.assertTrue(!testCaseLog.hasEncounteredErrors());
    }

    @Test
    public void methodHasEncounteredErrorShouldReturnFalseOnNoErrors(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        testCaseLog.log(LogLevel.DEBUG, "Debug");
        Assert.assertTrue(!testCaseLog.hasEncounteredErrors());
    }

    @Test
    public void methodToJsonShouldReturnLogPostIfExist(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        testCaseLog.log(LogLevel.DEBUG, "Dummy log post");
        Assert.assertTrue(testCaseLog.toJson(), testCaseLog.toJson().contains("Dummy log post"));
    }

    @Test
    public void methodToStringShouldNotReturnDebugRowsIfNoErrorExists(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        testCaseLog.log(LogLevel.DEBUG, "Dummy log post");
        Assert.assertTrue(!testCaseLog.toString().contains("Dummy log post"));
    }

    @Test
    public void methodToStringShouldReturnDebugRowsIfErrorsExist(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        testCaseLog.log(LogLevel.DEBUG, "Dummy log post");
        testCaseLog.log(LogLevel.FRAMEWORK_ERROR, "Framework errors");
        Assert.assertTrue(testCaseLog.toString().contains("Dummy log post"));
    }

    @Test
    public void methodToStringShouldReturnNonDebugLogPostsIfTheyExist(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        testCaseLog.log(LogLevel.EXECUTED, "Dummy log post");
        Assert.assertTrue(testCaseLog.toString().contains("Dummy log post"));
    }

    @Test
    public void methodToLogSectionsShouldProvideLogSectionsForLogPostsInDifferentSections(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        testCaseLog.logPosts.add(new LogPost(LogLevel.EXECUTED, "Section1 message", "Html Section1 message", "Test case 1", "Test step 1", "Test step class name 1"));
        testCaseLog.logPosts.add(new LogPost(LogLevel.EXECUTED, "Section2 message", "Html Section2 message", "Test case 1", "Test step 2", "Test step class name 2"));
        Assert.assertTrue(testCaseLog.toLogSections().size() == 2);
    }

    @Test
    public void methodToLogSectionsShouldReturnMessageIfNoLogPosts(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        Assert.assertTrue(testCaseLog.toLogSections().size() == 1);
        Assert.assertTrue(testCaseLog.logPosts.get(0).message.contains("Nothing logged"));
    }

    @Test
    public void registeringANonErrorShouldNotProduceALogRowWithSuggestionForRegisteringAKnownError(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        testCaseLog.log(LogLevel.INFO, "Info posts should not create extra log posts");
        Assert.assertTrue(testCaseLog.logPosts.size() == 1);
    }

    @Test
    public void registeringANonErrorInDifferentiatedLoggingShouldNotProduceALogRowWithSuggestionForRegisteringAKnownError(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        testCaseLog.logDifferentlyToTextLogAndHtmlLog(LogLevel.INFO, "Info posts should not create extra log posts", "HTML log post message");
        Assert.assertTrue(testCaseLog.logPosts.size() == 1);
    }

    @Test
    public void registeringANonErrorInComplexLoggingShouldNotProduceALogRowWithSuggestionForRegisteringAKnownError(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        testCaseLog.log(LogLevel.INFO, "Info posts should not create extra log posts", "HTML Log post message", "testName", "stepName", "className");
        Assert.assertTrue(testCaseLog.logPosts.size() == 1);
    }

    @Test
    public void registeringAnErrorShouldProduceALogRowWithSuggestionForRegisteringAKnownError(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "Problem posts should not create extra log posts");
        Assert.assertTrue(testCaseLog.logPosts.size() == 2);
        Assert.assertTrue(testCaseLog.logPosts.get(1).message, testCaseLog.logPosts.get(1).toString().contains("Info"));
        Assert.assertTrue(testCaseLog.logPosts.get(1).message, testCaseLog.logPosts.get(1).message.contains("If you want to add this error as a known error you should enter the line below to your test case:"));
        Assert.assertTrue(testCaseLog.logPosts.get(1).message, testCaseLog.logPosts.get(1).message.contains("currentTestCase().addKnownError(\"<Your description of this error>\", \".*Problem posts should not create extra log posts.*\");"));
        Assert.assertTrue(testCaseLog.logPosts.get(1).toHtmlTableRow(), testCaseLog.logPosts.get(1).toHtmlTableRow().contains("currentTestCase().addKnownError(\"-- Your description of this error --\", \".*Problem posts should not create extra log posts.*\");"));
    }

    @Test
    public void registeringAnErrorInDifferentLoggingShouldProduceALogRowWithSuggestionForRegisteringAKnownError(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        testCaseLog.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTION_PROBLEM, "Problem posts should not create extra log posts", "Problem HTML posts should not create extra log posts");
        Assert.assertTrue(testCaseLog.logPosts.size() == 2);
        Assert.assertTrue(testCaseLog.logPosts.get(1).message, testCaseLog.logPosts.get(1).toString().contains("Info"));
        Assert.assertTrue(testCaseLog.logPosts.get(1).message, testCaseLog.logPosts.get(1).message.contains("If you want to add this error as a known error you should enter the line below to your test case:"));
        Assert.assertTrue(testCaseLog.logPosts.get(1).message, testCaseLog.logPosts.get(1).message.contains("currentTestCase().addKnownError(\"<Your description of this error>\", \".*Problem posts should not create extra log posts.*\");"));
        Assert.assertTrue(testCaseLog.logPosts.get(1).toHtmlTableRow(), testCaseLog.logPosts.get(1).toHtmlTableRow().contains("currentTestCase().addKnownError(\"-- Your description of this error --\", \".*Problem posts should not create extra log posts.*\");"));
    }
//                "If you want to add this error as a known error you should enter the line below to your test case:" + System.lineSeparator() + System.lineSeparator() + "      currentTestCase.addKnownError(\"<Your description of this error>\", \".*" + pureTextMessage + ".*\");" + System.lineSeparator(),
//                "If you want to add this error as a known error you should enter the line below to your test case:<br><pre>     currentTestCase.addKnownError(\"-- Your description of this error --\", \".*" + pureTextMessage + ".*\");</pre>");

    @Test
    public void registeringAnErrorInStepLoggingShouldProduceALogRowWithSuggestionForRegisteringAKnownError(){
        TestCaseLog testCaseLog = new TestCaseLog("dummy");
        testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "Problem posts should not create extra log posts", "Problem HTML posts should not create extra log posts", "testCase", "testStep", "className");
        Assert.assertTrue(testCaseLog.logPosts.size() == 2);
        Assert.assertTrue(testCaseLog.logPosts.get(1).message, testCaseLog.logPosts.get(1).toString().contains("Info"));
        Assert.assertTrue(testCaseLog.logPosts.get(1).message, testCaseLog.logPosts.get(1).message.contains("If you want to add this error as a known error you should enter the line below to your test case:"));
        Assert.assertTrue(testCaseLog.logPosts.get(1).message, testCaseLog.logPosts.get(1).message.contains("currentTestCase().addKnownError(\"<Your description of this error>\", \".*Problem posts should not create extra log posts.*\");"));
        Assert.assertTrue(testCaseLog.logPosts.get(1).toHtmlTableRow(), testCaseLog.logPosts.get(1).toHtmlTableRow().contains("currentTestCase().addKnownError(\"-- Your description of this error --\", \".*Problem posts should not create extra log posts.*\");"));
    }

}
