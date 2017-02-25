package se.claremont.autotest.javasupport.interaction;

/*
import org.junit.*;
import org.openqa.selenium.WebElement;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.websupport.DomElement;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;
*/
/**
 * Created by jordam on 2017-02-22.
 */
@SuppressWarnings("WeakerAccess")
public class JavaWebTestGenericApproach  {
/*
    WebInteractionMethods web;

    @Before
    public void setup(){
        web = new WebInteractionMethods(currentTestCase);
    }

    @After
    public void tearDown(){
        web.makeSureDriverIsClosed();
    }

    @Test
    public void getTextOnWebElementShouldRetrieveTheText(){
        web.navigate("http://www.claremont.se");
        DomElement domElement = new DomElement("//div[contains(@class, 'col-md-7')]/p", DomElement.IdentificationType.BY_X_PATH);
        GenericInteractionMethods app = new GenericInteractionMethods(currentTestCase);
        WebElement link = web.getRuntimeElementWithoutLogging(domElement);
        System.out.println(app.getText(link));
        currentTestCase.log(LogLevel.INFO, app.getText(link));
        Assert.assertTrue(app.getText(link).contains("I de få orden ryms hela idén med Claremont"));
    }

    @Test
    public void getTextOnParentWebElementShouldRetrieveTheText(){
        web.navigate("http://www.claremont.se");
        DomElement domElement = new DomElement("//div[contains(@class, 'col-md-7')]", DomElement.IdentificationType.BY_X_PATH);
        GenericInteractionMethods app = new GenericInteractionMethods(currentTestCase);
        WebElement link = web.getRuntimeElementWithoutLogging(domElement);
        System.out.println(app.getText(link));
        currentTestCase.log(LogLevel.INFO, app.getText(link));
        Assert.assertTrue(app.getText(link).contains("I de få orden ryms hela idén med Claremont"));
    }
    */
}
