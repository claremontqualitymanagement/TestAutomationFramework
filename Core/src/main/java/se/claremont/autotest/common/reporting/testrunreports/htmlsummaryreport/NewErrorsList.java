package se.claremont.autotest.common.reporting.testrunreports.htmlsummaryreport;

import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.testcase.TestCase;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jordam on 2017-04-10.
 */
public class NewErrorsList {
    List<PotentiallySharedError> allErrorsFromTestRunPerTestCase = new ArrayList<>();
    List<PotentiallySharedError> actualSharedErrors = new ArrayList<>();
    List<PotentiallySharedError> nonSharedErrors = new ArrayList<>();
    List<LogPost> potentialMatchingLogPosts = new ArrayList<>(); //All encountered log posts for all new errors

    public NewErrorsList(List<PotentiallySharedError> allErrorsFromTestRunPerTestCase){
        this.allErrorsFromTestRunPerTestCase = allErrorsFromTestRunPerTestCase;
        fillPotentialMatchingLogPostsList();
        convertPotentialMatchingLogPostsListToSharedErrorsList();//List with one log post per PotentiallySharedError, and no test cases on them.
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
        if(actualSharedErrors.size() > 0){
            html.append("          <h3 class=\"sharedlogpostsheading\">Note: Similar log records found in multiple test cases</h3>").append(System.lineSeparator());
            for(PotentiallySharedError potentiallySharedError : actualSharedErrors){
                html.append("          <p>").append(System.lineSeparator());
                for(LogPost logPost : potentiallySharedError.sampleLogPosts){
                    html.append("              ").append(logPost.logLevel.toString()).append(": ").append(truncateLogMessageIfNeeded(LogPost.removeDataElements(logPost.message))).append("<br>").append(System.lineSeparator());
                }
                for(TestCase testCase : potentiallySharedError.testCasesWhereEncountered){
                    html.append("                  &#9659; ").append(testCase.testSetName).append(": ").append(testCase.testName).append(" (<a href=\"" + testCase.pathToHtmlLog + "\">Log</a>)");
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
            html.append("          <p><span class=\"moreerrorsasterisk\">*</span> = <i>Test case has problematic log records not part of shared log row.</i></p>").append(System.lineSeparator());
        }
        if(nonSharedErrors.size() > 0){
            html.append("              <h3 class=\"newerrorslisting\">Log extracts for test cases with unique problems</h3>").append(System.lineSeparator());
            mergeNonSharedErrorsWithExactlyTheSameTestCases();
            sortLogEntriesInTimeOrder(nonSharedErrors);
            for(PotentiallySharedError potentiallySharedError : nonSharedErrors){
                html.append(potentiallySharedError.toHtml());
            }
        }
        return html.toString();
    }

    private void moveUnSharedLogPostsToOtherList() {
        actualSharedErrors.stream().filter(o -> o.testCasesWhereEncountered.size() == 1).forEach(o -> nonSharedErrors.add(o));
        actualSharedErrors = actualSharedErrors.stream().filter(o -> o.testCasesWhereEncountered.size() > 1).collect(Collectors.toList());
    }

    private void sortSharedErrorsWithThoseWhoHasTheMostTestCasesFirst() {
        actualSharedErrors.stream().sorted(Comparator.comparingInt(PotentiallySharedError::getNumberOfTestCases).reversed());
    }

    private boolean testCaseHasProblemRecordsNotPartOfSharedLogRecords(TestCase testCase) {
        for(PotentiallySharedError potentiallySharedError : nonSharedErrors){
            if(potentiallySharedError.testCasesWhereEncountered.stream().anyMatch(o -> o.isSameAs(testCase))){
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
        for(PotentiallySharedError potentiallySharedError : actualSharedErrors){
            Set<TestCase> testCaseSet = new LinkedHashSet<>(potentiallySharedError.testCasesWhereEncountered);
            potentiallySharedError.testCasesWhereEncountered.clear();
            potentiallySharedError.testCasesWhereEncountered.addAll(testCaseSet);
        }
    }

    private void mergeSharedErrorsWithExactlyTheSameTestCases() {
        List<PotentiallySharedError> newPotentiallySharedErrorsList = new ArrayList<>();
        if(actualSharedErrors.size() < 2) return;
        for(int i = 0; i < actualSharedErrors.size() -1 ; i++){
            for(int j = i+1; j < actualSharedErrors.size(); j++){
                if(hasTheSameTestCases(actualSharedErrors.get(i), actualSharedErrors.get(j))){
                    List<LogPost> logPosts = new ArrayList<>();
                    logPosts.addAll(actualSharedErrors.get(i).sampleLogPosts);
                    logPosts.addAll(actualSharedErrors.get(j).sampleLogPosts);
                    actualSharedErrors.add(new PotentiallySharedError(actualSharedErrors.get(i).testCasesWhereEncountered, logPosts));
                    actualSharedErrors.remove(j);
                    actualSharedErrors.remove(i);
                    if(j > 0) j--;
                    if(i > 0) i--;
                }
            }
        }
    }

    private void mergeNonSharedErrorsWithExactlyTheSameTestCases() {
        List<PotentiallySharedError> newPotentiallySharedErrorsList = new ArrayList<>();
        if(nonSharedErrors.size() < 2) return;
        for(int i = 0; i < nonSharedErrors.size() -1 ; i++){
            for(int j = i+1; j < nonSharedErrors.size(); j++){
                if(hasTheSameTestCases(nonSharedErrors.get(i), nonSharedErrors.get(j))){
                    List<LogPost> logPosts = new ArrayList<>();
                    logPosts.addAll(nonSharedErrors.get(i).sampleLogPosts);
                    logPosts.addAll(nonSharedErrors.get(j).sampleLogPosts);
                    newPotentiallySharedErrorsList.add(new PotentiallySharedError(nonSharedErrors.get(i).testCasesWhereEncountered, logPosts));
                    nonSharedErrors.remove(j);
                    nonSharedErrors.remove(i);
                }
            }
        }
        nonSharedErrors.addAll(newPotentiallySharedErrorsList);
    }

    private void sortLogEntriesInTimeOrder(List<PotentiallySharedError> potentiallySharedErrors){
        for(PotentiallySharedError potentiallySharedError : potentiallySharedErrors){
            List<LogPost> tempList = new ArrayList<>();
            while(potentiallySharedError.sampleLogPosts.size() > 0){
                LogPost dummy = new LogPost(LogLevel.DEBUG, "");
                int index = -1;
                for(int i = 0; i < potentiallySharedError.sampleLogPosts.size(); i++){
                    LogPost logPost = potentiallySharedError.sampleLogPosts.get(i);
                    if(logPost.date.getTime()<dummy.date.getTime()) {
                        dummy = logPost;
                        index = i;
                    }
                }
                tempList.add(potentiallySharedError.sampleLogPosts.get(index));
                potentiallySharedError.sampleLogPosts.remove(index);
            }
            potentiallySharedError.sampleLogPosts = tempList;
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
    private boolean hasTheSameTestCases(PotentiallySharedError potentiallySharedError1, PotentiallySharedError potentiallySharedError2) {
        if(potentiallySharedError1.testCasesWhereEncountered.size() != potentiallySharedError2.testCasesWhereEncountered.size())return false;
        for(TestCase testCase : potentiallySharedError1.testCasesWhereEncountered){
            boolean thisTestCaseIsFound = false;
            for(TestCase innerTestCase : potentiallySharedError2.testCasesWhereEncountered){
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
        for(PotentiallySharedError potentiallySharedError : actualSharedErrors){
            for(LogPost registeredLogPost : potentiallySharedError.sampleLogPosts){
                for(PotentiallySharedError newErrorInfo : allErrorsFromTestRunPerTestCase){
                    for(LogPost logPost : newErrorInfo.sampleLogPosts){
                        if(logPost.isSimilar(registeredLogPost)){
                            potentiallySharedError.testCasesWhereEncountered.addAll(newErrorInfo.testCasesWhereEncountered);
                        }
                    }
                }
            }
        }
    }

    private void convertPotentialMatchingLogPostsListToSharedErrorsList() {
        potentialMatchingLogPosts.stream()
                .forEach(o -> actualSharedErrors.add(new PotentiallySharedError(o)));
    }

    private void fillPotentialMatchingLogPostsList(){
        for(PotentiallySharedError newErrorInfo : allErrorsFromTestRunPerTestCase){
            for(LogPost newErrorInfoPost : newErrorInfo.sampleLogPosts){
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
