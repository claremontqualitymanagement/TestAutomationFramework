package se.claremont.autotest.eyeautomatesupport;

import se.claremont.autotest.common.guidriverpluginstructure.GuiElement;

/**
 * Created by jordam on 2017-01-27.
 */
public class GuiImageElement implements GuiElement {
    public String getImageFilePath() {
        return imageFilePath;
    }

    private String imageFilePath;

    public String getName() {
        return name;
    }

    private String name;

    public GuiImageElement(String imageFilePath){
        this.imageFilePath = imageFilePath;
    }

    public GuiImageElement(String imageFilePath, String name){
        this.imageFilePath = imageFilePath;
        this.name = name;
    }

}

