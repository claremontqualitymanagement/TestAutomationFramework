package se.claremont.autotest.common.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to start external programs from within tests.
 *
 * Created by jordam on 2016-09-18.
 */
@SuppressWarnings("WeakerAccess")
public class ApplicationManager {

    private final static Logger logger = LoggerFactory.getLogger( ApplicationManager.class );

    final TestCase testCase;
    BufferedReader output;
    String programName;
    Process process;
    List<String> arguments;

    public ApplicationManager(TestCase testCase){
        this.testCase = testCase;
    }

   public void startProgram(String program, List<String> arguments){
       programName = program;
       this.arguments = arguments;
       List<String> commands = new ArrayList<>();
       commands.add(program);
       commands.addAll(arguments);
        try {
            App app = new App(commands, testCase);
            (new Thread(app)).start();
            //testCase.log(LogLevel.EXECUTED, "Started program '" + String.join(" ", commands) + "'.");
        } catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Coult not start program '" + String.join(" ", commands) + "'. "  + e.getMessage());
        }
    }

    public void startProgramInSameJVM(String programAndArguments){
        testCase.log(LogLevel.DEBUG, "Attempting to start program '" + programAndArguments + "'.");
        try {
            process = Runtime.getRuntime().exec(programAndArguments);
            testCase.log(LogLevel.EXECUTED, "Started '" + programAndArguments + "' in same JVM.");
        } catch (IOException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not start program '" + programAndArguments + "' in same JVM. " + e.getMessage());
        }
    }

    public void startProgram(String programAndArguments){
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
            startProgram(program, arguments);
        } else {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Must state program name at least, to start program.");
            logger.debug( "Must state program name at least, to start a program." );
        }
    }

    public void killProgram(){
        testCase.log(LogLevel.DEBUG, "Closing process '" + process.toString() + "'.");
        process.destroyForcibly();
        if(process.isAlive()){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not close process '" + process.toString() + "'.");
        }else {
            testCase.log(LogLevel.EXECUTED, "Closed application process.");
        }

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
                //System.out.println(line.split(" ")[0]); //<-- Parse data here.
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
            if(process == null) return "";
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

    class App implements Runnable{
        final List<String> commands = new ArrayList<>();
        final TestCase testCase;
        Process process;

        public App(List<String> commands, TestCase testCase){
            this.commands.addAll(commands);
            this.testCase = testCase;
        }

        public Process getProcess(){
            return process;
        }

        public void run(){
            try {
                if(commands == null || commands.size() < 1) {
                    testCase.log(LogLevel.EXECUTION_PROBLEM, "Cannot start program with no name given.");
                    return;
                }
                process = new ProcessBuilder(commands).start();
                testCase.log(LogLevel.EXECUTED, "Started program '" + String.join(" ", commands) + "'.");
            } catch (IOException e) {
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Cannot start program '" + String.join(" ", commands) + "'. Message: " + e.getMessage());
            }
        }

    }
}
