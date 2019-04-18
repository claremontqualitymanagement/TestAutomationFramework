package se.claremont.taf.javasupport.applicationundertest.applicationstarters.javawebstart;

import se.claremont.taf.testcase.TestCase;
import se.claremont.taf.javasupport.applicationundertest.applicationstarters.ApplicationStartMechanism;

public class ApplicationStartMechanismJavaWebStart extends ApplicationStartMechanism {

    public ApplicationStartMechanismJavaWebStart(TestCase testCase, String url, String localFileCacheFolder) {
        super(testCase);
        JnlpFile jnlpFile = new JnlpFile(testCase);
        jnlpFile.getJnlpContent(url, localFileCacheFolder);
        mainClass = jnlpFile.applicationEntryPoint;
        startUrlOrPathToJarFile = jnlpFile.mainJarFile;
        JarDownloaderFromJnlp jarDownloaderFromJnlp = new JarDownloaderFromJnlp(testCase, url);
        jarDownloaderFromJnlp.downloadResources();
        JavaWebStartApplicationStarter javaWebStartApplicationStarter = new JavaWebStartApplicationStarter();
        javaWebStartApplicationStarter.startApplication(localFileCacheFolder, mainClass, new String[] {});
    }
}
