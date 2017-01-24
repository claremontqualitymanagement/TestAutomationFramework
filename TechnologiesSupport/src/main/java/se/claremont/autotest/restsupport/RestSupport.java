package se.claremont.autotest.restsupport;

import okhttp3.OkHttpClient;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.dataformats.JsonParser;

/**
 * Rest support for the framework. Both information vice and communication vice
 *
 * Created by jordam on 2016-09-09.
 */
@SuppressWarnings("SameParameterValue")
public class RestSupport {
    private final TestCase testCase;
    public OkHttpClient client;

    /**
     * Enables support for interaction with a REST service.
     *
     * @param testCase The test case to log the interaction to.
     */
    public RestSupport(TestCase testCase){
        this.client = new OkHttpClient();
        this.testCase = testCase;
    }


    /**
     * Get the response body from the response of a POST request to a REST service.
     *
     * @param url The url of the REST service
     * @param mediaType The media type of the data
     * @param data The data to post
     * @return Return the response body of the request as a string.
     */
    public String responseBodyFromPostRequest(String url, String mediaType, String data) {
        RestPostRequest restPostRequest = new RestPostRequest(url, mediaType, data);
        RestResponse restResponse = restPostRequest.execute(client);
        String bodyString = null;
        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST POST request [" + restPostRequest.toString() + "'.");
        } else {
            bodyString = restResponse.body;
            testCase.log(LogLevel.EXECUTED, "Response for REST POST request [" + restPostRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString());
        }
        return bodyString;
    }

    /**
     * Get the response body from the response of a POST request to a REST service.
     *
     * @param url The url of the REST service
     * @param mediaType The media type of the data
     * @param data The data to post
     * @return Return the response body of the request as a string.
     */
    public RestResponse responseFromPostRequest(String url, String mediaType, String data) {
        RestPostRequest restPostRequest = new RestPostRequest(url, mediaType, data);
        RestResponse restResponse = restPostRequest.execute(client);
        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST POST request [" + restPostRequest.toString() + "'.");
        } else {
            testCase.log(LogLevel.EXECUTED, "Response for REST POST request [" + restPostRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString());
        }
        return restResponse;
    }

    /**
     * Get the response body from the response of a PUT request to a REST service.
     *
     * @param url The url of the REST service
     * @param mediaType The media type of the data
     * @param data The data to put
     * @return Return the response body of the request as a string.
     */
    public String responseBodyFromPutRequest(String url, String mediaType, String data) {
        RestPutRequest restPutRequest = new RestPutRequest(url, mediaType, data);
        RestResponse restResponse = restPutRequest.execute(client);
        String responseBody = null;
        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST PUT request [" + restPutRequest.toString() + "'.");
        } else {
            responseBody = restResponse.body;
            testCase.log(LogLevel.EXECUTED, "Response for REST PUT request [" + restPutRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString());
        }
        return responseBody;
    }

    /**
     * Get the response body from the response of a PUT request to a REST service.
     *
     * @param url The url of the REST service
     * @param mediaType The media type of the data
     * @param data The data to put
     * @return Return the response body of the request as a string.
     */
    public RestResponse responseFromPutRequest(String url, String mediaType, String data) {
        RestPutRequest restPutRequest = new RestPutRequest(url, mediaType, data);
        RestResponse restResponse = restPutRequest.execute(client);
        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST PUT request [" + restPutRequest.toString() + "'.");
        } else {
            testCase.log(LogLevel.EXECUTED, "Response for REST PUT request [" + restPutRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString());
        }
        return restResponse;
    }


    /**
     * Checks if a service is up and ready by checking that a GET request is responded to with the status code 200 (=ok).
     *
     * @param url The url for the service request
     * @return Return true if the response status is 200, othervice false;
     */
    public boolean isRespondingToGetRequest(String url){
        return responseCodeFromGetRequest(url).equals( "200" );
    }


