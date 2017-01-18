package se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode;

import org.junit.Ignore;
import org.junit.Test;
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
     */
    public void delayedDisplayOfElementShouldStillBeClickable(){
        System.setProperty("webdriver.chrome.driver", "C:\\temp\\chromedriver.exe");
        WebInteractionMethods web = new WebInteractionMethods(new TestCase(null, "dummy"));
        web.navigate("file://C:\\temp\\delayTest.html");
        web.click(new DomElement("button", DomElement.IdentificationType.BY_ID));
        web.verifyElementText(new DomElement("verifyingtext", DomElement.IdentificationType.BY_ID), "Clicked");
        web.makeSureDriverIsClosed();
    }
}
