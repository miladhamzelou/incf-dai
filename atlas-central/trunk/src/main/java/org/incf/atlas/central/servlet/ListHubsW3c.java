package org.incf.atlas.central.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.incf.atlas.central.xml.XmlNameSpaceContext;
import org.incf.atlas.central.xml.XmlUtilW3c;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class ListHubsW3c {
	
	private final Logger logger = LoggerFactory.getLogger(getClass().getName());
	
	private XmlUtilW3c xu;
	
	private DocumentBuilderFactory fac;
//	private DocumentBuilder builder;
	
	private Document docListHubs;
	
	public ListHubsW3c() {
		XmlNameSpaceContext context = new XmlNameSpaceContext();
//		context.addNamespace("ows", "http://www.opengis.net/ows/2.0");
		context.addNamespace("ows", "http://www.opengis.net/ows/1.1");
		context.addNamespace("wax", "http://www.incf.org/WaxML/");
		context.addNamespace("wps", "http://www.opengis.net/wps/1.0.0");
		xu = new XmlUtilW3c(context);
		
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
//		docListHubs = xu.xmlStringToDoc(docString);

		fac = DocumentBuilderFactory.newInstance();
//		try {
//			DocumentBuilder builder = fac.newDocumentBuilder();
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		docListHubs = parseListHubsSkeleton();
		URL url = getClass().getResource("/ListHubsSkeleton.xml");
		String file = url.getFile();
//		docListHubs = xu.xmlFileToDoc("/home/dave/workspace/atlas-central/src/main/resources/ListHubsSkeleton.xml");
		docListHubs = xu.xmlFileToDoc(file);
	}
	
	/*
	 * skeleton insert parent element; wax:Hubservice
	 * hub atlas GC children: (1) ows:ServiceIdentification
	 * (2) wps:ProcessOfferings
	 */
	
//	private Document parseListHubsSkeleton() {
//		Document doc = null;
//	    try {
//			DocumentBuilder builder = fac.newDocumentBuilder();
//			doc = builder.parse(this.getClass().getResourceAsStream("/ListHubsSkeleton.xml"));
//			xu.removeEmptyTextNodes(doc);
////			doc = builder.parse(this.getClass().getResourceAsStream("/test.xml"));
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return doc;
//	}
	
	/*
    <wax:Hub>
      <wax:description>ABA (Allen Brain Atlas) Hub</wax:description>
      <wax:WebPresence>
        <wax:Website>http://http://www.brain-map.org/</wax:Website>
      </wax:WebPresence>
      <wax:Status>Operational</wax:Status>
      <wax:Contacts>
        <wax:ServiceContact/>
      </wax:Contacts>
      <wax:HubServices>
        <wax:HubService>
          <wax:CapabilitiesUrl>...</wax:CapabilitiesUrl>
          <!-- begin atlas GetCapabilities SvcId, ProcOfferings -->
          <!-- end atlas GetCapabilities -->
        </wax:HubService>
      </wax:HubServices>
    </wax:Hub>
    
    - parseListHubsSkeleton() to DOM
    - for each hub (use skeleton)
    -   for each service (use skeleton) thread?
    -     parseHubServiceGetCapabilities(url from skeleton)
    -     extract ServiceIdentification & ProcessOfferings
    -     make wax:HubService
    -     append extracts to Hubservice
    -     append Hubservice to HubServices
    - serialize DOM
    - return ListHubs.xml
	 */
	
	private void processHubsServices() {

		logger.debug("doc: {}", convert(docListHubs));
		
		String nodeName = xu.getFirstNodeName(
				"/wax:ListHubsResponse", docListHubs);
//		"/wax:ListHubsResponse", docListHubs, 
//		"wax", "http://www.incf.org/WaxML/");
//				"/A:v", docListHubs, 
//				"A", "some_url");
		logger.debug("nodeName: {}", nodeName);
		
		
		NodeList hubs = xu.getNodes(
				"/wax:ListHubsResponse/wax:Hubs/wax:Hub", 
				docListHubs);
		for (int i = 0; i < hubs.getLength(); i++) {
			Element eHub = (Element) hubs.item(i);
			
			
			logger.debug("eHub: {}", xu.nodeToString(eHub));
			
			
			
			NodeList services = xu.getNodes(
					"/wax:Hub/wax:HubServices/wax:HubService", 
					eHub);
			for (int j = 0; j < services.getLength(); j++) {
				
				// get hub-service's getCapabilities url
				Element eHubService = (Element) services.item(j);
				
				
				logger.debug("eHubService: {}", xu.nodeToString(eHubService));
				
				
				String s = xu.getFirstNodeName("/wax:HubService/*", eHubService);
				logger.debug("s: {}", s);
				String getCapabilitiesUrl = xu.getFirstNodeValue(
						"/wax:HubService/wax:CapabilitiesUrl/text()", eHubService);
				logger.debug("getCapabilitiesUrl: *{}*", getCapabilitiesUrl);

				// parse getCapabilities
				Document doc = parseHubServiceGetCapabilities(
						getCapabilitiesUrl);

				logger.debug("doc: {}", xu.nodeToString(doc));
				
				Element eCapabilities = (Element) xu.getFirstNode(
						"/wps:Capabilities", doc);
				
				List<String> nodeNames = xu.getNodeNames("/wps:Capabilities/*", doc);
				for (Iterator<String> it = nodeNames.iterator(); it.hasNext(); ) {
					logger.debug("nodeName: {}", it.next());
				}
				
				Element eServiceIdentification = null;
				Element eProcessOfferings = null;
				NodeList nodes = xu.getNodes("/wps:Capabilities/*", doc);
				for (int k = 0; k < nodes.getLength(); k++) {
					Element e = (Element) nodes.item(k);
					logger.debug("nodeName: {}", e.getTagName());
					if (e.getTagName().equals("ows:ServiceIdentification")) {
						eServiceIdentification = e;
					}
					if (e.getTagName().equals("wps:ProcessOfferings")) {
						eProcessOfferings = e;
					}
				}
				
				// extract ServiceIdentification & ProcessOfferings
//				Element eProcessOfferings = (Element) xu.getFirstNode(
//						"/wps:Capabilities/wps:ProcessOfferings", doc);
//				Element eServiceIdentification = (Element) xu.getFirstNode(
//				Node eServiceIdentification = xu.getFirstNode(
//						"ows:ServiceIdentification", eCapabilities);
				
				logger.debug("eProcessOfferings: {}", xu.nodeToString(eProcessOfferings));
				logger.debug("eServiceIdentification: {}", xu.nodeToString(eServiceIdentification));
				
				
				// append extracts to HubService
//				Node nServiceIdentification = docListHubs.importNode(eServiceIdentification, true);
//				eHubService.appendChild(docListHubs.importNode(eServiceIdentification, true));
//				docListHubs.adoptNode(eServiceIdentification);
//				eHubService.appendChild(eServiceIdentification);
				Node nProcessOfferings = docListHubs.importNode(eProcessOfferings, true);
				eHubService.appendChild(nProcessOfferings);
			}
		}
	}
	
//	private void merge() {
//		
//		// get Hubs element
//		NodeList nodes = docListHubs.getElementsByTagName("wax:Hubs");
//		Element eHubs = (Element) nodes.item(0);		
//		
//		// get GetCapabilities elements to import
//		Document docAbaWpsGetCapabilities = parseHubServiceGetCapabilities("aba");
//		nodes = docAbaWpsGetCapabilities.getElementsByTagName("ows:ServiceIdentification");
//		Element eAbaServiceIdentification = (Element) nodes.item(0);
//		nodes = docAbaWpsGetCapabilities.getElementsByTagName("wps:ProcessOfferings");
//		Element eAbaProcessOfferings = (Element) nodes.item(0);
//		
//		// create Hub element for this hub
//		Element eAbaHub = docListHubs.createElement("wax:Hub");
//		
//		// import and append xxx elements
//		Node nAbaServiceIdentification = docListHubs.importNode(eAbaServiceIdentification, true);
//		eAbaHub.appendChild(nAbaServiceIdentification);
//		Node nAbaProcessOfferings = docListHubs.importNode(eAbaProcessOfferings, true);
//		eAbaHub.appendChild(nAbaProcessOfferings);
//		
//		// append this Hub to Hubs
//		eHubs.appendChild(eAbaHub);
//	}
	
	private Document parseHubServiceGetCapabilities(String hubServiceUrl) {
		Document doc = null;
		InputStream inputStream = null;
		try {
		    // Create a URL for the desired page
		    URL url = new URL(hubServiceUrl);
		    
		    logger.debug("url: {}", url.toString());
		 
//		    BufferedReader in = new BufferedReader(new InputStreamReader(
//		    		url.openStream()));
//		    StringBuilder buf = new StringBuilder();
//		    String line;
//		    while ((line = in.readLine()) != null) {
//		        buf.append(line).append('\n');
//		    }
//		    in.close();
		    
//		    DocumentBuilder builder = fac.newDocumentBuilder();
//		    doc = builder.parse(url.openStream());
		    inputStream = url.openStream();
		    doc = xu.xmlStreamToDoc(inputStream);
		} catch (MalformedURLException e) {
		} catch (IOException e) {
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} finally {
		    try {
				inputStream.close();
			} catch (IOException ignore) {
			}
		}
		return doc;
	}
	
	// parse fragment to xml element and append
//	public static void appendXmlFragment(DocumentBuilder docBuilder, 
//			Node parent, String fragment) throws IOException, SAXException {
//		Document doc = parent.getOwnerDocument();
//		Node fragmentNode = docBuilder.parse(
//				new InputSource(new StringReader(fragment)))
//				.getDocumentElement();
//		fragmentNode = doc.importNode(fragmentNode, true);
//		parent.appendChild(fragmentNode);
//	}
	
	public static StringBuilder convert(Document doc) {
		ByteArrayOutputStream stream = null;
		StringBuilder stringBuilder = null;
		try {
			stream = new ByteArrayOutputStream();
			OutputFormat outputformat = new OutputFormat();
			outputformat.setIndent(4);
			outputformat.setIndenting(true);
			outputformat.setPreserveSpace(false);
			XMLSerializer serializer = new XMLSerializer();
			serializer.setOutputFormat(outputformat);
			serializer.setOutputByteStream(stream);
			serializer.asDOMSerializer();
			serializer.serialize(doc.getDocumentElement());
			stringBuilder = new StringBuilder(stream.toString());
		} catch (Exception except) {
			except.getMessage();
		} finally {
			try {
				stream.close();
			} catch (IOException ignore) {
			}
		}
		return stringBuilder;
	}

	public static String xmlToString(Node node) {
		StringWriter stringWriter = null;
		try {
			Source source = new DOMSource(node);
			stringWriter = new StringWriter();
			StreamResult streamResult = new StreamResult(stringWriter);
			Result result = streamResult;
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.transform(source, result);
			return stringWriter.getBuffer().toString();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} finally {
			try {
				stringWriter.close();
			} catch (IOException ignore) {
			}
		}
		return null;
	}
    
    private Document getListHubsDoc() {
    	return docListHubs;
    }
    
    public static void main(String[] args) {
    	ListHubsW3c app = new ListHubsW3c();
    	app.processHubsServices();
    	System.out.println(xmlToString(app.getListHubsDoc()));
    }
}
