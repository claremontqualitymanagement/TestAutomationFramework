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
import java.util.HashMap;
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
    private static JavascriptExecutor javascriptDriver = null;
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
        javascriptDriver = (JavascriptExecutor)driver;
        web = new WebInteractionMethods(new TestCase(), driver);
        WebPageCodeConstructor webPageCodeConstructor = new WebPageCodeConstructor(driver);
        String descriptors = "//Auto-generated with mapCurrentPage() method of WebInteractionMethods." + System.lineSeparator() +
                System.lineSeparator() + webPageCodeConstructor.constructWebPageCodeElementByElement(mapEvenBadlyIdentifiedElements);
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
        javascriptDriver = (JavascriptExecutor)driver;
        web = new WebInteractionMethods(new TestCase(), driver);
        WebPageCodeConstructor webPageCodeConstructor = new WebPageCodeConstructor(driver);
        String descriptors = "//Auto-generated with mapCurrentPage() method of WebInteractionMethods." + System.lineSeparator() +
                //System.lineSeparator() + webPageCodeConstructor.constructWebPageCodeElementByElement(false);
                System.lineSeparator() + webPageCodeConstructor.constructWebPageCodeElementByElement(false);
        if(numberOfUnMappedElements > 0){
            descriptors += System.lineSeparator() + "//If you run the ConstructWebPageCode() method with mapEvenBadlyIdentifiedElements = true the " + numberOfUnMappedElements + " elements now currently not mapped will get Xpath identifications.";
        }
        SupportMethods.saveToFile(descriptors, pathToOutputFile);
        return descriptors;
    }

    public static String ConstructWebPageCodeThorough(WebDriver driver, String outputFilePath) {
        javascriptDriver = (JavascriptExecutor)driver;
        web = new WebInteractionMethods(new TestCase(), driver);
        WebPageCodeConstructor webPageCodeConstructor = new WebPageCodeConstructor(driver);
        String descriptors = "//Auto-generated with mapCurrentPage() method of WebInteractionMethods." + System.lineSeparator() +
                //System.lineSeparator() + webPageCodeConstructor.constructWebPageCodeElementByElement(false);
                System.lineSeparator() + webPageCodeConstructor.constructWebPageCodeByTreeTraversing();
        if(numberOfUnMappedElements > 0){
            descriptors += System.lineSeparator() + "//If you run the ConstructWebPageCode() method with mapEvenBadlyIdentifiedElements = true the " + numberOfUnMappedElements + " elements now currently not mapped will get Xpath identifications.";
        }
        SupportMethods.saveToFile(descriptors, outputFilePath);
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
    private String constructWebPageCodeElementByElement(boolean mapEvenBadlyIdentifiedElements){
        //In the best of worlds this method should recursively traverse down the DOM tree and try identifying leaf
        // nodes in the tree. If all leaf nodes for a parent are identified the parent does not have to be identified.
        List<WebElement> webElements = driver.findElements(By.xpath("//body//*"));
        for(WebElement webElement : webElements){
            Constructor constructor = attemptAddElementConstructor(webElement, mapEvenBadlyIdentifiedElements);
            if(constructor == null)continue;
            constructors.addConstructor(constructor);

        }
        return constructors.toString();
    }

    public String constructWebPageCodeByTreeTraversing(){
        addConstructorForSubElementsOf("//body");
        return constructors.toString();
    }

    private void addConstructorForSubElementsOf(String rootNodeXpath){
        for(WebElement child : getChildren(rootNodeXpath)){
            if(!hasBranschingChildren(generateXPATH(child, ""))){ //no child element has more than one child recursive over children until leaf
                Constructor constructor = attemptIdentifyElementConstructorRecursive(child);
                if(constructor != null) {
                    String nameSuggestion = identifyBestName(child);
                    if(nameSuggestion != null && nameSuggestion.length() > 0){
                        constructor.setName(nameSuggestion);
                    }
                    constructors.addConstructor(constructor);
                }
            } else { //At least one of the children, or childrens children recursive has branches
                addConstructorForSubElementsOf(generateXPATH(child, ""));
            }
        }
    }

    private String identifyBestName(WebElement element){
        String text = getAnyTextFromAnyChildren(element);
        if(text != null && text.length() > 0 && text.length() < 50)
            return StringManagement.methodNameWithOnlySafeCharacters(unusedMethodName(text.replace("\"", "\\\"") + "_" + tagNameToElementSuffix(element.getTagName())));
        text = getAnyAttributeFromAnyChildren(element);
        if(text != null && text.length() > 0)
            return StringManagement.methodNameWithOnlySafeCharacters(unusedMethodName(text.replace("\"", "\\\"") + "_" + tagNameToElementSuffix(element.getTagName())));
        return null;
    }

    private String getAnyAttributeFromAnyChildren(WebElement webElement){
        if(webElement == null)return null;
        Map<String, String> attributes = getAttributes(webElement);
        if(attributes != null && attributes.size() > 0){
            String attributeString = "";
            for(String attributeKey : attributes.keySet()){
                attributeString += attributeKey + "_" + attributes.get(attributeKey) + "_";
            }
            attributeString += tagNameToElementSuffix(webElement.getTagName());
            return StringManagement.methodNameWithOnlySafeCharacters(unusedMethodName(attributeString));
        }
        List<WebElement> children = getChildren(webElement);
        for(WebElement child : children){
            String attibutes = getAnyAttributeFromAnyChildren(child);
            if(attibutes != null){
                return attibutes;
            }
        }
        return null;
    }

    private WebElement getParent(WebElement child){
        WebElement parent = null;
        try{
            parent = child.findElement(By.xpath(".."));
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return parent;
    }

    private String getAnyTextFromAnyChildren(WebElement webElement){
        if(webElement == null) return null;
        List<WebElement> children = getChildren(webElement);
        if(children.size() == 0){ //Leaf
            String text = webElement.getText();
            if(text != null && text.length() > 0){
                return text;
            }
            WebElement parent = webElement;
            while (parent != null){
                parent = getParent(webElement);
                text = parent.getText();
                if(text != null && text.length() > 0){
                    return text;
                }
                webElement = parent;
            }
        } else {
            return getAnyTextFromAnyChildren(children.get(0));
        }
        return null;
    }

    private Constructor attemptIdentifyElementConstructorRecursive(WebElement webElement){
        Constructor constructor = attemptAddElementConstructor(webElement, false);
        if(constructor != null) return constructor;
        List<WebElement> children = getChildren(generateXPATH(webElement, ""));
        for(WebElement child : children){
            constructor = attemptIdentifyElementConstructorRecursive(child);
            if(constructor != null) return constructor;
        }
        return null;
    }

    private boolean hasBranschingChildren(String xpathOfParent){
        List<WebElement> children = getChildren(xpathOfParent);
        if(children.size() < 1) return false;
        if(children.size() > 1) return true;
        return hasBranschingChildren(xpathOfParent + "/*[1]");
    }


    private boolean isLeaf(String xpath){
        return getChildren(xpath).size() == 0;
    }

    private List<WebElement> getChildren(WebElement rootElement){
        String elementXpath = generateXPATH(rootElement, "");
        List<WebElement> directChildren = driver.findElements(By.xpath(elementXpath + "/*"));
        return directChildren;
    }

    private List<WebElement> getChildren(String rootElementXpath){
        List<WebElement> directChildren = driver.findElements(By.xpath(rootElementXpath + "/*"));
        return directChildren;
    }

    private Map<String, String> getAttributes(WebElement element){
        Map<String, Object> attributes = null;
        Map<String, String> returnMap = new HashMap<String, String>();
        if(element == null)return returnMap;
        try{
            attributes = (Map<String, Object>)javascriptDriver.executeScript("var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;", element) ;
        }catch (Exception e){
            System.out.println("Could not get attributes for element. " + e.toString());
        }
        if(attributes == null) return returnMap;
        for(String key : attributes.keySet()){
            returnMap.put(key, (String)attributes.get(key));
        }
        return returnMap;
    }

    private Constructor attemptAddElementConstructor(WebElement webElement, boolean mapEvenBadlyIdentifiedElements){
        try{
            String recognitionString = null;
            String id = webElement.getAttribute("id");
            String tagName = webElement.getTagName();
            if(id != null && id.length() > 0){
                String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(id) + "_" + tagNameToElementSuffix(tagName);
                recognitionString = id;
                if((suggestedElementName + recognitionString).length() < 200 && recognitionOnlyHasOneMatch(recognitionString, DomElement.IdentificationType.BY_ID)){
                    recognitionString = recognitionString.replace("\"", "\\\"");
                    String suggestedElementConstructorString = "\"" + recognitionString + "\", DomElement.IdentificationType.BY_ID";
                    return new Constructor(unusedMethodName(suggestedElementName), suggestedElementConstructorString);
                }
            }

            String name = webElement.getAttribute("name");
            if(name != null && name.length() > 0){
                String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(name) + "_" + tagNameToElementSuffix(tagName);
                recognitionString = name;
                if((suggestedElementName + recognitionString).length() < 200 && recognitionOnlyHasOneMatch(recognitionString, DomElement.IdentificationType.BY_NAME)){
                    recognitionString = recognitionString.replace("\"", "\\\"");
                    String suggestedElementConstructorString = "\"" + recognitionString + "\", DomElement.IdentificationType.BY_NAME";
                    return new Constructor(unusedMethodName(suggestedElementName), suggestedElementConstructorString);
                }
            }

            String text = null;
            if(tagName.equals("a")){
                text = webElement.getText();
                if(text != null && text.length() > 0) {
                    String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(text) + "_" + "Link";
                    recognitionString = text;
                    if (
                            !recognitionString.contains(System.lineSeparator()) &&
                                    !recognitionString.contains("\n") &&
                                    !recognitionString.contains("\r") &&
                                    (suggestedElementName + recognitionString).length() < 200 &&
                                    recognitionOnlyHasOneMatch(recognitionString, DomElement.IdentificationType.BY_LINK_TEXT)) {
                        recognitionString = recognitionString.replace("\"", "\\\"");
                        String suggestedElementConstructorString = "\"" + recognitionString + "\", DomElement.IdentificationType.BY_LINK_TEXT";
                        return new Constructor(unusedMethodName(suggestedElementName), suggestedElementConstructorString);
                    }
                }
            }

            //https://suitcss.github.io/
            String elementClass = webElement.getAttribute("class");
            if( elementClass != null && elementClass.length() > 0) {
                String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(elementClass) + "_" + tagNameToElementSuffix(tagName);
                recognitionString = elementClass;
                if((suggestedElementName + recognitionString).length() < 200 && recognitionOnlyHasOneMatch(recognitionString, DomElement.IdentificationType.BY_CLASS)){
                    recognitionString = recognitionString.replace("\"", "\\\"");
                    String suggestedElementConstructorString = "\"" + recognitionString + "\", DomElement.IdentificationType.BY_CLASS";
                    return new Constructor(unusedMethodName(suggestedElementName), suggestedElementConstructorString);
                }
            }

            Map<String, String> attributes = getAttributes(webElement);
            if(attributes != null){ //Maybe a combinatorical search for more than two attributes could be a futute feature?
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
                    recognitionString = recognitionString.replace("\"", "\\\"");
                    String suggestedElementConstructorString = "\"" + recognitionString + "\", DomElement.IdentificationType.BY_ATTRIBUTE_VALUE";
                    String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(recognitionString.replace("=", "_")) + "_" + tagNameToElementSuffix(tagName);
                    return new Constructor(unusedMethodName(suggestedElementName), suggestedElementConstructorString);
                } else if(attributes.size() > 1){
                    for(int i = 0; i < attributes.size()-1; i++){
                        for(int j = i+1; i < attributes.size(); i++){
                            String key1 = (String)attributes.keySet().toArray()[i];
                            String key2 = (String)attributes.keySet().toArray()[j];
                            recognitionString = "//" + tagName + "[@" + key1 + "='" + attributes.get(key1) + "' and @" + key2 + "='" + attributes.get(key2) + "']" ;
                            recognitionString = recognitionString.replace("\"", "\\\"");
                            if(recognitionString.length() < 200 && recognitionOnlyHasOneMatch(recognitionString, DomElement.IdentificationType.BY_X_PATH)){
                                matchFound = true;
                                String suggestedElementConstructorString = "\"" + recognitionString + "\", DomElement.IdentificationType.BY_ATTRIBUTE_VALUE";
                                String suggestedElementName = StringManagement.methodNameWithOnlySafeCharacters(recognitionString.replace("=", "_")) + "_" + tagNameToElementSuffix(tagName);
                                return new Constructor(unusedMethodName(suggestedElementName), suggestedElementConstructorString);
                            }
                        }
                    }
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
                        return new Constructor(unusedMethodName(suggestedElementName), suggestedElementConstructorString.replace("\"", "\\\""));
                    }
                }
            }

            //Default
            if(mapEvenBadlyIdentifiedElements){
                return new Constructor(unusedMethodName("Badly_identified_element"), "\"" + generateXPATH(webElement, "") + "\", DomElement.IdentificationType.BY_XPATH");
            } else {
                numberOfUnMappedElements++;
            }

        }catch (Exception e){ //Probably stale element
            System.out.println("Could not create costructor for element. " + e.toString());
        }
        return null;
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

        public void setName(String name){
            elementName = name;
        }

        public @Override String toString(){
            return SupportMethods.LF + "public static DomElement " + elementName + "() {" + SupportMethods.LF + "    return new DomElement (" + constructorString + ");" + SupportMethods.LF + "}" + SupportMethods.LF;
        }
    }
}
