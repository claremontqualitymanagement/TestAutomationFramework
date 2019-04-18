package se.claremont.taf.core.support;

import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

/**
 * Class to measure time spent in different test steps
 *
 * Created by jordam on 2016-10-04.
 */
@SuppressWarnings("WeakerAccess")
public class PerformanceTimer {
    final String name;
    long startTime;
    final TestCase testCase;

    /**
     * Constructor. Also starts timer.
     * @param timerName Name of timer, reported in logs.
     * @param testCase Current test case instance, for logging
     */
    public PerformanceTimer(String timerName, TestCase testCase){
        this.name = timerName;
        this.testCase = testCase;
        startTime = System.currentTimeMillis();
    }

    /**
     * Resets the starting time with current time as start time
     */
    public void restartTimer(){
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Verifies time spent for timer towards the millisecond threshold value.
     *
     * @param acceptedMillisecondsThresholdUntilFail If the timer has taken more than this amount of milliseconds a log post with a fail is written to the test case log, othervice a log post about a passed verification will be sent.
     */
    public void stopAndVerifyTime(int acceptedMillisecondsThresholdUntilFail){
        long stopTime = System.currentTimeMillis();
        if(stopTime - startTime <= acceptedMillisecondsThresholdUntilFail){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Timer '" + name + "' took " + (stopTime-startTime) + " which is less than the " + acceptedMillisecondsThresholdUntilFail + " millisecond threshold.");
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "Timer '" + name + "' took " + (stopTime-startTime) + " which is more than the " + acceptedMillisecondsThresholdUntilFail + " millisecond threshold.");
        }
    }

    /**
     * Checks if it has taken more than the stated amount of milliseconds
     *
     * @param milliseconds Number of milliseconds to use as threshold.
     * @return Returns true if the time used so far for this timer is longer than the given value, othervice returning false.
     */
    public boolean hasTakenLongerThan(int milliseconds){
        return System.currentTimeMillis() - startTime > milliseconds;
    }

    /**
     * Mainly used for debugging purposes. Writes the time spent to the test case log (as log level Executed).
     */
    public void stopAndLogTime(){
       testCase.log(LogLevel.EXECUTED, "Measured timer '" + name + "' to be " + (System.currentTimeMillis() - startTime) + " milliseconds.");
    }

    /**
     * Returning the number of milliseconds so far.
     *
     * @return The number of milliseconds used so far in this timer - in long number format.
     */
    public long millisecondsSoFar(){
        return System.currentTimeMillis() - startTime;
    }
}
