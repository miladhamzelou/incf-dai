package org.incf.atlas.central.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.Content;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.EntityRef;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.XSLTransformer;
import org.jdom.xpath.XPath;

/**
 * See tutorial: http://java.sun.com/webservices/jaxp/dist/1.1/docs/tutorial/.
 * 
 * @author drlittle, eross
 */
public class XmlUtilJdom {

	private SAXBuilder docBuilder = new SAXBuilder();
	private XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat()
			.setLineSeparator("\n"));

	@SuppressWarnings(value = "all")
	private static Log log = LogFactory.getLog(XmlUtilJdom.class);

	// test/demo
	public static void main(String[] args) {
		try {
			XmlUtilJdom xu = new XmlUtilJdom();
			String docString = 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
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
				+ "     <A:z za=\"za2\"/>" 
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
			List<?> nl = xu.getNodes("/A:v//*", doc, "A", "some_url");
			System.out.println("getNodes(/A:v//*, ...)");
			for (Iterator<?> i = nl.iterator(); i.hasNext();) {
				System.out.println("   " + i.next());
			}
			nl = xu.getNodes("/A:v//@*", doc, "A", "some_url");
			System.out.println("getNodes(/A:v//@*, ...)");
			for (Iterator<?> i = nl.iterator(); i.hasNext();) {
				System.out.println("   " + i.next());
			}
			nl = xu.getNodes("/A:v//text()", doc, "A", "some_url");
			System.out.println("getNodes(/A:v//text(), ...)");
			for (Iterator<?> i = nl.iterator(); i.hasNext();) {
				System.out.println("   " + i.next());
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
			Object yNode = xu.getFirstNode("/A:v/A:x/y", doc, "A", "some_url");
			System.out.println("getFirstNode(\"A:z\", yNode): "
					+ xu.nodeToString(xu.getFirstNode("A:z", yNode, "A",
							"some_url")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public XPath compile(String searchString) {
		try {
			return XPath.newInstance(searchString);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public XPath compile(String searchString, String prefix, String uri) {
		try {
			XPath xPath = XPath.newInstance(searchString);
			xPath.addNamespace(prefix, uri);
			return xPath;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Document emptyDoc() {
		return new Document();
	}

	public Object getFirstNode(String searchString, Object item) {
		try {
			return XPath.selectSingleNode(item, searchString);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Object getFirstNode(XPath xPathExp, Object item) {
		try {
			return xPathExp.selectSingleNode(item);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Object getFirstNode(String searchString, Object item, String prefix,
			String uri) {
		try {
			XPath xPath = XPath.newInstance(searchString);
			xPath.addNamespace(prefix, uri);
			return xPath.selectSingleNode(item);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getFirstNodeName(String searchString, Object item) {
		try {
			Object o = XPath.selectSingleNode(item, searchString);
			if (o == null) {
				return null;
			} else if (o instanceof Attribute) {
				return ((Attribute) o).getQualifiedName();
			} else if (o instanceof Element) {
				return ((Element) o).getQualifiedName();
			} else if (o instanceof EntityRef) {
				return ((EntityRef) o).getName();
			} else {
				return "";
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getFirstNodeName(XPath xPathExp, Object item) {
		try {
			Object o = xPathExp.selectSingleNode(item);
			if (o == null) {
				return null;
			} else if (o instanceof Attribute) {
				return ((Attribute) o).getQualifiedName();
			} else if (o instanceof Element) {
				return ((Element) o).getQualifiedName();
			} else if (o instanceof EntityRef) {
				return ((EntityRef) o).getName();
			} else {
				return "";
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getFirstNodeName(String searchString, Object item,
			String prefix, String uri) {
		try {
			XPath xPath = XPath.newInstance(searchString);
			xPath.addNamespace(prefix, uri);
			Object o = xPath.selectSingleNode(item);
			if (o == null) {
				return null;
			} else if (o instanceof Attribute) {
				return ((Attribute) o).getQualifiedName();
			} else if (o instanceof Element) {
				return ((Element) o).getQualifiedName();
			} else if (o instanceof EntityRef) {
				return ((EntityRef) o).getName();
			} else {
				return "";
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getFirstNodeValue(String searchString, Object item) {
		try {
			Object o = XPath.selectSingleNode(item, searchString);
			if (o == null) {
				return null;
			} else if (o instanceof Attribute) {
				return ((Attribute) o).getValue();
			} else if (o instanceof Content) {
				return ((Content) o).getValue();
			} else {
				return o.toString();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getFirstNodeValue(XPath xPathExp, Object item) {
		try {
			Object o = xPathExp.selectSingleNode(item);
			if (o == null) {
				return null;
			} else if (o instanceof Attribute) {
				return ((Attribute) o).getValue();
			} else if (o instanceof Content) {
				return ((Content) o).getValue();
			} else {
				return o.toString();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getFirstNodeValue(String searchString, Object item,
			String prefix, String uri) {
		try {
			XPath xPath = XPath.newInstance(searchString);
			xPath.addNamespace(prefix, uri);
			Object o = xPath.selectSingleNode(item);
			if (o == null) {
				return null;
			} else if (o instanceof Attribute) {
				return ((Attribute) o).getValue();
			} else if (o instanceof Content) {
				return ((Content) o).getValue();
			} else {
				return o.toString();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getNodeNames(String searchString, Object item) {
		try {
			List<?> l = XPath.selectNodes(item, searchString);
			ArrayList<String> result = new ArrayList<String>();
			Object o;
			for (Iterator<?> i = l.iterator(); i.hasNext();) {
				o = i.next();
				if (o == null) {
					result.add(null);
				} else if (o instanceof Attribute) {
					result.add(((Attribute) o).getQualifiedName());
				} else if (o instanceof Element) {
					result.add(((Element) o).getQualifiedName());
				} else if (o instanceof EntityRef) {
					result.add(((EntityRef) o).getName());
				} else {
					result.add("");
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getNodeNames(XPath xPathExp, Object item) {
		try {
			List<?> l = xPathExp.selectNodes(item);
			ArrayList<String> result = new ArrayList<String>();
			Object o;
			for (Iterator<?> i = l.iterator(); i.hasNext();) {
				o = i.next();
				if (o == null) {
					result.add(null);
				} else if (o instanceof Attribute) {
					result.add(((Attribute) o).getQualifiedName());
				} else if (o instanceof Element) {
					result.add(((Element) o).getQualifiedName());
				} else if (o instanceof EntityRef) {
					result.add(((EntityRef) o).getName());
				} else {
					result.add("");
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getNodeNames(String searchString, Object item,
			String prefix, String uri) {
		try {
			XPath xPath = XPath.newInstance(searchString);
			xPath.addNamespace(prefix, uri);
			List<?> l = xPath.selectNodes(item);
			ArrayList<String> result = new ArrayList<String>();
			Object o;
			for (Iterator<?> i = l.iterator(); i.hasNext();) {
				o = i.next();
				if (o == null) {
					result.add(null);
				} else if (o instanceof Attribute) {
					result.add(((Attribute) o).getQualifiedName());
				} else if (o instanceof Element) {
					result.add(((Element) o).getQualifiedName());
				} else if (o instanceof EntityRef) {
					result.add(((EntityRef) o).getName());
				} else {
					result.add("");
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<?> getNodes(String searchString, Object item) {
		try {
			return XPath.selectNodes(item, searchString);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<?> getNodes(XPath xPathExp, Object item) {
		try {
			return xPathExp.selectNodes(item);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<?> getNodes(String searchString, Object item, String prefix,
			String uri) {
		try {
			XPath xPath = XPath.newInstance(searchString);
			xPath.addNamespace(prefix, uri);
			return xPath.selectNodes(item);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getNodeValues(String searchString, Object item) {
		try {
			List<?> l = XPath.selectNodes(item, searchString);
			ArrayList<String> result = new ArrayList<String>();
			Object o;
			for (Iterator<?> i = l.iterator(); i.hasNext();) {
				o = i.next();
				if (o == null) {
					result.add(null);
				} else if (o instanceof Attribute) {
					result.add(((Attribute) o).getValue());
				} else if (o instanceof Content) {
					result.add(((Content) o).getValue());
				} else {
					result.add(o.toString());
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getNodeValues(XPath xPathExp, Object item) {
		try {
			List<?> l = xPathExp.selectNodes(item);
			ArrayList<String> result = new ArrayList<String>();
			Object o;
			for (Iterator<?> i = l.iterator(); i.hasNext();) {
				o = i.next();
				if (o == null) {
					result.add(null);
				} else if (o instanceof Attribute) {
					result.add(((Attribute) o).getValue());
				} else if (o instanceof Content) {
					result.add(((Content) o).getValue());
				} else {
					result.add(o.toString());
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getNodeValues(String searchString, Object item,
			String prefix, String uri) {
		try {
			XPath xPath = XPath.newInstance(searchString);
			xPath.addNamespace(prefix, uri);
			List<?> l = xPath.selectNodes(item);
			ArrayList<String> result = new ArrayList<String>();
			Object o;
			for (Iterator<?> i = l.iterator(); i.hasNext();) {
				o = i.next();
				if (o == null) {
					result.add(null);
				} else if (o instanceof Attribute) {
					result.add(((Attribute) o).getValue());
				} else if (o instanceof Content) {
					result.add(((Content) o).getValue());
				} else {
					result.add(o.toString());
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String makePretty(String xml) {
		Document doc = xmlStringToDoc(xml);
		return outputter.outputString(doc);
	}

	public InputStream nodeToStream(Object item) {
		try {
			return new ByteArrayInputStream(nodeToString(item).getBytes());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String nodeToString(Object item) {
		try {
			if (item == null) {
				return null;
			} else if (item instanceof CDATA) {
				return outputter.outputString((CDATA) item);
			} else if (item instanceof Comment) {
				return outputter.outputString((Comment) item);
			} else if (item instanceof DocType) {
				return outputter.outputString((DocType) item);
			} else if (item instanceof Document) {
				return outputter.outputString((Document) item);
			} else if (item instanceof Element) {
				return outputter.outputString((Element) item);
			} else if (item instanceof EntityRef) {
				return outputter.outputString((EntityRef) item);
			} else if (item instanceof List) {
				return outputter.outputString((List<?>) item);
			} else if (item instanceof ProcessingInstruction) {
				return outputter
						.outputString((ProcessingInstruction) item);
			} else if (item instanceof Text) {
				return outputter.outputString((Text) item);
			} else {
				return item.toString();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String transform(File xmlFile, File xsltFile) {
		try {
			return transform(xmlFileToDoc(xmlFile),
					xmlFileToDoc(xsltFile));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String transform(String xmlString, String xsltString) {
		try {
			return transform(xmlStringToDoc(xmlString),
					xmlStringToDoc(xsltString));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * If you use this method, you must close your streams in your code.
	 */
	public String transform(InputStream xmlStream, InputStream xsltStream) {
		try {
			return transform(xmlStreamToDoc(xmlStream),
					xmlStreamToDoc(xsltStream));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String transform(Document xmlDoc, Document xsltDoc) {
		try {
			
			// In case this is used inside tomcat 5.5.12.
			System.setProperty("javax.xml.transform.TransformerFactory",
					"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
			
			XSLTransformer transformer = new XSLTransformer(xsltDoc);
			return nodeToString(transformer.transform(xmlDoc));
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
			Document doc = docBuilder.build(new File(xmlfilepath));
			return doc;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Document xmlFileToDoc(File xmlfile) {
		try {
			Document doc = docBuilder.build(xmlfile);
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
			Document doc = docBuilder.build(stream);
			return doc;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Document xmlStringToDoc(String xml) {
		try {
			return docBuilder.build(new ByteArrayInputStream(xml
					.getBytes("UTF-8")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
