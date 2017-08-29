package se.claremont.autotest.javasupport.applicationundertest.applicationcontext;

import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LibraryLoader {
    private URLClassLoader tafClassLoader;
    TestCase testCase;

    public LibraryLoader(TestCase testCase){
        this.testCase = testCase;
        tafClassLoader = (URLClassLoader)Thread.currentThread().getContextClassLoader();
    }

    public void loadLibrary(String path){
        if(path == null){
            log(LogLevel.EXECUTION_PROBLEM, "Cannot load null library.");
            return;
        }
        if(path.length() == 0){
            log(LogLevel.EXECUTION_PROBLEM, "Cannot load library from empty path.");
            return;
        }
        if(!Files.exists(Paths.get(path))){
            log(LogLevel.EXECUTION_PROBLEM, "Cannot find any file at path '" + path + "'.");
            return;
        }
        if(Files.isDirectory(Paths.get(path))){
            log(LogLevel.EXECUTION_PROBLEM, "The path '" + path + "' is a directory. Use method loadAllLibrariesInFolder() instead.");
            return;
        }
        if(path.toLowerCase().endsWith(".jar")){
            addJarFileToClassPath(path);
            return;
        } else if(path.toLowerCase().endsWith(".dll")){
            loadDll(path);
            return;
        }
        log(LogLevel.EXECUTION_PROBLEM, "File at path '" + path + "' is neighter a jar file or a DLL. No attempts to load the file.");
    }

    public void loadAllLibrariesInFolder(String path){
        if(path == null){
            log(LogLevel.EXECUTION_PROBLEM, "Cannot load libraries from null folder.");
            return;
        }
        if(path.length() == 0){
            log(LogLevel.EXECUTION_PROBLEM, "Cannot load libraries from empty folder path.");
            return;
        }
        if(!Files.exists(Paths.get(path))){
            log(LogLevel.EXECUTION_PROBLEM, "Cannot find any folder or file at path '" + path + "'.");
            return;
        }
        if(!Files.isDirectory(Paths.get(path))){
            log(LogLevel.INFO, "The path '" + path + "' is not a directory. Loading it as a file.");
            loadLibrary(path);
            return;
        }
        loadAllDllsInFolder(path);
        addFolderToClassPath(path);
    }

    /**
     * Attempts to load a Windows DLL (Dynamic Link Library) file in order for a Java program to
     * be able to access the classes and methods within.
     *
     * DLL files are sensitive to bit length. Not all DLL compiled for 32 bits can be run in a 64 bit environment.
     *
     * @param path The file path to the DLL file
     */
    public void loadDll(java.lang.String path){
        if(path == null || path.length() == 0 || !path.toLowerCase().endsWith(".dll")){
            log(LogLevel.EXECUTION_PROBLEM, "The file '" + path + "' does not seem to be a DLL file. Loading of this file aborted. If you still want to load this file use the tryLoadLibrary() method.");
            return;
        }
        try {
            System.load(path);
            log(LogLevel.EXECUTED, "Successfully loaded code library '" + path + "'.");
        } catch (UnsatisfiedLinkError e) {
            log(LogLevel.EXECUTION_PROBLEM, "Native code library failed to load. Error: " + System.lineSeparator() + e.toString());
        }
    }

    /**
     * Attempts to load the identified Windows DLL (Dynamic Link Library) files in the specified folder in order for a Java program to
     * be able to access the classes and methods within.
     *
     * DLL files are sensitive to bit length. Not all DLL compiled for 32 bits can be run in a 64 bit environment.
     *
     * @param path The path to the folder to add all DLLs in.
     */
    public void loadAllDllsInFolder(java.lang.String path){
        for(String fileName : fileNamesInFolder(path)){
            if(path == null || path.length() == 0 || !path.toLowerCase().endsWith(".dll") || Files.isDirectory(Paths.get(fileName)))continue;
            try {
                System.load(fileName);
                log(LogLevel.EXECUTED, "Successfully loaded code library '" + path + "'.");
            } catch (UnsatisfiedLinkError e) {
                log(LogLevel.EXECUTION_PROBLEM, "Native code library failed to load. Error: " + System.lineSeparator() + e.toString());
            }
        }
    }

    public void tryLoadLibrary(java.lang.String path){
        if(path.contains("WFAPILink.dll"))return;
        if(path == null || path.length() == 0 || Files.isDirectory(Paths.get(path))){
            log(LogLevel.EXECUTION_PROBLEM, "Cannot load file since file path is empty.");
            return;
        }
        try {
            System.load(path);
            log(LogLevel.EXECUTED, "Successfully loaded code library '" + path + "'.");
        } catch (UnsatisfiedLinkError e) {
            log(LogLevel.EXECUTION_PROBLEM, "Native code library failed to load." + System.lineSeparator() + e.toString());
        }
    }

    public void addJarFileToClassPath(String filePath){
        try {
            testCase.log(LogLevel.EXECUTED, "Adding file '" + filePath + "' to classpath.");
            addURL(new File(filePath).toURL());
        } catch (Exception e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not add file '" + filePath + "' to classpath. Error: " + e.getMessage());
        }
    }

    public void addFolderToClassPath(String folderPath){
        for(String fileName : fileNamesInFolder(folderPath)){
            if(Files.isDirectory(Paths.get(fileName)) || !fileName.toLowerCase().endsWith(".jar")) continue;
            try {
                testCase.log(LogLevel.EXECUTED, "Simulating adding file '" + fileName + "' to classpath by loading it in ClassLoader.");
                addURL(new File(fileName).toURL());
            } catch (Exception e) {
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not add file '" + fileName + "' to classpath. Error: " + e.getMessage());
            }
        }
    }



    private List<String> fileNamesInFolder(String foldername){
        List<String> filenames = new ArrayList<>();
        File folder = new File(foldername);
        File[] listOfFiles = folder.listFiles();
        List<String> nonJarFiles = new ArrayList<>();
        List<String> subDirectories = new ArrayList<>();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()){
                if(listOfFile.getName().endsWith(".jar")) {
                    filenames.add(listOfFile.getAbsolutePath());
                } else {
                    nonJarFiles.add(listOfFile.getAbsolutePath());
                }
            } else if (listOfFile.isDirectory()) {
                subDirectories.add(listOfFile.getName());
            }
        }
        StringBuilder logMessage = new StringBuilder();
        if(filenames.size() == 0){
            logMessage.append("Could not find any jar files in folder '" + foldername + "'.").append(System.lineSeparator());
        } else {
            logMessage.append("Found the following jar files in folder '" + foldername + "':").append(System.lineSeparator()).append(String.join(System.lineSeparator() + " * ", filenames)).append(System.lineSeparator());
        }
        if(subDirectories.size() > 0){
            logMessage.append("Found the following sub-directories in the folder:").append(System.lineSeparator()).append(String.join(System.lineSeparator() + " * ", subDirectories)).append(System.lineSeparator());
        }
        if(nonJarFiles.size() > 0){
            logMessage.append("Found the following non-jar-files in the folder:").append(System.lineSeparator()).append(String.join(System.lineSeparator() + " * ", nonJarFiles)).append(System.lineSeparator());
        }
        testCase.log(LogLevel.INFO, logMessage.toString());

        if(nonJarFiles.size() == 0) return filenames;

        LibraryLoader libraryLoader = new LibraryLoader(testCase);
        for(String nonJarFile : nonJarFiles){
            libraryLoader.tryLoadLibrary(nonJarFile);
        }
        return filenames;
    }


    @SuppressWarnings("unchecked")
    private void addURL(URL url) throws Exception {
        URLClassLoader classLoader
                = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class clazz= URLClassLoader.class;
        // Use reflection
        Method method= clazz.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(classLoader, url);
    }

    private void log(LogLevel logLevel, String message){
        if(testCase == null){
            System.out.println(logLevel.toString() + ": " + message);
        } else {
            testCase.log(logLevel, message);
        }
    }
}
