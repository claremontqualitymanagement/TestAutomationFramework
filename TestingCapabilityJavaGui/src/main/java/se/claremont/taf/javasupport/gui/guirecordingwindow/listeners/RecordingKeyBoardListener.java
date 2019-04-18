package se.claremont.taf.javasupport.gui.guirecordingwindow.listeners;

import se.claremont.taf.gui.Gui;
import se.claremont.taf.gui.guistyle.TafHtmlTextPane;
import se.claremont.taf.javasupport.gui.guirecordingwindow.JavaGuiElementDeclarationManager;
import se.claremont.taf.javasupport.gui.guirecordingwindow.RecordWindow;
import se.claremont.taf.javasupport.gui.teststeps.JavaTextTypedTestStep;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RecordingKeyBoardListener implements KeyListener{

    static TafHtmlTextPane outputArea;

    public RecordingKeyBoardListener(TafHtmlTextPane outputArea){
        this.outputArea = outputArea;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        RecordWindow.keysPressedSinceLastWriteCommand.add(new KeyPress(e.getExtendedKeyCode(), PressActionType.DOWN));
        RecordWindow.activeComponent = e.getComponent();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        RecordWindow.keysPressedSinceLastWriteCommand.add(new KeyPress(e.getExtendedKeyCode(), PressActionType.DOWN));
    }

    public static void addIdentifiedTypeCommandIfApplicable(){
        if(RecordWindow.keysPressedSinceLastWriteCommand.size() == 0)return;
        StringBuilder text = new StringBuilder();
        for(KeyPress characterExtendedKeyCode : RecordWindow.keysPressedSinceLastWriteCommand){
            if(!Character.isAlphabetic(characterExtendedKeyCode.keyCode) && characterExtendedKeyCode.pressActionType == PressActionType.DOWN)
                text.append("+");
            if(!Character.isAlphabetic(characterExtendedKeyCode.keyCode) && characterExtendedKeyCode.pressActionType == PressActionType.UP)
                text.append("-");
            if(Character.isAlphabetic(characterExtendedKeyCode.keyCode) && characterExtendedKeyCode.pressActionType == PressActionType.DOWN)
               text.append(Character.getName(characterExtendedKeyCode.keyCode));
            if(!Character.isAlphabetic(characterExtendedKeyCode.keyCode))
                text.append(Character.getName(characterExtendedKeyCode.keyCode));
        }
        Gui.addTestStepToListOfAvailableTestSteps(new JavaTextTypedTestStep(text.toString(), JavaGuiElementDeclarationManager.createJavaGuiElement(RecordWindow.activeComponent)));
        RecordWindow.keysPressedSinceLastWriteCommand.clear();
        String outputText = "<pre>java.type(" +
                JavaGuiElementDeclarationManager.javaGuiElementAsCodeString(
                                JavaGuiElementDeclarationManager.createJavaGuiElement(RecordWindow.activeComponent)) +
                ", \"" + text + "\"));</pre><br>" + System.lineSeparator();
        outputArea.append(outputText);
        outputArea.revalidate();
        outputArea.repaint();

    }

    public static boolean isApplied(Component c) {
        for(KeyListener keyListener : c.getKeyListeners()){
            if(keyListener.getClass().equals(RecordingKeyBoardListener.class))
                return true;
        }
        return false;
    }

    public class KeyPress{
        Integer keyCode;
        PressActionType pressActionType;

        KeyPress(Integer keyCode, PressActionType pressActionType){
            this.pressActionType = pressActionType;
            this.keyCode = keyCode;
        }

    }

    enum PressActionType{
        UP,
        DOWN
    }
}
