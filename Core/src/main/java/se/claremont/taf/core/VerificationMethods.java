package se.claremont.taf.core;

import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

public class VerificationMethods {

    public TestCase testCase;
    public Boolean wasSuccess;
    public boolean noFailsInBuilderChain;

    public VerificationMethods(TestCase testCase){
        this.testCase = testCase;
        wasSuccess = null;
        noFailsInBuilderChain = true;
    }

    public VerificationMethods textEquals(String actualText, String expectedText){
        return text(actualText, expectedText, StringComparisonType.Exact);
    }

    public VerificationMethods text(String inputString, String pattern, StringComparisonType stringComparisonType){
        if(stringComparisonType.match(inputString, pattern)){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Verified that '" + pattern + "' was a match for string:" + System.lineSeparator() + inputString);
            wasSuccess = true;
        } else {
            wasSuccess = false;
            noFailsInBuilderChain = false;
            testCase.log(LogLevel.VERIFICATION_FAILED, "No match found for '" + pattern + "' in string:" + System.lineSeparator() + inputString + System.lineSeparator() + "Using matching method '" + stringComparisonType.toString() + "'.");
        }
        return this;
    }

    public VerificationMethods equals(Object actualObject, Object expectedObject){
        boolean match = (actualObject == null && expectedObject == null) || (expectedObject != null && expectedObject.equals(actualObject));
        if(match){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Verified that the object was '" + actualObject.toString() + "'.");
            wasSuccess = true;
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, actualObject.getClass().getName() + ":" + System.lineSeparator() + actualObject.toString() + System.lineSeparator() + "did not match " + expectedObject.getClass().getName() + ":" + System.lineSeparator() + expectedObject.toString());
            wasSuccess = false;
            noFailsInBuilderChain = false;
        }
        return this;
    }
}