    /**
     * Get the response body from the response of a DELETE request to a REST service.
     *
     * @param url The url of the REST service
     * @return Return the response body of the request as a string.
     */
    public String responseBodyFromDeleteRequest(String url) {
        RestDeleteRequest restDeleteRequest = new RestDeleteRequest(url);
        RestResponse restResponse = restDeleteRequest.execute();
        String responseBodyString = null;

        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST DELETE request [" + restDeleteRequest.toString() + "'.");
        } else {
            responseBodyString = restResponse.body;
            testCase.log(LogLevel.EXECUTED, "Response for REST DELETE request [" + restDeleteRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString());
        }
        return responseBodyString;
    }


    /**
     * Get the response body from the response of a DELETE request to a REST service.
     *
     * @param url The url of the REST service
     * @return Return the response body of the request as a string.
     */
    public RestResponse responseFromDeleteRequest(String url) {
        RestDeleteRequest restDeleteRequest = new RestDeleteRequest(url);
        RestResponse restResponse = restDeleteRequest.execute(client);
        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST DELETE request [" + restDeleteRequest.toString() + "'.");
        } else {
            testCase.log(LogLevel.EXECUTED, "Response for REST DELETE request [" + restDeleteRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString());
        }
        return restResponse;
    }


    /**
     * Get the response code from the response of a GET request to a REST service.
     *
     * @param url The url of the REST service
     * @return Return the response code of the request as a string.
     */

    public String responseCodeFromGetRequest(String url){
        RestGetRequest restGetRequest = new RestGetRequest(url);
        RestResponse restResponse = restGetRequest.execute(client);
        String responseCode = null;
        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST GET request [" + restGetRequest.toString() + "'.");
        } else {
            responseCode = restResponse.responseCode;
            testCase.log(LogLevel.EXECUTED, "Response for REST GET request [" + restGetRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString());
        }
        return responseCode;
    }


    /**
     * Get the response body from the response of a GET request to a REST service.
     *
     * @param url The url of the REST service
     * @return Return the response body of the request as a string.
     */
    public String responseBodyFromGetRequest(String url) {
        RestGetRequest restGetRequest = new RestGetRequest(url);
        RestResponse restResponse = restGetRequest.execute(client);
        String responseBody = null;
        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST GET request [" + restGetRequest.toString() + "'.");
        } else {
            responseBody = restResponse.body;
            testCase.log(LogLevel.EXECUTED, "Response for REST GET request [" + restGetRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString());
        }
        return responseBody;
    }

    /**
     * Get the response body from the response of a GET request to a REST service.
     *
     * @param url The url of the REST service
     * @return Return the response body of the request as a string.
     */
    public RestResponse responseFromGetRequest(String url) {
        RestGetRequest restGetRequest = new RestGetRequest(url);
        RestResponse restResponse = restGetRequest.execute(client);
        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST GET request [" + restGetRequest.toString() + "'.");
        } else {
            testCase.log(LogLevel.EXECUTED, "Response for REST GET request [" + restGetRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString());
        }
        return restResponse;
    }

    /**
     * Log to test case log if the service is up and running or not.
     *
     * @param url The url to check, and expect a status code 200 from.
     */
    public void verifyServiceIsResponding(String url){
        if(isRespondingToGetRequest(url)){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Service up and running. Request to '" + url + "' returned status code 200 (=ok).");
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "Service is not up and responding. Request to '" + url + "' did not return status code 200, but '" + responseCodeFromGetRequest(url) + "'..");
        }
    }

    /**
     * Get the parameter value of the given parameter from the response of the request.
     *
     * @param url Request url
     * @param parameterName Name of JSON parameter
     * @return Return the value of the given parameter, if the parameter exist.
     */
    public String getParameterValueFromResponseFromGetRequest(String url, String parameterName){
        String parameterValue = JsonParser.get(responseBodyFromGetRequest(url), parameterName);
        testCase.log(LogLevel.DEBUG, "Reading parameter value '" + parameterValue + "' for parameter '" + parameterName + "' from url '" + url + "'.");
        return parameterValue;
    }

}
