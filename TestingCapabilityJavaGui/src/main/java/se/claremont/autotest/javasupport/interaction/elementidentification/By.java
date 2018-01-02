package se.claremont.autotest.javasupport.interaction.elementidentification;

import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.ElementsList;
import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement;
import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedIdentificator;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.javasupport.interaction.GenericInteractionMethods;
import se.claremont.autotest.javasupport.interaction.MethodDeclarations;
import se.claremont.autotest.javasupport.interaction.MethodInvoker;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class By {

    public List<SearchCondition> searchConditions;

    public By() {
        searchConditions = new ArrayList<>();
    }

    public static By byClass(String className) {
        By by = new By();
        by.searchConditions.add(new SearchCondition(SearchConditionType.CLASS, className));
        return by;
    }

    public static By byOrdinalNumber(int ordinalNumber) {
        By by = new By();
        by.searchConditions.add(new SearchCondition(SearchConditionType.ORDINAL_NUMBER, ordinalNumber));
        return by;
    }

    public static By byRelativePosition(ElementsList staticPositionBasedIdentificatorReferenceToElementList){
        By by = new By();
        by.searchConditions.add(new SearchCondition(SearchConditionType.POSITION_BASED, staticPositionBasedIdentificatorReferenceToElementList));
        return by;
    }

    public static By byName(String elementName) {
        By by = new By();
        by.searchConditions.add(new SearchCondition(SearchConditionType.NAME, elementName));
        return by;
    }

    public static By byExactText(String... alternativesForElementText) {
        By by = new By();
        by.searchConditions.add(new SearchCondition(SearchConditionType.EXACT_TEXT, alternativesForElementText));
        return by;
    }

    public static By byTextContaining(String... alternativesForElementText) {
        By by = new By();
        by.searchConditions.add(new SearchCondition(SearchConditionType.TEXT_CONTAINS, alternativesForElementText));
        return by;
    }

    public static By byTextRegexMatch(String... alternativesForElementTextRegexPatterns) {
        By by = new By();
        by.searchConditions.add(new SearchCondition(SearchConditionType.TEXT_REGEX_MATCH, alternativesForElementTextRegexPatterns));
        return by;
    }

    public static By byBeingDescendantOf(JavaGuiElement ancestorElement) {
        By by = new By();
        by.searchConditions.add(new SearchCondition(SearchConditionType.BEING_DESCENDANT_OF, ancestorElement));
        return by;
    }

    public By andByClass(String className) {
        searchConditions.add(new SearchCondition(SearchConditionType.CLASS, className));
        return this;
    }

    public By andByName(String elementName) {
        searchConditions.add(new SearchCondition(SearchConditionType.NAME, elementName));
        return this;
    }

    public By andByExactText(String... alterativesForElementText) {
        searchConditions.add(new SearchCondition(SearchConditionType.EXACT_TEXT, alterativesForElementText));
        return this;
    }

    public By andByTextContains(String... alterativesForElementText) {
        searchConditions.add(new SearchCondition(SearchConditionType.TEXT_CONTAINS, alterativesForElementText));
        return this;
    }

    public By andByTextRegexMatch(String... alterativesForElementTextRegexPatterns) {
        searchConditions.add(new SearchCondition(SearchConditionType.TEXT_REGEX_MATCH, alterativesForElementTextRegexPatterns));
        return this;
    }

    public By andByBeingDescendantOf(JavaGuiElement ancestorElement) {
        searchConditions.add(new SearchCondition(SearchConditionType.BEING_DESCENDANT_OF, ancestorElement));
        return this;
    }

    public By andByOrdinalNumber(int ordinalNumber) {
        searchConditions.add(new SearchCondition(SearchConditionType.ORDINAL_NUMBER, ordinalNumber));
        return this;
    }

    public String asCode() {
        if (searchConditions == null) return null;
        StringBuilder sb = new StringBuilder();
        sb.append("By").append(System.lineSeparator());
        for (SearchCondition searchCondition : searchConditions) {
            String objectString = null;
            switch (searchCondition.searchConditionType) {
                case BEING_DESCENDANT_OF:
                    objectString = ((JavaGuiElement) searchCondition.objects[0]).getName();
                    break;
                default:
                    objectString = "\"" + searchCondition.objects[0].toString() + "\"";
                    break;

            }
            sb
                    .append("      ")
                    .append(".")
                    .append(searchCondition.searchConditionType.getMethodName())
                    .append("(")
                    .append(objectString)
                    .append(")")
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }

    public boolean isMatch(Object object) {

        boolean match = false;
        Integer ordinalNumber = null;

        for (SearchCondition searchCondition : searchConditions) {

            switch (searchCondition.searchConditionType) {

                case NAME:
                    match = false;
                    String name = (String) MethodInvoker.invokeTheFirstEncounteredMethodFromListOfMethodNames(
                            object,
                            MethodDeclarations.componentNameGetterMethodsInAttemptOrder);
                    if (name == null || name.length() == 0) return false;
                    for (Object nameAlternative : searchCondition.objects) {
                        if (((String) nameAlternative).equals(name)) {
                            match = true;
                            break;
                        }
                    }
                    if (!match) return false;
                    break;

                case CLASS:
                    match = false;
                    for (Object className : searchCondition.objects) {
                        if (object.getClass().getSimpleName().equals((String) className)) {
                            match = true;
                            break;
                        }
                        if (!match && object.getClass().getName().equals((String) className)) {
                            match = true;
                            break;
                        }
                    }
                    if (!match) return false;
                    break;

                case EXACT_TEXT:
                    match = false;
                    String text = (String) MethodInvoker.
                            invokeTheFirstEncounteredMethodFromListOfMethodNames(
                                    object,
                                    MethodDeclarations.textGettingMethodsInAttemptOrder);
                    if (text == null) return false;
                    for (Object textAlternative : searchCondition.objects) {
                        if (text.equals((String) textAlternative)) {
                            match = true;
                            break;
                        }
                    }
                    if (!match) return false;
                    break;

                case TEXT_CONTAINS:
                    match = false;
                    String text1 = (String) MethodInvoker.
                            invokeTheFirstEncounteredMethodFromListOfMethodNames(
                                    object,
                                    MethodDeclarations.textGettingMethodsInAttemptOrder);
                    if (text1 == null) return false;
                    for (Object textAlternative : searchCondition.objects) {
                        if (text1.contains((String) textAlternative)) {
                            match = true;
                            break;
                        }
                    }
                    if (!match) return false;
                    break;

                case TEXT_REGEX_MATCH:
                    match = false;
                    String elementText = (String) MethodInvoker.
                            invokeTheFirstEncounteredMethodFromListOfMethodNames(
                                    object,
                                    MethodDeclarations.textGettingMethodsInAttemptOrder);
                    if (elementText == null) return false;
                    for (Object textAlternative : searchCondition.objects) {
                        if (elementText.matches((String) textAlternative)) {
                            match = true;
                            break;
                        }
                    }
                    if (!match) return false;
                    break;

                case BEING_DESCENDANT_OF:
                    match = false;
                    GenericInteractionMethods java = new GenericInteractionMethods(new TestCase());
                    for (Object potentialParent : searchCondition.objects) {

                        JavaGuiElement javaGuiElement = new JavaGuiElement(potentialParent);
                        for (Object subElement : javaGuiElement.getSubElements()) {
                            if (isMatch(subElement)) {
                                match = true;
                                break;
                            }
                        }
                    }
                    if (!match) return false;
                    break;

                case ORDINAL_NUMBER:
                    ordinalNumber = (Integer) searchCondition.objects[0]; //Needs to be run last to be meaningful
                    break;
            }

        }

        if(ordinalNumber != null){
            match = false;
            JavaWindow window = new JavaWindow((Window)new JavaGuiElement(object).getWindow());
            List<Object> relevantSubComponents = new ArrayList<>();
            for(Object subComponenet : window.getComponents()){
                if(isMatch(subComponenet)){
                    relevantSubComponents.add(subComponenet);
                }
            }
            if(relevantSubComponents.size() > 0 && relevantSubComponents.size() <= ordinalNumber ){
                match = true;
            }
        }

        return match;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[By:").append(System.lineSeparator());
        for(SearchCondition sc : searchConditions){
            sb.append("   ").append(sc.toString()).append(System.lineSeparator());
        }
        sb.append("]");
        return sb.toString();
    }
}

