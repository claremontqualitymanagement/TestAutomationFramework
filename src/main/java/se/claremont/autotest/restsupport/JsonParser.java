package se.claremont.autotest.restsupport;

import org.json.JSONObject;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;

/**
 * Class for JSON data management and verification
 *
 * Created by jordam on 2016-09-09.
 */
public class JsonParser {

    public static String get(String content, String parameterName){
        JSONObject object = new JSONObject(content);
        return object.getString(parameterName);
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

    public static void nodesToString(String content){
        JSONObject jsonObject = new JSONObject(content);

    }
}
