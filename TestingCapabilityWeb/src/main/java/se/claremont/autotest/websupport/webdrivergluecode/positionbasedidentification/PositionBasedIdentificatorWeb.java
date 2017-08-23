package se.claremont.autotest.websupport.webdrivergluecode.positionbasedidentification;

import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedIdentificator;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.websupport.DomElement;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

public class PositionBasedIdentificatorWeb extends PositionBasedIdentificator {

    private static WebInteractionMethods web;

    public static WebElementList fromAllSubElementsOf(DomElement domElement, WebInteractionMethods web){
        long startTime = System.currentTimeMillis();
        WebElementList webElementList = new WebElementList(domElement.asPositionBasedWebElement(web).childrenRecursive());
        web.log(LogLevel.DEBUG, "Identified " + webElementList.elements.size() + " sub-elements of " + domElement.LogIdentification() + " in " + (System.currentTimeMillis() - startTime) + " ms.");
        return webElementList;
    }

    public static WebElementList fromAllSubElementsOfElementWithText(String text, WebInteractionMethods web){
        DomElement domElement = new DomElement(text, DomElement.IdentificationType.BY_VISIBLE_TEXT);
        return new WebElementList(domElement.asPositionBasedWebElement(web).childrenRecursive());
    }

    public static WebElementList fromAllElementsInTheSameContainerObjectAs(WebInteractionMethods web, DomElement domElement){
        PositionBasedWebElement posElement = new PositionBasedWebElement(domElement, web);
        return fromAllSubElementsOf(posElement.parentElement().asDomElement(), web);
    }

    public static WebElementList fromAllElementsInTheSameContainerObjectAsElementWithText(String text, WebInteractionMethods web){
        long startTime = System.currentTimeMillis();
        DomElement domElement = new DomElement(text, DomElement.IdentificationType.BY_VISIBLE_TEXT);
        PositionBasedWebElement posElement = new PositionBasedWebElement(domElement, web);
        web.log(LogLevel.DEBUG, "Identified element with text '" + text + "' in " + (System.currentTimeMillis() - startTime) + " ms.");
        return fromAllSubElementsOf(posElement.parentElement().asDomElement(), web);
    }

    public static DomElement elementImmediatelyToTheRightOfText(String text, WebInteractionMethods web){
        PositionBasedWebElement relativeElement = new PositionBasedWebElement(text, web);
        return fromAllElementsInTheSameContainerObjectAsElementWithText(text, web).
                atTheSameHeightAs(text, web, 10, 10).
                keepElementsToTheRightOf(text, web).
                theObjectMostToTheLeft();
    }

}
