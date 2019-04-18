package se.claremont.taf.core.support.api;

import se.claremont.taf.core.scp.Scp;
import se.claremont.taf.core.scp.ScpImpl;
import se.claremont.taf.core.support.TafUserInfo;
import se.claremont.taf.core.support.TafUserInfoImpl;

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