package se.claremont.autotest.websupport.gui.recorder.captureinfrastructure.restserver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.websupport.DomElement;
import se.claremont.autotest.websupport.elementidentification.By;
import se.claremont.autotest.websupport.gui.recorder.RecorderWindow;
import se.claremont.autotest.websupport.gui.teststeps.WebAttributeChangeTestStep;
import se.claremont.autotest.websupport.gui.teststeps.WebClickTestStep;
import se.claremont.autotest.websupport.gui.teststeps.WebInputTestStep;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
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

    @POST
    @Path("v1/click")
    public void clickPerformed(String data){
        data = data.replace("%20", " ");
        System.out.println("Received POST request to http://" +
                HttpServer.getIPAddressesOfLocalMachine() + ":" + Settings.port +
                "/tafwebrecorder/v1/click with content: '" + URLDecoder.decode(data) + "'.");
        Gui.availableTestSteps.add(new WebClickTestStep(new DomElement(URLDecoder.decode(data))));
    }

    @POST
    @Path("v1/checkbox")
    public void checkboxChangePerformed(String data){
        data = data.replace("%20", " ");
        System.out.println("Received POST request to http://" +
                HttpServer.getIPAddressesOfLocalMachine() + ":" + Settings.port +
                "/tafwebrecorder/v1/checkbox with content: '" + URLDecoder.decode(data) + "'.");
        Gui.availableTestSteps.add(new WebClickTestStep(new DomElement(URLDecoder.decode(data))));
    }

    @POST
    @Path("v1/input")
    public void inputPerformed(String data){
        try{
            data = URLDecoder.decode(data.substring("webElement=".length()).replace("%20", " "));
            System.out.println("Received POST request to http://" +
                    HttpServer.getIPAddressesOfLocalMachine() + ":" + Settings.port +
                    "/tafwebrecorder/v1/input with content: '" + data + "'.");
            DomElement domElement = new DomElement(data);
            ObjectMapper om = new ObjectMapper();
            JsonNode jsonNode = null;
            try {
                jsonNode = om.readTree(data);
            } catch (IOException e) {
                System.out.println(e.toString());
            }
            String text = "";
            if(jsonNode.get("text").asText() != null && jsonNode.get("text").asText().length() > 0) {
                text = jsonNode.get("text").asText();
            }
            Gui.availableTestSteps.add(new WebInputTestStep(domElement, text));
        }catch (Throwable ignored){
            System.out.println(ignored.getMessage());
        }
    }

    @POST
    @Path("v1/domchange")
    public void domChanged(String data){
        data = URLDecoder.decode(data.substring("mutation=".length()).replace("%20", " "));
        System.out.println("Received POST request to http://" +
                HttpServer.getIPAddressesOfLocalMachine() + ":" + Settings.port +
                "/tafwebrecorder/v1/domchange with content: '" + data + "'.");
        ObjectMapper om = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = om.readTree(data);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        if(jsonNode == null){
            return;
        }
        if(!valueForAttributeInJson(jsonNode, "targetTagName").toLowerCase().equals("input")) return;
        String elementJSON = "webElement={\"tagName\": \"" + valueForAttributeInJson(jsonNode, "targetTagName") + "\", ";
        elementJSON += "\"className\": \"" + valueForAttributeInJson(jsonNode, "targetClassName") + "\", ";
        elementJSON += "\"xpath\": \"" + valueForAttributeInJson(jsonNode, "targetXpath") + "\",  ";
        elementJSON += "\"text\": \"\", ";
        elementJSON += "\"id\": \"" + valueForAttributeInJson(jsonNode, "tergetId") + "\"}";
        DomElement element = null;
        try{
            element = new DomElement(elementJSON);
        }catch (Exception e){
            System.out.println(e.toString());
        }
        if(valueForAttributeInJson(jsonNode, "type").contains("attributes")){
            WebAttributeChangeTestStep testStep = new WebAttributeChangeTestStep(
                    element,
                    valueForAttributeInJson(jsonNode, "attributeName"),
                    valueForAttributeInJson(jsonNode, "oldValue"),
                    valueForAttributeInJson(jsonNode, "newValue"));
            Gui.availableTestSteps.add(testStep);
        }
    }

    private static String valueForAttributeInJson(JsonNode jsonNode, String attributeName){
        try{
            return jsonNode.get(attributeName).asText().replace("\"", "\\" + "\"");
        }catch (Throwable ignored){}
        return "";
    }
}