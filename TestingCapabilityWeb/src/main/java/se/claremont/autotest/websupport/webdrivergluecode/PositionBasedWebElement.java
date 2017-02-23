package se.claremont.autotest.websupport.webdrivergluecode;

import org.openqa.selenium.WebElement;

/**
 * Web element that is being accessed by its relative position to another element
 *
 * Created by jordam on 2016-10-02.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class PositionBasedWebElement implements se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement {
    public final int left;
    public final int right;
    public final int top;
    public final int bottom;
    public final String typeName;
    public final Object returnElement;

    public PositionBasedWebElement(WebElement webElement){
        this.left = webElement.getLocation().x;
        this.right = webElement.getLocation().x + webElement.getSize().width;
        this.top = webElement.getLocation().y;
        this.bottom = webElement.getLocation().y + webElement.getSize().height;
        this.typeName = webElement.getTagName();
        this.returnElement = webElement;
    }

    @Override
    public int getLeftPosition() {
        return left;
    }

    @Override
    public int getRightPosition() {
        return right;
    }

    @Override
    public int getTopPosition() {
        return top;
    }

    @Override
    public int getBottomPosition() {
        return bottom;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }
}
