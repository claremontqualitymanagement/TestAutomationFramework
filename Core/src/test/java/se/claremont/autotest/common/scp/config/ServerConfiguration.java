package se.claremont.autotest.common.scp.config;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement (name = "ServerConfiguration")
@XmlAccessorType (XmlAccessType.FIELD)
public class ServerConfiguration {

    @XmlElement (name = "ServerHostKey", required = true)
    private List<ServerConfiguration.ServerHostKey> serverHostKey;
    @XmlElement (name = "Port")
    private int port;
    @XmlElement (name = "ListenAddress", required = true)
    private String listenAddress;
    @XmlElement (name = "MaxConnections")
    private int maxConnections;
    @XmlElement (name = "AllowedAuthentication", required = true)
    private List<String> allowedAuthentication;
    @XmlElement (name = "Subsystem", required = true)
    private List<ServerConfiguration.Subsystem> subsystem;

    public List<ServerConfiguration.ServerHostKey> getServerHostKey () {
        return serverHostKey;
    }

    public void setServerHostKey (List<ServerConfiguration.ServerHostKey> serverHostKey) {
        this.serverHostKey = serverHostKey;
    }

    public void addServerHostKey (ServerConfiguration.ServerHostKey serverHostKeyI) {
        if (null == this.serverHostKey) {
            this.serverHostKey = new ArrayList<ServerConfiguration.ServerHostKey>();
        }
        this.serverHostKey.add(serverHostKeyI);
    }

    public int getPort () {
        return port;
    }

    public void setPort (int port) {
        this.port = port;
    }

    public String getListenAddress () {
        return listenAddress;
    }

    public void setListenAddress (String listenAddress) {
        this.listenAddress = listenAddress;
    }

    public int getMaxConnections () {
        return maxConnections;
    }

    public void setMaxConnections (int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public List<String> getAllowedAuthentication () {
        return allowedAuthentication;
    }

    public void setAllowedAuthentication (List<String> allowedAuthentication) {
        this.allowedAuthentication = allowedAuthentication;
    }

    public void addAllowedAuthentication (String allowedAuthenticationI) {
        if (null == this.allowedAuthentication) {
            this.allowedAuthentication = new ArrayList<String>();
        }
        this.allowedAuthentication.add(allowedAuthenticationI);
    }

    public List<ServerConfiguration.Subsystem> getSubsystem () {
        return subsystem;
    }

    public void setSubsystem (List<ServerConfiguration.Subsystem> subsystem) {
        this.subsystem = subsystem;
    }

    public void addSubsystem (ServerConfiguration.Subsystem subsystemI) {
        if (null == this.subsystem) {
            this.subsystem = new ArrayList<ServerConfiguration.Subsystem>();
        }
        this.subsystem.add(subsystemI);
    }

    @XmlAccessorType (XmlAccessType.FIELD)
    @XmlType (name = "")
    public static class ServerHostKey {

        @XmlAttribute (name = "PrivateKeyFile", required = true)
        protected String privateKeyFile;

        public String getPrivateKeyFile () {
            return privateKeyFile;
        }

        public void setPrivateKeyFile (String value) {
            this.privateKeyFile = value;
        }
    }

    @XmlAccessorType (XmlAccessType.FIELD)
    @XmlType (name = "")
    public static class Subsystem {

        @XmlAttribute (name = "Name", required = true)
        protected String name;
        @XmlAttribute (name = "Type", required = true)
        protected String type;
        @XmlAttribute (name = "Provider", required = true)
        protected String provider;

        public String getName () {
            return name;
        }

        public void setName (String value) {
            this.name = value;
        }

        public String getType () {
            return type;
        }

        public void setType (String value) {
            this.type = value;
        }

        public String getProvider () {
            return provider;
        }

        public void setProvider (String value) {
            this.provider = value;
        }
    }
}