package se.claremont.tools;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by magnusolsson on 2016-09-21.
 *
 * Singelton class for Utils stuff.
 *
 */
public class Utils {

    final Logger logger = LoggerFactory.getLogger( Utils.class );

    private static Utils instance = null;

    protected Utils() {

    }

    public static Utils getInstance() {
        if( instance == null)
            instance = new Utils();
        return instance;
    }

    /**
     *
     * @return os for running jvm
     */
    public String getOS() {
        String os = System.getProperty("os.name");
        logger.debug("JVM is running on OS: {}.", os);
        return os;
    }

    /**
     *
     * @return gets the separator for the default filesystem
     */
    public String FileSeparator() {
        return System.getProperty("file.separator");
    }

    /**
     * @return User home directory path
     */
    public String getUserHomeDirectory() {
        return System.getProperty("user.home");
    }

    /**
     * @return User working directory path
     */
    public String getUserWorkingDirectory() {
        return System.getProperty("user.dir");
    }

    /**
     *
     * @return true if jvm is running on Mac OS X, otherwise false
     */
    public boolean amIMacOS() {
        return getOS().toLowerCase().contains("mac");
    }

    /**
     *
     * @return root directory
     */
    public String getRootDirectory() {
        return File.listRoots()[0].getAbsolutePath();
    }

    /**
     * Checks if pathToFile exists and is a file
     * @param pathToFile The path to the file to check existance of.
     * @return true if file path exists and is file
     */
    public boolean doesFileExists(String pathToFile) {

        try {
            File f = new File( pathToFile );
            if( f.exists() && f.isFile() ) {
                return true;
            }
        }
        catch (Exception e) {
            //System.err.println("You got problem: " + e.getStackTrace());
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println( Utils.getInstance().getRootDirectory() );
        System.out.println( Utils.getInstance().getOS() );
        System.out.println( Utils.getInstance().getUserWorkingDirectory() );
    }

}
