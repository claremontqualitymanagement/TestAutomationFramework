package se.claremont.autotest.websupport.elementidentification;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/// <summary>
/// The TAF By class extends the Selenium one by chaining different 
/// Selenium alternatives together.<para />
/// Partial texts are also introduced for all element types, and matching 
/// of texts by regular expressions also is introduced, as well as using 
/// a parent element to limit searches to descendants.<para />
/// Be aware that all criterias can be combined except the CSS one, or the 
/// XPath one. If any of the latter is chosen this will always be run as is, 
/// without parsing the rest of the criterias. This is because it is not 
/// possible to combine xpath and CSS as search attributes, and the other 
/// criteria is used to build an xpath expression when trying to identify 
/// an element.
/// </summary>
@SuppressWarnings("unused")
public class By {
    final List<SearchCondition> conditions;

    By() {
        conditions = new ArrayList<>();
    }

    public int getConditionCount(){
        return conditions.size();
    }

    /// <summary>
    /// Filters out elements that do not have this class stated. Class names do not need to be in the same order as on page.
    /// </summary>
    /// <param name="className">Class name(s) of element to search for</param>
    /// <returns></returns>
    public static By classContains(String className) {
        By s = new By();
        s.conditions.add(new SearchCondition(SearchConditionType.AttributeValue, "classcontains", className));
        return s;
    }

    /// <summary>
    /// Filters out all element not having the exact class string as entered below.
    /// </summary>
    /// <param name="className">Class name(s) of element to search for</param>
    /// <returns></returns>
    public static By className(String className) {
        By s = new By();
        s.conditions.add(new SearchCondition(SearchConditionType.AttributeValue, "class", className));
        return s;
    }

    /// <summary>
    /// Identifying elements by xPath expression. If several elements matches the xPath 
    /// the first will be retrieved, but this will be logged to the test case log.
    /// </summary>
    /// <param name="xPath">XPath expression</param>
    /// <returns></returns>
    public static By xpath(String xPath) {
        By s = new By();
        s.conditions.add(new SearchCondition(SearchConditionType.XPath, xPath));
        return s;
    }

    /// <summary>
    /// Sometimes you want to limit your search to the descendants of a parent element. If such an element is given here, it will be the top node for the search.
    /// </summary>
    /// <param name="webElement">The root node for the search.</param>
    /// <returns></returns>
    public static By beingDescendantOf(WebElement webElement) {
        By s = new By();
        s.conditions.add(new SearchCondition(SearchConditionType.ByAncestor, webElement));
        return s;
    }

    /// <summary>
    /// Looking for elements with this exact text as InnerHTML
    /// </summary>
    /// <param name="text">Exact text</param>
    /// <returns></returns>
    public static By exactText(String text) {
        By s = new By();
        s.conditions.add(new SearchCondition(SearchConditionType.ExactText, text));
        return s;
    }

    /// <summary>
    /// For language sensitive automations sometimes different texts represent the 
    /// element in different contexts. With this method you can state as many 
    /// alternative texts as preffered.
    /// </summary>
    /// <param name="textStrings">Any number of strings</param>
    /// <returns></returns>
    public static By exactTextMatchOfAnyOfTheStrings(String... textStrings) {
        By s = new By();
        s.conditions.add(new SearchCondition(SearchConditionType.ExactText, (Object[]) textStrings));
        return s;
    }

    /// <summary>
    /// For language sensitive automations sometimes different texts represent the 
    /// element in different contexts. With this method you can state as many 
    /// alternative texts as preffered.
    /// </summary>
    /// <param name="textStrings">Any number of strings</param>
    /// <returns></returns>
    public static By textContainsAnyOfTheStrings(String... textStrings) {
        By s = new By();
        s.conditions.add(new SearchCondition(SearchConditionType.TextContains, (Object[]) textStrings));
        return s;
    }

    /// <summary>
    /// Identifies an element given its id attribute.
    /// </summary>
    /// <param name="id">The id of the element to identify</param>
    /// <returns></returns>
    public static By id(String id) {
        By s = new By();
        s.conditions.add(new SearchCondition(SearchConditionType.AttributeValue, "id", id));
        return s;
    }

