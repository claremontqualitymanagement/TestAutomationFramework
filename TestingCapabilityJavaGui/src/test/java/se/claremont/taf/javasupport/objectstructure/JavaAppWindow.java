package se.claremont.taf.javasupport.objectstructure;

import org.junit.Ignore;
import org.junit.Test;
import se.claremont.taf.core.testset.UnitTestClass;
import se.claremont.taf.javasupport.interaction.JavaAwtAppWithSomeSwingComponents;

/**
 * Test to assess successful GUI element map creation
 *
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
