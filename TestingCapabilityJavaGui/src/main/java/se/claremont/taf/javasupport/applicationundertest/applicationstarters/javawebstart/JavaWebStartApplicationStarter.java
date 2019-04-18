package se.claremont.taf.javasupport.applicationundertest.applicationstarters.javawebstart;

import se.claremont.taf.javasupport.applicationundertest.ChildFirstURLClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class to start Java Web Start based applications from within TAF, to enable interaction with those.
 *
 * Created by jordam on 2017-02-25.
 */
public class JavaWebStartApplicationStarter {
    private URLClassLoader tafClassLoader;
    private ChildFirstURLClassLoader testApplicationClassLoader;

    public JavaWebStartApplicationStarter(){
        tafClassLoader = (URLClassLoader)Thread.currentThread().getContextClassLoader();
    }

    public void startApplication(String localJarFileStorageFolder, String mainClassName, String[] args){
        for(String fileName : fileNamesInFolder(localJarFileStorageFolder)){
            try {
                System.out.println("Adding url '" + fileName + "'");
                //prepareJar(fileName);
                addURL(new File(fileName).toURI().toURL());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        invokeMethodOfClass(mainClassName, "main", args);
    }

    private List<String> fileNamesInFolder(String foldername){
        List<String> filenames = new ArrayList<>();
        File folder = new File(foldername);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) return filenames;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                System.out.println("File " + listOfFile.getAbsolutePath());
                filenames.add(listOfFile.getAbsolutePath());
            } else if (listOfFile.isDirectory()) {
                System.out.println("Directory " + listOfFile.getName());
            }
        }
        return filenames;
    }

    private void prepareJar(String pathToJar){
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(pathToJar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = new URL[0];

        try {
            urls = new URL[]{new URL("jar:file:" + pathToJar+"!/")};
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        ChildFirstURLClassLoader cl = (ChildFirstURLClassLoader) tafClassLoader.newInstance(urls);
        //ParentLastURLClassLoader cl = new ParentLastURLClassLoader(urls);

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0,je.getName().length()-6);
            className = className.replace('/', '.');
            try {
                Class c = cl.loadClass(className);
            } catch (ClassNotFoundException e1) {
                System.out.println(e1.toString());
            }

        }
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

    private void invokeMethodOfClass(String className, String methodName, String[] args) {
        try {
            URLClassLoader classLoader
                    = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Class e = classLoader.loadClass(className);
            @SuppressWarnings("unchecked") Method m = e.getMethod(methodName, args.getClass());
            m.setAccessible(true);
            int mods = m.getModifiers();
            if(m.getReturnType() != Void.TYPE || !Modifier.isStatic(mods) || !Modifier.isPublic(mods)) {
                return;
            }

            try {
                //noinspection RedundantCast
                m.invoke((Object)null, new Object[]{args});
            } catch (IllegalAccessException e1) {
                System.out.println(e1.toString());
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println(e.toString());
        }

    }
}