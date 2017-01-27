package se.claremont.autotest.eyeautomatesupport;

import eyeautomate.ScriptRunner;
import se.claremont.autotest.common.guidriverpluginstructure.GuiElement;
import se.claremont.autotest.common.logging.LogFolder;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.swingsupport.robotswinggluecode.RobotSwingInteractionMethods;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 * Created by jordam on 2017-01-27.
 */
public class EyeAutomateDriver {
    public ScriptRunner scriptRunner = new ScriptRunner(null);
    TestCase testCase;

    public EyeAutomateDriver(TestCase testCase){
        if(testCase == null){
            this.testCase = new TestCase(null, "EyeAutomateRunner");
        } else {
            this.testCase = testCase;
        }
    }

    public void runScriptFile(String filePath){
        scriptRunner.runScript(filePath);
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

    public void executeCommand(String commandLine){
        String[] scriptRowParts = commandLine.split(" ");
        ArrayList<String> validParts = new ArrayList<>();
        for(String part : scriptRowParts){
            if(part.trim().length() == 0) continue;
            validParts.add(part);
        }
        executeCommand(validParts);
    }

    public void wait(int seconds){
        try {
            Thread.sleep(seconds *1000);
            testCase.log(LogLevel.DEBUG, "Paused execution for " + seconds + "' seconds.");
        } catch (InterruptedException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not pause execution as expected.");
        }
    }

    private void executeCommand(ArrayList<String> scriptRowParts){
        String commandString = String.join(" ",scriptRowParts);
        String command = null;
        testCase.log(LogLevel.DEBUG, "Executing command '" + String.join(" ", scriptRowParts) + "'.");
        boolean success = scriptRunner.runScript(command, commandString);
        if(success){
            testCase.log(LogLevel.EXECUTED, command + ": " + commandString);
        } else {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Couldn't execute '" + commandString + "'.");
            GuiImageElement element = null;
            if(scriptRowParts.size() > 1){
                element = new GuiImageElement(scriptRowParts.get(1), scriptRowParts.get(1));
            }
            saveScreenshot(element);
            saveDesktopScreenshot();
            haltFurtherExecution();
        }
    }

    private void executeScriptRow(ArrayList<String> scriptRowParts){
        String command = null;
        String imageFile = null;
        String arguments = null;
        if(scriptRowParts.size() == 0) return;
        if(scriptRowParts.size() > 0) command = scriptRowParts.get(0);
        if(scriptRowParts.size() > 1) imageFile = scriptRowParts.get(1);
        if(scriptRowParts.size() > 2){
            for(int i = 3; i < scriptRowParts.size(); i++){
                arguments += scriptRowParts.get(i) + " ";
            }
            arguments = arguments.trim();
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
                    write(new GuiImageElement(imageFile, imageFile), arguments);
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
        GuiImageElement guiImageElement = (GuiImageElement) element;
        boolean success = scriptRunner.runScript("Click", "Click \"" + guiImageElement.getImageFilePath() + "\"", null);
        if(success){
            testCase.log(LogLevel.EXECUTED, "Clicked the '" + guiImageElement.getName() + "' element.");
        } else {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not click the '" + guiImageElement.getName() + "' element.");
            saveScreenshot(element);
            saveDesktopScreenshot();
            haltFurtherExecution();
        }
    }

    public void click(GuiElement element, int offsetX, int offsetY){
        GuiImageElement guiImageElement = (GuiImageElement) element;
        boolean success = scriptRunner.runScript("Click", "Click \"" + guiImageElement.getImageFilePath() + "\"", String.valueOf(offsetX) + " " + String.valueOf(offsetY));
        if(success){
            testCase.log(LogLevel.EXECUTED, "Clicked the '" + guiImageElement.getName() + "' element with offset X:" + String.valueOf(offsetX) + ", Y:" + String.valueOf(offsetY) + ".");
        } else {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not click the '" + guiImageElement.getName() + "' element with offset X:" + String.valueOf(offsetX) + ", Y:" + String.valueOf(offsetY) + ".");
            saveScreenshot(element);
            saveDesktopScreenshot();
            haltFurtherExecution();
        }
    }

    public void click(GuiElement element, String offsetX, String offsetY){
        GuiImageElement guiImageElement = (GuiImageElement) element;
        boolean success = scriptRunner.runScript("Click", "Click \"" + guiImageElement.getImageFilePath() + "\"", offsetX + " " + offsetY);
        if(success){
            testCase.log(LogLevel.EXECUTED, "Clicked the '" + guiImageElement.getName() + "' element with offset X:" + offsetX + ", Y:" + offsetY + ".");
        } else {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not click the '" + guiImageElement.getName() + "' element with offset X:" + offsetX + ", Y:" + offsetY + ".");
            saveScreenshot(element);
            saveDesktopScreenshot();
            haltFurtherExecution();
        }
    }

    public void write(GuiElement element, String text){
        GuiImageElement guiImageElement = (GuiImageElement) element;
        click(element);
        boolean success = scriptRunner.runScript("Write", "Write \"" + text + "\"", null);
        if(success){
            testCase.log(LogLevel.EXECUTED, "Wrote '" + text + "' to the '" + guiImageElement.getName() + "' element.");
        } else {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not write '" + text + "' to the '" + guiImageElement.getName() + "' element.");
            saveScreenshot(element);
            saveDesktopScreenshot();
            haltFurtherExecution();
        }
    }

    public void moveMouseToElement(GuiElement element){
        GuiImageElement guiImageElement = (GuiImageElement) element;
        boolean success = scriptRunner.runScript("WaitMouseMove", "WaitMouseMove \"" + guiImageElement.getImageFilePath() + "\"", null);
        if(success){
            testCase.log(LogLevel.DEBUG, "Moved mouse cursor to the '" + guiImageElement.getName() + "' element.");
        } else {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not move the mouse cursor to '" + guiImageElement.getName() + "' element.");
            saveScreenshot(element);
            saveDesktopScreenshot();
            haltFurtherExecution();
        }
    }

    public void verifyImage(GuiElement element){
        GuiImageElement guiImageElement = (GuiImageElement) element;
        boolean success = scriptRunner.runScript("WaitVerify", "WaitVerify \"" + guiImageElement.getImageFilePath() + "\"", null);
        if(success){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Found match for '" + guiImageElement.getName() + "'.");
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "Could not find any match for '" + guiImageElement.getName() + "'.");
            saveScreenshot(element);
            saveDesktopScreenshot();
            haltFurtherExecution();
        }
    }

    void haltFurtherExecution(){
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
            RobotSwingInteractionMethods e = new RobotSwingInteractionMethods(this.testCase);
            e.captureScreenshot();
        } catch (Exception e) {
            this.testCase.log(LogLevel.DEBUG, "Could not take desktop screenshot: " + e.toString());
        }

    }


    private void copyFile(String sourcePath, String targetPath){

    }

}