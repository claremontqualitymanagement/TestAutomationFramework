package se.claremont.autotest.dataformats;

import org.json.JSONArray;
import org.json.JSONObject;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for JSON data management and verification
 *
 * Created by jordam on 2016-09-09.
 */
@SuppressWarnings("SameParameterValue")
public class JsonParser {

    public static String get(String content, String parameterName){
        JSONObject object = null;
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

    public static boolean isJson(String content){
        try {
            JSONObject object = new JSONObject(content);
            return true;
        }catch (Exception e){
            return  false;
        }
    }

    public static Integer getInt(String content, String parameterName){
        if(!isJson(content)) return null;

        if(content == null || parameterName == null) return null;
        JSONObject object = null;
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

    public static void verifyMandatoryFieldIsNotEmpty(String content, String mandatoryParameterName, TestCase testCase){
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

    public static List<String> childObjects(String content, String parentParameter){
        ArrayList<String> returnString = new ArrayList<>();
        JSONObject object = new JSONObject(content);
        JSONArray children = object.getJSONArray(parentParameter);
        for(Object child : children){
            returnString.add(child.toString());
        }
        return returnString;
    }

    public static void nodesToString(String content){
        JSONObject jsonObject = new JSONObject(content);

    }
}
