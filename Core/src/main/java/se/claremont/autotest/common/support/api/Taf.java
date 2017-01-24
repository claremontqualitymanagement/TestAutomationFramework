package se.claremont.autotest.common.support.api;

import se.claremont.autotest.common.scp.Scp;
import se.claremont.autotest.common.scp.ScpImpl;
import se.claremont.autotest.common.support.TafUserInfo;
import se.claremont.autotest.common.support.TafUserInfoImpl;

/**
 * Created by magnusolsson on 2016-12-05.
 *
 * Main entry point for TAF api
 *
 */
public class Taf {

    /**
     * Scp API
     * @return Scp object
     */
    public static Scp scp() {
        return ScpImpl.getInstance();
    }

    /**
     *
     * @return TafUserInfo object
     */
    public static TafUserInfo tafUserInfon(){
        return TafUserInfoImpl.getInstance();
    }

}