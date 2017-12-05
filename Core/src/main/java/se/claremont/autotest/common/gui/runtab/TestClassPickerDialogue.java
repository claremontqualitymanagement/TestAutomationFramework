package se.claremont.autotest.common.gui.runtab;

import org.junit.Test;
import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.common.testset.TestSet;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class TestClassPickerDialogue {

    TafFrame classPickerWindow;
    Set<Class> identifiedClasses = new HashSet<>();
    private JCheckBox showAllClassesWithTestsCheckbox = new JCheckBox("Show all classes with tests, not only TestSets");
    private DefaultListModel listModel = new DefaultListModel();
    private JList testClasses = new JList(listModel);
    private JScrollPane listScroller = new JScrollPane(testClasses);

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

        testClasses.setName("TestClassesList");
        testClasses.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        testClasses.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        testClasses.setVisibleRowCount(-1);
        testClasses.setFont(AppFont.getInstance());
        reloadTestClassList();

        listScroller.setName("TestClassesListPanel");

        showAllClassesWithTestsCheckbox.setFont(AppFont.getInstance());
        showAllClassesWithTestsCheckbox.setName("ShowAllTestsCheckbox");
        showAllClassesWithTestsCheckbox.setBackground(Gui.colorTheme.backgroundColor);
        showAllClassesWithTestsCheckbox.setForeground(Gui.colorTheme.textColor);
        showAllClassesWithTestsCheckbox.setSelected(false);
        showAllClassesWithTestsCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadTestClassList();
            }
        });

        TafButton fileChooserButton = new TafButton("Pick file...");
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
                    addClassesFromFileToIdentifiedClassesList(file.getPath());
                    reloadTestClassList();
                }
            }
        });

        TafCloseButton closeButton = new TafCloseButton(classPickerWindow);

        TafButton saveButton = new TafButton("Save");
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
                                .addComponent(listScroller)
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
                        .addComponent(listScroller)
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

    private void reloadTestClassList() {
        Object[] classes = getTestClassesList().toArray();
        listModel.clear();
        testClasses.setForeground(Color.black);
        testClasses.setFont(AppFont.getInstance());
        testClasses.setEnabled(true);
        if (classes.length == 0) {
            classes = new String[]{" < no classes with tests identified > "};
            testClasses.setFont(new Font(AppFont.getInstance().getFontName(), Font.ITALIC, AppFont.getInstance().getSize()));
            testClasses.setForeground(Color.gray);
            testClasses.setEnabled(false);
        }
        for (Object o : classes) {
            listModel.addElement(o);
        }

        int width = listScroller.getWidth();
        int height = listScroller.getHeight();
        if (height > Toolkit.getDefaultToolkit().getScreenSize().height) {
            height = 9 * Toolkit.getDefaultToolkit().getScreenSize().height / 10;
            listScroller.createVerticalScrollBar();
        }
        if (width > Toolkit.getDefaultToolkit().getScreenSize().width) {
            width = 9 * Toolkit.getDefaultToolkit().getScreenSize().width / 10;
            listScroller.createHorizontalScrollBar();
        }
        listScroller.setSize(width, height);
    }

    private Component getTestClassPickerDialogue() {
        return this.classPickerWindow;
    }

    private void updateClassList(){
        addClassesFromFileToIdentifiedClassesList(theFileNameOfCurrentExecutingFile());
        for (Class c : getClassesFromClassLoader()) {
            identifiedClasses.add(c);
        }
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
    private static void addURLToSystemClassLoader(URL url){
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

    private void addClassesFromFileToIdentifiedClassesList(String filePath){
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
                            Class klass = Class.forName (className, true, Thread.currentThread().getContextClassLoader());
                            //Class<?> klass = ClassLoader.getSystemClassLoader().loadClass(className);
                            //Class<?> klass = Class.forName(className);
                            identifiedClasses.add(klass);
                        }catch (NoClassDefFoundError ignored ){
                            System.out.println(ignored.toString());
                        }catch (Exception ignored) {
                            System.out.println(ignored.toString());
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Set<String> getTestClassesList(){
        if(identifiedClasses.size() == 0) updateClassList();
        Set<String> returnList = new HashSet<>();
        for(Class c : identifiedClasses){
            if (!showAllClassesWithTestsCheckbox.isSelected()){
                 if(!TestSet.class.isAssignableFrom(c)) continue;
                returnList.add(c.getName());
            } else {
                Method[] methods = null;
                try {
                    methods = c.getMethods();
                } catch (NoClassDefFoundError e) {
                    continue;
                }
                if (methods == null) continue;
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Test.class)) {
                        returnList.add(c.getName());
                        break;
                    }
                }
            }
        }
        return returnList;
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
