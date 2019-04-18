package se.claremont.taf.support.api;

import se.claremont.taf.scp.Scp;
import se.claremont.taf.scp.ScpImpl;
import se.claremont.taf.support.TafUserInfo;
import se.claremont.taf.support.TafUserInfoImpl;

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