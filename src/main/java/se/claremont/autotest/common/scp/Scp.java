package se.claremont.autotest.common.scp;

/**
 * Created by magnusolsson on 2016-12-05.
 */
public interface Scp {

    void doSomeScpStuff();

    /**
     * Most likly to only work from linux to linux system due to sftp service?
     * @param user
     * @param pass
     * @param host
     * @param sourceFilePath
     * @param destinationFilePath
     * @return
     */
    boolean sftpUploadFromLinuxToLinux(String user, String pass, String host, String sourceFilePath, String destinationFilePath);

    /**
     * Most likly to only work from linux to linux system?
     * @param user
     * @param pass
     * @param host
     * @param sourceFilePath
     * @param destinationFilePath
     * @return
     */
    boolean sftpDownloadFromLinuxToLinux(String user, String pass, String host, String sourceFilePath, String destinationFilePath);

    /**
     * Method enables you to connect to sshd server and get the shell prompt
     * @param user
     * @param pass
     * @param host
     * @param command e.g. ls
     */
    void createShell(String user, String pass, String host, String command);


}
