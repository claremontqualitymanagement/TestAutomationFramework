package se.claremont.autotest.restsupport;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import se.claremont.autotest.common.testcase.TestCase;

/**
 * Usage of POST mechanism for REST
 *
 * Created by jordam on 2016-11-25.
 */
public class RestPostRequest extends RestRequest {

    public RestPostRequest(String url, String mediaType, String data, TestCase testCase){
        super(url, mediaType, data, testCase);
        builder = new Request.Builder().post(RequestBody.create(MediaType.parse(mediaType), data)).url(url);
    }

}
