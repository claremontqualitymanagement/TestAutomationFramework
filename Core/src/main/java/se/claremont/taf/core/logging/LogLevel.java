package se.claremont.taf.core.logging;

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
    DEBUG("Debug", 0, false),
    INFO("Info", 1, false),
    VERIFICATION_PASSED("Verification passed", 3, false),
    VERIFICATION_PROBLEM("Verification problem", 4, true),
    VERIFICATION_FAILED("Verification failed", 5, true),
    EXECUTED("Executed", 3, false),
    EXECUTION_PROBLEM("Execution problem", 6, true),
    FRAMEWORK_ERROR("Framework error", 7, true),
    DEVIATION_EXTRA_INFO("Deviation extra info", 4, false);

    private final int value;
    private final boolean isFail;
    private final String friendlyName;
    LogLevel(String friendlyName, int value, boolean isFail) {
        this.value = value;
        this.isFail = isFail;
        this.friendlyName = friendlyName;
    }

    public String toString(){ return friendlyName; }
    public int getValue() {
        return value;
    }
    @SuppressWarnings("unused")
    public boolean isFail() {return isFail; }

}
