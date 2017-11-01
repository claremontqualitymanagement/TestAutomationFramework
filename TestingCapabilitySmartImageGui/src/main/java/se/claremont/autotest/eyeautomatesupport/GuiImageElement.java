package se.claremont.autotest.eyeautomatesupport;

import se.claremont.autotest.common.guidriverpluginstructure.GuiElement;

/**
 * A screen captured image of the element to interact with
 *
 * Created by jordam on 2017-01-27.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class GuiImageElement implements GuiElement {

    private String imageFilePath;
    private String name;

    public GuiImageElement(String imageFilePath){
        this.imageFilePath = imageFilePath;
    }

    public GuiImageElement(String imageFilePath, String name){
        this.imageFilePath = imageFilePath;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getImageFilePath() {
        return imageFilePath;
    }

    @Override
    public String toString(){
        if(name == null || name.trim().length() == 0) name = "NoNameElement";
        if(imageFilePath == null || imageFilePath.trim().length() == 0) imageFilePath = "NoPath";
        return "'" + name + "' (" + imageFilePath + ")";
    }

    public String toHtml(){
        if(name == null || name.trim().length() == 0) name = "<span class=\"noname\">NoNameElement</span>";
        if(imageFilePath == null || imageFilePath.trim().length() == 0) imageFilePath = "<span class=\"noname\">NoPath</span>";
        return "<div class=\"guiimageelement\">" +
                "'" + name + "' element (" + imageFilePath + ")." +
                "<br>" +
                "<a href=\"file://" + imageFilePath.replace("<span class=\"noname\">", "").replace("</span>", "") + "\" class=\"guielementimagelink\" target=\"_blank\">" +
                "<img src=\"file://" + imageFilePath.replace("<span class=\"noname\">", "").replace("</span>", "") + "\" " +
                "alt=\"" + name.replace("<span class=\"noname\">", "").replace("</span>", "") + "\" " +
                "class=\"guielementimage\">" +
                "<br>" +
                "file://" + imageFilePath +
                "</a>" +
                "</div>";
    }

}

