package se.claremont.autotest.websupport.gui.recorder.restserver;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.websupport.DomElement;
import se.claremont.autotest.websupport.gui.teststeps.WebClickTestStep;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * The different end-points of the HTTP server
 *
 * Created by jordam on 2017-03-18.
 */
@Path("tafwebrecorder")
public class Resource {

    /**
     * Returns version of this server.
     *
     * @return Returns version of this server.
     */
    @GET
    @Path("version")
    @Produces(MediaType.TEXT_HTML)
    public String versionHtml() {
        return "<html><body><p>TAF Recording REST server code version 1.0.</p></body></html>";
    }

    /**
     * Landing page for general identification and instructions of this server.
     *
     * @return Return general identification information
     */
    @GET
    @Path("instructions")
    @Produces(MediaType.TEXT_HTML)
    public String instructions() {
        return
                "<html>" +
                "   <body>" +
                "      <h1>TAF web recording</h1>" +
                "      <p>" +
                "         Use at own risk." +
                "      </p>" +
                "    </body>" +
                "</html>";
    }

    /**
     * API endpoint for posting test run results from TAF to be read into Testlink. The response is status information.
     *
     * @param data The JSON from the TAF test run execution TestRunResults object.
     * @return Returns a report from the information transfer. If errors are encountered more information is displayed.
     */
    @POST
    @Path("v1/click")
    public void postTestRun(String data){
        data = data.replace("%20", " ");
        System.out.println("Received POST request to http://" +
                HttpServer.getIPAddressesOfLocalMachine() + ":" + Settings.port +
                "/tafwebrecorder/v1/click with content: '" + URLDecoder.decode(data) + "'.");
        Gui.availableTestSteps.add(new WebClickTestStep(new DomElement(URLDecoder.decode(data))));
    }
}