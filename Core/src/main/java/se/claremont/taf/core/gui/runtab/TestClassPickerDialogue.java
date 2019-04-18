package se.claremont.taf.core.gui.runtab;

import org.junit.Ignore;
import org.junit.Test;
import se.claremont.taf.core.gui.guistyle.*;
import se.claremont.taf.core.testset.TestSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class TestClassPickerDialogue {

    TafFrame classPickerWindow;
    private TafCheckbox showAllClassesWithTestsCheckbox = new TafCheckbox("Show all classes with tests, not only TestSets");
    private ClassesList testClasses = new ClassesList();
    private JScrollPane listClassesListPane;// = new JScrollPane(testClasses);

    public TestClassPickerDialogue(RunTestTabPanel parent) {

        classPickerWindow = new TafFrame();
        classPickerWindow.setName("ClassPickerWindow");
        classPickerWindow.setTitle("TAF - Test class picker");
        GroupLayout groupLayout = new GroupLayout(classPickerWindow.getContentPane());
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        classPickerWindow.setLayout(groupLayout);
        classPickerWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container pane = classPickerWindow.getContentPane();
        pane.setName("TestClassPickerContentPanel");

        TafLabel headline = new TafLabel("Pick your test classes");

        listClassesListPane.setName("TestClassesListPanel");

        showAllClassesWithTestsCheckbox.setMnemonic('h');
        showAllClassesWithTestsCheckbox.setSelected(false);
        showAllClassesWithTestsCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testClasses.updateClassList();
                testClasses.reloadTestClassList();
            }
        });

        TafButton fileChooserButton = new TafButton("Pick file...");
        fileChooserButton.setMnemonic('p');
        fileChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser window = new JFileChooser();
                window.setName("FilePickerWindow");
                window.setDialogTitle("TAF - File picker");
                window.setFont(AppFont.getInstance());
                try {
                    window.setCurrentDirectory(new File(TestClassPickerDialogue.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
                int returnVal = window.showOpenDialog(getTestClassPickerDialogue());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = window.getSelectedFile();
                    testClasses.addClassesFromFileToIdentifiedClassesList(file.getPath());
                }
            }
        });

        TafCloseButton closeButton = new TafCloseButton(classPickerWindow);
        closeButton.setMnemonic('c');

        TafButton saveButton = new TafButton("Save");
        saveButton.setMnemonic('s');
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<String> selectedOptions = new ArrayList<>();
                ;
                List<Object> selections = testClasses.getSelectedValuesList();
                for (Object o : selections) {
                    RunTestTabPanel.chosenTestClasses.add((String) o);
                }
                parent.updateCliCommandText("");
                classPickerWindow.dispose();
            }
        });

        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(headline)
                                .addComponent(listClassesListPane)
                                .addComponent(showAllClassesWithTestsCheckbox)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(fileChooserButton)
                                        .addComponent(saveButton)
                                        .addComponent(closeButton)
                                )
                        )
        );

        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addComponent(listClassesListPane)
                        .addComponent(showAllClassesWithTestsCheckbox)
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(fileChooserButton)
                                .addComponent(saveButton)
                                .addComponent(closeButton)
                        )
        );

        classPickerWindow.pack();

        if(classPickerWindow.getHeight() < Toolkit.getDefaultToolkit().getScreenSize().height /2)
            classPickerWindow.setSize(classPickerWindow.getWidth(), Toolkit.getDefaultToolkit().getScreenSize().height/2);
        if(classPickerWindow.getWidth() < Toolkit.getDefaultToolkit().getScreenSize().width /2)
            classPickerWindow.setSize(Toolkit.getDefaultToolkit().getScreenSize().width /2, classPickerWindow.getHeight());
        if(classPickerWindow.getHeight() >= Toolkit.getDefaultToolkit().getScreenSize().height)
            classPickerWindow.setSize(classPickerWindow.getWidth(), Toolkit.getDefaultToolkit().getScreenSize().height * 4/5);
        if(classPickerWindow.getWidth() >= Toolkit.getDefaultToolkit().getScreenSize().width)
            classPickerWindow.setSize(Toolkit.getDefaultToolkit().getScreenSize().width * 4/5, classPickerWindow.getHeight());

        classPickerWindow.setVisible(true);

    }

    private Component getTestClassPickerDialogue() {
        return this.classPickerWindow;
    }




    private class ClassesList extends JList{

        private HashMap<String, List<String>> classes = new HashMap<>();
        private DefaultListModel listModel;
        Set<Class> identifiedClasses = new HashSet<>();

        public ClassesList(){
            this(new DefaultListModel());
        }

        private ClassesList(DefaultListModel listModel){
            super(listModel);
            this.listModel = listModel;
            setName("TestClassesList");
            setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            setLayoutOrientation(JList.HORIZONTAL_WRAP);
            setVisibleRowCount(-1);
            setFont(AppFont.getInstance());
            reloadTestClassList();
            ToolTipManager.sharedInstance().registerComponent(this);
        }

        public String getToolTipText(MouseEvent event){
            Point p = event.getPoint();
            int location = locationToIndex(p);
            String className = (String) getModel().getElementAt(location);
            return "<html><div style=\"font-size: " + AppFont.getInstance().getSize() + "\">Identified runnable test methods:</div><ul><li style=\"font-size: " + AppFont.getInstance().getSize() * 3/4 + "\">" + String.join("</li><li style=\"font-size: " + AppFont.getInstance().getSize() * 3/4 + "\">", classes.get(className)) + "</li></ul></html>";
        }

        public void reloadTestClassList() {
            listModel.clear();
            setForeground(Color.black);
            setFont(AppFont.getInstance());
            setEnabled(true);
            if (classes.size() == 0) {
                List<String> tooltipText = new ArrayList<>();
                tooltipText.add(" - Select a file with tests to populate this list. - ");
                classes.put(" < no classes with tests identified > ", tooltipText);
                setFont(new Font(AppFont.getInstance().getFontName(), Font.ITALIC, AppFont.getInstance().getSize()));
                setForeground(Color.gray);
                setEnabled(false);
            } else {
                if(classes.containsKey(" < no classes with tests identified > "))
                    classes.remove(" < no classes with tests identified > ");
            }
            for (String className : classes.keySet()) {
                listModel.addElement(className);
            }

            if(listClassesListPane == null) listClassesListPane = new JScrollPane(this);
            int width = listClassesListPane.getWidth();
            int height = listClassesListPane.getHeight();
            if (height > Toolkit.getDefaultToolkit().getScreenSize().height) {
                height = 9 * Toolkit.getDefaultToolkit().getScreenSize().height / 10;
                listClassesListPane.createVerticalScrollBar();
            }
            if (width > Toolkit.getDefaultToolkit().getScreenSize().width) {
                width = 9 * Toolkit.getDefaultToolkit().getScreenSize().width / 10;
                listClassesListPane.createHorizontalScrollBar();
            }
            listClassesListPane.setSize(width, height);
        }

        private void updateClassList(){
            //addClassesFromFileToIdentifiedClassesList(theFileNameOfCurrentExecutingFile());
            //for (Class c : getClassesFromClassLoader()) {
            //    identifiedClasses.add(c);
            //}
            try {
                List<URL> roots = Collections.list(ClassLoader.getSystemClassLoader().getResources(""));
                for (URL url : roots) {
                    File root = new File(url.getPath());
                    for (File file : root.listFiles()) {
                        addClassesFromFileToIdentifiedClassesList(file.getPath());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void addURLToSystemClassLoader(URL url){
            URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Class<URLClassLoader> classLoaderClass = URLClassLoader.class;

            try {
                Method method = classLoaderClass.getDeclaredMethod("addURL", new Class[]{URL.class});
                method.setAccessible(true);
                method.invoke(systemClassLoader, new Object[]{url});
            } catch (Throwable t) {
                System.out.println("Error when adding url to system ClassLoader ");
            }
        }

        public void addClassesFromFileToIdentifiedClassesList(String filePath){
            try {
                addURLToSystemClassLoader(new File(filePath).toURI().toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            File file = new File(filePath);
            if (file.isDirectory()) return;
            if (file.getName().endsWith(".class") || file.getName().endsWith(".java")) {
                try {
                    identifiedClasses.add(ClassLoader.getSystemClassLoader().loadClass(file.getAbsolutePath()));
                } catch (Exception ignored) {
                }
            } else if (filePath.endsWith("jar")) {
                try {
                    ZipInputStream zip = new ZipInputStream(new FileInputStream(filePath));
                    for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                        if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                            try {
                                String className = entry.getName().replace("/", ".").replace("\\", ".");
                                className = className.substring(0, className.length() - ".class".length() );
                                Class klass;
                                try{
                                    klass = Class.forName (className, true, Thread.currentThread().getContextClassLoader());
                                }catch (Throwable e){
                                    System.out.println(e);
                                    klass = Class.forName (className, false, Thread.currentThread().getContextClassLoader());
                                }
                                identifiedClasses.add(klass);
                            }catch (NoClassDefFoundError ignored ){
                            }catch (ClassNotFoundException ignored){
                            }catch (Throwable ignored) {
                                System.out.println(ignored.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            testClasses.getTestClassesList();
            testClasses.reloadTestClassList();
        }

        private List<String> getTestMethodsOfClass(Class<?> c){
            List<String> methodNames = new ArrayList<>();
            Method[] methods = null;
            try {
                methods = c.getMethods();
            } catch (NoClassDefFoundError ignored) { }
            if (methods == null) return methodNames;
            for (Method method : methods) {
                if (method.isAnnotationPresent(Test.class) && !method.isAnnotationPresent(Ignore.class)) {
                    //returnList.add(c.getName());
                    methodNames.add(method.getName());
                }
            }
            return methodNames;
        }

        private void getTestClassesList(){
            if(identifiedClasses.size() == 0) updateClassList();
            //Set<String> returnList = new HashSet<>();
            for(Class c : identifiedClasses){
                String className = null;
                List<String> methodNames = new ArrayList<>();
                if (!showAllClassesWithTestsCheckbox.isSelected()){
                    if(!TestSet.class.isAssignableFrom(c)) continue;
                    className = c.getName();
                    methodNames = getTestMethodsOfClass(c);
                    //returnList.add(c.getName());
                } else {
                    methodNames = getTestMethodsOfClass(c);
                    if(methodNames.size() > 0){
                        className = c.getName();
                    }
                }
                if(className != null)
                    classes.put(className, methodNames);
            }
        }

        private String theFileNameOfCurrentExecutingFile() {
            return new java.io.File(RunTestTabPanel.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getAbsolutePath();
        }

        private List<Class> getClassesFromClassLoader() {
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



}
