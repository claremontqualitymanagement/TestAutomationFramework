package se.claremont.taf.core.scp;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * This class implements support for SCP, Secure Copy. This is a mechanism to copy files on Linux file systems.
 *
 * Created by magnusolsson on 2016-12-05.
 */
@SuppressWarnings("SameParameterValue")
public class ScpImpl implements Scp {

    //create localThread of the current thread
    private static final ThreadLocal<ScpImpl> myScp = ThreadLocal.withInitial(ScpImpl::new);
    private int port = 22;

    private ScpImpl() {}

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
            session = jsch.getSession( user, host, port );
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

        } catch (JSchException | SftpException | FileNotFoundException jschex){
            jschex.printStackTrace();
            return false;
        } finally {
            closeRunnable(channel);
            closeRunnable(session);
        }
        return true;
    }

    @Override
    public boolean sftpDownloadFromLinuxToLinux(String user, String pass, String host, String sourceFilePath, String destinationFilePath) {
        JSch jsch=new JSch();

        Session session = null;
        ChannelSftp channel = null;

        try{
            session = jsch.getSession( user, host, port );
            session.setPassword( pass );

            Properties config = new Properties();
            config.put("StrictHostKeyChecking","no");
            session.setConfig(config);
            session.connect();

            channel = (ChannelSftp)session.openChannel("sftp");
            channel.connect();

            channel.get( sourceFilePath, destinationFilePath);
        } catch (JSchException | SftpException jschex){
            jschex.printStackTrace();
            return false;
        } finally {
            closeRunnable(channel);
            closeRunnable(session);
        }
        return true;
    }

    @Override
    public void createShell(String user, String pass, String host, String command) {
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

        } catch (JSchException jschex){
            jschex.printStackTrace();
        } finally {
            closeRunnable(channel);
            closeRunnable(session);
        }
    }

    private void closeRunnable(Runnable runnable) {
        if(runnable != null) {
            if (runnable instanceof ChannelSftp) {
                ((ChannelSftp) runnable).disconnect();
            }
            if (runnable instanceof Session) {
                ((Session) runnable).disconnect();
            }
        }
    }

    public void setPort(int port) {
        this.port = port;
    }

}
