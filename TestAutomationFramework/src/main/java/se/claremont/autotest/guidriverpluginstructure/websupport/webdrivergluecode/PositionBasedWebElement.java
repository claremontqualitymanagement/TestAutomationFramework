package se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode;

import org.openqa.selenium.WebElement;
import se.claremont.autotest.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement;

/**
 * Created by jordam on 2016-10-02.
 */
@SuppressWarnings("WeakerAccess")
public class PositionBasedWebElement implements PositionBasedGuiElement{
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
