package se.claremont.autotest;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.gui.createtesttab.CreateTestTabPanel;
import se.claremont.autotest.common.gui.teststructure.SubProcedureTestStep;
import se.claremont.autotest.common.testrun.CliTestRunner;
import se.claremont.autotest.restsupport.gui.RestTestStep;

public class TAF implements Runnable{

    String[] args;

    public TAF(String[] args){
        this.args = args;
    }

    public static void main(String[] args){

        RestTestStep testStep1 = new RestTestStep("GET to http://claremont.se", "GET request to http://claremont.se.");
        testStep1.setActionName("GET");
        testStep1.setElementName("http://claremont.se");
        testStep1.setAssociatedData(null);

        RestTestStep testStep2 = new RestTestStep("POST to http://claremont.se", "POST request to http://claremont.se/user with data: '{id: 34531}'.");
        testStep2.setActionName("POST");
        testStep2.setElementName("http://claremont.se/user");
        testStep2.setAssociatedData("{id: 34531}");

        Gui.availableTestSteps.add(testStep1);
        Gui.availableTestSteps.add(testStep2);
        Thread t = new Thread(new TAF(args));
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            System.out.println("Closed TAF,");
        }
    }

    @Override
    public void run() {
        CliTestRunner.main(args);
    }
}
