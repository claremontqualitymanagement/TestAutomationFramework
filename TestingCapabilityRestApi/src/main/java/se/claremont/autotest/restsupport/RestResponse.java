package se.claremont.autotest.restsupport;

import okhttp3.Response;
import org.w3c.dom.NodeList;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;

import java.io.IOException;

/**
 * Response from REST request
 *
 * Created by jordam on 2016-11-25.
 */
@SuppressWarnings("WeakerAccess")
public class RestResponse {
    public String body;
    public String headers;
    public String responseCode;
    public String message;
    public Response response;
    public int responseTimeInMilliseconds;
    private TestCase testCase;

    public RestResponse(String body, String headers, String responseCode, String message, Response response, int responseTimeInMilliseconds, TestCase testCase){
        this.body = body;
        this.headers = headers;
        this.responseCode = responseCode;
        this.message = message;
        this.response = response;
        this.responseTimeInMilliseconds = responseTimeInMilliseconds;
        this.testCase = testCase;
    }

    @SuppressWarnings("unused")
    public String getHeaderValue(String name){
        if(response == null) return  null;
        return response.headers().get(name);
    }

    @SuppressWarnings("unused")
    public boolean tookLongerThan(int milliseconds){
        return (responseTimeInMilliseconds > milliseconds);
    }

    @SuppressWarnings("unused")
    public boolean isSuccessful(){
        return (responseCode.equals("200"));
    }

    public @Override String toString(){
        return "Header: '" + headers + "'" + System.lineSeparator() +
                "Body: '" + body + "'" + System.lineSeparator() +
                "Response code: '" + responseCode + "'" + System.lineSeparator() +
                "Message: '" + message + "'";
    }

    public boolean isXml() {
        if(response == null) return false;
        if(response.body() == null) return false;
        try {
            if(response.body().string().trim().startsWith("<") && response.body().string().trim().endsWith(">"))return true;
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public boolean isJson(){
        if(response == null) return false;
        if(response.body() == null) return false;
        try {
            if(response.body().string().trim().startsWith("{") && response.body().string().trim().endsWith("}"))return true;
        } catch (IOException e) {
            return false;
        }
        return false;
    }


    public NodeList getXmlObjects(String xPath){
        if(!isXml()){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not extract XML object from non-XML content.");
            return null;
        }
        XmlManager xmlManager = null;
        try {
            xmlManager = new XmlManager(response.body().string(), testCase);
        } catch (IOException e) {
            return null;
        }
        if(!xPath.startsWith("/"))
            testCase.log(LogLevel.EXECUTION_PROBLEM, "WARNING: Expression '" + xPath + "' does not seem to be an XPath expression.");

        return xmlManager.getObjectsByXPath(xPath);
    }

    public String getXmlObjectsAsString(String xPath){
        if(!isXml()){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not extract XML object from non-XML content.");
            return null;
        }
        XmlManager xmlManager = null;
        try {
            xmlManager = new XmlManager(response.body().string(), testCase);
        } catch (IOException e) {
            return null;
        }
        if(!xPath.startsWith("/"))
            testCase.log(LogLevel.EXECUTION_PROBLEM, "WARNING: Expression '" + xPath + "' does not seem to be an XPath expression.");

        return xmlManager.getObjectStringByXPath(xPath);
    }

    public String getJsonObjectByXPath(String xPath){
        if(!isJson()){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not extract JSON object from non-Json content.");
            return null;
        }
        JsonManager jsonManager = null;
        try {
            jsonManager = new JsonManager(response.body().string(), testCase);
        } catch (IOException e) {
            return null;
        }
        if(!xPath.startsWith("/"))
            testCase.log(LogLevel.EXECUTION_PROBLEM, "WARNING: Expression '" + xPath + "' does not seem to be an XPath expression.");

        return jsonManager.getObjectBySimpleXPath(xPath);

    }

    public String getJsonObjectByJsonPath(String jsonPath){
        if(!isJson()){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not extract JSON object from non-Json content.");
            return null;
        }
        JsonManager jsonManager = null;
        try {
            jsonManager = new JsonManager(response.body().string(), testCase);
        } catch (IOException e) {
            return null;
        }
        if(!jsonPath.startsWith("$") || !jsonPath.startsWith(".."))
            testCase.log(LogLevel.EXECUTION_PROBLEM, "WARNING: Expression '" + jsonPath + "' does not seem to be an JsonPath expression.");

        return jsonManager.getObjectByJsonPath(jsonPath);

    }

    public RestResponseVerification verify(){
        return new RestResponseVerification(this, testCase);
    }

}
