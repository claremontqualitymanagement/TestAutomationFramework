import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import se.claremont.autotest.common.logging.LogFolder;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;
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

    @Test
    @Ignore("Run only where you can clean your test log folder")
    public void scrollbarForLongLogPostMessages(){
        TestCase testCase = new TestCase();
        testCase.testCaseResult.testCaseLog.log(LogLevel.INFO, "Short message", "<br>Short message<br>", "Noname testcase", "Step1", "TestClass");
        testCase.testCaseResult.testCaseLog.log(LogLevel.EXECUTED, "Long message 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", "Longmessage123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890".replace(" ", ""), "Noname testcase", "Step2", "TestClass");
        testCase.report();
        //WebDriver driver = new HtmlUnitDriver();
        //WebInteractionMethods web = new WebInteractionMethods(currentTestCase(), driver);
        WebInteractionMethods web = new WebInteractionMethods(currentTestCase());
        Assume.assumeTrue("Not able to execute Javascript with this driver", web.driver instanceof JavascriptExecutor);
        web.navigate(testCase.pathToHtmlLogFile);
        web.click(new DomElement("Step2", DomElement.IdentificationType.BY_VISIBLE_TEXT));
        web.wait(1000);
        String xpath = "//td[contains(text(),'Longmessage')]";
        WebElement logMessage = web.driver.findElement(By.xpath(xpath));
        Assert.assertNotNull(logMessage);
        JavascriptExecutor js = (JavascriptExecutor)web.driver;

        int elementWidth = ((Long) js.executeScript("return arguments[0].scrollWidth", logMessage)).intValue();
        int browserWidth = web.driver.manage().window().getSize().width;
        Assert.assertTrue(elementWidth > browserWidth);
        web.makeSureDriverIsClosed();
    }


}
