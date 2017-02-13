package se.claremont.autotest.common.reporting.restserver;

import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by jordam on 2016-10-10.
 */
public class RestServer {

    private final static int port = 9998;
    private final static String host = "http://localhost/";
    private static HttpServer server;

    public static void start() {
        URI baseUri = UriBuilder.fromUri(host).port(port).build();
        ResourceConfig config = new ResourceConfig(LogEventHandler.class);
        RestServer.server = JdkHttpServerFactory.createHttpServer(baseUri, config);
    }

    public static void stop(){
        server.stop(port);
    }


}
