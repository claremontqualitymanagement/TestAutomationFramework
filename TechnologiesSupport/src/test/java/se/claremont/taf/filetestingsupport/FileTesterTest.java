package se.claremont.taf.filetestingsupport;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testset.UnitTestClass;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FileTesterTest extends UnitTestClass{
    private FileTester fileTester;
    @Mock
    private TestCase testCase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        fileTester = new FileTester(testCase);
    }

    @Test
    public void test_verifyFileContentMatchesRegex_Match() {
        String filePath = this.getClass().getClassLoader().getResource("FileContent.txt").getPath();

        fileTester.verifyFileContentMatchesRegex(filePath, ".*The necessary sleeping time is always ten minutes longer.*");

        verify(testCase, times(1)).log(eq(LogLevel.DEBUG), anyString());
        verify(testCase, times(1)).log(eq(LogLevel.VERIFICATION_PASSED), anyString());
    }

    @Test
    public void test_verifyFileContentMatchesRegex_NoRegex() {
        String filePath = this.getClass().getClassLoader().getResource("FileContent.txt").getPath();

        fileTester.verifyFileContentMatchesRegex(filePath, null);

        verify(testCase, times(1)).log(eq(LogLevel.DEBUG), anyString());
        verify(testCase, times(1)).log(eq(LogLevel.VERIFICATION_FAILED), anyString());
        verify(testCase, times(1)).log(eq(LogLevel.DEVIATION_EXTRA_INFO), anyString());
    }

    @Test
    public void test_verifyFileContentMatchesRegex_FileNotExisting() {
        String filePath = this.getClass().getClassLoader().getResource("FileContent.txt").getPath();
        filePath = filePath.replaceAll("FileContent.txt", "NonExisting.txt");

        fileTester.verifyFileContentMatchesRegex(filePath, null);

        verify(testCase, times(1)).log(eq(LogLevel.VERIFICATION_PROBLEM), anyString());
    }

    @Test
    public void test_searchForSpecificFiles_StringPath() {
        String dirPath = this.getClass().getClassLoader().getResource(".").getPath();

        List<File> result = FileTester.searchForSpecificFiles(dirPath, Arrays.asList("NonExisting.txt", "FileContent.txt"));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("FileContent.txt", result.get(0).getName());
    }

    @Test
    public void test_searchForSpecificFiles_File() {
        String dirPath = this.getClass().getClassLoader().getResource("FileContent.txt").getPath();

        List<File> result = FileTester.searchForSpecificFiles(new File(dirPath), Collections.singletonList("FileContent.txt"));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("FileContent.txt", result.get(0).getName());
    }
}
