package se.claremont.tools;

import org.junit.*;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.guidriverpluginstructure.websupport.DomElement;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebInteractionMethods;
import se.claremont.autotest.support.PerformanceTimer;

import java.io.File;

/**
 * Created by magnusolsson on 2016-09-23.
 */
public class UtilsTest{

    @Test
    public void getInstance(){
        Assert.assertTrue( Utils.getInstance() != null );
    }

    @Test
    public void getOS() {
        Assert.assertTrue( !Utils.getInstance().getOS().equalsIgnoreCase("") );
        Assert.assertTrue( Utils.getInstance().amIMacOS() ||
                Utils.getInstance().getOS().toLowerCase().contains("linux") ||
                Utils.getInstance().amIWindowsOS() );
    }

}