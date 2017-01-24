package se.claremont.autotest.common.scp;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

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
    public boolean sftpUploadFromLinuxToLinux(String user, String pass, String host, String sourceFilePath, String destinationFilePath) {
        JSch jsch=new JSch();

        Session session = null;
        ChannelSftp channel = null;

        try{
            session = jsch.getSession( user, host, 22 );
            session.setPassword( pass );

            Properties config = new Properties();
            config.put("StrictHostKeyChecking","no");
            session.setConfig(config);
            session.connect();

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
            jschex.printStackTrace();
            return false;
        }
        catch (SftpException sftpe){
            sftpe.printStackTrace();
            return false;
        }
        catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
            return false;
        }

        finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        return true;
    }

    @Override
    public boolean sftpDownloadFromLinuxToLinux(String user, String pass, String host, String sourceFilePath, String destinationFilePath) {
        JSch jsch=new JSch();

        Session session = null;
        ChannelSftp channel = null;

        try{
            session = jsch.getSession( user, host, 22 );
            session.setPassword( pass );

            Properties config = new Properties();
            config.put("StrictHostKeyChecking","no");
            session.setConfig(config);
            session.connect();

            channel = (ChannelSftp)session.openChannel("sftp");
            channel.connect();

            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.get( sourceFilePath, destinationFilePath);

            sftp.disconnect();
            channel.disconnect();
            session.disconnect();

        }
        catch (JSchException jschex){
            jschex.printStackTrace();
            return false;
        }
        catch (SftpException sftpe){
            sftpe.printStackTrace();
            return false;
        }

        finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        return true;
    }

    @Override
    public void createShell(String user, String pass, String host, String command)
    {
        JSch jsch=new JSch();

        Session session = null;
        Channel channel = null;
        int timeout = 50000;     //5 sec

        try{
            session = jsch.getSession( user, host, 22 );
            session.setPassword( pass );

            Properties config = new Properties();
            config.put("StrictHostKeyChecking","no");
            session.setConfig(config);
            session.connect( timeout );

            channel = session.openChannel("shell");

            channel.setInputStream( System.in, true );
            channel.setOutputStream( System.out );

            // or something!!!
            channel.connect( 3*timeout );

            channel.disconnect();
            session.disconnect();

        }
        catch (JSchException jschex){
            jschex.printStackTrace();
        }

        finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

}
