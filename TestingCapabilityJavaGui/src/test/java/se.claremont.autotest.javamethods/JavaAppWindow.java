package se.claremont.autotest.javamethods;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by jordam on 2017-02-19.
 */
public class JavaAppWindow {


    @Test
    @Ignore
    public void test(){
        JavaWindow window = new JavaWindow("Java test application");
        JavaAwtAppWithSomeSwingComponents javaApp = new JavaAwtAppWithSomeSwingComponents();
        window.mapWindowToDescriptionClass("C:\\Temp\\OutputFile.txt");

    }
}
