package se.claremont.autotest.javasupport.interaction.elementidentification;

import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;

import java.util.ArrayList;
import java.util.List;

public class By {

    public List<SearchCondition> searchConditions;

    private By(){
        searchConditions = new ArrayList<>();
    }

    public static By byClass(String className){
        By by = new By();
        by.searchConditions.add(new SearchCondition(SearchConditionType.CLASS, className));
        return by;
    }

    public static By byName(String elementName){
        By by = new By();
        by.searchConditions.add(new SearchCondition(SearchConditionType.NAME, elementName));
        return by;
    }

    public static By byExactText(String... alternativesForElementText){
        By by = new By();
        by.searchConditions.add(new SearchCondition(SearchConditionType.EXACT_TEXT, alternativesForElementText));
        return by;
    }

    public static By byTextContaining(String... alternativesForElementText){
        By by = new By();
        by.searchConditions.add(new SearchCondition(SearchConditionType.TEXT_CONTAINS, alternativesForElementText));
        return by;
    }

    public static By byTextRegexMatch(String... alternativesForElementTextRegexPatterns){
        By by = new By();
        by.searchConditions.add(new SearchCondition(SearchConditionType.TEXT_REGEX_MATCH, alternativesForElementTextRegexPatterns));
        return by;
    }

    public static By byBeingDescendantOf(JavaGuiElement ancestorElement){
        By by = new By();
        by.searchConditions.add(new SearchCondition(SearchConditionType.BEING_DESCENDANT_OF, ancestorElement));
        return by;
    }

    public By andByClass(String className){
        searchConditions.add(new SearchCondition(SearchConditionType.CLASS, className));
        return this;
    }

    public By andByName(String elementName){
        searchConditions.add(new SearchCondition(SearchConditionType.NAME, elementName));
        return this;
    }

    public By andByExactText(String... alterativesForElementText){
        searchConditions.add(new SearchCondition(SearchConditionType.EXACT_TEXT, alterativesForElementText));
        return this;
    }

    public By andByTextContains(String... alterativesForElementText){
        searchConditions.add(new SearchCondition(SearchConditionType.TEXT_CONTAINS, alterativesForElementText));
        return this;
    }

    public By andByTextRegexMatch(String... alterativesForElementTextRegexPatterns){
        searchConditions.add(new SearchCondition(SearchConditionType.TEXT_REGEX_MATCH, alterativesForElementTextRegexPatterns));
        return this;
    }

    public By andByBeingDescendantOf(JavaGuiElement ancestorElement){
        searchConditions.add(new SearchCondition(SearchConditionType.BEING_DESCENDANT_OF, ancestorElement));
        return this;
    }



}
