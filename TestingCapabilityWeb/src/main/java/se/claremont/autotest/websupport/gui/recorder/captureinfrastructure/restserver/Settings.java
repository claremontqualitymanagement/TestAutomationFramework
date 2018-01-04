package se.claremont.autotest.websupport.gui.recorder.captureinfrastructure.restserver;

import java.io.IOException;
import java.net.ServerSocket;

public class Settings {

    public static int port = portSingleton.getInt();

    private static class portSingleton{
        static Integer value = null;

        public static int getInt(){
            if(value == null){
                try {
                    value = new ServerSocket(0).getLocalPort();
                } catch (IOException e) {
                    value = 8083;
                }
            }
            return value;
        }
    }

}
