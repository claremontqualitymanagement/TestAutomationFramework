package se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import se.claremont.autotest.support.SupportMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2016-09-28.
 */
public class WebPageCodeConstructor {
    WebDriver driver;
    Constructors constructors = new Constructors();

    private WebPageCodeConstructor(WebDriver driver){
        if(driver == null) return;
        this.driver = driver;
    }

    public static String ConstructWebPageCode(WebDriver driver, String pathToOutputFile){
        WebPageCodeConstructor webPageCodeConstructor = new WebPageCodeConstructor(driver);
        String descriptors = webPageCodeConstructor.constructWebPageCode();
        SupportMethods.saveToFile(descriptors, pathToOutputFile);
        return descriptors;
    }

    private String constructWebPageCode(){
        List<String> domElementStrings = new ArrayList<>();
        StringBuilder element = new StringBuilder();
        List<WebElement> webElements = driver.findElements(By.xpath("//*"));
        for(WebElement webElement : webElements){
            if(webElement.getAttribute("id") != null){
                String suggestedElementName = webElement.getAttribute("id") + webElement.getTagName().replace("a", "Link");
                String suggestedElementConstrucorString = "\\\"\" + webElement.getAttribute(\"id\") + \"\", DomElement.IdentificationType.BY_ID";
                constructors.add(new Constructor(suggestedElementName, suggestedElementConstrucorString));
            }
            else if(webElement.getAttribute("name") != null){
                String suggestedElementName = webElement.getAttribute("name") + webElement.getTagName().replace("a", "Link");
                String suggestedElementConstrucorString = "\"" + webElement.getAttribute("name") + "\", DomElement.IdentificationType.BY_NAME";
                constructors.add(new Constructor(suggestedElementName, suggestedElementConstrucorString));
            }
            else if(webElement.getTagName().equals("a")){
                if(webElement.getText() == null) continue;
                String suggestedElementName = webElement.getText().replace(" ", "").replace(".", "_").replace("%", "").replace("a", "") + "Link";
                String suggestedElementConstructor = "\"" + webElement.getText() + "\", DomElement.IdentificationType.BY_LINK_TEXT";
                constructors.add(new Constructor(suggestedElementName, suggestedElementConstructor));
            }
            else if(webElement.getText() != null && webElement.getText().length() > 0){
                if(driver.findElements(By.xpath("//*[contains(text(),'" + webElement.getText() + "')]")).size() == 1){
                    String suggestedElementName = webElement.getText().replace(" ", "").replace(".", "_").replace("%", "").replace("&", "") + webElement.getTagName().replace("a", "Link");
                    String suggestedElementConstructorString = "\"//*[contains(text(),'" + webElement.getText() + "')]\", DomElement.IdentificationType.BY_XPATH";
                    constructors.add(new Constructor(suggestedElementName, suggestedElementConstructorString));
                }
            }
        }
        return constructors.toString();
    }

    private class Constructors extends ArrayList<Constructor>{
        int elementCounter = 1;

        public @Override boolean add(Constructor constructor){
            if(hasUniqueName(constructor)){
                this.add(constructor);
            } else {
                constructor.elementName = constructor.elementName + elementCounter;
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
            return SupportMethods.LF + "public static DomElement " + elementName + "() {" + SupportMethods.LF + "    return new DomElement (" + constructorString + ");" + SupportMethods.LF;
        }
    }
}
