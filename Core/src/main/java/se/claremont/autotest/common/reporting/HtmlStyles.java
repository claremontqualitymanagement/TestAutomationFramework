package se.claremont.autotest.common.reporting;

import java.util.ArrayList;

/**
 * Enabler to format the presentation of information from external modules with dependencies to TAF Core.
 *
 * Created by jordam on 2017-01-21.
 */
public class HtmlStyles {
    public static ArrayList<String> styleSections = new ArrayList<>();

    public static String asString(){
        return System.lineSeparator() + "    " + String.join(System.lineSeparator() + "    ", styleSections) + System.lineSeparator();
    }

}
