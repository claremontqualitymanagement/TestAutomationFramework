package se.claremont.autotest.common.testcase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.common.logging.ConsoleLogLevel;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testrun.TestRun;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Test case execution testCaseLog.
 *
 * Created by jordam on 2016-08-16.
 *
 */
@SuppressWarnings("SameParameterValue")
@JsonIgnoreProperties({"logger", "maxNumberOfCharactersInLogLevelNames"})
public class TestCaseLog {
    @JsonProperty public final ArrayList<LogPost> logPosts = new ArrayList<>();
    @JsonProperty private String testCaseName = null;
    public static int maxNumberOfCharactersInLogLevelNames = 0;
    private final static Logger logger = LoggerFactory.getLogger( TestCaseLog.class );


    public TestCaseLog(){} //For JSON object mapping usage

    public TestCaseLog(String testCaseName){
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
     * Returns the log post list in a serialized format, as a JSON object.
     *
     * @return Returns the log post list in a serialized format, as a JSON object.
     */
    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.out.println(e.toString());
        }
        return json;
    }

    /**
     * Writes a testCaseLog post to the test case testCaseLog
     *
     * @param logLevel The {@link LogLevel} of this testCaseLog entry
     * @param message The string message of the testCaseLog
     */
    public void log(LogLevel logLevel, String message){
        if(message == null) message = "[null]";
        String testStep = "Framework actions";
        String testStepClassName = "Framework actions";
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for(int i= 0; i < stackTraceElements.length; i++){
            if(stackTraceElements[i].getMethodName().equals(testCaseName)){
                testStep = stackTraceElements[i-1].getMethodName();
                testStepClassName = stackTraceElements[i-1].getClassName();
            }
        }
        LogPost logPost = new LogPost(logLevel, message, null, testCaseName, testStep, testStepClassName);
        outputLogPost(logPost);
        logger.debug( logPost.toString() );
        logPosts.add(logPost);
        addKnownErrorSuggestionIfApplicable(logLevel, message);
    }

    /**
     * Writes a testCaseLog post to the test case testCaseLog
     *
     * @param logLevel The {@link LogLevel} of this testCaseLog entry
     * @param message The string message of the testCaseLog
     * @param htmlMessage The html representation of the log message
     * @param testCaseName The name of the test case
     * @param testStepName The name of the test step
     * @param testStepClassName The name of the test class
     */
    public void log(LogLevel logLevel, String message, String htmlMessage, String testCaseName, String testStepName, String testStepClassName){
        LogPost logPost = new LogPost(logLevel, message, null, testCaseName, testStepName, testStepClassName);
        logger.debug( logPost.toString() );
        outputLogPost(logPost);
        logPosts.add(logPost);
        addKnownErrorSuggestionIfApplicable(logLevel, message);
    }

    /**
     * Used when logging benefits from being different between HTML formatted testCaseLog info and other testCaseLog info.
     *
     * @param logLevel The {@link LogLevel} of the entry
     * @param pureTextMessage A test string based testCaseLog message
     * @param htmlFormattedMessage A HTML formatted testCaseLog message representation to be displayed within a HTML table cell tag
     */
    public void logDifferentlyToTextLogAndHtmlLog(LogLevel logLevel, String pureTextMessage, String htmlFormattedMessage){
        String testStep = "Framework actions";
        String testStepClassName = "Framework actions";
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for(int i= 0; i < stackTraceElements.length; i++){
            if(stackTraceElements[i].getMethodName().equals(testCaseName)){
                testStep = stackTraceElements[i-1].getMethodName();
                testStepClassName = stackTraceElements[i-1].getClassName();
            }
        }
        LogPost logPost = new LogPost(logLevel, pureTextMessage, htmlFormattedMessage, testCaseName, testStep, testStepClassName);
        outputLogPost(logPost);
        this.logPosts.add(logPost);
        addKnownErrorSuggestionIfApplicable(logLevel, pureTextMessage);
    }

    private void outputLogPost(LogPost logPost){
        if(TestRun.consoleLogLevel.equals(ConsoleLogLevel.NONE))return;
        if(TestRun.consoleLogLevel.equals(ConsoleLogLevel.ERRORS) && logPost.isFail()){
            System.out.println(logPost.toString());
            return;
        } else if(TestRun.consoleLogLevel.equals(ConsoleLogLevel.MODERATE) &&
                (logPost.logLevel.equals(LogLevel.DEBUG) ||
                 logPost.logLevel.equals(LogLevel.DEVIATION_EXTRA_INFO) ||
                 logPost.logLevel.equals(LogLevel.INFO))
                ){
            return;
        }
        System.out.println(logPost.toString());
    }

    private void addKnownErrorSuggestionIfApplicable(LogLevel logLevel, String pureTextMessage){
        if(logLevel.equals(LogLevel.DEBUG) ||
                logLevel.equals(LogLevel.EXECUTED) ||
                logLevel.equals(LogLevel.VERIFICATION_PASSED) ||
                logLevel.equals(LogLevel.INFO) ||
                logLevel.equals(LogLevel.DEVIATION_EXTRA_INFO)) return;
        logDifferentlyToTextLogAndHtmlLog(LogLevel.INFO,
                "If you want to add this error as a known error you should enter the line below to your test case:" + System.lineSeparator() + System.lineSeparator() + "      currentTestCase.addKnownError(\"<Your description of this error>\", \".*" + pureTextMessage + ".*\");" + System.lineSeparator(),
                "If you want to add this error as a known error you should enter the line below to your test case:<br><pre>     currentTestCase.addKnownError(\"-- Your description of this error --\", \".*" + pureTextMessage + ".*\");</pre>");
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

    public ArrayList<TestCaseLogSection> toLogSections(){
        String lastTestStepName = "";
        if(logPosts.size() == 0) {
            logPosts.add(new LogPost(LogLevel.DEBUG, "Nothing logged", "Nothing logged", this.testCaseName, "Nothing here", "Nothing here"));
        }
        Date startTime = logPosts.get(0).date;
        Date stopTime = logPosts.get(logPosts.size() -1).date;
        ArrayList<TestCaseLogSection> logSectionsList = new ArrayList<>();
        ArrayList<LogPost> logPostsInTestStep = new ArrayList<>();
        for(LogPost logPost : logPosts){
            if(!logPost.testStepName.equals(lastTestStepName)){
                logSectionsList.add(new TestCaseLogSection(logPostsInTestStep, startTime, stopTime));
                logPostsInTestStep = new ArrayList<>();
                lastTestStepName = logPost.testStepName;
            }
            logPostsInTestStep.add(logPost);
        }
        logSectionsList.add(new TestCaseLogSection(logPostsInTestStep, startTime, stopTime));
        logSectionsList.remove(0);
        return logSectionsList;
    }


}
