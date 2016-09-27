package se.prv.minasidor;

import org.json.JSONObject;
import org.junit.Assert;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.restsupport.JsonParser;
import se.claremont.autotest.restsupport.RestSupport;

/**
 * Test actions for the Profile micro service for Mina Sidor
 *
 * Created by jordam on 2016-09-13.
 */
@SuppressWarnings("SameParameterValue")
class ProfileServiceTestActions {
    private final RestSupport rest;
    private final TestCase testCase;
    //private final String baseServer1 = "http://172.16.12.218";
    private final String baseServer1 = "http://www.prv.se";
    //String baseServer2 = "http://172.16.13.46";
    private final String urlToProfileService = baseServer1 + ":32377/profil";
    //String urlToLoginPage = baseServer1 + ":31211/login";


    public ProfileServiceTestActions(TestCase testCase){
        this.testCase = testCase;
        rest = new RestSupport(this.testCase);
    }

    @SuppressWarnings("WeakerAccess")
    public void getProfile(String personalIdentifier){
        String profile = rest.responseBodyFromGetRequest(urlToProfileService + "/" + personalIdentifier);
        testCase.addTestCaseData("profile", profile);

        if(profile == null || profile.length() < 1){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Couldn't get any response for REST GET request.");
            haltFurtherExecution();
        }

        verifyMandatoryFields(profile);
    }

    public void createProfile(String firstName, String lastName, String address){
        //String profileJson = rest.responseBodyFromPostRequest();
    }

    public void createProfile(String personalIdentifier, String personalIdentifierType, String firstName, String lastName, String addressLineText){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personalIdentifier", personalIdentifier);
        jsonObject.put("personalIdentifierType", personalIdentifierType);
        jsonObject.put("firstName", firstName);
        jsonObject.put("lastName", lastName);
        jsonObject.put("addressLineText", addressLineText);

        String profileJson = rest.responseBodyFromPostRequest(urlToProfileService + "/", "application/json", jsonObject.toString());
        if(profileJson == null || profileJson.length() < 1){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Couldn't get any response for REST POST request.");
            haltFurtherExecution();
        }

        getProfile(personalIdentifier);
    }

    public void deleteProfile(String personalIdentifier) {
//        ResponseEntity responseEntity with httpStatus? = rest.responseBodyFromDeleteRequest(urlToProfileService + "/" +  personalIdentifier);
        String responseEntity = rest.responseBodyFromDeleteRequest(urlToProfileService + "/" +  personalIdentifier);
        if(responseEntity == null){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Couldn't get any response for REST DELETE request.");
            haltFurtherExecution();
        }
    }

    private void haltFurtherExecution(){
        testCase.log(LogLevel.INFO, "Halting further execution.");
        Assert.assertTrue("Aborting further execution", false);
    }

    public void verifyProfileContent(String personalIdentifier){
        String profileJson = rest.responseBodyFromGetRequest(urlToProfileService + "/" + personalIdentifier);
        if(profileJson == null || profileJson.length() < 1){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Couldn't get any response for REST GET request.");
            haltFurtherExecution();
        }
        JsonParser.verifyMandatoryFieldIsNotEmpty(profileJson, "personID", testCase);
        JsonParser.verifyMandatoryFieldIsNotEmpty(profileJson, "personalIdentifier", testCase);
        JsonParser.verifyMandatoryFieldIsNotEmpty(profileJson, "personalIdentifierType", testCase);
        JsonParser.verifyMandatoryFieldIsNotEmpty(profileJson, "createdOf", testCase);
        JsonParser.verifyMandatoryFieldIsNotEmpty(profileJson, "createdWhen", testCase);
        JsonParser.verifyMandatoryFieldIsNotEmpty(profileJson, "countryCode", testCase);
    }

    public void getProfileFamilyName(String personalIdentifier){
        String familyName = rest.getParameterValueFromResponseFromGetRequest(urlToProfileService + "/" + personalIdentifier, "lastName");
        testCase.addTestCaseData("Family name", familyName);
        testCase.log(LogLevel.DEBUG, "Read family name '" + familyName + "' for personal identifier '" + personalIdentifier + "'.");
    }


    public void updateProfile(String personalIdentifier, String personalIdentifierType, String firstName, String lastName) {
        createProfile(personalIdentifier, personalIdentifierType, firstName, lastName, "Gatan 1");

        getProfile(personalIdentifier);

        getProfileFamilyName(personalIdentifier);
        String familyName = testCase.valueForFirstMatchForTestCaseDataParameter("Family name");

        // update
        createProfile(personalIdentifier, personalIdentifierType, firstName, "NyttEfternamn", "Gatan 2");

        getProfileFamilyName(personalIdentifier);
        familyName = testCase.valueForFirstMatchForTestCaseDataParameter("Family name");

        deleteProfile(personalIdentifier);
    }

    /**
     * Mandatory fields: personalIdentifier, personalIdentifierType, firstName, lastName
     * @param profileJson The profile information in JSON format
     */
    private void verifyMandatoryFields(String profileJson) {
        JsonParser.verifyMandatoryFieldIsNotEmpty(profileJson, "personalIdentifier", testCase);
        JsonParser.verifyMandatoryFieldIsNotEmpty(profileJson, "personalIdentifierType", testCase);
        JsonParser.verifyMandatoryFieldIsNotEmpty(profileJson, "firstName", testCase);
        JsonParser.verifyMandatoryFieldIsNotEmpty(profileJson, "lastName", testCase);
    }

}
