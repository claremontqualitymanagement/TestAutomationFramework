package se.claremont.autotest.websupport.webdrivergluecode.positionbasedidentification;

import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.ElementsList;
import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.websupport.DomElement;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

import java.util.ArrayList;

public class WebElementList extends ElementsList{
    WebInteractionMethods web = null;
    ArrayList<String> identificationDescription = new ArrayList<>();

    WebElementList(ArrayList<PositionBasedWebElement> webElementList, WebInteractionMethods web, ArrayList<String> previousIdentificationDescription){
        elements = webElementList;
        this.web = web;
        this.identificationDescription.addAll(previousIdentificationDescription);
    }

    public WebElementList keepOnlyElementsWithText(){
        long startTime = System.currentTimeMillis();
        ArrayList<PositionBasedWebElement> returnList = new ArrayList<>();
        for(PositionBasedGuiElement element : elements){
            PositionBasedWebElement webElement = (PositionBasedWebElement) element;
            String text = webElement.getText();
            if(text != null && text.length() > 0){
                returnList.add(webElement);
            }
        }
        identificationDescription.add("From the element set of " + elements.size() + " element(s) only the " + returnList.size() + " element(s) containing any text were kept. " + duration(startTime));
        return new WebElementList(returnList, web, identificationDescription);
    }

    private static String duration(long startTime) {
        return (System.currentTimeMillis()-startTime) + " milliseconds spent in this operation. ";
    }

    public WebElementList keepOnlyVisibleElements(){
        long startTime = System.currentTimeMillis();
        ArrayList<PositionBasedWebElement> returnList = new ArrayList<>();
        for(PositionBasedGuiElement element : elements){
            PositionBasedWebElement webElement = (PositionBasedWebElement) element;
            if(webElement.isDisplayed()){
                returnList.add(webElement);
            }
        }
        identificationDescription.add("From the element set of " + elements.size() + " element(s) only the " + returnList.size() + " visible element(s) were kept. " + duration(startTime));
        return new WebElementList(returnList, web, identificationDescription);
    }

    @Override
    public WebElementList keepElementsOfType(String typeName){
        long startTime = System.currentTimeMillis();
        WebElementList returnList = convertElementListToWebElementList(super.keepElementsOfType(typeName), web, identificationDescription);
        returnList.identificationDescription.add("From the element set of " + elements.size() + " element(s) only the " + returnList.elements.size() + " element(s) of type '" + typeName + "' were kept. " + duration(startTime));
        return returnList;
    }

    public WebElementList keepElementsToTheRightOf(DomElement domElement, WebInteractionMethods web){
        long startTime = System.currentTimeMillis();
        if(domElement == null || web == null) {
            identificationDescription.add("Could not attempt to only keep elements to the right of reference element since WebInteractionMethods or the reference element was null. " + duration(startTime));
            return new WebElementList(new ArrayList<>(), web, identificationDescription);
        }
        WebElementList returnList = convertElementListToWebElementList(super.keepElementsToTheRightOf(domElement.asPositionBasedWebElement(web)), web, identificationDescription);
        returnList.identificationDescription.add("From the element set of " + elements.size() + " element(s) only the " + returnList.elements.size() + " element(s) to the right of element [" + domElement.LogIdentification() + "] were kept. ");
        return returnList;
    }

    public WebElementList keepElementsToTheLeftOf(DomElement domElement, WebInteractionMethods web){
        long startTime = System.currentTimeMillis();
        if(domElement == null || web == null) {
            identificationDescription.add("Could not attempt to only keep elements to the left of reference element since WebInteractionMethods or the reference element was null. " + duration(startTime));
            return new WebElementList(new ArrayList<>(), web, identificationDescription );
        }
        WebElementList returnList = convertElementListToWebElementList(super.keepElementsToTheLeftOf(domElement.asPositionBasedWebElement(web)), web, identificationDescription);
        returnList.identificationDescription.add("From the element set of " + elements.size() + " element(s) only the " + returnList.elements.size() + " element(s) to the left of element [" + domElement.LogIdentification() + "] were kept. " + duration(startTime));
        return returnList;
    }

    public WebElementList keepElementsAbove(DomElement domElement, WebInteractionMethods web){
        long startTime = System.currentTimeMillis();
        if(domElement == null || web == null) {
            identificationDescription.add("Could not attempt to only keep elements above the reference element since WebInteractionMethods or the reference element was null. " + duration(startTime));
            return new WebElementList(new ArrayList<>(), web, identificationDescription);
        }
        WebElementList returnList = convertElementListToWebElementList(super.keepElementsAbove(domElement.asPositionBasedWebElement(web)), web, identificationDescription);
        returnList.identificationDescription.add("From the element set of " + elements.size() + " element(s) only the " + returnList.elements.size() + " element(s) above the element [" + domElement.LogIdentification() + "] were kept. " + duration(startTime));
        return returnList;
    }

