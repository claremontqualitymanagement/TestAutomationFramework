package se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2016-10-02.
 */
public class PositionBasedIdentificator {

    public static ElementsList fromAllTheElements(ArrayList<? extends PositionBasedGuiElement> elements){
        return new ElementsList(elements);
    }

    public static ElementsList fromAllTheElements(ElementsList elements){
        return elements;
    }

    public static ElementsList fromAllTheElements(List<Object> elements){
        ArrayList<PositionBasedGuiElement> returnElements = new ArrayList<>();
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
        for(Object element:elements){
            objects.add(element);
        }
        return fromAllTheElements(objects);
    }

}
