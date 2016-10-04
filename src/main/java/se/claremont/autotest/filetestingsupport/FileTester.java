package se.claremont.autotest.filetestingsupport;

import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.support.SupportMethods;

import java.io.*;

/**
 * Created by jordam on 2016-10-04.
 */
public class FileTester {
    TestCase testCase;

    public FileTester(TestCase testCase){
        this.testCase = testCase;
    }

    public void verifyFileContentMatchesRegex(String filePath, String regexPattern){
        if(!fileExist(filePath)) {
            testCase.log(LogLevel.VERIFICATION_PROBLEM, "Could not verify regex pattern '" + regexPattern + "' matched file content in file '" + filePath + "'. File doesn't seem to exist.");
            return;
        }
        String fileContent = getFileContent(filePath);
        if(SupportMethods.isRegexMatch(fileContent, regexPattern)){
            testCase.log(LogLevel.VERIFICATION_PASSED, "The regex pattern '" + regexPattern + "' returned a match for file content of file '" + filePath + "'.");
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "The regex pattern '" + regexPattern + "' could not be matched for file content of file '" + filePath + "'.");
            testCase.log(LogLevel.DEVIATION_EXTRA_INFO, "File content: " + SupportMethods.LF + fileContent);
        }
    }

    public boolean fileExist(String filePath) {
        File f = new File(filePath);
        if(f.exists() && !f.isDirectory()) {
            testCase.log(LogLevel.DEBUG, "Checked if file '" + filePath + "' existed (and that it isn't a directory), and it does. ");
            return true;
        } else {
            testCase.log(LogLevel.DEBUG, "Checked if file '" + filePath + "' existed (and that it isn't a directory), and it doesn't. ");
            return false;
        }
    }

    public boolean directoryExist(String directoryPath) {
        File f = new File(directoryPath);
        if(f.exists() && f.isDirectory()) {
            testCase.log(LogLevel.DEBUG, "Checked if directory '" + directoryPath + "' existed (and that it isn't a file), and it does. ");
            return true;
        } else {
            testCase.log(LogLevel.DEBUG, "Checked if directory '" + directoryPath + "' existed (and that it isn't a file), and it doesn't. ");
            return false;
        }
    }

    public void verifyFileExists(String filePath){
        if(fileExist(filePath)){
            testCase.log(LogLevel.VERIFICATION_PASSED, "File '" + filePath + "' exists, as expected.");
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "File '" + filePath + "' does not seem to exist, but was expected to exist.");
        }
    }

    public void verifyDirectoryExists(String directoryPath){
        if(directoryExist(directoryPath)){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Directory '" + directoryPath + "' exists, as expected.");
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "Directory '" + directoryPath + "' does not seem to exist, but was expected to exist.");
        }
    }

    public String getFileContent(String filePath){
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
        } catch (FileNotFoundException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not read content of file '" + filePath + "'. It does not seem to be found.");
        } catch (IOException e) {
            testCase.log(LogLevel.FRAMEWORK_ERROR, "Could not get file content from file '" + filePath + "'. " + e.getMessage());
        }
        return fileContent;
    }
}
