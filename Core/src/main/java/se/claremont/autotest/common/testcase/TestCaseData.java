package se.claremont.autotest.common.testcase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.claremont.autotest.common.support.ValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2017-06-24.
 */
public class TestCaseData {
    public List<ValuePair> testCaseDataList = new ArrayList<>();

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
