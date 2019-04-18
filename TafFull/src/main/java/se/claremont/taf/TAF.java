package se.claremont.taf;

import se.claremont.taf.gui.Gui;
import se.claremont.taf.testrun.CliTestRunner;
import se.claremont.taf.restsupport.gui.RestTestStep;

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

        Gui.addTestStepToListOfAvailableTestSteps(testStep1);
        Gui.addTestStepToListOfAvailableTestSteps(testStep2);
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
