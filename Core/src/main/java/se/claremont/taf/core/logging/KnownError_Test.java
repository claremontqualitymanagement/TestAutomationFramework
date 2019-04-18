package se.claremont.taf.core.logging;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 *
 * Tests for KnownError class
 *
 * Created by jordam on 2016-08-30.
 */
public class KnownError_Test extends UnitTestClass {

    @Test
    public void equalTest(){
        KnownError knownError1 = new KnownError("Description1", "TestCaseLog row 1.");
        KnownError knownError2 = new KnownError("Description1", "TestCaseLog row 1.");
        Assert.assertTrue("Known errors equality test failed. " + knownError1.toString() + " is not equal " + knownError2.toString(), knownError1.equals(knownError2));
        KnownError knownError3 = new KnownError("Description1", "TestCaseLog row 2.");
        Assert.assertFalse("Known errors equality test failed. " + knownError1.toString() + " is equal " + knownError3.toString(), knownError1.equals(knownError3));
        KnownError knownError4 = new KnownError("Description2", "TestCaseLog row 1.");
        Assert.assertFalse("Known errors equality test failed. " + knownError1.toString() + " is equal " + knownError4.toString(), knownError1.equals(knownError4));

    }

    @Test
    public void matchOfSeveralStringsEmptyLog(){
        String[] patterns = {"Pattern1", "Pattern2"};
        TestCase testCase = new TestCase(new KnownErrorsList(), "KnownError_Test");
        KnownError knownError = new KnownError("Description", patterns);
        Assert.assertFalse("The known error was reported found, but wasn't present in the testCaseLog", knownError.thisErrorIsEncountered(testCase));
    }

    @Test
    public void matchOfSeveralStringsNoError(){
        String[] patterns = {"Pattern1", "Pattern2"};
        KnownError knownError = new KnownError("Description", patterns);
        TestCase testCase = new TestCase(new KnownErrorsList(), "KnownError_Test");
        testCase.testCaseResult.testCaseLog.log(LogLevel.EXECUTED, "Pattern0");
        Assert.assertFalse("The known error was reported found, but wasn't present in the testCaseLog", knownError.thisErrorIsEncountered(testCase));
    }

    @Test
    public void matchOfSeveralStringsMatch(){
        String[] patterns = {"Pattern1", "Pattern2"};
        KnownError knownError = new KnownError("Description", patterns);
        TestCase testCase = new TestCase(new KnownErrorsList(), "KnownError_Test");
        testCase.testCaseResult.testCaseLog.log(LogLevel.EXECUTED, "Pattern0");
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern1");
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern2");
        testCase.testCaseResult.testCaseLog.log(LogLevel.EXECUTED, "Pattern0");
        Assert.assertTrue("The known error wasn't reported found, but was present in the testCaseLog", knownError.thisErrorIsEncountered(testCase));
    }

    @Test
    public void matchOfSeveralStringsBothMatchAndOtherErrors(){
        String[] patterns = {"Pattern1", "Pattern2"};
        KnownError knownError = new KnownError("Description", patterns);
        TestCase testCase = new TestCase(new KnownErrorsList(), "KnownError_Test");
        testCase.testCaseResult.testCaseLog.log(LogLevel.EXECUTED, "Pattern0");
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern1");
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern2");
        testCase.testCaseResult.testCaseLog.log(LogLevel.EXECUTED, "Pattern0");
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern3");
        Assert.assertTrue("The known error wasn't reported found, but was present in the testCaseLog", knownError.thisErrorIsEncountered(testCase));
    }

    @Test
    public void matchOfSeveralStringsPartialMatch(){
        String[] patterns = {"Pattern1", "Pattern2"};
        KnownError knownError = new KnownError("Description", patterns);
        TestCase testCase = new TestCase(new KnownErrorsList(), "KnownError_Test");
        testCase.testCaseResult.testCaseLog.log(LogLevel.EXECUTED, "Pattern0");
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern1");
        testCase.testCaseResult.testCaseLog.log(LogLevel.EXECUTED, "Pattern0");
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern3");
        Assert.assertFalse("The known error was reported found, but wasn't present in the testCaseLog", knownError.thisErrorIsEncountered(testCase));
    }

    @Test
    public void matchOfSeveralStringsOnlyNewErrors(){
        String[] patterns = {"Pattern1", "Pattern2"};
        KnownError knownError = new KnownError("Description", patterns);
        TestCase testCase = new TestCase(new KnownErrorsList(), "KnownError_Test");
        testCase.testCaseResult.testCaseLog.log(LogLevel.EXECUTED, "Pattern0");
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern4");
        testCase.testCaseResult.testCaseLog.log(LogLevel.EXECUTED, "Pattern0");
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern3");
        Assert.assertFalse("The known error was reported found, but wasn't present in the testCaseLog", knownError.thisErrorIsEncountered(testCase));
    }

