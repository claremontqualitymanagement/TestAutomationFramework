package se.claremont.taf.api;

import se.claremont.autotest.common.scp.Scp;
import se.claremont.autotest.common.scp.ScpImpl;

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

}
