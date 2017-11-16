package se.claremont.autotest.common.logging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.common.logging.logmessage.LogMessage;
import se.claremont.autotest.common.reporting.UxColors;
import se.claremont.autotest.common.reporting.testcasereports.TestCaseLogReporterHtmlLogFile;
import se.claremont.autotest.common.support.ColoredConsolePrinter;
import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testcase.TestCaseLog;
import se.claremont.autotest.common.testrun.TestRun;

import java.text.SimpleDateFormat;
import java.util.Date;

import static se.claremont.autotest.common.reporting.testcasereports.TestCaseLogReporterHtmlLogFile.enumMemberNameToLower;

/**
 * A testCaseLog post for a test case execution testCaseLog.
 *
 * Created by jordam on 2016-08-25.
 */
@SuppressWarnings("WeakerAccess")
@JsonIgnoreProperties({"fail", "logger"})
public class LogPost {

    private final static Logger logger = LoggerFactory.getLogger( LogPost.class );

    @JsonProperty public LogLevel logLevel;
    @JsonProperty public LogMessage logMessage;
    @JsonProperty public String message;
    @JsonProperty private String htmlMessage;
    @JsonProperty public final Date date;
    @JsonProperty String testCaseName = null;
    @JsonProperty public String testStepName = null;
    @JsonProperty public String testStepClassName = null;
    @JsonProperty public boolean identifiedToBePartOfKnownError = false;

    //Default dummy constructor for ObjectMapper for JSON to work
    LogPost(){
        date = new Date();
    }

    /*
    /**
     * Creates a new testCaseLog post with component sorting enabled
     * @param logLevel the testCaseLog level of the testCaseLog post
     * @param message the testCaseLog message string
     * @param htmlMessage The HTML representation of the log message - used for HTML based log reports
     */
    /*
    public LogPost(LogLevel logLevel, String message, String htmlMessage) {
        this.logLevel = logLevel;
        this.message = message;
        this.htmlMessage = htmlMessage;
        this.date = new Date();
    }
    */

    /**
     * Creating a log post for a test log.
     *
     * @param logLevel The type of log post, also relevant for severity.
     * @param message Text based representation of the log message.
     * @param htmlMessage HTML based representation of the log message. If left as null, the text message will be used instead.
     * @param testCaseName The name of the test case, for logging, but also to identify the test step name through reflection.
     * @param testStepName The test step name, for HTML logging purposes.
     * @param testStepClassName The test step class name - to ease debugging.
     */
    public LogPost(LogLevel logLevel, String message, String htmlMessage, String testCaseName, String testStepName, String testStepClassName){
        this.logLevel = logLevel;
        this.message = message;
        this.htmlMessage = htmlMessage;
        this.date = new Date();
        this.testStepName = testStepName;
        this.testStepClassName = testStepClassName;
        this.testCaseName = testCaseName;
    }

    /*

/**
 * Creates a new testCaseLog post
 * @param logLevel the testCaseLog level of the testCaseLog post
 * @param message the testCaseLog message string
 */
    /*
    public LogPost(LogLevel logLevel, String message){
        this.logLevel = logLevel;
        this.message = message;
        this.date = new Date();
        this.htmlMessage = "";
    }
    */

    /**
     * Creating a log post for a test log.
     *
     * @param logLevel The type of log post, also relevant for severity.
     * @param logMessage The log message (possibly divided into different parts of different types).
     * @param testCaseName The name of the test case, for logging, but also to identify the test step name through reflection.
     * @param testStepName The test step name, for HTML logging purposes.
     * @param testStepClassName The test step class name - to ease debugging.
     */
    public LogPost(LogLevel logLevel, LogMessage logMessage, String testCaseName, String testStepName, String testStepClassName){
        this.htmlMessage = logMessage.toHtml();
        this.message = logMessage.toString();
        this.date = new Date();
        this.logLevel = logLevel;
        this.testCaseName = testCaseName;
        this.testStepName = testStepName;
        this.testStepClassName = testStepClassName;
    }

    /**
     * Checks if the testCaseLog post is deemed as a fail
     *
     * @return Return true if the testCaseLog level is in the fail category
     */
    public boolean isFail(){
        return !(logLevel.equals(LogLevel.DEBUG)
                || logLevel.equals(LogLevel.EXECUTED)
                || logLevel.equals(LogLevel.INFO)
                || logLevel.equals(LogLevel.DEVIATION_EXTRA_INFO)
                || logLevel.equals(LogLevel.VERIFICATION_PASSED));
    }

