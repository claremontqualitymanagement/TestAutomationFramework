package se.claremont.autotest.guidriverpluginstructure.websupport;

import se.claremont.autotest.guidriverpluginstructure.GuiElement;
import se.claremont.autotest.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement;

/**
 * Object declaration mechanisms for web elements
 *
 * Created by jordam on 2016-08-17.
 */
public class DomElement implements GuiElement {

    @SuppressWarnings("WeakerAccess")
    public final String name;
    private final String page;
    public final String recognitionString;
    public final IdentificationType identificationType;

    /**
     * Identification mechanisms
     */
    public enum IdentificationType{
        BY_LINK_TEXT,
        BY_X_PATH,
        BY_ID,
        BY_CSS,
        BY_NAME,
        BY_VISIBLE_TEXT
    }

    /**
     * Declares a DOM element to be used in test execution
     * @param recognitionString the recognition string that identifies the object
     * @param identificationType what mechanism to use for identification
     */
    public DomElement (String recognitionString, IdentificationType identificationType){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        //System.out.println(callingMethodUsingConstructor.getMethodName() + " " +  callingMethodUsingConstructor.getClassName());
        name = callingMethodUsingConstructor.getMethodName();
        page = callingMethodUsingConstructor.getClassName();
        this.recognitionString = recognitionString;
        this.identificationType = identificationType;
    }

    /**
     * Enables unified logging formats for element references in the testCaseLog
     * @return a string to use in testCaseLog posts
     */
    public String LogIdentification(){
        return name + " (declared in page class " + page + ")";
    }


}
