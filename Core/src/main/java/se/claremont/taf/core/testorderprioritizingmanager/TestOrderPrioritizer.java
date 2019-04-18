package se.claremont.taf.core.testorderprioritizingmanager;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class attempts to re-order test cases planned for execution so that the most relevant test cases
 * are executed first. This could be essential when the test execution suites get longer and maybe test
 * execution has to be abandoned at a certain point in time. Then it's good to execute test cases in
 * order of relevance.
 */
//Todo: Fininsh and write tests - also check performance
public class TestOrderPrioritizer {

    TestCaseResults testCaseResultsList;
    private PrioritizedTestCaseResults prioritizedTestCaseResults;
    private String fileName = "TestCaseExecutionHistory.json";
    private int timeSinceLastExecutionWeight = 40;
    private int errorInRecentHistoryWeight = 40;
    private Date now = new Date();
    private String dateToSetAsLastExecutionDateForNonExecutedTests = "2000-01-01 00:00:00";

    public TestOrderPrioritizer(){
        testCaseResultsList = new TestCaseResults();
        prioritizedTestCaseResults = new PrioritizedTestCaseResults();
        populateHistoricExecutionRecords();
    }

    public static void saveTestResults(String testName, TestOutcome testOutcome){

    }

    public List<String> getRecomendedExecutionOrder(){
        prioritize();
        List<String> testCaseExecutionOrder = new ArrayList<>();
        for(PrioritizedTestCaseResult prioritizedTestCaseResult : prioritizedTestCaseResults){
            testCaseExecutionOrder.add(prioritizedTestCaseResult.testCaseResults.get(0).testName);
        }
        return testCaseExecutionOrder;
    }

    private void prioritize(){
        sortTestResults();
        scoreTestResults();
        sortTestCasesInRecommendedExecutionOrder();
    }

    private void sortTestCasesInRecommendedExecutionOrder() {
        prioritizedTestCaseResults.sort(Comparator.comparing(PrioritizedTestCaseResult::getScore));
        Collections.reverse(prioritizedTestCaseResults);
    }

    private void scoreTestResults() {
        for(PrioritizedTestCaseResult prioritizedTestCaseResult : prioritizedTestCaseResults){
            scoreForTimeSinceLastExecution(prioritizedTestCaseResult);
            scoreForErrorHistory(prioritizedTestCaseResult);
        }
    }

    private void scoreForErrorHistory(PrioritizedTestCaseResult prioritizedTestCaseResult) {
        if(prioritizedTestCaseResult.testCaseResults.size() > 0 &&
                prioritizedTestCaseResult.testCaseResults.get(prioritizedTestCaseResult.testCaseResults.size()-1).testOutcome == TestOutcome.FAIL){
            prioritizedTestCaseResult.prioritazionScore += 100000 * errorInRecentHistoryWeight;
        }
        if(prioritizedTestCaseResult.testCaseResults.size() > 0 &&
                prioritizedTestCaseResult.testCaseResults.get(prioritizedTestCaseResult.testCaseResults.size()-1).testOutcome == TestOutcome.KNOWN_ERROR){
            prioritizedTestCaseResult.prioritazionScore += 50000 * errorInRecentHistoryWeight;
        }
        if(prioritizedTestCaseResult.testCaseResults.size() > 0 &&
                prioritizedTestCaseResult.testCaseResults.get(prioritizedTestCaseResult.testCaseResults.size()-1).testOutcome == TestOutcome.NO_RUN){
            prioritizedTestCaseResult.prioritazionScore += 10000 * errorInRecentHistoryWeight;
        }
        for(TestCaseResult testCaseResult : prioritizedTestCaseResult.testCaseResults){
        }
    }

    private void scoreForTimeSinceLastExecution(PrioritizedTestCaseResult prioritizedTestCaseResult) {
        Date lastExecution = null;
        for(TestCaseResult testCaseResult : prioritizedTestCaseResult.testCaseResults){
            if(lastExecution == null) {
                lastExecution = testCaseResult.executionTime;
                continue;
            }
            if(testCaseResult.executionTime != null && testCaseResult.executionTime.after(lastExecution)){
                lastExecution = testCaseResult.executionTime;
            }
        }
        if(lastExecution == null) { // No registered run for the test case
            prioritizedTestCaseResult.prioritazionScore += 100000 * timeSinceLastExecutionWeight;
            return;
        }
        prioritizedTestCaseResult.setScore((int) (prioritizedTestCaseResult.prioritazionScore + timeSinceLastExecutionWeight*(now.getTime()/1000 - lastExecution.getTime()/1000)));
    }

    private void sortTestResults(){
        for(TestCaseResult testCaseResult : testCaseResultsList){
            boolean duplicate = false;
            for(PrioritizedTestCaseResult prioritizedTestCaseResult : prioritizedTestCaseResults){
                if(prioritizedTestCaseResult.testCaseResults.size() != 0 && prioritizedTestCaseResult.testCaseResults.get(0).testName.equals(testCaseResult.testName)){
                    prioritizedTestCaseResult.testCaseResults.add(testCaseResult);
                    duplicate = true;
                    break;
                }
            }
            if(!duplicate){
                prioritizedTestCaseResults.add(new PrioritizedTestCaseResult(testCaseResult));
            }
        }
        for(PrioritizedTestCaseResult prioritizedTestCaseResult : prioritizedTestCaseResults){
            Collections.sort(prioritizedTestCaseResult.testCaseResults, Comparator.comparing(TestCaseResult::getDate));
        }
    }

    private void populateHistoricExecutionRecords() {
        if(!Files.exists(Paths.get(fileName))) return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            testCaseResultsList = mapper.readValue(new File(fileName), TestCaseResults.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class PrioritizedTestCaseResults extends ArrayList<PrioritizedTestCaseResult>{}

    class PrioritizedTestCaseResult {
        ArrayList<TestCaseResult> testCaseResults = new ArrayList<>();
        int prioritazionScore = 0;

        public PrioritizedTestCaseResult(){}

        public int getScore(){
            return prioritazionScore;
        }

        public PrioritizedTestCaseResult(TestCaseResult testCaseResult){
            addPrioritizedTestCaseResult(testCaseResult);
        }

        public void addPrioritizedTestCaseResult(TestCaseResult testCaseResult) {
            testCaseResults.add(testCaseResult);
        }

        public void setScore(int score){
            prioritazionScore = score;
        }
    }

}
