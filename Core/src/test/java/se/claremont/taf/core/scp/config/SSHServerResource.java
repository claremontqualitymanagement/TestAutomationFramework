package se.claremont.taf.core.scp.config;


import com.sshtools.common.configuration.XmlConfigurationContext;
import com.sshtools.daemon.SshServer;
import com.sshtools.daemon.configuration.XmlServerConfigurationContext;
import com.sshtools.daemon.forwarding.ForwardingServer;
import com.sshtools.daemon.session.SessionChannelFactory;
import com.sshtools.j2ssh.configuration.ConfigurationException;
import com.sshtools.j2ssh.configuration.ConfigurationLoader;
import com.sshtools.j2ssh.connection.ConnectionProtocol;
import org.junit.rules.ExternalResource;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.Executors;

@SuppressWarnings({"SameParameterValue", "RedundantThrows", "ConstantConditions", "unused"})
public class SSHServerResource extends ExternalResource {

    private static XmlServerConfigurationContext serverPlatformConfiguration;

    private final String userId;
    private final int port;
    private final String bindAddress;

    private static final File BASE_DIR = new File(SSHServerResource.class.getClassLoader().getResource("basedir/").getFile());
    private static final File CONFIG_DIR = new File(SSHServerResource.class.getClassLoader().getResource("configdir/").getFile());

    public SSHServerResource (String userId, int port, String bindAddress) {
        this.userId = userId;
        this.port = port;
        this.bindAddress = bindAddress;
    }

    @Override
    protected void before () throws Throwable {
        setupConfiguration();

        Executors.newSingleThreadExecutor().submit(() -> {
            start();
            return null;
        });
    }

    @Override
    protected void after () {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(),
                    ((com.sshtools.daemon.configuration.ServerConfiguration) ConfigurationLoader
                            .getConfiguration(com.sshtools.daemon.configuration.ServerConfiguration.class))
                            .getCommandPort());

            socket.getOutputStream().write(0x3a);

            String msg = "bye";
            int len = (msg.length() <= 255) ? msg.length() : 255;
            socket.getOutputStream().write(len);

            if (len > 0) {
                socket.getOutputStream().write(msg.substring(0, len).getBytes());
            }

