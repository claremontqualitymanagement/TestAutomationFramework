package se.claremont.autotest.javasupport.interaction;

import se.claremont.autotest.common.testcase.TestCase;

/**
 * Lists of what methods to invoke are used throughout this module, so it made sense gathering them here.
 *
 * If the dream of having a generic approach to test automation is achieved these should be moved to
 * core and any technology module should just make sure their methods are within these lists - and
 * expose their own method overrides and error handling methods.
 *
 * Created by jordam on 2017-02-24.
 */
public class MethodDeclarations {
    public static String[] titleGetterMethodsInAttemptOrder =             { "getTitle()" };
    public static String[] componentNameGetterMethodsInAttemptOrder =     { "getName()" };

    //Used for acquiring clickpoint
    static String[] componentLocationGetterMethodsInAttemptOrder = { "getLocationOnScreen()", "getLocation()" };
    public static String[] componentHightGetterMethodsInAttemptOrder =    { "getHeight()" };
    public static String[] componentWidthGetterMethodsInAttemptOrder =    { "getWidth()" };
    public static String[] methodsToGetLeftPositionInOrder =              { "getX()" };
    public static String[] methodsToGetTopPositionInOrder =               { "getY()" };
    //static String[] methodsToGetElementHeightInOrder =             { "getHeight()" };
    //static String[] methodsToGetElementWidthInOrder =              { "getWidth()" };

    //Read and write methods
    public static String[] componentWriteMethodsInAttemptOrder =          { "setText(java.lang.String)", "write(java.lang.String)", "type(java.lang.String)" };
    public static String[] textGettingMethodsInAttemptOrder =             { "getText()", "getLabel()" };

    //State assessors
    public static String[] componentIsVisibleMethodsInAttemptOrder =      { "isShowing()", "isDisplayedWithinTimeout()", "isVisible()" };

    //Component collector helpers
    public static String[] componentParentGetterMethodsInAttemptOrder =   { "getParent()" };
    public static String[] subComponentCountMethodsInAttemptOrder =       { "getComponentCount()" };
    public static String[] subComponentGetterMethodsInAttemptOrder =      { "getComponent(int)" };
    public static String[] subAllComponentsGettersMethodsInAttemptOrder = { "getComponents()" };

    //ComboBox interactions
    public static String[] getAllSelectedObjectsInComboBox =              { "getSelectedObjects()" }; //returns Object[]
    public static String[] getItemCountOfComboBoxOptions =                { "getItemCount()" }; //returns int
    public static String[] getSpecificComboBoxItemBasedOnIndex =          { "getItemAt(int)" }; //returns Object
    public static String[] setSelectionItemBasedOnIndex =                 { "setSelectedIndex(int)" }; //void
    public static String[] getAllPossibleOptionsInCombobox =              { "getItems()" }; //returns ObservableList for JavaFX

    //Checkbox interactions
    public static String[] getCheckboxCurrentStatus =                     { "isSelected()", "getState()" };
    public static String[] setCheckboxCurrentStatus =                     { "setSelected(boolean)", "setState(boolean)" };
}
