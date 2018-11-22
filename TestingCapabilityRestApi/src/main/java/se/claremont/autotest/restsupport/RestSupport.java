package se.claremont.autotest.restsupport;

import okhttp3.OkHttpClient;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.json.JsonParser;

import javax.net.ssl.*;
import java.util.HashMap;

/**
 * Rest support for the framework. Both information vice and communication vice
 *
 * Created by jordam on 2016-09-09.
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public class RestSupport {
    private final TestCase testCase;
    public OkHttpClient client;
    public HashMap<String, String> defaultHeaders;
    public HashMap<String, String> singleTimeHeaders;

    /**
     * Enables support for interaction with a REST service.
     *
     * @param testCase The test case to log the interaction to.
     */
    public RestSupport(TestCase testCase){
        this.client = new OkHttpClient();
        this.testCase = testCase;
        defaultHeaders = new HashMap<String, String>();
        singleTimeHeaders = new HashMap<String, String>();
    }

    public void addDefaultHeader(String headerName, String headerValue){
        if(defaultHeaders.containsKey(headerName)){
            defaultHeaders.replace(headerName, headerValue);
            return;
        }
        defaultHeaders.put(headerName, headerValue);
    }

    public void addSingleTimeHeaderValueForNextRequest(String headerName, String headerValue){
        if(defaultHeaders.containsKey(headerName)){
            singleTimeHeaders.replace(headerName, headerValue);
            return;
        }
        singleTimeHeaders.put(headerName, headerValue);
    }

    public RestResponse execute(RestRequest restRequest){
        for(String headerName : defaultHeaders.keySet()){
            restRequest.addHeaderValue(headerName, defaultHeaders.get(headerName));
        }
        for(String headerName : singleTimeHeaders.keySet()){
            restRequest.addHeaderValue(headerName, singleTimeHeaders.get(headerName));
        }
        singleTimeHeaders.clear();
        RestResponse restResponse = restRequest.execute(client);
        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST request [" + restRequest.toString() + "'.");
        } else {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Response for REST request [" + restRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString(),
                    "Response for REST request [" + restRequest.toString() + "]:<pre><code>" + System.lineSeparator() + StringManagement.htmlContentToDisplayableHtmlCode(restResponse.toString()) + "</code></pre>");
        }
        return restResponse;
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
        RestPostRequest restPostRequest = null;
        try {
            restPostRequest = new RestPostRequest(url, mediaType, data, testCase);
        }  catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "[rest request failed] " + e.toString());
        }
        for(String headerName : defaultHeaders.keySet()){
            restPostRequest.addHeaderValue(headerName, defaultHeaders.get(headerName));
        }
        for(String headerName : singleTimeHeaders.keySet()){
            restPostRequest.addHeaderValue(headerName, singleTimeHeaders.get(headerName));
        }
        singleTimeHeaders.clear();
        RestResponse restResponse = restPostRequest.execute(client);
        String bodyString = null;
        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST POST request [" + restPostRequest.toString() + "'.");
        } else {
            bodyString = restResponse.body.toString();
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Response for REST POST request [" + restPostRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString(),
                    "Response for REST POST request [" + restPostRequest.toString() + "]:<pre><code>" + System.lineSeparator() + StringManagement.htmlContentToDisplayableHtmlCode(restResponse.toString()) + "</code></pre>");
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
    @SuppressWarnings("unused")
    public RestResponse responseFromPostRequest(String url, String mediaType, String data) {
        RestPostRequest restPostRequest = null;
        try {
            restPostRequest = new RestPostRequest(url, mediaType, data, testCase);
        }  catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "[rest request failed] " + e.toString());
        }
        for(String headerName : defaultHeaders.keySet()){
            restPostRequest.addHeaderValue(headerName, defaultHeaders.get(headerName));
        }
        for(String headerName : singleTimeHeaders.keySet()){
            restPostRequest.addHeaderValue(headerName, singleTimeHeaders.get(headerName));
        }
        singleTimeHeaders.clear();
        RestResponse restResponse = restPostRequest.execute(client);
        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST POST request [" + restPostRequest.toString() + "'.");
        } else {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Response for REST POST request [" + restPostRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString(),
                    "Response for REST POST request [" + restPostRequest.toString() + "]:<pre><code>" + System.lineSeparator() + StringManagement.htmlContentToDisplayableHtmlCode(restResponse.toString()) + "</code></pre>");
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
    @SuppressWarnings("unused")
    public String responseBodyFromPutRequest(String url, String mediaType, String data) {
        RestPutRequest restPutRequest = null;
        try {
            restPutRequest = new RestPutRequest(url, mediaType, data, testCase);
        }  catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "[rest request failed] " + e.toString());
        }
        for(String headerName : defaultHeaders.keySet()){
            restPutRequest.addHeaderValue(headerName, defaultHeaders.get(headerName));
        }
        for(String headerName : singleTimeHeaders.keySet()){
            restPutRequest.addHeaderValue(headerName, singleTimeHeaders.get(headerName));
        }
        singleTimeHeaders.clear();
        RestResponse restResponse = restPutRequest.execute(client);
        String responseBody = null;
        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST PUT request [" + restPutRequest.toString() + "'.");
        } else {
            responseBody = restResponse.body.toString();
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Response for REST PUT request [" + restPutRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString(),
                    "Response for REST PUT request [" + restPutRequest.toString() + "]:" + System.lineSeparator() + "<pre><code>" + StringManagement.htmlContentToDisplayableHtmlCode(restResponse.toString()) + "</code></pre>");
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
    @SuppressWarnings("unused")
    public RestResponse responseFromPutRequest(String url, String mediaType, String data) {
        RestPutRequest restPutRequest = null;
        try {
            restPutRequest =new RestPutRequest(url, mediaType, data, testCase);
        }  catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "[rest request failed] " + e.toString());
        }
        for(String headerName : defaultHeaders.keySet()){
            restPutRequest.addHeaderValue(headerName, defaultHeaders.get(headerName));
        }
        for(String headerName : singleTimeHeaders.keySet()){
            restPutRequest.addHeaderValue(headerName, singleTimeHeaders.get(headerName));
        }
        singleTimeHeaders.clear();
        RestResponse restResponse = restPutRequest.execute(client);
        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST PUT request [" + restPutRequest.toString() + "'.");
        } else {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Response for REST PUT request [" + restPutRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString(),
                    "Response for REST PUT request [" + restPutRequest.toString() + "]:" + System.lineSeparator() + "<pre><code>" + StringManagement.htmlContentToDisplayableHtmlCode(restResponse.toString()) + "</code></pre>");
        }
        return restResponse;
    }


    /**
     * Checks if a service is up and ready by checking that a GET request is responded to with the status code 200 (=ok).
     *
     * @param url The url for the service request
     * @return Return true if the response status is 200, otherwise false;
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
    @SuppressWarnings("unused")
    public String responseBodyFromDeleteRequest(String url) {
        RestDeleteRequest restDeleteRequest = null;
        try {
            restDeleteRequest = new RestDeleteRequest(url, testCase);
        }  catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "[rest request failed] " + e.toString());
        }
        for(String headerName : defaultHeaders.keySet()){
            restDeleteRequest.addHeaderValue(headerName, defaultHeaders.get(headerName));
        }
        for(String headerName : singleTimeHeaders.keySet()){
            restDeleteRequest.addHeaderValue(headerName, singleTimeHeaders.get(headerName));
        }
        singleTimeHeaders.clear();
        RestResponse restResponse = restDeleteRequest.execute();
        String responseBodyString = null;

        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST DELETE request [" + restDeleteRequest.toString() + "'.");
        } else {
            responseBodyString = restResponse.body.toString();
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Response for REST DELETE request [" + restDeleteRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString(),
                    "Response for REST DELETE request [" + restDeleteRequest.toString() + "]:" + System.lineSeparator() + "<pre><code>" + StringManagement.htmlContentToDisplayableHtmlCode(restResponse.toString()) + "</code></pre>");
        }
        return responseBodyString;
    }


    /**
     * Get the response body from the response of a DELETE request to a REST service.
     *
     * @param url The url of the REST service
     * @return Return the response body of the request as a string.
     */
    @SuppressWarnings("unused")
    public RestResponse responseFromDeleteRequest(String url) {
        RestDeleteRequest restDeleteRequest = null;
        try {
            restDeleteRequest = new RestDeleteRequest(url, testCase);
        }  catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "[rest request failed] " + e.toString());
        }
        for(String headerName : defaultHeaders.keySet()){
            restDeleteRequest.addHeaderValue(headerName, defaultHeaders.get(headerName));
        }
        for(String headerName : singleTimeHeaders.keySet()){
            restDeleteRequest.addHeaderValue(headerName, singleTimeHeaders.get(headerName));
        }
        singleTimeHeaders.clear();
        RestResponse restResponse = restDeleteRequest.execute(client);
        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST DELETE request [" + restDeleteRequest.toString() + "'.");
        } else {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Response for REST DELETE request [" + restDeleteRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString(),
                    "Response for REST DELETE request [" + restDeleteRequest.toString() + "]:" + System.lineSeparator() + "<pre><code>" + StringManagement.htmlContentToDisplayableHtmlCode(restResponse.toString()) + "</code></pre>");
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
        RestGetRequest restGetRequest = null;
        try {
            restGetRequest = new RestGetRequest(url, testCase);
        }  catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "[rest request failed] " + e.toString());
        }
        for(String headerName : defaultHeaders.keySet()){
            restGetRequest.addHeaderValue(headerName, defaultHeaders.get(headerName));
        }
        for(String headerName : singleTimeHeaders.keySet()){
            restGetRequest.addHeaderValue(headerName, singleTimeHeaders.get(headerName));
        }
        singleTimeHeaders.clear();
        RestResponse restResponse = restGetRequest.execute(client);
        String responseCode = null;
        if(restResponse == null) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST GET request [" + restGetRequest.toString() + "'.");
        } else {
            responseCode = restResponse.responseCode;
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Response for REST GET request [" + restGetRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString(),
                    "Response for REST GET request [" + restGetRequest.toString() + "]:" + System.lineSeparator() + "<pre><code>" + StringManagement.htmlContentToDisplayableHtmlCode(restResponse.toString()) + "</code></pre>");
        }
        return responseCode;
    }

    /**
     * Get the response code from the response of a GET request to a REST service.
     *
     * @param url The url of the REST service
     * @return Return the response code of the request as a string.
     */

    public String responseCodeFromGetRequestWithoutLogging(String url){
        RestGetRequest restGetRequest = null;
        try {
            restGetRequest = new RestGetRequest(url, testCase);
        }  catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "[rest request failed] " + e.toString());
        }
        for(String headerName : defaultHeaders.keySet()){
            restGetRequest.addHeaderValue(headerName, defaultHeaders.get(headerName));
        }
        for(String headerName : singleTimeHeaders.keySet()){
            restGetRequest.addHeaderValue(headerName, singleTimeHeaders.get(headerName));
        }
        singleTimeHeaders.clear();
        RestResponse restResponse = restGetRequest.execute(client);
        String responseCode = null;
        if(restResponse != null) {
            responseCode = restResponse.responseCode;
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
        RestGetRequest restGetRequest = null;
        try {
            restGetRequest = new RestGetRequest(url, testCase);
        } catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "[rest request failed] " + e.toString());
        }
        for(String headerName : defaultHeaders.keySet()){
            restGetRequest.addHeaderValue(headerName, defaultHeaders.get(headerName));
        }
        for(String headerName : singleTimeHeaders.keySet()){
            restGetRequest.addHeaderValue(headerName, singleTimeHeaders.get(headerName));
        }
        singleTimeHeaders.clear();
        singleTimeHeaders.clear();
        RestResponse restResponse = restGetRequest.execute(client);
        String responseBody = null;
        if(restResponse == null) {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST GET request [" + restGetRequest.toString() + "'.",
                    "Could not get response for REST GET request <pre><code>" + StringManagement.htmlContentToDisplayableHtmlCode(restGetRequest.toString()) + "</code></pre>.");
        } else {
            responseBody = restResponse.body.toString();
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Response for REST GET request [" + restGetRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString(),
                    "Response for REST GET request <pre><code>" + restGetRequest.toString() + "</code></pre>is: " + System.lineSeparator() + "<pre><code>" + StringManagement.htmlContentToDisplayableHtmlCode(restResponse.toString()) + "</code></pre>");
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
        RestGetRequest restGetRequest = null;
        try {
            restGetRequest = new RestGetRequest(url, testCase);
        }  catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "[rest request failed] " + e.toString());
        }
        for(String headerName : defaultHeaders.keySet()){
            restGetRequest.addHeaderValue(headerName, defaultHeaders.get(headerName));
        }
        for(String headerName : singleTimeHeaders.keySet()){
            restGetRequest.addHeaderValue(headerName, singleTimeHeaders.get(headerName));
        }
        singleTimeHeaders.clear();
        RestResponse restResponse = restGetRequest.execute(client);
        if(restResponse == null) {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTION_PROBLEM, "Could not get response for REST GET request [" + restGetRequest.toString() + "'.",
                    "Could not get response for REST GET request <pre><code>" + StringManagement.htmlContentToDisplayableHtmlCode(restGetRequest.toString()) + "</code></pre>.");
        } else {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Response for REST GET request [" + restGetRequest.toString() + "]:" + System.lineSeparator() + restResponse.toString(),
                    "Response for REST GET request: <pre><code>" + restGetRequest.toString() + "</code></pre>is:" + System.lineSeparator() + "<pre><code>" + StringManagement.htmlContentToDisplayableHtmlCode(restResponse.toString()) + "</code></pre>");
        }
        return restResponse;
    }

    /**
     * Making driver accept unsafe certificates
     */
    public void makeDriverAcceptUnsafeCertificates(){
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = client.newBuilder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            client = builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Log to test case log if the service is up and running or not.
     *
     * @param url The url to check, and expect a status code 200 from.
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public String getParameterValueFromResponseFromGetRequest(String url, String parameterName){
        String parameterValue = JsonParser.get(responseBodyFromGetRequest(url), parameterName);
        testCase.log(LogLevel.DEBUG, "Reading parameter value '" + parameterValue + "' for parameter '" + parameterName + "' from url '" + url + "'.");
        return parameterValue;
    }

}
