package se.claremont.taf.javasupport.interaction;

import org.junit.Assume;

import java.awt.*;

/**
 * Tests of Java program start class
 *
 * Created by jordam on 2017-02-25.
 */
public class JavaTestApplicationRunner {
    public static JavaAwtAppWithSomeSwingComponents javaApp = null;
    final static boolean desktopSupported = Desktop.isDesktopSupported();

    public static void tryStart(){
        if(javaApp == null && desktopSupported){
            System.out.println("Desktop is supported. Starting 'Java Test Application'.");
            try{
                javaApp = new JavaAwtAppWithSomeSwingComponents();
            } catch (Exception e){
                Assume.assumeTrue("Could not start 'Java Test Application'. Prevention exist: " + e.toString(), false);
            }
        }else if(!desktopSupported){
            Assume.assumeTrue("No desktop available. Skipping start of 'Java Test Application'", false);
        }
    }

    public static void showWindow(){
        if(javaApp == null) return;
        Assume.assumeTrue("Cannot start GUI interface since there is no desktop.", Desktop.isDesktopSupported());
        try{
            //noinspection deprecation
            javaApp.setVisible(true);
        }catch (Exception e){
            Assume.assumeTrue("Cannot start GUI interface since there is no desktop available. Error: " + System.lineSeparator() + e.toString(), false);
        }
    }

    public static void hideWindow(){
        if(javaApp == null) return;
        //noinspection deprecation
        javaApp.hide();
    }

    public static void exitApplication(){
        if(javaApp == null)return;
        javaApp.removeAll();
        javaApp.dispose();
    }

}
