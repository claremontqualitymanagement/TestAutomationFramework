package se.claremont.autotest.restsupport;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by jordam on 2016-11-25.
 */
class RestPutRequest extends RestRequest {

    RestPutRequest(String url, String mediaType, String data){
        super(url, mediaType, data);
        builder = new Request.Builder().put(RequestBody.create(MediaType.parse(mediaType), data)).url(url);
    }

}
