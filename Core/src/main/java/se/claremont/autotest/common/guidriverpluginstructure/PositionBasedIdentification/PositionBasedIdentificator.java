package se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2016-10-02.
 */
public class PositionBasedIdentificator {

    public static ElementsList fromAllTheElements(ArrayList<PositionBasedGuiElement> elements){
        return new ElementsList(elements);
    }

    public static ElementsList fromAllTheElements(ElementsList elements){
        return elements;
    }
}
