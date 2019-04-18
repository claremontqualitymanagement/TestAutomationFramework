package se.claremont.taf.core.scp;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import se.claremont.taf.core.scp.config.SSHServerResource;
import se.claremont.taf.core.support.api.Taf;
import se.claremont.taf.core.testset.UnitTestClass;

import java.io.File;
import java.net.URL;

/**
 * Tests of shell interactions with Linux hosts
 *
 * Created by magnusolsson on 2016-12-05.
 */
public class ScpImplTest extends UnitTestClass{
    private static final String USERNAME = "test";
    private static final String PASSWORD = "password";
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 2022;

    @Rule
    public SSHServerResource sshServer = new SSHServerResource(USERNAME, PORT, ADDRESS);

    @Test
    @Ignore
    public void SftpUploadFileTest() {
        URL source = ScpImplTest.class.getClassLoader().getResource("copyfile.txt");
        if(source == null)return;
        String sourceFilePath = source.getPath();
        String destinationFilePath = "/copyfile.txt";

        ScpImpl scp = (ScpImpl) Taf.scp();
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
            //noinspection ResultOfMethodCallIgnored
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
