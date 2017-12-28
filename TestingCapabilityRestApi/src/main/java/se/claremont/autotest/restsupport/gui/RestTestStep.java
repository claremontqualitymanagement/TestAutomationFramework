package se.claremont.autotest.restsupport.gui;

import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.restsupport.*;

import java.util.ArrayList;
import java.util.List;

public class RestTestStep extends TestStep{

    List<HeaderValue> headerValues = new ArrayList<>();
    String mediaType = "text/html";

    public RestTestStep(){
        super();
    }

    public void addHeaderValue(String name, String value){
        headerValues.add(new HeaderValue(name, value));
    }
    public void setMediaType(String mediaType){
        this.mediaType = mediaType;
    }

    public RestTestStep(String name, String description){
        super(name, description);
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public void setAssociatedData(Object data) {
        this.data = data;
    }

    @Override
    public String getTestStepTypeShortName() {
        return "REST";
    }

    @Override
    public TestStepResult execute() {
        RestSupport restSupport = new RestSupport(new TestCase());
        RestRequest restRequest = null;
        RestResponse response = null;
        try {
            switch (actionName) {
                case "PUT":
                    restRequest = new RestPutRequest(elementName, mediaType, (String)data);
                    for(HeaderValue headerPair : headerValues){
                        restRequest.addHeaderValue(headerPair.name, headerPair.value);
                    }
                    response = restRequest.execute(restSupport.client);
                    break;
                case "GET":
                    restRequest = new RestGetRequest(elementName);
                    for(HeaderValue headerPair : headerValues){
                        restRequest.addHeaderValue(headerPair.name, headerPair.value);
                    }
                    response = restRequest.execute(restSupport.client);
                    break;
                case "POST":
                    restRequest = new RestPostRequest(elementName, mediaType, (String)data);
                    for(HeaderValue headerPair : headerValues){
                        restRequest.addHeaderValue(headerPair.name, headerPair.value);
                    }
                    response = restRequest.execute(restSupport.client);
                    break;
                case "DELETE":
                    restRequest = new RestDeleteRequest(elementName);
                    for(HeaderValue headerPair : headerValues){
                        restRequest.addHeaderValue(headerPair.name, headerPair.value);
                    }
                    response = restRequest.execute(restSupport.client);
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            return new TestStepResult(this, TestStepResult.Result.FAIL);
        }
        return new TestStepResult(this, TestStepResult.Result.PASS);
    }

    class HeaderValue{
        String name;
        String value;

        HeaderValue(String name, String value){
            this.name = name;
            this.value = value;
        }
    }
}
