package org.incf.emap.atlas.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import org.jdom.JDOMException;
import org.jdom.Namespace;
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
			//String xmlString = readXML.convertFromURLToString("http://incf-dev-local.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:Sagittal;tolerance=3");
			//System.out.println("XML String is - " + xmlString);

/*			
			//Step 2 - Create a xml document from the string 
			Element xmlElement = readXML.getDocumentElementFromString(xmlString);

			//Step 3 - Get a value from the xml document
			String [] elementValues = readXML.getStringValuesForXMLTag(xmlElement, "ows:Identifier");

			for (int i = 0; i < elementValues.length; i++) {
				System.out.println("Element Value is - " + elementValues[i]);
			}
*/

			//String xmlString = "http://incf-dev-local.crbs.ucsd.edu/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:Sagittal;tolerance=3";
			//String xmlString = "http://132.239.131.188:8080/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:sagittal;tolerance=3";
			//xmlString = "http://132.239.131.188:8080/atlas-central?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsname=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:Sagittal;tolerance=3";
			//String xmlString = "http://incf-dev-local.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_AGEA_1.0;x=6600;y=4000;z=5600;filter=maptype:coronal";
			String xmlString = "http://lxbisel.macs.hw.ac.uk:8080/EMAPServiceController?request=Execute&identifier=TransformPOI&dataInputs=inputSRSCode=Mouse_WHS_1.0;targetSRSCode=Mouse_EMAP-T26_1.0;x=12;y=-29;z=-73";
			
			EMAPServiceVO vo = new EMAPServiceVO();
			vo = readXML.getPOIFromEMAPData(xmlString, vo);
			System.out.println("VO - " + vo.getValue());
			
			/*			ArrayList list = new ArrayList();
			//list = readXML.getImageData(xmlString);
			list = readXML.get2DImageDataList(xmlString, list);
			System.out.println("*****************List = "+list.size());
			
			Iterator iterator = list.iterator();
			EMAPServiceVO vo = null;
			while (iterator.hasNext()) {
				vo = (EMAPServiceVO)iterator.next();
				
				System.out.println("*****************URL = "+vo.getWms());
				System.out.println("*****************TFW Values = "+vo.getTfwValues());
			}
*/

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
/*			CentralServiceVO vo = null;
			while (iterator.hasNext()) {
				vo = (CentralServiceVO)iterator.next();
				//System.out.println("URL = "+vo.getWms());
				//System.out.println("TFW Values = "+vo.getTfwValues());
			}
*/
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}


	public ArrayList getStructureData(String urlString, ArrayList list) {

		KeyValueBean keyValue = new KeyValueBean();
		Element xmlElement = null;
		
		try {

			System.out.println("1");
			ReadXML readXML = new ReadXML();
			System.out.println("2");
			
			String xmlString = readXML.convertFromURLToString(urlString); 
			System.out.println("3");

			xmlElement = readXML.getDocumentElementFromString(xmlString);
			System.out.println("9");
			keyValue.setKey(readXML.getStringValueForXMLTag(xmlElement, "Code"));
			System.out.println("10");
			keyValue.setValue(readXML.getStringValueForXMLTag(xmlElement, "Description")); 
			System.out.println("11");
			
			list.add(keyValue);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			xmlElement = null;
		}
	
		return list;
		
	}


	public EMAPServiceVO getPOIFromEMAPData(String urlString, EMAPServiceVO vo) {

		try {

			ReadXML readXML = new ReadXML();
			
			String xmlString = readXML.convertFromURLToString(urlString); 

			Element xmlElement = readXML.getDocumentElementFromString(xmlString);

			vo.setValue(readXML.getStringValueForXMLTag(xmlElement, "gml:pos"));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return vo;
		
	}


 	public String convertFromURLToString(String stringURL) {

 		String responseString = "";
		System.out.println("1" + stringURL);
		URL url = null;
		URLConnection urlCon = null;
		
 		try {
			url = new URL(stringURL);
			System.out.println("2");
			urlCon = url.openConnection();
			System.out.println("3");
			urlCon.setUseCaches(false);
			System.out.println("4");
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
			System.out.println("5");
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("inputLine - "+inputLine);
				responseString = responseString + inputLine;
			}
			in.close();
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
		} finally {
			url = null;
			urlCon = null;
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

		System.out.println("1 - before: " +arg);

		String xml = arg.trim().replaceFirst("^([\\W]+)<","<");

		DocumentBuilderFactory dbf;
		System.out.println("2 - after: " + xml);
		DocumentBuilder db;
		System.out.println("3");
		Document document;
		System.out.println("4");
		dbf = DocumentBuilderFactory.newInstance();
		System.out.println("5");
		db = dbf.newDocumentBuilder();
		System.out.println("6");
		document = db.parse(new InputSource(new StringReader(xml)));
		System.out.println("7");
		Element element = document.getDocumentElement();
		System.out.println("8");
		
		db = null;
		dbf = null;
		document = null;

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
    	EMAPServiceVO vo = null;
    	
    	try {

    		org.jdom.Document document = builder.build(new ByteArrayInputStream(data.getBytes()));

    		Iterator responseList = document.getRootElement().getChildren()
            .listIterator();

    		org.jdom.Element root = document.getRootElement();
    		//root.getChild("wps:ProcessOutputs").getChild("wps:Output").getChild("wps:Data").getChild("wps:ComplexData").getChild("ImagesResponse").getChild("Image2DCollection");
    		
    		System.out.println("****Data****" + data);
    		
    		Namespace ns = Namespace.getNamespace("http://www.opengis.net/wps/1.0.0");
            //while (responseList.hasNext()) { 

            	org.jdom.Element imageResponseElements = root.getChild("ProcessOutputs", ns).getChild("Output", ns).getChild("Data", ns).getChild("ComplexData", ns).getChild("ImagesResponse").getChild("Image2Dcollection");
            	
                if (imageResponseElements.getName().equalsIgnoreCase("Image2Dcollection")) {

                    Iterator image2DIterator = imageResponseElements.getChildren().listIterator();
                    
                    int z = 0;
                    while (image2DIterator.hasNext()) { 
                    	z = z++;
                    	vo = new EMAPServiceVO();
                    	System.out.println("IMAGE-2DCOUNT: " + z);
                    	
                        org.jdom.Element image2DElement = (org.jdom.Element) image2DIterator.next();

	                    List columns = image2DElement.getChildren();
	                    for (int j = 0; j < columns.size(); j++) {
	                    	
	                    	org.jdom.Element column = (org.jdom.Element) columns.get(j);

	                    	if ( column.getName().equalsIgnoreCase("imageposition")) {
		                    	System.out.println("IMAGEPOSITION-COUNT: " + z);
	                    		//System.out.println("IMAGEPOSITION: " + z);	
	                    		List imagePositionList = column.getChildren();

		                        //System.out.println("ImagePosition List is - "+imagePositionList.size());
	    	                    for (int x = 0; x < imagePositionList.size(); x++) {

	    	                    	org.jdom.Element imagePositionElements = (org.jdom.Element) imagePositionList.get(x);
			                        if (imagePositionElements.getName().equalsIgnoreCase("ImagePlanePlacement") ) {
			                        	//System.out.println("TFW in ImagePlanePlacement: " + imagePositionElements.getText()); 
			                        	vo.setTfwValues(imagePositionElements.getText());
			                        } 

			                        if (imagePositionElements.getName().equalsIgnoreCase("corners")) {
				                        List cornerList = column.getChild("Corners").getChildren(); 
				                        
				                        //1st corner
				                        org.jdom.Element corner = (org.jdom.Element) cornerList.get(0);
		    	                    	//System.out.println("Corner::: "+corner.getAttributeValue("position"));
		    	                    	List pointsList = corner.getChildren();
			    	                    for (int l = 0; l < pointsList.size(); l++) {
			    	                    	org.jdom.Element point = (org.jdom.Element) pointsList.get(l);
			    	                    	//System.out.println("position::: "+point.getChildren().size());
			    	                    	List posList = point.getChildren();
				    	                    for (int m = 0; m < posList.size(); m++) {
				    	                    	org.jdom.Element pos = (org.jdom.Element) posList.get(m);
				    	                    	vo.setTopLeft(pos.getText());
				    	                    	//System.out.println("::Top Left::: "+vo.getTopLeft());
				    	                    }
			    	                    }

		                        	//2nd corner
	    	                    	corner = (org.jdom.Element) cornerList.get(1);
	    	                    	//System.out.println("Corner::: "+corner.getAttributeValue("position"));
	    	                    	pointsList = corner.getChildren();
		    	                    for (int l = 0; l < pointsList.size(); l++) {
		    	                    	org.jdom.Element point = (org.jdom.Element) pointsList.get(l);
		    	                    	//System.out.println("position::: "+point.getChildren().size());
		    	                    	List posList = point.getChildren();
			    	                    for (int m = 0; m < posList.size(); m++) {
			    	                    	org.jdom.Element pos = (org.jdom.Element) posList.get(m);
			    	                    	vo.setBottomLeft(pos.getText());
			    	                    	//System.out.println("::Bottom Left::: "+vo.getBottomLeft());
			    	                    }
		    	                    }
	    	                    	
		    	                    //3rd corner
	    	                    	corner = (org.jdom.Element) cornerList.get(2);
	    	                    	//System.out.println("Corner::: "+corner.getAttributeValue("position"));
	    	                    	pointsList = corner.getChildren();
		    	                    for (int l = 0; l < pointsList.size(); l++) {
		    	                    	org.jdom.Element point = (org.jdom.Element) pointsList.get(l);
		    	                    	//System.out.println("position::: "+point.getChildren().size());
		    	                    	List posList = point.getChildren();
			    	                    for (int m = 0; m < posList.size(); m++) {
			    	                    	org.jdom.Element pos = (org.jdom.Element) posList.get(m);
			    	                    	vo.setTopRight(pos.getText());
			    	                    	//System.out.println("::Top Right::: "+vo.getTopRight());
			    	                    }
		    	                    }

		    	                    //4th corner
	    	                    	corner = (org.jdom.Element) cornerList.get(3);
	    	                    	//System.out.println("Corner::: "+corner.getAttributeValue("position"));
	    	                    	pointsList = corner.getChildren();
		    	                    for (int l = 0; l < pointsList.size(); l++) {
		    	                    	org.jdom.Element point = (org.jdom.Element) pointsList.get(l);
		    	                    	//System.out.println("position::: "+point.getChildren().size());
		    	                    	List posList = point.getChildren();
			    	                    for (int m = 0; m < posList.size(); m++) {
			    	                    	org.jdom.Element pos = (org.jdom.Element) posList.get(m);
			    	                    	vo.setBottomRight(pos.getText());
			    	                    	//System.out.println("::Bottom Right::: "+vo.getBottomRight());
			    	                    }
		    	                    }
//}				                        
			                        }
			                    }

	                    	if ( column.getName().equalsIgnoreCase("imageplaneequation")) {
	                    		vo.setImagePlaneEquation(column.getText());
	                    	}

                    } else if ( column.getName().equalsIgnoreCase("imagesource")) {
	                    		System.out.println("IMAGESOURCE-COUNT- " + z + column.getText());
		                    	vo.setSrsName(column.getAttribute("srsName").getValue());
		                        vo.setWms(column.getText());
		                        //System.out.println("WMS - " + vo.getWms());
	                }	                        

	                    }
                    	list.add(vo);
                    }
                    System.out.println("*** LIST IS: *** " +list.size());
                }
            //}

    	} catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;

    }

/*	  public org.jdom.Element convertToDOM(org.w3c.dom.Element domElement)
	     throws Exception {
	     JDOMOutputter outputter = new DOMOutputter();
	     return outputter.output(jdomElement);
	}
*/
}
