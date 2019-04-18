package se.claremont.taf.core.logging;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.testcase.TestCaseLog;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Tests for the LogPost class
 *
 * Created by jordam on 2016-08-31.
 */
public class LogPost_Tests extends UnitTestClass{

    @Test
    public void instantiationPureText(){
        LogPost logPost = new LogPost(LogLevel.EXECUTED, "Executed stuff.", null, "", "", "");
        Assert.assertNotNull(logPost);
    }

    @Test
    public void registerAllLogLevelsAndCheckToString(){
        for(LogLevel logLevel : LogLevel.values()){
            LogPost logPost = new LogPost(logLevel, "Test1", null, "", "", "");
            Assert.assertTrue(logPost.toString().toLowerCase().replace("_", " ").contains(logLevel.toString().toLowerCase().replace("_", " ")));
            Assert.assertTrue(logPost.toString().contains("Test1"));
        }
    }

    @Test
    public void isFailedTest(){
        TestCaseLog testCaseLog = new TestCaseLog("isFailedTest");
        for(LogLevel logLevel : LogLevel.values()){
            LogPost logPost = new LogPost(logLevel, "Test1", null, null, null, null);
            if(logPost.isFail()){
                testCaseLog.logPosts.add(logPost);
            }
        }
        Assert.assertTrue(testCaseLog.logPosts.size() == 4);
    }

    @Test
    public void isSimilarTestPassed(){
        LogPost logPost1 = new LogPost(LogLevel.EXECUTED, "Hej '123' då.", "", "", "", "");
        LogPost logPost2 = new LogPost(LogLevel.EXECUTED, "Hej '321' då.", "", "", "", "");
        Assert.assertTrue(logPost1.isSimilar(logPost2));
    }

    @Test
    public void isSimilarTestFailed1(){
        LogPost logPost1 = new LogPost(LogLevel.EXECUTED, "Hej '123' då.", "", "", "", "");
        LogPost logPost2 = new LogPost(LogLevel.INFO, "Hej '321' då.", "", "", "", "");
        Assert.assertFalse(logPost1.isSimilar(logPost2));
    }

    @Test
    public void isSimilarTestFailed2(){
        LogPost logPost1 = new LogPost(LogLevel.EXECUTED, "He1 '123' då.", "", "", "", "");
        LogPost logPost2 = new LogPost(LogLevel.EXECUTED, "Hej '321' då.", "", "", "", "");
        Assert.assertFalse(logPost1.isSimilar(logPost2));
    }

    @Test
    public void isSimilarTestFailed3(){
        LogPost logPost1 = new LogPost(LogLevel.EXECUTED, "Hej '123' då.", "", "", "", "");
        LogPost logPost2 = new LogPost(LogLevel.EXECUTED, "Hej '321' du.", "", "", "", "");
        Assert.assertFalse(logPost1.isSimilar(logPost2));
    }

    @Test
    public void isSimilarTestFailed4(){
        LogPost logPost1 = new LogPost(LogLevel.EXECUTED, "Hej '123' nej.", "", "", "", "");
        LogPost logPost2 = new LogPost(LogLevel.EXECUTED, "Hej '321' nja.", "", "", "", "");
        Assert.assertFalse(logPost1.isSimilar(logPost2));
    }

    @Test
    public void isSimilarTestFailed5(){
        LogPost logPost1 = new LogPost(LogLevel.EXECUTED, "'Hej' 123 då.", "", "", "", "");
        LogPost logPost2 = new LogPost(LogLevel.EXECUTED, "'He1' 123 då.", "", "", "", "");
        Assert.assertTrue(logPost1.isSimilar(logPost2));
    }

    @Test
    public void removeDataElementsTestInitialDataElement(){
        Assert.assertTrue("Expected the washed string to be ''...' text1 '...' text2' but it was '" + LogPost.removeDataElements("'data0' text1 'data1' text2") + "'.", LogPost.removeDataElements("'data0' text1 'data1' text2").equals("'...' text1 '...' text2"));
    }

    @Test
    public void removeDataElementsTestNoDataElement(){
        Assert.assertTrue("Expected the washed string to be 'text1' but it was '" + LogPost.removeDataElements("text1") + "'.", LogPost.removeDataElements("text1").equals("text1"));
    }

    @Test
    public void removeDataElementsTestEndingDataElement(){
        Assert.assertTrue("Expected the washed string to be 'text1 '...'' but it was '" + LogPost.removeDataElements("text1 'data1'") + "'.", LogPost.removeDataElements("text1 'data1'").equals("text1 '...'"));
    }

    @Test
    public void removeDataElementsTestLogPostDataElementEmbeddedInTheString(){
        Assert.assertTrue("Expected the washed string to be 'text1 '...' text2' but it was '" + LogPost.removeDataElements("text1 'data1' text2") + "'.", LogPost.removeDataElements("text1 'data1' text2").equals("text1 '...' text2"));
    }

    @Test
    public void isSimilarStringComparisonInitialDataElement(){
        Assert.assertTrue(LogPost.isSimilar("'data0' text1 'data1' text2", "'datab' text1 'data2' text2"));
    }

    @Test
    public void isSimilarStringComparisonSuccessiveDataElement(){
        Assert.assertTrue(LogPost.isSimilar("text1 'data1' text2", "text1 'data2' text2"));
    }
}
