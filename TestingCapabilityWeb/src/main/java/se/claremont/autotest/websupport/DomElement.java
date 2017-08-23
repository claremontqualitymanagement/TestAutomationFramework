package se.claremont.autotest.websupport;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import se.claremont.autotest.common.guidriverpluginstructure.GuiElement;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;
import se.claremont.autotest.websupport.webdrivergluecode.positionbasedidentification.PositionBasedWebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Object declaration mechanisms for web elements
 *
 * Created by jordam on 2016-08-17.
 */
@SuppressWarnings("unused")
public class DomElement implements GuiElement {

    @SuppressWarnings("WeakerAccess")
    public final String name;
    private final String page;
    public final List<String> recognitionStrings;
    public final IdentificationType identificationType;
    public Integer ordinalNumber = null;

    /**
     * Identification mechanisms
     */
    public enum IdentificationType{
        BY_LINK_TEXT,
        BY_X_PATH,
        BY_ID,
        BY_CLASS,
        BY_CSS,
        BY_NAME,
        BY_VISIBLE_TEXT,
        BY_ATTRIBUTE_VALUE
    }

    /**
     * Declares a DOM element to be used in test execution
     * @param recognitionString the recognition string that identifies the object
     * @param identificationType what mechanism to use for identification
     */
    public DomElement (String recognitionString, IdentificationType identificationType){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        this.name = callingMethodUsingConstructor.getMethodName();
        this.page = callingMethodUsingConstructor.getClassName();
        this.recognitionStrings = new ArrayList<>(Collections.singletonList(recognitionString));
        this.identificationType = identificationType;
    }

    /**
     * Constructor for use for example with several languages
     *
     * @param alternativeRecognitionStrings An array of recognition strings for this element
     * @param identificationType The method of identification
     */
    public DomElement(String[] alternativeRecognitionStrings, IdentificationType identificationType){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        this.name = callingMethodUsingConstructor.getMethodName();
        this.page = callingMethodUsingConstructor.getClassName();
        this.identificationType = identificationType;
        this.recognitionStrings = new ArrayList<>(Arrays.asList(alternativeRecognitionStrings));
    }

    /**
     * Declares a DOM element to be used in test execution
     * @param recognitionString the recognition string that identifies the object
     * @param identificationType what mechanism to use for identification
     * @param ordinalNumber The ordinal number of the occurrence on the web page, if multiple matches for search criteria.
     */
    public DomElement (String recognitionString, IdentificationType identificationType, Integer ordinalNumber){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        this.name = callingMethodUsingConstructor.getMethodName();
        this.page = callingMethodUsingConstructor.getClassName();
        this.recognitionStrings = new ArrayList<>(Collections.singletonList(recognitionString));
        this.ordinalNumber = ordinalNumber;
        this.identificationType = identificationType;
    }

    /**
     * Constructor for use for example with several languages
     *
     * @param alternativeRecognitionStrings An array of recognition strings for this element
     * @param identificationType The method of identification
     * @param ordinalNumber The ordinal number of the occurrence on the web page, if multiple matches for search criteria.
     */
    public DomElement(String[] alternativeRecognitionStrings, IdentificationType identificationType, Integer ordinalNumber){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        this.name = callingMethodUsingConstructor.getMethodName();
        this.ordinalNumber = ordinalNumber;
        this.page = callingMethodUsingConstructor.getClassName();
        this.recognitionStrings = new ArrayList<>(Arrays.asList(alternativeRecognitionStrings));
        this.identificationType = identificationType;
    }


    public DomElement(WebElement webElement){
        this.name = "Dynamically identified " + webElement.getTagName() + " element";
        this.recognitionStrings = new ArrayList<>();
        this.recognitionStrings.add(getElementXPath(webElement));
        this.identificationType = IdentificationType.BY_X_PATH;
        this.page = "TempElementPage";
    }

    public DomElement(PositionBasedWebElement positionBasedWebElement){
        this.name = "Dynamically identified " + positionBasedWebElement.webElement.getTagName() + " element " + positionBasedWebElement.getText();
        this.recognitionStrings = new ArrayList<>();
        this.recognitionStrings.add(getElementXPath(positionBasedWebElement.webElement));
        this.identificationType = IdentificationType.BY_X_PATH;
        this.page = "TempElementPage";
    }

    private WebDriver getDriver(WebElement webElement){
        WebDriver driver = null;
        try{
            driver = ((WrapsDriver) webElement).getWrappedDriver();
        }catch (Exception ignored){ }
        return driver;
    }

    public PositionBasedWebElement asPositionBasedWebElement(WebInteractionMethods web){
        return new PositionBasedWebElement(web.getRuntimeElementWithoutLogging(this));
    }

    public String getElementXPath(WebElement element) {
        if(element == null) return null;
        WebDriver driver = getDriver(element);
        if(driver == null) return null;
        return (String)((JavascriptExecutor)driver).executeScript("gPt=function(c){if(c.id!==''){return'id(\"'+c.id+'\")'}if(c===document.body){return c.tagName}var a=0;var e=c.parentNode.childNodes;for(var b=0;b<e.length;b++){var d=e[b];if(d===c){return gPt(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'}if(d.nodeType===1&&d.tagName===c.tagName){a++}}};return gPt(arguments[0]).toLowerCase();", element);
    }
    /**
     * Enables unified logging formats for element references in the testCaseLog
     * @return a string to use in testCaseLog posts
     */
    public String LogIdentification(){
        return name + " (declared in page class " + page + ")";
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[DomElement: ");
        sb.append("Name='").append(name).append("', ");
        sb.append("page='").append(page).append("', ");
        for(String recognitionString : recognitionStrings){
            sb.append(" recognitionString='").append(recognitionString).append("', ");
        }
        sb.append("identificationType='").append(identificationType.toString()).append("', ");
        sb.append("ordinalNumber=").append(String.valueOf(ordinalNumber)).append("]");
        return sb.toString();
    }

}
