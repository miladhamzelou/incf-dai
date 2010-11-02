package org.incf.atlas.central.xml;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * See tutorial: http://java.sun.com/webservices/jaxp/dist/1.1/docs/tutorial/.
 * 
 * @author drlittle, eross
 */
public class XmlUtilW3c {

	private DocumentBuilder docBuilder;
	private DocumentBuilderFactory docBuilderFactory;
	private Transformer transformer;
	private TransformerFactory transformerFactory;
	private XPath xpath;
	private XPathFactory xpathFactory;

	public XmlUtilW3c() {
		try {
			docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setNamespaceAware(true);
			docBuilder = docBuilderFactory.newDocumentBuilder();
			transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			
			// for pretty print
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			
			xpathFactory = XPathFactory.newInstance();
			xpath = xpathFactory.newXPath();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public XmlUtilW3c(XmlNameSpaceContext context) {
		this();
		xpath.setNamespaceContext(context);
	}

	@SuppressWarnings(value = "all")
	private static Log log = LogFactory.getLog(XmlUtilW3c.class);

	public static void main(String[] args) {
		try {
			XmlUtilW3c xu = new XmlUtilW3c();
			String docString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<A:v va=\"va\" xmlns:A=\"some_url\">"
					+ "  <A:x xa=\"xa1\">" 
					+ "    <y ya=\"ya1\">"
					+ "      <A:z za=\"za1\"/>" 
					+ "    </y>" 
					+ "    <y ya=\"ya2\">yaya</y>"
					+ "  </A:x>" 
					+ "  <A:x xa=\"xa2\">xaxa</A:x>"
					+ "  <A:x xa=\"xa3\">" 
					+ "    <y ya=\"ya3\">"
					+ "      <A:z za=\"za2\"/>" 
					+ "    </y>" 
					+ "    <y ya=\"ya4\">"
					+ "      <A:z za=\"za3\"/>" 
					+ "      <A:z za=\"za4\"/>" 
					+ "    </y>"
					+ "    <y ya=\"ya5\">yayayayaya</y>" 
					+ "  </A:x>" 
					+ "</A:v>";
			Document doc = xu.xmlStringToDoc(docString);

			// Xpath queries that return an arbitrary node. (Element, text node,
			// attribute)
			System.out.println("Queries for one node.");
			System.out.println("getFirstNode(/A:v/A:x/y, ...): "
					+ xu.getFirstNode("/A:v/A:x/y", doc, "A", "some_url"));
			System.out
					.println("getFirstNode(/A:v/A:x/y/text(), ...): "
							+ xu.getFirstNode("/A:v/A:x/y/text()", doc, "A",
									"some_url"));
			System.out.println("getFirstNode(/A:v/A:x/y/@ya, ...): "
					+ xu.getFirstNode("/A:v/A:x/y/@ya", doc, "A", "some_url"));

			// Xpath queries that return the name of an arbitrary node.
			// (Element,
			// text node, attribute)
			System.out.println("\nQueries for one node name.");
			System.out.println("getFirstNodeName(/A:v/A:x/y, ...): "
					+ xu.getFirstNodeName("/A:v/A:x/y", doc, "A", "some_url"));
			System.out.println("getFirstNodeName(/A:v/A:x/y/text(), ...): "
					+ xu.getFirstNodeName("/A:v/A:x/y/text()", doc, "A",
							"some_url"));
			System.out.println("getFirstNodeName(/A:v/A:x/y/@ya, ...): "
					+ xu.getFirstNodeName("/A:v/A:x/y/@ya", doc, "A",
							"some_url"));

			// Xpath queries that return the string content of the first
			// selected
			// text node.
			System.out
					.println("\nQueries for the string content of the first selected text node.");
			System.out.println("getFirstNodeValue(/A:v/A:x/y/text(), ...)): "
					+ xu.getFirstNodeValue("/A:v/A:x/y/text()", doc, "A",
							"some_url"));

			// Xpath queries that return the string content of the first
			// selected
			// attribute's value.
			System.out
					.println("\nQueries for the string content of the first selected attribute's "
							+ "value.");
			System.out.println("getFirstNodeValue(/A:v/A:x/y/@ya, ...): "
					+ xu.getFirstNodeValue("/A:v/A:x/y/@ya", doc, "A",
							"some_url"));

			// Xpath queries that return many nodes.
			System.out.println("\nQueries for many nodes.");
			NodeList nl = xu.getNodes("/A:v//*", doc, "A", "some_url");
			System.out.println("getNodes(/A:v//*, ...)");
			for (int i = 0; i < nl.getLength(); i++) {
				System.out.println("   " + nl.item(i));
			}
			nl = xu.getNodes("/A:v//@*", doc, "A", "some_url");
			System.out.println("getNodes(/A:v//@*, ...)");
			for (int i = 0; i < nl.getLength(); i++) {
				System.out.println("   " + nl.item(i));
			}
			nl = xu.getNodes("/A:v//text()", doc, "A", "some_url");
			System.out.println("getNodes(/A:v//text(), ...)");
			for (int i = 0; i < nl.getLength(); i++) {
				System.out.println("   " + nl.item(i));
			}

			// Xpath queries that return many node names.
			System.out.println("\nQueries for many nodes names.");
			List<String> names = xu.getNodeNames("/A:v//*", doc, "A",
					"some_url");
			System.out.println("getNodeNames(/A:v//*, ...)");
			for (int i = 0; i < names.size(); i++) {
				System.out.println("   " + names.get(i));
			}
			names = xu.getNodeNames("/A:v//@*", doc, "A", "some_url");
			System.out.println("getNodeNames(/A:v//@*, ...)");
			for (int i = 0; i < names.size(); i++) {
				System.out.println("   " + names.get(i));
			}
			names = xu.getNodeNames("/A:v//text()", doc, "A", "some_url");
			System.out.println("getNodeNames(/A:v//text(), ...)");
			for (int i = 0; i < names.size(); i++) {
				System.out.println("   " + names.get(i));
			}

			// Xpath queries that return many node values.
			System.out.println("\nQueries for many nodes values.");
			List<String> values = xu.getNodeValues("/A:v//*", doc, "A",
					"some_url");
			System.out.println("getNodeValues(/A:v//*, ...)");
			for (int i = 0; i < values.size(); i++) {
				System.out.println("   " + values.get(i));
			}
			values = xu.getNodeValues("/A:v//@*", doc, "A", "some_url");
			System.out.println("getNodeValues(/A:v//@*, ...)");
			for (int i = 0; i < values.size(); i++) {
				System.out.println("   " + values.get(i));
			}
			values = xu.getNodeValues("/A:v//y/@ya", doc, "A", "some_url");
			System.out.println("getNodeValues(/A:v//y/@ya, ...)");
			for (int i = 0; i < values.size(); i++) {
				System.out.println("   " + values.get(i));
			}
			values = xu.getNodeValues("/A:v//text()", doc, "A", "some_url");
			System.out.println("getNodeValues(/A:v//text(), ...)");
			for (int i = 0; i < values.size(); i++) {
				System.out.println("   " + values.get(i));
			}

			// Xpath query that operates on an element node.
			System.out.println("Xpath query that operates on an element node.");
			Node yNode = xu.getFirstNode("/A:v/A:x/y", doc, "A", "some_url");
			System.out.println("getFirstNode(\"A:z\", yNode): "
					+ xu.nodeToString(xu.getFirstNode("A:z", yNode, "A",
							"some_url")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public XPathExpression compile(String searchString) {
		try {
			return xpath.compile(searchString);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public XPathExpression compile(String searchString, String prefix,
			String uri) {
		try {
			XmlNameSpaceContext context = new XmlNameSpaceContext();
			context.addNamespace(prefix, uri);
			xpath.setNamespaceContext(context);
			return xpath.compile(searchString);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Document emptyDoc() {
		return docBuilder.newDocument();
	}

	public Node getFirstNode(String searchString, Node item) {
		try {
			Document doc = rootify(item);
			Node result = (Node) xpath.evaluate(searchString, doc,
					XPathConstants.NODE);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Node getFirstNode(XPathExpression xPathExp, Node item) {
		try {
			Document doc = rootify(item);
			Node result = (Node) xPathExp.evaluate(doc, XPathConstants.NODE);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Node getFirstNode(String searchString, Node item, String prefix,
			String uri) {
		try {
			XmlNameSpaceContext context = new XmlNameSpaceContext();
			context.addNamespace(prefix, uri);
			xpath.setNamespaceContext(context);
			return getFirstNode(searchString, item);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

//	public String getFirstNodeName(String searchString, Node item) {
//		try {
//			Document doc = rootify(item);
//			String result = ((Node) xpath.evaluate(searchString, doc,
//					XPathConstants.NODE)).getNodeName();
//			return result;
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}

	public String getFirstNodeName(String searchString, Node item) {
		try {
			Document doc = rootify(item);
			Node n = (Node) xpath.evaluate(searchString, doc,
					XPathConstants.NODE);
			String result = n.getNodeName();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getFirstNodeName(XPathExpression xPathExp, Node item) {
		try {
			Document doc = rootify(item);
			String result = ((Node) xPathExp.evaluate(doc, XPathConstants.NODE))
					.getNodeName();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getFirstNodeName(String searchString, Node item,
			String prefix, String uri) {
		try {
			XmlNameSpaceContext context = new XmlNameSpaceContext();
			context.addNamespace(prefix, uri);
			xpath.setNamespaceContext(context);
			return getFirstNodeName(searchString, item);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getFirstNodeValue(String searchString, Node item) {
		try {
			Document doc = rootify(item);
			String result = ((Node) xpath.evaluate(searchString, doc,
					XPathConstants.NODE)).getNodeValue();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getFirstNodeValue(XPathExpression xPathExp, Node item) {
		try {
			Document doc = rootify(item);
			String result = ((Node) xPathExp.evaluate(doc, XPathConstants.NODE))
					.getNodeValue();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getFirstNodeValue(String searchString, Node item,
			String prefix, String uri) {
		try {
			XmlNameSpaceContext context = new XmlNameSpaceContext();
			context.addNamespace(prefix, uri);
			xpath.setNamespaceContext(context);
			return getFirstNodeValue(searchString, item);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getNodeNames(String searchString, Node item) {
		try {
			Document doc = rootify(item);
			NodeList nl = (NodeList) xpath.evaluate(searchString, doc,
					XPathConstants.NODESET);
			List<String> result = new ArrayList<String>();
			for (int i = 0; i < nl.getLength(); i++) {
				result.add(nl.item(i).getNodeName());
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getNodeNames(XPathExpression xPathExp, Node item) {
		try {
			Document doc = rootify(item);
			NodeList nl = (NodeList) xPathExp.evaluate(doc,
					XPathConstants.NODESET);
			List<String> result = new ArrayList<String>();
			for (int i = 0; i < nl.getLength(); i++) {
				result.add(nl.item(i).getNodeName());
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getNodeNames(String searchString, Node item,
			String prefix, String uri) {
		try {
			XmlNameSpaceContext context = new XmlNameSpaceContext();
			context.addNamespace(prefix, uri);
			xpath.setNamespaceContext(context);
			return getNodeNames(searchString, item);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public NodeList getNodes(String searchString, Node item) {
		try {
			Document doc = rootify(item);
			NodeList result = (NodeList) xpath.evaluate(searchString, doc,
					XPathConstants.NODESET);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public NodeList getNodes(XPathExpression xPathExp, Node item) {
		try {
			Document doc = rootify(item);
			NodeList result = (NodeList) xPathExp.evaluate(doc,
					XPathConstants.NODESET);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public NodeList getNodes(String searchString, Node item, String prefix,
			String uri) {
		try {
			XmlNameSpaceContext context = new XmlNameSpaceContext();
			context.addNamespace(prefix, uri);
			xpath.setNamespaceContext(context);
			return getNodes(searchString, item);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getNodeValues(String searchString, Node item) {
		try {
			Document doc = rootify(item);
			NodeList nl = (NodeList) xpath.evaluate(searchString, doc,
					XPathConstants.NODESET);
			List<String> result = new ArrayList<String>();
			for (int i = 0; i < nl.getLength(); i++) {
				result.add(nl.item(i).getNodeValue());
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getNodeValues(XPathExpression xPathExp, Node item) {
		try {
			Document doc = rootify(item);
			NodeList nl = (NodeList) xPathExp.evaluate(doc,
					XPathConstants.NODESET);
			List<String> result = new ArrayList<String>();
			for (int i = 0; i < nl.getLength(); i++) {
				result.add(nl.item(i).getNodeValue());
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getNodeValues(String searchString, Node item,
			String prefix, String uri) {
		XmlNameSpaceContext context = new XmlNameSpaceContext();
		context.addNamespace(prefix, uri);
		xpath.setNamespaceContext(context);
		return getNodeValues(searchString, item);
	}

	public InputStream nodeToStream(Node item) {
		try {
			return new ByteArrayInputStream(nodeToString(item).getBytes());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Convert a w3c.dom.Document to a string.
	 * 
	 * @param item
	 * 
	 * @return
	 * 
	 * @throws RuntimeException
	 */
	public String nodeToString(Node item) {
		try {
			DOMSource source = new DOMSource(item);
			StreamResult result = new StreamResult(new StringWriter());
			transformer.transform(source, result);
			return ((StringWriter) result.getWriter()).toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void removeEmptyTextNodes(Node item) {
		NodeList children = item.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Text) {
				if ((child.getNodeValue() == null)
						|| child.getNodeValue().trim().equals("")) {
					item.removeChild(child);
					i--;
				}
			} else {
				removeEmptyTextNodes(child);
			}
		}
	}

	public Document rootify(Node item) {
		if (item instanceof Document) {
			return (Document) item;
		}
		if (item instanceof Element) {
			Document doc = docBuilder.newDocument();
			doc.appendChild(doc.importNode(item, true));
			return doc;
		} else {
			throw new RuntimeException("Submitted object cannot be rootified.");
		}
	}

	public String transform(ResourceBackUp xmlResource,
			ResourceBackUp xsltResource) {
		try {
			InputStream xmlResourceStream = xmlResource.getReadStream();
			InputStream xsltResourceStream = xsltResource.getReadStream();
			String result = transformedXml(xmlResourceStream,
					xsltResourceStream);
			xmlResourceStream.close();
			xsltResourceStream.close();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String transform(File xmlFile, File xsltFile) {
		try {
			FileInputStream xmlFileInputStream = new FileInputStream(xmlFile);
			FileInputStream xsltFileInputStream = new FileInputStream(xsltFile);
			BufferedInputStream xmlBufferedInputStream = new BufferedInputStream(
					xmlFileInputStream);
			BufferedInputStream xsltBufferedInputStream = new BufferedInputStream(
					xsltFileInputStream);
			String result = transformedXml(new BufferedInputStream(
					new FileInputStream(xmlFile)), new BufferedInputStream(
					new FileInputStream(xsltFile)));
			xmlFileInputStream.close();
			xsltFileInputStream.close();
			xmlBufferedInputStream.close();
			xsltBufferedInputStream.close();
			return result;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String transform(String xmlString, String xsltString) {
		try {
			return transformedXml(
					new ByteArrayInputStream(xmlString.getBytes()),
					new ByteArrayInputStream(xsltString.getBytes()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * If you use this method, you *must* close your streams in your own code.
	 */
	public String transform(InputStream xmlStream, InputStream xsltStream) {
		try {
			return transformedXml(xmlStream, xsltStream);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Build a w3c.dom.Document from an XML file.
	 * 
	 * @param filepath
	 * 
	 * @return
	 * 
	 * @throws RuntimeException
	 */
	public Document xmlFileToDoc(String xmlfilepath) {
		try {
			Document doc = docBuilder.parse(new File(xmlfilepath));
			removeEmptyTextNodes(doc);
			return doc;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Document xmlFileToDoc(File xmlfile) {
		try {
			Document doc = docBuilder.parse(xmlfile);
			removeEmptyTextNodes(doc);
			return doc;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Document xmlResourceToDoc(ResourceBackUp resource) {
		try {
			InputStream resourceStream = resource.getReadStream();
			Document doc = docBuilder.parse(resourceStream);
			resourceStream.close();
			removeEmptyTextNodes(doc);
			return doc;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * If you use this method, you *must* close your stream in your own code.
	 */
	public Document xmlStreamToDoc(InputStream stream) {
		try {
			Document doc = docBuilder.parse(stream);
			removeEmptyTextNodes(doc);
			return doc;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Document xmlStringToDoc(String xml) {
		try {
			Document doc = docBuilder.parse(new ByteArrayInputStream(xml
					.getBytes()));
			removeEmptyTextNodes(doc);
			return doc;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * If you use this method, you *must* close your streams in your own code;
	 */
	private String transformedXml(InputStream xmlStream, InputStream xsltStream) {
		try {
			
			// In case this is used inside tomcat 5.5.12.
			System.setProperty("javax.xml.transform.TransformerFactory",
					"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");

			// Construct an xsl transformer.
			StreamSource xsltSrc = new StreamSource(xsltStream);
			Transformer transformer = transformerFactory
					.newTransformer(xsltSrc);

			// Construct the input source.
			StreamSource inputSrc = new StreamSource(xmlStream);

			// Construct the output stream.
			StreamResult result = new StreamResult(new StringWriter());

			// Transform the input and return the output.
			transformer.transform(inputSrc, result);
			return (((StringWriter) result.getWriter()).toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
