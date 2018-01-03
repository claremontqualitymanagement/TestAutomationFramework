package se.claremont.autotest.websupport.gui.recorder;

import org.openqa.selenium.WebDriver;
import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.websupport.gui.recorder.restserver.HttpServer;
import se.claremont.autotest.websupport.gui.recorder.restserver.Settings;
import se.claremont.autotest.websupport.gui.teststeps.WebNavigationTestStep;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

public class RecorderWindow {

    static WebInteractionMethods web;

    public RecorderWindow() {
        HttpServer recorderRESTServer = new HttpServer();
        recorderRESTServer.start();
        web = new WebInteractionMethods(new TestCase());
        web.navigate("http://" + HttpServer.getIPAddressesOfLocalMachine() + ":" + Settings.port + "/tafwebrecorder/instructions");
        invokeJavascriptBasedListeners();
        new Thread(new UrlNavigationListener(web.driver)).start();
    }

    public static void invokeJavascriptBasedListeners() {
        web.executeJavascript(
                //Javascripts.elementChangeListener +
                        Javascripts.domChangeListener +
                        Javascripts.elementXpathExtractor +
                        Javascripts.postHttpRequest +
                        Javascripts.registerDocumentClickListener +
                        Javascripts.textChangedElementListener +
                        Javascripts.addListenersToAllDomElements
        );
    }

    class UrlNavigationListener implements Runnable {

        int checkIntervalInMilliseconds;
        WebDriver driver;
        String lastUrl;

        UrlNavigationListener(WebDriver driver) {
            this.driver = driver;
            lastUrl = driver.getCurrentUrl();
        }

        @Override
        public void run() {
            while (true) {
                try{
                    if (!driver.getCurrentUrl().equals(lastUrl)) {
                        System.out.println("New url detected: '" + driver.getCurrentUrl());
                        lastUrl = driver.getCurrentUrl();
                        Gui.availableTestSteps.add(new WebNavigationTestStep(lastUrl));
                        invokeJavascriptBasedListeners();
                    }
                }catch (Exception ignored){} //Sometimes getCurrentUrl fails during page transitions
                try {
                    Thread.sleep(checkIntervalInMilliseconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
