package se.claremont.autotest.restsupport;

import okhttp3.*;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;

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

    public RestSupport(TestCase testCase){
        this.testCase = testCase;
        try{
            restClient = new OkHttpClient();
            testCase.log(LogLevel.DEBUG, "Created new REST client.");
        } catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not create REST client.");
        }
    }

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

    public String responseBodyFromDeleteRequest(String url) {
        String responseBodyString = null;
        Request request = new Request.Builder().delete().url(url).build();
        Response response;
        try{
            testCase.log(LogLevel.DEBUG, "Executing REST DELETE request '" + request.toString() + "'.");
            response = restClient.newCall(request).execute();
            try{
                responseBodyString = response.body().string();
            }catch (IOException e){
                testCase.log(LogLevel.DEBUG, "Could not get body content as string.");
            }
            testCase.log(LogLevel.EXECUTED, "Response for REST DELETE action = '" + response.message() + "'. Response content: '" + responseBodyString + "'.");
        } catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get REST response for request '" + request.toString() + "' to url '" + url + "'.");
        }
        return responseBodyString;
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("WeakerAccess")
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

    public Object getParameterValueFromResponseFromGetRequest(String url, String parameterName){
        Object parameterValue = JsonParser.get(responseBodyFromGetRequest(url), parameterName);
        testCase.log(LogLevel.DEBUG, "Reading parameter value '" + parameterValue + "' for parameter '" + parameterName + "' from url '" + url + "'.");
        return parameterValue;
    }
}
