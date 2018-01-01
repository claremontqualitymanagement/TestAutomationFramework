package se.claremont.autotest.websupport.gui.teststeps;

import se.claremont.autotest.common.gui.teststructure.TestCaseManager;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

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
