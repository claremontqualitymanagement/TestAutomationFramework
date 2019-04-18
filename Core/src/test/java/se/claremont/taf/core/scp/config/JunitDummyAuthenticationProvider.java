package se.claremont.taf.core.scp.config;

import com.sshtools.daemon.platform.NativeAuthenticationProvider;
import com.sshtools.daemon.platform.PasswordChangeException;
import com.sshtools.j2ssh.configuration.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class JunitDummyAuthenticationProvider extends NativeAuthenticationProvider {

    @SuppressWarnings("unused")
    public JunitDummyAuthenticationProvider() {
        System.out.println("JunitDummyAuthenticationProvider is in use. This is only for testing.");
    }

    @Override
    public boolean changePassword(String username, String oldpassword, String newpassword) {
        return false;
    }

    @Override
    public void logoffUser() throws IOException {}

    @Override
    public boolean logonUser(String username, String password) throws
            PasswordChangeException, IOException {
        return true;
    }

    @Override
    public boolean logonUser(String username) throws IOException {
        return true;
    }

    @Override
    public String getHomeDirectory (String username) throws IOException {
        PlatformConfiguration.VFSMount vfsroot = ((PlatformConfiguration) ConfigurationLoader.getConfiguration(PlatformConfiguration.class)).getVfsMounts().get(0);
        String base = vfsroot.getPath();
        File homeDir = new File (base + "/home/" + username);
        return homeDir.getAbsolutePath().replace('\\', '/');
    }

}