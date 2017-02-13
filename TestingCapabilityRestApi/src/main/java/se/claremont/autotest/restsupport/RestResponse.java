package se.claremont.autotest.restsupport;

import okhttp3.Response;

/**
 * Response from REST request
 *
 * Created by jordam on 2016-11-25.
 */
@SuppressWarnings("WeakerAccess")
public class RestResponse {
    public String body = null;
    public String headers = null;
    public String responseCode = null;
    public String message = null;
    public Response response = null;
    public int responseTimeInMilliseconds = 0;

    public RestResponse(String body, String headers, String responseCode, String message, Response response, int responseTimeInMilliseconds){
        this.body = body;
        this.headers = headers;
        this.responseCode = responseCode;
        this.message = message;
        this.response = response;
        this.responseTimeInMilliseconds = responseTimeInMilliseconds;
    }

    @SuppressWarnings("unused")
    public String getHeaderValue(String name){
        if(response == null) return  null;
        return response.headers().get(name);
    }

    @SuppressWarnings("unused")
    public boolean tookLongerThan(int milliseconds){
        return (responseTimeInMilliseconds > milliseconds);
    }

    @SuppressWarnings("unused")
    public boolean isSuccessful(){
        return (responseCode.equals("200"));
    }

    public @Override String toString(){
        return "Header: '" + headers + "'" + System.lineSeparator() +
                "Body: '" + body + "'" + System.lineSeparator() +
                "Response code: '" + responseCode + "'" + System.lineSeparator() +
                "Message: '" + message + "'";
    }
}
