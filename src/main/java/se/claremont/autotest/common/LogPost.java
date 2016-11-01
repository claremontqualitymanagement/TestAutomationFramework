package se.claremont.autotest.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.support.SupportMethods;
import se.claremont.tools.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static se.claremont.autotest.common.TestCaseLogReporterHtmlLogFile.enumMemberNameToLower;
import static se.claremont.autotest.support.SupportMethods.LF;

/**
 * A testCaseLog post for a test case execution testCaseLog.
 *
 * Created by jordam on 2016-08-25.
 */
@SuppressWarnings("WeakerAccess")
class LogPost {

    private final static Logger logger = LoggerFactory.getLogger( LogPost.class );

    protected final LogLevel logLevel;
    protected final String message;
    private final String htmlMessage;
    final Date date;
    String testCaseName = null;
    String testStepName = null;
    String testStepClassName = null;
    protected boolean identifiedToBePartOfKnownError = false;


    /**
     * Creates a new testCaseLog post with component sorting enabled
     * @param logLevel the testCaseLog level of the testCaseLog post
     * @param message the testCaseLog message string
     */
    public LogPost(LogLevel logLevel, String message, String htmlMessage) {
        this.logLevel = logLevel;
        this.message = message;
        this.htmlMessage = htmlMessage;
        this.date = new Date();
    }

    public LogPost(LogLevel logLevel, String message, String htmlMessage, String testCaseName, String testStepName, String testStepClassName){
        this.logLevel = logLevel;
        this.message = message;
        this.htmlMessage = htmlMessage;
        this.date = new Date();
        this.testStepName = testStepName;
        this.testStepClassName = testStepClassName;
        this.testCaseName = testCaseName;
    }

    /**
     * Creates a new testCaseLog post
     * @param logLevel the testCaseLog level of the testCaseLog post
     * @param message the testCaseLog message string
     */
    public LogPost(LogLevel logLevel, String message){
        this.logLevel = logLevel;
        this.message = message;
        this.date = new Date();
        this.htmlMessage = "";
    }


    /**
     * Checks if the testCaseLog post is deemed as a fail
     *
     * @return Return true if the testCaseLog level is in the fail category
     */
    public boolean isFail(){
        return !(logLevel.equals(LogLevel.DEBUG) || logLevel.equals(LogLevel.EXECUTED) || logLevel.equals(LogLevel.INFO) || logLevel.equals(LogLevel.DEVIATION_EXTRA_INFO) || logLevel.equals(LogLevel.VERIFICATION_PASSED));
    }

    /**
     * Makes the testCaseLog level name even length for testCaseLog formatting purposes
     *
     * @param logLevelName The testCaseLog level name
     * @return A friendly name variant of the testCaseLog level name
     */
    private String logLevelToString(String logLevelName){
        StringBuilder sb = new StringBuilder(SupportMethods.enumCapitalNameToFriendlyString(logLevelName));
        for(int i = 0; i <  TestCaseLog.maxNumberOfCharactersInLogLevelNames - logLevelName.length(); i++){
            sb.append(" ");
        }
        return sb.toString();
    }

    public @Override String toString(){
        return new SimpleDateFormat("HH:mm:ss").format(date) + " " + logLevelToString(logLevel.toString()) + " " + message;// + " - (test case '" + testCaseName + "', test step '" + testStepName + "' in class '" + testStepClassName + "').";
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
            sb.append("Saved screenshot as '<span class=\"").append(enumMemberNameToLower(TestCaseLogReporterHtmlLogFile.HtmlLogStyleNames.DATA.toString())).append("\">").append(parts[1]).append("</span>'<br><a href=\"file://").append(parts[1].replace("\\", "/")).append("\" target=\"_blank\"><img class=\"screenshot\" alt=\"screenshot\" src=\"file://").append(parts[1].replace("\\", "/")).append("\"></a>");
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
        return sb.toString();
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
        return "{" + SupportMethods.LF +
                "    \"testcasename\": \"" + this.testCaseName + "\"," + SupportMethods.LF +
                "    \"teststepname\": \"" + this.testStepName + "\"," + SupportMethods.LF +
                "    \"teststepclassname\": \"" + this.testStepClassName+ "\"," + SupportMethods.LF +
                "    \"time\": \"" + new SimpleDateFormat("yyyMMdd HH:mm:ss").format(date) + "\"," + SupportMethods.LF +
                "    \"loglevel\": \"" + SupportMethods.enumCapitalNameToFriendlyString(this.logLevel.toString()) + "\"," + SupportMethods.LF +
                "    \"puretextmessage\": \"" + this.message + "\"," + SupportMethods.LF +
                "    \"htmlmessage\": \"" + this.htmlMessage + "\"," +
                "    \"identifiedtobepartofknownerror\": " + String.valueOf(this.identifiedToBePartOfKnownError).toLowerCase() + SupportMethods.LF +
                "}" + SupportMethods.LF;
    }

    public static String htmlStyleInformation(){
        return  "      span." + enumMemberNameToLower(TestCaseLogReporterHtmlLogFile.HtmlLogStyleNames.DATA.toString()) + "               { color: blue; }" + LF +
                "      td." + enumMemberNameToLower(TestCaseLogReporterHtmlLogFile.HtmlLogStyleNames.TIMESTAMP.toString()) + "            { color: grey; width: 80px; }" + LF +
                "      td.logpostloglevel.debug                 { color: darkgrey; }" + LF +
                "      td.logpostloglevel.verification-passed   { color: green; }" + LF +
                "      td.logpostloglevel.verification-failed   { color: red; font-weight: bold; }" + LF +
                "      td.logpostloglevel.verification-problem  { color: red; font-weight: bold; }" + LF +
                "      td.logpostloglevel.execution-problem     { color: red; font-weight: bold; }" + LF +
                "      td.logpostloglevel.executed              { color: black; }" + LF +
                "      td.logpostloglevel.deviation-extra-info  { color: blue; }" + LF +
                "      td.logpostloglevel.info                  { color: blue; }" + LF +
                "      td.logpostloglevel.framework-error       { color: red; font-weight: bold; }" + LF;
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
            rowMessage = message.replace(LF, "<br>").replace("\n", "<br>");
        } else {
          rowMessage = htmlMessage;
        }
        sb.append("              <tr class=\"logpost ").append(logRowClass).append("\">").append(SupportMethods.LF);
        sb.append("                 <td class=\"").append(enumMemberNameToLower(TestCaseLogReporterHtmlLogFile.HtmlLogStyleNames.TIMESTAMP.toString())).append("\">").append(new SimpleDateFormat("HH:mm:ss").format(date)).append("</td>").append(SupportMethods.LF);
        sb.append("                 <td class=\"logpostloglevel " + logRowClass + "\">").append(logLevelToString(logLevel.toString().trim())).append("</td>").append(SupportMethods.LF);
        sb.append("                 <td class=\"logmessage\">").append(LF).append(LF).append(substituteDataElements(rowMessage)).append(LF).append(LF).append("                 </td>").append(SupportMethods.LF);
        sb.append("              </tr>").append(SupportMethods.LF);
        return sb.toString();
    }


}
