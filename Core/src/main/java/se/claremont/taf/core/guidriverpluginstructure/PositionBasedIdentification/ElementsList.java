package se.claremont.taf.core.guidriverpluginstructure.PositionBasedIdentification;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of elements relevant for further evaluation to identify a specific element by its position relative to other elements.
 *
 * Created by jordam on 2017-02-22.
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public class ElementsList {
    public List<? extends PositionBasedGuiElement> elements;

    public ElementsList(List<? extends PositionBasedGuiElement> elements){
        this.elements = elements;
    }

    public ElementsList(){
        this.elements = new ArrayList<>();
    }

    public ElementsList keepElementsOfType(String typeName){
        ArrayList<PositionBasedGuiElement> returnElements = new ArrayList<>();

        for(PositionBasedGuiElement possibleElement : this.elements){
            String possibleElementTypeName = possibleElement.getTypeName();
            if(possibleElementTypeName == null) continue;
            if(possibleElementTypeName.equals(typeName)){
                returnElements.add(possibleElement);
            }
        }
        if(returnElements.size() == 0){ //User might have misunderstood object type naming convention
            for(PositionBasedGuiElement possibleElement : this.elements){
                String possibleElementTypeName = possibleElement.getTypeName();
                if(possibleElementTypeName == null) continue;
                if(possibleElementTypeName.contains(typeName)){
                    returnElements.add(possibleElement);
                }
            }
        }
        return new ElementsList(returnElements);
    }

    public ElementsList keepElementsToTheRightOf(PositionBasedGuiElement relativeElement){
        ArrayList<PositionBasedGuiElement> returnElements = new ArrayList<>();
        Integer relativeElementPosition = relativeElement.getRightPosition();
        if(relativeElementPosition == null) return new ElementsList(returnElements);
        for (PositionBasedGuiElement possibleElement : this.elements){
            Integer possibleElementPosition = possibleElement.getLeftPosition();
            if(possibleElementPosition == null) continue;
            if(possibleElementPosition > relativeElementPosition) {
                returnElements.add(possibleElement);
            }
        }
        return new ElementsList(returnElements);
    }

    public ElementsList keepElementsToTheLeftOf(PositionBasedGuiElement relativeElement){
        ArrayList<PositionBasedGuiElement> returnElements = new ArrayList<>();
        Integer relativeElementPosition = relativeElement.getLeftPosition();
        if(relativeElementPosition == null) return new ElementsList(returnElements);
        for (PositionBasedGuiElement possibleElement : this.elements){
            Integer possibleElementPosition = possibleElement.getRightPosition();
            if(possibleElementPosition == null) continue;
            if(possibleElementPosition < relativeElementPosition) {
                returnElements.add(possibleElement);
            }
        }
        return new ElementsList(returnElements);
    }

    public ElementsList keepElementsAbove(PositionBasedGuiElement relativeElement){
        ArrayList<PositionBasedGuiElement> returnElements = new ArrayList<>();
        Integer relativeElementPosition = relativeElement.getTopPosition();
        if(relativeElementPosition == null) return new ElementsList(returnElements);
        for (PositionBasedGuiElement possibleElement : this.elements){
            Integer possibleElementPosition = possibleElement.getBottomPosition();
            if(possibleElementPosition == null) continue;
            if(possibleElementPosition < relativeElementPosition) {
                returnElements.add(possibleElement);
            }
        }
        return new ElementsList(returnElements);
    }

    public ElementsList keepElementsBelow(PositionBasedGuiElement relativeElement){
        ArrayList<PositionBasedGuiElement> returnElements = new ArrayList<>();
        Integer relativeElementPosition = relativeElement.getBottomPosition();
        if(relativeElementPosition == null) return new ElementsList(returnElements);
        for (PositionBasedGuiElement possibleElement : this.elements){
            Integer possibleElementPosition = possibleElement.getTopPosition();
            if(possibleElementPosition == null) continue;
            if(possibleElementPosition > relativeElementPosition) {
                returnElements.add(possibleElement);
            }
        }
        return new ElementsList(returnElements);
    }

    public ElementsList atTheSameHeightAs(PositionBasedGuiElement relativeElement){
        return atTheSameHeightAs(relativeElement, 10, 10);
    }

    public ElementsList atTheSameHeightAs(PositionBasedGuiElement relativeElement, int marginPixelsAbove, int marginPixelsBelow){
        ArrayList<PositionBasedGuiElement> returnElements = new ArrayList<>();
        Integer relativeElementTopPosition = relativeElement.getTopPosition();
        if(relativeElementTopPosition == null)return new ElementsList(returnElements);
        Integer relativeElementBottomPosition = relativeElement.getBottomPosition();
        if(relativeElementBottomPosition == null) return new ElementsList(returnElements);
        //System.out.println("Relative element '" + relativeElement.getTypeName() + "': top=" + relativeElementTopPosition+ ", bottom=" + relativeElementBottomPosition);
        for(PositionBasedGuiElement possibleElement : this.elements){
            if(possibleElement.equals(relativeElement))continue;
            Integer possibleElementBottomPosition = possibleElement.getBottomPosition();
            if(possibleElementBottomPosition == null)continue;
            if(possibleElementBottomPosition > relativeElementBottomPosition + marginPixelsBelow) continue;
            Integer possibleElementTopPosition = possibleElement.getTopPosition();
            if(possibleElementTopPosition == null)continue;
            if(possibleElementTopPosition < relativeElementTopPosition - marginPixelsAbove)continue;
           // System.out.println("Possible element '" + possibleElement.getTypeName() + "': top=" + possibleElementTopPosition + ", bottom=" + possibleElementBottomPosition);
            if(possibleElementTopPosition >= relativeElementTopPosition - marginPixelsAbove && //Nedanför överkantens marginal
                    possibleElementBottomPosition <= relativeElementBottomPosition + marginPixelsBelow //&& //Ovanför nederkantens marginal
                    //possibleElementBottomPosition >= relativeElementBottomPosition - marginPixelsBelow //&&
                    //possibleElementTopPosition <= relativeElementTopPosition + marginPixelsAbove){
                            ){
                returnElements.add(possibleElement);
            }
        }
        return new ElementsList(returnElements);
    }

    public PositionBasedGuiElement theObjectMostToTheRight(){
        PositionBasedGuiElement returnElement = null;
        int mostRight = Integer.MIN_VALUE;
        for(PositionBasedGuiElement element : this.elements){
            Integer position = element.getRightPosition();
            if(position == null) continue;
            if(position > mostRight){
                returnElement = element;
                mostRight = position;
            }
        }
        return returnElement;
    }

    public PositionBasedGuiElement theObjectMostToTheBottom(){
        PositionBasedGuiElement returnElement = null;
        int mostExtreme = Integer.MIN_VALUE;
        for(PositionBasedGuiElement element : this.elements){
            Integer position = element.getBottomPosition();
            if(position == null) continue;
            if(position > mostExtreme){
                returnElement = element;
                mostExtreme = position;
            }
        }
        return returnElement;
    }

    public PositionBasedGuiElement theObjectMostToTheTop(){
        PositionBasedGuiElement returnElement = null;
        int mostExtreme = Integer.MAX_VALUE;
        for(PositionBasedGuiElement element : this.elements){
            Integer position = element.getTopPosition();
            if(position == null) continue;
            if(position < mostExtreme){
                returnElement = element;
                mostExtreme = position;
            }
        }
        return returnElement;
    }

    public PositionBasedGuiElement theObjectMostToTheLeft(){
        PositionBasedGuiElement returnElement = null;
        int mostExtreme = Integer.MAX_VALUE;
        for(PositionBasedGuiElement element : this.elements){
            Integer position = element.getBottomPosition();
            if(position == null) continue;
            if(position < mostExtreme){
                returnElement = element;
                mostExtreme = position;
            }
        }
        return returnElement;
    }

    public PositionBasedGuiElement theOnlyElementThatShouldBeLeft(){
        if(elements.size() > 0) return elements.get(0);
        return null;
    }

    @Override
    public String toString(){
        StringBuilder returnString = new StringBuilder();
        for(PositionBasedGuiElement element :elements){
            returnString.append(element.toString()).append(System.lineSeparator());
        }
        return returnString.toString();
    }

    public PositionBasedGuiElement elementImmediatelyToTheRightOf(PositionBasedGuiElement element){
        return atTheSameHeightAs(element).keepElementsToTheRightOf(element).theObjectMostToTheLeft();
    }

    public PositionBasedGuiElement elementImmediatelyToTheLeftOf(PositionBasedGuiElement element){
        return atTheSameHeightAs(element).keepElementsToTheLeftOf(element).theObjectMostToTheRight();
    }
}
