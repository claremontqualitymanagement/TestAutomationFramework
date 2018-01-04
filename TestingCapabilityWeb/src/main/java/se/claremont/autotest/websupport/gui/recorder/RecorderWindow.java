package se.claremont.autotest.websupport.gui.recorder;

import org.openqa.selenium.WebDriver;
import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.gui.guistyle.TafButton;
import se.claremont.autotest.common.gui.guistyle.TafDialog;
import se.claremont.autotest.common.gui.guistyle.TafFrame;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.websupport.gui.recorder.captureinfrastructure.Javascripts;
import se.claremont.autotest.websupport.gui.recorder.captureinfrastructure.restserver.HttpServer;
import se.claremont.autotest.websupport.gui.recorder.captureinfrastructure.restserver.Settings;
import se.claremont.autotest.websupport.gui.recorder.capturemanagers.ClickCaptureManager;
import se.claremont.autotest.websupport.gui.recorder.capturemanagers.DomChangeCaptureManager;
import se.claremont.autotest.websupport.gui.teststeps.WebNavigationTestStep;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

public class RecorderWindow {

    WebInteractionMethods web;
    WebRecorder webRecorder;

    public RecorderWindow(TafFrame parentWindow) {
        web = new WebInteractionMethods(new TestCase());
        try{
            webRecorder = new WebRecorder(web.driver);
            new Thread(new RecordingEnvironment(web, webRecorder)).start();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        RecordStopButtonWindow recordStopButtonWindow = new RecordStopButtonWindow(parentWindow, web, webRecorder);
        new Thread(recordStopButtonWindow).start();
    }

    class RecordStopButtonWindow extends TafDialog implements Runnable{
        TafButton stopRecordingButton = new TafButton("Stop recording");
        TafDialog recordingWindow;
        TafFrame parentWindow;
        WebInteractionMethods web;
        WebRecorder recorder;

        public RecordStopButtonWindow(TafFrame parentWindow, WebInteractionMethods web, WebRecorder recorder){
            this.parentWindow = parentWindow;
            this.recorder = recorder;
            this.web = web;
        }

        public TafDialog getRecordingWindow() {
            return recordingWindow;
        }

        @Override
        public void run() {
            recordingWindow = new TafDialog(parentWindow, "TAF - Web recorder", true);
            recordingWindow.setLocationRelativeTo(parentWindow);
            stopRecordingButton.setEnabled(false);
            stopRecordingButton.addActionListener(e -> {
                if(web != null)web.makeSureDriverIsClosed();
                if(webRecorder!=null)webRecorder.stop();
                recordingWindow.setVisible(false);
                recordingWindow.dispose();
            });
            stopRecordingButton.setText("Stop recording");
            stopRecordingButton.setEnabled(true);
            stopRecordingButton.revalidate();
            recordingWindow.getContentPane().add(stopRecordingButton);
            recordingWindow.pack();
            recordingWindow.setVisible(true);
        }
    }

    class RecordingEnvironment implements Runnable{

        WebInteractionMethods web;
        WebRecorder recorder;

        public RecordingEnvironment(WebInteractionMethods web, WebRecorder recorder){
            this.recorder = recorder;
            this.web = web;
        }

        @Override
        public void run() {
            try {
                webRecorder.start();
            } catch (WebRecorder.WebDriverIsNotJavaScriptExecutorException e) {
                e.printStackTrace();
            }
        }
    }

}
