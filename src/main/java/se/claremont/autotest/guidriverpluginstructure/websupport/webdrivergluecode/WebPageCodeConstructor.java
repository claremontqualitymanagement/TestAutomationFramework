package se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
    private WebDriver driver;
    private Constructors constructors = new Constructors();

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
        String descriptors = webPageCodeConstructor.constructWebPageCode();
        SupportMethods.saveToFile(descriptors, pathToOutputFile);
        return descriptors;
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
            System.out.println(webElement.toString());
            if(webElement.getAttribute("id") != null && webElement.getAttribute("id").length() > 0){
                String suggestedElementName = methodNameWithOnlySafeCharacters(webElement.getAttribute("id")) + "_" + tagNameToElementSuffix(webElement.getTagName());
                String suggestedElementConstrucorString = "\"" + webElement.getAttribute("id") + "\", DomElement.IdentificationType.BY_ID";
                constructors.addConstructor(new Constructor(suggestedElementName, suggestedElementConstrucorString));
            }
            else if(webElement.getAttribute("name") != null && webElement.getAttribute("name").length() > 0){
                String suggestedElementName = methodNameWithOnlySafeCharacters(webElement.getAttribute("name")) + "_" + tagNameToElementSuffix(webElement.getTagName());
                String suggestedElementConstrucorString = "\"" + webElement.getAttribute("name") + "\", DomElement.IdentificationType.BY_NAME";
                constructors.addConstructor(new Constructor(suggestedElementName, suggestedElementConstrucorString));
            }
            else if(webElement.getTagName().equals("a")){
                if(webElement.getText() == null || webElement.getText().length() < 1) continue;
                String suggestedElementName = methodNameWithOnlySafeCharacters(webElement.getText()) + "_" + "Link";
                String suggestedElementConstructor = "\"" + webElement.getText() + "\", DomElement.IdentificationType.BY_LINK_TEXT";
                constructors.addConstructor(new Constructor(suggestedElementName, suggestedElementConstructor));
            }
            else if(webElement.getText() != null && webElement.getText().length() > 0){
                if(driver.findElements(By.xpath("//*[contains(text(),'" + webElement.getText() + "')]")).size() == 1){
                    String suggestedElementName = methodNameWithOnlySafeCharacters(webElement.getText()) + "_" + tagNameToElementSuffix(webElement.getTagName());
                    String suggestedElementConstructorString = "";
                    int numberOfElementsFound = driver.findElements(By.xpath("//*[contains(text(),'" + webElement.getText() + "')]")).size();
                    if(numberOfElementsFound != 1){
                        suggestedElementConstructorString = "//Warning: " + numberOfElementsFound + " elements found for this xpath query. " + SupportMethods.LF;
                    }
                    suggestedElementConstructorString += "\"//*[contains(text(),'" + webElement.getText().replace("\"", "\\\"") + "')]\", DomElement.IdentificationType.BY_X_PATH";
                    constructors.addConstructor(new Constructor(suggestedElementName, suggestedElementConstructorString));
                }
            }
        }
        return constructors.toString();
    }

    /**
     * Method naming should only consist of method name safe characters, and be formatted according to method naming conventions in java, and according to coding guidelines.
     *
     * @param instring The string to convert
     * @return Returns the converted string
     */
    private static String methodNameWithOnlySafeCharacters(String instring){
        String returnString = "";
        for(String spaceDividedWord : instring.split(" ")){
            for(String dashDividedWord : spaceDividedWord.split("-")){
                for(String underscoreDividedWord : dashDividedWord.split("_")){
                    returnString += firstUpperLetterTrailingLowerLetter(underscoreDividedWord);
                }

            }
        }
        return returnString.
                replace(" ", "").
                replace(",", "").
                replace("–", "_").
                replace(".", "_").
                replace("%", "").
                replace("&", "").
                replace("$", "").
                replace("\\", "_").
                replace("\"", "").
                replace("'", "").
                replace("!", "").
                replace("-", "_").
                replace("©", "Copyright").
                replace("å", "a").
                replace("ä", "a").
                replace("ö", "o").
                replace("Å", "A").
                replace("Ä", "A").
                replace("Ö", "O");
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
        return firstUpperLetterTrailingLowerLetter(tagName);
    }

    private static String firstUpperLetterTrailingLowerLetter(String instring){
        String returnString = instring.substring(0,1).toUpperCase();
        if(instring.length() > 1){
            returnString += instring.substring(1).toLowerCase();
        }
        return returnString;

    }

    private class Constructors extends ArrayList<Constructor>{
        int elementCounter = 1;

        public boolean addConstructor(Constructor constructor){
            if(hasUniqueName(constructor)){
                this.add(constructor);
            } else {
                constructor.elementName = constructor.elementName + Integer.toString(elementCounter);
                elementCounter++;
                this.add(constructor);
            }
            return false;
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
            System.out.println("Creating: " + SupportMethods.LF + this.toString());
        }

        public @Override String toString(){
            return SupportMethods.LF + "public static DomElement " + elementName + "() {" + SupportMethods.LF + "    return new DomElement (" + constructorString + ");" + SupportMethods.LF + "}" + SupportMethods.LF;
        }
    }
}
