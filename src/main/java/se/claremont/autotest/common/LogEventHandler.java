package se.claremont.autotest.common;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;

/**
 * Created by jordam on 2016-10-10.
 */
@Path("logevent")
public class LogEventHandler {
    public LogEventHandler() {
        super();
    }

    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    public String postLogEvent(@Context Request request, String json) {
        System.out.println("received event:" + json);
        //LogEventRouter.sendPost(json);
        return "event received " + json;
    }

    @GET
    @Produces("text/plain")
    public String getLogEvent(@Context Request request) {
        return "nothing to report from getMovieEvent";
    }

}
