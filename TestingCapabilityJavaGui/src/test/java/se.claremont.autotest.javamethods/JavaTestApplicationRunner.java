package se.claremont.autotest.javamethods;

import org.junit.Assume;

import java.awt.*;

/**
 * Created by jordam on 2017-02-25.
 */
public class JavaTestApplicationRunner {
    public static final JavaAwtAppWithSomeSwingComponents javaApp = new JavaAwtAppWithSomeSwingComponents();

    public static void showWindow(){
        Assume.assumeTrue("Cannot start GUI interface since there is no desktop.", Desktop.isDesktopSupported());
        javaApp.show();
    }

    public static void hideWindow(){
        javaApp.hide();
    }

    public void exitApplication(){
        javaApp.removeAll();
        javaApp.dispose();
    }
}