    @Test
    public void thisErrorIsEncounteredSingleRowTest(){
        KnownError knownError = new KnownError("Description", ".*TestCaseLog row.*");
        TestCase testCase = new TestCase(new KnownErrorsList(), this.getClass().getName());
        testCase.testCaseResult.testCaseLog.log(LogLevel.DEBUG, "No error TestCaseLog row");
        Assert.assertFalse(knownError.thisErrorIsEncountered(testCase));
        testCase.testCaseResult.testCaseLog.log(LogLevel.FRAMEWORK_ERROR, "No error TestCaseLog row");
        Assert.assertTrue(knownError.thisErrorIsEncountered(testCase));
    }

    @Test
    public void thisErrorIsEncounteredMultipleRowsTestPositive(){
        String[] patterns = {".*TestCaseLog row.*", ".*Problem.*"};
        KnownError knownError = new KnownError("Description", patterns);
        TestCase testCase = new TestCase(new KnownErrorsList(), this.getClass().getName());
        testCase.testCaseResult.testCaseLog.log(LogLevel.FRAMEWORK_ERROR, "No error TestCaseLog row");
        testCase.testCaseResult.testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "Problem");
        Assert.assertTrue(knownError.thisErrorIsEncountered(testCase));
    }

    @Test
    public void thisErrorIsEncounteredMultipleRowsTestNegative(){
        String[] patterns = {"TestCaseLog row", "Problem"};
        KnownError knownError = new KnownError("Description", patterns);
        TestCase testCase = new TestCase(new KnownErrorsList(), this.getClass().getName());
        testCase.testCaseResult.testCaseLog.log(LogLevel.DEBUG, "No error TestCaseLog row");
        Assert.assertFalse(knownError.thisErrorIsEncountered(testCase));
        testCase.testCaseResult.testCaseLog.log(LogLevel.FRAMEWORK_ERROR, "Problem");
        Assert.assertFalse(knownError.thisErrorIsEncountered(testCase));
    }

    @Test
    public void knownErrorToString(){
        KnownError knownError1 = new KnownError("Description", "Pattern");
        Assert.assertTrue("Known error toString() was expected to be '['Description': 'Pattern', encountered: false]', but was '" + knownError1.toString() + "'", knownError1.toString().equals("['Description': 'Pattern', encountered: false]"));
        String[] patterns = {"Pattern1", "Pattern2"};
        KnownError knownError2 = new KnownError("Description", patterns);
        Assert.assertTrue("Known error toString() was expected to be '['Description': 'Pattern1', 'Pattern2', encountered: false]', but was '" + knownError2.toString() + "'", knownError2.toString().equals("['Description': 'Pattern1', 'Pattern2', encountered: false]"));
    }

    @SuppressWarnings({"ConstantConditions", "UnusedAssignment", "unused"})
    @Test
    public void constructorNullCheck(){
        try{
            String string1 = null;
            String string2 = null;
            KnownError knownError = new KnownError(string1, string2);
        }catch (Exception e){
            Assert.assertTrue("KnownError constructor cannot handle nulls", false);
        }
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    @Test
    public void toStringNullCheck(){
        try{
            String string1 = null;
            String string2 = null;
            KnownError knownError = new KnownError(string1, string2);
            knownError.toString();
        }catch (Exception e){
            Assert.assertTrue("KnownError toString() cannot handle nulls", false);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void thisErrorIsEncounteredNullCheck(){
        try{
            TestCase testCase = new TestCase(new KnownErrorsList(), this.getClass().getName());
            testCase.log(LogLevel.DEBUG, "Test");
            String string1 = null;
            String string2 = null;
            KnownError knownError = new KnownError(string1, string2);
            knownError.thisErrorIsEncountered(testCase);
        }catch (Exception e){
            Assert.assertTrue("KnownError thisErrorIsEncountered() cannot handle nulls", false);
        }
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    @Test
    public void equalsNullCheck(){
        try{
            String string1 = null;
            String string2 = null;
            KnownError knownError = new KnownError(string1, string2);
            KnownError knownError2 = new KnownError(string1, string2);
            knownError.equals(knownError2);
            KnownError knownError3 = new KnownError("Desc", "Log row");
            knownError.equals(knownError3);
            knownError3.equals(knownError);
        }catch (Exception e){
            Assert.assertTrue("KnownError toString cannot handle nulls", false);
        }
    }

    @Test
    public void methodToJsonShouldReturnErrors(){
        KnownError knownError = new KnownError("description", "pattern");
        Assert.assertTrue(knownError.toJson().contains("description") && knownError.toJson().contains("pattern"));

    }

}
