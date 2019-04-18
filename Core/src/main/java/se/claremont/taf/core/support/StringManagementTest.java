package se.claremont.taf.core.support;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Tests String manipulation methods
 *
 * Created by jordam on 2016-11-04.
 */
public class StringManagementTest extends UnitTestClass{

    @Test
    public void filePathToHtmlCheck(){
        Assert.assertTrue(StringManagement.filePathToHtmlSrc("C:\\Temp\\tempfile.txt"),
                StringManagement.filePathToHtmlSrc("C:\\Temp\\tempfile.txt").equals("file://C:/Temp/tempfile.txt"));
    }

    @Test
    public void enumToFriendlyString(){
        Assert.assertTrue(StringManagement.enumCapitalNameToFriendlyString("MY_STRANGE_ENUM_NAME").equals("My strange enum name"));
    }

    @Test
    public void osFilePathFromWinFormat(){
        String converted = StringManagement.filePathToCurrentOsAdaptedFormat("c:\\temp\\tempfile.txt");
        if(Utils.getInstance().amIWindowsOS()){
            Assert.assertTrue(converted.equals("c:\\temp\\tempfile.txt"));
        } else {
            Assert.assertTrue(converted.equals("c:/temp/tempfile.txt"));
        }

    }

    @Test
    public void htmlCodeToHtml(){
        String expected = "<pre>" + SupportMethods.LF + SupportMethods.LF +
                "&lt;html&gt;&lt;body&gt;Text &amp; more text&lt;/body&gt;&lt;/html&gt;" + SupportMethods.LF + SupportMethods.LF +
                "</pre>" + SupportMethods.LF;
        Assert.assertEquals(StringManagement.htmlContentToDisplayableHtmlCode("<html><body>Text & more text</body></html>"), StringManagement.htmlContentToDisplayableHtmlCode("<html><body>Text & more text</body></html>"), expected);
    }

    @Test
    public void filePathToWebFormat(){
        Assert.assertEquals(StringManagement.filePathToHtmlSrc("C:\\Temp\\myFile.txt"), "file://C:/Temp/myFile.txt");
        Assert.assertEquals(StringManagement.filePathToHtmlSrc("file://C:/Temp/myFile.txt"), "file://C:/Temp/myFile.txt");
    }

    @Test
    public void stringToCapitalInitialCharacterForEachWordAndNoSpacesTest(){
        String testString = " id think this woULd Set working agos";
        Assert.assertTrue("'" + StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(testString) + "'", StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(testString).equals("IdThinkThisWoULdSetWorkingAgos"));
    }


    @Test
    public void sentenziseString(){
        Assert.assertTrue(StringManagement.firstUpperLetterTrailingLowerLetter("id Love to be").equals("Id love to be"));
    }

    @Test
    public void osFilePathFromLinuxFormat(){
        String converted = StringManagement.filePathToCurrentOsAdaptedFormat("/temp/tempfile.txt");
        if(Utils.getInstance().amIWindowsOS()){
            Assert.assertTrue(converted.equals("\\temp\\tempfile.txt"));
        } else {
            Assert.assertTrue(converted.equals("/temp/tempfile.txt"));
        }

    }

    @Test
    public void osFilePathFromUncFormat(){
        String converted = StringManagement.filePathToCurrentOsAdaptedFormat("\\\\temp\\tempfile.txt");
        if(Utils.getInstance().amIWindowsOS()){
            Assert.assertTrue(converted.equals("\\\\temp\\tempfile.txt"));
        } else {
            Assert.assertTrue(converted.equals("\\\\temp\\tempfile.txt"));
        }

    }

    @Test
    public void generatedMethodName(){
        String attemptedMethodName = "1 Å-land will, as%. The$ cä'r & None! edCard_?";
        Assert.assertTrue(attemptedMethodName +
                        SupportMethods.LF +
                        StringManagement.methodNameWithOnlySafeCharacters(attemptedMethodName),
                StringManagement.
                methodNameWithOnlySafeCharacters(attemptedMethodName).
                equals("_1ALandWillAsProc_TheDollarCarAmpersandNoneEdcard"));
    }
}
