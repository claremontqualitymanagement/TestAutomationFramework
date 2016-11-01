package se.claremont.autotest.guidriverpluginstructure.swingsupport.festswinggluecode;

import com.sun.javaws.Main;
import org.fest.swing.awt.AWT;
import org.fest.swing.fixture.FrameFixture;
import org.glassfish.jersey.jaxb.internal.XmlCollectionJaxbProvider;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.guidriverpluginstructure.GuiDriver;
import se.claremont.autotest.guidriverpluginstructure.GuiElement;
import se.claremont.autotest.guidriverpluginstructure.swingsupport.SwingElement;
import se.claremont.autotest.support.SupportMethods;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by jordam on 2016-09-18.
 */
public class SwingInteractionMethods implements GuiDriver{
    TestCase testCase;
    ApplicationManager applicationManager;
    //private FrameFixture app = new FrameFixture();

    public SwingInteractionMethods(TestCase testCase){
        this.testCase = testCase;
        //FrameFixture ff = new FrameFixture("Hej");
        //ff.maximize();
    }

    public void startProgram(String programStringAndArguments){
        applicationManager = new ApplicationManager(testCase);
        applicationManager.startProgram(programStringAndArguments);
    }

    public void closeApplication(){
        applicationManager.killProgram();
    }

    public Object loadClassInJar(String pathToJar, String pathToClass, String methodName, TestCase testCase){
        Object returnObject = null;
        URL url = null;
        try{
            url = new URL(pathToJar);
        }catch (Exception e){
            e.printStackTrace();
        }
        //URLClassLoader child = new URLClassLoader(new URL[]{url}, Main.class.getClassLoader());
        URLClassLoader child = new URLClassLoader(new URL[]{url}, this.getClass().getClassLoader());
        Class classToLoad = null;
        try {
            classToLoad = Class.forName (pathToClass, true, child);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method method = null;
        try {
            method = classToLoad.getDeclaredMethod (methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Object instance = null;
        try {
            instance = classToLoad.newInstance ();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            returnObject = method.invoke (instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return returnObject;
    }


    public void startProgramInSameJVM(String programStringAndArguments){
        applicationManager = new ApplicationManager(testCase);
        //applicationManager.startProgramInSameJVM(programStringAndArguments);
        Runtime.getRuntime().loadLibrary("QtpUsageAnalysis.jar");
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Window[] windows = AWT.ownerLessWindows();
        for(Window window : windows){
            System.out.println(window.toString());
        }
    }

    public void click(GuiElement guiElement){
        testCase.log(LogLevel.DEBUG, "Attempting to click the " + guiElement.name + " " + guiElement.getClass().getSimpleName());
        try {
            SwingElement.Button button = (SwingElement.Button) guiElement;
            button.click();
            testCase.log(LogLevel.EXECUTED, "Clicked the button.");
        }catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not click the button.");
        }
    }

    @Override
    public void write(GuiElement guiElement, String textToWrite) {

    }

    @Override
    public String getText(GuiElement guiElement) {
        return null;
    }

    @Override
    public boolean exists(GuiElement guiElement) {
        return false;
    }

    @Override
    public boolean existsWithTimeout(GuiElement guiElement, int timeOutInSeconds) {
        return false;
    }

    @Override
    public void verifyObjectExistence(GuiElement guiElement) {

    }

    @Override
    public void verifyObjectExistenceWithTimeout(GuiElement guiElement, int timeoutInSeconds) {

    }

    @Override
    public void selectInDropdown(GuiElement guiElement, String choice) {

    }

    @Override
    public boolean isDisplayed(GuiElement guiElement) {
        return false;
    }

    @Override
    public boolean isNotDisplayed(GuiElement guiElement) {
        return false;
    }

    @Override
    public void verifyObjectIsDisplayed(GuiElement guiElement) {

    }

    @Override
    public void verifyObjectIsNotDisplayed(GuiElement guiElement) {

    }

    @Override
    public void chooseRadioButton(GuiElement guiElement, String text) {

    }

    @Override
    public void verifyImage(GuiElement guiElement, String pathToOracleImage) {

    }
}
