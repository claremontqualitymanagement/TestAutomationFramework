package se.claremont.taf;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.taf.core.logging.GenericJavaObjectToHtml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GenericJavaObjectToHtmlTests {

    @SuppressWarnings("ConstantConditions")
    @Test
    public void nullObjectTest(){
        String object = null;
        Assert.assertTrue(GenericJavaObjectToHtml.toHtml(object).equals("<span class='nullobject'><i>null</i></span>"));
    }

    @Test
    public void primitivesTest(){
        Assert.assertTrue(GenericJavaObjectToHtml.toHtml("MyString"), GenericJavaObjectToHtml.toHtml("MyString").trim().equals("MyString"));
    }

    @Test
    public void primitivesTestStringReplacementOfLineBreakWithBrTag(){
        Assert.assertTrue("'" + GenericJavaObjectToHtml.toHtml("String" + System.lineSeparator()) + "'", GenericJavaObjectToHtml.toHtml("String" + System.lineSeparator()).contains("String<br>"));
    }

    @Test
    @Ignore
    public void nameIdentificationTests() throws IOException {
        Files.write(Paths.get("C:\\temp\\test.html"), GenericJavaObjectToHtml.toHtmlPage(new TestObject()).getBytes());
        //System.out.println(GenericJavaObjectToHtml.toHtml(new TestObject()));
    }

    @SuppressWarnings("unused")
    class TestObject{
        String name = "ThisTestObject";
        TestObject2 subObject = new TestObject2();

    }

    class TestObject2{
        String id = "12425";
    }
}
