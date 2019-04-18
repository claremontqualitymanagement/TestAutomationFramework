package se.claremont.taf.restsupport;

import okhttp3.Response;
import org.w3c.dom.NodeList;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

/**
 * Response from REST request
 *
 * Created by jordam on 2016-11-25.
 */
@SuppressWarnings("WeakerAccess")
public class RestResponse {
    public RestResponseBody body;
    public String headers;
    public String responseCode;
    public String message;
    public Response response;
    public int responseTimeInMilliseconds;
    private TestCase testCase;

    public RestResponse(String body, String headers, String responseCode, String message, Response response, int responseTimeInMilliseconds, TestCase testCase){
        this.body = new RestResponseBody(body);
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
                "Body: '" + body.toString() + "'" + System.lineSeparator() +
                "Response code: '" + responseCode + "'" + System.lineSeparator() +
                "Message: '" + message + "'";
    }

    public RestResponseVerification verify(){
        return new RestResponseVerification(this, testCase);
    }

    public class RestResponseBody {
       private String body;

        public RestResponseBody(String body) {
            this.body = body;
        }

        public boolean isXml() {
            if (response == null) return false;
            if (body == null) return false;
            if (body.trim().startsWith("<") && body.trim().endsWith(">")) return true;
            return false;
        }

        public boolean isJson() {
            if (response == null) return false;
            if (body == null) return false;
            if (body.trim().startsWith("{") && body.trim().endsWith("}")) return true;
            return false;
        }


        public NodeList getXmlObjects(String xPath) {
            if (!isXml()) {
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not extract XML object from non-XML content.");
                return null;
            }
            XmlManager xmlManager = null;
            xmlManager = new XmlManager(body, testCase);
            if (!xPath.startsWith("/"))
                testCase.log(LogLevel.EXECUTION_PROBLEM, "WARNING: Expression '" + xPath + "' does not seem to be an XPath expression.");

            return xmlManager.getObjectsByXPath(xPath);
        }

        public String getXmlObjectsAsString(String xPath) {
            if (!isXml()) {
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not extract XML object from non-XML content.");
                return null;
            }
            XmlManager xmlManager = null;
            xmlManager = new XmlManager(body, testCase);
            if (!xPath.startsWith("/"))
                testCase.log(LogLevel.EXECUTION_PROBLEM, "WARNING: Expression '" + xPath + "' does not seem to be an XPath expression.");

            return xmlManager.getObjectStringByXPath(xPath);
        }

        public String getJsonObjectByXPath(String xPath) {
            if (!isJson()) {
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not extract JSON object from non-Json content.");
                return null;
            }
            JsonManager jsonManager = null;
            jsonManager = new JsonManager(body, testCase);
            if (!xPath.startsWith("/"))
                testCase.log(LogLevel.EXECUTION_PROBLEM, "WARNING: Expression '" + xPath + "' does not seem to be an XPath expression.");

            return jsonManager.getObjectBySimpleXPath(xPath);

        }

        @Override
        public String toString(){
            return body;
        }

        public String getJsonObjectByJsonPath(String jsonPath) {
            if (!isJson()) {
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not extract JSON object from non-Json content.");
                return null;
            }
            JsonManager jsonManager = null;
            jsonManager = new JsonManager(body, testCase);

            return jsonManager.getObjectByJsonPath(jsonPath);

        }
    }

}