            socket.close();
        } catch(IOException e) {
            //Ignore
        }
    }

    public File getRootDirectory () {
        return BASE_DIR;
    }

    public File getUserHome () {
        return new File (BASE_DIR.getAbsolutePath().replace('\\', '/') + "/home/" + userId);
    }

    public String getUserId () {
        return userId;
    }

    public int getPort () {
        return port;
    }

    public String getBindAddress () {
        return bindAddress;
    }

    private void setupConfiguration () throws IOException {
        createPlatformConfig ();
        createServerConfig ();
        copyResources ();
        setupHomeDir ();

        configureServer ();
    }

    private void start () throws IOException {
        SshServer server = new SshServer() {
            public void configureServices (ConnectionProtocol connection) throws IOException {
                connection.addChannelFactory(SessionChannelFactory.SESSION_CHANNEL,
                        new SessionChannelFactory());

                if (ConfigurationLoader.isConfigurationAvailable(ServerConfiguration.class)) {
                    if (((com.sshtools.daemon.configuration.ServerConfiguration) ConfigurationLoader
                            .getConfiguration(com.sshtools.daemon.configuration.ServerConfiguration.class)).getAllowTcpForwarding()) {
                        new ForwardingServer(connection);
                    }
                }
            }
            public void shutdown (String msg) {}
        };

        server.startServer();
    }

    private void configureServer () throws ConfigurationException {

        String configBase = CONFIG_DIR.getAbsolutePath().replace('\\', '/') + '/';

        if (null == serverPlatformConfiguration) {
            serverPlatformConfiguration = new XmlServerConfigurationContext();
            serverPlatformConfiguration.setServerConfigurationResource(ConfigurationLoader
                    .checkAndGetProperty("sshtools.server", configBase + "server.xml"));
            serverPlatformConfiguration.setPlatformConfigurationResource(System.getProperty(
                    "sshtools.platform", configBase + "platform.xml"));
            ConfigurationLoader.initialize(false, serverPlatformConfiguration);
        } else {
            serverPlatformConfiguration.setServerConfigurationResource(ConfigurationLoader
                    .checkAndGetProperty("sshtools.server", configBase + "server.xml"));
            serverPlatformConfiguration.setPlatformConfigurationResource(System.getProperty(
                    "sshtools.platform", configBase + "platform.xml"));
            serverPlatformConfiguration.initialize();
        }

        XmlConfigurationContext context2 = new XmlConfigurationContext();
        context2.setFailOnError(false);
        context2.setAPIConfigurationResource(ConfigurationLoader.checkAndGetProperty(
                "sshtools.config", configBase + "sshtools.xml"));
        context2.setAutomationConfigurationResource(ConfigurationLoader.checkAndGetProperty(
                "sshtools.automate", configBase + "automation.xml"));
        ConfigurationLoader.initialize(false, context2);
    }

    private void setupHomeDir () {
        File homeBase = new File(BASE_DIR, "home");
        //noinspection ResultOfMethodCallIgnored
        homeBase.mkdirs();
        File userHome = new File(homeBase, userId);
        //noinspection ResultOfMethodCallIgnored
        userHome.mkdirs();
    }

    private void copyResources () throws IOException {
        for (String file : Arrays.asList("sshtools.xml", "automation.xml", "test-dsa.key")) {
            URL fileUrl = ClassLoader.getSystemResource(file);
            File source = new File(fileUrl.getFile());
            File target = new File(CONFIG_DIR.getAbsolutePath(), file);
            try {
                Files.copy(source.toPath(), target.toPath());
            } catch (FileAlreadyExistsException e) {
                //Ignore
            }
        }
    }

    private void createPlatformConfig () throws IOException {
        PlatformConfiguration platformConfig = new PlatformConfiguration();
        platformConfig.setNativeProcessProvider("com.sshtools.daemon.platform.UnsupportedShellProcessProvider");
        platformConfig.setNativeAuthenticationProvider("se.claremont.autotest.common.scp.config.JunitDummyAuthenticationProvider");
        platformConfig.setNativeFileSystemProvider("com.sshtools.daemon.vfs.VirtualFileSystem");
        PlatformConfiguration.VFSRoot root = new PlatformConfiguration.VFSRoot();
        root.setPath(BASE_DIR.getAbsolutePath().replace('\\', '/'));
        platformConfig.setVfsRoot(root);

        marshall(platformConfig, "platform.xml");
    }

    private void createServerConfig () throws IOException {
        ServerConfiguration serverConfig = new ServerConfiguration();

        ServerConfiguration.ServerHostKey hostKey = new ServerConfiguration.ServerHostKey();
        hostKey.setPrivateKeyFile(CONFIG_DIR.getAbsolutePath().replace('\\', '/') + "/test-dsa.key");
        serverConfig.addServerHostKey(hostKey);

        serverConfig.setPort(port);
        serverConfig.setListenAddress(bindAddress);
        serverConfig.setMaxConnections(3);
        serverConfig.addAllowedAuthentication("password");
        serverConfig.addAllowedAuthentication("keyboard-interactive");

        ServerConfiguration.Subsystem subsystem = new ServerConfiguration.Subsystem();
        subsystem.setName("sftp");
        subsystem.setType("class");
        subsystem.setProvider("com.sshtools.daemon.sftp.SftpSubsystemServer");
        serverConfig.addSubsystem(subsystem);

        marshall(serverConfig, "server.xml");
    }

    private void marshall (Object source, String fileName) {
        File outputFile = new File(CONFIG_DIR, fileName);
        JAXB.marshal(source, outputFile);
    }
}
