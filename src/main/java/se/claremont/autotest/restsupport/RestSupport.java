package se.claremont.autotest.restsupport;

import okhttp3.*;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.dataformats.JsonParser;

import java.io.IOException;

/**
 * Rest support for the framework. Both information vice and communication vice
 *
 * Created by jordam on 2016-09-09.
 */
@SuppressWarnings("SameParameterValue")
public class RestSupport {

    private OkHttpClient restClient = null;
    private final TestCase testCase;

    /**
     * Enables support for interaction with a REST service.
     *
     * @param testCase The test case to log the interaction to.
     */
    public RestSupport(TestCase testCase){
        this.testCase = testCase;
        try{
            restClient = new OkHttpClient();
            testCase.log(LogLevel.DEBUG, "Created new REST client.");
        } catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not create REST client.");
        }
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
        String responseBodyString = null;
        Request request = new Request.Builder().post(RequestBody.create(MediaType.parse(mediaType), data)).url(url).build();
        Response response;
        try{
            testCase.log(LogLevel.DEBUG, "Executing REST POST request '" + request.toString() + "'.");
            response = restClient.newCall(request).execute();
            try{
                responseBodyString = response.body().string();
            }catch (IOException e){
                testCase.log(LogLevel.DEBUG, "Could not get response body content as string.");
            }
            testCase.log(LogLevel.EXECUTED, "Response for REST POST action = '" + response.message() + "'. Response content: '" + responseBodyString + "'.");
        } catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get REST response for request '" + request.toString() + "' to url '" + url + "'.");
        }
        return responseBodyString;
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
        String responseBodyString = null;
        Request request = new Request.Builder().put(RequestBody.create(MediaType.parse(mediaType), data)).url(url).build();
        Response response;
        try{
            testCase.log(LogLevel.DEBUG, "Executing REST PUT request '" + request.toString() + "'.");
            response = restClient.newCall(request).execute();
            try{
                responseBodyString = response.body().string();
            }catch (IOException e){
                testCase.log(LogLevel.DEBUG, "Could not get response body content as string.");
            }
            testCase.log(LogLevel.EXECUTED, "Response for REST PUT action = '" + response.message() + "'. Response content: '" + responseBodyString + "'.");
        } catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get REST response for request '" + request.toString() + "' to url '" + url + "'.");
        }
        return responseBodyString;
    }

    /**
     * Checks if a service is up and ready by checking that a GET request is responded to with the status code 200 (=ok).
     *
     * @param url The url for the service request
     * @return Return true if the response status is 200, othervice false;
     */
    public boolean isRespondingToGetRequest(String url){
        return responseCodeFromGetRequest(url).equals(200);
    }

    /**
     * Get the response body from the response of a DELETE request to a REST service.
     *
     * @param url The url of the REST service
     * @return Return the response body of the request as a string.
     */
    public String responseBodyFromDeleteRequest(String url) {
        String responseBodyString = null;
        Request request = new Request.Builder().delete().url(url).build();
        Response response;
        try{
            testCase.log(LogLevel.DEBUG, "Executing REST DELETE request '" + request.toString() + "'.");
            response = restClient.newCall(request).execute();
            try{
                responseBodyString = response.body().string();

                testCase.log(LogLevel.DEBUG, "Response status code =  '" + response.code() + "'.");
                if(response.code() != 200) testCase.log(LogLevel.EXECUTION_PROBLEM, "Delete didn't end with status 200 (OK). Status code was '" + response.code() + "'.");
            }catch (IOException e){
                testCase.log(LogLevel.DEBUG, "Could not get body content as string.");
            }
            testCase.log(LogLevel.EXECUTED, "Response for REST DELETE action = '" + response.message() + "'. Response content: '" + responseBodyString + "'.");
        } catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get REST response for request '" + request.toString() + "' to url '" + url + "'.");
        }
        return responseBodyString;
    }

    /**
     * Get the response code from the response of a GET request to a REST service.
     *
     * @param url The url of the REST service
     * @return Return the response code of the request as a string.
     */

    public String responseCodeFromGetRequest(String url){
        String responseCode = null;
        Request request = new Request.Builder().url(url).build();
        Response response;
        try{
            testCase.log(LogLevel.DEBUG, "Executing REST request '" + request.toString() + "'.");
            response = restClient.newCall(request).execute();
            try{
                responseCode = Integer.toString(response.code());
            }catch (Exception e){
                testCase.log(LogLevel.DEBUG, "Could not get response code as string.");
            }
            testCase.log(LogLevel.EXECUTED, "REST response = '" + response.message() + "'. Response code: '" + responseCode + "'.");
        } catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get REST response for request '" + request.toString() + "' to url '" + url + "'.");
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
        String bodyString = null;
        Request request = new Request.Builder().url(url).build();
        Response response;
        try{
            testCase.log(LogLevel.DEBUG, "Executing REST request '" + request.toString() + "'.");
            response = restClient.newCall(request).execute();
            try{
                bodyString = response.body().string();
            }catch (IOException e){
                testCase.log(LogLevel.DEBUG, "Could not get body content as string.");
            }
            testCase.log(LogLevel.EXECUTED, "REST response = '" + response.message() + "'. Response content: '" + bodyString + "'.");
        } catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get REST response for request '" + request.toString() + "' to url '" + url + "'.");
        }
        return bodyString;
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
