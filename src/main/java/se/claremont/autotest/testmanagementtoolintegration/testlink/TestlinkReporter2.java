package se.claremont.autotest.testmanagementtoolintegration.testlink;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.model.Platform;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import static br.eti.kinoshita.testlinkjavaapi.constants.TestLinkParams.DEV_KEY;

/**
 * Created by jordam on 2016-10-26.
 */
public class TestlinkReporter2 {
    TestLinkAPI api = null;
    Platform platform = null;


    public TestlinkReporter2(String devKey, String url) {
        URL testlinkURL = null;

        try {
            testlinkURL = new URL(url);
        } catch (MalformedURLException mue) {
            mue.printStackTrace(System.err);
            System.exit(-1);
        }

        try {
            api = new TestLinkAPI(testlinkURL, devKey);
        } catch (TestLinkAPIException te) {
            te.printStackTrace(System.err);
            System.exit(-1);
        }

        System.out.println(api.ping());
    }

    public void createPlatform(Integer projectId, String name, String notes) {
        platform = new Platform(projectId, name, notes);
    }

    public void createTestProject(String testProjectName, String testProjectPrefix, String notes) {
        TestProject project = null;

        try {
            project = api.createTestProject(
                    testProjectName, //testProjectName
                    testProjectPrefix, //testProjectPrefix
                    notes, //notes
                    true, //enableRequirements
                    true, //enableTestPriority
                    true, //enableAutomation
                    false, //enableInventory
                    true, //isActive
                    true); //isPublic
        } catch (TestLinkAPIException e) {
            e.printStackTrace(System.err);
            System.exit(-1);
        }
        System.out.println("Test project created! ");
        System.out.println("Test Project ID: [ " + project.getId() + " ].");
    }

    public static void doIt(String devKey, String url, int testProjectID, int testPlanID, String testCaseExternalID, int version) {
        try {
            XmlRpcClient rpcClient;
            XmlRpcClientConfigImpl config;

            config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(url));
            rpcClient = new XmlRpcClient();
            rpcClient.setConfig(config);

            ArrayList<Object> params = new ArrayList<Object>();
            Hashtable<String, Object> methodData = new Hashtable<String, Object>();
            methodData.put("devKey", devKey);
            methodData.put("testprojectid", testProjectID);
            methodData.put("testplanid", testPlanID);
            methodData.put("testcaseexternalid", testCaseExternalID);
            methodData.put("version", version);
            params.add(methodData);

            Object result = rpcClient.execute("tl.addTestCaseToTestPlan", params);
            // Typically you'd want to validate the result here and probably do something more useful with it
            System.out.println("Result was:\n");
            Map item = (Map) result;
            System.out.println("Keys: " + item.keySet().toString() + " values: " + item.values().toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
    }
}