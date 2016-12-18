package se.claremont.autotest.guidriverpluginstructure.swingsupport;

import se.claremont.autotest.guidriverpluginstructure.GuiElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by jordam on 2016-09-18.
 */
public class SwingElement implements GuiElement {
    public String name = "";
    public SwingWindow swingWindow;
    public String recognitionString;
    public Class<?> awtClass;

    public SwingElement(SwingWindow swingWindow, String recognitionString, Class<?> awtClass){
        this.swingWindow = swingWindow;
        this.recognitionString = recognitionString;
        this.awtClass = awtClass;
    }


    public Point clickablePoint(){
        Component component = (Component)getElement();
        if(component == null) return null;
        Point location = component.getLocationOnScreen();
        return new Point(location.x + component.getWidth()/2, location.y + component.getHeight()/2);
    }

    public Object getElement(){
        double timeoutInMilliseconds = 5000;
        Object returnObject = null;
        double startTime = System.currentTimeMillis();
        while (returnObject == null && System.currentTimeMillis() - startTime < timeoutInMilliseconds){
            for(Component component : swingWindow.getChildren()){
                if(!component.getClass().equals(awtClass)) continue;
                if(awtClass.equals(JButton.class)){
                    try {
                        JButton button = (JButton) component;
                        if(button.getText().contains(recognitionString) || button.getName().contains(recognitionString)){
                            return button;
                        }
                    }catch (Exception ignored){}
                }
            }
        }
        return null;
    }

    public static class Button extends SwingElement{

        public Button(SwingWindow swingWindow, String visibleText){
            super(swingWindow, visibleText, JButton.class);
        }

        /*
        private JButton getElement(){
            for(Component component : swingWindow.getChildren()){
                if(!component.getClass().equals(JButton.class)) continue;
                try {
                    JButton button = (JButton) component;
                    if(button.getText().contains(recognitionString) || button.getName().contains(recognitionString)){
                        return button;
                    }
                }catch (Exception ignored){}
            }
            return null;
        }
*/
        public void click(){
            JButton button = (JButton)getElement();
            if(button == null) {
                System.out.println("Could not identify button '" + recognitionString + "'.");
                return;
            }
            Point point = button.getLocationOnScreen();
            Robot robot = null;
            try {
                robot = new Robot();
            } catch (AWTException e) {
                System.out.println(e.getMessage());
            }
            robot.setAutoWaitForIdle(true);
            Point clickablePoint = clickablePoint();
            robot.mouseMove(clickablePoint.x, clickablePoint.y);
            robot.mousePress(MouseEvent.BUTTON1_MASK);
            robot.mouseRelease(MouseEvent.BUTTON1_MASK);
            System.out.println("Clicked the '" + recognitionString + "' button.");
        }
    }
}
