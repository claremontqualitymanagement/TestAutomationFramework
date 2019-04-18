package se.claremont.taf.javasupport.applicationundertest.applicationstarters;

import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

import java.awt.*;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.Attributes;

/**
 * Helps start Java applications from within TAF, so they end up in same JVM, and hence stand a
 * better chance of being possible to interact with.
 *
 * Created by jordam on 2017-02-10.
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public class ApplicationStarter extends ApplicationStartMechanism{
    URL url;

    public ApplicationStarter(TestCase testCase){
        super(testCase);
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
        startUrlOrPathToJarFile = url.toString();
        mainClass = getMainClassName();
        invokeMainMethodOfMainClassWithArguments();
    }

    public void startJar(URL url, String[] args){
        log(LogLevel.DEBUG, "Starting jar file '" + url.toString() + "' from jar main class.");
        classLoader = new JarClassLoader(url);
        this.url = url;
        startUrlOrPathToJarFile = url.toString();
        mainClass = getMainClassName();
        arguments = new ArrayList<>();
        for(String arg : args){
            arguments.add(arg);
        }
        invokeMainMethodOfMainClassWithArguments();
    }

    public void startJar(URL url, String mainClass){
        log(LogLevel.DEBUG, "Starting jar file '" + url.toString() + "' from jar with main class '" + mainClass + "'.");
        classLoader = new JarClassLoader(url);
        this.url = url;
        this.startUrlOrPathToJarFile = url.toString();
        this.mainClass = mainClass;
        invokeMainMethodOfMainClassWithArguments();
    }

    public void startJar(URL url, String mainClass, String[] args) throws SecurityException{
        log(LogLevel.DEBUG, "Starting jar file '" + url.toString() + "' from jar with main class '" + mainClass + "'.");
        classLoader = new JarClassLoader(url);
        this.url = url;
        startUrlOrPathToJarFile = url.toString();
        this.mainClass = mainClass;
        arguments = new ArrayList<>();
        for(String arg : args){
            arguments.add(arg);
        }
        MethodInvoker.invokeMethodOfClass(classLoader, testCase, mainClass, "main", args);
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

    public void invokeMainMethodOfMainClassWithArguments(){
        MethodInvoker.invokeMethodOfClass(classLoader, testCase, mainClass, "main", (String[])arguments.toArray());
    }

    public static Window[] getWindows(){
        return Window.getOwnerlessWindows ();
    }

    public Window getWindow(){
        Window[] windows = getWindows();
        if(windows.length == 0)return null;
        return windows[0];
    }


    private void log(LogLevel logLevel, String message){
        if(testCase == null){
            System.out.println(message);
        } else {
            testCase.log(logLevel, message);
        }
    }


    class JarClassLoader extends URLClassLoader{
        URL url;

        public JarClassLoader(URL url) {
            super(new URL[] { url });
            this.url = url;
        }
    }
}
