package se.claremont.autotest.common;

import se.claremont.autotest.support.SupportMethods;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Test case execution testCaseLog.
 *
 * Created by jordam on 2016-08-16.
 *
 */
@SuppressWarnings("SameParameterValue")
class TestCaseLog {
    final ArrayList<LogPost> logPosts = new ArrayList<>();
    static int maxNumberOfCharactersInLogLevelNames = 0;
    private String testCaseName = null;

    TestCaseLog(String testCaseName){
        maxNumberOfCharactersInLogLevelNames = getMaxNumberOfCharactersInLogLevelNames();
        this.testCaseName = testCaseName;
    }

    /**
     * Calculates the number of characters in the log level name with the longest name of all log levels.
     * Used to fill in space characters to even the text based mono type font style log.
     *
     * @return Returns the maximum number of characters in the names of all log levels
     */
    private int getMaxNumberOfCharactersInLogLevelNames(){
        int max = 0;
        for(LogLevel levelName : LogLevel.values()){
            int current = levelName.toString().length();
            if(current > max){
                max = current;
            }
        }
        return max;
    }

    /**
     * Checks if test case testCaseLog has logged any testCaseLog posts that are deemed as fails
     *
     * @return Returns true if testCaseLog has errors
     */
    public boolean hasEncounteredErrors(){
        for (LogPost logPost : logPosts){
            if(logPost.isFail()){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        boolean errorsEncountered = hasEncounteredErrors();
        StringBuilder sb = new StringBuilder();
        for(LogPost logPost : logPosts){
            if(!errorsEncountered && logPost.logLevel.equals(LogLevel.DEBUG)) continue;
            sb.append(logPost.toString()).append(SupportMethods.LF);
        }
        return sb.toString();
    }


    /**
     * Writes a testCaseLog post to the test case testCaseLog
     *
     * @param logLevel The {@link LogLevel} of this testCaseLog entry
     * @param message The string message of the testCaseLog
     */
    public void log(LogLevel logLevel, String message){
        String testStep = "Framework";
        String testStepClassName = "Framework";
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for(int i= 0; i < stackTraceElements.length; i++){
            if(stackTraceElements[i].getMethodName().equals(testCaseName)){
                testStep = stackTraceElements[i-1].getMethodName();
                testStepClassName = stackTraceElements[i-1].getClassName();
            }
        }
        LogPost logPost = new LogPost(logLevel, message, null, testCaseName, testStep, testStepClassName);
        System.out.println(logPost.toString());
        logPosts.add(logPost);
    }


    /**
     * Used when logging benefits from being different between HTML formatted testCaseLog info and other testCaseLog info.
     *
     * @param logLevel The {@link LogLevel} of the entry
     * @param pureTextMessage A test string based testCaseLog message
     * @param htmlFormattedMessage A HTML formatted testCaseLog message representation to be displayed within a HTML table cell tag
     */
    public void logDifferentlyToTextLogAndHtmlLog(LogLevel logLevel, String pureTextMessage, String htmlFormattedMessage){
        String testStep = "Framework";
        String testStepClassName = "Framework";
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for(int i= 0; i < stackTraceElements.length; i++){
            if(stackTraceElements[i].getMethodName().equals(testCaseName)){
                testStep = stackTraceElements[i-1].getMethodName();
                testStepClassName = stackTraceElements[i-1].getClassName();
            }
        }
        this.logPosts.add(new LogPost(logLevel, pureTextMessage, htmlFormattedMessage, testCaseName, testStep, testStepClassName));
    }

    /**
     * Method to get the testCaseLog posts considered as non-successful from the testCaseLog
     *
     * @return LogPost list consisting of only the non-successful testCaseLog posts of the testCaseLog
     */
    public ArrayList<LogPost> onlyErroneousLogPosts(){
        return logPosts.stream().filter(LogPost::isFail).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     *
     * Returns the first erroneous log post. Subsequent erroneous log posts might be triggered by this first error.
     *
     * @return Returns the first erroneous log post
     */
    public LogPost firstNonSuccessfulLogPost(){
        ArrayList<LogPost> fails = onlyErroneousLogPosts();
        if(fails.size() > 0){
            return fails.get(0);
        }
        return null;
    }

}
