package se.claremont.autotest.websupport.webdrivergluecode;

import org.openqa.selenium.WebElement;

/**
 * Created by jordam on 2016-10-02.
 */
@SuppressWarnings("WeakerAccess")
public class PositionBasedWebElement implements se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement {
    public int left;
    public int right;
    public int top;
    public int bottom;
    public String typeName;
    public Object returnElement;

    public PositionBasedWebElement(WebElement webElement){
        this.left = webElement.getLocation().x;
        this.right = webElement.getLocation().x + webElement.getSize().width;
        this.top = webElement.getLocation().y;
        this.bottom = webElement.getLocation().y + webElement.getSize().height;
        this.typeName = webElement.getTagName();
        this.returnElement = webElement;
    }
}
