package se.claremont.autotest.javasupport.interaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by jordam on 2017-02-08.
 */
@SuppressWarnings("WeakerAccess")
public class JavaAwtAppWithSomeSwingComponents extends Frame{

    JButton okButton = new JButton("Ok");
    Button cancelButton = new Button("Cancel");
    JTextField textField = new JTextField("Text field");
    TextField textField2 = new TextField("The text");
    Checkbox checkbox = new Checkbox("Checkbox awt");
    JCheckBox jCheckBox = new JCheckBox("Checkbox Swing");

    Panel panel = new Panel();

    public JavaAwtAppWithSomeSwingComponents(){
        okButton.setName("OkButton");
        textField2.setName("Textfield2");
        cancelButton.setName("Cancel");
        textField.setName("Text field");
        checkbox.setName("Checkbox awt");
        jCheckBox.setName("Checkbox swing");
        panel.add(textField);
        panel.add(textField2);
        panel.add(okButton);
        panel.add(checkbox);
        panel.add(jCheckBox);
        panel.add(cancelButton);
        this.add(panel);
        this.setTitle("Java test application");
        this.setSize(new Dimension(500, 200));
        this.doLayout();
        //this.show();
        this.addWindowListener(new WindowAdapter() {
                                   public void windowClosing(WindowEvent we) {
                                       removeAll();
                                       dispose();
                                       System.exit(0);
                                   }
                               }
        );


    }

    public static void main(String[] args){
        new JavaAwtAppWithSomeSwingComponents();
    }

}
