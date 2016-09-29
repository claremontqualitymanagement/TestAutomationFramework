package se.claremont.autotest.support;

import org.junit.Assert;
import se.claremont.tools.Utils;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A collection of mainly static methods that are used in several places within the framework
 *
 * Created by jordam on 2016-08-17.
 */
public class SupportMethods {

    /**
     * Saves text based content to a file. If the file folder doesn't exist it is created.
     * @param content test based file content
     * @param filePath file path as a string
     */
    public static void saveToFile(String content, String filePath){
        Writer writer;

        if(filePath == null ){
            System.out.println("Could not write file to null file path.");
            return;
        }
        System.out.println("Writing file content to '" + filePath + "'.");
        if(content == null){
            System.out.println("Warning! Attempting to write a null string to file '." + filePath + "' Replacing null with empty string.");
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
}
