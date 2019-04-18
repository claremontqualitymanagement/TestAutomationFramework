package se.claremont.taf.websupport.elementidentification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class SearchCondition implements Serializable{
    private final SearchConditionType type;
    private final Object[] values;

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
