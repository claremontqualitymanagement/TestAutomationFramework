package se.claremont.autotest.common.backendserverinteraction;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.reporting.testrunreports.TafBackendServerTestRunReporter;

import java.io.IOException;

/**
 * Created by jordam on 2017-03-19.
 */
public class BackendServerConnection {
    private Boolean isConnected = null;
    private Boolean apiVersionCompatible = null;
    private String tafFrameworkApiVersion = "v1";
    public static String defaultServerUrl = "http://-server-url-not-set-/taf";

    public BackendServerConnection(){
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

    public boolean apiVersionCompatibleWithBackend(){
        checkConnectionStatus();
        return apiVersionCompatible;
    }

    private void checkConnectionStatus(){
        if(isConnected == null || apiVersionCompatible == null){
            TestRun.initializeIfNotInitialized();
            if(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND).equals(defaultServerUrl)){
                isConnected = false;
                apiVersionCompatible = false;
                return;
            }
            String response = sendGetRequest(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND));
            if(response != null && response.length() > 0){
                isConnected = true;
            } else {
                isConnected = false;
            }
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
        if(restRequest == null) return null;
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

    private String sendPostRequest(String url, String mediaType, String data){
        RestRequest restRequest = new RestRequest(url, mediaType, data);
        if(restRequest == null) return null;
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
