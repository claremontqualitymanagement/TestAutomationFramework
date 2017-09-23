package se.claremont.autotest.websupport.webdrivergluecode;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testset.TestSet;
import se.claremont.autotest.websupport.DomElement;

public class WebInteractionMethodsTest {

    @Test
    public void childObjectIdentificationTest(){
        TestCase testCase = new TestCase();
        WebDriver driver = new HtmlUnitDriver(true);

        WebInteractionMethods web = new WebInteractionMethods(testCase, driver);
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("tableTest.html"));
        DomElement parent = new DomElement("toManyCellsInOneRowHtml", DomElement.IdentificationType.BY_ID);
        Assert.assertNotNull(parent);
        DomElement child = parent.getDomElementFromDescendants(new DomElement("Row2Heading2", DomElement.IdentificationType.BY_VISIBLE_TEXT), web);
        WebElement element = web.getRuntimeElementWithoutLogging(parent, new DomElement("Row2Heading2", DomElement.IdentificationType.BY_VISIBLE_TEXT));
        Assert.assertNotNull(element);
        Assert.assertNotNull(child);
        System.out.println(child.toString());
        WebElement webElement = web.getRuntimeElementWithTimeout(child, 5);
        Assert.assertNotNull(webElement);
        web.verifyElementIsDisplayed(child);
        Assert.assertTrue(web.getRuntimeElementWithoutLogging(child) != null);
    }
}
