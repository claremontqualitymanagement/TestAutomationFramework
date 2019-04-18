package se.claremont.taf.websupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import se.claremont.taf.guidriverpluginstructure.GuiElement;
import se.claremont.taf.support.StringManagement;
import se.claremont.taf.websupport.elementidentification.By;
import se.claremont.taf.websupport.elementidentification.WebElementIdentifier;
import se.claremont.taf.websupport.webdrivergluecode.WebInteractionMethods;
import se.claremont.taf.websupport.webdrivergluecode.positionbasedidentification.PositionBasedWebElement;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Object declaration mechanisms for web elements
 * <p>
 * Created by jordam on 2016-08-17.
 */
@SuppressWarnings("unused")
public class DomElement implements GuiElement, Serializable {

    @SuppressWarnings("WeakerAccess")
    public String name;
    public By by;
    public List<String> recognitionStrings;
    public IdentificationType identificationType;
    public Integer ordinalNumber = null;
    private String page = null;

    /**
     * Declares a DOM element on a web page, for use in automation.
     *
     * @param by Describes how to identify the DOM element.
     */
    public DomElement(By by) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        //this.name = callingMethodUsingConstructor.getMethodName();
        this.name = identifyElementName();
        this.page = callingMethodUsingConstructor.getClassName();
        this.by = by;
    }

    public DomElement(String json){
        ObjectMapper om = new ObjectMapper();
        json = json.substring("webElement=".length());
        JsonNode jsonNode = null;
        try {
            jsonNode = om.readTree(json);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        if(jsonNode == null){
            name = "NullElement";
            return;
        }
        by = By.tagName(jsonNode.get("tagName").asText());
        if(jsonNode.get("id").asText() != null && jsonNode.get("id").asText().length() > 0) {
            by.andById(jsonNode.get("id").asText());
            name = jsonNode.get("id").asText();
        }
        if(jsonNode.get("text").asText() != null && jsonNode.get("text").asText().length() > 0) {
            by.andByExactText(jsonNode.get("text").asText());
            if(name == null || name.length() == 0)
                name = StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(jsonNode.get("text").asText());
        }
        if(jsonNode.get("className").asText() != null && jsonNode.get("className").asText().length() > 0) {
            by.andByClass(jsonNode.get("className").asText());
            if(name == null || name.length() == 0)
                name = StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(jsonNode.get("className").asText());
        }
        if(by.getConditionCount() == 0) by.andByXPath(jsonNode.get("xpath").asText());
        if(name.length() == 0) name = "UnNamedElement";
        if(name.length() > 30) name = name.substring(0, 30);
        name = StringManagement.safeVariableName(StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(name + jsonNode.get("tagName").asText()));
    }

    /**
     * Please, use the more powerful se.claremont.autotest.websupport.elementidentification.By instead.
     * This constructor converts the Selenium By to the TAF By class. With this you get the logging of TAF,
     * but not the flexibility and versatility of the TAF By class.
     *
     * @param seleniumBy Regular Selenium By statement.
     */
    public DomElement(org.openqa.selenium.By seleniumBy) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        //this.name = callingMethodUsingConstructor.getMethodName();
        this.name = identifyElementName();
        this.page = callingMethodUsingConstructor.getClassName();
        switch (seleniumBy.getClass().getSimpleName()) {
            case "ById":
                by = By.id(removeSeleniumByTypeFromString(seleniumBy));
                break;
            case "ByClassName":
                by = By.className(removeSeleniumByTypeFromString(seleniumBy));
                break;
            case "ByXPath":
                by = By.xpath(removeSeleniumByTypeFromString(seleniumBy));
                break;
            case "ByName":
                by = By.name(removeSeleniumByTypeFromString(seleniumBy));
                break;
            case "ByCssSelector":
                by = By.cssSelector(removeSeleniumByTypeFromString(seleniumBy));
                break;
            case "ByTagName":
                by = By.tagName(removeSeleniumByTypeFromString(seleniumBy));
                break;
            case "ByLinkText":
                by = By.tagName("a").andByExactText(removeSeleniumByTypeFromString(seleniumBy));
                break;
            case "ByPartialLinkText":
                by = By.tagName("a").andByTextContainsString(removeSeleniumByTypeFromString(seleniumBy));
                break;
            default:
                System.out.println(seleniumBy.toString());
                break;
        }
    }

    private String removeSeleniumByTypeFromString(org.openqa.selenium.By seleniumBy) {
        return seleniumBy.toString().substring(seleniumBy.toString().indexOf(": ") + 2);
    }

    /**
     * Declares a DOM element on a web page, for use in automation.
     *
     * @param by   Describes how to identify the DOM element.
     * @param name The element name, to be used in logs.
     */
    public DomElement(By by, String name) {
        this.name = name;
        this.by = by;
    }

    /**
     * Defines a DOM element based on the given Selenium WebElement.
     *
     * @param webElement The Selenium WebElement to use as base for this DomElement.
     */
    public DomElement(WebElement webElement) {
        this.name = "Dynamically identified " + webElement.getTagName() + " element";
        this.recognitionStrings = new ArrayList<>();
        this.recognitionStrings.add(getElementXPath(webElement));
        this.identificationType = IdentificationType.BY_X_PATH;
        this.page = getElementPageNameFromCurrentTitle(webElement);
    }

    /**
     * Defines a DOM element based on the given Selenium WebElement.
     *
     * @param webElement      Selenium WebElement object.
     * @param elementName     The name for the element, for use in logs.
     * @param elementPageName The name of the page of the element, for use in logs.
     */
    public DomElement(WebElement webElement, String elementName, String elementPageName) {
        if (elementName == null) {
            this.name = "Dynamically identified " + webElement.getTagName() + " element";
        } else {
            this.name = elementName;
        }
        this.recognitionStrings = new ArrayList<>();
        this.by = By.xpath(getElementXPath(webElement));
        this.recognitionStrings.add(getElementXPath(webElement));
        this.identificationType = IdentificationType.BY_X_PATH;
        if (elementPageName == null) {
            elementPageName = getElementPageNameFromCurrentTitle(webElement);
        }
        this.page = elementPageName;
    }

    private String getElementPageNameFromCurrentTitle(WebElement webElement) {
        try {
            WebDriver driver = getDriver(webElement);
            return driver.getTitle();
        } catch (Exception e) {
            return "TempElementPage";
        }
    }

    /**
     * Declares a DOM element to be used in test execution.
     *
     * @param recognitionString  the recognition string that identifies the object.
     * @param identificationType what mechanism to use for identification.
     * @param name               Element name, for logging.
     */
    public DomElement(String recognitionString, IdentificationType identificationType, String name) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        //this.name = callingMethodUsingConstructor.getMethodName();
        this.recognitionStrings = new ArrayList<>(Collections.singletonList(recognitionString));
        this.name = name;
        this.page = callingMethodUsingConstructor.getClassName();
        this.by = setByFromRecognitionStringAndIdentificationType(recognitionString, identificationType);
        this.identificationType = identificationType;
    }

    /**
     * Declares a DOM element to be used in test execution.
     *
     * @param recognitionString  the recognition string that identifies the object.
     * @param identificationType what mechanism to use for identification.
     */
    public DomElement(String recognitionString, IdentificationType identificationType) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        //this.name = callingMethodUsingConstructor.getMethodName();
        this.recognitionStrings = new ArrayList<>(Collections.singletonList(recognitionString));
        this.name = identifyElementName();
        this.page = callingMethodUsingConstructor.getClassName();
        this.by = setByFromRecognitionStringAndIdentificationType(recognitionString, identificationType);
        this.identificationType = identificationType;
    }

    /**
     * Constructor for use for example with several languages.
     *
     * @param alternativeRecognitionStrings An array of recognition strings for this element.
     * @param identificationType            The method of identification.
     * @param name                          Element name, for logging.
     */
    public DomElement(String[] alternativeRecognitionStrings, IdentificationType identificationType, String name) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        this.name = name;
        this.page = callingMethodUsingConstructor.getClassName();
        this.identificationType = identificationType;
        this.recognitionStrings = new ArrayList<>(Arrays.asList(alternativeRecognitionStrings));
    }

    /**
     * Constructor for use for example with several languages.
     *
     * @param alternativeRecognitionStrings An array of recognition strings for this element.
     * @param identificationType            The method of identification.
     */
    public DomElement(String[] alternativeRecognitionStrings, IdentificationType identificationType) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        this.name = identifyElementName();
        this.page = callingMethodUsingConstructor.getClassName();
        this.identificationType = identificationType;
        this.recognitionStrings = new ArrayList<>(Arrays.asList(alternativeRecognitionStrings));
    }

    /**
     * Declares a DOM element to be used in test execution.
     *
     * @param recognitionString  the recognition string that identifies the object.
     * @param identificationType what mechanism to use for identification.
     * @param ordinalNumber      The ordinal number of the occurrence on the web page, if multiple matches for search criteria.
     * @param name               Element name, for logging.
     */
    public DomElement(String recognitionString, IdentificationType identificationType, Integer ordinalNumber, String name) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        this.name = name;
        this.page = callingMethodUsingConstructor.getClassName();
        this.by = setByFromRecognitionStringAndIdentificationType(recognitionString, identificationType);
        if (this.by != null) this.by = this.by.andByOrdinalNumber(ordinalNumber);
        this.recognitionStrings = new ArrayList<>(Collections.singletonList(recognitionString));
        this.ordinalNumber = ordinalNumber;
        this.identificationType = identificationType;
    }

    /**
     * Declares a DOM element to be used in test execution.
     *
     * @param recognitionString  the recognition string that identifies the object.
     * @param identificationType what mechanism to use for identification.
     * @param ordinalNumber      The ordinal number of the occurrence on the web page, if multiple matches for search criteria.
     */
    public DomElement(String recognitionString, IdentificationType identificationType, Integer ordinalNumber) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        this.name = identifyElementName();
        this.page = callingMethodUsingConstructor.getClassName();
        this.by = setByFromRecognitionStringAndIdentificationType(recognitionString, identificationType);
        if (this.by != null) this.by = this.by.andByOrdinalNumber(ordinalNumber);
        this.recognitionStrings = new ArrayList<>(Collections.singletonList(recognitionString));
        this.ordinalNumber = ordinalNumber;
        this.identificationType = identificationType;
    }

    /**
     * Constructor for use for example with several languages.
     *
     * @param alternativeRecognitionStrings An array of recognition strings for this element.
     * @param identificationType            The method of identification.
     * @param ordinalNumber                 The ordinal number of the occurrence on the web page, if multiple matches for search criteria.
     * @param name                          Element name, for logging.
     */
    public DomElement(String[] alternativeRecognitionStrings, IdentificationType identificationType, Integer ordinalNumber, String name) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        this.name = name;
        this.ordinalNumber = ordinalNumber;
        this.page = callingMethodUsingConstructor.getClassName();
        this.recognitionStrings = new ArrayList<>(Arrays.asList(alternativeRecognitionStrings));
        this.identificationType = identificationType;
    }

    /**
     * Constructor for use for example with several languages.
     *
     * @param alternativeRecognitionStrings An array of recognition strings for this element.
     * @param identificationType            The method of identification.
     * @param ordinalNumber                 The ordinal number of the occurrence on the web page, if multiple matches for search criteria.
     */
    public DomElement(String[] alternativeRecognitionStrings, IdentificationType identificationType, Integer ordinalNumber) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        this.name = identifyElementName();
        this.ordinalNumber = ordinalNumber;
        this.page = callingMethodUsingConstructor.getClassName();
        this.recognitionStrings = new ArrayList<>(Arrays.asList(alternativeRecognitionStrings));
        this.identificationType = identificationType;
    }

    /**
     * Defines a DOM element on a web page, based on its relative position with other elements on the page.
     *
     * @param positionBasedWebElement A DOM element prepared for position based identification.
     */
    public DomElement(PositionBasedWebElement positionBasedWebElement) {
        this.name = "Dynamically identified " + positionBasedWebElement.webElement.getTagName() + " element " + positionBasedWebElement.getText();
        this.recognitionStrings = new ArrayList<>();
        this.recognitionStrings.add(getElementXPath(positionBasedWebElement.webElement));
        this.identificationType = IdentificationType.BY_X_PATH;
        this.page = "TempElementPage";
    }

    private By setByFromRecognitionStringAndIdentificationType(String recognitionString, IdentificationType identificationType) {
        switch (identificationType) {
            case BY_CSS:
                return By.cssSelector(recognitionString);
            case BY_CLASS:
                return By.className(recognitionString);
            case BY_ID:
                return By.id(recognitionString);
            case BY_X_PATH:
                return By.xpath(recognitionString);
            case BY_VISIBLE_TEXT:
                return By.textContainsString(recognitionString);
            case BY_NAME:
                return By.name(recognitionString);
            case BY_LINK_TEXT:
                return By.exactText(recognitionString).andByTagName("a");
            default:
                return null;
        }
    }

    private String identifyElementName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[3];
        String elementName = callingMethodUsingConstructor.getMethodName();
        Class klass = null;
        try {
            klass = Thread.currentThread().getContextClassLoader().loadClass(callingMethodUsingConstructor.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (recognitionStrings != null && (elementNameMatchesNameOfAJunitTest(elementName, klass) || elementName.equals("<init>"))) {
            elementName = "'" + String.join(" and ", recognitionStrings) + "'";
        }
        /*
            Field[] fields = klass.getDeclaredFields();
            if(fields.length > 1){
                elementName = String.join("_", recognitionStrings);
            } else {
                for (Field f : fields) {
                    if (f.getType().getName().equals("se.claremont.autotest.websupport.DomElement")) {
                        elementName = f.getName();
                    }
                    System.out.println("Field: '" + f.getName() + "', type: '" + f.getType().getName() + "'");
                }
            }
        }
        */
        return elementName;
    }

    private boolean elementNameMatchesNameOfAJunitTest(String elementName, Class klass) {
        for (Method m : klass.getMethods()) {
            if (!m.getName().equals(elementName)) continue;
            for (Annotation a : m.getDeclaredAnnotations()) {
                if (a.annotationType().toString().equals("interface org.junit.Test")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Searches the descendants for the given DOM element, using the WebInteractionMethods given.
     *
     * @param domElement The DOM element that should exist among the descendants of this element.
     * @param web        WebInteractionMethods to use.
     * @return Returns the DomElement, for later use.
     */
    public DomElement getDomElementFromDescendants(DomElement domElement, WebInteractionMethods
            web) {
        return new DomElement(web.getRuntimeElementWithoutLogging(this, domElement));
    }

    //Don't like this since it brings a bond to Selenium from this class
    private WebDriver getDriver(WebElement webElement) {
        WebDriver driver = null;
        try {
            driver = ((WrapsDriver) webElement).getWrappedDriver();
        } catch (Exception ignored) {
        }
        return driver;
    }

    /**
     * Converts a DomElement to a PositionBasedWebElement.
     *
     * @param web WebInteractionMethods to use.
     * @return Returns this element as a PositionBasedWebElement.
     */
    public PositionBasedWebElement asPositionBasedWebElement(WebInteractionMethods web) {
        return new PositionBasedWebElement(web.getRuntimeElementWithoutLogging(this));
    }

    /**
     * Retrieves the xpath of the current element, in the current browser, and with the current web page structure.
     * Remember that different web browsers parses the DOM differently, giving different xpaths. Hence this method
     * should only be used for runtime xpath context, not permanent declarations.
     *
     * @param element The Selenium WebElement object to find the xpath for.
     * @return Returns the xpath as a String object.
     */
    public String getElementXPath(WebElement element) {
        if (element == null) return null;
        WebDriver driver = getDriver(element);
        if (driver == null) return null;
        String tag = element.getTagName();
        return (String) ((JavascriptExecutor) driver).executeScript("" +
                "gPt=function(c){" +
//                "   if(c.id!==''){" +
//                "       return'//*[@id=\"'+c.id+'\"]'" +
//                "   }" +
                "   if(c===document.body){" +
                "       return c.tagName" +
                "   }" +
                "   var a=0;" +
                "   var e=c.parentNode.childNodes;" +
                "   for(var b=0;b<e.length;b++){" +
                "       var d=e[b];" +
                "       if(d===c){" +
                "           return gPt(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'" +
                "       }" +
                "       if(d.nodeType===1&&d.tagName===c.tagName){" +
                "           a++}" +
                "       }" +
                "   };" +
                "   return '//' + gPt(arguments[0]).toLowerCase();", element);
    }

    /**
     * Enables unified logging formats for element references in the testCaseLog.
     *
     * @return a string to use in testCaseLog posts.
     */
    public String LogIdentification() {
        {
            String idMessage = name;
            if (page != null && page.length() > 0) idMessage += " (declared in page class " + page + ")";
            return idMessage;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[DomElement: ");
        sb.append("Name='").append(name).append("', ");
        sb.append("page='").append(page).append("', ");
        if (recognitionStrings != null) {
            for (String recognitionString : recognitionStrings) {
                sb.append(" recognitionString='").append(recognitionString).append("', ");
            }
        }
        if (by != null) {
            sb.append("by='").append(by.toString()).append("', ");
            sb.append("generatedXPath='").append(WebElementIdentifier.createXPathFromBy(by)).append("', ");
        }
        if (identificationType != null)
            sb.append("identificationType='").append(identificationType.toString()).append("', ");
        sb.append("ordinalNumber=").append(String.valueOf(ordinalNumber)).append("]");
        return sb.toString();
    }

    /**
     * Identification mechanisms
     */
    public enum IdentificationType {
        BY_LINK_TEXT,
        BY_X_PATH,
        BY_ID,
        BY_CLASS,
        BY_CSS,
        BY_NAME,
        BY_VISIBLE_TEXT,
        BY_ATTRIBUTE_VALUE
    }

}
