package se.claremont.autotest.restsupport;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Usage of POST mechanism for REST
 *
 * Created by jordam on 2016-11-25.
 */
public class RestPostRequest extends RestRequest {

    public RestPostRequest(String url, String mediaType, String data){
        super(url, mediaType, data);
        builder = new Request.Builder().post(RequestBody.create(MediaType.parse(mediaType), data)).url(url);
    }

}
