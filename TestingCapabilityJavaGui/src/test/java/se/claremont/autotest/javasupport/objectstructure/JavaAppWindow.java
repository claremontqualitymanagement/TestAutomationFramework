package se.claremont.autotest.javasupport.objectstructure;

import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.common.testset.UnitTestClass;
import se.claremont.autotest.javasupport.interaction.JavaAwtAppWithSomeSwingComponents;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

/**
 * Created by jordam on 2017-02-19.
 */
public class JavaAppWindow extends UnitTestClass{


    @Test
    @Ignore
    public void test(){
        JavaWindow window = new JavaWindow("Java test application");
        JavaAwtAppWithSomeSwingComponents javaApp = new JavaAwtAppWithSomeSwingComponents();
        window.mapWindowToDescriptionClass("C:\\Temp\\OutputFile.txt");

    }
}