    public WebElementList keepElementsBelow(DomElement domElement, WebInteractionMethods web){
        long startTime = System.currentTimeMillis();
        if(domElement == null || web == null) {
            identificationDescription.add("Could not attempt to only keep elements below the reference element since WebInteractionMethods or the reference element was null. " + duration(startTime));
            return new WebElementList(new ArrayList<>(), web, identificationDescription);
        }
        WebElementList returnList = convertElementListToWebElementList(super.keepElementsBelow(domElement.asPositionBasedWebElement(web)), web, identificationDescription);
        returnList.identificationDescription.add("From the element set of " + elements.size() + " element(s) only the " + returnList.elements.size() + " element(s) below the element [" + domElement.LogIdentification() + "] were kept. " + duration(startTime));
        return returnList;
    }

    public WebElementList keepElementsToTheRightOf(String elementText, WebInteractionMethods web){
        return keepElementsToTheRightOf(identifyDomElementFromText(elementText), web);
    }

    public WebElementList keepElementsToTheLeftOf(String elementText, WebInteractionMethods web){
        return keepElementsToTheLeftOf(identifyDomElementFromText(elementText), web);
    }

    public WebElementList keepElementsAbove(String elementText, WebInteractionMethods web){
        return keepElementsAbove(identifyDomElementFromText(elementText), web);
    }

    public WebElementList keepElementsBelow(String elementText, WebInteractionMethods web){
        return keepElementsBelow(identifyDomElementFromText(elementText), web);
    }

    public WebElementList atTheSameHightAs(DomElement domElement, WebInteractionMethods web){
        long startTime = System.currentTimeMillis();
        if(domElement == null || web == null) {
            identificationDescription.add("Could not attempt to identify the elements at the same height as the reference element since either the reference element or the WebInteractionMethods object was null. " + duration(startTime));
            return new WebElementList(new ArrayList<>(), web, identificationDescription);
        }
        WebElementList returnList = convertElementListToWebElementList(super.atTheSameHeightAs(domElement.asPositionBasedWebElement(web)), web, identificationDescription);
        returnList.identificationDescription.add("From the element set of " + elements.size() + " element(s) only the " + returnList.elements.size() + " element(s) at the same hight as element [" + domElement.LogIdentification() + "] were kept. " + duration(startTime));
        return returnList;
    }

    public WebElementList atTheSameHeightAs(DomElement domElement, WebInteractionMethods web, int marginPixelsAbove, int marginPixelsBelow){
        long startTime = System.currentTimeMillis();
        if(domElement == null || web == null) {
            identificationDescription.add("Could not attempt to identify the elements at the same height as the reference element since either the reference element or the WebInteractionMethods object was null. " + duration(startTime));
            return new WebElementList(new ArrayList<>(), web, identificationDescription);
        }
        WebElementList returnList = convertElementListToWebElementList(super.atTheSameHeightAs(domElement.asPositionBasedWebElement(web), marginPixelsAbove, marginPixelsBelow), web, identificationDescription);
        returnList.identificationDescription.add("From the element set of " + elements.size() + " element(s) only the " + returnList.elements.size() + " element(s) at the same hight as element [" + domElement.LogIdentification() + "] were kept (margin above " + marginPixelsAbove + "px and margin below " + marginPixelsBelow + "px.) " + duration(startTime));
        return returnList;
    }

    public WebElementList atTheSameHightAs(String elementText, WebInteractionMethods web){
        long startTime = System.currentTimeMillis();
        if(web == null) {
            identificationDescription.add("Could not attempt to identify the elements at the same height as the reference element since the WebInteractionMethods object was null. " + duration(startTime));
            return new WebElementList(new ArrayList<>(), web, identificationDescription);
        }
        WebElementList returnList = convertElementListToWebElementList(super.atTheSameHeightAs(identifyDomElementFromText(elementText).asPositionBasedWebElement(web)), web, identificationDescription);
        returnList.identificationDescription.add("From the element set of " + elements.size() + " element(s) only the " + returnList.elements.size() + " element(s) at the same hight as element with test " + elementText + "' were kept. " + duration(startTime));
        return returnList;
    }

