package se.claremont.taf.core.logging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.claremont.taf.core.support.SupportMethods;
import se.claremont.taf.core.testcase.TestCase;

import java.util.ArrayList;

/**
 * A known error is based on testCaseLog entries for a test case testCaseLog. A known error is considered to occur when all testCaseLog row patterns are found in the same testCaseLog.
 *
 * Created by jordam on 2016-08-25.
 */
@JsonIgnoreProperties({"testCasesWhereErrorWasEncountered"})
public class KnownError {
    @JsonProperty private final String[] regexpPatternMatchForLogString;
    public final ArrayList<TestCase> testCasesWhereErrorWasEncountered = new ArrayList<>();
    @JsonProperty public final String description;

    @SuppressWarnings("WeakerAccess")
    public KnownError(){
        description = "";
        regexpPatternMatchForLogString = new String[]{};
    } //Default constructor for JSON purposes

    /**
     * Creating a known error with several testCaseLog row match strings. All strings must be matched for the known error to be considered a match.
     *
     * @param description Friendly description of the known error
     * @param regexpPatternMatchForLogString Regular expression pattern to use for identification of testCaseLog rows in the pure text representation of the testCaseLog post message (not the HTML representation of ir)
     */
    public KnownError(String description, String[] regexpPatternMatchForLogString){
        this.regexpPatternMatchForLogString = regexpPatternMatchForLogString;
        this.description = description;
    }

    /**
     * Creating a known error that matches single testCaseLog rows from fail or problem testCaseLog rows.
     *
     * @param description Friendly description of the known error
     * @param regexpPatternForLogString Regular expression pattern to use for identification of testCaseLog rows in the pure text representation of the testCaseLog post message (not the HTML representation of ir)
     */
    public KnownError(String description, String regexpPatternForLogString){
        this.regexpPatternMatchForLogString = new String[]{regexpPatternForLogString};
        this.description = description;
    }

    @Override
    public boolean equals(Object theKnownError){
        if(theKnownError.getClass() != KnownError.class){
            return false;
        }
        try {
            KnownError knownError = (KnownError) theKnownError;
            //Description matches
            if(!this.description.equals(knownError.description)) {
                return false;
            }

            //All patterns in 2 exist in 1
            for(String pattern : this.regexpPatternMatchForLogString){
                boolean thisPatternIsMatch = false;
                for(String otherPattern : ((KnownError) theKnownError).regexpPatternMatchForLogString){
                    if(pattern.equals(otherPattern)){
                        thisPatternIsMatch = true;
                        break;
                    }
                }
                if(!thisPatternIsMatch) return false;
            }

            //All patterns in 1 exist in 2
            for(String pattern : ((KnownError) theKnownError).regexpPatternMatchForLogString){
                boolean thisPatternIsMatch = false;
                for(String otherPattern : this.regexpPatternMatchForLogString){
                    if(pattern.equals(otherPattern)){
                        thisPatternIsMatch = true;
                        break;
                    }
                }
                if(!thisPatternIsMatch) return false;
            }
        }catch (Exception e){
            //Not a known error
            return false;
        }
        return true;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (String pattern : regexpPatternMatchForLogString){
            sb.append("'").append(pattern).append("', ");
        }
        return "['" + description + "': " + sb.toString() + "encountered: " + encountered() + "]";
    }

    /**
     * Checks if this {@link KnownError} has been encountered in any {@link TestCase}.
     *
     * @return Return true if encountered
     */
    public boolean encountered(){
        return this.testCasesWhereErrorWasEncountered.size() > 0;
    }


    /**
     * Checks if this error is encountered in a test case execution testCaseLog.
     *
     * @param testCase {@link TestCase} to evaluate
     * @return Returns true if all row patterns of the known error is found in the testCaseLog
     */
    public boolean thisErrorIsEncountered(TestCase testCase){
        for(String pattern : regexpPatternMatchForLogString){
            boolean thisPatternFound = false;
            for(LogPost logPost : testCase.testCaseResult.testCaseLog.onlyErroneousLogPosts()){
                if(SupportMethods.isRegexMatch(logPost.message, pattern)){
                    thisPatternFound = true;
                    logPost.identifiedToBePartOfKnownError = true;
                    break;
                }
            }
            if(!thisPatternFound){
                return false;
            }
        }
        boolean registeredAlready = false;
        for(TestCase registeredTestCase : testCasesWhereErrorWasEncountered){
            if(testCase.isSameAs(registeredTestCase)){
                registeredAlready = true;
                break;
            }
        }
        if(!registeredAlready) testCasesWhereErrorWasEncountered.add(testCase);
        return true;
    }

    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.out.println("Could not create json from the KnownError. Error: " + e.toString());
        }
        return result;
    }

}
