package se.claremont.autotest.websupport.webdrivergluecode.positionbasedidentification;

import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedIdentificator;
import se.claremont.autotest.websupport.DomElement;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

import java.util.ArrayList;

public class PositionBasedIdentificatorWeb extends PositionBasedIdentificator {

    private static WebInteractionMethods web;

    public static WebElementList fromAllSubElementsOf(DomElement domElement, WebInteractionMethods web){
        long startTime = System.currentTimeMillis();
        ArrayList<String> identificationDescription = new ArrayList<>();
        identificationDescription.add("Gathering all sub-elements of DomElement " + domElement.LogIdentification() + ". ");
        WebElementList webElementList = new WebElementList(domElement.asPositionBasedWebElement(web).childrenRecursive(), web, identificationDescription);
        webElementList.identificationDescription.add("Identified " + webElementList.elements.size() + " sub-elements of " + domElement.LogIdentification() + " in " + (System.currentTimeMillis() - startTime) + " ms. ");
        return webElementList;
    }

    public static WebElementList fromAllSubElementsOfElementWithText(String text, WebInteractionMethods web){
        long startTime = System.currentTimeMillis();
        DomElement domElement = new DomElement(text, DomElement.IdentificationType.BY_VISIBLE_TEXT);
        ArrayList<String> identificationDescription = new ArrayList<>();
        identificationDescription.add("Gathering all sub-elements of element with text '" + text + "'. Identified element " + domElement.LogIdentification() + " with that text. ");
        WebElementList webElementList = new WebElementList(domElement.asPositionBasedWebElement(web).childrenRecursive(), web, identificationDescription);
        webElementList.identificationDescription.add("Identified " + webElementList.elements.size() + " sub-elements of " + domElement.LogIdentification() + " in " + (System.currentTimeMillis() - startTime) + " ms. ");
        return webElementList;
    }

    public static WebElementList fromAllElementsInTheSameContainerObjectAs(DomElement domElement, WebInteractionMethods web){
        PositionBasedWebElement posElement = new PositionBasedWebElement(domElement, web);
        return fromAllSubElementsOf(posElement.parentElement().asDomElement(), web);
    }

    public static WebElementList fromAllElementsInTheSameContainerObjectAsElementWithText(String text, WebInteractionMethods web){
        long startTime = System.currentTimeMillis();
        DomElement domElement = new DomElement(text, DomElement.IdentificationType.BY_VISIBLE_TEXT);
        PositionBasedWebElement posElement = new PositionBasedWebElement(domElement, web);
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
