package se.claremont.autotest.websupport;

import se.claremont.autotest.common.guidriverpluginstructure.GuiElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Object declaration mechanisms for web elements
 *
 * Created by jordam on 2016-08-17.
 */
@SuppressWarnings("unused")
public class DomElement implements GuiElement {

    @SuppressWarnings("WeakerAccess")
    public final String name;
    private final String page;
    public final List<String> recognitionStrings;
    public final IdentificationType identificationType;
    public Integer ordinalNumber = null;

    /**
     * Identification mechanisms
     */
    public enum IdentificationType{
        BY_LINK_TEXT,
        BY_X_PATH,
        BY_ID,
        BY_CLASS,
        BY_CSS,
        BY_NAME,
        BY_VISIBLE_TEXT,
        By_ATTRIBUTE_VALUE
    }

    /**
     * Declares a DOM element to be used in test execution
     * @param recognitionString the recognition string that identifies the object
     * @param identificationType what mechanism to use for identification
     */
    public DomElement (String recognitionString, IdentificationType identificationType){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        this.name = callingMethodUsingConstructor.getMethodName();
        this.page = callingMethodUsingConstructor.getClassName();
        this.recognitionStrings = new ArrayList<>(Collections.singletonList(recognitionString));
        this.identificationType = identificationType;
    }

    /**
     * Constructor for use for example with several languages
     *
     * @param alternativeRecognitionStrings An array of recognition strings for this element
     * @param identificationType The method of identification
     */
    public DomElement(String[] alternativeRecognitionStrings, IdentificationType identificationType){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        this.name = callingMethodUsingConstructor.getMethodName();
        this.page = callingMethodUsingConstructor.getClassName();
        this.identificationType = identificationType;
        this.recognitionStrings = new ArrayList<>(Arrays.asList(alternativeRecognitionStrings));
    }

    /**
     * Declares a DOM element to be used in test execution
     * @param recognitionString the recognition string that identifies the object
     * @param identificationType what mechanism to use for identification
     * @param ordinalNumber The ordinal number of the occurrence on the web page, if multiple matches for search criteria.
     */
    public DomElement (String recognitionString, IdentificationType identificationType, Integer ordinalNumber){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        this.name = callingMethodUsingConstructor.getMethodName();
        this.page = callingMethodUsingConstructor.getClassName();
        this.recognitionStrings = new ArrayList<>(Collections.singletonList(recognitionString));
        this.ordinalNumber = ordinalNumber;
        this.identificationType = identificationType;
    }

    /**
     * Constructor for use for example with several languages
     *
     * @param alternativeRecognitionStrings An array of recognition strings for this element
     * @param identificationType The method of identification
     * @param ordinalNumber The ordinal number of the occurrence on the web page, if multiple matches for search criteria.
     */
    public DomElement(String[] alternativeRecognitionStrings, IdentificationType identificationType, Integer ordinalNumber){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethodUsingConstructor = stackTraceElements[2];
        this.name = callingMethodUsingConstructor.getMethodName();
        this.ordinalNumber = ordinalNumber;
        this.page = callingMethodUsingConstructor.getClassName();
        this.recognitionStrings = new ArrayList<>(Arrays.asList(alternativeRecognitionStrings));
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
