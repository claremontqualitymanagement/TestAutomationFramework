package se.claremont.autotest.common.reporting.testrunreports;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.common.reporting.testrunreports.BaseFolderHtmlIndexFile;
import se.claremont.autotest.common.testset.UnitTestClass;

/**
 * Tests to assess the base folder index.html file
 *
 * Created by jordam on 2017-01-05.
 */
public class BaseFolderHtmlIndexFileTest extends UnitTestClass{

    @Ignore
    @Test
    public void fileCreationTest(){
        BaseFolderHtmlIndexFile b = new BaseFolderHtmlIndexFile();
        Assert.assertTrue(true);
    }
}
