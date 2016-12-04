package se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.support.StringManagement;
import se.claremont.autotest.support.SupportMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for creating the DomElement descriptions for a draft page class for a WebDriver automation.
 * It traverses the current page the WebDriver is at and try to identify the elements on it and create
 * access methods for these. This class should be used to speed up page object creation and maintenance.
 * It should be possible to improve this class very much, but so far it produces code that is good
 * enough to paste into an existing page class. Hence it saves time already although it takes some
 * minutes to create the descriptors for large pages.
 *
 * Created by jordam on 2016-09-28.
 */
class WebPageCodeConstructor {

    private final static Logger logger = LoggerFactory.getLogger( WebPageCodeConstructor.class );

    private WebDriver driver;
    private Constructors constructors = new Constructors();
    List<String> methodNames = new ArrayList<>();

    private WebPageCodeConstructor(WebDriver driver){
        if(driver == null) return;
        this.driver = driver;
    }

    /**
     * Static method to create output file with DomElement descriptions for the web page.
     *
     * @param driver The web driver instance.
     * @param pathToOutputFile Path to the file to be written
     * @return Returns a string with a draft page class
     */
    @SuppressWarnings("UnusedReturnValue")
    public static String ConstructWebPageCode(WebDriver driver, String pathToOutputFile){
        WebPageCodeConstructor webPageCodeConstructor = new WebPageCodeConstructor(driver);
        String descriptors = "//Auto-generated with mapCurrentPage() method of WebInteractionMethods." + System.lineSeparator() +
                System.lineSeparator() + webPageCodeConstructor.constructWebPageCode();
        SupportMethods.saveToFile(descriptors, pathToOutputFile);
        return descriptors;
    }


    private String unusedMathodName(String suggestedMethodName){
        int elementCounter = 2;
        String methodNameToTry = suggestedMethodName;
        while (methodNames.contains(methodNameToTry)){
            methodNameToTry = suggestedMethodName + String.valueOf(elementCounter);
            elementCounter++;
        }
        methodNames.add(methodNameToTry);
        return methodNameToTry;
    }

    /**
     * This is the actual method that produce the DomElements for the page, and returns them as a string.
     *
     * @return Returns a string with the relevant objects.
     */
    private String constructWebPageCode(){
        List<String> domElementStrings = new ArrayList<>();
        StringBuilder element = new StringBuilder();
        List<WebElement> webElements = driver.findElements(By.xpath("//*"));
        for(WebElement webElement : webElements){
            if(webElement.getAttribute("id") != null && webElement.getAttribute("id").length() > 0){
                String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(webElement.getAttribute("id")) + "_" + tagNameToElementSuffix(webElement.getTagName());
                String suggestedElementConstrucorString = "\"" + webElement.getAttribute("id") + "\", DomElement.IdentificationType.BY_ID";
                constructors.addConstructor(new Constructor(unusedMathodName(suggestedElementName), suggestedElementConstrucorString));
            }
            else if(webElement.getAttribute("name") != null && webElement.getAttribute("name").length() > 0){
                String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(webElement.getAttribute("name")) + "_" + tagNameToElementSuffix(webElement.getTagName());
                String suggestedElementConstrucorString = "\"" + webElement.getAttribute("name") + "\", DomElement.IdentificationType.BY_NAME";
                constructors.addConstructor(new Constructor(unusedMathodName(suggestedElementName), suggestedElementConstrucorString));
            }
            else if(webElement.getTagName().equals("a")){
                if(webElement.getText() == null || webElement.getText().length() < 1) continue;
                String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(webElement.getText()) + "_" + "Link";
                String suggestedElementConstructor = "\"" + webElement.getText() + "\", DomElement.IdentificationType.BY_LINK_TEXT";
                constructors.addConstructor(new Constructor(unusedMathodName(suggestedElementName), suggestedElementConstructor));
            }
            //https://suitcss.github.io/
            else if( webElement.getAttribute("class") != null && webElement.getAttribute("class").length() > 0) {
                String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(webElement.getAttribute("class")) + "_" + tagNameToElementSuffix(webElement.getTagName());
                String suggestedElementConstrucorString = "\"" + webElement.getAttribute("class") + "\", DomElement.IdentificationType.BY_CLASS";
                constructors.addConstructor(new Constructor(unusedMathodName(suggestedElementName), suggestedElementConstrucorString));
            }
            else if(webElement.getText() != null && webElement.getText().length() > 0){
                if(driver.findElements(By.xpath("//*[contains(text(),'" + webElement.getText() + "')]")).size() == 1){
                    String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(webElement.getText()) + "_" + tagNameToElementSuffix(webElement.getTagName());
                    String suggestedElementConstructorString = "";
                    int numberOfElementsFound = driver.findElements(By.xpath("//*[contains(text(),'" + webElement.getText() + "')]")).size();
                    if(numberOfElementsFound != 1){
                        suggestedElementConstructorString = "//Warning: " + numberOfElementsFound + " elements found for this xpath query. " + SupportMethods.LF;
                    }
                    suggestedElementConstructorString += "\"//*[contains(text(),'" + webElement.getText().replace("\"", "\\\"") + "')]\", DomElement.IdentificationType.BY_X_PATH";
                    constructors.addConstructor(new Constructor(unusedMathodName(suggestedElementName), suggestedElementConstructorString));
                }
            }
        }
        return constructors.toString();
    }

    private static String tagNameToElementSuffix(String tagName){
        if(tagName.toLowerCase().equals("a")) return "Link";
        if(tagName.toLowerCase().equals("li")) return "ListItem";
        if(tagName.toLowerCase().equals("ul")) return "UnordereredList";
        if(tagName.toLowerCase().equals("ol")) return "NumberedList";
        if(tagName.toLowerCase().equals("h1")) return "MainHeading";
        if(tagName.toLowerCase().equals("h2")) return "Heading";
        if(tagName.toLowerCase().equals("h3")) return "SubHeading";
        if(tagName.toLowerCase().equals("p")) return "Paragraph";
        if(tagName.toLowerCase().equals("div")) return "Div";
        return StringManagement.firstUpperLetterTrailingLowerLetter(tagName);
    }

    private class Constructors extends ArrayList<Constructor>{
        int elementCounter = 1;

        public void addConstructor(Constructor constructor){
            if(hasUniqueName(constructor)){
                this.add(constructor);
            } else {
                constructor.elementName = constructor.elementName + Integer.toString(elementCounter);
                elementCounter++;
                this.add(constructor);
            }
        }

        public boolean hasUniqueName(Constructor constructor){
            for(Constructor constr : this){
                if(constr.elementName.equals(constructor.elementName)) return false;
            }
            return true;
        }

        public boolean hasUniqueDescriptor(Constructor constructor){
            for(Constructor constr : this){
                if(constr.constructorString.equals(constructor.constructorString)) return false;
            }
            return true;
        }

        public @Override String toString(){
            StringBuilder stringBuilder = new StringBuilder();
            for(Constructor constructor : this){
                stringBuilder.append(constructor.toString());
            }
            return stringBuilder.toString();
        }
    }

    private class Constructor{
        String elementName;
        String constructorString;

        public Constructor(String elementName, String constructorString){
            this.elementName = elementName;
            this.constructorString = constructorString;
            logger.debug( "Creating: " + SupportMethods.LF + this.toString() );
        }

        public @Override String toString(){
            return SupportMethods.LF + "public static DomElement " + elementName + "() {" + SupportMethods.LF + "    return new DomElement (" + constructorString + ");" + SupportMethods.LF + "}" + SupportMethods.LF;
        }
    }
}
