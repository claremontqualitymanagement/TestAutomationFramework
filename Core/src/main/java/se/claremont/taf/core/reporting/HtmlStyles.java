package se.claremont.taf.core.reporting;

import java.util.ArrayList;

/**
 * Enabler to format the presentation of information from external modules with dependencies to TAF Core.
 *
 * Created by jordam on 2017-01-21.
 */
@SuppressWarnings("WeakerAccess")
public class HtmlStyles {
    public final static ArrayList<String> styleSections = new ArrayList<>();

    public static String asString(){
        return System.lineSeparator() + "    " + String.join(System.lineSeparator() + "    ", styleSections) + System.lineSeparator();
    }

    public static void addStyleInfo(String styles){
        for(String section : styleSections){
            if(section.equals(styles)) return;
        }
        styleSections.add(styles);
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static String tableVerificationStyles(){
        String returnString = "    table.tableverificationresulttable   { border:5px solid " + UxColors.DARK_GREY.getHtmlColorCode() + "; border-collapse: collapse; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable td { border: 2px solid " + UxColors.MID_GREY.getHtmlColorCode() + "; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable th { border: 2px solid " + UxColors.DARK_GREY.getHtmlColorCode() + "; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.headlines { border: 1px solid " + UxColors.MID_GREY.getHtmlColorCode() + "; background-color: " + UxColors.MID_GREY.getHtmlColorCode() + "; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.headlines th { color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; text-align: left; font-weight: bold; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.headlines th.found_heading { color: " + UxColors.BLACK.getHtmlColorCode() + "; text-align: left; font-weight: bold; background-color: " + UxColors.GREEN.getHtmlColorCode() + "; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.headlines th.not_found_heading { color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; text-align: left; font-weight: bold; background-color: " + UxColors.YELLOW.getHtmlColorCode() + "; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.unevaluated { color: " + UxColors.MID_GREY.getHtmlColorCode() + "; background-color: " + UxColors.WHITE.getHtmlColorCode() + "; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.no_match { color: " + UxColors.MID_GREY.getHtmlColorCode() + "; background-color: " + UxColors.WHITE.getHtmlColorCode() + "; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.only_matches { color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; background-color: " + UxColors.YELLOW.getHtmlColorCode() + "; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.only_matches td.exact_match { color: " + UxColors.BLACK.getHtmlColorCode() +"; font-weight: bold; background-color: " + UxColors.GREEN.getHtmlColorCode() + "; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.only_matches td.contains_match { color: " + UxColors.BLACK.getHtmlColorCode() +"; background-color: " + UxColors.GREEN.getHtmlColorCode() + "; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.only_matches td.regex_match { color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; background-color: " + UxColors.GREEN.getHtmlColorCode() + "; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.both_matches_and_non_matches td.exact_match { color: " + UxColors.BLACK.getHtmlColorCode() +"; font-weight: bold; background-color: " + UxColors.GREEN.getHtmlColorCode() +"; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.both_matches_and_non_matches td.regex_match { color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; background-color: " + UxColors.GREEN.getHtmlColorCode() + "; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.both_matches_and_non_matches td.contains_match { color: " + UxColors.BLACK.getHtmlColorCode() + "; background-color: " + UxColors.GREEN.getHtmlColorCode() + "; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.both_matches_and_non_matches td.no_match { color: " + UxColors.BLACK.getHtmlColorCode() + "; background-color: " + UxColors.RED.getHtmlColorCode() + "; }" + System.lineSeparator();
        returnString += "    table.tableverificationresulttable tr.tableevaluationrow.both_matches_and_non_matches td.unevaluated { color: " + UxColors.MID_GREY.getHtmlColorCode() + "; background-color: " + UxColors.WHITE.getHtmlColorCode() + "; }" + System.lineSeparator();
        return returnString;
    }

}
