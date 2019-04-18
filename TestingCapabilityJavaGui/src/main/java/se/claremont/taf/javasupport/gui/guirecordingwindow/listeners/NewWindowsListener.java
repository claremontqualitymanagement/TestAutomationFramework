package se.claremont.taf.javasupport.gui.guirecordingwindow.listeners;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;

public class NewWindowsListener implements AWTEventListener {
    public void eventDispatched(AWTEvent event) {
        switch (event.getID()){
            case WindowEvent.WINDOW_OPENED:
                break;
            case WindowEvent.WINDOW_CLOSED:
                break;
        }
    }

    // ...
}