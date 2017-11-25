package se.claremont.autotest.javasupport.interaction.elementidentification;

public class SearchCondition {
    public Object[] objects;
    public SearchConditionType searchConditionType;

    public SearchCondition(SearchConditionType searchConditionType, Object... objects) {
        this.searchConditionType = searchConditionType;
        this.objects = objects;
    }
}
