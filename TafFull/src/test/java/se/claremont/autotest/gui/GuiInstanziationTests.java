package se.claremont.autotest.gui;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import se.claremont.autotest.TAF;
import se.claremont.autotest.javasupport.applicationundertest.ApplicationUnderTest;

import java.awt.*;

public class GuiInstanziationTests {

    @Test
    public void startNoArguments(){
        Assume.assumeTrue("Desktop not supported. Cannot execute test.", Desktop.isDesktopSupported());
        int numberOfWindows = ApplicationUnderTest.getWindows().size();
        TAF.main(null);
        Assert.assertTrue(ApplicationUnderTest.getWindows().size() == numberOfWindows + 1);
    }
}
