package se.claremont.autotest.common.scp;

/**
 * Created by magnusolsson on 2016-12-05.
 */
public interface Scp {

    void doSomeScpStuff();

    /**
     *
     * @param user
     * @param pass
     * @param host
     * @param sourceFilePath
     * @param destinationFilePath
     * @return
     */
    boolean sftpFromLinuxToLinux(String user, String pass, String host, String sourceFilePath, String destinationFilePath);
}
