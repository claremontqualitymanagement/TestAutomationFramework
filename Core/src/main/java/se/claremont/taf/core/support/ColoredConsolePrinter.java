package se.claremont.taf.core.support;

@SuppressWarnings("WeakerAccess")
public class ColoredConsolePrinter {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BOLD = "\u001B[1m";

    public static String green(String stringToColor){
        return ANSI_GREEN + stringToColor + ANSI_RESET;
    }

    public static String red(String stringToColor){
        return ANSI_RED + stringToColor + ANSI_RESET;
    }

    public static String yellow(String stringToColor){
        return ANSI_YELLOW + stringToColor + ANSI_RESET;
    }

    public static String cyan(String stringToColor){
        return ANSI_CYAN + stringToColor + ANSI_RESET;
    }

    public static String blue(String stringToColor){
        return ANSI_BLUE + stringToColor + ANSI_RESET;
    }

    public static String bold(String stringToFormat){
        return ANSI_BOLD + stringToFormat + ANSI_RESET;
    }

    public static String removeFormattingFromString(String instring){
        return instring
                .replace(ANSI_RESET, "")
                .replace(ANSI_BLUE, "")
                .replace(ANSI_BOLD, "")
                .replace(ANSI_CYAN, "")
                .replace(ANSI_GREEN, "")
                .replace(ANSI_RED, "")
                .replace(ANSI_YELLOW, "")
                .replace(ANSI_BLACK, "")
                .replace(ANSI_PURPLE, "")
                .replace(ANSI_WHITE, "");
    }

}
