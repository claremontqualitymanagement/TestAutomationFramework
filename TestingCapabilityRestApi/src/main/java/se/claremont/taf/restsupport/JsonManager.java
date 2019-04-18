package se.claremont.taf.restsupport;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

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
        String returnString = null;
        if(content == null) return null;
        DocumentContext jsonContext = JsonPath.parse(content);
        try {
            returnString = jsonContext.read(jsonPath);
        } catch (Exception e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM,"WARNING: Expression '" + jsonPath + "' does not seem to be an JsonPath expression. [" + e.toString() + "]");
        }
        testCase.log(LogLevel.DEBUG, "Extracted '" + returnString + "' as result of query '" + jsonPath + "'.");
        return returnString;
    }
}
