package se.claremont.taf.javasupport.applicationundertest.applicationstarters;

import se.claremont.taf.logging.LogLevel;
import se.claremont.taf.testcase.TestCase;
import se.claremont.taf.javasupport.applicationundertest.applicationcontext.ApplicationContextManager;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ApplicationStartMechanismCli extends ApplicationStartMechanism {
    private ApplicationContextManager context;

    public ApplicationStartMechanismCli(TestCase testCase) {
        super(testCase);
        context = new ApplicationContextManager(testCase);
    }

    public void startProgramFromCLI(String cliStartString){
        testCase.log(LogLevel.DEBUG, "Attempting to start Java program from CLI string '" + cliStartString + "'.");
        if(cliStartString == null || cliStartString.length() == 0){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not start jar from empty string.");
            return;
        }
        String[] parts = cliStartString.split(" ");
        parts = applyJVMSettings(parts);
        parts = applyEnvironmentVariables(parts);
        parts = applyClassPath(parts);
        parts = removeJavaProgramStatement(parts);
        String jarFile = identifyJarFile(parts);
        parts = removeJarFileParameterFromPars(parts);
        if(jarFile == null){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not identify jar start file in CLI string '" + cliStartString + "'.");
            return;
        }
        try {
            testCase.log(LogLevel.DEBUG, "Starting jar file '" + jarFile + "' with arguments '" + String.join(" ", parts) + "'.");
            //startJar(new File(jarFile).toURI().toURL(), parts);
            MethodInvoker.invokeMethodOfClass(classLoader, testCase, mainClass, "main", parts);
        } catch (Exception e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Error encountered while trying to start jar file. Error: " + e.getMessage());
        }
    }

    private String[] removeJarFileParameterFromPars(String[] parts) {
        List<String> remainingParts = new ArrayList<>();
        for(int i = 0; i < parts.length;i++){
            String part = parts[i];
            if(part.trim().toLowerCase().equals("-jar")){
                if(parts.length > i) {
                    i++;
                }
            } else {
                remainingParts.add(part);
            }
        }
        return remainingParts.toArray(new String[0]);
    }

    private String identifyJarFile(String[] parts) {
        for(int i = 0; i < parts.length; i++){
            String part = parts[i];
            if(part.trim().toLowerCase().equals("-jar") && parts.length > i){
                startUrlOrPathToJarFile = parts[i+1].trim();
                return parts[i+1];
            }
        }
        return null;
    }

    /**
     * Unfortunately will not yet cope with java.exe paths stated with spaces in the folder names.
     *
     * @param parts The arguments
     * @return Returns the rest of the arguments.
     */
    private String[] removeJavaProgramStatement(String[] parts) {
        List<String> remainingParts = new ArrayList<>();
        for(String part : parts){
            if(
                    part.trim().toLowerCase().equals("java") ||
                            part.trim().toLowerCase().equals("javaw") ||
                            part.trim().toLowerCase().endsWith(File.separator + "javaw") ||
                            part.trim().toLowerCase().endsWith(File.separator + "java") ||
                            part.trim().toLowerCase().equals("java.exe") ||
                            part.trim().toLowerCase().equals("javaw") ||
                            part.trim().toLowerCase().endsWith(File.separator + "javaw.exe") ||
                            part.trim().toLowerCase().endsWith(File.separator + "java.exe")){
                testCase.log(LogLevel.DEBUG, "Removed JRE argument from CLI path since JRE is managed through ClassLoaders.");
            }else{
                remainingParts.add(part);
            }
        }
        return remainingParts.toArray(new String[0]);
    }

    private String[] applyClassPath(String[] parts) {
        List<String> remainingParts = new ArrayList<>();
        for(int i = 0; i < parts.length; i++){
            String part = parts[i];
            if(part.trim().toLowerCase().equals("-cp") || part.trim().toLowerCase().equals("-classpath")){
                if(parts.length == i){
                    testCase.log(LogLevel.EXECUTION_PROBLEM, "Expecting java classpath since " + part + " was stated. None found.");
                }
                String[] classPaths = parts[i+1].split(File.pathSeparator);
                for(String classPath : classPaths){
                    if(Files.isDirectory(new File(classPath).toPath())){
                        context.loadedLibraries.addFolderToClassPath(classPath);
                    }else {
                        context.loadedLibraries.addJarFileToClassPath(classPath);
                    }
                }
                i++;
            } else {
                remainingParts.add(parts[i]);
            }
        }
        return remainingParts.toArray(new String[0]);
    }
    private String[] applyEnvironmentVariables(String[] parts) {
        List<String> remainingParts = new ArrayList<>();
        for(String part : parts){
            if(part.trim().startsWith("-D")){
                String[] setting = part.trim().split("=");
                String settingName = setting[0].trim().substring(2);
                String settingValue = "";
                if(setting.length>1){
                    for(int i = 1; i < setting.length; i++){
                        settingValue += setting[i] + "=";
                    }
                    settingValue = settingValue.substring(0, settingValue.length()-1);
                }
                context.environmentVariables.setEnvironmentVariable(settingName, settingValue);
            } else {
                remainingParts.add(part);
            }
        }
        return remainingParts.toArray(new String[0]);
    }

    private String[] applyJVMSettings(String[] parts) {
        List<String> remainingParts = new ArrayList<>();
        for(String part : parts){
            if(part.trim().startsWith("-X")){
                String[] setting = part.trim().split("=");
                String settingName = setting[0].trim().substring(2);
                String settingValue = "";
                if(setting.length>1){
                    for(int i = 1; i < setting.length; i++){
                        settingValue += setting[i] + "=";
                    }
                    settingValue = settingValue.substring(0, settingValue.length()-1);
                }
                context.jvmSettings.setVMOption(settingName, settingValue);
            } else {
                remainingParts.add(part);
            }
        }
        return remainingParts.toArray(new String[0]);
    }

}
