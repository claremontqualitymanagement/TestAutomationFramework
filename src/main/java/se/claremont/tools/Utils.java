package se.claremont.tools;

import java.io.File;

/**
 * Created by magnusolsson on 2016-09-21.
 */
public class Utils {

    /**
     *
     * @return os for running jvm
     */
    public String getOS() {
        return System.getProperty("os.name");
    }

    /**
     *
     * @return gets the separator for the default filesystem
     */
    public String FileSeparator() {
        return System.getProperty("file.separator");
    }

}
