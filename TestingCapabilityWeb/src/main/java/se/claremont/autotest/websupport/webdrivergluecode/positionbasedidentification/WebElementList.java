package se.claremont.autotest.websupport.webdrivergluecode.positionbasedidentification;

import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.ElementsList;
import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement;
import se.claremont.autotest.websupport.DomElement;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

import java.util.ArrayList;

public class WebElementList extends ElementsList{

    WebElementList(ArrayList<PositionBasedWebElement> webElementList){
        elements = webElementList;
    }

    public WebElementList keepOnlyElementsWithText(){
        ArrayList<PositionBasedWebElement> returnList = new ArrayList<>();
        for(PositionBasedGuiElement element : elements){
            PositionBasedWebElement webElement = (PositionBasedWebElement) element;
            String text = webElement.getText();
            if(text != null && text.length() > 0){
                returnList.add(webElement);
            }
        }
        return new WebElementList(returnList);
    }

    public WebElementList keepOnlyVisibleElements(){
        ArrayList<PositionBasedWebElement> returnList = new ArrayList<>();
        for(PositionBasedGuiElement element : elements){
            PositionBasedWebElement webElement = (PositionBasedWebElement) element;
            if(webElement.isDisplayed()){
                returnList.add(webElement);
            }
        }
        return new WebElementList(returnList);
    }

    @Override
    public WebElementList keepElementsOfType(String typeName){
        return convertElementListToWebElementList(super.keepElementsOfType(typeName));
    }

    public WebElementList keepElementsToTheRightOf(DomElement domElement, WebInteractionMethods web){
        if(domElement == null || web == null) return new WebElementList(new ArrayList<>());
        return convertElementListToWebElementList(super.keepElementsToTheRightOf(domElement.asPositionBasedWebElement(web)));
    }

    public WebElementList keepElementsToTheLeftOf(DomElement domElement, WebInteractionMethods web){
        if(domElement == null || web == null) return new WebElementList(new ArrayList<>());
        return convertElementListToWebElementList(super.keepElementsToTheLeftOf(domElement.asPositionBasedWebElement(web)));
    }

    public WebElementList keepElementsAbove(DomElement domElement, WebInteractionMethods web){
        if(domElement == null || web == null) return new WebElementList(new ArrayList<>());
        return convertElementListToWebElementList(super.keepElementsAbove(domElement.asPositionBasedWebElement(web)));
    }

    public WebElementList keepElementsBelow(DomElement domElement, WebInteractionMethods web){
        if(domElement == null || web == null) return new WebElementList(new ArrayList<>());
        return convertElementListToWebElementList(super.keepElementsBelow(domElement.asPositionBasedWebElement(web)));
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
        if(domElement == null || web == null) return new WebElementList(new ArrayList<>());
        return convertElementListToWebElementList(super.atTheSameHeightAs(domElement.asPositionBasedWebElement(web)));
    }

    public WebElementList atTheSameHeightAs(DomElement domElement, WebInteractionMethods web, int marginPixelsAbove, int marginPixelsBelow){
        if(domElement == null || web == null) return new WebElementList(new ArrayList<>());
        return convertElementListToWebElementList(super.atTheSameHeightAs(domElement.asPositionBasedWebElement(web), marginPixelsAbove, marginPixelsBelow));
    }

    public WebElementList atTheSameHightAs(String elementText, WebInteractionMethods web){
        return convertElementListToWebElementList(super.atTheSameHeightAs(identifyDomElementFromText(elementText).asPositionBasedWebElement(web)));
    }

    public WebElementList atTheSameHeightAs(String elementText, WebInteractionMethods web, int marginPixelsAbove, int marginPixelsBelow){
        return convertElementListToWebElementList(super.atTheSameHeightAs(identifyDomElementFromText(elementText).asPositionBasedWebElement(web), marginPixelsAbove, marginPixelsBelow));
    }

    @Override
    public DomElement theObjectMostToTheRight(){
        PositionBasedGuiElement positionBasedGuiElement = (PositionBasedGuiElement)super.theObjectMostToTheRight();
        if(positionBasedGuiElement == null) return null;
        return ((PositionBasedWebElement)positionBasedGuiElement).asDomElement();
    }

    @Override
    public DomElement theObjectMostToTheLeft(){
        PositionBasedGuiElement positionBasedGuiElement = (PositionBasedGuiElement)super.theObjectMostToTheLeft();
        if(positionBasedGuiElement == null) return null;
        return ((PositionBasedWebElement)positionBasedGuiElement).asDomElement();
    }

    @Override
    public DomElement theObjectMostToTheTop(){
        PositionBasedGuiElement positionBasedGuiElement = (PositionBasedGuiElement)super.theObjectMostToTheTop();
        if(positionBasedGuiElement == null) return null;
        return ((PositionBasedWebElement)positionBasedGuiElement).asDomElement();
    }

    @Override
    public DomElement theObjectMostToTheBottom(){
        PositionBasedGuiElement positionBasedGuiElement = (PositionBasedGuiElement)super.theObjectMostToTheBottom();
        if(positionBasedGuiElement == null) return null;
        return ((PositionBasedWebElement)positionBasedGuiElement).asDomElement();
    }

    @Override
    public DomElement theOnlyElementThatShouldBeLeft(){
        PositionBasedGuiElement positionBasedGuiElement = (PositionBasedGuiElement)super.theOnlyElementThatShouldBeLeft();
        if(positionBasedGuiElement == null)return null;
        return ((PositionBasedWebElement)positionBasedGuiElement).asDomElement();
    }

    public DomElement elementImmediatelyToTheRightOf(DomElement domElement, WebInteractionMethods web){
        if(domElement == null || web == null) return null;
        PositionBasedGuiElement positionBasedGuiElement = (PositionBasedGuiElement)super.elementImmediatelyToTheRightOf(domElement.asPositionBasedWebElement(web));
        if(positionBasedGuiElement == null) return null;
        return ((PositionBasedWebElement) positionBasedGuiElement).asDomElement();
    }

    public DomElement elementImmediatelyToTheLeftOf(DomElement domElement, WebInteractionMethods web){
        if(domElement == null || web == null) return null;
        PositionBasedGuiElement positionBasedGuiElement = (PositionBasedGuiElement)super.elementImmediatelyToTheLeftOf(domElement.asPositionBasedWebElement(web));
        if(positionBasedGuiElement == null) return null;
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

    private static WebElementList convertElementListToWebElementList(ElementsList elementsList){
        ArrayList<PositionBasedWebElement> webElements = new ArrayList<>();
        for(PositionBasedGuiElement guiElement:elementsList.elements){
            webElements.add((PositionBasedWebElement)guiElement);
        }
        return new WebElementList(webElements);
    }

    private DomElement identifyDomElementFromText(String elementText){
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
            domElement = new DomElement(possibleElements.get(0));
        } else if(possibleElements.size() > 1) {
            for(PositionBasedWebElement element : possibleElements){
                if(element.getText().equals(elementText)){
                    domElement = new DomElement(element);
                    break;
                }
            }
        }
        if(domElement == null) domElement = new DomElement(elementText, DomElement.IdentificationType.BY_VISIBLE_TEXT);
        return domElement;
    }

}
