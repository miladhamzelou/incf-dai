package org.incf.common.atlas.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XPathEvaluator {

	private Document doc;
	private XPath xPath;

	/**
	 * Converts an XML file to a DOM tree.
	 * 
	 * @param xmlFile
	 * @throws IOException
	 */
	public XPathEvaluator(String xmlFilename) throws IOException {
		this(new File(xmlFilename));
	}

	/**
	 * Converts an XML file to a DOM tree.
	 * 
	 * @param xmlFile
	 * @throws IOException
	 */
	public XPathEvaluator(File xmlFile) throws IOException {
		try {
			
			// convert file to dom
			DocumentBuilderFactory domFactory = 
					DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			doc = domFactory.newDocumentBuilder().parse(xmlFile);
			
			// get xPath for this dom
			xPath = XPathFactory.newInstance().newXPath();
			xPath.setNamespaceContext(new XPathNamespaceResolver(doc, true));
		} catch (SAXException e) {
			throw new IOException("Error in document parsing: "
					+ e.getMessage());
		} catch (ParserConfigurationException e) {
			throw new IOException("Error in configuring parser: "
					+ e.getMessage());
		}
	}

	/**
	 * Evaluates an XPath expression and returns a list of strings.
	 * 
	 * @param xPathExpression
	 * @return
	 * @throws IOException
	 */
	public List<String> evaluateXPath(String xPathExpression) 
			throws IOException {
//		XPath xPath = XPathFactory.newInstance().newXPath();
//		xPath.setNamespaceContext(new XPathNamespaceResolver(doc, true));
		List<String> values = new ArrayList<String>();
		try {
			NodeList nodeList = (NodeList) xPath.evaluate(
					xPathExpression, doc,
					XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				values.add(nodeList.item(i).getNodeValue());
			}
		} catch (XPathExpressionException e) {
			throw new IOException("Error evaluating XPath: " + e.getMessage());
		}
		return values;
	}
	
	public boolean isNodePresent(String xPathExpression) 
			throws IOException {
		try {
			NodeList nodeList = (NodeList) xPath.evaluate(
					xPathExpression, doc,
					XPathConstants.NODESET);
			if (nodeList.getLength() > 0) {
				return true;
			}
		} catch (XPathExpressionException e) {
			throw new IOException("Error evaluating XPath: " + e.getMessage());
		}
		return false;
	}
	
}
