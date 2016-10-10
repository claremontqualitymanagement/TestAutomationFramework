package se.claremont.autotest.common;

import se.claremont.autotest.support.SupportMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A list of known errors.
 *
 * Created by jordam on 2016-08-25.
 */
class KnownErrorsList {
    List<KnownError> knownErrors = new ArrayList<>();

    /**
     * Default constructor
     */
    KnownErrorsList(){

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
        for(KnownError knownError : knownErrors) {
            knownError.thisErrorIsEncountered(testCase);
        }
    }

    public boolean testCaseHasAnyKnownError(TestCase testCase){
        for (KnownError knownError: knownErrors){
            for(TestCase test : knownError.testCasesWhereErrorWasEncountered){
                if(test.isSameAs(testCase)){
                    return true;
                }
            }
        }
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
        StringBuilder json = new StringBuilder();
        json.append("   {\"knownerrors\": [").append(SupportMethods.LF);
        List<String> knownErrorStrings = new ArrayList<>();
        for(KnownError knownError : knownErrors){
            knownErrorStrings.add(knownError.toJson());
        }
        json.append(String.join("," + SupportMethods.LF, knownErrorStrings));
        json.append("      ]").append(SupportMethods.LF).append("}").append(SupportMethods.LF);
        return json.toString();
    }
}
