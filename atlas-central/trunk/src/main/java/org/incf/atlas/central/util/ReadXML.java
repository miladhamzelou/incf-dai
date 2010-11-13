package org.incf.atlas.central.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.incf.atlas.central.resource.CentralServiceVO;
import org.incf.atlas.central.resource.KeyValueBean;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadXML {

	public static void main ( String [] s ) {
	
		ReadXML readXML = new ReadXML();

		try { 

			//Step 1 - read xml response string
			String xmlString = readXML.convertFromURLToString("http://incf-dev-local.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:Sagittal;tolerance=3");
			System.out.println("XML String is - " + xmlString);

/*			
			//Step 2 - Create a xml document from the string 
			Element xmlElement = readXML.getDocumentElementFromString(xmlString);

			//Step 3 - Get a value from the xml document
			String [] elementValues = readXML.getStringValuesForXMLTag(xmlElement, "ows:Identifier");

			for (int i = 0; i < elementValues.length; i++) {
				System.out.println("Element Value is - " + elementValues[i]);
			}
*/

			xmlString = "http://132.239.131.188:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:Sagittal;tolerance=3";
			//xmlString = "http://132.239.131.188:8080/atlas-central?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsname=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:Sagittal;tolerance=3";
			ArrayList list = new ArrayList();
			//list = readXML.getImageData(xmlString);
			list = readXML.get2DImageDataList(xmlString, list);
			System.out.println("*****************List = "+list.size());
			
			Iterator iterator = list.iterator();
			CentralServiceVO vo = null;
			while (iterator.hasNext()) {
				vo = (CentralServiceVO)iterator.next();
				
				System.out.println("*****************URL = "+vo.getUrlString());
				System.out.println("*****************TFW Values = "+vo.getTfwValues());
			}


/*	        ArrayList completeStructureList = new ArrayList();
			CentralServiceVO vo = new CentralServiceVO();
	        // 2a - Call the method from ABA Hub
	        String abaURL = "http://incf-dev.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=280;y=112;z=162;vocabulary=Mouse_ABAvoxel_1.0;filter=structureset:Fine";
	        completeStructureList = readXML.getStructureData(abaURL, completeStructureList);

	        // 2b - Call the method from UCSD Hub
	        String ucsdURL = "http://incf-dev.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=280;y=112;z=162;vocabulary=Mouse_ABAvoxel_1.0;filter=structureset:Fine";
	        completeStructureList = readXML.getStructureData(ucsdURL, completeStructureList);

	        // 2c - Call the method from WHS Hub
	        String whsURL = "http://incf-dev.crbs.ucsd.edu:8080/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=280;y=112;z=162;vocabulary=Mouse_ABAvoxel_1.0;filter=structureset:Fine";
	        completeStructureList = readXML.getStructureData(whsURL, completeStructureList);
			Iterator iterator = completeStructureList.iterator();

			KeyValueBean keyValue = null;
			while (iterator.hasNext()) {
				keyValue = (KeyValueBean)iterator.next();
				
				System.out.println("Name = "+keyValue.getKey());
				System.out.println("Description = "+keyValue.getValue());
			}
*/			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	public ArrayList get2DImageDataList(String urlString, ArrayList list) {

		try {

			ReadXML readXML = new ReadXML();
			
			System.out.println("************************URLString is******************* - " + urlString);
			String xmlString = readXML.convertFromURLToString(urlString); 
			
			System.out.println("XMLString is - " + xmlString);
			list = readXML.getImageData(xmlString);
			System.out.println("List Size is - " + list.size());
			
			Iterator iterator = list.iterator();
			CentralServiceVO vo = null;
			while (iterator.hasNext()) {
				vo = (CentralServiceVO)iterator.next();
				System.out.println("URL = "+vo.getWms());
				System.out.println("TFW Values = "+vo.getTfwValues());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}


	public ArrayList getStructureData(String urlString, ArrayList list) {

		KeyValueBean keyValue = new KeyValueBean();

		try {

			ReadXML readXML = new ReadXML();
			
			String xmlString = readXML.convertFromURLToString(urlString); 

			Element xmlElement = readXML.getDocumentElementFromString(xmlString);

			keyValue.setKey(readXML.getStringValueForXMLTag(xmlElement, "Code"));
			keyValue.setValue(readXML.getStringValueForXMLTag(xmlElement, "Description")); 
			
			list.add(keyValue);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return list;
		
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
				System.out.println("inputLine - "+inputLine);
				responseString = responseString + inputLine;
			}
		} catch (MalformedURLException ex) {
			System.out.println("^^^^ERROR1^^^^^");
			System.err.println(ex);
	    } catch (IOException ex) {
			System.out.println("^^^^ERROR2^^^^^");
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

	
	public static String getStringValueForXMLTag(Element xmlElement, String key) {

		NodeList nl = xmlElement.getElementsByTagName(key);

		if (nl.getLength() > 0) {

			String output = "";

			for ( int i = 0; i < nl.getLength(); i++ ) { 
			Node node = nl.item(i).getFirstChild();
			if (node != null) {
				output = node.getNodeValue().trim();
			}
			}
			return output;
		}
		return null;
	}

	
    public ArrayList getImageData(String data) {

    	SAXBuilder builder = new SAXBuilder();
    	ArrayList list = new ArrayList();
    	CentralServiceVO vo = null;
    	
    	try {
        
    		org.jdom.Document document = builder.build(new ByteArrayInputStream(data.getBytes()));

    		Iterator responseList = document.getRootElement().getChildren()
            .listIterator();

            while (responseList.hasNext()) { 

                org.jdom.Element imageResponseElements = (org.jdom.Element) responseList.next();
                
                if (imageResponseElements.getName().equalsIgnoreCase("Image2Dcollection")) {
                	
                    Iterator image2DIterator = imageResponseElements.getChildren().listIterator();

                    while (image2DIterator.hasNext()) { 
                        org.jdom.Element image2DElement = (org.jdom.Element) image2DIterator.next();
                    	
	                    List columns = image2DElement.getChildren();
	                	System.out.println("Size is - "+columns.size());
	                    for (int j = 0; j < columns.size(); j++) {
	                    	
	                    	vo = new CentralServiceVO();
	                    	org.jdom.Element column = (org.jdom.Element) columns.get(j);
	                    	System.out.println("Column Name is - "+column.getName());

	                    	if ( column.getName().equalsIgnoreCase("imageposition")) {
	                    		List imagePositionList = column.getChildren();

		                        System.out.println("ImagePosition List is - "+imagePositionList.size());
	    	                    for (int x = 0; x < imagePositionList.size(); x++) {
	    	                    	org.jdom.Element imagePositionElements = (org.jdom.Element) imagePositionList.get(x);

			                        if (!imagePositionElements.getName().equalsIgnoreCase("corners") ) {
			                    		//vo.setSrsName(imagePositionElements.getAttribute("srsName").getValue());
			                        	vo.setTfwValues(imagePositionElements.getText());
				                        
			                        } else {
			                        }
			                    }
	    	                 }
	                    	if ( column.getName().equalsIgnoreCase("imagesource")) {
		                    	vo.setSrsName(column.getAttribute("srsName").getValue());
		                        vo.setWms(column.getText());
		                        System.out.println("WMS - " + vo.getWms());
	                    	}	                        

	                    	list.add(vo);
	                    }
                    }
                }
                
            }

    	} catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return list;
        
    }

}
