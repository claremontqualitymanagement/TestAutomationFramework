package se.claremont.autotest.javasupport.applicationundertest.applicationcontext;

import javax.management.ObjectName;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaVmRuntimeChanger implements HotSpotDiagnosticMXBean {
    @JsonIgnore TestCase testCase;

    public JavaVmRuntimeChanger(TestCase testCase) {
        this.testCase = testCase;
    }

    public native void dumpHeap(String outputFile, boolean live) throws IOException;

    public List<VMOption> getDiagnosticOptions() {
        List<Flag> allFlags = Flag.getAllFlags();
        List<VMOption> result = new ArrayList<VMOption>();
        for (Flag flag : allFlags) {
            if (flag.isWriteable() && flag.isExternal()) {
                result.add(flag.getVMOption());
            }
        }
        return result;
    }

    public VMOption getVMOption(String name) {
        if (name == null) {
            log(LogLevel.EXECUTION_PROBLEM, "Cannot get JVM option for null.");
            return null;
        }

        Flag f = Flag.getFlag(name);
        if (f == null) {
            log(LogLevel.EXECUTION_PROBLEM, "VM option '" + name + "' does not exist");
            return null;
        }
        return f.getVMOption();
    }

    /**
     * A few of the JVM settings that are settable from the command line
     * (usually with -X option) can be manipulated or added programatically.
     * This method attempts to do that.
     *
     * @param name The name of the parameter to attempt to set.
     * @param value The settings value to try to apply.
     */
    public void setVMOption(String name, String value) {
        if (name == null || name.length() == 0) {
            log(LogLevel.EXECUTION_PROBLEM, "JVM option name cannot be null or empty when setting option value.");
            return;
        }
        if (value == null) {
            log(LogLevel.EXECUTION_PROBLEM, "Cannot set JVM option value to null. You might want to try with an empty string.");
            return;
        }

        Util.checkControlAccess();
        Flag flag = Flag.getFlag(name);
        if (flag == null) {
            log(LogLevel.EXECUTION_PROBLEM, "Cannot set VM option '" + name + "' since it does not exist");
            return;
        }
        if (!flag.isWriteable()) {
            log(LogLevel.EXECUTION_PROBLEM, "Cannot set VM Option '" + name + "' to '" + value + "' since is read-only.");
            return;
        }

        // Check the type of the value
        Object v = flag.getValue();
        if (v instanceof Long) {
            try {
                long l = Long.parseLong(value);
                Flag.setLongValue(name, l);
            } catch (NumberFormatException e) {
                log(LogLevel.EXECUTION_PROBLEM, "Cannot set VM Option '" + name + "' to '" + value + "'. Invalid value:" +
                                " VM Option \"" + name + "\"" +
                                " expects numeric value. Error: " + e.toString());
                return;
            }
        } else if (v instanceof Boolean) {
            if (!value.equalsIgnoreCase("true") &&
                    !value.equalsIgnoreCase("false")) {
                log(LogLevel.EXECUTION_PROBLEM, "Cannot set VM Option. Invalid value:" +
                        " VM Option \"" + name + "\"" +
                        " expects \"true\" or \"false\".");
                return;
            }
            Flag.setBooleanValue(name, Boolean.parseBoolean(value));
        } else if (v instanceof String) {
            Flag.setStringValue(name, value);
        } else {
            log(LogLevel.FRAMEWORK_ERROR, "Cannot set VM Option '" + name + "' to '" + value + "' since it is of an unsupported type: " +
                    v.getClass().getName());
            return;
        }
        log(LogLevel.EXECUTED, "VM Option '" + name + "' successfully set to '" + value + "'.");
    }

    public ObjectName getObjectName() {
        return Util.newObjectName("com.sun.management:type=HotSpotDiagnostic");
    }

    private void log(LogLevel logLevel, String message){
        if(testCase == null){
            System.out.println(logLevel.toString() + ": " + message);
        } else {
            testCase.log(logLevel, message);
        }
    }

}













