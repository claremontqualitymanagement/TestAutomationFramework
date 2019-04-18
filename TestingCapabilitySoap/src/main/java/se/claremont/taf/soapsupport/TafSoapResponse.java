package se.claremont.taf.soapsupport;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

/**
 * The request and response for a SOAP call
 */
@SuppressWarnings("WeakerAccess")
public class TafSoapResponse {

    private SOAPMessage requestSoapMessage;
    private SOAPMessage responseSoapMessage;
    private String requestBody;
    private String responseBody;
    private TestCase testCase;

    public TafSoapResponse(TestCase testCase) {
        if (testCase == null) testCase = new TestCase();
        this.testCase = testCase;
    }

    private static Document convertStringToXMLDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setRequestSoapMessage(SOAPMessage soapMessage) {
        this.requestSoapMessage = soapMessage;
    }

    public void setResponseSoapMessage(SOAPMessage soapMessage) {
        this.responseSoapMessage = soapMessage;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Node getFirstElementByXPath(String xPath) {
        NodeList nodes = getElementsByXPath(xPath);
        if (nodes.getLength() > 0) return nodes.item(0);
        return null;
    }

    public String getRequestBodyAsString() {
        return requestBody;
    }

    public String getResponseBodyAsString() {
        return responseBody;
    }

    public SOAPMessage getResponseSOAPMessage() {
        return responseSoapMessage;
    }

    public Document getResponseBodyAsXml() {
        return convertStringToXMLDocument(responseBody);
    }

    public NodeList getElementsByXPath(String xPath) {
        NodeList nodeList = null;
        XPath xPathBuilder = XPathFactory.newInstance().newXPath();
        try {
            nodeList = (NodeList) xPathBuilder.compile(xPath).evaluate(
                    getResponseBodyAsXml(), XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not use XPath '" + xPath + "': " + e.toString());
        }
        return nodeList;
    }

    public String toString() {
        return "[TafSoapResponse:" + System.lineSeparator()
                + "requestSoapMessageBody:" + System.lineSeparator()
                + "      " + requestBody.replace(System.lineSeparator(), "      " + System.lineSeparator()) + System.lineSeparator()
                + "responseSoapMessageBody:" + System.lineSeparator()
                + "      " + responseBody.replace(System.lineSeparator(), "      " + System.lineSeparator()) + System.lineSeparator()
                + "]";
    }

}
