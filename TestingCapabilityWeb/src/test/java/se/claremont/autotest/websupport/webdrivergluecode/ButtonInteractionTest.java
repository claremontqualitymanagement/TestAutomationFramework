package se.claremont.autotest.websupport.webdrivergluecode;

import org.junit.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testset.UnitTestClass;
import se.claremont.autotest.websupport.DomElement;

/**
 * Tests for the WebInteractionMethods class
 *
 * Created by jordam on 2017-01-18.
 */
public class ButtonInteractionTest extends UnitTestClass {
    TestCase testCase;
    WebInteractionMethods web;

    @Before
    public void setup() {
        testCase = new TestCase();
        HtmlUnitDriver driver = new HtmlUnitDriver();
        driver.setJavascriptEnabled(true);
        web = new WebInteractionMethods(testCase, driver);
    }

    @After
    public void teardown(){
        web.makeSureDriverIsClosed();
    }

    @Test
    /*
      This test case tries clicking a button that at first is not displayed, and then is not enabled.
      When enabled the click is performed.
     */
    public void delayedDisplayOfElementShouldStillBeClickable(){
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("delayTest.html"));
        web.click(new DomElement("button", DomElement.IdentificationType.BY_ID));
        web.verifyElementText(new DomElement("verifyingText", DomElement.IdentificationType.BY_ID), "Clicked");
    }

    @Test
    @Ignore
    /*
      This test case tries clicking a button that at first is not displayed, and then is not enabled.
      When enabled the click is performed.
      It seem to be working, but since the failed click-attempt stops execution it kind of fails.
     */
    public void delayedDisplayOfElementWithToShortTimeoutShouldGiveErrorMessage(){
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("delayTest.html"));
        try{
            web.click(new DomElement("button", DomElement.IdentificationType.BY_ID), 1);
        }catch (Exception ignored){}
        LogPost error = testCase.testCaseLog.firstNonSuccessfulLogPost();
        Assert.assertNotNull(error);
        Assert.assertTrue(error.toString().contains("Execution problem"));
        Assert.assertTrue(error.toString().contains("Seems unnatural to click it"));
    }

    @Test
    @Ignore
    /*
      This test case tries clicking a button that at first is not displayed, and then is not enabled.
      When enabled the click is performed.
     */
    public void clickingDisabledButton(){
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("delayTest.html"));
        web.clickEvenIfDisabled(new DomElement("button", DomElement.IdentificationType.BY_ID), 1);
    }
}
