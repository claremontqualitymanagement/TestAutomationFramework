package se.claremont.autotest.javasupport.applicationundertest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.javasupport.applicationundertest.applicationcontext.ApplicationContextManager;
import se.claremont.autotest.javasupport.applicationundertest.applicationstarters.ApplicationStartMechanism;
import se.claremont.autotest.javasupport.applicationundertest.applicationstarters.ApplicationThreadRunner;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ApplicationUnderTest {
    @JsonProperty public String friendlyName;
    @JsonProperty public ApplicationContextManager context;
    @JsonProperty public ApplicationStartMechanism startMechanism;
    @JsonIgnore TestCase testCase;
    @JsonIgnore static ArrayList<Window> windowsStartedBeforeStartingSut = new ArrayList<>();

    private ApplicationUnderTest(){ //For JSON parsing to work
        TestCase testCase = new TestCase();
        context = new ApplicationContextManager(testCase);
        this.startMechanism = new ApplicationStartMechanism(testCase);
        this.testCase = testCase;
    }

    public ApplicationUnderTest(TestCase testCase, ApplicationStartMechanism startMechanism){
        context = new ApplicationContextManager(testCase);
        this.startMechanism = startMechanism;
        this.testCase = testCase;
    }

    public ApplicationUnderTest(ApplicationUnderTest aut){
        this.testCase = aut.testCase;
        context = new ApplicationContextManager(testCase);
        startMechanism = new ApplicationStartMechanism(testCase);
        this.context.jvmSettings.appliedSetting = new ArrayList<>(aut.context.jvmSettings.appliedSetting);
        this.context.loadedLibraries.appliedFiles = new ArrayList<>(aut.context.loadedLibraries.appliedFiles);
        this.context.properties.appliedProperties = new ArrayList<>(aut.context.properties.appliedProperties);
        this.context.environmentVariables.appliedVariableChanges = new ArrayList<>(aut.context.environmentVariables.appliedVariableChanges);
        this.startMechanism.mainClass = aut.startMechanism.mainClass;
        this.startMechanism.startUrlOrPathToJarFile = aut.startMechanism.startUrlOrPathToJarFile;
        this.startMechanism.arguments = aut.startMechanism.arguments;
    }

    public ApplicationUnderTest(ApplicationStartMechanism startMechanism, ApplicationContextManager context){
        this.context = context;
        this.startMechanism = startMechanism;
    }

    public void setName(String name){
        this.friendlyName = name;
    }

    @JsonIgnore
    public void setProgramArguments(String[] args){
        for(String arg : args){
            startMechanism.arguments.add(arg);
        }
    }

    @JsonIgnore
    public void addProgramArgument(String arg){
        startMechanism.arguments.add(arg);
    }

    @JsonIgnore
    public void setMainClass(String mainClassName){
        startMechanism.mainClass = mainClassName;
    }

    @JsonIgnore
    public void loadLibrary(String path){
        context.loadedLibraries.loadLibrary(path);
    }

    @JsonIgnore
    public void loadAllLibrariesInFolder(String path){
        context.loadedLibraries.loadAllDllsInFolder(path);
    }

    @JsonIgnore
    public void setEnvironmentVariableValue(String variableName, String variableValue){
        context.environmentVariables.setEnvironmentVariable(variableName, variableValue);
    }

    @JsonIgnore
    public void setSystemPropertyValue(String name, String value){
        context.properties.setProperty(name, value);
    }

    @JsonIgnore
    public void attemptToAddJVMSetting(String name, String value){
        context.jvmSettings.setVMOption(name, value);
    }

    @JsonIgnore
    public void setMainJarOrUrl(String jarFilePathOrUrl){
        startMechanism.startUrlOrPathToJarFile = jarFilePathOrUrl;
    }

    @JsonIgnore
    public void start(){
        windowsStartedBeforeStartingSut.addAll(getWindows());
        Thread t = ApplicationThreadRunner.start(startMechanism);

    }

    @JsonIgnore
    public void stop(){
        closeAllWindows();
    }

    @JsonIgnore
    public Object getWindow(){
        ArrayList<Window> windows = getWindows();
        log(LogLevel.DEBUG, "Found " + windows.size()+ " windows in JVM.");
        if(windows.size() == 0) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not find any windows.");
            return null;
        }
        if(windows.size() == 1) {
            log(LogLevel.DEBUG, "Found one window.");
            return windows.get(0);
        }
        if(windows.size()> 1){
            log(LogLevel.INFO, "Found more than one window. Returning the first one. Consider using the enumerator method implementation for specific window.");
            return windows.get(0);
        }
        return null;
    }

    @JsonIgnore
    public static ArrayList<Window> getWindows(){
        ArrayList<Window> windows = new ArrayList<>();
        Window [] swingWindows = Window.getOwnerlessWindows ();
        Collections.addAll(windows, swingWindows);
        return windows;
    }

    @JsonIgnore
    public static ArrayList<Window> getWindowsForSUT(){
        ArrayList<Window> windows = new ArrayList<>();
        Window [] swingWindows = Window.getOwnerlessWindows ();
        Collections.addAll(windows, swingWindows);
        windows.removeAll(windowsStartedBeforeStartingSut);
        return windows;
    }

    @JsonIgnore
    public static void closeAllWindows(){
        for(Window window : getWindows()){
            closeSubWindows(window);
        }
    }

    @JsonIgnore
    private static void closeSubWindows(Window window){
        Window[] subWindows = window.getOwnedWindows();
        for(Window w : subWindows){
            closeSubWindows(w);
        }
        window.dispose();
    }

    @JsonIgnore
    public Object getWindow(int windowCount){
        ArrayList<Window> windows = getWindows();
        log(LogLevel.DEBUG, "Found " + windows.size()+ " windows in JVM.");
        if(windows.size() < windowCount) {
            log(LogLevel.EXECUTION_PROBLEM, "Only found " + windows.size()+ " open windows, and request was for window " + windowCount + ".");
            return null;
        }
        if(windows.size() == 1) {
            log(LogLevel.DEBUG, "Found one window returning this although window count " + windowCount + " was requested.");
            return windows.get(0);
        }
        return windows.get(windowCount);
    }

    @JsonIgnore
    public void logCurrentWindows(){
        logCurrentWindows(testCase);
    }

    @JsonIgnore
    public static void logCurrentWindows(TestCase testCase) {
        StringBuilder logMessage = new StringBuilder("Current active windows:" + System.lineSeparator());
        for(Window w : getWindows()){
            try{
                Frame frame = (Frame) w;
                logMessage.append("Window title: '").append(frame.getTitle()).append("'. Shown:").append(frame.isShowing()).append(System.lineSeparator());
                continue;
            } catch (Exception ignored){
                testCase.log(LogLevel.DEBUG, "Could not retrieve title of Window as Frame. Error: " + ignored.getMessage());
            }
            try{
                JFrame frame = (JFrame) w;
                logMessage.append("Window title: '").append(frame.getTitle()).append("'. Shown:").append(frame.isShowing()).append(System.lineSeparator());
                continue;
            } catch (Exception ignored){
                testCase.log(LogLevel.DEBUG, "Could not retrieve title of Window as JFrame. Error: " + ignored.getMessage());
            }
            logMessage.append("Could not find title of element of class '").append(w.getClass().toString()).append("' since it is not implemented");
        }
        if(testCase == null){
            System.out.println(logMessage.toString());
        } else {
            testCase.log(LogLevel.INFO, logMessage.toString());
        }
    }

    @JsonIgnore
    public String saveToJsonFile(String filePath){
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(filePath), this);
        } catch (IOException e) {
            return e.toString();
        }
        return "ok";
    }

    @JsonIgnore
    public static ApplicationUnderTest readFromJsonFile(String filePath){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(filePath), ApplicationUnderTest.class);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        TestCase testCase = new TestCase();
        return new ApplicationUnderTest(testCase, new ApplicationStartMechanism(testCase));
    }

    @JsonIgnore
    private void log(LogLevel logLevel, String message){
        if(testCase == null){
            System.out.println(message);
        } else {
            testCase.log(logLevel, message);
        }
    }

}
