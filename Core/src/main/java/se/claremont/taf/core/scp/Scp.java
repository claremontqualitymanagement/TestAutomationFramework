package se.claremont.taf.core.scp;

/**
 * Shell interactions with Linux based machines
 *
 * Created by magnusolsson on 2016-12-05.
 */
@SuppressWarnings({"SameParameterValue", "unused"})
public interface Scp {

    @SuppressWarnings({"EmptyMethod", "unused"})
    void doSomeScpStuff();

    /**
     * Most likly to only work from linux to linux system due to sftp service?
     * @param user The username that will be used for login
     * @param pass The password for the user
     * @param host The address for the machine being logged into
     * @param sourceFilePath path of sourcefile
     * @param destinationFilePath path of destinationfile
     * @return boolean of if upload was completed successfully
     */
    boolean sftpUploadFromLinuxToLinux(String user, String pass, String host, String sourceFilePath, String destinationFilePath);

    /**
     * Most likly to only work from linux to linux system?
     * @param user The username that will be used for login
     * @param pass The password for the user
     * @param host The address for the machine being logged into
     * @param sourceFilePath path of sourcefile
     * @param destinationFilePath path of destinationfile
     * @return boolean of if download was completed successfully
     */
    boolean sftpDownloadFromLinuxToLinux(String user, String pass, String host, String sourceFilePath, String destinationFilePath);

    /**
     * Method enables you to connect to sshd server and get the shell prompt
     * @param user The username that will be used for login
     * @param pass The password for the user
     * @param host The address for the machine being logged into
     * @param command e.g. ls
     */
    void createShell(String user, String pass, String host, String command);


}
