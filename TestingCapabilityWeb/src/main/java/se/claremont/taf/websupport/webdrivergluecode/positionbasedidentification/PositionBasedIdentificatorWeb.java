package se.claremont.taf.websupport.webdrivergluecode.positionbasedidentification;

import se.claremont.taf.core.guidriverpluginstructure.PositionBasedIdentification.PositionBasedIdentificator;
import se.claremont.taf.core.logging.GenericJavaObjectToHtml;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.websupport.DomElement;
import se.claremont.taf.websupport.webdrivergluecode.WebInteractionMethods;

import java.util.ArrayList;

@SuppressWarnings({"WeakerAccess", "unused", "ConstantConditions"})
public class PositionBasedIdentificatorWeb extends PositionBasedIdentificator {

    public static WebElementList fromAllSubElementsOf(DomElement domElement, WebInteractionMethods web){
        long startTime = System.currentTimeMillis();
        ArrayList<String> identificationDescription = new ArrayList<>();
        WebElementList webElementList = new WebElementList(domElement.asPositionBasedWebElement(web).childrenRecursive(), web, identificationDescription);
        webElementList.identificationDescription.add("Identified " + webElementList.elements.size() + " sub-element(s) of " + domElement.LogIdentification() + ". Operation took " + (System.currentTimeMillis() - startTime) + " milliseconds. ");
        web.getTestCase().testCaseResult.testCaseLog.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, null, "Identified the following elements:" + System.lineSeparator() + GenericJavaObjectToHtml.toHtml(webElementList));
        return webElementList;
    }

    public static DomElement subElementOfOtherElement(DomElement parentElement, DomElement childElement, WebInteractionMethods web){
        return new DomElement(web.getRuntimeElementWithoutLogging(parentElement, childElement));
    }

    public static WebElementList fromAllSubElementsOfElementWithText(String text, WebInteractionMethods web){
        long startTime = System.currentTimeMillis();
        DomElement domElement = new DomElement(text, DomElement.IdentificationType.BY_VISIBLE_TEXT);
        ArrayList<String> identificationDescription = new ArrayList<>();
        identificationDescription.add("Identified element [" + domElement.LogIdentification() + "] with text '" + text + "' to gather sub-elements from. Operation took " + (System.currentTimeMillis()-startTime) + " milliseconds. ");
        WebElementList webElementList = new WebElementList(domElement.asPositionBasedWebElement(web).childrenRecursive(), web, identificationDescription);
        webElementList.identificationDescription.add("Identified " + webElementList.elements.size() + " sub-element(s) of " + domElement.LogIdentification() + ". Operation took " + (System.currentTimeMillis() - startTime) + " milliseconds. ");
        web.getTestCase().testCaseResult.testCaseLog.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, null, "Identified the following elements:" + System.lineSeparator() + GenericJavaObjectToHtml.toHtml(webElementList));
        return webElementList;
    }

    public static WebElementList fromAllElementsInTheSameContainerObjectAs(DomElement domElement, WebInteractionMethods web){
        PositionBasedWebElement posElement = new PositionBasedWebElement(domElement, web);
        return fromAllSubElementsOf(posElement.parentElement().asDomElement(), web);
    }

    public static WebElementList fromAllElementsInTheSameContainerObjectAsElementWithText(String text, WebInteractionMethods web){
        DomElement domElement = new DomElement(text, DomElement.IdentificationType.BY_VISIBLE_TEXT);
        PositionBasedWebElement posElement = new PositionBasedWebElement(domElement, web);
        return fromAllSubElementsOf(posElement.parentElement().asDomElement(), web);
    }

    public static DomElement elementImmediatelyToTheRightOfText(String text, WebInteractionMethods web){
        if(text == null || web == null) return null;
        PositionBasedWebElement relativeElement = new PositionBasedWebElement(text, web);
        if(relativeElement == null) return null;
        DomElement relativeElementAsDomElement = relativeElement.asDomElement();
        return (DomElement)fromAllElementsInTheSameContainerObjectAsElementWithText(text, web).
                atTheSameHeightAs(relativeElementAsDomElement, web, 10, 10).
                keepElementsToTheRightOf(relativeElementAsDomElement, web).
                theObjectMostToTheLeft();
    }

    public static DomElement visibleElementImmediatelyToTheRightOfText(String text, WebInteractionMethods web){
        if(text == null || web == null) return null;
        PositionBasedWebElement relativeElement = new PositionBasedWebElement(text, web);
        if(relativeElement == null) return null;
        DomElement relativeElementAsDomElement = relativeElement.asDomElement();
        return (DomElement)fromAllElementsInTheSameContainerObjectAsElementWithText(text, web).
                atTheSameHeightAs(relativeElementAsDomElement, web, 10, 10).
                keepElementsToTheRightOf(relativeElementAsDomElement, web).
                keepOnlyVisibleElements().
                theObjectMostToTheLeft();
    }
}
