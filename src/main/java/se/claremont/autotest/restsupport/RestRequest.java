package se.claremont.autotest.restsupport;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jordam on 2016-11-25.
 */
public class RestRequest {

    Request request;
    Request.Builder builder;
    String url;
    String data;
    String mediaType;

    public RestRequest(String url){
        this.url = url;
        builder = new Request.Builder().url(url);
    }

    public RestRequest(String url, String mediaType, String data){
        this.url = url;
        this.mediaType = mediaType;
        this.data = data;
        builder = new Request.Builder().url(url);
    }

    public void removeHeaderValue(String name){
        builder.removeHeader(name);
    }

    public void addHeaderValue(String name, String value){
        builder.addHeader(name, value);
    }

    public RestResponse execute(){
        long startTime = System.currentTimeMillis();
        Response response;
        RestResponse restResponse = null;
        request = builder.build();
        try {
            response = RestSupport.client.newCall(request).execute();
            restResponse = new RestResponse(response.body().string(), response.headers().toString(), Integer.toString(response.code()), response.message(), response, (int) (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
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
