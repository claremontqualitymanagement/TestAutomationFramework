package se.claremont.autotest.websupport.webdrivergluecode.positionbasedidentification;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement;
import se.claremont.autotest.websupport.DomElement;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * Web element that is being accessed by its relative position to another element
 *
 * Created by jordam on 2016-10-02.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class PositionBasedWebElement implements PositionBasedGuiElement {
    private Integer left = null;
    private Integer right = null;
    private Integer top = null;
    private Integer bottom = null;
    private String typeName = null;
    private String text = null;
    private Boolean displayed = null;

    public final WebElement webElement;
    private WebDriver driver = null;

    public PositionBasedWebElement(DomElement domElement, WebInteractionMethods web){
        this.webElement = web.getRuntimeElementWithoutLogging(domElement);
        this.driver = web.driver;
    }

    public PositionBasedWebElement(String elementText, WebInteractionMethods web){
        this.driver = web.driver;
        this.text = elementText;
        webElement = web.getRuntimeElementWithoutLogging(new DomElement(text, DomElement.IdentificationType.BY_VISIBLE_TEXT));
    }

    public PositionBasedWebElement(WebElement webElement){
        this.webElement = webElement;
    }

    private WebDriver getDriver(){
        if(driver == null){
            try{
                driver = ((org.openqa.selenium.WrapsDriver) webElement).getWrappedDriver();
            }catch (Exception ignored){ }
        }
        return driver;
    }

    public WebElement getWebElement(){
        return webElement;
    }

    public ArrayList<PositionBasedWebElement> childrenImmediate(){
        List<WebElement> subElements = webElement.findElements(By.xpath("/*"));
        ArrayList<PositionBasedWebElement> positionBasedWebElements = new ArrayList<>();
        for(WebElement we : subElements){
            positionBasedWebElements.add(new PositionBasedWebElement(we));
        }
        return positionBasedWebElements;

    }

    public WebElement asWebElement() {return getWebElement(); }

    public DomElement asDomElement(){
        return new DomElement(webElement);
    }

    public DomElement asDomElement(String elementName, String elementPageName) { return new DomElement(webElement, elementName, elementPageName); }

    public ArrayList<PositionBasedWebElement> childrenRecursive(){
        List<WebElement> subElements = webElement.findElements(By.xpath("//*"));
        ArrayList<PositionBasedWebElement> positionBasedWebElements = new ArrayList<>();
        for(WebElement we : subElements){
            positionBasedWebElements.add(new PositionBasedWebElement(we));
        }
        return positionBasedWebElements;
    }

    @Override
    public Integer getLeftPosition() {
        if(left == null){
            try {
                left = webElement.getLocation().x;
            }catch (Exception ignored){} //Stale element exception
        }
        return left;
    }

    @Override
    public Integer getRightPosition() {
        if(right == null){
            try{
                right = webElement.getLocation().x + webElement.getSize().width;
            }catch (Exception ignored){} //Stale element exception
        }
        return right;
    }

    @Override
    public Integer getTopPosition() {
        if(top == null){
            try{
                top = webElement.getLocation().y;
            }catch (Exception ignored) {} //Stale element exception
        }
        return top;
    }

    @Override
    public Integer getBottomPosition() {
        if(bottom == null){
            try{
                bottom = webElement.getLocation().y + webElement.getSize().height;
            }catch (Exception ignored){} //Stale element exception
        }
        return bottom;
    }

    @Override
    public String getTypeName() {
        if(typeName == null){
            typeName = webElement.getTagName();
        }
        return typeName;
    }

    @Override
    public Object runtimeElement() {
        return webElement;
    }

    public String getText(){
        if(text == null){
            try {
                text = webElement.getText();
            }catch (Exception ignored){}
        }
        return text;
    }

    @Override
    public ArrayList<PositionBasedGuiElement> childElements() {
        ArrayList<PositionBasedGuiElement> list = new ArrayList<>();
        for(PositionBasedWebElement element : childrenRecursive()){
            list.add(element);
        }
        return list;
    }

    public PositionBasedWebElement parentElement(){
        WebElement parent = null;
        try{
            parent = this.webElement.findElement(By.xpath(".."));
        }catch (Exception ignored){

        }
        return new PositionBasedWebElement(parent);
    }

    public boolean isDisplayed(){
        if(displayed == null) {
            try {
                displayed = webElement.isDisplayed();
            } catch (Exception e) {
                displayed = false;
            }
        }
        return displayed;
    }

    @Override
    public String toString(){
        return "[PositionBasedWebElement: ElementType='" + getTypeName() + "', " +
                "text='" + getText() + "', " +
                "top=" + getTopPosition() + ", " +
                "bottom=" + getBottomPosition() + ", " +
                "left=" + getLeftPosition() + ", " +
                "right=" + getRightPosition() + ", " +
                "displayed=" + String.valueOf(isDisplayed()) +
                "]";
    }
}
