package se.claremont.autotest.websupport.webdrivergluecode.positionbasedidentification;

import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.ElementsList;
import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement;

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
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[WebElementList: ");
        for(PositionBasedGuiElement element : elements){
            sb.append(element).toString();
        }
        sb.append("]");
        return sb.toString();
    }

}
