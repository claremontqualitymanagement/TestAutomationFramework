package se.claremont.taf.restsupport;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;

public class XmlManager {
    String xml;
    TestCase testCase;

    public XmlManager(String xml, TestCase testCase){
        this.testCase = testCase;
        this.xml = xml;
    }

    public NodeList getObjectsByXPath(String xPath){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try{
            builder = factory.newDocumentBuilder();
        }catch (Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get started parsing XML for XPath '" + xPath + "'.");
            return null;
        }

        Document doc = null;
        try {
            doc = builder.parse(xml);
        } catch (SAXException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not parse XML. Error: " + e.getMessage() + "'." + System.lineSeparator() + xml);
            return null;
        } catch (IOException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not parse XML. Error: " + e.getMessage() + "'." + System.lineSeparator() + xml);
            return null;
        }
        XPath xPathInstance =  XPathFactory.newInstance().newXPath();
        NodeList nodeList = null;
        try {
            nodeList = (NodeList) xPathInstance.compile(xPath).evaluate(
                    doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not parse XPath '" + xPath + "'. Error: " + e.getMessage());
            return null;
        }
        return nodeList;
    }

    public String getObjectStringByXPath(String xPath){
        NodeList nodeList = getObjectsByXPath(xPath);
        if(nodeList == null)return null;
        return nodeList.toString();
    }
}
