package se.claremont.autotest.javamethods;

import se.claremont.autotest.common.testcase.TestCase;

/**
 * Created by jordam on 2017-02-24.
 */
public class MethodDeclarations {
    static String[] titleGetterMethodsInAttemptOrder =             { "getTitle()" };
    static String[] componentNameGetterMethodsInAttemptOrder =     { "getName()" };

    //Used for acquiring clickpoint
    static String[] componentLocationGetterMethodsInAttemptOrder = { "getLocationOnScreen()", "getLocation()" };
    static String[] componentHightGetterMethodsInAttemptOrder =    { "getHeight()" };
    static String[] componentWidthGetterMethodsInAttemptOrder =    { "getWidth()" };
    static String[] methodsToGetLeftPositionInOrder =              { "getX()" };
    static String[] methodsToGetTopPositionInOrder =               { "getY()" };
    //static String[] methodsToGetElementHeightInOrder =             { "getHeight()" };
    //static String[] methodsToGetElementWidthInOrder =              { "getWidth()" };

    //Read and write methods
    static String[] componentWriteMethodsInAttemptOrder =          { "setText(java.lang.String)", "write(java.lang.String)", "type(java.lang.String)" };
    static String[] textGettingMethodsInAttemptOrder =             { "getLabel()", "getText()" };

    //State assessors
    static String[] componentIsVisibleMethodsInAttemptOrder =      { "isShowing()", "isDisplayedWithinTimeout()", "isVisible()" };

    //Component collector helpers
    static String[] componentParentGetterMethodsInAttemptOrder =   { "getParent()" };
    static String[] subComponentCountMethodsInAttemptOrder =       { "getComponentCount()" };
    static String[] subComponentGetterMethodsInAttemptOrder =      { "getComponent(int)" };
    static String[] subAllComponentsGettersMethodsInAttemptOrder = { "getComponents()" };

}