    public WebElementList atTheSameHeightAs(String elementText, WebInteractionMethods web, int marginPixelsAbove, int marginPixelsBelow){
        long startTime = System.currentTimeMillis();
        if(elementText == null || web == null) {
            identificationDescription.add("Could not attempt to identify the elements at the same height as the reference element since either the reference element or the WebInteractionMethods object was null. " + duration(startTime));
            return new WebElementList(new ArrayList<>(), web, identificationDescription);
        }
        WebElementList returnList = convertElementListToWebElementList(super.atTheSameHeightAs(identifyDomElementFromText(elementText).asPositionBasedWebElement(web), marginPixelsAbove, marginPixelsBelow), web, identificationDescription);
        returnList.identificationDescription.add("From the element set of " + elements.size() + " element(s) only the " + returnList.elements.size() + " element(s) at the same hight as element with text '" + elementText + "' were kept (margin above " + marginPixelsAbove + "px and margin below " + marginPixelsBelow + "px.) " + duration(startTime));
        return returnList;
    }

    @Override
    public DomElement theObjectMostToTheRight(){
        long startTime = System.currentTimeMillis();
        PositionBasedGuiElement positionBasedGuiElement = (PositionBasedGuiElement)super.theObjectMostToTheRight();
        if(positionBasedGuiElement == null) {
            identificationDescription.add("Tried returning a DomElement of the element most to the right, but could not identify any element. Returning null. " + duration(startTime));
            attemptLog();
            return null;
        }
        identificationDescription.add("Returning the element most to the right as a DomElement. " + duration(startTime));
        attemptLog();
        return ((PositionBasedWebElement)positionBasedGuiElement).asDomElement();
    }

    @Override
    public DomElement theObjectMostToTheLeft(){
        long startTime = System.currentTimeMillis();
        PositionBasedGuiElement positionBasedGuiElement = (PositionBasedGuiElement)super.theObjectMostToTheLeft();
        if(positionBasedGuiElement == null) {
            identificationDescription.add("Tried returning a DomElement of the element most to the left, but could not identify any element. Returning null. " + duration(startTime));
            attemptLog();
            return null;
        }
        identificationDescription.add("Returning the element most to the left as a DomElement. " + duration(startTime));
        attemptLog();
        return ((PositionBasedWebElement)positionBasedGuiElement).asDomElement();
    }

    @Override
    public DomElement theObjectMostToTheTop(){
        long startTime = System.currentTimeMillis();
        PositionBasedGuiElement positionBasedGuiElement = (PositionBasedGuiElement)super.theObjectMostToTheTop();
        if(positionBasedGuiElement == null) {
            identificationDescription.add("Tried returning a DomElement of the element most to the top, but could not identify any element. Returning null. " + duration(startTime));
            attemptLog();
            return null;
        }
        identificationDescription.add("Returning the element most to the top as a DomElement. " + duration(startTime));
        attemptLog();
        return ((PositionBasedWebElement)positionBasedGuiElement).asDomElement();
    }

    @Override
    public DomElement theObjectMostToTheBottom(){
        long startTime = System.currentTimeMillis();
        PositionBasedGuiElement positionBasedGuiElement = (PositionBasedGuiElement)super.theObjectMostToTheBottom();
        if(positionBasedGuiElement == null) {
            identificationDescription.add("Tried returning a DomElement of the element most to the bottom, but could not identify any element. Returning null. " + duration(startTime));
            attemptLog();
            return null;
        }
        identificationDescription.add("Returning the element most to the bottom as a DomElement. " + duration(startTime));
        attemptLog();
        return ((PositionBasedWebElement)positionBasedGuiElement).asDomElement();
    }

    @Override
    public DomElement theOnlyElementThatShouldBeLeft(){
        long startTime = System.currentTimeMillis();
        PositionBasedGuiElement positionBasedGuiElement = (PositionBasedGuiElement)super.theOnlyElementThatShouldBeLeft();
        if(positionBasedGuiElement == null){
            identificationDescription.add("Tried returning a DomElement of the only element that should be left in the collection, but could not identify any element. Returning null. " + duration(startTime));
            attemptLog();
            return null;
        }
        identificationDescription.add("Returning the only element that should be left in the collection (actually returning the first element) as a DomElement. " + duration(startTime));
        attemptLog();;
        return ((PositionBasedWebElement)positionBasedGuiElement).asDomElement();
    }

