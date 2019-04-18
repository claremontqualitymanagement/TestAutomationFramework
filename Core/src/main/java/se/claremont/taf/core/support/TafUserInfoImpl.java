package se.claremont.taf.core.support;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by magnusolsson on 2016-12-15.
 *
 * User info container for who is running the TAF.
 *
 */
@SuppressWarnings("AnonymousHasLambdaAlternative")
public class TafUserInfoImpl implements TafUserInfo {

    //create localThread of the current thread
    private static final ThreadLocal<TafUserInfoImpl> myTafUserInfo = new ThreadLocal<TafUserInfoImpl>() {
        @Override protected TafUserInfoImpl initialValue() {
            return new TafUserInfoImpl();
        }
    };

    //get instace of the object and access all the methods of it
    public static TafUserInfoImpl getInstance(){
        return myTafUserInfo.get();
    }

    @Override
    public String getUserAccountName(){
        try{
            return System.getProperty( "user.name" );
        }
        catch (Exception e){
            return "";
        }
    }

    @Override
    public String getUserHomeDirectory(){
        try{
            return System.getProperty( "user.home" );
        }
        catch (Exception e){
            return "";
        }
    }

    @Override
    public String getUserWorkingDirectory(){
        try{
            return System.getProperty( "user.dir" );
        }
        catch (Exception e){
            return "";
        }
    }

    @Override
    public String getOperatingSystemArchitecture(){
        try{
            return System.getProperty( "os.arch" );
        }
        catch (Exception e){
            return "";
        }
    }

    @Override
    public String getOperatingSystemName(){
        try{
            return System.getProperty( "os.name" );
        }
        catch (Exception e){
            return "";
        }
    }

    @Override
    public String getOperatingSystemVersion(){
        try{
            return System.getProperty( "os.version" );
        }
        catch (Exception e){
            return "";
        }
    }

    @Override
    public String getJavaVersion(){
        try{
            return System.getProperty( "java.version" );
        }
        catch (Exception e){
            return "";
        }
    }

    @Override
    public String getJavaHome(){
        try{
            return System.getProperty( "java.home" );
        }
        catch (Exception e){
            return "";
        }
    }

    @Override
    public String getHostName(){
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String getCanonicalHostName(){
        try {
            return InetAddress.getLocalHost().getCanonicalHostName();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String getHostAdress(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }

}
