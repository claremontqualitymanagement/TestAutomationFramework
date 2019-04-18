package se.claremont.taf.javasupport.gui.guirecordingwindow;

import se.claremont.taf.core.gui.guistyle.TafHtmlTextPane;
import se.claremont.taf.javasupport.applicationundertest.ApplicationUnderTest;
import se.claremont.taf.javasupport.gui.JavaSupportTab;
import se.claremont.taf.javasupport.gui.guirecordingwindow.listeners.NewWindowsListener;
import se.claremont.taf.javasupport.gui.guirecordingwindow.listeners.RecordingFocusListener;
import se.claremont.taf.javasupport.gui.guirecordingwindow.listeners.RecordingKeyBoardListener;
import se.claremont.taf.javasupport.gui.guirecordingwindow.listeners.RecordingMouseListener;
import se.claremont.taf.javasupport.objectstructure.JavaWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.Collection;

public class RecordingListenersManager {

    private static TafHtmlTextPane scriptOutputArea;
    private static final NewWindowsListener newWindowsListener = new NewWindowsListener();
    private static JFrame keyBoardEventCatcher;

    static void setScriptArea(TafHtmlTextPane scriptArea){
        scriptOutputArea = scriptArea;
    }

    public static void startRecording(){
        Toolkit.getDefaultToolkit().addAWTEventListener(newWindowsListener, AWTEvent.WINDOW_EVENT_MASK);
        Collection<Window> windows;
        if(RecordingOptionsWindow.recordTafWindows){
            windows = ApplicationUnderTest.getWindows();
        } else {
            windows = JavaSupportTab.applicationUnderTest.getWindowsForSUT();
        }
        for(Window window : windows){
            makeSureAllComponentsHasRegisteredListeners(window);
        }
        if(RecordingOptionsWindow.recordKeyStrokesOutsideOfWindows){
            keyBoardEventCatcher = new JFrame();
            keyBoardEventCatcher.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
            keyBoardEventCatcher.setUndecorated(true);
            keyBoardEventCatcher.setOpacity(0);
            keyBoardEventCatcher.setVisible(true);
            keyBoardEventCatcher.getRootPane().setOpaque(false);
            keyBoardEventCatcher.addKeyListener(new RecordingKeyBoardListener(scriptOutputArea));
        }

    }

    public static void stopRecording(){
        Toolkit.getDefaultToolkit().removeAWTEventListener(newWindowsListener);
        Collection<Window> windows;
        if(RecordingOptionsWindow.recordTafWindows){
            windows = ApplicationUnderTest.getWindows();
        } else {
            windows = JavaSupportTab.applicationUnderTest.getWindowsForSUT();
        }
        for(Window window : windows){
            removeAllTafRecordingListenersFromWindow(window);
        }
        if(keyBoardEventCatcher != null){
            keyBoardEventCatcher.dispose();
        }
    }

    public static void removeAllTafRecordingListenersFromWindow(Window window) {

        JavaWindow javaWindow = new JavaWindow(window);

        for(Object object : javaWindow.getComponents()){

            Component c = (Component)object;

            for(MouseListener mouseListener : c.getMouseListeners()){
                if(mouseListener.getClass().equals(RecordingMouseListener.class)){
                    c.removeMouseListener(mouseListener);
                }
            }

            for(FocusListener focusListener: c.getFocusListeners()){
                if(focusListener.getClass().equals(RecordingFocusListener.class)){
                    c.removeFocusListener(focusListener);
                }
            }

            for(KeyListener keyListener : c.getKeyListeners()){
                if(keyListener.getClass().equals(RecordingKeyBoardListener.class)){
                    c.removeKeyListener(keyListener);
                }
            }
        }
    }

    public static void makeSureAllComponentsHasRegisteredListeners(Window window) {
        JavaWindow javaWindow = new JavaWindow(window);

        for(Object object : javaWindow.getComponents()){

            Component c = (Component)object;

            if(!RecordingMouseListener.isApplied(c))
                c.addMouseListener(new RecordingMouseListener(scriptOutputArea));

            if(!RecordingFocusListener.isApplied(c))
                c.addFocusListener(new RecordingFocusListener(scriptOutputArea));

            if(!RecordingOptionsWindow.recordKeyStrokesOutsideOfWindows && !RecordingKeyBoardListener.isApplied(c))
                c.addKeyListener(new RecordingKeyBoardListener(scriptOutputArea));

        }
    }

}
