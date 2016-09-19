package se.claremont.autotest.common;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the Settings class
 *
 * Created by jordam on 2016-09-18.
 */
public class Settings_Tests {

     @Test
    public void loadDefaults(){
         Settings settings = new Settings();
         Assert.assertTrue(settings.getValueForProperty("baseLogFolder").equals("C:\\Temp\\"));
     }

    @Test
    public void add(){
        Settings settings = new Settings();
        settings.setValueForProperty("dummy", "dummyvalue");
        Assert.assertTrue(settings.getValueForProperty("dummy").equals("dummyvalue"));
    }

    @Test
    public void update(){
        Settings settings = new Settings();
        settings.setValueForProperty("dummy", "dummyvalue");
        settings.setValueForProperty("dummy", "dummyvalue2");
        Assert.assertTrue(settings.getValueForProperty("dummy").equals("dummyvalue2"));
    }

    @Test
    public void print(){
        Settings settings = new Settings();
        settings.setValueForProperty("dummy", "dummyvalue");
        Assert.assertTrue(settings.toString().contains("dummyvalue"));
    }
}
