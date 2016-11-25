package se.claremont.autotest.restsupport;

import okhttp3.Request;

/**
 * Created by jordam on 2016-11-25.
 */
public class RestDeleteRequest extends RestRequest{

    public RestDeleteRequest(String url){
        super(url);
        builder = new Request.Builder().delete().url(url);
    }

}
