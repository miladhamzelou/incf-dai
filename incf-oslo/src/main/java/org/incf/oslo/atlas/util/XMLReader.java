package org.incf.oslo.atlas.util;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class XMLReader {

	public String getXMLString(String xmlPath) {

		String xmlString = "";

		try {
			RandomAccessFile file = new RandomAccessFile(xmlPath, "r");
			int length = (int) file.length();
			byte[] a = new byte[length];
			file.readFully(a, 0, length);
			xmlString = new String(a);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return xmlString;

	}

	public String setAnnotation(String annotationURL) {

		System.out.println("1");
        //String surl = "http://ccdb-stage-portal.crbs.ucsd.edu:8081/SLASH_Annotation_Service/SubmitAnnotationByINCFXML";
		String surl = "http://ccdb-stage-portal.crbs.ucsd.edu:8081/SLASH_Annotation_Service2/SubmitAnnotationByINCFXML";
		
        String line = null;
        String responseString = "";

        try {
    		System.out.println("2");
	            URL url = new URL(surl);
	            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	            conn.setDoOutput(true);
	            conn.setRequestMethod( "POST" );

	    		System.out.println("3");
	            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream() , "UTF-8");

	            PrintWriter pw = new PrintWriter(osw);
		        //annotationURL = "http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetAnnotationsByPOI&DataInputs=srsName=Mouse_ABAreference_1.0;x=-2;y=-1;z=0;tolerance=3";
		        String xml = getXMLFromURL(annotationURL);

                pw.write(xml);
                pw.close();
		        InputStreamReader inputS = new InputStreamReader(conn.getInputStream() , "UTF-8");

				System.out.println("4");
		        BufferedReader in = null;
		        in = new BufferedReader(inputS);
                  StringBuffer buff = new StringBuffer();
                  line = in.readLine();
                  buff.append(line);
          		System.out.println("5");
                  while(line != null)
                  {
                   line = in.readLine();
                   if(line != null)
                        buff.append(line+"\n");
                   }
                   String result = buff.toString();

                   responseString = result; 
                   System.out.println("Result is - " + result);

		        }
		         catch(Exception e)
		         {
		             e.printStackTrace();
		         }
		        
         return responseString;
		         
	}

    public String getXMLFromURL(String url) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        org.jdom.Document document = builder.build(new URL(url).openStream());
        ByteArrayOutputStream bi = new ByteArrayOutputStream();
        XMLOutputter serializer = new XMLOutputter();
        serializer.output(document, bi);
        return bi.toString();
    }
	
	public ArrayList getPolygonData(String data, ArrayList list) {

		SAXBuilder builder = new SAXBuilder();
		// ArrayList list = new ArrayList();
		AnnotationVO vo = null;
		String uniqueFilePath = "";

		try {

			org.jdom.Document document = builder
					.build(new ByteArrayInputStream(data.getBytes()));

			Iterator responseList = document.getRootElement().getChildren()
					.listIterator();

			org.jdom.Element root = document.getRootElement();

			uniqueFilePath = root.getChild("RESOURCE").getAttributeValue(
					"filepath");

			org.jdom.Element geometriesElements = root.getChild("GEOMETRIES");

			if (geometriesElements.getName().equalsIgnoreCase("GEOMETRIES")) {

				Iterator geometriesIterator = geometriesElements.getChildren()
						.listIterator();

				int z = 0;

				while (geometriesIterator.hasNext()) {
					z = z++;
					vo = new AnnotationVO();
					vo.setFilePath(uniqueFilePath);
					org.jdom.Element geometryElement = (org.jdom.Element) geometriesIterator
							.next();

					org.jdom.Element polygon = geometryElement
							.getChild("POLYGON");
					String polygonID = polygon.getAttributeValue("ID");
					// System.out.println("*****POLYGON ID***********" +
					// polygonID);
					vo.setPolygonID(polygonID);
					List pointElementsList = polygon.getChildren();

					// System.out.println("*****Polygon***********" +
					// pointElementsList);
					String polygonString = "";
					for (int j = 0; j < pointElementsList.size(); j++) {
						// System.out.println("*************************************"+j);
						org.jdom.Element point = (org.jdom.Element) pointElementsList
								.get(j);
						StringTokenizer tokens = new StringTokenizer(point
								.getValue(), ",");
						while (tokens.hasMoreTokens()) {
							vo.setPointX(tokens.nextToken());
							vo.setPointY(tokens.nextToken());
							vo.setPointZ(tokens.nextToken());
						}
						if (!polygonString.equals("")) {
							polygonString = polygonString + ", "
									+ vo.getPointX() + " " + vo.getPointY();
							;
						} else {
							polygonString = vo.getPointX() + " "
									+ vo.getPointY();
							;
						}

					}
					vo.setPolygonString(polygonString);
					list.add(vo);
				}

			}

			// Update the database
			System.out
					.println("********************Before entering into the database");
			BaseDAO dao = new BaseDAO();
			dao.addAnnotation(list, uniqueFilePath);

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public String updateCoordinates(String registrationID,
			String polygonString, String polygonID) {

		String pixelX = "";
		String pixelY = "";
		String xyPair = "";
		String transformedX = "";
		String transformedY = "";
		String firstPoint = "";
		String firstPointWithZ = "";
		String transformedCoordinateString = "";
		String transformedCoordinateStringWithZ = "";

		try {

			// 1) Get the tfw values from the oracle DB
			BaseDAO dao = new BaseDAO();
			AnnotationVO vo = dao.getTFWStringFromDB(registrationID);

			// 2) Run the method to convert pixels into coordinates for each set
			StringTokenizer xyPairTokens = new StringTokenizer(polygonString,
					",");

			while (xyPairTokens.hasMoreTokens()) {

				xyPair = xyPairTokens.nextToken();
				//System.out.println("XYPair is: " + xyPair);

				StringTokenizer coordinateTokens = new StringTokenizer(xyPair,
						" ");
				while (coordinateTokens.hasMoreTokens()) {
					pixelX = coordinateTokens.nextToken();
					pixelY = coordinateTokens.nextToken();
					//System.out.println("X is: " + pixelX);
					//System.out.println("Y is: " + pixelY);
					Point2D point = getTransformedCoordinates(pixelX, pixelY,
							vo);
					transformedX = String.valueOf(point.getX());
					transformedY = String.valueOf(point.getY());
				}

				if (!transformedCoordinateString.equals("")) {
					transformedCoordinateString = transformedCoordinateString
							+ ", " + transformedX + " " + transformedY;

					transformedCoordinateStringWithZ = transformedCoordinateStringWithZ
							+ ", " + transformedX + " " + transformedY + " 0";

				} else {
					transformedCoordinateString = transformedX + " "
							+ transformedY;
					firstPoint = transformedX + " " + transformedY;

					transformedCoordinateStringWithZ = transformedX + " "
							+ transformedY;
					firstPointWithZ = transformedX + " " + transformedY + " 0";

				}

			}
			// Just to make sure the polygon is closed, we will use the first
			// point
			transformedCoordinateString = transformedCoordinateString + ", "
					+ firstPoint;
			transformedCoordinateStringWithZ = transformedCoordinateStringWithZ
					+ ", " + firstPoint;

			// Update postgres database
			dao.updateAnnotation(polygonID, transformedCoordinateString,
					transformedCoordinateStringWithZ);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transformedCoordinateString;

	}

	public Point2D getTransformedCoordinates(String pixelX, String pixelY,
			AnnotationVO vo) {

		Point2D transformedCoordinates = null;

		System.out.println("TFW Line 1 from DB: "
				+ Double.parseDouble(vo.getTfwLine1()));
		System.out.println("TFW Line 2 from DB: "
				+ Double.parseDouble(vo.getTfwLine2()));
		System.out.println("TFW Line 3 from DB: "
				+ Double.parseDouble(vo.getTfwLine3()));
		System.out.println("TFW Line 4 from DB: "
				+ Double.parseDouble(vo.getTfwLine4()));
		System.out.println("TFW Line 5 from DB: "
				+ Double.parseDouble(vo.getTfwLine5()));
		System.out.println("TFW Line 6 from DB: "
				+ Double.parseDouble(vo.getTfwLine6()));

		System.out.println("PIXELX: " + pixelX);
		System.out.println("PIXELY: " + pixelY);

		try {

			AffineTransform at = new AffineTransform(Double.parseDouble(vo
					.getTfwLine1()), Double.parseDouble(vo.getTfwLine2()),
					Double.parseDouble(vo.getTfwLine3()), Double.parseDouble(vo
							.getTfwLine4()), Double.parseDouble(vo
							.getTfwLine5()), Double.parseDouble(vo
							.getTfwLine6()));

			transformedCoordinates = at.transform(new Point2D.Double(Double
					.parseDouble(pixelX), Double.parseDouble(pixelY)), null);

			/*
			 * double abaX = ( Double.parseDouble(pixelX) -
			 * Double.parseDouble(vo.getTfwLine3()) ) /
			 * Double.parseDouble(vo.getTfwLine1()); double abaY = (
			 * Double.parseDouble(pixelY) - Double.parseDouble(vo.getTfwLine6())
			 * ) / Double.parseDouble(vo.getTfwLine5());
			 * System.out.println("abaX: " + abaX); System.out.println("abaY: "
			 * + abaY);
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transformedCoordinates;

	}

}