    /// <summary>
    /// Identifies an element given its name attribute.
    /// </summary>
    /// <param name="name">The name of the element to identify</param>
    /// <returns></returns>
    public static By name(String name) {
        By s = new By();
        s.conditions.add(new SearchCondition(SearchConditionType.AttributeValue, name));
        return s;
    }

    /// <summary>
    /// Limits the search scope to a certain type of element, based on the html tag names.
    /// </summary>
    /// <param name="tagName">HTML tag name</param>
    /// <returns></returns>
    public static By tagName(String tagName) {
        By s = new By();
        s.conditions.add(new SearchCondition(SearchConditionType.ControlType, tagName));
        return s;
    }

    /// <summary>
    /// If several elements are identified with the conditions applied, this could be used 
    /// to state wich one you want to interact with.
    /// </summary>
    /// <param name="ordinalNumber">Ordinal number, given the DOM tree parsing algorithm used.</param>
    /// <returns></returns>
    public static By ordinalNumber(int ordinalNumber) {
        By s = new By();
        s.conditions.add(new SearchCondition(SearchConditionType.OrdinalNumber, ordinalNumber));
        return s;
    }

    /// <summary>
    /// Identifies elements given the text matching the regular expression pattern.
    /// </summary>
    /// <param name="regularExpressionPattern">Regular expression pattern</param>
    /// <returns></returns>
    public static By textIsRegexMatch(String regularExpressionPattern) {
        By s = new By();
        s.conditions.add(new SearchCondition(SearchConditionType.TextRegex, regularExpressionPattern));
        return s;
    }

    /// <summary>
    /// Identifies elements containing the specified String as InnerHTML.
    /// </summary>
    /// <param name="text">Text to find within the element.</param>
    /// <returns></returns>
    public static By textContainsString(String text) {
        By s = new By();
        s.conditions.add(new SearchCondition(SearchConditionType.TextContains, text));
        return s;
    }


    /// <summary>
    /// Identifies elements based on their properties, e.g. <para />
    /// 
    /// a html element described by "input class='myclass' type='checkbox' value='first name'" 
    /// has property name 'type' for property value 'checkbox'.
    /// </summary>
    /// <param name="attributeName"></param>
    /// <param name="attributeValue"></param>
    /// <returns></returns>
    public static By attributeValue(String attributeName, String attributeValue) {
        By s = new By();
        s.conditions.add(new SearchCondition(SearchConditionType.AttributeValue, attributeName, attributeValue));
        return s;
    }

    /// <summary>
    /// Identifies element with the same syntax as CSS locators in the HTML style section
    /// </summary>
    /// <param name="cssLocator"></param>
    /// <returns></returns>
    public static By cssSelector(String cssLocator) {
        By s = new By();
        s.conditions.add(new SearchCondition(SearchConditionType.CssLocator, cssLocator));
        return s;
    }

    /// <summary>
    /// Refines selection of element by adding class name or class names. Class names 
    /// filter treates class names regardless of their order.
    /// </summary>
    /// <param name="className">The class name or class names to match with</param>
    /// <returns></returns>
    public By andByClassContains(String className) {
        conditions.add(new SearchCondition(SearchConditionType.AttributeValue, "classcontains", className));
        return this;
    }

    /// <summary>
    /// Refines element identification by exact class string match.
    /// </summary>
    /// <param name="className">The class string to match</param>
    /// <returns></returns>
    public By andByClass(String className) {
        conditions.add(new SearchCondition(SearchConditionType.AttributeValue, "class", className));
        return this;
    }

    /// <summary>
    /// Selecting an element based on its ordinal value from within a search scope. First element is element 1. <para />
    /// Ordinal numbers are always run last, when other filter mechanisms have already been applied.
    /// </summary>
    /// <param name="ordinalNumber">Element number. First element is element 1.</param>
    /// <returns></returns>
    public By andByOrdinalNumber(int ordinalNumber) {
        conditions.add(new SearchCondition(SearchConditionType.OrdinalNumber, ordinalNumber));
        return this;
    }

