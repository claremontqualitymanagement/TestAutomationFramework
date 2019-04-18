package se.claremont.taf.core;

import se.claremont.taf.core.support.SupportMethods;

public enum StringComparisonType {
    Regex,
    Exact,
    Contains,
    ExactIgnoreCase,
    ContainsIgnoreCase;

    public boolean match(String inputString, String pattern) {
        switch (this){
            case Regex:
                return SupportMethods.isRegexMatch(inputString, pattern);
            case Exact:
                if(inputString == null && pattern == null) return true;
                if(inputString == null || pattern == null) return false;
                if(inputString.equals(pattern))return true;
                return false;
            case Contains:
                if(inputString == null && pattern == null) return true;
                if(inputString == null || pattern == null) return false;
                if(inputString.contains(pattern))return true;
                return false;
            case ExactIgnoreCase:
                if(inputString == null && pattern == null) return true;
                if(inputString == null || pattern == null) return false;
                if(inputString.toLowerCase().equals(pattern.toLowerCase()))return true;
                return false;
            case ContainsIgnoreCase:
                if(inputString == null && pattern == null) return true;
                if(inputString == null || pattern == null) return false;
                if(inputString.toLowerCase().contains(pattern.toLowerCase()))return true;
                return false;
            default:
                return false;
        }
    }
}
