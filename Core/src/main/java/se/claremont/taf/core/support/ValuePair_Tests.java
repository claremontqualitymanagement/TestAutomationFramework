package se.claremont.taf.core.support;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Tests for the ValuePair class
 *
 * Created by jordam on 2016-09-18.
 */
public class ValuePair_Tests extends UnitTestClass{

    @Test
    public void createAndRead(){
        ValuePair valuePair = new ValuePair("Name", "Value");
        Assert.assertTrue(valuePair.parameter.equals("Name"));
        Assert.assertTrue(valuePair.value.equals("Value"));
    }

    @Test
    public void update(){
        ValuePair valuePair = new ValuePair("Name", "Value");
        valuePair.reassign("Value2");
        Assert.assertTrue(valuePair.value.equals("Value2"));
    }

    @SuppressWarnings("UnusedAssignment")
    @Test
    public void nulls(){
        try {
            ValuePair valuePair = new ValuePair(null, null);
        }catch (Exception e){
            //noinspection ConstantConditions
            Assert.assertTrue("Cannot instantiate ValuePair with null values", false);
        }
    }

    @Test
    public void integers(){
        ValuePair valuePair = new ValuePair("Parameter name", 123);
        Assert.assertTrue(valuePair.value.equals("123"));
    }

    @Test
    public void toStringTestInt(){
        ValuePair valuePair = new ValuePair("Parameter name", 123);
        Assert.assertTrue(valuePair.toString().equals("['Parameter name' = '123']"));
    }

    @Test
    public void toStringTestString(){
        ValuePair valuePair = new ValuePair("Parameter name", "123");
        Assert.assertTrue(valuePair.toString().equals("['Parameter name' = '123']"));
    }
}
