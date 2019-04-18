package se.claremont.taf.javasupport.interaction.elementidentification;

public enum  SearchConditionType {
    CLASS("andByClass"),
    EXACT_TEXT("andByExactText"),
    TEXT_CONTAINS("andByTextContains"),
    TEXT_REGEX_MATCH("andByTextAsRegex"),
    BEING_DESCENDANT_OF("andByBeingDescendantOf"),
    ORDINAL_NUMBER("andByOrdinalNumber"),
    NAME("andByName"),
    POSITION_BASED("andByRelativePosition");

    private String methodName;

    public String getMethodName(){
        return methodName;
    }

    @Override
    public String toString(){
        String name = super.toString().replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private SearchConditionType(String methodName){
        this.methodName = methodName;
    }
}
