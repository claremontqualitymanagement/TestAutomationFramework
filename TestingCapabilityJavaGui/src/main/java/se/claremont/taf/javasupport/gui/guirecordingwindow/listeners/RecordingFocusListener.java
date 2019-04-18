package se.claremont.taf.javasupport.gui.guirecordingwindow.listeners;

import se.claremont.taf.core.gui.Gui;
import se.claremont.taf.core.gui.guistyle.TafHtmlTextPane;
import se.claremont.taf.javasupport.gui.guirecordingwindow.RecordWindow;
import se.claremont.taf.javasupport.gui.teststeps.JavaWriteTestStep;
import se.claremont.taf.javasupport.interaction.MethodDeclarations;
import se.claremont.taf.javasupport.interaction.MethodInvoker;
import se.claremont.taf.javasupport.objectstructure.JavaGuiElement;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class RecordingFocusListener implements FocusListener {
    Component lastComponentInteractedUpon = null;
    java.util.List<Integer> keyCodesUsedUponComponent = new LinkedList<>();
    String keysPressedUponComponent = "";
    TafHtmlTextPane scriptArea;
    KeyBoardRecorder recorder = new KeyBoardRecorder();
    JavaGuiElement actualComponent;
    String initialText = null;

    public RecordingFocusListener(TafHtmlTextPane scriptArea) {
        this.scriptArea = scriptArea;
    }

    @Override
    public void focusGained(FocusEvent e) {
        initialText = (String) MethodInvoker.invokeTheFirstEncounteredMethodFromListOfMethodNames(
                e.getComponent(),
                MethodDeclarations.textGettingMethodsInAttemptOrder
        );
        actualComponent = new JavaGuiElement(e.getComponent());
        e.getComponent().addKeyListener(recorder);
    }

    @Override
    public void focusLost(FocusEvent e) {
        Object actualText = (String)MethodInvoker.invokeTheFirstEncounteredMethodFromListOfMethodNames(e.getComponent(), MethodDeclarations.textGettingMethodsInAttemptOrder);
        if(actualText == null || (initialText != null && initialText.equals(actualText))) {
            if(RecordWindow.keysPressedSinceLastWriteCommand.size() > 0)
                RecordingKeyBoardListener.addIdentifiedTypeCommandIfApplicable();
            return;
        }
        Gui.addTestStepToListOfAvailableTestSteps(new JavaWriteTestStep(new JavaGuiElement(e.getComponent()), keysPressedUponComponent));
        RecordingKeyBoardListener.addIdentifiedTypeCommandIfApplicable();
        if (e.getComponent() == null) return;
        String text = "<pre>java.write(new JavaGuiElement(By.byName(\"" + actualComponent.getName() + "\")), \"" + keysPressedUponComponent + "\");</pre><br>" + System.lineSeparator();
        scriptArea.append(text);
        scriptArea.revalidate();
        scriptArea.repaint();
        e.getComponent().removeKeyListener(recorder);
    }

    public static boolean isApplied(Component c) {
        for(FocusListener focusListener : c.getFocusListeners()){
            if(focusListener.getClass().equals(RecordingFocusListener.class)){
                return true;
            }
        }
        return false;
    }


    class KeyBoardRecorder implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            lastComponentInteractedUpon = e.getComponent();
            keysPressedUponComponent += e.getKeyChar();
            keyCodesUsedUponComponent.add(e.getExtendedKeyCode());
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
