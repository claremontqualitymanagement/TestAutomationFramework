package se.claremont.taf.restsupport;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import se.claremont.taf.core.testcase.TestCase;

/**
 * Building a REST call and executes a request.
 *
 * Created by jordam on 2016-11-25.
 */
@SuppressWarnings("WeakerAccess")
public class RestRequest {
    Request request;
    Request.Builder builder;
    final String url;
    String data;
    String mediaType;
    TestCase testCase;

    /**
     * Constructor for requests without data.
     *
     * @param url the url to send the request to (default = GET request).
     */
    public RestRequest(String url, TestCase testCase){
        this.url = url;
        this.testCase = testCase;
        builder = new Request.Builder().url(url);
    }

    /**
     * Constructor for REST requests transferring data.
     *
     * @param url The url to send the request to.
     * @param mediaType The media type of the data sent.
     * @param data The data sent.
     */
    public RestRequest(String url, String mediaType, String data, TestCase testCase){
        this.url = url;
        this.mediaType = mediaType;
        this.data = data;
        this.testCase = testCase;
        builder = new Request.Builder().url(url);
    }

    /**
     * Method to remove a header value from a REST request.
     *
     * @param name The name of the header parameter to remove.
     */
    @SuppressWarnings("unused")
    public void removeHeaderValue(String name){
        builder.removeHeader(name);
    }

    /**
     * Method to add a header parameter to a REST request.
     *
     * @param name The name of the header parameter.
     * @param value The value of the header parameter.
     */
    @SuppressWarnings("unused")
    public void addHeaderValue(String name, String value){
        builder.addHeader(name, value);
    }

    /**
     * Sends the requests and get the response. This executor uses a generic and simple http client. If you need
     * authorization, proxy settings or extra performance, use the executor that takes a client as parameter and
     * make sure you setup your client.
     *
     * @return Returns the response of the the request.
     */
    public RestResponse execute(){
        return execute(new OkHttpClient());
    }

    /**
     * Sends the request and get the response. This executor uses the provided http client. This executor should
     * be used when client need authorization, proxy settings and so forth.
     *
     * @param client The client used to send the request.
     * @return Returns the response of the request.
     */
    public RestResponse execute(OkHttpClient client){
        long startTime = System.currentTimeMillis();
        Response response;
        RestResponse restResponse = null;
        request = builder.build();
        try {
            response = client.newCall(request).execute();
            restResponse = new RestResponse(response.body().string(), response.headers().toString(), Integer.toString(response.code()), response.message(), response, (int) (System.currentTimeMillis() - startTime), testCase);
        } catch (Exception ignored) {
        }
        return restResponse;
    }

    public @Override String toString(){
        if(request != null) return request.toString();
        if(builder != null) return builder.toString();
        String requestString = "Url: '" + url + "'";
        if(data != null) requestString += ", data: '" + data + "'";
        if(mediaType != null) requestString += ", media type: '" + mediaType + "'";
        return requestString;
    }

}
