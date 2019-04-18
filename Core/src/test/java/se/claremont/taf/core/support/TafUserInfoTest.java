package se.claremont.taf.core.support;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.support.api.Taf;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Tests for UserInfo class
 *
 * Created by magnusolsson on 2016-12-20.
 */
public class TafUserInfoTest extends UnitTestClass{

    @Test
    public void getInstance(){
        Assert.assertTrue( Taf.tafUserInfon() != null );
    }

    @Test
    public void fetchAllTafUserInfoData()
    {
        //ArrayList<String> tafUserInfoData = new ArrayList<String>();
        Assert.assertTrue( !Taf.tafUserInfon().getCanonicalHostName().equalsIgnoreCase("") );
        Assert.assertTrue( !Taf.tafUserInfon().getHostAdress().equalsIgnoreCase("") );
        Assert.assertTrue( !Taf.tafUserInfon().getHostName().equalsIgnoreCase("") );
        Assert.assertTrue( !Taf.tafUserInfon().getOperatingSystemArchitecture().equalsIgnoreCase("") );
        Assert.assertTrue( !Taf.tafUserInfon().getOperatingSystemName().equalsIgnoreCase("") );
        Assert.assertTrue( !Taf.tafUserInfon().getOperatingSystemVersion() .equalsIgnoreCase("") );
        Assert.assertTrue( !Taf.tafUserInfon().getJavaHome().equalsIgnoreCase("") );
        Assert.assertTrue( !Taf.tafUserInfon().getJavaVersion().equalsIgnoreCase("") );
        Assert.assertTrue( !Taf.tafUserInfon().getUserAccountName().equalsIgnoreCase("") );
        Assert.assertTrue( !Taf.tafUserInfon().getUserHomeDirectory().equalsIgnoreCase("") );
        Assert.assertTrue( !Taf.tafUserInfon().getUserWorkingDirectory().equalsIgnoreCase("") );

    }

}
