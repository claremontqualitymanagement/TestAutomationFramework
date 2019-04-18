package se.claremont.taf.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.taf.support.api.Taf;

import java.io.File;

/**
 * Created by magnusolsson on 2016-09-21.
 *
 * Singelton class for Utils stuff.
 *
 */
public class Utils {

    private final static Logger logger = LoggerFactory.getLogger( Utils.class );
    private static Utils instance = null;
    public final int SUPPORTED_TAF_JVM_VERSION = 8;

    private Utils() {

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
     * @return true if jvm is running on Windows, otherwise false
     */
    public boolean amIWindowsOS() {
        return getOS().toLowerCase().contains("win");
    }

    /**
     *
     * @return true if jvm is running on Linux distribution otherwise false
     */
    public boolean amILinuxOS() {
        return getOS().toLowerCase().contains("linux");
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
    @SuppressWarnings("WeakerAccess")
    public String getRootDirectory() {
        return File.listRoots()[0].getAbsolutePath();
    }

    /**
     * Checks if pathToFile exists and is a file
     * @param pathToFile The path to the file to check existance of.
     * @return true if file path exists and is file
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean doesFileExists(String pathToFile) {

        try {
            File f = new File( pathToFile );
            if( f.exists() && f.isFile() ) {
                return true;
            }
        }
        catch (Exception fe) {
            //System.err.println("You got problem: " + e.getStackTrace());
            logger.debug( fe.getMessage() );
        }
        return false;
    }

    /**
     * Checks if running jvm supports TAF supported java version 8.
     * @return true if java version is 8 otherwise false
     */
    public boolean checkSupportedJavaVersionForTAF()
    {
        try {
            int presentJVMVersion = Integer.parseInt( Taf.tafUserInfon().getJavaVersion().substring( Taf.tafUserInfon().getJavaVersion().indexOf(".") + 1, Taf.tafUserInfon().getJavaVersion().indexOf(".") + 2 ) );
            if( presentJVMVersion >= SUPPORTED_TAF_JVM_VERSION )
                return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static void main(String[] args) {
        logger.debug( Utils.getInstance().getRootDirectory() );
        logger.debug( Utils.getInstance().getOS() );
        logger.debug( Utils.getInstance().getUserWorkingDirectory() );

        logger.debug( Taf.tafUserInfon().getCanonicalHostName() );
        logger.debug( Taf.tafUserInfon().getHostAdress() );
        logger.debug( Taf.tafUserInfon().getHostName() );
        logger.debug( Taf.tafUserInfon().getOperatingSystemArchitecture() );
        logger.debug( Taf.tafUserInfon().getOperatingSystemName() );
        logger.debug( Taf.tafUserInfon().getOperatingSystemVersion() );
        logger.debug( Taf.tafUserInfon().getJavaHome() );
        logger.debug( Taf.tafUserInfon().getJavaVersion() );
        logger.debug( Taf.tafUserInfon().getJavaVersion().substring( Taf.tafUserInfon().getJavaVersion().indexOf(".") + 1, Taf.tafUserInfon().getJavaVersion().indexOf(".") + 2 ) );
        logger.debug( Taf.tafUserInfon().getUserAccountName() );
        logger.debug( Taf.tafUserInfon().getUserHomeDirectory() );
        logger.debug( Taf.tafUserInfon().getUserWorkingDirectory() );
        logger.debug( Runtime.class.getPackage().getImplementationVersion() );
    }

}
