package se.claremont.autotest.javasupport.interaction.elementidentification;

import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;

import java.awt.*;
import java.util.ArrayList;

public class SearchCondition {
    public Object[] objects;
    public SearchConditionType searchConditionType;

    public SearchCondition(SearchConditionType searchConditionType, Object... objects) {
        this.searchConditionType = searchConditionType;
        this.objects = objects;
    }

    @Override
    public String toString(){
        String returnString = searchConditionType.toString() + ": ";
        java.util.List<String> objectDescriptions = new ArrayList<>();
        for(Object o : objects){
            if(o.getClass().getSimpleName().equals(JavaGuiElement.class.getSimpleName())){
                objectDescriptions.add(((JavaGuiElement)o).getName());
            } else if (Component.class.isAssignableFrom(o.getClass())){
                objectDescriptions.add(new JavaGuiElement(o.getClass()).getName());
            } else {
                objectDescriptions.add(o.toString());
            }
        }
        return returnString + "'" + String.join("', '", objectDescriptions) + "'";
    }
}
