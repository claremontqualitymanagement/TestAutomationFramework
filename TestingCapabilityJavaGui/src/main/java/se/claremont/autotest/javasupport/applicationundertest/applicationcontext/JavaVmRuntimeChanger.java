package se.claremont.autotest.javasupport.applicationundertest.applicationcontext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;

import javax.management.ObjectName;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JavaVmRuntimeChanger implements HotSpotDiagnosticMXBean, Serializable {
    @JsonIgnore
    private transient TestCase testCase;
    @JsonProperty
    public List<String> appliedSetting = new ArrayList<>();

    private JavaVmRuntimeChanger() {//For JSON parsing to work
        this.testCase = new TestCase();
    }

    public JavaVmRuntimeChanger(TestCase testCase) {
        this.testCase = testCase;
    }

    @JsonIgnore
    public native void dumpHeap(String outputFile, boolean live) throws IOException;

    @JsonIgnore
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

    @JsonIgnore
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
     * @param name  The name of the parameter to attempt to set.
     * @param value The settings value to try to apply.
     */
    @JsonIgnore
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
        appliedSetting.add(name + "=" + value);
    }

    @JsonIgnore
    public ObjectName getObjectName() {
        return Util.newObjectName("com.sun.management:type=HotSpotDiagnostic");
    }

    @JsonIgnore
    private void log(LogLevel logLevel, String message) {
        if (testCase == null) {
            System.out.println(logLevel.toString() + ": " + message);
        } else {
            testCase.log(logLevel, message);
        }
    }

}