    /// <summary>
    /// Limiting search scope to elements with a certain HTML tag.
    /// </summary>
    /// <param name="tagName">Any of the official HTML tags, or your own ones.</param>
    /// <returns></returns>
    public By andByTagName(String tagName) {
        conditions.add(new SearchCondition(SearchConditionType.ControlType, tagName));
        return this;
    }

    /// <summary>
    /// Limiting search scope to elements with a specific text, based on exact matching.
    /// </summary>
    /// <param name="exactText"></param>
    /// <returns></returns>
    public By andByExactText(String exactText) {
        conditions.add(new SearchCondition(SearchConditionType.ExactText, exactText));
        return this;
    }

    /// <summary>
    /// Limiting search scope to elements with a secific text, based on regular expression pattern.
    /// </summary>
    /// <param name="regexPattern"></param>
    /// <returns></returns>
    public By andByTextIsRegexMatch(String regexPattern) {
        conditions.add(new SearchCondition(SearchConditionType.TextRegex, regexPattern));
        return this;
    }

    /// <summary>
    /// Limiting search scope to elements containing a specific text.
    /// </summary>
    /// <param name="textContained"></param>
    /// <returns></returns>
    public By andByTextContainsString(String textContained) {
        conditions.add(new SearchCondition(SearchConditionType.TextContains, textContained));
        return this;
    }

    /// <summary>
    /// Limiting search scope to elements with a specific name as an attribute.
    /// </summary>
    /// <param name="name"></param>
    /// <returns></returns>
    public By andByElementName(String name) {
        conditions.add(new SearchCondition(SearchConditionType.AttributeValue, "name", name));
        return this;
    }

    /// <summary>
    /// Limiting search scope to elements with a specific id as an attribute.
    /// </summary>
    /// <param name="id"></param>
    /// <returns></returns>
    public By andById(String id) {
        conditions.add(new SearchCondition(SearchConditionType.AttributeValue, "id", id));
        return this;
    }

    /// <summary>
    /// Limiting search scope for specific attributes.
    /// </summary>
    /// <param name="attributeName"></param>
    /// <param name="attributeValue"></param>
    /// <returns></returns>
    public By andByAttributeValue(String attributeName, String attributeValue) {
        conditions.add(new SearchCondition(SearchConditionType.AttributeValue, attributeName, attributeValue));
        return this;
    }

    /// <summary>
    /// Limits search scope to the descendantso of a specific element, known as the ancestor.
    /// </summary>
    /// <param name="webElement"></param>
    /// <returns></returns>
    public By andByBeingDescendantOf(WebElement ancestor) {
        conditions.add(new SearchCondition(SearchConditionType.ByAncestor, ancestor));
        return this;
    }

    /// <summary>
    /// For language sensitive automations sometimes different texts represent the 
    /// element in different contexts. With this method you can state as many 
    /// alternative texts as preffered.
    /// </summary>
    /// <param name="textStrings">Any number of strings</param>
    /// <returns></returns>
    public By andByTextBeingExactlyAnyOfTheStrings(String... textStrings) {
        conditions.add(new SearchCondition(SearchConditionType.ExactText, (Object[]) textStrings));
        return this;
    }

    /// <summary>
    /// For language sensitive automations sometimes different texts represent the 
    /// element in different contexts. With this method you can state as many 
    /// alternative texts as preffered.
    /// </summary>
    /// <param name="textStrings">Any number of strings</param>
    /// <returns></returns>
    public By andByTextContainingAnyOfTheStrings(String... textStrings) {
        conditions.add(new SearchCondition(SearchConditionType.TextContains, (Object[]) textStrings));
        return this;
    }

    /// <summary>
    /// Finding the element based on specific explicit xPath. If several elements 
    /// are matched, an ordinal number can be applied.
    /// </summary>
    /// <param name="xPath"></param>
    /// <returns></returns>
    public By andByXPath(String xPath) {
        conditions.add(new SearchCondition(SearchConditionType.XPath, xPath));
        return this;
    }

    @Override
    public String toString() {
        String returnString = "[By:" + System.lineSeparator();
        for (SearchCondition sc : conditions) {
            returnString += "   " + sc.toString() + System.lineSeparator();
        }
        return returnString + "]";
    }

}
