package se.claremont.taf.json;

import org.json.JSONArray;
import org.json.JSONObject;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for JSON data management and verification
 *
 * Created by jordam on 2016-09-09.
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public class JsonParser {

    public static String get(String content, String parameterName){
        JSONObject object;
        if(!isJson(content)) return null;
        String returnValue = null;
        try {
            object = new JSONObject(content);
        }catch (Exception e){
            return null;
        }
        try {
            returnValue = object.getString(parameterName);
        }catch (Exception ignored){}
        return returnValue;
    }

    @SuppressWarnings("unused")
    public static boolean isJson(String content){
        try {
            @SuppressWarnings("unused")
            JSONObject object = new JSONObject(content);
            return true;
        }catch (Exception e){
            return  false;
        }
    }

    public static Integer getInt(String content, String parameterName){
        if(!isJson(content)) return null;

        if(content == null || parameterName == null) return null;
        JSONObject object;
        try {
            object = new JSONObject(content);
        }catch (Exception ignored){
            return null;
        }
        Integer returnValue = null;
        try{
            returnValue = object.getInt(parameterName);
        }catch (Exception ignored){}
        return returnValue;
    }

    @SuppressWarnings("unused")
    public static void verifyMandatoryFieldIsNotEmpty(String content, String mandatoryParameterName, TestCase testCase){
        if(content == null) testCase.log(LogLevel.VERIFICATION_FAILED, "Content was null when trying to verify that mandatory parameter '" + mandatoryParameterName + "' was not empty.");
        if(mandatoryParameterName == null) testCase.log(LogLevel.VERIFICATION_PROBLEM, "mandatoryParameterName was null when trying to verify that mandatory parameter was not empty.");
        JSONObject object;
        boolean parameterNameExist = true;
        String parameterValue = null;
        try {
            object = new JSONObject(content);
            if(!object.has(mandatoryParameterName)){
                parameterNameExist = false;
            } else {
                parameterValue = object.get(mandatoryParameterName).toString();
            }
        }catch (Exception e){
            testCase.log(LogLevel.FRAMEWORK_ERROR, "Could not create JSONObject from content '" + content + "'.");
        }
        if(parameterValue != null && parameterValue.length() > 0){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Mandatory parameter '" + mandatoryParameterName + "' was populated with content '" + parameterValue + "'.");
        }else {
            if(parameterNameExist){
                testCase.log(LogLevel.VERIFICATION_FAILED, "Mandatory parameter '" + mandatoryParameterName + "' had no content. Mandatory parameters cannot be empty.");
            } else {
                testCase.log(LogLevel.VERIFICATION_FAILED, "Mandatory parameter '" + mandatoryParameterName + "' is missing. Mandatory fields should be populated.");
            }
            testCase.log(LogLevel.DEVIATION_EXTRA_INFO, "JSON content for where the parameter '" + mandatoryParameterName + "' was looked for: '" + content + "'.");
        }
    }

    @SuppressWarnings("unused")
    public static List<String> childObjects(String content, String parentParameter){
        ArrayList<String> returnString = new ArrayList<>();
        JSONObject object = new JSONObject(content);
        JSONArray children = object.getJSONArray(parentParameter);
        for(Object child : children){
            returnString.add(child.toString());
        }
        return returnString;
    }

    @SuppressWarnings("unused")
    public static String jsonToHtml(String json){
        StringBuilder html = new StringBuilder();
        json = json.replace(System.lineSeparator(), "");
        String[] parts = json.split("}");
        for(String part : parts){
            String[] values = part.split(",");
            for(String value : values){
                html.append(value).append(",<br>").append(System.lineSeparator());
            }
            html.append("}<br>").append(System.lineSeparator());
        }
        return html.toString();
    }
}
