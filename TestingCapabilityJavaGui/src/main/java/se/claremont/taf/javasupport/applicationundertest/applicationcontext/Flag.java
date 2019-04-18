package se.claremont.taf.javasupport.applicationundertest.applicationcontext;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.management.VMOption;
import com.sun.management.VMOption.Origin;

class Flag {
    @JsonIgnore private String name;
    @JsonIgnore private Object value;
    @JsonIgnore private Origin origin;
    @JsonIgnore private boolean writeable;
    @JsonIgnore private boolean external;

    private Flag(){} //For JSON parser

    Flag(String name, Object value, boolean writeable,
         boolean external, Origin origin) {
        this.name = name;
        this.value = value == null ? "" : value;
        this.origin = origin;
        this.writeable = writeable;
        this.external = external;
    }

    @JsonIgnore
    Object getValue() {
        return value;
    }

    @JsonIgnore
    boolean isWriteable() {
        return writeable;
    }

    @JsonIgnore
    boolean isExternal() {
        return external;
    }

    @JsonIgnore
    VMOption getVMOption() {
        return new VMOption(name, value.toString(), writeable, origin);
    }

    @JsonIgnore
    static Flag getFlag(String name) {
        String[] names = new String[1];
        names[0] = name;

        List<Flag> flags = getFlags(names, 1);
        if (flags.isEmpty()) {
            return null;
        } else {
            // flags should have only one element
            return flags.get(0);
        }
    }

    @JsonIgnore
    static List<Flag> getAllFlags() {
        int numFlags = getInternalFlagCount();

        // Get all internal flags with names = null
        return getFlags(null, numFlags);
    }

    @JsonIgnore
    private static List<Flag> getFlags(String[] names, int numFlags) {
        Flag[] flags = new Flag[numFlags];
        int count = getFlags(names, flags, numFlags);

        List<Flag> result = new ArrayList<Flag>();
        for (Flag f : flags) {
            if (f != null) {
                result.add(f);
            }
        }
        return result;
    }

    @JsonIgnore
    private static native String[] getAllFlagNames();

    // getFlags sets each element in the given flags array
    // with a Flag object only if the name is valid and the
    // type is support2ed. The flags array may contain null elements.
    @JsonIgnore
    private static native int getFlags(String[] names, Flag[] flags, int count);

    @JsonIgnore
    private static native int getInternalFlagCount();

    // These set* methods are synchronized on the class object
    // to avoid multiple threads updating the same flag at the same time.
    @JsonIgnore
    static synchronized native void setLongValue(String name, long value);

    @JsonIgnore
    static synchronized native void setBooleanValue(String name, boolean value);

    @JsonIgnore
    static synchronized native void setStringValue(String name, String value);

    static {
        initialize();
    }

    @JsonIgnore
    private static native void initialize();
}