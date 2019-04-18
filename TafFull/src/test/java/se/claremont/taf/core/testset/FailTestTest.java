package se.claremont.taf.core.testset;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test class used for testing test runs
 *
 * Created by jordam on 2017-03-17.
 */
public class FailTestTest extends UnitTestClass{

    @Test
    @Ignore
    public void fail1(){
        System.out.println("This is some text related to the test output.");
        System.out.println("This is some text related to the test output, second row.");
        Assert.assertTrue(false);
    }

    @Test
    public void pass1(){
        System.out.println("This is some text related to the test output.");
        System.out.println("This is some text related to the test output, second row.");
        Assert.assertTrue(true);
    }

    @Test
    @Ignore
    public void failWithMessage1(){
        System.out.println("This is some text related to the test output.");
        System.out.println("This is some text related to the test output, second row.");
        Assert.assertTrue("Gone wrong", false);
    }

    @Test
    public void passWithMessage1(){
        System.out.println("This is some text related to the test output.");
        System.out.println("This is some text related to the test output, second row.");
        Assert.assertTrue("Gone right", true);
    }

    @Test
    @Ignore
    public void failWithThrowsError() throws Exception {
        System.out.println("This is some text related to the test output.");
        System.out.println("This is some text related to the test output, second row.");
        throw new Exception("Oups. Bad. Throwing exception for testing purposes");
    }

    @Test
    public void vanillaFlavourPassing(){
        System.out.println("This is some text related to the test output.");
        System.out.println("This is some text related to the test output, second row.");
        Assert.assertTrue(true);
    }

    @Test
    @Ignore
    public void assumeFailed(){
        System.out.println("This is some text related to the test output.");
        System.out.println("This is some text related to the test output, second row.");
        Assume.assumeTrue("This test does not have correct pre-requisites.", false);
    }
}
