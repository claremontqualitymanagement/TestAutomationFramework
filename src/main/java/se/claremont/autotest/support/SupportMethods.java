package se.claremont.autotest.support;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

}
