package se.claremont.taf.core.reporting.testrunreports.htmlsummaryreport;

import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.logging.LogPost;
import se.claremont.taf.core.testcase.TestCase;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jordam on 2017-04-10.
 */
class NewErrorsList {
    private List<NewError> allErrorsFromTestRunPerTestCase = new ArrayList<>();
    private List<NewError> actualSharedErrors = new ArrayList<>();
    private final List<NewError> nonSharedErrors = new ArrayList<>();
    private final List<LogPost> potentialMatchingLogPosts = new ArrayList<>(); //All encountered log posts for all new errors
    private final List<String> classesForRunTestCases;

    public NewErrorsList(List<NewError> allErrorsFromTestRunPerTestCase, List<String> classesForRunTestCases){
        this.allErrorsFromTestRunPerTestCase = allErrorsFromTestRunPerTestCase;
        this.classesForRunTestCases = classesForRunTestCases;
        fillPotentialMatchingLogPostsList();
        convertPotentialMatchingLogPostsListToSharedErrorsList();//List with one log post per NewError, and no test cases on them.
        addTestCasesToSharedErrorsList();
        mergeSharedErrorsWithExactlyTheSameTestCases();
        removeDuplicateTestCasesOnSameSharedErrors();
        moveUnSharedLogPostsToOtherList();
        sortSharedErrorsWithThoseWhoHasTheMostTestCasesFirst();
    }

    public String toString(){
        StringBuilder html = new StringBuilder();
        boolean asteriskTextShouldBePrinted = false;
        sortLogEntriesInTimeOrder(actualSharedErrors);
        html.append(commonClassOrCommonMethodForSharedErrors());
        html.append(sharedErrorsAsString());
        html.append(nonSharedErrorsAsString());
        return html.toString();
    }

    @SuppressWarnings("SpellCheckingInspection")
    private String nonSharedErrorsAsString() {
        mergeNonSharedErrorsWithExactlyTheSameTestCases();
        StringBuilder html = new StringBuilder();
        if(nonSharedErrors.size() > 0){
            html.append("              <h3 class=\"newerrorslisting\">Log extracts for failed test cases");
            if(actualSharedErrors.size() > 0){
                html.append(" with unique problems");
            }
            html.append("</h3>").append(System.lineSeparator());
            mergeNonSharedErrorsWithExactlyTheSameTestCases();
            sortLogEntriesInTimeOrder(nonSharedErrors);
            for(NewError newError : nonSharedErrors){
                html.append(newError.toHtml());
            }
        }
        return html.toString();
    }

    @SuppressWarnings("SpellCheckingInspection")
    private String sharedErrorsAsString() {
        StringBuilder html = new StringBuilder();
        boolean asteriskTextShouldBePrinted = false;
        if(actualSharedErrors.size() > 0){
            html.append("          <h3 class=\"sharedlogpostsheading\">Note: Similar log records found in multiple test cases</h3>").append(System.lineSeparator());
            for(NewError newError : actualSharedErrors){
                html.append("          <p>").append(System.lineSeparator());
                html.append("             <table class=\"knownerrorlogposts\">").append(System.lineSeparator());
                for(LogPost logPost : newError.sampleLogPosts){
                    html.append("                <tr>").append(System.lineSeparator());
                    html.append("                   <td><span class=\"errorloglevel\">").append(logPost.logLevel.toString()).append("</span></td><td><span class=\"logmessage\">").append(truncateLogMessageIfNeeded(LogPost.removeDataElements(logPost.message))).append("</span></td>").append(System.lineSeparator());
                    html.append("                </tr>").append(System.lineSeparator());
                }
                html.append("             </table>").append(System.lineSeparator());
                for(TestCase testCase : newError.testCasesWhereEncountered){
                    String link = testCase.pathToHtmlLogFile.replace("\\", "/");
                    String[] parts = link.split("/");
                    link = parts[parts.length -1];
                    if (testCase.urlToCloudResultStorage!=null) {
                        link = testCase.urlToCloudResultStorage + link;
                    }
                    html.append("                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&#9655; <span class=\"testsetname\">").append(testCase.testSetName).append("</span>: <span class=\"testcasename\">").append(testCase.testName).append("</span> (<a href=\"").append(link).append("\">Log</a>)");
                    if(testCaseHasProblemRecordsNotPartOfSharedLogRecords(testCase)){
                        html.append("<span class=\"moreerrorsasterisk\">*</span>");
                        asteriskTextShouldBePrinted = true;
                    }
                    html.append("<br>").append(System.lineSeparator());
                }
                html.append(System.lineSeparator()).append("          </p>").append(System.lineSeparator());
            }
        }
        if(asteriskTextShouldBePrinted){
            html.append("          <p><span class=\"moreerrorsasterisk\">*</span> = <i>Test case also has problematic log records not part of shared log row.</i></p>").append(System.lineSeparator());
        }
        return html.toString();
    }

