package se.claremont.autotest.javasupport.interaction;

import org.junit.Assume;

import java.awt.*;

/**
 * Created by jordam on 2017-02-25.
 */
public class JavaTestApplicationRunner {
    public static final JavaAwtAppWithSomeSwingComponents javaApp = new JavaAwtAppWithSomeSwingComponents();

    public static void showWindow(){
        Assume.assumeTrue("Cannot start GUI interface since there is no desktop.", Desktop.isDesktopSupported());
        try{
            javaApp.show();
        }catch (Exception e){
            Assume.assumeTrue("Cannot start GUI interface since there is no desktop available. Error: " + System.lineSeparator() + e.toString(), false);
        }
    }

    public static void hideWindow(){
        javaApp.hide();
    }

    public void exitApplication(){
        javaApp.removeAll();
        javaApp.dispose();
    }
}
