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

    public static void addStyleInfo(String styles){
        for(String section : styleSections){
            if(section.equals(styles)) return;
        }
        styleSections.add(styles);
    }

    public static String tableVerificationStyles(){
        String returnString = "    table.tableverificationresulttable   { border: 5px solid darkgrey; border-collapse: collapse; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable td { border: 1px solid darkgrey; }" + System.lineSeparator();;
        returnString += "    table.tableverificationresulttable th { border: 1px solid darkslategrey; }" + System.lineSeparator();;
        returnString += "    table.tableverificationresulttable tr.headlines { border: 1px solid grey; background-color: lightgrey; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.headlines th { color: darkslategrey; text-align: left; font-weight: bold; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.unevaluated { color: grey; background-color: white; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.no_match { color: grey; background-color: white; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.only_matches { color: darkgrey; background-color: cornsilk; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.only_matches td.exact_match { color: black; font-weight: bold; background-color: lightgreen; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.only_matches td.contains_match { color: black; background-color: lightgreen; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.only_matches td.regex_match { color: darkslategrey; background-color: lightgreen; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.both_matches_and_non_matches td.exact_match { color: black; font-weight: bold; background-color: lightgreen; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.both_matches_and_non_matches td.regex_match { color: darkgrey; background-color: lightgreen; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.both_matches_and_non_matches td.contains_match { color: black; background-color: lightgreen; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.both_matches_and_non_matches td.no_match { color: black; background-color: salmon; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.both_matches_and_non_matches td.unevaluated { color: grey; background-color: white; }" + System.lineSeparator();
        return returnString;
    }

}
