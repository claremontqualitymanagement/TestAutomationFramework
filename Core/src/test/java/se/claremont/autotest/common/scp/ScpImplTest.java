package se.claremont.autotest.common.scp;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import se.claremont.autotest.common.scp.config.SSHServerResource;
import se.claremont.autotest.common.support.api.Taf;

import java.io.File;

/**
 * Created by magnusolsson on 2016-12-05.
 */
public class ScpImplTest {
    private static final String USERNAME = "test";
    private static final String PASSWORD = "password";
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 2022;

    @Rule
    public SSHServerResource sshServer = new SSHServerResource(USERNAME, PORT, ADDRESS);

    @Test
    @Ignore
    public void SftpUploadFileTest() throws InterruptedException {
        String sourceFilePath = ScpImplTest.class.getClassLoader().getResource("copyfile.txt").getPath();
        String destinationFilePath = "/copyfile.txt";

        ScpImpl scp = (ScpImpl)Taf.scp();
        scp.setPort(PORT);
        boolean result = scp.sftpUploadFromLinuxToLinux(USERNAME, PASSWORD, ADDRESS, sourceFilePath, destinationFilePath);
        Assert.assertTrue("sftp uploading file " + sourceFilePath + " failed!", result);
    }

    @Test
    @Ignore
    public void SftpDownloadFileTest() throws InterruptedException {
        //Fails for some reason unless there's a timeout.. Guessing it's threadrelated
        Thread.sleep(10L);

        String sourceFilePath = "/emptyfile";
        String destinationFilePath = new File(ClassLoader.getSystemResource(".").getPath(), "emptyfile2").getPath();

        ScpImpl scp = (ScpImpl) Taf.scp();
        scp.setPort(PORT);
        boolean result = scp.sftpDownloadFromLinuxToLinux(USERNAME, PASSWORD, ADDRESS, sourceFilePath, destinationFilePath);

        Assert.assertTrue("sftp downloading file " + sourceFilePath + " failed!", result);
        Assert.assertTrue("File should have been downloaded to resources", new File(destinationFilePath).exists());

        //cleanup
        File f = new File(destinationFilePath);
        if(f.isFile()) {
            f.delete();
        }
    }

    @Test
    @Ignore
    public void ShellTest(){
        String command = "ls";
        Taf.scp().createShell(USERNAME, PASSWORD, ADDRESS, command);
        //Assert.assertTrue("creating shell failed!", );
    }

}
