package se.claremont.autotest.restsupport;

import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;

public class RestResponseVerification {

    private RestResponse restResponse;
    private TestCase testCase;

    public RestResponseVerification(RestResponse restResponse, TestCase testCase){
        this.restResponse = restResponse;
        this.testCase = testCase;
    }

    @SuppressWarnings("unused")
    public RestResponseVerification isReceived(){
        if(restResponse == null){
            testCase.log(LogLevel.VERIFICATION_FAILED, "Expected response. None registered.");
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Expected response. Response received.");
        }
        return this;
    }

    @SuppressWarnings("unused")
    public RestResponseVerification responseReceivedWithinGivenMilliseconds(int milliseconds){
        if(restResponse.responseTimeInMilliseconds > milliseconds){
            testCase.log(LogLevel.VERIFICATION_FAILED, "Expected response to be received within " + milliseconds + " milliseconds, but it took " + restResponse.responseTimeInMilliseconds + " milliseconds.");
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Expected response to be received within " + milliseconds + " milliseconds. It was received after " + restResponse.responseTimeInMilliseconds + " milliseconds.");
        }
        return this;
    }


    @SuppressWarnings("unused")
    public RestResponseVerification responseStatusCode(String statusCode){
        if(restResponse == null){
            testCase.log(LogLevel.VERIFICATION_PROBLEM, "Could not verify response status code of null response.");
            return this;
        }
        if(restResponse.responseCode == null){
            if(statusCode == null){
                testCase.log(LogLevel.VERIFICATION_PASSED, "Response for request was null, as expected.");
            } else {
                testCase.log(LogLevel.VERIFICATION_FAILED, "Response code for request was null, but was expected to be '" + statusCode + "'.");
            }
            return this;
        }
        if(restResponse.responseCode.equals(statusCode)){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Response code for request was '" + statusCode + "', as expected.");
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "Response code for request was expected to be '" + statusCode + "' but it was '" + restResponse.responseCode + "'.");
        }
        return this;
    }

    @SuppressWarnings("unused")
    public RestResponseVerification bodyContent(String searchPattern, StringComparisonMethod stringComparisonMethod){
        if(restResponse == null){
            testCase.log(LogLevel.VERIFICATION_PROBLEM, "Could not verify body content of null response.");
            return this;
        }
        if(StringComparisonMethod.isMatch(stringComparisonMethod, restResponse.body, searchPattern)){
            testCase.log(LogLevel.VERIFICATION_PASSED, "String '" + searchPattern + "' successfully matched in response body.");
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "Could not match '" + searchPattern + "' in the response body:" + System.lineSeparator() + restResponse.body);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public RestResponseVerification bodyIsJson(){
        if(restResponse == null || !restResponse.isJson()){
            testCase.log(LogLevel.VERIFICATION_FAILED, "REST response body was expected to be JSON. It was not. Response:" + System.lineSeparator() + restResponse.body);
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Verified that the response body was JSON. It was.");
        }
        return this;
    }

    @SuppressWarnings("unused")
    public RestResponseVerification bodyIsXml(){
        if(restResponse == null || !restResponse.isXml()){
            testCase.log(LogLevel.VERIFICATION_FAILED, "REST response body was expected to be XML. It was not. Response:" + System.lineSeparator() + restResponse.body);
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Verified that the response body was XML. It was.");
        }
        return this;
    }

    /**
     * Verifies that a XML or JSON formatted REST response body contains element at the given XPath. For JSON a simplified XPath is applied.
     * @param xPath The XPath expression used for matching
     */
    @SuppressWarnings("unused")
    public RestResponseVerification bodyContainsNode(String xPath){
        if(restResponse == null){
            testCase.log(LogLevel.VERIFICATION_FAILED, "Could not verify XPath '" + xPath + "' to body content of null response.");
            return this;
        }
        if(restResponse.isXml()){
            if(restResponse.getXmlObjects(xPath).getLength() > 0){
                testCase.log(LogLevel.VERIFICATION_PASSED, "Search defined by XPath '" + xPath + "' successfully matched in response body.");
            } else {
                testCase.log(LogLevel.VERIFICATION_FAILED, "Search defined by XPath '" + xPath + "' returned no matches in the in the response body:" + System.lineSeparator() + restResponse.body);
            }
        } else if(restResponse.isJson()){
            if(restResponse.getJsonObjectByXPath(xPath) != null){
                testCase.log(LogLevel.VERIFICATION_PASSED, "Search defined by XPath '" + xPath + "' successfully matched in response body.");
            } else {
                testCase.log(LogLevel.VERIFICATION_FAILED, "Search defined by XPath '" + xPath + "' returned no matches in the in the response body:" + System.lineSeparator() + restResponse.body);
            }
        } else {
            testCase.log(LogLevel.VERIFICATION_PROBLEM, "Could not determine if response was JSON or XML. You might resort to specific method for this data type. " + System.lineSeparator() + System.lineSeparator() + restResponse.body);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public RestResponseVerification headerValue(String headerName, String headerValue, StringComparisonMethod stringComparisonMethod){
        if(restResponse == null){
            testCase.log(LogLevel.VERIFICATION_PROBLEM, "Could not verify header value of null response.");
            return this;
        }
        String actualValue = restResponse.getHeaderValue(headerName);
        if(StringComparisonMethod.isMatch(stringComparisonMethod, headerValue, actualValue)){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Header value for '" + headerName + "' was '" + headerValue + "' as expected.");
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "Header value for '" + headerName + "' was expected to match '" + headerValue + "' (" + stringComparisonMethod + ") but was '" + actualValue + "'.");
        }
        return this;
    }
}