    /**
     * Makes the testCaseLog level name even length for testCaseLog formatting purposes
     *
     * @param logLevelName The testCaseLog level name
     * @return A friendly name variant of the testCaseLog level name
     */
    private String logLevelToString(String logLevelName){
        StringBuilder sb = new StringBuilder(StringManagement.enumCapitalNameToFriendlyString(logLevelName));
        for(int i = 0; i <  TestCaseLog.maxNumberOfCharactersInLogLevelNames - logLevelName.length(); i++){
            sb.append(" ");
        }
        return sb.toString();
    }

    public @Override String toString(){
        String logLevelString = logLevelToString(logLevel.toString());
        if(logLevel.isFail()){
            logLevelString = ColoredConsolePrinter.bold(ColoredConsolePrinter.red(logLevelString));
        } else if(logLevel == LogLevel.DEBUG){
            logLevelString = ColoredConsolePrinter.cyan(logLevelString);
        } else if(logLevel == LogLevel.INFO){
            logLevelString = ColoredConsolePrinter.blue(logLevelString);
        } else if(logLevel == LogLevel.VERIFICATION_PASSED){
            logLevelString = ColoredConsolePrinter.green(logLevelString);
        }
        return new SimpleDateFormat("HH:mm:ss").format(date) + " " + logLevelString + " " + message;// + " - (test case '" + testCaseName + "', test step '" + testStepName + "' in class '" + testStepClassName + "').";
    }

    public @Override boolean equals(Object object){
        if(object.getClass() != LogPost.class) return false;
        try {
            LogPost other = (LogPost) object;
            if( this.htmlMessage.equals(other.htmlMessage)&&
                    this.message.equals(other.message) &&
                    this.logLevel.equals(other.logLevel))
                return true;
        }catch (Exception e){return false;}
        return false;
    }

    /**
     * Compares two testCaseLog posts to see if they are similar. Similarity is
     * decided by replacing DATA sections of the testCaseLog post message and
     * then comparing strings.
     * @param object the LogPost to compare against
     * @return true if the testCaseLog posts are considered similar
     */
    public boolean isSimilar(Object object){
        try {
            LogPost other = (LogPost) object;
            if(this.logLevel.equals(other.logLevel) && isSimilar(this.message, other.message)){
                return true;
            }
        }catch (Exception e){return false;}
        return false;
    }

