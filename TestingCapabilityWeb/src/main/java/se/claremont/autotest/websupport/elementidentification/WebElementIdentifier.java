package se.claremont.autotest.websupport.elementidentification;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.websupport.DomElement;

import java.util.ArrayList;
import java.util.List;

public class WebElementIdentifier {
    DomElement domElement;
    WebDriver driver;
    TestCase testCase;
    boolean performLogging;

    public static WebElement getWebElement(TestCase testCase, WebDriver driver, DomElement domElement, boolean performLogging) {
        WebElementIdentifier webElementIdentifier = new WebElementIdentifier(testCase, driver, domElement, performLogging);
        return webElementIdentifier.getRuntimeElement();
    }

    public WebElementIdentifier(TestCase testCase, WebDriver driver, DomElement domElement, boolean performLogging) {
        this.domElement = domElement;
        this.driver = driver;
        this.testCase = testCase;
        this.performLogging = performLogging;
    }

    /// <summary>
    /// Identifies the runtime IWebElement object for the given DomElement describing it. 
    /// </summary>
    /// <returns>Returns the IWebElement object corresponding to the DomElement if found, othervice null.</returns>
    public WebElement getWebElement() {
        return getRuntimeElement();
    }

    private WebElement getRuntimeElement() {
        String xpath = createXPathFromBy();
        if (xpath == null) return getElementByCssSelector();
        return getElementByXpath(xpath);
    }

    private WebElement getElementByXpath(String xpath) {
        WebElement returnElement;
        try {
            List<WebElement> elements = driver.findElements(org.openqa.selenium.By.xpath(xpath));
            if (elements.size() == 0) {
                log(new LogPost(LogLevel.DEBUG, "Could not identify any matches for '" + domElement.name
                        + "' by using xpath:" + System.lineSeparator() + "'" + xpath + "'"
                        + System.lineSeparator() + "Built from By statement:"
                        + System.lineSeparator() + domElement.by.toString(), null, testCase.testName, testCase.getCurrentTestStepName(), this.getClass().getSimpleName()));
                return null;
            }
            int ordinalNumber = 1;
            for (SearchCondition sc : domElement.by.conditions) {
                if (sc.getType() == SearchConditionType.OrdinalNumber) {
                    ordinalNumber = (int) sc.value();
                }
            }
            if (elements.size() < ordinalNumber) {
                List<String> elementDescriptions = new ArrayList<>();
                for (WebElement webElement : elements) {
                    elementDescriptions.add(webElement.toString());
                }
                log(new LogPost(LogLevel.DEBUG, "Element '" + domElement.name + "' was supposed to be the "
                        + ordinalNumber + " match for its search criteria. However, only "
                        + elements.size() + " metching elements were found when it is identified by " +
                        "its xPath:" + System.lineSeparator() +
                        "'" + xpath + "'" + System.lineSeparator() +
                        ", based on its By statement: + " + System.lineSeparator() +
                        domElement.by.toString() + System.lineSeparator() +
                        "Elements found:" + System.lineSeparator() +
                        String.join(System.lineSeparator(), elementDescriptions), null, testCase.testName, testCase.getCurrentTestStepName(), this.getClass().getSimpleName()));
                return elements.get(0);
            }
            if (ordinalNumber < 1) {
                log(new LogPost(LogLevel.DEBUG, "Ordinal number to identify element '" + domElement.name + "' is less than one. Must be a positive integer.", null, testCase.testName, testCase.getCurrentTestStepName(), this.getClass().getSimpleName()));
                return null;
            }
            returnElement = elements.get(ordinalNumber - 1);
        } catch (Exception poff) {
            log(new LogPost(LogLevel.DEBUG, "Something went wrong trying to identify element '" + domElement.name + "'. Error: " + poff.toString(), null, testCase.testName, testCase.getCurrentTestStepName(), this.getClass().getSimpleName()));
            return null;
        }
        if (returnElement != null) log(new LogPost(LogLevel.DEBUG, "Found exactly one IWebElement match for element '" +
                domElement.name + "' using xpath:" + System.lineSeparator() +
                "'" + xpath + "'" + System.lineSeparator() +
                ", created from the By statement:" + System.lineSeparator() +
                domElement.by.toString(), null, testCase.testName, testCase.getCurrentTestStepName(), this.getClass().getSimpleName()));
        return returnElement;

    }

    private WebElement getElementByCssSelector() {
        try {
            List<WebElement> potentialMatches = driver.findElements(org.openqa.selenium.By.cssSelector(cssLocatorDescription()));
            if (potentialMatches == null || potentialMatches.size() == 0) {
                log(new LogPost(LogLevel.DEBUG, "Could not identify any match for element '" + domElement.name + "'. Used CSS identification to try to find it.", null, testCase.testName, testCase.getCurrentTestStepName(), this.getClass().getSimpleName()));
                return null;
            }
            int ordinalNumber = 1;
            for (SearchCondition sc : domElement.by.conditions) {
                if (sc.getType() == SearchConditionType.OrdinalNumber) {
                    ordinalNumber = (int) sc.value();
                }
            }
            if (ordinalNumber > potentialMatches.size()) {
                log(new LogPost(LogLevel.DEBUG, "Was expected to return element number " + ordinalNumber + " among the elements matching search criteria for '" + domElement.name + "' but only " + potentialMatches.size() + " element(s) were found. Continuing using the first element, even if this could be the wrong one.", null, testCase.testName, testCase.getCurrentTestStepName(), this.getClass().getSimpleName()));
            }
            if (potentialMatches.size() == 1) {
                log(new LogPost(LogLevel.DEBUG, "Found exactly one match for element '" + domElement.name + "' identified by CSS identification.", null, testCase.testName, testCase.getCurrentTestStepName(), this.getClass().getSimpleName()));
                return potentialMatches.get(0);
            } else {
                log(new LogPost(LogLevel.DEBUG, "Found " + potentialMatches.size() + " elements given the search conditions when trying to identify the '" + domElement.name + "' element. Continuing using match number " + ordinalNumber + ", even if this could be the wrong element.", null, testCase.testName, testCase.getCurrentTestStepName(), this.getClass().getSimpleName()));
                if (ordinalNumber > 0) {
                    return potentialMatches.get(ordinalNumber - 1);
                }
                return potentialMatches.get(0);
            }

        } catch (Exception e) {
            log(new LogPost(LogLevel.DEBUG, "Could not identify element '" + domElement.name + "'. Error: " + e.toString(), null, testCase.testName, testCase.getCurrentTestStepName(), this.getClass().getSimpleName()));
            return null;
        }
    }

