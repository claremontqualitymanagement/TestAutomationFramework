package se.claremont.autotest.websupport.gui.recorder;

import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.websupport.gui.recorder.restserver.HttpServer;
import se.claremont.autotest.websupport.gui.recorder.restserver.Settings;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

public class RecorderWindow {

    public RecorderWindow(){
        HttpServer recorderRESTServer = new HttpServer();
        recorderRESTServer.start();
        WebInteractionMethods web = new WebInteractionMethods(new TestCase());
        web.navigate("http://" + HttpServer.getIPAddressesOfLocalMachine() + ":" + Settings.port + "/tafwebrecorder/instructions");
        web.executeJavascript(Javascripts.postHttpRequest + "\n" + Javascripts.registerDocumentClickListener);
    }

}
