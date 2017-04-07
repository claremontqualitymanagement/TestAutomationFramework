package se.claremont.autotest.websupport.webdrivergluecode;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.websupport.DomElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private final Constructors constructors = new Constructors();
    private final List<String> methodNames = new ArrayList<>();
    private static WebInteractionMethods web;
    private static int numberOfUnMappedElements = 0;

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
    static String ConstructWebPageCode(WebDriver driver, String pathToOutputFile, boolean mapEvenBadlyIdentifiedElements){
        web = new WebInteractionMethods(new TestCase(), driver);
        WebPageCodeConstructor webPageCodeConstructor = new WebPageCodeConstructor(driver);
        String descriptors = "//Auto-generated with mapCurrentPage() method of WebInteractionMethods." + System.lineSeparator() +
                System.lineSeparator() + webPageCodeConstructor.constructWebPageCode(mapEvenBadlyIdentifiedElements);
        if(numberOfUnMappedElements > 0){
            descriptors += System.lineSeparator() + "//If you run the ConstructWebPageCode() method with mapEvenBadlyIdentifiedElements = true the " + numberOfUnMappedElements + " elements now currently not mapped will get Xpath identifications.";
        }
        SupportMethods.saveToFile(descriptors, pathToOutputFile);
        return descriptors;
    }

    /**
     * Static method to create output file with DomElement descriptions for the web page.
     *
     * @param driver The web driver instance.
     * @param pathToOutputFile Path to the file to be written
     * @return Returns a string with a draft page class
     */
    @SuppressWarnings("UnusedReturnValue")
    static String ConstructWebPageCode(WebDriver driver, String pathToOutputFile){
        web = new WebInteractionMethods(new TestCase(), driver);
        WebPageCodeConstructor webPageCodeConstructor = new WebPageCodeConstructor(driver);
        String descriptors = "//Auto-generated with mapCurrentPage() method of WebInteractionMethods." + System.lineSeparator() +
                System.lineSeparator() + webPageCodeConstructor.constructWebPageCode(false);
        if(numberOfUnMappedElements > 0){
            descriptors += System.lineSeparator() + "//If you run the ConstructWebPageCode() method with mapEvenBadlyIdentifiedElements = true the " + numberOfUnMappedElements + " elements now currently not mapped will get Xpath identifications.";
        }
        SupportMethods.saveToFile(descriptors, pathToOutputFile);
        return descriptors;
    }

    private String unusedMethodName(String suggestedMethodName){
        int elementCounter = 2;
        String methodNameToTry = suggestedMethodName;
        while (methodNameAlreadyUsed(methodNameToTry)){
            methodNameToTry = suggestedMethodName + String.valueOf(elementCounter);
            elementCounter++;
        }
        methodNames.add(methodNameToTry);
        return methodNameToTry;
    }

    private boolean methodNameAlreadyUsed(String nameToTry){
        return methodNames.stream().anyMatch(m -> m.equals(nameToTry));
    }

    /**
     * This is the actual method that produce the DomElements for the page, and returns them as a string.
     *
     * @return Returns a string with the relevant objects.
     */
    private String constructWebPageCode(boolean mapEvenBadlyIdentifiedElements){
        List<WebElement> webElements = driver.findElements(By.xpath("//*"));
        JavascriptExecutor javascriptDriver = (JavascriptExecutor)driver;
        for(WebElement webElement : webElements){
            try{
                String recognitionString = null;
                String id = webElement.getAttribute("id");
                String tagName = webElement.getTagName();
                if(id != null && id.length() > 0){
                    String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(id) + "_" + tagNameToElementSuffix(tagName);
                    recognitionString = id;
                    if((suggestedElementName + recognitionString).length() < 200 && recognitionOnlyHasOneMatch(recognitionString, DomElement.IdentificationType.BY_ID)){
                        String suggestedElementConstructorString = "\"" + recognitionString + "\", DomElement.IdentificationType.BY_ID";
                        constructors.addConstructor(new Constructor(unusedMethodName(suggestedElementName), suggestedElementConstructorString));
                        continue;
                    }
                }

                String name = webElement.getAttribute("name");
                if(name != null && name.length() > 0){
                    String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(name) + "_" + tagNameToElementSuffix(tagName);
                    recognitionString = name;
                    if((suggestedElementName + recognitionString).length() < 200 && recognitionOnlyHasOneMatch(recognitionString, DomElement.IdentificationType.BY_NAME)){
                        String suggestedElementConstructorString = "\"" + name + "\", DomElement.IdentificationType.BY_NAME";
                        constructors.addConstructor(new Constructor(unusedMethodName(suggestedElementName), suggestedElementConstructorString));
                        continue;
                    }
                }

                String text = null;
                if(tagName.equals("a")){
                    text = webElement.getText();
                    if(text == null || text.length() < 1) continue;
                    String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(text) + "_" + "Link";
                    recognitionString = text;
                    if(
                        !recognitionString.contains(System.lineSeparator()) &&
                        !recognitionString.contains("\n") &&
                        !recognitionString.contains("\r") &&
                        (suggestedElementName + recognitionString).length() < 200 &&
                        recognitionOnlyHasOneMatch(recognitionString, DomElement.IdentificationType.BY_LINK_TEXT))
                    {
                        String suggestedElementConstructor = "\"" + text + "\", DomElement.IdentificationType.BY_LINK_TEXT";
                        constructors.addConstructor(new Constructor(unusedMethodName(suggestedElementName), suggestedElementConstructor));
                        continue;
                    }
                }

                //https://suitcss.github.io/
                String elementClass = webElement.getAttribute("class");
                if( elementClass != null && elementClass.length() > 0) {
                    String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(elementClass) + "_" + tagNameToElementSuffix(tagName);
                    recognitionString = elementClass;
                    if((suggestedElementName + recognitionString).length() < 200 && recognitionOnlyHasOneMatch(recognitionString, DomElement.IdentificationType.BY_CLASS)){
                        String suggestedElementConstructorString = "\"" + recognitionString + "\", DomElement.IdentificationType.BY_CLASS";
                        constructors.addConstructor(new Constructor(unusedMethodName(suggestedElementName), suggestedElementConstructorString));
                        continue;
                    }
                }

                Map<String, Object> attributes = null;
                try{
                    attributes = (Map<String, Object>)javascriptDriver.executeScript("var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;", webElement) ;
                }catch (Exception e){
                    System.out.println("Could not get attributes for element. " + e.toString());
                }
                if(attributes != null){ //Maybe a combinatorical search for attributes could be a futute feature?
                    boolean matchFound = false;
                    for(String key : attributes.keySet()){
                        if(key.toLowerCase().equals("class") || key.toLowerCase().equals("id")) continue;
                        recognitionString = key + "=" + attributes.get(key);
                        if(recognitionString.length() < 150 && recognitionOnlyHasOneMatch(recognitionString, DomElement.IdentificationType.BY_ATTRIBUTE_VALUE)){
                            matchFound = true;
                            break;
                        }
                    }
                    if(matchFound){
                        String suggestedElementConstructorString = "\"" + recognitionString + "\", DomElement.IdentificationType.BY_ATTRIBUTE_VALUE";
                        String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(recognitionString.replace("=", "_")) + "_" + tagNameToElementSuffix(tagName);
                        constructors.addConstructor(new Constructor(unusedMethodName(suggestedElementName), suggestedElementConstructorString));
                        continue;
                    }
                }

                if(text == null){
                    text = webElement.getText();
                }
                if(text != null &&
                        text.length() > 0 &&
                        !text.contains(System.lineSeparator()) &&
                        !text.contains("\n") &&
                        !text.contains("\r") &&
                        text.length() < 200)
                {
                    List<WebElement> matchingElements = null;
                    try {
                        matchingElements = driver.findElements(By.xpath("//" + tagName + "[contains(text(),'" + webElement.getText() + "')]"));
                    } catch (Exception e) {
                        System.out.println("Problems matching elements for page contruction: " + e.toString());
                    }
                    if (matchingElements != null && matchingElements.size() == 1) {
                        String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(webElement.getText()) + "_" + tagNameToElementSuffix(tagName);
                        String suggestedElementConstructorString = "";
                        int numberOfElementsFound = driver.findElements(By.xpath("//" + tagName + "[contains(text(),'" + text + "')]")).size();
                        if (numberOfElementsFound == 1) {
                            suggestedElementConstructorString += "\"//" + tagName + "[contains(text(),'" + text.replace("\"", "\\\"") + "')]\", DomElement.IdentificationType.BY_X_PATH";
                            constructors.addConstructor(new Constructor(unusedMethodName(suggestedElementName), suggestedElementConstructorString));
                            continue;
                        }
                    }
                }


                //Default
                if(mapEvenBadlyIdentifiedElements){
                    constructors.addConstructor(new Constructor(unusedMethodName("Badly_identified_element"), "\"" + generateXPATH(webElement, "") + "\", DomElement.IdentificationType.BY_XPATH"));
                } else {
                    numberOfUnMappedElements++;
                }

            }catch (Exception e){ //Probably stale element
                System.out.println("Could not create costructor for element. " + e.toString());
            }
        }
        return constructors.toString();
    }

    private static String generateXPATH(WebElement childElement, String current) {
        String childTag = childElement.getTagName();
        if(childTag.equals("html")) {
            return "/html[1]"+current;
        }
        WebElement parentElement = childElement.findElement(By.xpath(".."));
        List<WebElement> childrenElements = parentElement.findElements(By.xpath("*"));
        int count = 0;
        for(int i=0;i<childrenElements.size(); i++) {
            WebElement childrenElement = childrenElements.get(i);
            String childrenElementTag = childrenElement.getTagName();
            if(childTag.equals(childrenElementTag)) {
                count++;
            }
            if(childElement.equals(childrenElement)) {
                return generateXPATH(parentElement, "/" + childTag + "[" + count + "]"+current);
            }
        }
        return null;
    }

    private static boolean recognitionOnlyHasOneMatch(String suggestedRecognitionString, DomElement.IdentificationType identificationType){
        Integer numberOfMatches = web.getRuntimeElementMatchCount(new DomElement(suggestedRecognitionString, identificationType));
        return numberOfMatches == 1;
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

        void addConstructor(Constructor constructor){
            if(hasUniqueName(constructor)){
                this.add(constructor);
            } else {
                constructor.elementName = constructor.elementName + Integer.toString(elementCounter);
                elementCounter++;
                this.add(constructor);
            }
        }

        boolean hasUniqueName(Constructor constructor){
            for(Constructor constr : this){
                if(constr.elementName.equals(constructor.elementName)) return false;
            }
            return true;
        }

        @SuppressWarnings("unused")
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
        final String constructorString;

        Constructor(String elementName, String constructorString){
            this.elementName = elementName;
            this.constructorString = constructorString;
            logger.debug( "Creating: " + SupportMethods.LF + this.toString() );
            System.out.println("Creating element: " + System.lineSeparator() + toString());
        }

        public @Override String toString(){
            return SupportMethods.LF + "public static DomElement " + elementName + "() {" + SupportMethods.LF + "    return new DomElement (" + constructorString + ");" + SupportMethods.LF + "}" + SupportMethods.LF;
        }
    }
}
