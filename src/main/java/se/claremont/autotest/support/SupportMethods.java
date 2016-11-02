package se.claremont.autotest.support;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.guidriverpluginstructure.swingsupport.festswinggluecode.ApplicationManager;
import se.claremont.tools.Utils;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A collection of mainly static methods that are used in several places within the framework
 *
 * Created by jordam on 2016-08-17.
 */
public class SupportMethods {

    private final static Logger logger = LoggerFactory.getLogger( SupportMethods.class );

    /**
     * Saves text based content to a file. If the file folder doesn't exist it is created.
     * @param content test based file content
     * @param filePath file path as a string
     */
    public static void saveToFile(String content, String filePath){
        Writer writer;

        if(filePath == null ){
            System.out.println("Could not write file to null file path.");
            logger.debug( "Could not write file to null file path." );
            return;
        }
        logger.debug( "Writing file content to '" + filePath + "'." );
        if(content == null){
            logger.debug( "Warning! Attempting to write a null string to file '." + filePath + "' Replacing null with empty string." );
            content = "";
        }
        try {
            File file = new File(filePath);
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
            if( !Utils.getInstance().doesFileExists( filePath ) )
                file.createNewFile();
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePath), "utf-8"));
            writer.write(content);
            writer.close();
        } catch (Exception ex) {
            Assert.fail("Could not write content to file '" + filePath + "'.");
        }
    }


    /**
     * Parses a string to a date
     * @param instring a date or time formatted string
     * @return A Date object corresponding to the instring
     */
    public static Date stringToDate(String instring){
        Date returnDate = null;
        instring = instring.replaceAll(":", "").replaceAll("-", "").replaceAll(" ", "");
        if(instring.length() == 8){
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            try {
                returnDate =  df.parse(instring);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if(instring.length() == 6){
            DateFormat df = new SimpleDateFormat("yyMMdd");
            try {
                returnDate =  df.parse(instring);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return returnDate;
    }


    public static String htmlContentToDisplayableHtmlCode(String htmlContent){
        return "<pre>" + LF + LF + htmlContent.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;") + LF + LF + "</pre>" + LF;
    }

    /**
     * Line-feed for current OS
     */
    public static final String LF = System.getProperty("line.separator");

    /**
     * Debugging method to see current stacktrace
     */
    @SuppressWarnings("unused")
    public static void printStacktrace(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for(int i= 0; i < stackTraceElements.length; i++){
            System.out.println("level: " + i + ": " + stackTraceElements[i].getMethodName() + " in class " + stackTraceElements[i].getClassName());
            logger.debug( "level: " + i + ": " + stackTraceElements[i].getMethodName() + " in class " + stackTraceElements[i].getClassName() );
        }
    }

    /**
     * Return the class name of the stated stack trace level
     * @param stacktraceLevel what level of the stack trace to return the class of
     * @return the name of the class at given stack trace level
     */
    @SuppressWarnings("SameParameterValue")
    public static String classNameAtStacktraceLevel(int stacktraceLevel){
        return Thread.currentThread().getStackTrace()[stacktraceLevel].getClassName();
    }

    public static String stringToCapitalInitialCharacterForEachWordAndNoSpaces(String instring){
        StringBuilder stringBuilder = new StringBuilder();
        String[] words = instring.split(" ");
        for(String word : words){
            if(word.length() > 0){
                stringBuilder.append(word.trim().substring(0,1).toUpperCase());
                if(word.length() > 1){
                    stringBuilder.append(word.trim().substring(1).toLowerCase());
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Matches a string with a regex pattern
     * @param instring the string to match
     * @param pattern the regex pattern to match the string against
     * @return Return true if match is found
     */
    public static boolean isRegexMatch(String instring, String pattern){
        if(pattern == null || instring == null) return false;
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(instring);
        return m.matches();
        //return instring.contains(pattern);
    }

    /**
     * Since java enum elements are written in capital letters (considered being
     * constants) and enum values are parsed as string frequently this method
     * re-formats the enum values to initial capital letter and the rest of the
     * string in lower case - and underscore characters substituted with spaces.
     * @param CAPITALIZED_STRING A string from an enum value
     * @return The modified string, in friendlier text format
     */
    public static String enumCapitalNameToFriendlyString(String CAPITALIZED_STRING){
        return CAPITALIZED_STRING.substring(0, 1).toUpperCase() +
                CAPITALIZED_STRING.substring(1).replace('_', ' ').toLowerCase();
    }

    /**
     * Starts a program.
     *
     * @param program Program path
     * @param arguments Arguments to pass to the program
     */
    public static void startProgram(String program, List<String> arguments){
        ApplicationManager am = new ApplicationManager(new TestCase(null, "dummyTestCase"));
        am.startProgram(program, arguments);
    }

    /**
     * Starts a program.
     *
     * @param programPathAndArgumentsString Program name, and path, and arguments.
     */
    public static void startProgram(String programPathAndArgumentsString, TestCase testCase){
        ApplicationManager am = new ApplicationManager(testCase);
        am.startProgram(programPathAndArgumentsString);
    }

    /**
     * Read file content of specified file
     *
     * @param filePath The path to the file to read.
     * @return The text content of the file.
     */
    public static String getFileContent(String filePath){
        String fileContent = null;
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            fileContent = sb.toString();
        } catch (FileNotFoundException fnfe) {
            logger.error( "Could not read content of file '" + filePath + "'. It does not seem to be found.", fnfe );
        } catch (IOException ioe) {
            logger.error( "Could not get file content from file '" + filePath + "'. " + ioe.getMessage() );
        }
        return fileContent;
    }

    /**
     * Method naming should only consist of method name safe characters, and be formatted according to method naming conventions in java, and according to coding guidelines.
     *
     * @param instring The string to convert
     * @return Returns the converted string
     */
    public static String methodNameWithOnlySafeCharacters(String instring){
        if(instring == null || instring.length() < 1){
            return "";
        }
        instring = instring.trim();
        String returnString = "";

        for(String spaceDividedWord : instring.split(" ")){
            for(String dashDividedWord : spaceDividedWord.split("-")){
                for(String underscoreDividedWord : dashDividedWord.split("_")){
                    returnString += firstUpperLetterTrailingLowerLetter(underscoreDividedWord);
                }

            }
        }

        returnString = returnString.replaceAll("--", "-").
                replace(" ", "").
                replace(",", "").
                replace("–", "_").
                replace(".", "_").
                replace("%", "").
                replace("&", "Proc").
                replace("$", "Dollar").
                replace("£", "Pound").
                replace("€", "Euro").
                replace("\\", "_").
                replace("\"", "").
                replace("'", "").
                replace("!", "").
                replace("?", "").
                replace("é", "e").
                replace("è", "e").
                replace("-", "_").
                replace("*", "_").
                replace("+", "Plus").
                replace("©", "Copyright").
                replace("å", "a").
                replace("ä", "a").
                replace("|", "_").
                replace("ö", "o").
                replace("Å", "A").
                replace("Ä", "A").
                replace("=", "").
                replace("@", "At").
                replace("/", "_").
                replace("(", "_").
                replace(")", "_").
                replace(";", "").
                replace("^", "_").
                replace(":", "").
                replace("__", "_").
                replace("Ö", "O");


        if(Character.isDigit(returnString.charAt(0))){
            returnString = "_" + returnString;
        } //Method names cannot start with digits
        return returnString;
    }

    public static String firstUpperLetterTrailingLowerLetter(String instring){
        if(instring == null || instring.length() < 1) return "";
        String returnString;
        returnString = instring.substring(0,1).toUpperCase();
        if(instring.length() > 1){
            returnString += instring.substring(1).toLowerCase();
        }
        return returnString;

    }
}
