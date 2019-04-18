package se.claremont.taf.core.support;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Tests to assess Utils class in TAF Core
 *
 * Created by magnusolsson on 2016-09-23.
 */
public class UtilsTest extends UnitTestClass{

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

    @Test
    public void checkSupportedJavaVersionForTAF()
    {
        Assert.assertTrue( Utils.getInstance().checkSupportedJavaVersionForTAF() );
    }

}