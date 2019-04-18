package se.claremont.taf.core.backendserverinteraction;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Building a REST call and executes a request.
 *
 */
@SuppressWarnings("WeakerAccess")
public class RestRequest {
    Request request;
    Request.Builder builder;
    final String url;
    String data;
    String mediaType;

    /**
     * Constructor for requests without data.
     *
     * @param url the url to send the request to (default = GET request).
     */
    public RestRequest(String url){
        this.url = url;
        builder = new Request.Builder().url(url);
    }

    /**
     * Constructor for REST requests transferring data.
     *
     * @param url The url to send the request to.
     * @param mediaType The media type of the data sent.
     * @param data The data sent.
     */
    public RestRequest(String url, String mediaType, String data){
        this.url = url;
        this.mediaType = mediaType;
        this.data = data;
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
    public Response execute(){
        return execute(new OkHttpClient());
    }

    /**
     * Sends the request and get the response. This executor uses the provided http client. This executor should
     * be used when client need authorization, proxy settings and so forth.
     *
     * @param client The client used to send the request.
     * @return Returns the response of the request.
     */
    public Response execute(OkHttpClient client){
        Response response = null;
        request = builder.build();
        try {
            response = client.newCall(request).execute();
        } catch (Exception ignored) {
        }
        return response;
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
