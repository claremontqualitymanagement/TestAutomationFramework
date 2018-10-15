package se.claremont.autotest.restsupport;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;

public class JsonManager {
    String content;
    TestCase testCase;

    public JsonManager(String content, TestCase testCase){
        this.content = content;
        this.testCase = testCase;
    }

    public String getObjectBySimpleXPath(String xPath){
        String originalXpath = xPath;
        String jsonPath = "$";
        if(xPath.contains("@")){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "WARNING: JSON does't support attributes, as in '" + xPath + "'. Attribute ignored.");
        }
        if(xPath.startsWith("//")){
            jsonPath += "..";
            xPath = xPath.substring(1);
        }
        jsonPath += xPath.substring(1).replace("/", ".");
        testCase.log(LogLevel.DEBUG, "Converted XPath '" + originalXpath + "' to JsonPath '" + jsonPath + "'.");
        return getObjectByJsonPath(jsonPath);
    }

    public String getObjectByJsonPath(String jsonPath){
        if(content == null) return null;
        DocumentContext jsonContext = JsonPath.parse(content);
        String returnString = jsonContext.read(jsonPath);
        testCase.log(LogLevel.DEBUG, "Extracted '" + returnString + "' as result of query '" + jsonPath + "'.");
        return returnString;
    }
}
