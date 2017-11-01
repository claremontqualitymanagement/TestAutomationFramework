package se.claremont.autotest.eyeautomatesupport;

import eyeautomate.ScriptRunner;
import org.junit.Assume;
import se.claremont.autotest.common.guidriverpluginstructure.GuiElement;
import se.claremont.autotest.common.logging.LogFolder;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.reporting.HtmlStyles;
import se.claremont.autotest.common.reporting.testcasereports.TestCaseLogReporterHtmlLogFile;
import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.javasupport.interaction.GenericInteractionMethods;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;

/**
 * Driver for image based GUI automation with smart image recognition. This enables any OS to be automated.
 * Uses library from the EyeAutomate tool from Auqtus. Cred and kudos to those guys for enabling this.
 *
 * Created by jordam on 2017-01-27.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class EyeAutomateDriver {
    public final ScriptRunner scriptRunner = new ScriptRunner(null);
    private final TestCase testCase;

    public EyeAutomateDriver(TestCase testCase){
        if(testCase == null) testCase = new TestCase(null, "EyeAutomateRunner");
        HtmlStyles.addStyleInfo(HtmlStyleElements.styles);
        this.testCase = testCase;
        addJarFileToClassPath(getTestFileFromTestResourcesFolder("EyeAutomate.jar"));
        testCase.log(LogLevel.INFO, "Started EyeAutomate driver.");
    }

    public void runScriptFile(String filePath){
        Log(LogLevel.DEBUG, "Attempting to run scriptFile '" + filePath + "'.");
        if(!Files.exists(Paths.get(filePath))){
            Log(LogLevel.EXECUTION_PROBLEM, "Attempting to run EyeAutomate script file '" + filePath + "', but it does not exist.");
            return;
        }
        boolean success = scriptRunner.runScript(filePath);
        if(success){
            Log(LogLevel.EXECUTED, "Executed EyeAutomate script file '" + filePath + "' successfully.");
        } else {
            Log(LogLevel.EXECUTION_PROBLEM, "Could not successfully execute EyeAutomate script file '" + filePath + "'.");
        }
        /*
        String script = SupportMethods.getFileContent(filePath);
        String[] scriptRows = script.split(System.lineSeparator());
        for(String scriptRow : scriptRows){
            if(scriptRow.trim().length() == 0) continue;
            scriptRow = scriptRow.replace("\"", "");
            String[] words = scriptRow.split(" ");
            ArrayList<String> validRowParts = new ArrayList<>();
            for(String word : words){
                if(word.trim().length() == 0) continue;
                validRowParts.add(word.trim());
            }
            executeScriptRow(validRowParts);
        }
        */
    }

    public boolean executeCommand(String commandLine){
        String[] scriptRowParts = commandLine.split(" ");
        ArrayList<String> validParts = new ArrayList<>();
        for(String part : scriptRowParts){
            if(part.trim().length() == 0) continue;
            validParts.add(part);
        }
        boolean success = executeCommand(validParts);
        return success;
    }

    public void wait(int seconds){
        try {
            Thread.sleep(seconds *1000);
            testCase.log(LogLevel.DEBUG, "Paused execution for " + seconds + "' seconds.");
        } catch (InterruptedException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not pause execution as expected.");
        }
    }

    private boolean executeCommand(ArrayList<String> scriptRowParts){
        Log(LogLevel.DEBUG, "Attempting to run EyeAutomate command '" + String.join(" ", scriptRowParts) + "'.");
        String commandString = "";
        String command = null;
        String imageFile;
        StringBuilder arguments = null;
        if(scriptRowParts.size() == 0) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "EyeAutomate command string was null. Cannot execute command.");
            return false;
        }
        if(scriptRowParts.size() > 0) {
            command = scriptRowParts.get(0);
            commandString += command + " ";
        }
        if(scriptRowParts.size() > 1) {
            imageFile = scriptRowParts.get(1);
            commandString += imageFile + " ";
        }
        if(scriptRowParts.size() > 2){
            for(int i = 3; i < scriptRowParts.size(); i++){
                arguments.append(scriptRowParts.get(i)).append(" ");
            }
            if(arguments != null) {
                arguments = new StringBuilder(arguments.toString().trim());
                commandString += arguments + " ";
            }
        }
        commandString = commandString.trim();

        boolean success = scriptRunner.runScript(command, commandString);
        if(success){
            testCase.log(LogLevel.EXECUTED, "EyeAutomate executed '" + commandString + "' command.");
        } else {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "EyeAutomate could not execute '" + commandString + "'.");
            GuiImageElement element = null;
            if(scriptRowParts.size() > 1){
                element = new GuiImageElement(scriptRowParts.get(1), scriptRowParts.get(1));
            }
            saveScreenshot(element);
            saveDesktopScreenshot();
            haltFurtherExecution();
        }
        return success;
    }

    private void executeScriptRow(ArrayList<String> scriptRowParts){
        String command = null;
        String imageFile = null;
        StringBuilder arguments = null;
        if(scriptRowParts.size() == 0) return;
        if(scriptRowParts.size() > 0) command = scriptRowParts.get(0);
        if(scriptRowParts.size() > 1) imageFile = scriptRowParts.get(1);
        if(scriptRowParts.size() > 2){
            for(int i = 3; i < scriptRowParts.size(); i++){
                arguments.append(scriptRowParts.get(i)).append(" ");
            }
            if(arguments != null)
                arguments = new StringBuilder(arguments.toString().trim());
        }
        if(command == null) {
            testCase.log(LogLevel.DEBUG, "EyeAutomate command string was null.");
            return;
        }
        switch (command.toLowerCase()){
            case "click":
                if(scriptRowParts.size() == 2){
                    click(new GuiImageElement(imageFile, imageFile));
                } else if (scriptRowParts.size() == 4){
                    click(new GuiImageElement(imageFile, imageFile), scriptRowParts.get(2), scriptRowParts.get(3));
                } else {
                    executeCommand(scriptRowParts);
                }
                break;
            case "write":
                if(scriptRowParts.size() > 2){
                    write(new GuiImageElement(imageFile, imageFile), arguments.toString());
                } else {
                    executeCommand(scriptRowParts);
                }
                break;
            default:
                executeCommand(scriptRowParts);
                break;
        }
    }

    public void click(GuiElement element){
        click(element, true);
    }

    public void click(GuiElement element, boolean performLogging){
        if(performLogging) verifyGuiElementFileExistance(element);
        GuiImageElement guiImageElement = (GuiImageElement) element;
        boolean success = scriptRunner.runScript("Click", "Click \"" + guiImageElement.getImageFilePath() + "\"", null);
        if(success && performLogging){
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Clicked the '" + guiImageElement.getName() + "' element ('" + guiImageElement.getImageFilePath() + "').",
                    "Clicked " + guiImageElementToHtml(guiImageElement));
        } else if(performLogging) {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTION_PROBLEM, "Could not click the '" + guiImageElement.getName() +
                            "' element located at '" + guiImageElement.getImageFilePath() + "'. The file was found, but the click could not be performed.",
                    "Could not click " + guiImageElementToHtml(guiImageElement));
            saveScreenshot(element);
            saveDesktopScreenshot();
            haltFurtherExecution();
        }
    }

    /**
     * Clicking the provided image, with an offset from the middle point.
     * @param element The element as base for click
     * @param offsetX The horizontal offset, positive numbers to the right
     * @param offsetY The vertical offset, positive numbers downwards
     */
    public void click(GuiElement element, int offsetX, int offsetY){
        verifyGuiElementFileExistance(element);
        GuiImageElement guiImageElement = (GuiImageElement) element;
        boolean success = scriptRunner.runScript("Click", "Click \"" + guiImageElement.getImageFilePath() + "\"", String.valueOf(offsetX) + " " + String.valueOf(offsetY));
        if(success){
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Clicked the '" + guiImageElement.getName() + "' element with offset X:" + String.valueOf(offsetX) + ", Y:" + String.valueOf(offsetY) + ".",
                    "Clicked with offset X:" + String.valueOf(offsetX) + ", Y:" + String.valueOf(offsetY) + " to " + guiImageElementToHtml(guiImageElement));
        } else {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not click with offset X:" + String.valueOf(offsetX) + ", Y:" + String.valueOf(offsetY) + " to " + guiImageElementToHtml(guiImageElement));
            saveScreenshot(element);
            saveDesktopScreenshot();
            haltFurtherExecution();
        }
    }

    public void click(GuiElement element, String offsetX, String offsetY){
        verifyGuiElementFileExistance(element);
        GuiImageElement guiImageElement = (GuiImageElement) element;
        boolean success = scriptRunner.runScript("Click", "Click \"" + guiImageElement.getImageFilePath() + "\"", offsetX + " " + offsetY);
        if(success){
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Clicked the '" + guiImageElement.getName() + "' element with offset X:" + String.valueOf(offsetX) + ", Y:" + String.valueOf(offsetY) + ".",
                    "Clicked with offset X:" + String.valueOf(offsetX) + ", Y:" + String.valueOf(offsetY) + " to " + guiImageElementToHtml(guiImageElement));
        } else {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not click with offset X:" + String.valueOf(offsetX) + ", Y:" + String.valueOf(offsetY) + " to " + guiImageElementToHtml(guiImageElement));
            saveScreenshot(element);
            saveDesktopScreenshot();
            haltFurtherExecution();
        }
    }

    public void write(GuiElement element, String text){
        verifyGuiElementFileExistance(element);
        GuiImageElement guiImageElement = (GuiImageElement) element;
        click(element);
        boolean success = scriptRunner.runScript("Write", "Write \"" + text + "\"", null);
        if(success){
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Wrote '" + text + "' to the '" + guiImageElement.getName() + "' element.",
                    "Wrote '" + text + "' to " + guiImageElementToHtml(guiImageElement));
        } else {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTION_PROBLEM, "Could not write '" + text + "' to the '" + guiImageElement.getName() + "' element (" + guiImageElement.getImageFilePath() + ").",
                    "Could not write '" + text + "' to " + guiImageElementToHtml(guiImageElement));
            saveScreenshot(element);
            saveDesktopScreenshot();
            haltFurtherExecution();
        }
    }

    public void moveMouseToElement(GuiElement element){
        verifyGuiElementFileExistance(element);
        GuiImageElement guiImageElement = (GuiImageElement) element;
        boolean success = scriptRunner.runScript("WaitMouseMove", "WaitMouseMove \"" + guiImageElement.getImageFilePath() + "\"", null);
        if(success){
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, "Moved mouse cursor to the '" + guiImageElement.getName() + "' element (" + guiImageElement.getImageFilePath() + ").",
                    "Moved mouse cursor to " + guiImageElementToHtml(guiImageElement));
        } else {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTION_PROBLEM, "Could not move the mouse cursor to '" + guiImageElement.getName() + "' element.",
                    "Could not move mouse cursor to " + guiImageElementToHtml(guiImageElement));
            saveScreenshot(element);
            saveDesktopScreenshot();
            haltFurtherExecution();
        }
    }

    public void verifyImage(GuiElement element){
        verifyGuiElementFileExistance(element);
        GuiImageElement guiImageElement = (GuiImageElement) element;
        boolean success = scriptRunner.runScript("WaitVerify", "WaitVerify \"" + guiImageElement.getImageFilePath() + "\"", null);
        if(success){
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_PASSED, "Found match for '" + guiImageElement.getName() + "' (" + guiImageElement.getImageFilePath() + ").",
                    "Found image match for " + guiImageElementToHtml(guiImageElement));
        } else {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_FAILED, "Could not find any match for '" + guiImageElement.getName() + "' element (" + guiImageElement.getImageFilePath() + ").",
                    "Could not find any match for " + guiImageElementToHtml(guiImageElement));
            saveScreenshot(element);
            saveDesktopScreenshot();
            haltFurtherExecution();
        }
    }

    public void exists(GuiElement element) {
        GuiImageElement guiImageElement = (GuiImageElement) element;
        boolean success = scriptRunner.callCommand("Find", new String[]{guiImageElement.getImageFilePath()}, null);
        if(success){
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, "Found match for '" + guiImageElement.getName() + "' (" + guiImageElement.getImageFilePath() + ").",
                    "Found image match for " + guiImageElementToHtml(guiImageElement));
        } else {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, "Could not find any match for '" + guiImageElement.getName() + "' element (" + guiImageElement.getImageFilePath() + ").",
                    "Could not find any match for " + guiImageElementToHtml(guiImageElement));
        }
    }

    private String guiImageElementToHtml(GuiImageElement guiImageElement){
        return "the " + guiImageElement.toHtml();
    }

    public void haltFurtherExecution(){
        testCase.writeProcessListDeviationsFromSystemStartToLog();
        testCase.report();
    }

    public void saveScreenshot(GuiElement element) {
        String filePath = LogFolder.testRunLogFolder + this.testCase.testName + TestRun.fileCounter + ".png";
        ++TestRun.fileCounter;
        if(element == null){
            saveDesktopScreenshot();
        } else {
            GuiImageElement guiImageElement = (GuiImageElement)element;

            if(guiImageElement.getImageFilePath().contains("http://") ||
                    guiImageElement.getImageFilePath().contains("https://"))
                return;

            CopyOption copyOption = StandardCopyOption.REPLACE_EXISTING;
            try {
                File file = new File(filePath);
                file.getParentFile().mkdirs();
                Files.copy(Paths.get(guiImageElement.getImageFilePath()), Paths.get(filePath), copyOption);
                testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.INFO, "Image of element in question copied from '" + guiImageElement.getImageFilePath() + "' to '" + filePath + "'.",
                        "Image of element<br><img src=\"file://" + filePath + "\" alt=\"Image " + filePath + "\">");
            } catch (IOException e) {
                testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTION_PROBLEM, "Could not copy image of element in question copied from '" + guiImageElement.getImageFilePath() + "' to '" + filePath + "'.",
                        "Could not copy image of element<br><img src=\"file://" + filePath.replace("\\", "/") + "\" alt=\"Image " + filePath + "\"><br>From '" + guiImageElement.getImageFilePath() + "' to '" + filePath + "'");
            }
        }
    }

    public void saveDesktopScreenshot() {
        try {
            GenericInteractionMethods e = new GenericInteractionMethods(this.testCase);
            e.takeScreenshot();
        } catch (Exception e) {
            this.testCase.log(LogLevel.DEBUG, "Could not take desktop screenshot: " + e.toString());
        }

    }

    public void Log(LogLevel logLevel, String message){
        if(testCase == null){
            System.out.println(new LogPost(logLevel, message).toString());
            return;
        }
        testCase.log(logLevel, message);
    }

    private void verifyGuiElementFileExistance(GuiElement guiElement){
        GuiImageElement guiImageElement = (GuiImageElement)guiElement;
        if(guiElement == null
                || guiImageElement == null
                || guiImageElement.getImageFilePath() == null
                || guiImageElement.getImageFilePath().trim().length() == 0
                || !Files.exists(Paths.get(guiImageElement.getImageFilePath()))){
            String name = "";
            try{
                name = guiImageElement.getName();
            }catch (Exception ignored){}
            String path = "";
            try{
                path = ((GuiImageElement) guiElement).getImageFilePath();
            }catch (Exception ignored) {}
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not identify the '" + name + "' element since its image file could not be found at '" + path + "'.");
            saveScreenshot(guiElement);
            saveDesktopScreenshot();
            haltFurtherExecution();
        } else {
            testCase.log(LogLevel.DEBUG, "Identified image file for element '" + guiImageElement.getName() + "' at '" + guiImageElement.getImageFilePath() + "'.");
        }

    }

    private static String getTestFileFromTestResourcesFolder(String fileName){
        URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
        assertNotNull("Could not identify file '" + fileName + "'", url);
        File file = new File(url.getPath());
        Assume.assumeNotNull(file);
        return file.getAbsolutePath();
    }

    @SuppressWarnings("unchecked")
    private void addURL(URL url) throws Exception {
        URLClassLoader classLoader
                = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class clazz= URLClassLoader.class;
        // Use reflection
        Method method= clazz.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(classLoader, url);
    }

    private void addJarFileToClassPath(String filePath){
        try {
            testCase.log(LogLevel.EXECUTED, "Adding file '" + filePath + "' to classpath.");
            addURL(new File(filePath).toURL());
        } catch (Exception e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not add file '" + filePath + "' to classpath. Error: " + e.getMessage());
        }
    }

}

