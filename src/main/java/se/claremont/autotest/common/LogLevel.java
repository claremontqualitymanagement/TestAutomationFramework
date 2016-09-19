package se.claremont.autotest.common;

/**
 * TestCaseLog level for {@link LogPost} in the TestCaseLog.
 * The logger can testCaseLog several different testCaseLog levels depending on classification of testCaseLog post. The testCaseLog levels can be divided into informational ones (DEBUG, INFO, DEVIATION_EXTRA_INFO), verification ones for checks in tests (VERIFICATION_PASSED, VERIFICATION_FAILED), and execution flow ones (VERIFICATION_PROBLEM, EXECUTED, EXECUTION_PROBLEM, and FRAMEWORK_ERROR). DEBUG is for debugging purposes and should be ignored if running smoothly<br>
 * INFO is for relevant contextual info.
 * <p>
 * VERIFICATION_PASSED is used when a verification is performed with result deemed as ok
 * VERIFICATION_PROBLEM is used when a verification couldn't be performed, for some reason
 * VERIFICATION_FAILED is used when a verification is performed with result with a deviation from expected oracle
 * EXECUTED is for actions performed. Generally used for navigation description
 * EXECUTION_PROBLEM is used when execution of an action could not be carried out
 * FRAMEWORK_ERROR is when an unhandled exception occurs
 * DEVIATION_EXTRA_INFO is complimentary information for debugging purposes when a deviation from expected behavior occurs
 */
public enum LogLevel{
    DEBUG,
    INFO,
    VERIFICATION_PASSED,
    VERIFICATION_PROBLEM,
    VERIFICATION_FAILED,
    EXECUTED,
    EXECUTION_PROBLEM,
    FRAMEWORK_ERROR,
    DEVIATION_EXTRA_INFO
}
