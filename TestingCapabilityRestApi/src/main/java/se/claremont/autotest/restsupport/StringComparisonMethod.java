package se.claremont.autotest.restsupport;

import se.claremont.autotest.common.support.SupportMethods;

public enum StringComparisonMethod {

    ExactMatch,
    RegularExpression,
    ContainsMatch,
    ContainsMatchIgnoreCase,
    ExactMatchIgnoreCase;

    public static boolean isMatch(StringComparisonMethod stringComparisonMethod, String searchPattern, String actualValue){
        if(searchPattern == null && actualValue == null) return true;
        if(searchPattern == null || actualValue == null) return false;
        switch (stringComparisonMethod){
            case ExactMatch:
                if(actualValue.equals(searchPattern)) return true;
                return false;
            case ContainsMatch:
                if(actualValue.contains(searchPattern)) return true;
                return false;
            case ExactMatchIgnoreCase:
                if(actualValue.toLowerCase().equals(searchPattern.toLowerCase())) return true;
                return false;
            case ContainsMatchIgnoreCase:
                if(actualValue.toLowerCase().contains(searchPattern.toLowerCase()))return true;
                return false;
            case RegularExpression:
                return SupportMethods.isRegexMatch(actualValue, searchPattern);
            default:
                return false;
        }
    }
}
