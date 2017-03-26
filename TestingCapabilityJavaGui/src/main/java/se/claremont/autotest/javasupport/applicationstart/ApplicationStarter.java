package se.claremont.autotest.javasupport.applicationstart;

import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.jar.Attributes;

/**
 * Helps start Java applications from within TAF, so they end up in same JVM, and hence stand a
 * better chance of being possible to interact with.
 *
 * Created by jordam on 2017-02-10.
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public class ApplicationStarter {

    JarClassLoader classLoader;
    URL url;
    String mainClass;
    TestCase testCase;

    public ApplicationStarter(TestCase testCase){
        this.testCase = testCase;
    }

    public void startJar(String filePath){
        filePath = filePath.replace("\\", "/");
        ArrayList<String> usableParts = new ArrayList<>();
        String[] parts = filePath.split(" ");
        for(String part:parts)
            if(part.trim().length() > 0) usableParts.add(part.trim());
        if(usableParts.size() == 0){
            log(LogLevel.EXECUTION_PROBLEM, "Cannot start application without any file path.");
            return;
        }
        URL fileUrl = null;
        if(usableParts.size() > 0){
            try {
                String url = usableParts.get(0);
                if(!url.startsWith("file:///") && !url.startsWith("http://") && !url.startsWith("https://"))
                    url = "file:///" + url;
                fileUrl = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        if(usableParts.size() == 1){
            startJar(fileUrl);
        } else {
            String[] args = new String[usableParts.size()-1];
            for(int i = 1; i < usableParts.size(); i++){
                args[i-1] = usableParts.get(i);
            }
            startJar(fileUrl, args);
        }

    }

    public void startJar(URL url){
        log(LogLevel.DEBUG, "Starting jar file '" + url.toString() + "' from jar main class.");
        classLoader = new JarClassLoader(url);
        this.url = url;
        mainClass = getMainClassName();
        invokeMainMethodOfMainClass(mainClass);
    }

    public void startJar(URL url, String[] args){
        log(LogLevel.DEBUG, "Starting jar file '" + url.toString() + "' from jar main class.");
        classLoader = new JarClassLoader(url);
        this.url = url;
        mainClass = getMainClassName();
        invokeMainMethodOfMainClass(mainClass);
    }

    public void startJar(URL url, String mainClass){
        log(LogLevel.DEBUG, "Starting jar file '" + url.toString() + "' from jar with main class '" + mainClass + "'.");
        classLoader = new JarClassLoader(url);
        this.url = url;
        this.mainClass = mainClass;
        invokeMainMethodOfMainClass(mainClass);
    }

    public void startJar(URL url, String mainClass, String[] args){
        log(LogLevel.DEBUG, "Starting jar file '" + url.toString() + "' from jar with main class '" + mainClass + "'.");
        classLoader = new JarClassLoader(url);
        this.url = url;
        this.mainClass = mainClass;
        invokeMethodOfClass(mainClass, "main", args);
    }

    public String getMainClassName(){
        URL u = null;
        try {
            u = new URL("jar", "", url + "!/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JarURLConnection uc = null;
        Attributes attr = null;
        try {
            uc = (JarURLConnection)u.openConnection();
            attr = uc.getMainAttributes();
        } catch (IOException e) {
            log(LogLevel.EXECUTION_PROBLEM, "Problem opening jar file:" + e.toString());
        }
        String returnString = attr != null
                ? attr.getValue(Attributes.Name.MAIN_CLASS)
                : null;
        log(LogLevel.DEBUG, "Found main class '" + returnString + "'.");
        return returnString;
    }

    public void invokeMainMethodOfMainClass(String mainClassName){
        invokeMethodOfClass(mainClassName, "main", new String[]{});
    }

    public void invokeMethodOfClass(String className, String methodName, String[] args) {
        try{
            Class c = classLoader.loadClass(className);
            log(LogLevel.DEBUG, "Loaded class '" + className + "'.");
            @SuppressWarnings("unchecked") Method m = c.getMethod(methodName, args.getClass());
            log(LogLevel.DEBUG, "Found method 'main'.");
            m.setAccessible(true);
            int mods = m.getModifiers();
            if (m.getReturnType() != void.class || !Modifier.isStatic(mods) ||
                    !Modifier.isPublic(mods)) {
                log(LogLevel.EXECUTION_PROBLEM, "Could not find any '" + methodName + "' method in class '" + className + "'.");
                return;
            }
            try {
                m.invoke(null, new Object[] { args });
                log(LogLevel.DEBUG, "Invoked method '" + methodName + "'.");
            } catch (IllegalAccessException e) {
                // This should not happen, as we have disabled access checks
            }
        } catch (ClassNotFoundException e) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not identify class '" + mainClass + "' in '" + url.toString() + "'. Error: " + e.getMessage());
        } catch (InvocationTargetException e) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not invoke class '" + mainClass + "'. Error: " + e.getMessage());
        } catch (NoSuchMethodException e) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not find method '" + methodName + "' in class '" + mainClass + "'. Error: " + e.getMessage());
        }
    }

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
            log(LogLevel.INFO, "Found more than one window. Returning the last one. Consider using the enumerator method implementation for specific window.");
            return windows.get(0);
        }
        return null;
    }

    public static ArrayList<Window> getWindows(){
        ArrayList<Window> windows = new ArrayList<>();
        Window [] swingWindows = Window.getOwnerlessWindows ();
        Collections.addAll(windows, swingWindows);
        return windows;
    }

    public static void closeAllWindows(){
        for(Window window : getWindows()){
            closeSubWindows(window);
        }
    }

    private static void closeSubWindows(Window window){
        Window[] subWindows = window.getOwnedWindows();
        for(Window w : subWindows){
            closeSubWindows(w);
        }
        window.dispose();
    }

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

    private void log(LogLevel logLevel, String message){
        if(testCase == null){
            System.out.println(message);
        } else {
            testCase.log(logLevel, message);
        }
    }

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
        testCase.log(LogLevel.INFO, logMessage.toString());
    }

    class JarClassLoader extends URLClassLoader{
        URL url;

        public JarClassLoader(URL url) {
            super(new URL[] { url });
            this.url = url;
        }
    }
}
