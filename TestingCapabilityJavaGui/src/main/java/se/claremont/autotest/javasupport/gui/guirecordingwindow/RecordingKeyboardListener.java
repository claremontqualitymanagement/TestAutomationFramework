package se.claremont.autotest.javasupport.gui.guirecordingwindow;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.gui.guistyle.TafHtmlTextPane;
import se.claremont.autotest.javasupport.gui.teststeps.JavaWriteTestStep;
import se.claremont.autotest.javasupport.interaction.MethodDeclarations;
import se.claremont.autotest.javasupport.interaction.MethodInvoker;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class RecordingKeyboardListener implements FocusListener {
    Component lastComponentInteractedUpon = null;
    java.util.List<Integer> keyCodesUsedUponComponent = new LinkedList<>();
    String keysPressedUponComponent = "";
    TafHtmlTextPane scriptArea;
    KeyBoardRecorder recorder = new KeyBoardRecorder();
    JavaGuiElement actualComponent;
    String initialText = null;

    public RecordingKeyboardListener(TafHtmlTextPane scriptArea) {
        this.scriptArea = scriptArea;
    }

    @Override
    public void focusGained(FocusEvent e) {
        initialText = (String)MethodInvoker.invokeTheFirstEncounteredMethodFromListOfMethodNames(
                e.getComponent(),
                MethodDeclarations.textGettingMethodsInAttemptOrder
        );
        actualComponent = new JavaGuiElement(e.getComponent());
        e.getComponent().addKeyListener(recorder);
    }

    @Override
    public void focusLost(FocusEvent e) {
        Object actualText = (String)MethodInvoker.invokeTheFirstEncounteredMethodFromListOfMethodNames(e.getComponent(), MethodDeclarations.textGettingMethodsInAttemptOrder);
        if(actualText == null || (initialText != null && initialText.equals(actualText))) return;
        Gui.availableTestSteps.add(new JavaWriteTestStep(new JavaGuiElement(e.getComponent()), keysPressedUponComponent));
        if (e.getComponent() == null) return;
        String text = "<pre>java.write(new JavaGuiElement(By.byName(\"" + actualComponent.getName() + "\")), \"" + keysPressedUponComponent + "\");</pre><br>" + System.lineSeparator();
        scriptArea.append(text);
        scriptArea.revalidate();
        scriptArea.repaint();
        e.getComponent().removeKeyListener(recorder);
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
