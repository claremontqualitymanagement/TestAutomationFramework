package se.claremont.autotest.websupport.gui.recorder;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.websupport.gui.recorder.captureinfrastructure.Javascripts;
import se.claremont.autotest.websupport.gui.recorder.captureinfrastructure.restserver.HttpServer;
import se.claremont.autotest.websupport.gui.recorder.captureinfrastructure.restserver.Settings;
import se.claremont.autotest.websupport.gui.recorder.capturemanagers.CaptureInfrastructureManager;
import se.claremont.autotest.websupport.gui.recorder.capturemanagers.ClickCaptureManager;
import se.claremont.autotest.websupport.gui.recorder.capturemanagers.DomChangeCaptureManager;
import se.claremont.autotest.websupport.gui.recorder.capturemanagers.InputCaptureManager;
import se.claremont.autotest.websupport.gui.teststeps.WebNavigationTestStep;

public class WebRecorder {

    WebDriver driver;
    HttpServer recorderRESTServer;

    public WebRecorder(WebDriver driver) throws NullWebDriverException {
        if(driver == null) throw new NullWebDriverException();
        this.driver = driver;
    }

    public class NullWebDriverException extends Exception{}

    public void start() throws WebDriverIsNotJavaScriptExecutorException {
        if (!(driver instanceof JavascriptExecutor)) throw new WebDriverIsNotJavaScriptExecutorException();
        recorderRESTServer = new HttpServer();
        recorderRESTServer.start();
        try {
            driver.get("http://" + HttpServer.getIPAddressesOfLocalMachine() + ":" + Settings.port + "/tafwebrecorder/instructions");
        }catch (Exception ignored){}
        invokeJavascriptBasedListeners();
        new Thread(new UrlNavigationListener(driver)).start();
    }

    public void stop(){
        if(recorderRESTServer != null) {
            try{
                recorderRESTServer.stop();
            }catch (Throwable ignored){
            }
        }
        if(driver != null) driver.quit();
    }

    public class WebDriverIsNotJavaScriptExecutorException extends Exception{}

    public Object executeJavascript(String script){
        Object returnObject = null;
            try {
                JavascriptExecutor javascriptExecutor = ((JavascriptExecutor)driver);
                returnObject = javascriptExecutor.executeScript(script);
                String returnObjectAsString = null;
                if(returnObject != null){
                    try{
                        returnObjectAsString = returnObject.toString();
                    }catch (Exception ignored){}
                }
            }catch (Exception e){
                System.out.println(e.toString());
            }
        return returnObject;
    }


    public void invokeJavascriptBasedListeners() {
        executeJavascript(
                new CaptureInfrastructureManager().script +
                Javascripts.textChangedOrCheckboxClickedElementListener +
                new DomChangeCaptureManager().script + //Captures added DOM elements and adds listeners to those.
                new InputCaptureManager().script + //Captures input activities on page
                new ClickCaptureManager().script + //Captures click events on whole page
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
