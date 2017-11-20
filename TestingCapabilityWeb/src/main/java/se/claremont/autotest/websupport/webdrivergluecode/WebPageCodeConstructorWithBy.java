package se.claremont.autotest.websupport.webdrivergluecode;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.support.SupportMethods;

import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("WeakerAccess")
public class WebPageCodeConstructorWithBy {

    /// <summary>
/// Class to generate DomElement descriptions based on the elements on a web page. 
/// Will take about 15 minutes to run, but should produce runnable code.
/// </summary>
    private final WebInteractionMethods web;
    private final List<WebElement> unidentifiedElements = new ArrayList<>();
    private final StringBuilder sb = new StringBuilder();
    private final Map<String, WebElement> identifiedElementsByName = new HashMap<>();

    /// <summary>
    /// Class to generate DomElement descriptions based on the elements on a web page. 
    /// Will take about 15 minutes to run, but should produce runnable code.
    /// </summary>
    /// <param name="web">The instance of WebInteractionSelenium to use for interaction with the page.</param>
    public WebPageCodeConstructorWithBy(WebInteractionMethods web)
    {
        this.web = web;
    }

    /// <summary>
    /// Attempts to map the current page to specified file
    /// </summary>
    /// <param name="outPutFilePath"></param>
    /// <returns>Returns inteded content of file.</returns>
    @SuppressWarnings("UnusedReturnValue")
    public String createPageObjectFromCurrentPage(String outPutFilePath, boolean quickAndSloppyMode)
    {
        long startTime = System.currentTimeMillis();
        String pageTitle;
        if (web.driver.getTitle() != null && web.driver.getTitle().length() > 0)
        {
            pageTitle = web.driver.getTitle();
        }
        else
        {
            pageTitle = web.driver.getCurrentUrl().substring(web.driver.getCurrentUrl().lastIndexOf("/"));
        }
        List<WebElement> elements = web.driver.findElements(org.openqa.selenium.By.xpath("//body//*"));
        for (WebElement element : elements)
        {
            try
            {
                if (element.getTagName().equals("script") || element.getTagName().equals("style")) continue;
                unidentifiedElements.add(element);
            }
            catch (Exception ignored) { } //Really short lived elements
        }
        //unidentifiedElements = elements.ToList<WebElement>();
        web.getTestCase().log(LogLevel.DEBUG, "Took " + (System.currentTimeMillis() - startTime) + " milliseconds to gather all elements in HTML Body.");
        startTime = System.currentTimeMillis();
        sb.append("import se.claremont.autotest.websupport.DomElement;").append(System.lineSeparator());
                sb.append("import se.claremont.autotest.websupport.elementidentification.By;").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("//Automatically started generaton with TAF ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(
                new Date())).append(System.lineSeparator());
        sb.append("public class ").append(upperCaseInitialLetterOfEachWord(programmaticallySafeName(pageTitle + "_Page"))).append(" {").append(System.lineSeparator());
        addElementsIdentifiableWithIds();
        web.getTestCase().log(LogLevel.DEBUG, "Identifying elements by id took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
        startTime = System.currentTimeMillis();
        addElementsIdentifiableWithNames();
        web.getTestCase().log(LogLevel.DEBUG, "Identifying elements by names took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
        startTime = System.currentTimeMillis();
        addElementsIdentifiableWithText();
        web.getTestCase().log(LogLevel.DEBUG, "Identifying elements by text took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
        startTime = System.currentTimeMillis();
        addElementsIdentifiableWithClasses();
        web.getTestCase().log(LogLevel.DEBUG, "Identifying elements by classes took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
        startTime = System.currentTimeMillis();
        addElementsIdentifiableWithTagNames();
        web.getTestCase().log(LogLevel.DEBUG, "Identifying elements by tag names took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
        startTime = System.currentTimeMillis();
        addElementsIdentifiableWithAttributes();
        web.getTestCase().log(LogLevel.DEBUG, "Identifying elements by attributes took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
        startTime = System.currentTimeMillis();
        if (!quickAndSloppyMode)
        {
            addUniqueElementsFromParent();
            web.getTestCase().log(LogLevel.DEBUG, "Identifying elements by parents took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
            startTime = System.currentTimeMillis();
        }
        //AddRemainingElementsByXPath();
        sb.append("}").append(System.lineSeparator());
        if (unidentifiedElements.size() > 0)
        {
            sb.append("/*").append(System.lineSeparator());
            for (WebElement element : unidentifiedElements)
            {
                try
                {
                    sb.append("   Element not identified: [tag: '").append(element.getTagName()).append("', outerHtml: '").append(element.getAttribute("outerHTML")).append("']").append(System.lineSeparator()).append(System.lineSeparator());
                }
                catch (Exception e)
                {
                    System.out.println("Problems describing unidentified element. Error: " + e.getMessage());
                }
            }
            sb.append("*/").append(System.lineSeparator()).append(System.lineSeparator());
            sb.append("//Stopped page class generaton at ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(
                    new Date())).append(System.lineSeparator());
            web.getTestCase().log(LogLevel.DEBUG, "Describing unidentified elements by id took " + (System.currentTimeMillis() - startTime) + " milliseconds.");

        }
        web.getTestCase().log(LogLevel.DEBUG, "Content of file created:" + System.lineSeparator() + sb.toString());
        if (outPutFilePath != null) SupportMethods.saveToFile(sb.toString(), outPutFilePath);
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private String identifyName(WebElement element, int recursionLevel, String parentTagName)
    {
        String name = element.getAttribute("name");
        if (name != null && name.length() > 0) return programmaticallySafeName
                (name) + "_" + tagName(element.getTagName());

        name = element.getAttribute("title");
        if (name != null && name.length() > 0) return programmaticallySafeName(name) + "_" + tagName(element.getTagName());

        name = element.getAttribute("id");
        if (name != null && name.length() > 0) return programmaticallySafeName(name) + "_" + tagName(element.getTagName());

        name = element.getText();
        if (name != null && name.length() > 0)
        {
            if (name.length() > 40) return programmaticallySafeName(name.substring(0, 40)) + "_" + tagName(element.getTagName());
            return programmaticallySafeName(name) + "_" + tagName(element.getTagName());
        }

        name = element.getAttribute("alt");
        if (name != null && name.length() > 0) return programmaticallySafeName(name) + "_" + tagName(element.getTagName());

        name = element.getAttribute("value");
        if (name != null && name.length() > 0) return programmaticallySafeName(name) + "_" + tagName(element.getTagName());

        name = element.getAttribute("class");
        if (name != null && name.length() > 0) return programmaticallySafeName(name) + "_" + tagName(element.getTagName());

        JavascriptExecutor javascript = (JavascriptExecutor) web.driver;
        Map<String, Object> attributes = (Map<String, Object>)javascript.executeScript("var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;", element);
        if (attributes.size() > 0)
        {
            for (String attributeName : attributes.keySet())
            {
                name += attributeName + " " + attributes.get(attributeName);
            }
            name = upperCaseInitialLetterOfEachWord(name);
        }
        if (name != null && name.length() > 0) return programmaticallySafeName(name) + "_" + tagName(element.getTagName());

        if (parentTagName == null) parentTagName = element.getTagName();
        if(recursionLevel < 2)
        {
            for(WebElement child : element.findElements(org.openqa.selenium.By.xpath(".//"))){
            name = identifyName(child, recursionLevel + 1, parentTagName);
                //noinspection ConstantConditions
                if (name != null && name.length() > 0 && !name.startsWith("NoName_")) return name + "_" + tagName(parentTagName);
        }
        }
        return "NoName_" + tagName(element.getTagName());
    }

    private String getUnusedName(String baseName)
    {
        String elementName = baseName;
        if (identifiedElementsByName.containsKey(elementName))
        {
            int counter = 1;
            while (identifiedElementsByName.containsKey(elementName + String.valueOf(counter)))
            {
                counter++;
            }
            elementName = elementName + String.valueOf(counter);
        }
        return elementName;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private void addUniqueElementsFromParent()
    {
        List<WebElement> elementsToRemove = new ArrayList<>();
        for (String parentName : identifiedElementsByName.keySet())
        {
            try
            {
                WebElement parent = identifiedElementsByName.get(parentName);
                if (parent == null) continue;
                for (int i = 0; i < unidentifiedElements.size(); i++)
                {
                    WebElement descendant = unidentifiedElements.get(i);
                    if (descendant == null)
                    {
                        continue;
                    }
                    String id = descendant.getAttribute("id");
                    if (id == null || id.length() == 0) continue;
                    if (parent.findElements(org.openqa.selenium.By.id(id)).size() == 1)
                    {
                        String elementName = getUnusedName(parentName + "_" + identifyName(descendant, 0, null));
                        identifiedElementsByName.put(elementName, descendant);
                        sb.append("      public static DomElement ").append(elementName).append(" = new DomElement(By").append(System.lineSeparator());
                        sb.append("         .id(\"").append(id.replace("\"", "'")).append("\")").append(System.lineSeparator());
                        sb.append("         .andByBeingDescendantOf(").append(parentName).append(")").append(System.lineSeparator());
                        sb.append("         .andByTagName(\"").append(descendant.getTagName()).append("\"),").append(System.lineSeparator());
                        sb.append("         \"").append(elementName).append("\");").append(System.lineSeparator());
                        sb.append(System.lineSeparator());
                        elementsToRemove.add(descendant);
                        continue;
                    }

                    String name = descendant.getAttribute("name");
                    if (parent.findElements(org.openqa.selenium.By.name(name)).size() == 1)
                    {
                        String elementNam = getUnusedName(parentName + "_" + identifyName(descendant, 0, null));
                        identifiedElementsByName.put(elementNam, descendant);
                        sb.append("      public static DomElement ").append(elementNam).append(" = new DomElement(By").append(System.lineSeparator()).append(System.lineSeparator());
                        sb.append("         .name(\"").append(name.replace("\"", "'")).append("\")").append(System.lineSeparator()).append(System.lineSeparator());
                        sb.append("         .andByBeingDescendantOf(").append(parentName).append(")").append(System.lineSeparator()).append(System.lineSeparator());
                        sb.append("         .andByTagName(\"").append(descendant.getTagName()).append("\"),").append(System.lineSeparator()).append(System.lineSeparator());
                        sb.append("         \"").append(elementNam).append("\");").append(System.lineSeparator());
                        sb.append(System.lineSeparator());
                        elementsToRemove.add(descendant);
                        continue;
                    }

                    String klass = descendant.getAttribute("class");
                    if (parent.findElements(org.openqa.selenium.By.className(klass)).size() == 1)
                    {
                        String elementName2 = getUnusedName(parentName + "_" + identifyName(descendant, 0, null));
                        identifiedElementsByName.put(elementName2, descendant);
                        sb.append("      public static DomElement ").append(elementName2).append(" = new DomElement(By").append(System.lineSeparator()).append(System.lineSeparator());
                        sb.append("         .className(\"").append(klass.replace("\"", "'")).append("\")").append(System.lineSeparator()).append(System.lineSeparator());
                        sb.append("         .andByBeingDescendantOf(").append(parentName).append(")").append(System.lineSeparator()).append(System.lineSeparator());
                        sb.append("         .andByTagName(\"").append(descendant.getTagName()).append("\"),").append(System.lineSeparator()).append(System.lineSeparator());
                        sb.append("         \"").append(elementName2).append("\");").append(System.lineSeparator());
                        sb.append(System.lineSeparator());
                        elementsToRemove.add(descendant);
                        //noinspection UnnecessaryContinue
                        continue;
                    }
                }
            }
            catch (Exception e)
            {
                System.out.println("Problems with parent or descendant. Error: " + e.toString());
            }
        }

        //elementsToRemove.Reverse();
        for (WebElement element : elementsToRemove)
        {
            try
            {
                unidentifiedElements.remove(element);
            }
            catch (Exception e)
            {
                System.out.println("Could not remove element from unidentifiedList. Error: " + e.toString());
            }
        }
    }

    private void addElementsIdentifiableWithNames()
    {
        List<Integer> elementsToRemove = new ArrayList<>();
        for (int i = 0; i < unidentifiedElements.size(); i++)
        {
            try
            {
                WebElement element = unidentifiedElements.get(i);
                String name = element.getAttribute("name");
                if (name != null
                        && name.length() > 0
                        && web.driver.findElements(org.openqa.selenium.By.xpath("//" + element.getTagName() + "[@name='" + name.replace("'", "\"") + "']")).size() == 1)
                {
                    String elementName = getUnusedName(identifyName(element, 0, null));
                    identifiedElementsByName.put(elementName, element);
                    sb.append("      public static DomElement ").append(elementName).append(" = new DomElement(By").append(System.lineSeparator());
                    sb.append("         .name(\"").append(name.replace("\"", "'")).append("\")").append(System.lineSeparator());
                    sb.append("         .andByTagName(\"").append(element.getTagName()).append("\"),").append(System.lineSeparator());
                    sb.append("         \"").append(elementName).append("\");").append(System.lineSeparator());
                    sb.append(System.lineSeparator());
                    elementsToRemove.add(i);
                }
            }
            catch (Exception e)
            {
                System.out.println("Problem with element in names. Error: " + e.toString());
                elementsToRemove.add(i);
            }
        }
        Collections.reverse(elementsToRemove);
        for (int elementNr : elementsToRemove)
        {
            try
            {
                unidentifiedElements.remove(elementNr);
            }
            catch (Exception e)
            {
                System.out.println("Could not remove element from unidentifiedList. Error: " + e.toString());
            }
        }
    }

    private void addElementsIdentifiableWithTagNames()
    {
        List<Integer> elementsToRemove = new ArrayList<>();
        for (int i = 0; i < unidentifiedElements.size(); i++)
        {
            try
            {
                WebElement element = unidentifiedElements.get(i);
                String tag = element.getTagName();
                if (tag != null
                        && tag.length() > 0
                        && web.driver.findElements(org.openqa.selenium.By.xpath("//" + element.getTagName())).size() == 1)
                {
                    String elementName = getUnusedName(identifyName(element, 0, null));
                    identifiedElementsByName.put(elementName, element);
                    sb.append("      public static DomElement ").append(elementName).append(" = new DomElement(By").append(System.lineSeparator());
                    sb.append("         .tagName(\"").append(tag).append("\"),").append(System.lineSeparator());
                    sb.append("         \"").append(elementName).append("\");").append(System.lineSeparator());
                    sb.append(System.lineSeparator());
                    elementsToRemove.add(i);
                }
            }
            catch (Exception e)
            {
                System.out.println("Problems with element getter for tag name. Error: " + e.toString());
                elementsToRemove.add(i);
            }
        }
        Collections.reverse(elementsToRemove);
        for (int elementNr : elementsToRemove)
        {
            try
            {
                unidentifiedElements.remove(elementNr);
            }
            catch (Exception e)
            {
                System.out.println("Could not remove element from unidentifiedList. Error: " + e.toString());
            }
        }
    }

    private void addElementsIdentifiableWithClasses()
    {
        List<Integer> elementsToRemove = new ArrayList<>();
        for (int i = 0; i < unidentifiedElements.size(); i++)
        {
            try
            {
                WebElement element = unidentifiedElements.get(i);
                String klass = element.getAttribute("class");
                if (klass != null
                        && klass.length() > 0
                        && web.driver.findElements(org.openqa.selenium.By.xpath("//" + element.getTagName() + "[@class='" + klass.replace("'", "\"") + "']")).size() == 1)
                {
                    String elementName = getUnusedName(identifyName(element, 0, null));
                    identifiedElementsByName.put(elementName, element);
                    sb.append("      public static DomElement ").append(elementName).append(" = new DomElement(By").append(System.lineSeparator());
                    sb.append("         .className(\"").append(klass.replace("\"", "'")).append("\")").append(System.lineSeparator());
                    sb.append("         .andByTagName(\"").append(element.getTagName()).append("\"),").append(System.lineSeparator());
                    sb.append("         \"").append(elementName).append("\");").append(System.lineSeparator());
                    sb.append(System.lineSeparator());
                    elementsToRemove.add(i);
                }
            }
            catch (Exception e)
            {
                System.out.println("Problems with element getters in classes. Error: " + e.toString());
                elementsToRemove.add(i);
            }
        }
        Collections.reverse(elementsToRemove);
        for (int elementNr : elementsToRemove)
        {
            try
            {
                unidentifiedElements.remove(elementNr);
            }
            catch (Exception e)
            {
                System.out.println("Could not remove element from unidentifiedList. Error: " + e.toString());
            }
        }
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private void addElementsIdentifiableWithAttributes()
    {
        List<Integer> elementsToRemove = new ArrayList<>();
        JavascriptExecutor javascript = (JavascriptExecutor)web.driver;
        for (int i = 0; i < unidentifiedElements.size(); i++)
        {
            try
            {
                WebElement element = unidentifiedElements.get(i);
                Map<String, Object> attributes = (Map<String, Object>)javascript.executeScript("var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;", element);
                String xpath = "//" + element.getTagName();
                List<String> attributeStrings = new ArrayList<>();
                if (attributes.size() > 0)
                {
                    List<String> condition = new ArrayList<>();
                    for (String attributeName : attributes.keySet())
                    {
                        String attributeValue = attributes.get(attributeName).toString();
                        condition.add("@" + attributeName + "='" + attributeValue.replace("'", "\"") + "'");
                        attributeStrings.add("\"" + attributeName + "\", \"" + attributes.get(attributeName).toString().replace("\"", "'") + "\"");
                    }
                    xpath += "[" + String.join(" and ", condition) + "]";
                }
                if (xpath != null
                        && xpath.length() > 0
                        && web.driver.findElements(org.openqa.selenium.By.xpath(xpath)).size() == 1)
                {
                    String elementName = getUnusedName(identifyName(element, 0, null));
                    identifiedElementsByName.put(elementName, element);
                    sb.append("      public static DomElement ").append(elementName).append(" = new DomElement(By").append(System.lineSeparator()).append("         .tagName(\"").append(element.getTagName()).append("\")").append(System.lineSeparator()).append("         .andByAttributeValue(").append(String.join(")" + System.lineSeparator()
                            + "         .andByAttributeValue(", attributeStrings)).append("),").append(System.lineSeparator()).append("         \"").append(elementName).append("\");").append(System.lineSeparator());
                    sb.append(System.lineSeparator());
                    elementsToRemove.add(i);
                }
            }
            catch (Exception e)
            {
                System.out.println("Problem getting element for attributes. Error: " + e.toString());
                elementsToRemove.add(i);
            }
        }
        Collections.reverse(elementsToRemove);
        for (int elementNr : elementsToRemove)
        {
            try
            {
                unidentifiedElements.remove(elementNr);
            }
            catch (Exception e)
            {
                System.out.println("Could not remove element from unidentifiedList. Error: " + e.toString());
            }
        }
    }

    private static String tagName(String tagName)
    {
        if (tagName.toLowerCase().equals("a")) return "Link";
        return upperCaseInitialLetterOfEachWord(tagName);
    }

    private void addElementsIdentifiableWithIds()
    {
        List<Integer> elementsToRemove = new ArrayList<>();
        for (int i = 0; i < unidentifiedElements.size(); i++)
        {
            try
            {
                WebElement element = unidentifiedElements.get(i);
                String id = element.getAttribute("id");
                if (id != null
                        && id.length() > 0
                        && web.driver.findElements(org.openqa.selenium.By.xpath("//" + element.getTagName() + "[@id='" + id.replace("'", "\"") + "']")).size() == 1)
                {
                    String elementName = getUnusedName(identifyName(element, 0, null));
                    identifiedElementsByName.put(elementName, element);
                    sb.append("      public static DomElement ").append(elementName).append(" = new DomElement(By").append(System.lineSeparator());
                    sb.append("         .id(\"").append(id.replace("\"", "'")).append("\")").append(System.lineSeparator());
                    sb.append("         .andByTagName(\"").append(element.getTagName()).append("\"),").append(System.lineSeparator());
                    sb.append("         \"").append(elementName).append("\");").append(System.lineSeparator());
                    sb.append(System.lineSeparator());
                    elementsToRemove.add(i);
                }
            }
            catch (Exception e)
            {
                System.out.println("Problem with element using ids. Error: " + e.toString());
                elementsToRemove.add(i);
            }
        }
        Collections.reverse(elementsToRemove);
        for (int elementNr : elementsToRemove)
        {
            try
            {
                unidentifiedElements.remove(elementNr);
            }
            catch (Exception e)
            {
                System.out.println("Could not remove element from unidentifiedList. Error: " + e.toString());
            }
        }
    }

    private void addElementsIdentifiableWithText()
    {
        List<Integer> elementsToRemove = new ArrayList<>();
        for (int i = 0; i < unidentifiedElements.size(); i++)
        {
            try
            {
                WebElement element = unidentifiedElements.get(i);
                String text = element.getText();
                if (text != null
                        && text.length() > 0
                        && web.driver.findElements(org.openqa.selenium.By.xpath("//*[text()='" + text.replace("'", "\"") + "']")).size() == 1)
                {
                    List<String> textLines = lineBreakedText(text);
                    String exactTextString;
                    if (textLines.size() == 1)
                    {
                        exactTextString = "         .exactText(\"" + textLines.get(0) + "\")" + System.lineSeparator();
                    }
                    else
                    {
                        exactTextString = "         .exactText(" + System.lineSeparator()
                                + "              \"" + String.join("\"" + System.lineSeparator()
                                + "            + \"", textLines) + "\"" + System.lineSeparator()
                                + "            )" + System.lineSeparator();
                    }
                    String elementName = getUnusedName(identifyName(element, 0, null));
                    identifiedElementsByName.put(elementName, element);
                    sb.append("      public static DomElement ").append(elementName).append(" = new DomElement(By").append(System.lineSeparator()).append(exactTextString).append("         .andByTagName(\"").append(element.getTagName()).append("\"),").append(System.lineSeparator()).append("         \"").append(elementName).append("\"); ").append(System.lineSeparator());
                    sb.append(System.lineSeparator());
                    elementsToRemove.add(i);
                }
            }
            catch (Exception e)
            {
                System.out.println("Problems with element in text getting. Error: " + e.toString());
                elementsToRemove.add(i);
            }
        }
        Collections.reverse(elementsToRemove);
        for (int elementNr : elementsToRemove)
        {
            try
            {
                unidentifiedElements.remove(elementNr);
            }
            catch (Exception e)
            {
                System.out.println("Could not remove element from unidentifiedList. Error: " + e.toString());
            }
        }
    }

    private List<String> lineBreakedText(String text)
    {
        text = text.replace("\"", "'");
        List<String> parts = new ArrayList<>();
        while (text.length() > 90)
        {
            parts.add(text.substring(0, 90));
            text = text.substring(90);
        }
        parts.add(text);
        return parts;
    }

    private static String upperCaseInitialLetterOfEachWord(String inString)
    {
        StringBuilder sb = new StringBuilder();
        String[] words = inString.split(" ");
        for (String word : words)
        {
            if (word == null || word.trim().length() == 0) continue;
            if (word.length() > 1)
            {
                sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1));
            }
            else
            {
                sb.append(word.toUpperCase());
            }
        }
        return sb.toString();
    }

    public static String programmaticallySafeName(String inString)
    {
        if (inString == null) inString = "";
        if (inString.matches("^\\d")) inString = "_" + inString;
        inString = upperCaseInitialLetterOfEachWord(inString);
        if(inString.length() > 0) inString = inString.substring(0,1).toLowerCase() + inString.substring(1);
        //inString = Regex.replace(inString, "[^0-9a-zA-Z]+", "_");
        inString = inString
                .replace(" ", "")
                .replace("\\", "")
                .replace("/", "")
                .replace("©", "Copyright")
                .replace(":", "_")
                .replace("@", "At")
                .replace(";", "_")
                .replace("&", "And")
                .replace("#", "_")
                .replace("%", "Proc")
                .replace("^", "_")
                .replace("|", "_")
                .replace("-", "_")
                .replace("¨", "")
                .replace("~", "_")
                .replace(System.lineSeparator(), "")
                .replace("\n", "")
                .replace("\t", "")
                .replace("\"", "")
                .replace("'", "")
                .replace("<", "")
                .replace("-", "_")
                .replace("+", "Plus")
                .replace(".", "_")
                .replace(",", "_")
                .replace("*", "")
                .replace("$", "Dollar")
                .replace("£", "Pound")
                .replace("€", "Euro")
                .replace("}", "")
                .replace("[", "_")
                .replace("]", "_")
                .replace("?", "_")
                .replace("!", "_")
                .replace("{", "")
                .replace("-", "_")
                .replace("(", "")
                .replace(")", "")
                .replace(">", "")
                .replace("=", "");

        while (inString.contains("__"))
        {
            inString = inString.replace("__", "_");
        }
        if (inString.length() > 50) inString = inString.substring(0, 50);
        if (inString.length() == 0) inString = "NoName";
        return inString;
    }
}

