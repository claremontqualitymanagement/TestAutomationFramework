package se.claremont.autotest.restsupport;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by jordam on 2016-11-25.
 */
class RestPostRequest extends RestRequest {

    RestPostRequest(String url, String mediaType, String data){
        super(url, mediaType, data);
        builder = new Request.Builder().post(RequestBody.create(MediaType.parse(mediaType), data)).url(url);
    }

}
