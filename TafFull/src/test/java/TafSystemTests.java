import org.junit.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import se.claremont.autotest.common.testset.TestSet;
import se.claremont.autotest.websupport.DomElement;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertNotNull;

public class TafSystemTests extends TestSet{
    WebInteractionMethods web;

    @Before
    public void setup(){
        HtmlUnitDriver driver = new HtmlUnitDriver();
        web = new WebInteractionMethods(currentTestCase(), driver);
    }

    @After
    public void teardown(){
        if(web == null) return;
        web.makeSureDriverIsClosed();
    }

    @Test
    public void testIdentifyingWebElementByVisibleText(){
        web.navigate("file://" + getTestFileFromTestResourcesFolder("TestPage.html"));
        web.verifyElementText(new DomElement("The text", DomElement.IdentificationType.BY_VISIBLE_TEXT), "The text");
    }

    @Test
    public void testIdentifyingElementById(){
        web.navigate("file://" + getTestFileFromTestResourcesFolder("TestPage.html"));
        web.verifyElementText(new DomElement("text", DomElement.IdentificationType.BY_ID), "The text");
    }


    public static String getTestFileFromTestResourcesFolder(String fileName){
        URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);

        assertNotNull(url);
        File file = new File(url.getPath());
        Assume.assumeNotNull(file);
        return file.getAbsolutePath();
    }

}
