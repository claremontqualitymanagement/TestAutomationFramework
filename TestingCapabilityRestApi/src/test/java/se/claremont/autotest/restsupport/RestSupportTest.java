package se.claremont.autotest.restsupport;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import se.claremont.autotest.common.testset.TestSet;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RestSupportTest extends TestSet {
    @Rule
    public TestName currentTestName = new TestName();

    private MockWebServer mockServer;
    private RestSupport restSupport;
    private String url;

    @Before
    public void setup() throws IOException {
        startUpTestCase(currentTestName.getMethodName());
        mockServer = new MockWebServer();
        restSupport = new RestSupport(currentTestCase);

        mockServer.enqueue(new MockResponse().setBody("hello"));

        mockServer.start();

        URL serverUrl = mockServer.url("/").url();

        url = String.format("%s://%s:%s%s",
                serverUrl.getProtocol(),
                serverUrl.getHost(),
                serverUrl.getPort(),
                serverUrl.getPath());
    }

    @After
    public void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    public void test_responseBodyFromPostRequest() throws IOException, InterruptedException {
        String result = restSupport.responseBodyFromPostRequest(url, "application/json", "data");

        assertNotNull(result);
        assertEquals("hello", result);

        RecordedRequest request = mockServer.takeRequest();
        assertEquals("/", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void test_responseFromPostRequest() throws IOException, InterruptedException {
        RestResponse result = restSupport.responseFromPostRequest(url, "application/json", "data");

        assertNotNull(result);
        assertEquals("hello", result.body);
        assertEquals("200", result.responseCode);

        RecordedRequest request = mockServer.takeRequest();
        assertEquals("/", request.getPath());
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void test_responseBodyFromPutRequest() throws IOException, InterruptedException {
        String result = restSupport.responseBodyFromPutRequest(url, "application/json", "data");

        assertNotNull(result);
        assertEquals("hello", result);

        RecordedRequest request = mockServer.takeRequest();
        assertEquals("/", request.getPath());
        assertEquals("PUT", request.getMethod());
    }

    @Test
    public void test_responseFromPutRequest() throws IOException, InterruptedException {
        RestResponse result = restSupport.responseFromPutRequest(url, "application/json", "data");

        assertNotNull(result);
        assertEquals("hello", result.body);
        assertEquals("200", result.responseCode);

        RecordedRequest request = mockServer.takeRequest();
        assertEquals("/", request.getPath());
        assertEquals("PUT", request.getMethod());
    }

    @Test
    public void test_responseBodyFromDeleteRequest() throws IOException, InterruptedException {
        String result = restSupport.responseBodyFromDeleteRequest(url);

        assertNotNull(result);
        assertEquals("hello", result);

        RecordedRequest request = mockServer.takeRequest();
        assertEquals("/", request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void test_responseFromDeleteRequest() throws IOException, InterruptedException {
        RestResponse result = restSupport.responseFromDeleteRequest(url);

        assertNotNull(result);
        assertEquals("hello", result.body);
        assertEquals("200", result.responseCode);

        RecordedRequest request = mockServer.takeRequest();
        assertEquals("/", request.getPath());
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void test_responseBodyFromGetRequest() throws IOException, InterruptedException {
        String result = restSupport.responseBodyFromGetRequest(url);

        assertNotNull(result);
        assertEquals("hello", result);

        RecordedRequest request = mockServer.takeRequest();
        assertEquals("/", request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void test_responseCodeFromGetRequest() throws IOException, InterruptedException {
        String result = restSupport.responseCodeFromGetRequest(url);

        assertNotNull(result);
        assertEquals("200", result);

        RecordedRequest request = mockServer.takeRequest();
        assertEquals("/", request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void test_responseFromGetRequest() throws IOException, InterruptedException {
        RestResponse result = restSupport.responseFromGetRequest(url);

        assertNotNull(result);
        assertEquals("hello", result.body);
        assertEquals("200", result.responseCode);

        RecordedRequest request = mockServer.takeRequest();
        assertEquals("/", request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void test_isRespondingToGetRequest() throws IOException, InterruptedException {
        boolean result = restSupport.isRespondingToGetRequest(url);

        assertTrue(result);

        RecordedRequest request = mockServer.takeRequest();
        assertEquals("/", request.getPath());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void test_verifyServiceIsResponding() throws IOException, InterruptedException {
        try {
            restSupport.verifyServiceIsResponding(url);
        } catch(Exception e) {
            fail(e.getMessage());
        }

        RecordedRequest request = mockServer.takeRequest();
        assertEquals("/", request.getPath());
        assertEquals("GET", request.getMethod());
    }

}