    private String commonClassOrCommonMethodForSharedErrors(){
        Map<String, Integer> classNames = new HashMap<>();
        Map<String, Integer> testStepNames = new HashMap<>();
        for(NewError error : allErrorsFromTestRunPerTestCase){
            for(LogPost logPost : error.sampleLogPosts) {
                if(logPost.identifiedToBePartOfKnownError)continue;
                if(classNames.containsKey(logPost.testStepClassName)){
                    Integer classCount = classNames.get(logPost.testStepClassName);
                    classCount++;
                    classNames.put(logPost.testStepClassName, classCount);
                } else {
                    classNames.put(logPost.testStepClassName, 1);
                }
                if(testStepNames.containsKey(logPost.testStepName)){
                    Integer testStepCount = testStepNames.get(logPost.testStepName);
                    testStepCount++;
                    testStepNames.put(logPost.testStepName, testStepCount);
                } else {
                    testStepNames.put(logPost.testStepName, 1);
                }
            }
        }
        String returnString = "";
        if(classNames.size() > 0 && classNames.size() < classesForRunTestCases.size()){
            if(classNames.size() == 1){
                returnString += "Class ";
            } else {
                returnString += "Classes ";
            }
            returnString += "where errors were found: '" + String.join("', '", classNames.keySet() + "'<br>.") + System.lineSeparator();
        }
        if(testStepNames.size() == 1){
            returnString += "All errors were found in test step '" + String.join("', '", testStepNames.keySet() + "'<br>.") + System.lineSeparator();
        }
        return returnString;
    }

    private void moveUnSharedLogPostsToOtherList() {
        //noinspection Convert2MethodRef
        actualSharedErrors.stream().filter(o -> o.testCasesWhereEncountered.size() == 1).forEach(o -> nonSharedErrors.add(o));
        actualSharedErrors = actualSharedErrors.stream().filter(o -> o.testCasesWhereEncountered.size() > 1).collect(Collectors.toList());
    }

    private void sortSharedErrorsWithThoseWhoHasTheMostTestCasesFirst() {
        actualSharedErrors = actualSharedErrors
                .stream()
                .sorted(Comparator
                        .comparingInt(NewError::getNumberOfTestCases)
                        .reversed()
                ).collect(Collectors.toList());
    }

    private boolean testCaseHasProblemRecordsNotPartOfSharedLogRecords(TestCase testCase) {
        for(NewError newError : nonSharedErrors){
            if(newError.testCasesWhereEncountered.stream().anyMatch(o -> o.isSameAs(testCase))){
                return true;
            }
        }
        return false;
    }

    private String truncateLogMessageIfNeeded(String logMessage){
        if(logMessage.length() < 100)return logMessage;
        return logMessage.substring(0, 97) + "...";
    }


    private void removeDuplicateTestCasesOnSameSharedErrors() {
        for(NewError newError : actualSharedErrors){
            Set<TestCase> testCaseSet = new LinkedHashSet<>(newError.testCasesWhereEncountered);
            newError.testCasesWhereEncountered.clear();
            newError.testCasesWhereEncountered.addAll(testCaseSet);
        }
    }

    private void mergeSharedErrorsWithExactlyTheSameTestCases() {
        List<NewError> newNewErrorsList = new ArrayList<>();
        if(actualSharedErrors.size() < 2) return;
        for(int i = 0; i < actualSharedErrors.size() -1 ; i++){
            for(int j = i+1; j < actualSharedErrors.size(); j++){
                if(hasTheSameTestCases(actualSharedErrors.get(i), actualSharedErrors.get(j))){
                    List<LogPost> logPosts = new ArrayList<>();
                    logPosts.addAll(actualSharedErrors.get(i).sampleLogPosts);
                    logPosts.addAll(actualSharedErrors.get(j).sampleLogPosts);
                    actualSharedErrors.add(new NewError(actualSharedErrors.get(i).testCasesWhereEncountered, logPosts));
                    actualSharedErrors.remove(j);
                    actualSharedErrors.remove(i);
                    if(i > 0) i--;
                    if(j > 0) j--;
                }
            }
        }
    }

