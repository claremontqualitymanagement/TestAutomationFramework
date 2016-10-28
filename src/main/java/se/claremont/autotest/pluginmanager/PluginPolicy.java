package se.claremont.autotest.pluginmanager;

import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;

/**
 * Created by jordam on 2016-10-28.
 */
public class PluginPolicy extends Policy {

    public PermissionCollection getPermissions(CodeSource codeSource) {
        Permissions p = new Permissions();
//        if (!codeSource.getLocation().toString().endsWith("/unsafe.jar")) {
//            p.add(new AllPermission());
//        }
        return p;
    }

    public void refresh() {
    }

}
