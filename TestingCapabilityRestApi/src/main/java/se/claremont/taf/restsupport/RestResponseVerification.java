package se.claremont.taf.restsupport;

import se.claremont.taf.core.StringComparisonType;
import se.claremont.taf.core.VerificationMethods;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

public class RestResponseVerification extends VerificationMethods{

    private RestResponse restResponse;

    public RestResponseVerification(RestResponse restResponse, TestCase testCase){
        super(testCase);
        this.restResponse = restResponse;
    }

    @SuppressWarnings("unused")
    public RestResponseVerification isReceived(){
        if(restResponse == null){
            testCase.log(LogLevel.VERIFICATION_FAILED, "Expected response. None registered.");
            wasSuccess = false;
            noFailsInBuilderChain = false;
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Expected response. Response received.");
            wasSuccess = true;
        }
        return this;
    }

    @SuppressWarnings("unused")
    public RestResponseVerification responseReceivedWithinGivenMilliseconds(int milliseconds){
        if(restResponse.responseTimeInMilliseconds > milliseconds){
            testCase.log(LogLevel.VERIFICATION_FAILED, "Expected response to be received within " + milliseconds + " milliseconds, but it took " + restResponse.responseTimeInMilliseconds + " milliseconds.");
            wasSuccess = false;
            noFailsInBuilderChain = false;
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Expected response to be received within " + milliseconds + " milliseconds. It was received after " + restResponse.responseTimeInMilliseconds + " milliseconds.");
            wasSuccess = true;
        }
        return this;
    }


    @SuppressWarnings("unused")
    public RestResponseVerification responseStatusCode(String statusCode){
        if(restResponse == null){
            testCase.log(LogLevel.VERIFICATION_PROBLEM, "Could not verify response status code of null response.");
            wasSuccess = false;
            noFailsInBuilderChain = false;
            return this;
        }
        if(restResponse.responseCode == null){
            if(statusCode == null){
                testCase.log(LogLevel.VERIFICATION_PASSED, "Response for request was null, as expected.");
                wasSuccess = true;
            } else {
                testCase.log(LogLevel.VERIFICATION_FAILED, "Response code for request was null, but was expected to be '" + statusCode + "'.");
                wasSuccess = false;
                noFailsInBuilderChain = false;
            }
            return this;
        }
        if(restResponse.responseCode.equals(statusCode)){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Response code for request was '" + statusCode + "', as expected.");
            wasSuccess = true;
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "Response code for request was expected to be '" + statusCode + "' but it was '" + restResponse.responseCode + "'.");
            wasSuccess = false;
            noFailsInBuilderChain = false;
        }
        return this;
    }

    @SuppressWarnings("unused")
    public RestResponseVerification bodyContent(String searchPattern, StringComparisonType stringComparisonMethod){
        if(restResponse == null){
            testCase.log(LogLevel.VERIFICATION_PROBLEM, "Could not verify body content of null response.");
            wasSuccess = false;
            noFailsInBuilderChain = false;
            return this;
        }
        if(stringComparisonMethod.match(restResponse.body.toString(), searchPattern)){
            testCase.log(LogLevel.VERIFICATION_PASSED, "String '" + searchPattern + "' successfully matched in response body.");
            wasSuccess = true;
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "Could not match '" + searchPattern + "' in the response body:" + System.lineSeparator() + restResponse.body);
            wasSuccess = false;
            noFailsInBuilderChain = false;
        }
        return this;
    }

    @SuppressWarnings("unused")
    public RestResponseVerification bodyIsJson(){
        if(restResponse == null || !restResponse.body.isJson()){
            testCase.log(LogLevel.VERIFICATION_FAILED, "REST response body was expected to be JSON. It was not. Response:" + System.lineSeparator() + restResponse.body);
            wasSuccess = false;
            noFailsInBuilderChain = false;
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Verified that the response body was JSON. It was.");
            wasSuccess = true;
        }
        return this;
    }

    @SuppressWarnings("unused")
    public RestResponseVerification bodyIsXml(){
        if(restResponse == null || !restResponse.body.isXml()){
            testCase.log(LogLevel.VERIFICATION_FAILED, "REST response body was expected to be XML. It was not. Response:" + System.lineSeparator() + restResponse.body);
            wasSuccess = false;
            noFailsInBuilderChain = false;
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Verified that the response body was XML. It was.");
            wasSuccess = true;
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
            wasSuccess = false;
            noFailsInBuilderChain = false;
            return this;
        }
        if(restResponse.body.isXml()){
            if(restResponse.body.getXmlObjects(xPath).getLength() > 0){
                testCase.log(LogLevel.VERIFICATION_PASSED, "Search defined by XPath '" + xPath + "' successfully matched in response body.");
                wasSuccess = true;
            } else {
                testCase.log(LogLevel.VERIFICATION_FAILED, "Search defined by XPath '" + xPath + "' returned no matches in the in the response body:" + System.lineSeparator() + restResponse.body);
                wasSuccess = false;
                noFailsInBuilderChain = false;
            }
        } else if(restResponse.body.isJson()){
            if(restResponse.body.getJsonObjectByXPath(xPath) != null){
                testCase.log(LogLevel.VERIFICATION_PASSED, "Search defined by XPath '" + xPath + "' successfully matched in response body.");
                wasSuccess = true;
            } else {
                testCase.log(LogLevel.VERIFICATION_FAILED, "Search defined by XPath '" + xPath + "' returned no matches in the in the response body:" + System.lineSeparator() + restResponse.body);
                wasSuccess = false;
                noFailsInBuilderChain = false;
            }
        } else {
            testCase.log(LogLevel.VERIFICATION_PROBLEM, "Could not determine if response was JSON or XML. You might resort to specific method for this data type. " + System.lineSeparator() + System.lineSeparator() + restResponse.body);
            wasSuccess = false;
            noFailsInBuilderChain = false;
        }
        return this;
    }

    @SuppressWarnings("unused")
    public RestResponseVerification headerValue(String headerName, String headerValue, StringComparisonType stringComparisonMethod){
        if(restResponse == null){
            testCase.log(LogLevel.VERIFICATION_PROBLEM, "Could not verify header value of null response.");
            wasSuccess = false;
            noFailsInBuilderChain = false;
            return this;
        }
        String actualValue = restResponse.getHeaderValue(headerName);
        if(stringComparisonMethod.match(actualValue, headerValue)){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Header value for '" + headerName + "' was '" + headerValue + "' as expected.");
            wasSuccess = true;
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "Header value for '" + headerName + "' was expected to match '" + headerValue + "' (" + stringComparisonMethod + ") but was '" + actualValue + "'.");
            wasSuccess = false;
            noFailsInBuilderChain = false;
        }
        return this;
    }
}
