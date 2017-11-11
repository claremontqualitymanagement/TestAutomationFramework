package se.claremont.autotest.websupport.elementidentification;

import java.util.ArrayList;
import java.util.List;

public class SearchCondition {
    SearchConditionType type;
    Object[] values;

    public SearchCondition(SearchConditionType type, Object... values) {
        this.type = type;
        this.values = values;
    }

    public SearchConditionType getType() {
        return type;
    }

    public Object value() {
        if (values.length == 0) return null;
        return values[0];
    }

    public Object[] Values() {
        return values;
    }

    @Override
    public String toString() {
        List<String> parametervalues = new ArrayList<>();
        for (Object o : values) {
            parametervalues.add(o.toString());
        }
        return type.toString() + ": '" + String.join("', '", parametervalues) + "'";
    }

}
