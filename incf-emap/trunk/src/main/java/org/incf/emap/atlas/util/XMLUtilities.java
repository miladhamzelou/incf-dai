package org.incf.emap.atlas.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class XMLUtilities {

	private static final Logger LOG = LoggerFactory
	.getLogger(XMLUtilities.class);

	/**
     * Print an XML string in "pretty" format to an output stream.
     * 
     * @param xmlString
     * @param out
     * @param indent the number of characters to indent
     */
    public static void prettyPrintXmlString(String xmlString, 
    		OutputStream out, int indent) {
    	prettyPrintXml(new ByteArrayInputStream(xmlString.getBytes()), out,
    			indent);
    }
    
    /**
     * Print XML in "pretty" format.
     * 
     * @param in an XML input stream
     * @param out where the result will go
     * @param indent the number of characters to indent
     */
    public static void prettyPrintXml(InputStream in, 
    		OutputStream out, int indent) {
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        TransformerFactory tfactory = TransformerFactory.newInstance();
        
        // set indent spaces on factory
//        tfactory.setAttribute("indent-number", new Integer(indent));
        
        try {
        	DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
        	Document doc = docBuilder.parse(in);
        	Transformer serializer = tfactory.newTransformer();
            
            // turn on indenting on transformer (serializer)
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(
            		"{http://xml.apache.org/xslt}indent-amount", 
//            		String.valueOf(indent));
            		"2");

            serializer.transform(new DOMSource(doc),
            		new StreamResult(new OutputStreamWriter(out, "utf-8")));
        } catch (Exception e) {
        	
            // this is fatal, just dump stack and throw runtime exception
            e.printStackTrace();
            
            throw new RuntimeException(e);
        }
    }
    
    public static void prettyPrintXml(Document doc, 
    		OutputStream out, int indent) {
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        TransformerFactory tfactory = TransformerFactory.newInstance();
        
        // set indent spaces on factory
//        tfactory.setAttribute("indent-number", new Integer(indent));
        
        try {
        	Transformer serializer = tfactory.newTransformer();
            
            // turn on indenting on transformer (serializer)
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(
            		"{http://xml.apache.org/xslt}indent-amount", 
//            		String.valueOf(indent));
            		"2");

            serializer.transform(new DOMSource(doc),
            		new StreamResult(new OutputStreamWriter(out, "utf-8")));
        } catch (Exception e) {
        	
            // this is fatal, just dump stack and throw runtime exception
            e.printStackTrace();
            
            throw new RuntimeException(e);
        }
    }
    
	
    public static void main(String[] args) throws FileNotFoundException {
    	String inFile = "src/main/resources/exampleResponses/GetCapabilitiesResponse.xml";
    	String outFile = "src/main/resources/exampleResponses/GetCapabilitiesResponse1.xml";
//    	String inFile = "dbDump.xml";
//    	String outFile = "dbDumpPretty.xml";
    	prettyPrintXml(new FileInputStream(inFile), new FileOutputStream(outFile), 4);
    }

	public String coordinateTransformation( String transformationChainURL, String x, String y, String z ) {

		XMLUtilities util = new XMLUtilities();
		String responseString = "";
		String resultStringFromURL = "";

		EMAPConfigurator config = EMAPConfigurator.INSTANCE;

		String abaReference = config.getValue("srsname.abareference.10");
		String abaVoxel = config.getValue("srsname.abavoxel.10");
		String agea = config.getValue("srsname.agea.10");
		String whs09 = config.getValue("srsname.whs.09");
		String whs10 = config.getValue("srsname.whs.10");
		String emap = config.getValue("srsname.emap.10");
		String paxinos = config.getValue("srsname.paxinos.10");

		LOG.debug("transformationChainURL String - {}",transformationChainURL);

		try { 

			resultStringFromURL = util.convertFromURLToString(transformationChainURL);
			Element xmlElement = util.getDocumentElementFromString(resultStringFromURL);
			String[] elementValue = util.getStringValuesForXMLTag(xmlElement, "CoordinateTransformation");

			String filter = ";filter=cerebellum";
			String resultURL = "";
			String resultURLReturnString = "";
			Element resultURLReturnXMLElement = null;

			for (int i = 0; i < elementValue.length; i++ ) {

				String resultURLReturnElementValue = null;
/*				String abavoxel = abaVoxel; 
				String abareference = abaReference; 
				String paxinos = paxinos; 
				String agea = agea; 
				String whs = whs09;
				String emap = emap;
*/
				resultURL = elementValue[i].replace("&amp;", "&").replace(";x=", x).replace(";y=", y).replace(";z=", z);

				//resultURL = elementValue[i].replace("&amp;", "&").replace(";x=", x).replace(";y=", y).replace(";z=", z).replace(";filter=", filter).replace(abaReference, abareference).replace("mouse_abavoxel_1.0", abavoxel).replace("mouse_agea_1.0", agea).replace("mouse_whs_1.0", whs).replace("mouse_paxinos_1.0", paxinos).replace("mouse_emap-t26_1.0", emap);

				LOG.debug("Element Value - {}",i + ": {}", resultURL);
				resultURLReturnString = util.convertFromURLToString(resultURL);
				LOG.debug("1{}",resultURLReturnString);

				if (resultURLReturnString.equalsIgnoreCase("transformation-error") ) {
					resultURLReturnElementValue = "Error: Please check the coordinates in the chain url - " + resultURL; 
					return resultURLReturnElementValue;
				} else {
					resultURLReturnXMLElement = util.getDocumentElementFromString(resultURLReturnString);
					resultURLReturnElementValue = util.getStringValueForXMLTag(resultURLReturnXMLElement, "pos");
					StringTokenizer tokens = new StringTokenizer(resultURLReturnElementValue, " ");
					while ( tokens.hasMoreTokens() ) {
						x = ";x=" + tokens.nextToken();
						y = ";y=" + tokens.nextToken();
						z = ";z=" + tokens.nextToken();
					}
					responseString = x + " " + y + " " + z;
				}
		
			}

			responseString = responseString.replace(";x=", "").replace(";y=", "").replace(";z=", "");
			//responseString = x + ", " + y + ", " + z;
			LOG.debug("after responseString - {}",responseString);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseString;
		
	}

 	public String convertFromURLToString(String stringURL) {
 		
 		String responseString = "";
 		
		try {
			URL url = new URL(stringURL);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				LOG.debug("inputLine - {}",inputLine);
				responseString = responseString + inputLine;
			}
		} catch (MalformedURLException ex) {
			LOG.debug("^^^^ERROR1^^^^^");
			System.err.println(ex);
	    } catch (IOException ex) {
			LOG.debug("^^^^ERROR2^^^^^");
			System.err.println(ex);
			responseString = "transformation-error";
			return responseString;
	    } catch (Exception e) {
			e.printStackTrace();
		}
		return responseString.replace("&", "&amp;");
 	}

	/**
	 * Get root element from XML String
	 * 
	 * @param arg
	 *            XML String
	 * @return Root Element
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Element getDocumentElementFromString(String arg)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		Document document;
		dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
		document = db.parse(new InputSource(new StringReader(arg)));
		Element element = document.getDocumentElement();
		return element;

	}
 
 	public static String getStringValueForXMLTag(Element xmlElement, String key) {
		NodeList nl = xmlElement.getElementsByTagName(key);
		if (nl.getLength() > 0) {
			Node node = nl.item(0).getFirstChild();
			if (node != null) {
				String output = node.getNodeValue().trim();
				return output;
			}
		}
		return "";
	}

 	public static String[] getStringValuesForXMLTag(Element xmlElement, String key) {
		NodeList nl = xmlElement.getElementsByTagName(key);

		if (nl.getLength() > 0) {

			String[] output = new String[nl.getLength()];

			for ( int i = 0; i < nl.getLength(); i++ ) { 
			Node node = nl.item(i).getFirstChild();
			if (node != null) {
				output[i] = node.getNodeValue().trim();
			}
			}
			return output;
		}
		return null;
	}

 	
}
