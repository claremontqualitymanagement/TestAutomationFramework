package se.claremont.autotest.guidriverpluginstructure.PositionBasedIdentification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2016-10-02.
 */
public class PositionBasedIdentificator {

    public static ElementsList fromAllTheElements(ArrayList<PositionBasedGuiElement> elements){
        return (ElementsList) elements;
    }

    private class ElementsList extends ArrayList<PositionBasedGuiElement>{

        public ElementsList keepElementsOfType(String typeName){
            ElementsList returnElements = new ElementsList();
            for(PositionBasedGuiElement possibleElement : this){
                if(possibleElement.typeName.equals(typeName)){
                    returnElements.add(possibleElement);
                }
            }
            return returnElements;
        }

        public ElementsList keepElementsToTheRightOf(PositionBasedGuiElement relativeElement){
            ElementsList returnElements = new ElementsList();
            for (PositionBasedGuiElement possibleElement : this){
                if(possibleElement.left > relativeElement.right) {
                    returnElements.add(possibleElement);
                }
            }
            return returnElements;
        }

        public ElementsList keepElementsToTheLeftOf(PositionBasedGuiElement relativeElement){
            ElementsList returnElements = new ElementsList();
            for (PositionBasedGuiElement possibleElement : this){
                if(possibleElement.right < relativeElement.left) {
                    returnElements.add(possibleElement);
                }
            }
            return returnElements;
        }

        public ElementsList keepElementsAbove(PositionBasedGuiElement relativeElement){
            ElementsList returnElements = new ElementsList();
            for (PositionBasedGuiElement possibleElement : this){
                if(possibleElement.bottom < relativeElement.top) {
                    returnElements.add(possibleElement);
                }
            }
            return returnElements;
        }

        public ElementsList keepElementsBelow(PositionBasedGuiElement relativeElement){
            ElementsList returnElements = new ElementsList();
            for (PositionBasedGuiElement possibleElement : this){
                if(possibleElement.top > relativeElement.bottom) {
                    returnElements.add(possibleElement);
                }
            }
            return returnElements;
        }

        public ElementsList atTheSameHeightAs(PositionBasedGuiElement relativeElement, int marginPixelsAbove, int marginPixelsBelow){
            ElementsList returnElements = new ElementsList();
            for(PositionBasedGuiElement possibleElement : this){
                if(possibleElement.top > relativeElement.top - marginPixelsAbove &&
                   possibleElement.bottom < relativeElement.bottom + marginPixelsBelow &&
                   possibleElement.bottom > relativeElement.top &&
                   possibleElement.top < relativeElement.bottom){
                    returnElements.add(possibleElement);
                }
            }
            return returnElements;
        }

        public PositionBasedGuiElement theObjectMostToTheRight(){
            PositionBasedGuiElement returnElement = null;
            for(PositionBasedGuiElement element : this){
                int mostRight;
                if(returnElement == null){
                    mostRight = 0;
                } else {
                    mostRight = returnElement.right;
                }
                if(element.right > mostRight){
                    returnElement = element;
                }
            }
            return returnElement;
        }

        public PositionBasedGuiElement theObjectMostToTheBottom(){
            PositionBasedGuiElement returnElement = null;
            for(PositionBasedGuiElement element : this){
                int mostBottom;
                if(returnElement == null){
                    mostBottom = 0;
                } else {
                    mostBottom = returnElement.bottom;
                }
                if(element.bottom > mostBottom){
                    returnElement = element;
                }
            }
            return returnElement;
        }

        public PositionBasedGuiElement theObjectMostToTheTop(){
            PositionBasedGuiElement returnElement = null;
            for(PositionBasedGuiElement element : this){
                int mostOnTop;
                if(returnElement == null){
                    mostOnTop = Integer.MAX_VALUE;
                } else {
                    mostOnTop = returnElement.top;
                }
                if(element.top < mostOnTop){
                    returnElement = element;
                }
            }
            return returnElement;
        }

        public PositionBasedGuiElement theObjectMostToTheLeft(){
            PositionBasedGuiElement returnElement = null;
            for(PositionBasedGuiElement element : this){
                int mostToTheLeft;
                if(returnElement == null){
                    mostToTheLeft = Integer.MAX_VALUE;
                } else {
                    mostToTheLeft = returnElement.left;
                }
                if(element.left < mostToTheLeft){
                    returnElement = element;
                }
            }
            return returnElement;
        }
    }


}