    /**
     * Adds HTML formatting information to DATA entities in the testCaseLog post message string.<br>
     *     Also inserts link references to screenshot testCaseLog rows
     * @param instring The testCaseLog post message string
     * @return The same testCaseLog post message string but with html formatting applied
     */
    private String substituteDataElements(String instring){
        StringBuilder sb = new StringBuilder();
        if(instring.startsWith("Saved screenshot as '")){
            String[] parts = instring.split("'");
            sb.append("Saved screenshot as '<span class=\"").append(enumMemberNameToLower(TestCaseLogReporterHtmlLogFile.HtmlLogStyleNames.DATA.toString())).append("\">").append(parts[1]).append("</span>'<br><a href=\"").append(TestRun.reportLinkPrefix()).append("://").append(parts[1].replace("\\", "/")).append("\" target=\"_blank\"><img class=\"screenshot\" alt=\"screenshot\" src=\"").append(TestRun.reportLinkPrefix()).append("://").append(parts[1].replace("\\", "/")).append("\"></a>");
        }else {
            String[] parts = instring.split("'");
            if(instring.startsWith("'")){
                for(int i = 0; i < parts.length; i = i+2){
                    sb.append("'<span class=\"").append(enumMemberNameToLower(TestCaseLogReporterHtmlLogFile.HtmlLogStyleNames.DATA.toString())).append("\">").append(parts[i]).append("</span>'");
                    if(i+1 < parts.length){
                        sb.append(parts[i+1]);
                    }
                }
            }else {
                for (int i = 0; i < parts.length; i = i+2){
                    sb.append(parts[i]);
                    if(i+1 < parts.length){
                        sb.append("'<span class=\"").append(enumMemberNameToLower(TestCaseLogReporterHtmlLogFile.HtmlLogStyleNames.DATA.toString())).append("\">").append(parts[i + 1]).append("</span>'");
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * Parses the testCaseLog post message string for "'" characters, assuming
     * they contain DATA sections. The DATA in the DATA sections are then
     * substituted with "..." in order to be able to compare testCaseLog strings.
     * @param instring The testCaseLog post message string to analyze
     * @return Returns a testCaseLog string with DATA in DATA sections substituted with "..."
     */
    public static String removeDataElements(String instring){
        StringBuilder sb = new StringBuilder();
        String[] parts = instring.split("'");
        if(instring.startsWith("'a")){
            logger.debug( "Initial DATA element." );
            for(int i = 1; i < parts.length; i = i+2){
                logger.debug( "Data " + i + ": '" + parts[i] + "'" );
                sb.append("'...'");
                if(i+1 < parts.length){
                    logger.debug( "Text " + i+1 + ": '" + parts[i+1] + "'" );
                    sb.append(parts[i+1]);
                }
            }
        }else {
            System.out.println("Initial text element.");
            for (int i = 0; i < parts.length; i = i+2){
                sb.append(parts[i]);
                logger.debug( "Text " + i + ": '" + parts[i] + "'" );
                if(i+1 < parts.length){
                    logger.debug( "Data " + i+1 + ": '" + parts[i+1] + "'" );
                    sb.append("'...'");
                }
            }
        }
        String cleanedOfData = sb.toString();
        String[] words = cleanedOfData.split(" ");
        for(int i = 0; i < words.length; i++){
            String word = words[i];
            if(SupportMethods.isRegexMatch(word, "^[0-9]+$")){ // If the word only consist of numbers, like a millisecond count
                words[i] = "...";
            }
        }
        return String.join(" ", words);
    }


    @SuppressWarnings("WeakerAccess")
    public static boolean isSimilar(String string1, String string2){
        return removeDataElements(string1).equals(removeDataElements(string2));
    }


    /**
     * Return the log post instance in a serialized way in JSON format
     * @return Return the log post instance in a serialized way in JSON format
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

    public static String htmlStyleInformation(){
        return  "      span." + enumMemberNameToLower(TestCaseLogReporterHtmlLogFile.HtmlLogStyleNames.DATA.toString()) + "               { color: " + UxColors.DARK_BLUE.getHtmlColorCode() + "; }" + SupportMethods.LF +
                "      td." + enumMemberNameToLower(TestCaseLogReporterHtmlLogFile.HtmlLogStyleNames.TIMESTAMP.toString()) + "            { color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; width: 80px; }" + SupportMethods.LF +
                "      td.logpostloglevel.debug                 { color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; }" + SupportMethods.LF +
                "      td.logpostloglevel.verification.passed   { color: " + UxColors.GREEN.getHtmlColorCode() + "; }" + SupportMethods.LF +
                "      td.logpostloglevel.verification.failed   { color: " + UxColors.RED.getHtmlColorCode() + "; font-weight: bold; }" + SupportMethods.LF +
                "      td.logpostloglevel.verification.problem  { color: " + UxColors.RED.getHtmlColorCode() + "; font-weight: bold; }" + SupportMethods.LF +
                "      td.logpostloglevel.execution.problem     { color: " + UxColors.RED.getHtmlColorCode() + "; font-weight: bold; }" + SupportMethods.LF +
                "      td.logpostloglevel.executed              { color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; }" + SupportMethods.LF +
                "      td.logpostloglevel.deviation.extra.info  { color: " + UxColors.DARK_BLUE.getHtmlColorCode() + "; }" + SupportMethods.LF +
                "      td.logpostloglevel.info                  { color: " + UxColors.DARK_BLUE.getHtmlColorCode() + "; }" + SupportMethods.LF +
                "      td.logpostloglevel.framework.error       { color: " + UxColors.RED.getHtmlColorCode() + "; font-weight: bold; }" + SupportMethods.LF;
    }

    /**
     * Used for HTML formatting for HTML based logs
     * @return Returns a table row element in HTML syntax
     */
    @SuppressWarnings("SameParameterValue")
    public String toHtmlTableRow(){
        StringBuilder sb = new StringBuilder();
        String logRowClass = this.logLevel.toString().toLowerCase().replace("_", "-");
        //String logRowClass = enumMemberNameToLower(TestCaseLogReporterHtmlLogFile.HtmlLogStyleNames.LOG_ROW.toString());
        String rowMessage;
        if(htmlMessage == null || htmlMessage.length() == 0){
            rowMessage = message.replace(SupportMethods.LF, "<br>").replace("\n", "<br>");
        } else {
          rowMessage = htmlMessage;
        }
        sb.append("              <tr class=\"logpost ").append(logRowClass).append("\">").append(SupportMethods.LF);
        sb.append("                 <td class=\"").append(enumMemberNameToLower(TestCaseLogReporterHtmlLogFile.HtmlLogStyleNames.TIMESTAMP.toString())).append("\">").append(new SimpleDateFormat("HH:mm:ss").format(date)).append("</td>").append(SupportMethods.LF);
        sb.append("                 <td class=\"logpostloglevel ").append(logRowClass).append("\">").append(logLevelToString(logLevel.toString().trim())).append("</td>").append(SupportMethods.LF);
        sb.append("                 <td class=\"logmessage\">").append(SupportMethods.LF).append(SupportMethods.LF).append(substituteDataElements(rowMessage)).append(SupportMethods.LF).append(SupportMethods.LF).append("                 </td>").append(SupportMethods.LF);
        sb.append("              </tr>").append(SupportMethods.LF);
        return sb.toString();
    }

}
