package se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification;

import java.util.ArrayList;

/**
 * Created by jordam on 2017-02-22.
 */
public class ElementsList {
    ArrayList<PositionBasedGuiElement> elements;

    public ElementsList(ArrayList<PositionBasedGuiElement> elements){
        this.elements = elements;
    }

    public ElementsList(){
        this.elements = new ArrayList<>();
    }

    void add(PositionBasedGuiElement positionBasedGuiElement){
        if(this.elements == null){
            this.elements = new ArrayList<>();
        }
        this.elements.add(positionBasedGuiElement);
    }

    public ElementsList(PositionBasedGuiElement positionBasedGuiElement){
        if(this.elements == null){
            this.elements = new ArrayList<>();
        }
        this.elements.add(positionBasedGuiElement);
    }

    public ElementsList(PositionBasedGuiElement[] positionBasedGuiElements){
        if(this.elements == null){
            this.elements = new ArrayList<>();
        }
        for(PositionBasedGuiElement positionBasedGuiElement : positionBasedGuiElements){
            this.elements.add(positionBasedGuiElement);
        }
    }

    public ElementsList keepElementsOfType(String typeName){
        ElementsList returnElements = new ElementsList();
        for(PositionBasedGuiElement possibleElement : this.elements){
            if(possibleElement.getTypeName().equals(typeName)){
                returnElements.add(possibleElement);
            }
        }
        if(returnElements.elements.size() == 0){ //User might have misunderstood object type naming convention
            for(PositionBasedGuiElement possibleElement : this.elements){
                if(possibleElement.getTypeName().contains(typeName)){
                    returnElements.add(possibleElement);
                }
            }
        }
        return returnElements;
    }

    public ElementsList keepElementsToTheRightOf(PositionBasedGuiElement relativeElement){
        ElementsList returnElements = new ElementsList();
        for (PositionBasedGuiElement possibleElement : this.elements){
            if(possibleElement.getLeftPosition() > relativeElement.getRightPosition()) {
                returnElements.add(possibleElement);
            }
        }
        return returnElements;
    }

    public ElementsList keepElementsToTheLeftOf(PositionBasedGuiElement relativeElement){
        ElementsList returnElements = new ElementsList();
        for (PositionBasedGuiElement possibleElement : this.elements){
            if(possibleElement.getRightPosition() < relativeElement.getLeftPosition()) {
                returnElements.add(possibleElement);
            }
        }
        return returnElements;
    }

    public ElementsList keepElementsAbove(PositionBasedGuiElement relativeElement){
        ElementsList returnElements = new ElementsList();
        for (PositionBasedGuiElement possibleElement : this.elements){
            if(possibleElement.getBottomPosition() < relativeElement.getTopPosition()) {
                returnElements.add(possibleElement);
            }
        }
        return returnElements;
    }

    public ElementsList keepElementsBelow(PositionBasedGuiElement relativeElement){
        ElementsList returnElements = new ElementsList();
        for (PositionBasedGuiElement possibleElement : this.elements){
            if(possibleElement.getTopPosition() > relativeElement.getBottomPosition()) {
                returnElements.add(possibleElement);
            }
        }
        return returnElements;
    }

    public ElementsList atTheSameHeightAs(PositionBasedGuiElement relativeElement, int marginPixelsAbove, int marginPixelsBelow){
        ElementsList returnElements = new ElementsList();
        for(PositionBasedGuiElement possibleElement : this.elements){
            if(possibleElement.getTopPosition() > relativeElement.getTopPosition() - marginPixelsAbove &&
                    possibleElement.getBottomPosition() < relativeElement.getBottomPosition() + marginPixelsBelow &&
                    possibleElement.getBottomPosition() > relativeElement.getTopPosition() &&
                    possibleElement.getTopPosition() < relativeElement.getBottomPosition()){
                returnElements.add(possibleElement);
            }
        }
        return returnElements;
    }

    public PositionBasedGuiElement theObjectMostToTheRight(){
        PositionBasedGuiElement returnElement = null;
        for(PositionBasedGuiElement element : this.elements){
            int mostRight;
            if(returnElement == null){
                mostRight = 0;
            } else {
                mostRight = returnElement.getRightPosition();
            }
            if(element.getRightPosition() > mostRight){
                returnElement = element;
            }
        }
        return returnElement;
    }

    public PositionBasedGuiElement theObjectMostToTheBottom(){
        PositionBasedGuiElement returnElement = null;
        for(PositionBasedGuiElement element : this.elements){
            int mostBottom;
            if(returnElement == null){
                mostBottom = 0;
            } else {
                mostBottom = returnElement.getBottomPosition();
            }
            if(element.getBottomPosition() > mostBottom){
                returnElement = element;
            }
        }
        return returnElement;
    }

    public PositionBasedGuiElement theObjectMostToTheTop(){
        PositionBasedGuiElement returnElement = null;
        for(PositionBasedGuiElement element : this.elements){
            int mostOnTop;
            if(returnElement == null){
                mostOnTop = Integer.MAX_VALUE;
            } else {
                mostOnTop = returnElement.getTopPosition();
            }
            if(element.getTopPosition() < mostOnTop){
                returnElement = element;
            }
        }
        return returnElement;
    }

    public PositionBasedGuiElement theObjectMostToTheLeft(){
        PositionBasedGuiElement returnElement = null;
        for(PositionBasedGuiElement element : this.elements){
            int mostToTheLeft;
            if(returnElement == null){
                mostToTheLeft = Integer.MAX_VALUE;
            } else {
                mostToTheLeft = returnElement.getLeftPosition();
            }
            if(element.getLeftPosition() < mostToTheLeft){
                returnElement = element;
            }
        }
        return returnElement;
    }

    public PositionBasedGuiElement theOnlyElementThatShouldBeLeft(){
        if(elements.size() > 0) return elements.get(0);
        return null;
    }
}
