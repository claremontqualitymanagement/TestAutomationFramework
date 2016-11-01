package se.claremont.autotest.guidriverpluginstructure.swingsupport;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jordam on 2016-09-18.
 */
@SuppressWarnings("WeakerAccess")
public class SwingWindow {
    Window window;
    Container rootContainer;
    SwingApplication swingApplication;

    public SwingWindow(SwingApplication application, String windowTitle){
        this.swingApplication = application;
        for(Frame frame : application.frame.getFrames()){
            if(frame.getTitle().equals(windowTitle)){
                window = new Window(frame);
                this.rootContainer = frame;
                break;
            }
        }
    }

    public ArrayList<Component> getChildren(){
        return getChildrenRecursive(rootContainer);
    }

    private ArrayList<Component> getChildrenRecursive(Container container){
        ArrayList<Component> componentList = new ArrayList<>();
        for(Component child : container.getComponents()){
            componentList.add(child);
            try{
                componentList.addAll(getChildrenRecursive((Container) child));
            }catch (Exception ignored){
                System.out.println("Could not containerize '" + child.toString() + "'.");
            }
        }
        return componentList;
    }

    //System.out.println(child.getClass().toString() + ", name: '" + child.getName() + "'");
    //if(child.getClass().getName().toLowerCase().contains("label")){
    //    JLabel label = (JLabel) child;
    //    System.out.println(label.getText());
    //}
    //System.out.println(child.toString());

}
