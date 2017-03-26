package se.claremont.autotest.pluginmanager;

import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;

//TODO: Not using this approach for plugins anymore. Should this class stay?
/**
 * Genering plug in manager policy provider
 *
 * Created by jordam on 2016-10-28.
 */
@SuppressWarnings("WeakerAccess")
public class PluginPolicy extends Policy {

    public PermissionCollection getPermissions(CodeSource codeSource) {
        @SuppressWarnings("UnnecessaryLocalVariable") Permissions p = new Permissions();
//        if (!codeSource.getLocation().toString().endsWith("/unsafe.jar")) {
//            p.add(new AllPermission());
//        }
        return p;
    }

    public void refresh() {
    }

}
