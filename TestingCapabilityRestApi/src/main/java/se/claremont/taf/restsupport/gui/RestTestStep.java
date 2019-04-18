package se.claremont.taf.restsupport.gui;

import se.claremont.taf.core.gui.teststructure.TestCaseManager;
import se.claremont.taf.core.gui.teststructure.TestStep;
import se.claremont.taf.core.gui.teststructure.TestStepResult;
import se.claremont.taf.restsupport.*;

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

    @Override
    public String asCode() {
        TestCaseManager.testSetCode.makeSureRequiredImportIsAdded("import se.claremont.autotest.restsupport.*;");
        TestCaseManager.testSetCode.makeSureClassVariableIsDeclared("RestSupport rest;");
        TestCaseManager.testSetCode.makeSureBeginTestSectionDeclarationExist("rest = new RestSupport(currentTestCase());");
        return "RestResponse response = rest.responseFromGetRequest(\"" + elementName + "\");";
    }

    @Override
    public TestStep clone() {
        RestTestStep clonedStep = new RestTestStep(getName(), getDescription());
        clonedStep.setActionName(actionName);
        clonedStep.setElementName(elementName);
        clonedStep.setAssociatedData(data);
        for(HeaderValue headerValue : headerValues){
            clonedStep.addHeaderValue(headerValue.name, headerValue.value);
        }
        clonedStep.mediaType = mediaType;
        return clonedStep;

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
        TestCaseManager.startTestStep();
        RestSupport restSupport = new RestSupport(TestCaseManager.getTestCase());
        RestRequest restRequest = null;
        RestResponse response = null;
        try {
            switch (actionName) {
                case "PUT":
                    restRequest = new RestPutRequest(elementName, mediaType, (String)data, getTestCase());
                    for(HeaderValue headerPair : headerValues){
                        restRequest.addHeaderValue(headerPair.name, headerPair.value);
                    }
                    response = restRequest.execute(restSupport.client);
                    break;
                case "GET":
                    restRequest = new RestGetRequest(elementName, getTestCase());
                    for(HeaderValue headerPair : headerValues){
                        restRequest.addHeaderValue(headerPair.name, headerPair.value);
                    }
                    response = restRequest.execute(restSupport.client);
                    break;
                case "POST":
                    restRequest = new RestPostRequest(elementName, mediaType, (String)data, getTestCase());
                    for(HeaderValue headerPair : headerValues){
                        restRequest.addHeaderValue(headerPair.name, headerPair.value);
                    }
                    response = restRequest.execute(restSupport.client);
                    break;
                case "DELETE":
                    restRequest = new RestDeleteRequest(elementName, getTestCase());
                    for(HeaderValue headerPair : headerValues){
                        restRequest.addHeaderValue(headerPair.name, headerPair.value);
                    }
                    response = restRequest.execute(restSupport.client);
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            TestCaseManager.wrapUpTestCase();
            return new TestStepResult(this, TestStepResult.Result.FAIL);
        }
        TestCaseManager.wrapUpTestCase();
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
