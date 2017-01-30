package se.claremont.autotest.common.support;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.logging.LogFolder;
import se.claremont.autotest.common.support.SupportMethods;

import java.io.File;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * SecondSet_Test for methods in the SupportMethods class
 *
 * Created by jordam on 2016-08-31.
 */
public class SupportMethodsTest {

    @Test
    public void isRegexMatchTest(){
        Assert.assertTrue(SupportMethods.isRegexMatch("abc123", "abc.*3"));
    }

    @Test
    public void testFileWriteToReportFolder(){
        LogFolder.setLogFolder(this.getClass().getName());
        try{
            int i = 0;
            File f = new File(LogFolder.testRunLogFolder + "dummyFile" + i + ".txt");
            boolean fileExist = f.exists() && !f.isDirectory();
            while(fileExist){
                i++;
                f = new File(LogFolder.testRunLogFolder + "dummyFile" + i + ".txt");
                fileExist = f.exists() && !f.isDirectory();
            }

            SupportMethods.saveToFile("Dummy text string", LogFolder.testRunLogFolder + "dummyFile" + i + ".txt");
            f = new File(LogFolder.testRunLogFolder + "dummyFile" + i + ".txt");
            fileExist = f.exists() && !f.isDirectory();
            Assert.assertTrue(fileExist);
            //noinspection ResultOfMethodCallIgnored
            f.delete();
        }catch (Exception e){
            Assert.fail("Could not save to disk at '" + LogFolder.testRunLogFolder + "'");
        }

    }

    @Test
    public void test_stringToDate_8Chars() {
        Date date = SupportMethods.stringToDate("20160101");

        assertNotNull(date);
        assertEquals("Fri Jan 01 00:00:00 CET 2016", date.toString());
    }
    @Test
    public void test_stringToDate_6Chars() {
        Date date = SupportMethods.stringToDate("160101");

        assertNotNull(date);
        assertEquals("Fri Jan 01 00:00:00 CET 2016", date.toString());
    }

}
