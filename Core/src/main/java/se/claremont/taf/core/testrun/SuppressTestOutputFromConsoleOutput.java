package se.claremont.taf.core.testrun;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Redirects output to temporary console for analysis in testing.
 *
 * Created by jordam on 2017-03-06.
 */
@SuppressWarnings("WeakerAccess")
public class SuppressTestOutputFromConsoleOutput {
    @SuppressWarnings("WeakerAccess")
    static PrintStream originalOutputChannel;
    @SuppressWarnings("WeakerAccess")
    static ByteArrayOutputStream testOutputChannel;

    public static void restoreOutputChannel(){
        System.setOut(originalOutputChannel);
    }

    private static void rememberOriginalOutputChannel(){
        if(originalOutputChannel == null) originalOutputChannel = System.out;
    }

    public static void redirectOutputChannel(){
        rememberOriginalOutputChannel();
        testOutputChannel = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOutputChannel));
    }

}