    private void mergeNonSharedErrorsWithExactlyTheSameTestCases() {
        if(nonSharedErrors.size() < 2) return;
        for(int i = 0; i < nonSharedErrors.size() -1 ; i++){
            for(int j = i+1; j < nonSharedErrors.size(); j++){
                if(hasTheSameTestCases(nonSharedErrors.get(i), nonSharedErrors.get(j))){
                    List<LogPost> logPosts = new ArrayList<>();
                    logPosts.addAll(nonSharedErrors.get(i).sampleLogPosts);
                    logPosts.addAll(nonSharedErrors.get(j).sampleLogPosts);
                    nonSharedErrors.add(new NewError(nonSharedErrors.get(i).testCasesWhereEncountered, logPosts));
                    nonSharedErrors.remove(j);
                    nonSharedErrors.remove(i);
                    if(i > 0) i--;
                    if(j > 0) j--;
                }
            }
        }
    }

    private void sortLogEntriesInTimeOrder(List<NewError> newErrors){
        for(NewError newError : newErrors){
            List<LogPost> tempList = new ArrayList<>();
            while(newError.sampleLogPosts.size() > 0){
                LogPost dummy = new LogPost(LogLevel.DEBUG, "", "", "", "", "");
                int index = -1;
                for(int i = 0; i < newError.sampleLogPosts.size(); i++){
                    LogPost logPost = newError.sampleLogPosts.get(i);
                    if(logPost.date.getTime()<dummy.date.getTime()) {
                        dummy = logPost;
                        index = i;
                    }
                }
                tempList.add(newError.sampleLogPosts.get(index));
                newError.sampleLogPosts.remove(index);
            }
            newError.sampleLogPosts = tempList;
        }
    }

    /*
    private void removeDuplicateSharedErrors() {
        if(actualSharedErrors.size() < 2) return;
        for(int i = 0; i < actualSharedErrors.size()-1; i++){
            boolean isDuplicate = false;
            for(int j = i+1; j < actualSharedErrors.size(); j++){
                if(actualSharedErrors.get(i).isSimilar(actualSharedErrors.get(j))) {
                    actualSharedErrors.remove(j);
                    break;
                }
            }
        }
    }
*/
    private boolean hasTheSameTestCases(NewError newError1, NewError newError2) {
        if(newError1.testCasesWhereEncountered.size() != newError2.testCasesWhereEncountered.size())return false;
        for(TestCase testCase : newError1.testCasesWhereEncountered){
            boolean thisTestCaseIsFound = false;
            for(TestCase innerTestCase : newError2.testCasesWhereEncountered){
                System.out.println(testCase.testName + ": " + testCase.uid.toString() + " - " + innerTestCase.testName + ": " + innerTestCase.uid.toString());
                if(testCase.isSameAs(innerTestCase)){
                    thisTestCaseIsFound = true;
                    break;
                }
            }
            if(!thisTestCaseIsFound){
                return false;
            }
        }
        return true;
    }

    private void addTestCasesToSharedErrorsList() {
        for(NewError newError : actualSharedErrors){
            for(LogPost registeredLogPost : newError.sampleLogPosts){
                for(NewError newErrorInfo : allErrorsFromTestRunPerTestCase){
                    for(LogPost logPost : newErrorInfo.sampleLogPosts){
                        if(logPost.isSimilar(registeredLogPost)){
                            newError.testCasesWhereEncountered.addAll(newErrorInfo.testCasesWhereEncountered);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("SimplifyStreamApiCallChains")
    private void convertPotentialMatchingLogPostsListToSharedErrorsList() {
        potentialMatchingLogPosts.stream()
                .forEach(o -> actualSharedErrors.add(new NewError(o)));
    }

    private void fillPotentialMatchingLogPostsList(){
        for(NewError newErrorInfo : allErrorsFromTestRunPerTestCase){
            for(LogPost newErrorInfoPost : newErrorInfo.sampleLogPosts){
                if(newErrorInfoPost.identifiedToBePartOfKnownError)continue;
                boolean logPostRegisteredAlready = false;
                for(LogPost logPost : potentialMatchingLogPosts){
                    if(logPost.isSimilar(newErrorInfoPost)){
                        logPostRegisteredAlready = true;
                        break;
                    }
                }
                if(!logPostRegisteredAlready){
                    potentialMatchingLogPosts.add(newErrorInfoPost);
                }
            }
        }
    }

}
