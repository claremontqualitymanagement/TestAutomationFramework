package se.claremont.autotest.common.scp;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.taf.api.Taf;

import java.io.File;

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
        boolean result = new Taf().scp().sftpUploadFromLinuxToLinux(user, pass, host, sourceFilePath, destinationFilePath);
        Assert.assertTrue("sftp uploading file " + sourceFilePath + "failed!", result == true);
    }

    @Test
    @Ignore
    public void SftpDownloadFileTest(){
        String user = "taf";
        String pass = "Claremont16!";
        String host = "46.101.193.212";
        String sourceFilePath = "/home/taf/.bashrc";
        String destinationFilePath = ".bashrc_downloaded";
        boolean result = new Taf().scp().sftpDownloadFromLinuxToLinux(user, pass, host, sourceFilePath, destinationFilePath);
        Assert.assertTrue("sftp downloading file " + sourceFilePath + " failed!", result == true);

        //cleanup
        File f = new File(destinationFilePath);
        if(f.isFile())
            f.delete();
    }

    @Test
    @Ignore
    public void ShellTest(){
        String user = "taf";
        String pass = "Claremont16!";
        String host = "46.101.193.212";
        String command = "ls";
        new Taf().scp().createShell(user, pass, host, command);
        //Assert.assertTrue("creating shell failed!", );
    }

}
