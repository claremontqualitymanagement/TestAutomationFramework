package se.claremont.taf.websupport.gui.teststeps;

import se.claremont.taf.gui.teststructure.TestCaseManager;
import se.claremont.taf.websupport.webdrivergluecode.WebInteractionMethods;

public class ReplayManager {

    static class WebDriverSingleton{
        static WebInteractionMethods web = null;

        public static WebInteractionMethods getInstance(){
            if(web == null){
                web = new WebInteractionMethods(TestCaseManager.getTestCase());
            }
            return web;
        }
    }
}
