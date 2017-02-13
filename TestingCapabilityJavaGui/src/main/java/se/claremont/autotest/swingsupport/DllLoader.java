package se.claremont.autotest.swingsupport;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import org.fest.swing.util.Platform;

/**
 * Created by jordam on 2016-11-14.
 */
public class DllLoader {

    public interface simpleDLL extends Library {
        simpleDLL INSTANCE = (simpleDLL) Native.loadLibrary(
                (Platform.isWindows() ? "simpleDLL" : "simpleDLLLinuxPort"), simpleDLL.class);
        // it's possible to check the platform on which program runs, for example purposes we assume that there's a linux port of the library (it's not attached to the downloadable project)
        byte giveVoidPtrGetChar(Pointer param); // char giveVoidPtrGetChar(void* param);
        int giveVoidPtrGetInt(Pointer param);   //int giveVoidPtrGetInt(void* param);
        int giveIntGetInt(int a);               // int giveIntGetInt(int a);
        void simpleCall();                      // void simpleCall();
    }

    public static void main(String[] args) {

        simpleDLL sdll = simpleDLL.INSTANCE;

        sdll.simpleCall();  // call of void function

        int a = 3;
        int result1 = sdll.giveIntGetInt(a);  // calling function with int parameter&result
        System.out.println("giveIntGetInt("+a+"): " + result1);

        String testStr = "ToBeOrNotToBe";
        Memory mTest = new Memory(testStr.length()+1);  // '+1' remember about extra byte for \0 character!
        mTest.setString(0, testStr);
        String testReturn = mTest.getString(0); // you can see that String got properly stored in Memory object
        System.out.println("String in Memory:"+testReturn);

        Memory intMem = new Memory(4);  // allocating space
        intMem.setInt(0, 666); // setting allocated memory to an integer
        Pointer intPointer = intMem.getPointer(0);

        int int1 = sdll.giveVoidPtrGetInt(Pointer.NULL); // passing null, getting default result
        System.out.println("giveVoidPtrGetInt(null):" + int1); // passing int stored in Memory object, getting it back
        int int2 = sdll.giveVoidPtrGetInt(intMem);
        //int int2 = sdll.giveVoidPtrGetInt(intPointer);  causes JVM crash, use memory object directly!
        System.out.println("giveVoidPtrGetInt(666):" + int2);

        byte char1 = sdll.giveVoidPtrGetChar(Pointer.NULL);  // passing null, getting default result
        byte char2 = sdll.giveVoidPtrGetChar(mTest);        // passing string stored in Memory object, getting first letter

        System.out.println("giveVoidPtrGetChar(null):" + (char)char1);
        System.out.println("giveVoidPtrGetChar('ToBeOrNotToBe'):" + (char)char2);

    }
}
