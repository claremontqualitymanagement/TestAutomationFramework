package se.claremont.taf.javasupport.applicationundertest.applicationstarters.javawebstart;

import se.claremont.taf.logging.LogLevel;
import se.claremont.taf.testcase.TestCase;

import java.util.ArrayList;

/**
 * Helps start Java Web Start based Java programs. Downloads the Java Web Start
 * JNLP specification file, and downloads all dependency jars stated in that file.
 *
 * Created by p901dqj on 2017-02-24.
 */
public class JarDownloaderFromJnlp {
    TestCase testCase;
    String baseFilePath = System.getProperty("java.io.tmpdir") + "TAF_JNLP_temp";
    String baseUrl;

    public JarDownloaderFromJnlp(TestCase testCase, String baseUrl){
        this.testCase = testCase;
        this.baseUrl = baseUrl;
    }
    private void log(LogLevel logLevel, String message){
        if(testCase == null){
            System.out.println(logLevel.toString() + ":" + message);
        }else{
            testCase.log(logLevel, message);
        }
    }

    public void downloadResources() {
        JnlpFile jnlpFile = new JnlpFile(testCase);
        jnlpFile.getJnlpContent(baseUrl + "jnlp?app=calypsox.apps.startup.StartMainEntrySSL&config=default", "C:\\temp\\jnlp.jnlp");
        downloadAndSaveJarFiles(baseUrl, jnlpFile.jarFiles);
    }

    private void downloadAndSaveJarFiles(String baseUrl, ArrayList<String> jarFiles){
        long startTime = System.currentTimeMillis();
        for(String jarFile : jarFiles) downloadAndSaveJarFile(baseUrl, jarFile);
        log(LogLevel.DEBUG, "It took " + String.valueOf((System.currentTimeMillis()-startTime)/1000) + " seconds to download all jar files.");
    }

    private void downloadAndSaveJarFile(String baseUrl, String jarFile){
        long startTime = System.currentTimeMillis();
        SslFileDownloader sslFileDownloader = new SslFileDownloader(testCase);
        sslFileDownloader.downloadFileOverSSLAndPotentiallyBadCertificate(baseUrl + jarFile, baseFilePath + jarFile);
        long millis = (System.currentTimeMillis() - startTime);
        log(LogLevel.DEBUG, "It took " + String.valueOf(millis) + " milliseconds to download '" + baseUrl + jarFile + "' to '" + baseFilePath + jarFile + "'.");
    }
}
