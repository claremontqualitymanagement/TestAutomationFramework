package se.claremont.taf.core.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.claremont.taf.core.testcase.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A list of known errors.
 *
 * Created by jordam on 2016-08-25.
 */
@SuppressWarnings("unused")
public class KnownErrorsList {
    @JsonProperty public List<KnownError> knownErrors = new ArrayList<>();

    /**
     * Default constructor
     */
    public KnownErrorsList(){

    }

    @SuppressWarnings("WeakerAccess")
    KnownErrorsList(List<KnownError> knownErrors){
        this.knownErrors = knownErrors;
    }

    /*
    public static KnownErrorsList returnEncounteredKnownErrorsFromKnownErrorsListMatchedToLog(KnownErrorsList knownErrorsList, TestCaseLog testCaseLog){
        KnownErrorsList returnList = new KnownErrorsList();
        for(KnownError knownError : knownErrorsList.knownErrors) {
            boolean initialEncounteredValue = knownError.encountered();
            if(knownError.thisErrorIsEncountered(testCaseLog)){
                returnList.knownErrors.add(knownError);
            }
        }
        return returnList;

    }
     */

    public void add(KnownError knownError){
        knownErrors.add(knownError);
    }

    public int size(){
        return knownErrors.size();
    }

    public void assessLogForKnownErrors(TestCase testCase){
        for(KnownError knownError : knownErrors) knownError.thisErrorIsEncountered(testCase);
    }

    public boolean testCaseHasAnyKnownError(TestCase testCase){
        for (KnownError knownError: knownErrors)
            for (TestCase test : knownError.testCasesWhereErrorWasEncountered) if (test.isSameAs(testCase)) return true;
        return false;
    }

    public KnownErrorsList nonencounteredKnownErrors(){
        List<KnownError> nonencountered = knownErrors.stream().filter(knownError -> !knownError.encountered()).collect(Collectors.toList());
        return new KnownErrorsList(nonencountered);
    }

    public KnownErrorsList encounteredKnownErrors(){
        List<KnownError> encountered = knownErrors.stream().filter(KnownError::encountered).collect(Collectors.toList());
        return new KnownErrorsList(encountered);
    }

    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.out.println("Could not create json from KnownErrorList. Error: "+ e.toString());
        }
        return result;
    }
}