    public DomElement elementImmediatelyToTheRightOf(DomElement domElement, WebInteractionMethods web){
        long startTime = System.currentTimeMillis();
        if(domElement == null || web == null) {
            identificationDescription.add("Could not identify element immediately to the right of reference element since the reference element or the WebInteractionMethods object was null. Returning null. " + duration(startTime));
            attemptLog();
            return null;
        }
        PositionBasedGuiElement positionBasedGuiElement = (PositionBasedGuiElement)super.elementImmediatelyToTheRightOf(domElement.asPositionBasedWebElement(web));
        if(positionBasedGuiElement == null) {
            identificationDescription.add("Could not identify element immediately to the right of reference element since no suitable element could be found. Returning null. " + duration(startTime));
            attemptLog();
            return null;
        }
        identificationDescription.add("Identified a suitable element being immediately to the right of DomElement [" + domElement.LogIdentification() + "]. Returning the following element: " + positionBasedGuiElement.toString() + ". " + duration(startTime));
        attemptLog();
        return ((PositionBasedWebElement) positionBasedGuiElement).asDomElement();
    }

    public DomElement elementImmediatelyToTheLeftOf(DomElement domElement, WebInteractionMethods web){
        long startTime = System.currentTimeMillis();
        if(domElement == null || web == null) {
            identificationDescription.add("Could not identify element immediately to the left of reference element since the reference element or the WebInteractionMethods object was null. Returning null. " + duration(startTime));
            attemptLog();
            return null;
        }
        PositionBasedGuiElement positionBasedGuiElement = (PositionBasedGuiElement)super.elementImmediatelyToTheLeftOf(domElement.asPositionBasedWebElement(web));
        if(positionBasedGuiElement == null) {
            identificationDescription.add("Could not identify element immediately to the left of reference element since no suitable element could be found. Returning null. " + duration(startTime));
            attemptLog();
            return null;
        }
        identificationDescription.add("Identified a suitable element being immediately to the left of DomElement [" + domElement.LogIdentification() + "]. Returning the following element: " + positionBasedGuiElement.toString() + ". " + duration(startTime));
        attemptLog();
        return ((PositionBasedWebElement) positionBasedGuiElement).asDomElement();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[WebElementList: ");
        for(PositionBasedGuiElement element : elements){
            sb.append(element).toString();
        }
        sb.append("]");
        return sb.toString();
    }

    private static WebElementList convertElementListToWebElementList(ElementsList elementsList, WebInteractionMethods web, ArrayList<String> previousIdentificationDescription){
        long startTime = System.currentTimeMillis();
        ArrayList<PositionBasedWebElement> webElements = new ArrayList<>();
        for(PositionBasedGuiElement guiElement:elementsList.elements){
            webElements.add((PositionBasedWebElement)guiElement);
        }
        return new WebElementList(webElements, web, previousIdentificationDescription);
    }

    private DomElement identifyDomElementFromText(String elementText){
        long startTime = System.currentTimeMillis();
        DomElement domElement = null;
        ArrayList<PositionBasedWebElement> possibleElements = new ArrayList<>();
        for(PositionBasedGuiElement element : elements){
            PositionBasedWebElement positionBasedWebElement = (PositionBasedWebElement) element;
            String text = positionBasedWebElement.getText();
            if(text == null) continue;
            if(text.contains(elementText)){
                possibleElements.add(positionBasedWebElement);
            }
        }
        if(possibleElements.size() == 1){
            identificationDescription.add("Found one single element containing the text '" + elementText + ". Making a DomElement from it. " + duration(startTime));
            domElement = new DomElement(possibleElements.get(0));
        } else if(possibleElements.size() > 1) {
            for(PositionBasedWebElement element : possibleElements){
                if(element.getText().equals(elementText)){
                    identificationDescription.add("Found " + possibleElements.size() + " element containing the text '" + elementText + "', but also found element with exact match for the text. Making a DomElement from it. " + duration(startTime));
                    domElement = new DomElement(element);
                    break;
                }
            }
        }
        if(domElement == null) {
            identificationDescription.add("Could not find any element containing the text '" + elementText + "', but created a DomElement regardsless for chance of it to appear. " + duration(startTime));
            domElement = new DomElement(elementText, DomElement.IdentificationType.BY_VISIBLE_TEXT);
        }
        return domElement;
    }

    private void attemptLog(){
        StringBuilder sb = new StringBuilder();
        sb.append("Procedure for identifying element by position:" + System.lineSeparator());
        for(int i = 1; i < identificationDescription.size() + 1; i++){
            sb.append("  ").append(i).append("). ").append(identificationDescription.get(i-1)).append(System.lineSeparator());
        }
        if(web == null){
            System.out.println(sb.toString());
        }else {
            web.log(LogLevel.DEBUG, sb.toString());
        }
    }

}
