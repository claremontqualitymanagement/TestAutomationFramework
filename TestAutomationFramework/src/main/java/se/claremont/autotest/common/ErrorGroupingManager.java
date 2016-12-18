package se.claremont.autotest.common;

import java.util.ArrayList;

/**
 * Must be run after evaluation of test case log towards known errors has been run
 *
 * Created by jordam on 2016-09-21.
 */
class ErrorGroupingManager {
    private ArrayList<Error> errors = new ArrayList<>();

    /**
     * Must be run after evaluation of test case log towards known errors has been run
     *
     * @param testCase The test case to evaluate
     */
    void evaluateTestCase(TestCase testCase){
        for(LogPost logPost : testCase.testCaseLog.logPosts){
            if(logPost.isFail() && !logPost.identifiedToBePartOfKnownError){
                boolean logPostInErrorsListAready = false;
                for(Error error : errors){
                    if(error.containsSimilarLogPost(logPost)){
                        error.addTestCase(testCase);
                        logPostInErrorsListAready = true;
                        break;
                    }
                }
                if(!logPostInErrorsListAready){
                    errors.add(new Error(logPost, testCase));
                }
            }
        }
    }

    private void groupErrorLogPosts(){
        for(int i = 0; i < errors.size()-1; i++){
            for(int j = i+1; j > errors.size(); j++){
                boolean allTestCasesAreSame = true;
                for(TestCase testCase : errors.get(i).testCases){
                    for(TestCase tc : errors.get(j).testCases){
                        if(!testCase.isSameAs(tc)){
                            allTestCasesAreSame = false;
                            break;
                        }
                    }
                }

            }
        }
    }

    private class Error{
        ArrayList<LogPost>  logPosts  = new ArrayList<>();
        ArrayList<TestCase> testCases = new ArrayList<>();


        public Error(LogPost logPost, TestCase testCase){
            boolean logPostInSetAlready = false;
            for(LogPost lp : logPosts){
                if(lp.isSimilar(logPost)){
                    logPostInSetAlready = true;
                    break;
                }
            }
            if(!logPostInSetAlready){
                this.logPosts.add(logPost);
            }
            boolean testCaseInSetAlready = false;
            for(TestCase tc : testCases){
                if(tc.isSameAs(testCase)){
                    testCaseInSetAlready = true;
                    break;
                }
            }
            if(!testCaseInSetAlready){
                this.testCases.add(testCase);
            }
        }

        public void addTestCase(TestCase testCase){
            this.testCases.add(testCase);
        }

        public boolean containsSimilarLogPost(LogPost logPost){
            for (LogPost lp : logPosts){
                if(lp.isSimilar(logPost)){
                    return true;
                }
            }
            return false;
        }
    }

}
