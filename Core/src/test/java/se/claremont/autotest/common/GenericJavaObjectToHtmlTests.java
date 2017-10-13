package se.claremont.autotest.common;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.logging.GenericJavaObjectToHtml;

public class GenericJavaObjectToHtmlTests {

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
}
