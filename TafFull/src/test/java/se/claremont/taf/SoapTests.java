package se.claremont.taf;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.testcase.TestCase;
import se.claremont.taf.soapsupport.SoapInteraction;

public class SoapTests {

    @Test
    public void accessOfSoapFunctionality(){
        SoapInteraction soap = new SoapInteraction(new TestCase(), "se.claremont.test");
        Assert.assertNotNull(soap);
    }
}
