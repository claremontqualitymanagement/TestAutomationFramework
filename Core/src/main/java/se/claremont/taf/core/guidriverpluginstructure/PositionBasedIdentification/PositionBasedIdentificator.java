package se.claremont.taf.core.guidriverpluginstructure.PositionBasedIdentification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Set of static methods to enable identification of GUI elements by their relative position to other elements.
 *
 * Created by jordam on 2016-10-02.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class PositionBasedIdentificator {

    public static ElementsList fromAllThePositionBasedElements(List<? extends PositionBasedGuiElement> elements){
        return new ElementsList(elements);
    }

    public static ElementsList fromAllTheElements(ElementsList elements){
        return elements;
    }

    public static ElementsList fromAllSubElementsOf(PositionBasedGuiElement positionBasedGuiElement){
        return new ElementsList(positionBasedGuiElement.childElements());
    }


    public static ElementsList fromAllTheElements(List<Object> elements){
        List<PositionBasedGuiElement> returnElements = new ArrayList<>();
        for(Object element : elements){
            try{
                returnElements.add((PositionBasedGuiElement)element);
            }catch (Exception e){
                System.out.println("Could not cast element '" + element.toString() + "' of type '" + element.getClass().toString() + "' to a PositionBasedGuiElement.");
            }
        }
        return new ElementsList(returnElements);
    }

    public static ElementsList fromAllTheElements(Object[] elements){
        List<Object> objects = new ArrayList<>();
        Collections.addAll(objects, elements);
        return fromAllTheElements(objects);
    }

}
