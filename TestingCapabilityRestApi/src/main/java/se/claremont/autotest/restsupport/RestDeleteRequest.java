package se.claremont.autotest.restsupport;

import okhttp3.Request;

/**
 * Usage of DELETE mechanism for REST
 *
 * Created by jordam on 2016-11-25.
 */
class RestDeleteRequest extends RestRequest{

    RestDeleteRequest(String url){
        super(url);
        builder = new Request.Builder().delete().url(url);
    }

}
