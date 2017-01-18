package se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.common.LogPost;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.common.TestSet;
import se.claremont.autotest.guidriverpluginstructure.websupport.DomElement;

/**
 * Created by jordam on 2017-01-18.
 */
public class WebInteractionMethods_Test extends TestSet {


    @Test
    @Ignore
    /**
     * This test case tries clicking a button that at first is not displayed, and then is not enabled.
     * When enabled the click is performed.
     *
     * This method could be improved by opening the html file from the project.
     */
    public void delayedDisplayOfElementShouldStillBeClickable(){
        System.setProperty("webdriver.chrome.driver", "C:\\temp\\chromedriver.exe");
        WebInteractionMethods web = new WebInteractionMethods(new TestCase(null, "dummy"));
        web.navigate("file://C:\\temp\\delayTest.html");
        web.click(new DomElement("button", DomElement.IdentificationType.BY_ID));
        web.verifyElementText(new DomElement("verifyingtext", DomElement.IdentificationType.BY_ID), "Clicked");
        web.makeSureDriverIsClosed();
    }

    @Test
    @Ignore
    /**
     * This test case tries clicking a button that at first is not displayed, and then is not enabled.
     * When enabled the click is performed.
     *
     * This method could be improved by opening the html file from the project.
     */
    public void delayedDisplayOfElementWithToShortTimeoudShouldGiveErrorMessage(){
        TestCase testCase = new TestCase(null, "dummy");
        System.setProperty("webdriver.chrome.driver", "C:\\temp\\chromedriver.exe");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate("file://C:\\temp\\delayTest.html");
        web.click(new DomElement("button", DomElement.IdentificationType.BY_ID), 1);
        LogPost error = testCase.testCaseLog.firstNonSuccessfulLogPost();
        Assert.assertNotNull(error);
        Assert.assertTrue(error.toString().contains("Execution problem"));
        Assert.assertTrue(error.toString().contains("Seems unnatural to click it"));
        web.makeSureDriverIsClosed();
    }

    @Test
    @Ignore
    /**
     * This test case tries clicking a button that at first is not displayed, and then is not enabled.
     * When enabled the click is performed.
     *
     * This method could be improved by opening the html file from the project.
     */
    public void clickingDisabledButton(){
        TestCase testCase = new TestCase(null, "dummy");
        System.setProperty("webdriver.chrome.driver", "C:\\temp\\chromedriver.exe");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate("file://C:\\temp\\delayTest.html");
        web.clickEvenIfDisabled(new DomElement("button", DomElement.IdentificationType.BY_ID), 1);
        web.makeSureDriverIsClosed();
    }
}
