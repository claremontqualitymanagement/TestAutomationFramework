package se.claremont.taf.core.gui.plugins;

import se.claremont.taf.core.gui.abouttab.AboutTabPanel;
import se.claremont.taf.core.gui.createtesttab.CreateTestTabPanel;
import se.claremont.taf.core.gui.runtab.RunTestTabPanel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class PluginLoader {
    static List<IGuiTab> panels = new ArrayList<>();
    static Set<Class<?>> classesDerivedFromIGuiTabInterface = new HashSet<>();
    static String[] possibleGuiPluginClasses = {
            "se.claremont.autotest.javasupport.gui.JavaSupportTab",
            "se.claremont.autotest.restsupport.gui.RestSupportTabPanel",
            "se.claremont.autotest.websupport.gui.WebSupportTabPanel",
            "se.claremont.autotest.eyeautomatesupport.gui.SmartImageRecognitionSupportTabPanel"
    };

    public static List<IGuiTab> identifyGuiTabs() {
        ClassLoader myCL = Thread.currentThread().getContextClassLoader();
        while (myCL != null) {
            //System.out.println("ClassLoader: " + myCL);
            try {
                for (Iterator iter = list(myCL); iter.hasNext(); ) {
                    addToTabClassesIfApplicable((Class<?>) iter.next());
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            myCL = myCL.getParent();
        }
        addKnownPotentialTabClasses();
        loadPanelObjectFromTabClasses();
        return panels;
    }

    private static void addKnownPotentialTabClasses() {
        for (String pluginGuiClassName : possibleGuiPluginClasses) {
            //System.out.println("Attempting to load plugin '" + pluginGuiClassName + "'.");
            IGuiTab panel = tryGetTab(pluginGuiClassName);
            if (panel == null) continue;
            //System.out.println("Loaded panel '" + panel.getName() + "'.");
            panels.add(panel);
        }
    }

    private static IGuiTab tryGetTab(String pluginClassName) {
        Class panel = null;
        try {
            panel = PluginLoader.class.getClassLoader().loadClass(pluginClassName);
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }
        if (panel == null) return null;
        try {
            if (IGuiTab.class.isAssignableFrom(panel)) {
                IGuiTab returnPanel = (IGuiTab) panel.newInstance();
                return returnPanel;
            }
        } catch (InstantiationException e) {
            System.out.println(e.toString());
        } catch (IllegalAccessException e) {
            System.out.println(e.toString());
        }
        return null;
    }

    private static void loadPanelObjectFromTabClasses() {
        for (Class<?> klass : classesDerivedFromIGuiTabInterface) {
            try {
                IGuiTab tab = (IGuiTab) klass.newInstance();
                panels.add(tab);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static Iterator list(ClassLoader CL)
            throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        Class CL_class = CL.getClass();
        while (CL_class != java.lang.ClassLoader.class) {
            CL_class = CL_class.getSuperclass();
        }
        java.lang.reflect.Field ClassLoader_classes_field = CL_class
                .getDeclaredField("classes");
        ClassLoader_classes_field.setAccessible(true);
        Vector classes = (Vector) ClassLoader_classes_field.get(CL);
        return classes.iterator();
    }

    private static void addToTabClassesIfApplicable(Class<?> klass) {
        String className = klass.getName();
        if (IGuiTab.class.isAssignableFrom(klass) && !className.equals(IGuiTab.class.getName())) {
            if (className.equals(RunTestTabPanel.class.getName()) ||
                    className.equals(AboutTabPanel.class.getName()) ||
                    className.equals(CreateTestTabPanel.class.getName())) {
                //System.out.println("Class '" + className + "' is identified as TAF GUI tab but not loaded as plugin since it is loaded with special attention to order.");
                return;
            }
            //System.out.println("Identified class '" + className + "' as TAF GUI plugin tab. Loading it.");
            classesDerivedFromIGuiTabInterface.add(klass);
        }

    }

    private Object loadClassToClassLoader(URL url) {
        Object returnObject = null;
        URLClassLoader classLoader
                = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class clazz = URLClassLoader.class;
        // Use reflection
        Method method = null;
        try {
            method = clazz.getDeclaredMethod("addURL", URL.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        method.setAccessible(true);
        try {
            returnObject = method.invoke(classLoader, url);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return returnObject;

    }

}
