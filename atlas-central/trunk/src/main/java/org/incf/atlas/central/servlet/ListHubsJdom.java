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
import org.incf.atlas.central.xml.XmlUtilJdom;
import org.incf.atlas.central.xml.XmlUtilW3c;
import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class ListHubsJdom {
	
	private final Logger logger = LoggerFactory.getLogger(getClass().getName());
	
	private XmlUtilJdom xu;
	
	private DocumentBuilderFactory fac;
//	private DocumentBuilder builder;
	
	private Document docListHubs;
	
	public ListHubsJdom() {
		XmlNameSpaceContext context = new XmlNameSpaceContext();
//		context.addNamespace("ows", "http://www.opengis.net/ows/2.0");
		context.addNamespace("ows", "http://www.opengis.net/ows/1.1");
		context.addNamespace("wax", "http://www.incf.org/WaxML/");
		context.addNamespace("wps", "http://www.opengis.net/wps/1.0.0");
		xu = new XmlUtilJdom();
		
		// testing
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

		logger.debug("doc:\n{}", xu.nodeToString(docListHubs));
		
		String nodeName = xu.getFirstNodeName(
				"/wax:ListHubsResponse", docListHubs);
		logger.debug("nodeName: {}", nodeName);
		
		
		List<Element> hubs = (List<Element>) xu.getNodes(
				"/wax:ListHubsResponse/wax:Hubs/wax:Hub", 
				docListHubs);
		
		logger.debug("hubs size: {}", hubs.size());
		
		for (int i = 0; i < hubs.size(); i++) {
			Element eHub = (Element) hubs.get(i);
			
			
			logger.debug("eHub:\n{}", xu.nodeToString(eHub));
			
			
			
			List<Element> services = (List<Element>) xu.getNodes(
					"/wax:HubServices/*", 
					eHub);
			
			logger.debug("services size: {}", services.size());
			
			for (int j = 0; j < services.size(); j++) {
				
				// get hub-service's getCapabilities url
				Element eHubService = (Element) services.get(j);
				
				
				logger.debug("eHubService:\n{}", xu.nodeToString(eHubService));
				
				
				String s = xu.getFirstNodeName("/wax:HubService/*", eHubService);
				logger.debug("s: {}", s);
				String getCapabilitiesUrl = xu.getFirstNodeValue(
						"/wax:HubService/wax:CapabilitiesUrl/text()", eHubService);
				logger.debug("getCapabilitiesUrl: *{}*", getCapabilitiesUrl);

				// parse getCapabilities
				Document docGC = parseHubServiceGetCapabilities(
						getCapabilitiesUrl);

				logger.debug("doc:\n{}", xu.nodeToString(docGC));
				
				// get target element from hub service's GetCapabilities
				Element eCapabilities = (Element) xu.getFirstNode(
						"/wps:Capabilities", docGC);
				
				List<String> nodeNames = xu.getNodeNames("/wps:Capabilities/*", docGC);
				for (Iterator<String> it = nodeNames.iterator(); it.hasNext(); ) {
					logger.debug("nodeName: {}", it.next());
				}
				
				// get ServiceIdentification and ProcessOfferings from hub's GC
//				Element eServiceIdentification = null;
//				Element eProcessOfferings = null;
//				List<Element> nodes = (List<Element>) xu.getNodes("/wps:Capabilities/*", docGC);
//				for (int k = 0; k < nodes.size(); k++) {
//					Element e = nodes.get(k);
//					logger.debug("nodeName: {}", e.getTagName());
//					if (e.getTagName().equals("ows:ServiceIdentification")) {
//						eServiceIdentification = e;
//					}
//					if (e.getTagName().equals("wps:ProcessOfferings")) {
//						eProcessOfferings = e;
//					}
//				}
				Element eServiceIdentification = (Element) xu.getFirstNode(
						"/wps:Capabilities/ows:ServiceIdentification", docGC);
				Element eProcessOfferings = (Element) xu.getFirstNode(
						"/wps:Capabilities/wps:eProcessOfferings", docGC);
				
				// extract ServiceIdentification & ProcessOfferings
//				Element eProcessOfferings = (Element) xu.getFirstNode(
//						"/wps:Capabilities/wps:ProcessOfferings", doc);
//				Element eServiceIdentification = (Element) xu.getFirstNode(
//				Node eServiceIdentification = xu.getFirstNode(
//						"ows:ServiceIdentification", eCapabilities);
				
				logger.debug("eProcessOfferings:\n{}", xu.nodeToString(eProcessOfferings));
				logger.debug("eServiceIdentification:\n{}", xu.nodeToString(eServiceIdentification));
				
				
				// append extracts to HubService
//				Node nServiceIdentification = docListHubs.importNode(eServiceIdentification, true);
//				eHubService.appendChild(docListHubs.importNode(eServiceIdentification, true));
//				docListHubs.adoptNode(eServiceIdentification);
//				eHubService.appendChild(eServiceIdentification);
//				Node nProcessOfferings = docListHubs.importNode(eProcessOfferings, true);
//				eHubService.appendChild(nProcessOfferings);
			}
		}
	}
	
	private Document parseHubServiceGetCapabilities(String hubServiceUrl) {
		Document doc = null;
		InputStream inputStream = null;
		try {
		    // Create a URL for the desired page
		    URL url = new URL(hubServiceUrl);
		    
		    logger.debug("url: {}", url.toString());
		 
		    inputStream = url.openStream();
		    doc = xu.xmlStreamToDoc(inputStream);
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} finally {
		    try {
				inputStream.close();
			} catch (IOException ignore) {
			}
		}
		return doc;
	}
	
//	public static StringBuilder convert(Document doc) {
//		ByteArrayOutputStream stream = null;
//		StringBuilder stringBuilder = null;
//		try {
//			stream = new ByteArrayOutputStream();
//			OutputFormat outputformat = new OutputFormat();
//			outputformat.setIndent(4);
//			outputformat.setIndenting(true);
//			outputformat.setPreserveSpace(false);
//			XMLSerializer serializer = new XMLSerializer();
//			serializer.setOutputFormat(outputformat);
//			serializer.setOutputByteStream(stream);
//			serializer.asDOMSerializer();
//			serializer.serialize(doc.getDocumentElement());
//			stringBuilder = new StringBuilder(stream.toString());
//		} catch (Exception except) {
//			except.getMessage();
//		} finally {
//			try {
//				stream.close();
//			} catch (IOException ignore) {
//			}
//		}
//		return stringBuilder;
//	}

//	public static String xmlToString(Node node) {
//		StringWriter stringWriter = null;
//		try {
//			Source source = new DOMSource(node);
//			stringWriter = new StringWriter();
//			StreamResult streamResult = new StreamResult(stringWriter);
//			Result result = streamResult;
//			TransformerFactory factory = TransformerFactory.newInstance();
//			Transformer transformer = factory.newTransformer();
//			transformer.transform(source, result);
//			return stringWriter.getBuffer().toString();
//		} catch (TransformerConfigurationException e) {
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				stringWriter.close();
//			} catch (IOException ignore) {
//			}
//		}
//		return null;
//	}
    
    private Document getListHubsDoc() {
    	return docListHubs;
    }
    
    public static void main(String[] args) {
    	ListHubsJdom app = new ListHubsJdom();
    	app.processHubsServices();
//    	System.out.println(xmlToString(app.getListHubsDoc()));
    }

}
