package se.claremont.autotest.restsupport;

import se.claremont.autotest.common.testcase.TestCase;

/**
 * Usage of GET mechanism for REST
 *
 * Created by jordam on 2016-11-25.
 */
public class RestGetRequest extends RestRequest {

    public RestGetRequest(String url, TestCase testCase){
        super(url, testCase);
    }

}
