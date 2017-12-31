package se.claremont.autotest.websupport.gui.recorder.restserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class holds the gateway server itself. This is the simple HTTP server
 * that hosts the REST services the TAF installation connects to as well as
 * some HTML pages.
 *
 * Created by jordam on 2017-03-18.
 */
public class HttpServer {

    private ResourceConfig config = new ResourceConfig();
    Server server;

    /**
     * Starting the server to enable communication.
     */
    public void start(){
        System.out.println(System.lineSeparator() + "Starting TAF recording listener REST Server." + System.lineSeparator());
        config.packages("se.claremont.autotest.websupport.gui.recorder.restserver");
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        server = new Server(Settings.port);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");
        try {
            server.start();
        }catch (Exception e){
            System.out.println(System.lineSeparator() + e.toString());
        }
        if(isStarted()){
            System.out.println(System.lineSeparator() + "TAF Recording listener REST server started.");
        } else {
            System.out.println(System.lineSeparator() + "Could not start REST server." + System.lineSeparator());
        }
    }

    /**
     * Checks if the server is started.
     *
     * @return Returns true if server is running, else false.
     */
    public boolean isStarted(){
        return (server != null && !server.isFailed());
    }


    /**
     * Stop the server.
     */
    public void stop(){
        try{
            server.stop();
            server.destroy();
            System.out.println(System.lineSeparator() + "Server stopped." + System.lineSeparator());
        }catch (Exception e){
            System.out.println("Error stopping HTTP server: " + e.toString());
        }
    }

    /**
     * Identifies local IP-address of the machine this server is executed on. Used to display connect help information.
     *
     * @return Returns IP address of the machine the server is executed upon.
     */
    public static String getIPAddressesOfLocalMachine(){
        String ip = "Could not identify local IP address.";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }


}
