package se.claremont.autotest;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.soapsupport.SoapInteraction;

public class SoapTests {

    @Test
    public void accessOfSoapFunctionality(){
        SoapInteraction soap = new SoapInteraction(new TestCase(), "se.claremont.test");
        Assert.assertNotNull(soap);
    }
}
