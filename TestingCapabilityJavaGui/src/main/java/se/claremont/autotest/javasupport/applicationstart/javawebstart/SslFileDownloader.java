package se.claremont.autotest.javasupport.applicationstart.javawebstart;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Downloads files over SSL, over HTTP
 *
 * Created by p901dqj on 2017-01-17.
 */
public class SslFileDownloader {
    TestCase testCase;

    public SslFileDownloader(TestCase testCase){
        this.testCase = testCase;
    }

    public void downloadFileOverSSLAndPotentiallyBadCertificate(String url, String pathToSaveTo){
        log(LogLevel.DEBUG, "Attempting to download the resource at url '" + url + "' and save it to '" + pathToSaveTo + "'.");
        Response response = sslFileDownloader(url);
        File downloadedFile = new File(pathToSaveTo);
        BufferedSink sink = null;
        try {
            sink = Okio.buffer(Okio.sink(downloadedFile));
            sink.writeAll(response.body().source());
            sink.close();
        } catch (IOException e) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not save resource content to file. " + e.getMessage());
            e.printStackTrace();
            return;
        }
        log(LogLevel.EXECUTED, "Downloaded the resource at url '" + url + "' to file '" + pathToSaveTo + "'.");
    }

    public Response sslFileDownloader(String url){
        Response response = null;
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder clientBuilder = client.newBuilder();
        Request request = new Request.Builder().get().url(url).build();

        log(LogLevel.DEBUG, "Attempting to download resource from '" + url + "'.");

        boolean allowUntrusted = true;

        if (  allowUntrusted) {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    @SuppressWarnings("UnnecessaryLocalVariable") X509Certificate[] cArrr = new X509Certificate[0];
                    return cArrr;
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] chain,
                                               final String authType) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(final X509Certificate[] chain,
                                               final String authType) throws CertificateException {
                }
            }};

            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("SSL");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            try {
                sslContext.init(null, trustAllCerts, new SecureRandom());
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            clientBuilder.sslSocketFactory(sslContext.getSocketFactory());

            @SuppressWarnings("Convert2Lambda") HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            clientBuilder.hostnameVerifier( hostnameVerifier);
        }

        final Call call = clientBuilder.build().newCall(request);
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void log(LogLevel logLevel, String message){
        if(testCase == null){
            System.out.println(logLevel.toString() + ":" + message);
        }else{
            testCase.log(logLevel, message);
        }
    }

}
