package se.claremont.autotest.javasupport.interaction.elementidentification;

public enum  SearchConditionType {
    CLASS("andByClass"),
    EXACT_TEXT("andByExactText"),
    TEXT_CONTAINS("andByTextContains"),
    TEXT_REGEX_MATCH("andByTextAsRegex"),
    BEING_DESCENDANT_OF("andByBeingDescendantOf"),
    ORDINAL_NUMBER("andByOrdinalNumber"),
    NAME("andByName");

    private String methodName;

    public String getMethodName(){
        return methodName;
    }

    private SearchConditionType(String methodName){
        this.methodName = methodName;
    }
}
