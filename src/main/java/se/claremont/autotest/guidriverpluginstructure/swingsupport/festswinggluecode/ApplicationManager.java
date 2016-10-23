package se.claremont.autotest.guidriverpluginstructure.swingsupport.festswinggluecode;

import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.common.TestCaseLogReporter;
import se.claremont.autotest.support.SupportMethods;
import se.claremont.tools.Utils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jordam on 2016-09-18.
 */
public class ApplicationManager {

    TestCase testCase;
    Process process;
    BufferedReader output;
    String programName;
    List<String> arguments;

    public ApplicationManager(TestCase testCase){
        this.testCase = testCase;
    }

   public static ApplicationManager startProgram(String program, List<String> arguments, TestCase testCase){
       ApplicationManager applicationManager = new ApplicationManager(testCase);
       applicationManager.programName = program;
       applicationManager.arguments = arguments;
       List<String> commands = new ArrayList<>();
       Frame returnFrame = null;
       commands.add(program);
       commands.addAll(arguments);
        try {
            applicationManager.process = new ProcessBuilder(commands).start();
            System.out.println("Started program '" + String.join(" ", commands) + "'.");
            testCase.log(LogLevel.EXECUTED, "Started program '" + String.join(" ", commands) + "'.");
        } catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Coult not start program '" + String.join(" ", commands) + "'. "  + e.getMessage());
            System.out.println("Cannot start program '" + String.join(" ", commands) + "'. "  + e.getMessage());
        }
       return applicationManager;
    }

    public static ApplicationManager startProgram(String programAndArguments, TestCase testCase){
        ApplicationManager applicationManager = new ApplicationManager(testCase);
        String program;
        List<String> arguments = new ArrayList<>();
        String[] stringComponents = programAndArguments.split(" ");
        if(stringComponents.length > 0){
            program = stringComponents[0];
            if(stringComponents.length > 1){
                for(int i = 1; i< stringComponents.length; i++){
                    if(stringComponents[i].trim().length() > 0){
                        arguments.add(stringComponents[i]);
                    }
                }
            }
            applicationManager = startProgram(program, arguments, testCase);
        } else {
            applicationManager.testCase.log(LogLevel.EXECUTION_PROBLEM, "Must state program name at least, to start program.");
            System.out.println("Must state program name at least, to start a program.");
        }
        return applicationManager;
    }

    public List<String> listActiveRunningProcessesOnLocalMachine(){
        List<String> processes = new ArrayList<>();
        try {
            String line;
            Process p;
            if(Utils.getInstance().amIWindowsOS()){
                p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
            } else {
                p = Runtime.getRuntime().exec("ps -e");
            }
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                processes.add(line.split(" ")[0]);
                System.out.println(line.split(" ")[0]); //<-- Parse data here.
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return processes;
    }

    public String getApplicationOutput(){
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = input.readLine()) != null) {
                sb.append(line).append(SupportMethods.LF);
            }
            input.close();
        } catch (Exception err) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Problems while getting output from program. Output captured:" + SupportMethods.LF + sb.toString() + SupportMethods.LF + "Error message: " + err.getMessage());
            err.printStackTrace();
            return sb.toString() + "Error reading output stream: " + err.getMessage();
        }
        testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.INFO, "Output from application:" + SupportMethods.LF + sb.toString(), "Output from application:<br>" + SupportMethods.LF + sb.toString().replace(SupportMethods.LF, "<br>" + SupportMethods.LF));
        return sb.toString();
    }
}
