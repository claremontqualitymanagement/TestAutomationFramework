package se.claremont.autotest.common.scp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import com.jcraft.jsch.*;

/**
 * Created by magnusolsson on 2016-12-05.
 */
public class ScpImpl implements Scp {

    //create localThread of the current thread
    private static ThreadLocal<ScpImpl> myScp = new ThreadLocal<ScpImpl>() {
        @Override protected ScpImpl initialValue() {
            return new ScpImpl();
        }
    };

    //get instace of the object and access all the methods of it
    public static ScpImpl getInstance(){
        return myScp.get();
    }

    @Override
    public void doSomeScpStuff() {

    }

    @Override
    public boolean sftpFromLinuxToLinux(String user, String pass, String host, String sourceFilePath, String destinationFilePath) {
        JSch jsch=new JSch();

        try{
            Session session = jsch.getSession( user, host, 22 );
            session.setPassword( pass );

            Properties config = new Properties();
            config.put("StrictHostKeyChecking","no");
            session.setConfig(config);
            session.connect();

            ChannelSftp channel = null;
            channel = (ChannelSftp)session.openChannel("sftp");
            channel.connect();
            File localFile = new File( sourceFilePath );
            File remoteFile = new File( destinationFilePath );

            //If you want you can change the directory using the following line.
            channel.cd( remoteFile.getParent() );
            channel.put( new FileInputStream(localFile),localFile.getName() );

            channel.disconnect();
            session.disconnect();

        }
        catch (JSchException jschex){
            return false;
        }
        catch (SftpException sftpe){
            return false;
        }
        catch (FileNotFoundException fnfe){
            return false;
        }

        return true;
    }
}
