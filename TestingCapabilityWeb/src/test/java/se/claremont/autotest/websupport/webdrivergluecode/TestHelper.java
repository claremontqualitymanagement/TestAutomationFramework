package se.claremont.autotest.websupport.webdrivergluecode;

import org.junit.Assume;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertNotNull;

@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public final class TestHelper {

    public static String getTestFileFromTestResourcesFolder(String fileName){
        URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);

        assertNotNull(url);
        File file = new File(url.getPath());
        Assume.assumeNotNull(file);
        return file.getAbsolutePath();
    }

}