    public String createXPathFromBy() {
        if (cssLocatorDescription() != null) return null;
        String xpathString = getXPathIfCriteria();
        if (xpathString != null) return addXPathForAcestorIfApplicable() + xpathString;
        StringBuilder xpath = new StringBuilder();
        xpath.append("//").append(identifyExpectedTagNameFromSearchCriterias());

        List<String> parts = new ArrayList<>();

        List<String> attributes = getPropertiesFromBy();
        if (attributes.size() > 0) parts.addAll(attributes);
        List<String> textIdentification = textRecognitionPartsOfElementBy();
        if (textIdentification.size() > 0) parts.addAll(textIdentification);
        if (parts.size() > 0) xpath.append("[").append(String.join(" and ", parts)).append("]");
        return addXPathForAcestorIfApplicable() + xpath.toString();
    }

    private String addXPathForAcestorIfApplicable() {
        String ancestorXPath = "";
        for (SearchCondition sc : domElement.by.conditions) {
            if (sc.getType() == SearchConditionType.ByAncestor) {
                WebElement ancestor = (WebElement) sc.value();
                ancestorXPath += getWebElementXPath(ancestor);
            }
        }
        return ancestorXPath;
    }

    private void log(LogPost logPost) {
        if (!performLogging) return;
        if (testCase == null) {
            System.out.println(logPost.toString());
        } else {
            testCase.log(logPost.logLevel, logPost.message);
        }
    }

    private String cssLocatorDescription() {
        for (SearchCondition sc : domElement.by.conditions) {
            if (sc.getType() == SearchConditionType.CssLocator) return sc.value().toString();
        }
        return null;
    }

    private List<String> textRecognitionPartsOfElementBy() {
        List<String> parts = new ArrayList<>();
        List<String> textPattern = new ArrayList<>();
        String xpathString;
        for (SearchCondition sc : domElement.by.conditions) {
            switch (sc.getType()) {
                case ExactText:
                    for (Object text : sc.values) {
                        textPattern.add((String) text);
                    }
                    xpathString = "text()='" + String.join("' or text()='", textPattern) + "'";
                    if (textPattern.size() > 1) xpathString = "(" + xpathString + ")";
                    parts.add(xpathString);
                    break;
                case TextContains:
                    for (Object text : sc.values) {
                        textPattern.add((String) text);
                    }
                    xpathString = "contains(text(),'" + String.join("') or contains(text(),'", textPattern) + "')";
                    if (textPattern.size() > 1) xpathString = "(" + xpathString + ")";
                    parts.add(xpathString);
                    break;
                case TextRegex:
                    parts.add("matches(text(), '" + (String) sc.value() + "')");
                    break;
                default:
                    break;
            }
        }
        return parts;
    }

    private List<String> getPropertiesFromBy() {
        List<String> attributes = new ArrayList<>();
        for (SearchCondition sc : domElement.by.conditions) {
            if (sc.getType() == SearchConditionType.AttributeValue) {
                if (((String) sc.values[0]).toLowerCase().equals("classcontains")) {
                    String[] classes = ((String) sc.values[1]).split(" ");
                    for (String klass : classes) {
                        if (klass.trim().length() == 0) continue;
                        attributes.add("contains(@class, '" + klass.trim() + "')");
                    }

                } else {
                    attributes.add("@" + (String) sc.values[0] + "='" + (String) sc.values[1] + "'");
                }
            }
        }
        return attributes;
    }

    private String getXPathIfCriteria() {
        String xpath = null;
        for (SearchCondition sc : domElement.by.conditions) {
            if (sc.getType() == SearchConditionType.XPath) {
                xpath = (String) sc.value();
                break;
            }
        }
        if (xpath == null) return null;
        for (SearchCondition sc : domElement.by.conditions) {
            if (sc.getType() == SearchConditionType.OrdinalNumber) {
                xpath = xpath + "[" + (int) sc.value() + "]";
                break;
            }
        }
        return xpath;
    }

    private String identifyExpectedTagNameFromSearchCriterias() {
        for (SearchCondition sc : domElement.by.conditions) {
            if (sc.getType() == SearchConditionType.ControlType) {
                return (String) sc.value();
            }
        }
        return "*";
    }


    private WebDriver getDriver(WebElement webElement) {
        WebDriver driver = null;
        try {
            driver = ((WrapsDriver) webElement).getWrappedDriver();
        } catch (Exception ignored) {
        }
        return driver;
    }

    public String getWebElementXPath(WebElement element) {
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

    public static String createXPathFromBy(By by) {
        WebElementIdentifier identifier = new WebElementIdentifier(null, null, new DomElement(by), false);
        return identifier.createXPathFromBy();
    }
}

