package se.claremont.autotest.filetestingsupport;

import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testcase.TestCase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2016-10-04.
 */
@SuppressWarnings("WeakerAccess")
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

    /**
     * Return a list of the files matching the file names provided, in the root folder and its sub-folders.
     *
     * @param rootFolder The root folder where search should be performed. Search includes sub-folders.
     * @param fileNamesToFind File names to find. Exact matches.
     * @return Returns a list of the files matching the query.
     */
    public static List<File> searchForSpecificFiles(File rootFolder, List<String> fileNamesToFind) {
        List<File> matchingFiles = new ArrayList<>();
        if(rootFolder == null || fileNamesToFind == null) { return matchingFiles; }
        if(rootFolder.isDirectory()) {
            for(File file : rootFolder.listFiles()) {
                try{
                    matchingFiles.addAll(searchForSpecificFiles(file, fileNamesToFind));
                }catch (Exception ignore){

                }
            }
        } else if(rootFolder.isFile() && fileNamesToFind.contains(rootFolder.getName())) {
            matchingFiles.add(rootFolder);
        }
        return matchingFiles;
    }

    public static List<File> searchForSpecificFiles(String rootFolder, List<String> fileNamesToFind){
        return searchForSpecificFiles(new File(rootFolder), fileNamesToFind);
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
