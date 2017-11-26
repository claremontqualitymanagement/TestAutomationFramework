package se.claremont.autotest.common.gui.runtab;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class TestClassPickerDialogue {

    JFrame classPickerWindow;

    public TestClassPickerDialogue(Font appFont, RunTestTabPanel parent) {
        classPickerWindow = new JFrame();
        classPickerWindow.setName("ClassPickerWindow");
        classPickerWindow.setTitle("TAF - Test class picker");
        GroupLayout groupLayout = new GroupLayout(classPickerWindow.getContentPane());
        classPickerWindow.setLayout(groupLayout);
        classPickerWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container pane = classPickerWindow.getContentPane();
        JLabel headline = new JLabel("Pick your test classes");
        headline.setName("HeadlineLabel");
        headline.setFont(appFont);
        DefaultListModel listModel = new DefaultListModel();
        JList testClasses = new JList(listModel);
        testClasses.setName("TestClassesList");
        testClasses.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        testClasses.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        testClasses.setVisibleRowCount(-1);
        testClasses.setFont(appFont);
        try {
            Object[] classes = getLoadedClassesAndClassesInClassPath().toArray();
            if(classes.length == 0){
                classes = new String[] {" < no classes with tests identified > "};
                testClasses.setFont(new Font(appFont.getFontName(), Font.ITALIC, appFont.getSize()));
                testClasses.setForeground(Color.gray);
                testClasses.setEnabled(false);
            }
            for(Object o : classes){
                listModel.addElement(o);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JScrollPane listScroller = new JScrollPane(testClasses);
        listScroller.setName("TestClassesListPanel");
        //listScroller.setPreferredSize(new Dimension(250, 80));
        JButton closeButton = new JButton("Cancel");
        closeButton.setName("CloseButton");
        closeButton.setFont(appFont);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classPickerWindow.dispose();
            }
        });
        JButton saveButton = new JButton("Save");
        saveButton.setName("SaveButton");
        saveButton.setFont(appFont);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<String> selectedOptions = new ArrayList<>();;
                List<Object> selections = testClasses.getSelectedValuesList();
                for(Object o : selections){
                    RunTestTabPanel.chosenTestClasses.add((String)o);
                }
                parent.updateCliCommandText("");
                classPickerWindow.dispose();
            }
        });
        pane.add(headline);
        pane.add(listScroller);
        pane.add(closeButton);
        pane.add(saveButton);
        int width = listScroller.getWidth();
        int height = listScroller.getHeight();
        if(height > Toolkit.getDefaultToolkit().getScreenSize().height){
            height = 9* Toolkit.getDefaultToolkit().getScreenSize().height/10;
            listScroller.createVerticalScrollBar();
        }
        if(width > Toolkit.getDefaultToolkit().getScreenSize().width){
            width = 9* Toolkit.getDefaultToolkit().getScreenSize().width /10;
            listScroller.createHorizontalScrollBar();
        }
        listScroller.setSize(width, height);
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(listScroller)
                                .addComponent(saveButton)
                                .addComponent(closeButton)
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(listScroller)
                        .addComponent(saveButton)
                        .addComponent(closeButton)
        );
        classPickerWindow.pack();
        classPickerWindow.setVisible(true);

    }

    private Set<String> getLoadedClassesAndClassesInClassPath() throws IOException {
        java.util.List<String> jarFilesInClassPath = new ArrayList<>();
        jarFilesInClassPath.add(theFileNameOfCurrentExecutingFile());
        Set<String> classNamesForLoadedClasses = new HashSet<String>();
        for(Class c : getClassesFromClassLoader()){
            classNamesForLoadedClasses.add(c.getName());
        }
        try {
            List<URL> roots = Collections.list(ClassLoader.getSystemClassLoader().getResources(""));
            for (URL url : roots) {
                File root = new File(url.getPath());
                for (File file : root.listFiles()) {
                    if (file.isDirectory())continue;
                    if(file.getName().endsWith(".class") || file.getName().endsWith(".java")){
                        classNamesForLoadedClasses.add(file.getName());
                    }
                    if(file.getName().endsWith(".jar")){
                        jarFilesInClassPath.add(file.getName());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(String fileName : jarFilesInClassPath){
            if(fileName.endsWith("jar")){
                try{
                    ZipInputStream zip = new ZipInputStream(new FileInputStream(fileName));
                    for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                        if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                            String className = entry.getName().replace('/', '.'); // including ".class"
                            classNamesForLoadedClasses.add(className.substring(0, className.length() - ".class".length()));
                        }
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
        Set<String> classNamesForLoadedClassesWithJUnitTests = new HashSet<>();
        for(String className : classNamesForLoadedClasses){
            Method[] methods;
            try {
                Class c = ClassLoader.getSystemClassLoader().loadClass(className);
                methods = c.getMethods();
            } catch (NoClassDefFoundError e) {
                continue;
            } catch (ClassNotFoundException e) {
                continue;
            }
            if(methods == null) continue;
            for(Method method : methods){
                if(method.isAnnotationPresent(Test.class)){
                    classNamesForLoadedClassesWithJUnitTests.add(className);
                    break;
                }
            }
        }
        return classNamesForLoadedClassesWithJUnitTests;
    }

    private String theFileNameOfCurrentExecutingFile() {
        return new java.io.File(RunTestTabPanel.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getAbsolutePath();
    }

    private List<Class> getClassesFromClassLoader(){
        Field f = null;
        try {
            f = ClassLoader.class.getDeclaredField("classes");
            f.setAccessible(true);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            return (Vector<Class>) f.get(classLoader);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ArrayList<Class>();
    }

}
