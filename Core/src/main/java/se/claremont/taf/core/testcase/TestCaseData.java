package se.claremont.taf.core.testcase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.claremont.taf.core.support.ValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2017-06-24.
 */
public class TestCaseData {
    public final List<ValuePair> testCaseDataList = new ArrayList<>();

    @SuppressWarnings("unused")
    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.out.println(e.toString());
        }
        return json;
    }
}
