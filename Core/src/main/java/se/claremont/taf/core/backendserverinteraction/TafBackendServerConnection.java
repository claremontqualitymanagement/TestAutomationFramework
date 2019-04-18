package se.claremont.taf.core.backendserverinteraction;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import se.claremont.taf.core.logging.LogPost;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testrun.Settings;
import se.claremont.taf.core.testrun.TestRun;

import java.io.IOException;

/**
 * Attempts to connect to a TAF Backend Server, and send test result information to it, if the URL_TO_TAF_BACKEND Settings parameter is set.
 *
 * Created by jordam on 2017-03-19.
 */
public class TafBackendServerConnection {
    private Boolean isConnected = null;
    private Boolean apiVersionCompatible = null;
    private final String tafFrameworkApiVersion = "v1";
    @SuppressWarnings("CanBeFinal")
    public static String defaultServerUrl = "http://-server-url-not-set-/taf";

    public TafBackendServerConnection(){
        if(isConnected() && apiVersionCompatible){
            System.out.println("New successful connection to TAF Backend Server at '" + TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND) + "' with API version '" + tafFrameworkApiVersion + "'.");
        } else {
            System.out.println("Connection to TAF Backend Server at '" + TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND) + "' failed. Is connected: " + String.valueOf(isConnected) + ", API version compatile with this version (Version '" + tafFrameworkApiVersion + "'): " + String.valueOf(apiVersionCompatible) + ".");
        }
    }

    public String getBackendVersion(){
        if(isConnected && apiVersionCompatible){
            return sendGetRequest(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND) + "/version");
        } else {
            return "Could not get TAF Backend Server version. Error: Not connected.";
        }
    }

    public String postTestCase(TestCase testCase){
        String responseBody = null;
        if(isConnected && apiVersionCompatible){
            responseBody = sendPostRequest(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND) + "/" + tafFrameworkApiVersion + "/testcase", "application/json", testCase.toJson());
            System.out.println(responseBody);
        } else {
            System.out.println("Could not post test case to TAF Backend Server version. Error: Not connected or wrong API version.");
        }
        return responseBody;
    }

    public String postTestRunResult(String json){
        String responseBody = null;
        if(isConnected && apiVersionCompatible){
            responseBody = sendPostRequest(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND) + "/" + tafFrameworkApiVersion + "/testrun", "application/json", json);
            System.out.println("Response from test run results posting to TAF Backend Server: " + responseBody);
        } else {
            System.out.println("Could not post test run results to TAF Backend Server version. Error: Not connected or wrong API version.");
        }
        return responseBody;
    }

    public String postLogPost(LogPost logPost){
        String responseBody = null;
        if(isConnected && apiVersionCompatible){
            responseBody = sendPostRequest(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND) + "/" + tafFrameworkApiVersion + "/log", "application/json", logPost.toJson());
            System.out.println(responseBody);
        } else {
            System.out.println("Could not post log entry to TAF Backend Server version. Error: Not connected or wrong API version.");
        }
        return responseBody;
    }

    @SuppressWarnings("unused")
    public boolean apiVersionCompatibleWithBackend(){
        checkConnectionStatus();
        return apiVersionCompatible;
    }

    private void checkConnectionStatus(){
        if(isConnected == null || apiVersionCompatible == null){
            if(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND).equals(defaultServerUrl)){
                isConnected = false;
                apiVersionCompatible = false;
                return;
            }
            String response = sendGetRequest(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND));
            isConnected = response != null && response.length() > 0;
            if(isConnected){
                String versionResponse = sendGetRequest(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND) + "/apiversion");
                if(versionResponse != null && versionResponse.contains(tafFrameworkApiVersion)){
                    apiVersionCompatible = true;
                    return;
                }
            }
            apiVersionCompatible = false;
        }
    }

    private String sendGetRequest(String url){
        RestRequest restRequest = new RestRequest(url);
        Response response = restRequest.execute();
        if(response == null) return null;
        if(response.code() == 200){
            try {
                return response.body().string();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        } else {
            return response.toString();
        }
        return null;
    }

    private String sendPostRequest(String url, @SuppressWarnings("SameParameterValue") String mediaType, String data){
        RestRequest restRequest = new RestRequest(url, mediaType, data);
        restRequest.builder = new Request.Builder().post(RequestBody.create(MediaType.parse(mediaType), data)).url(url);
        Response response = restRequest.execute();
        if(response == null) return null;
        if(response.code() == 200){
            try {
                return response.body().string();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        } else {
            return response.toString();
        }
        return null;
    }

    public boolean isConnected() {
        checkConnectionStatus();
        return isConnected;
    }
}
