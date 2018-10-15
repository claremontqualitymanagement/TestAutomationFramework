package se.claremont.autotest.restsupport;

import okhttp3.Request;
import se.claremont.autotest.common.testcase.TestCase;

/**
 * Usage of DELETE mechanism for REST
 *
 * Created by jordam on 2016-11-25.
 */
public class RestDeleteRequest extends RestRequest{

    public RestDeleteRequest(String url, TestCase testCase){
        super(url, testCase);
        builder = new Request.Builder().delete().url(url);
    }

}
