package se.claremont.autotest.websupport.gui.guispy;

import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.websupport.gui.recorder.captureinfrastructure.restserver.HttpServer;
import se.claremont.autotest.websupport.gui.recorder.captureinfrastructure.restserver.Settings;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

import javax.swing.*;

public class GuiSpy {

    TafFrame window = new TafFrame("TAF - GUI Spy web");

    String elementListener = "function(){ var q = document.querySelectorAll(\":hover\"); return q[q.length-1]; })()";

    public GuiSpy(){
        WebInteractionMethods web = new WebInteractionMethods(new TestCase());
        web.navigate("http://" + HttpServer.getIPAddressesOfLocalMachine() + ":" + Settings.port + "/guispy");
        web.executeJavascript(elementListener);
        TafLabel currentElementLabel = new TafLabel("Current element:");
        TafTextArea currentElementDescriptionArea = new TafTextArea("CurrentElementDescriptionArea");
        TafCloseButton closeButton = new TafCloseButton(window);
        TafPanel panel = new TafPanel("GuiSpyContentPanel");
        panel.add(currentElementLabel);
        panel.add(currentElementDescriptionArea);
        panel.add(closeButton);
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.getContentPane().add(panel);
        window.pack();
        window.setVisible(true);
    }
}
