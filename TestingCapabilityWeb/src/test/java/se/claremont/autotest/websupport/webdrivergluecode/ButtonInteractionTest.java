package se.claremont.autotest.websupport.webdrivergluecode;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testset.TestSet;
import se.claremont.autotest.websupport.DomElement;

import java.io.File;
import java.net.URL;

/**
 * Tests for the WebInteractionMethods class
 *
 * Created by jordam on 2017-01-18.
 */
public class ButtonInteractionTest extends TestSet {
    @Test
    @Ignore
    /*
      This test case tries clicking a button that at first is not displayed, and then is not enabled.
      When enabled the click is performed.
     */
    public void delayedDisplayOfElementShouldStillBeClickable(){
        WebInteractionMethods web = new WebInteractionMethods(new TestCase(null, "dummy"));
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("delayTest.html"));
        web.click(new DomElement("button", DomElement.IdentificationType.BY_ID));
        web.verifyElementText(new DomElement("verifyingText", DomElement.IdentificationType.BY_ID), "Clicked");
        web.makeSureDriverIsClosed();
    }

    @Test
    @Ignore
    /*
      This test case tries clicking a button that at first is not displayed, and then is not enabled.
      When enabled the click is performed.
      It seem to be working, but since the failed click-attempt stops execution it kind of fails.
     */
    public void delayedDisplayOfElementWithToShortTimeoutShouldGiveErrorMessage(){
        TestCase testCase = new TestCase(null, "dummy");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("delayTest.html"));
        try{
            web.click(new DomElement("button", DomElement.IdentificationType.BY_ID), 1);
        }catch (Exception ignored){}
        LogPost error = testCase.testCaseLog.firstNonSuccessfulLogPost();
        Assert.assertNotNull(error);
        Assert.assertTrue(error.toString().contains("Execution problem"));
        Assert.assertTrue(error.toString().contains("Seems unnatural to click it"));
        web.makeSureDriverIsClosed();
    }

    @Test
    @Ignore
    /*
      This test case tries clicking a button that at first is not displayed, and then is not enabled.
      When enabled the click is performed.
     */
    public void clickingDisabledButton(){
        TestCase testCase = new TestCase(null, "dummy");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("delayTest.html"));
        web.clickEvenIfDisabled(new DomElement("button", DomElement.IdentificationType.BY_ID), 1);
        web.makeSureDriverIsClosed();
    }
}
