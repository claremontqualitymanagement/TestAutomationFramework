package se.claremont.autotest.common.scp;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.taf.api.Taf;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by magnusolsson on 2016-12-05.
 */
public class ScpTest {

    @Test
    @Ignore
    public void SftpUploadFileTest(){
        String user = "taf";
        String pass = "Claremont16!";
        String host = "46.101.193.212";
        String sourceFilePath = "taf.log";
        String destinationFilePath = "/home/taf/test.log";
        boolean result = Taf.scp().sftpUploadFromLinuxToLinux(user, pass, host, sourceFilePath, destinationFilePath);
        Assert.assertTrue("sftp uploading file " + sourceFilePath + "failed!", result);
    }

    @Test
    @Ignore
    public void SftpDownloadFileTest() throws IOException {
        String user = "taf";
        String pass = "Claremont16!";
        String host = "46.101.193.212";
        String sourceFilePath = "/home/taf/.bashrc";
        String destinationFilePath = ".bashrc_downloaded";
        boolean result = Taf.scp().sftpDownloadFromLinuxToLinux(user, pass, host, sourceFilePath, destinationFilePath);
        Assert.assertTrue("sftp downloading file " + sourceFilePath + " failed!", result);

        //cleanup
        File f = new File(destinationFilePath);
        Files.delete(Paths.get(f.toURI()));
    }

    @Ignore("Somehow makes all unittests after ignored")
    @Test
    public void ShellTest(){
        String user = "taf";
        String pass = "Claremont16!";
        String host = "46.101.193.212";
        String command = "ls";
        Taf.scp().createShell(user, pass, host, command);
        //Assert.assertTrue("creating shell failed!", );
    }

}
