package org.incf.atlas.aba.dev;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xquery.XQException;

import org.incf.atlas.aba.util.XQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Get2DImageTest {
	
	private static final Logger logger = LoggerFactory.getLogger(
			Get2DImageTest.class);
	
	public static final ImageSeriesType IMAGE_SERIES_TYPE = ImageSeriesType.CORONAL;
	public static final String GENE_SYMBOL = "C1ql2";
	public static final double POI_X = 6272.0;
	public static final double POI_Y = 3678.0;
	public static final double POI_Z = 4874.0;
//	public static final SRSName SRS_NAME = SRSName.ABA_VOXEL;
	public static final String ZOOM_LEVEL = "25";  	// 100 = full resolution
	
	public static final Point3d POI = new Point3d(POI_X, POI_Y, POI_Z);
	
	public static final String ABA_BASE_URL = "http://www.brain-map.org/aba/api/";
	public static final String ABA_BASE_GENE_URL = ABA_BASE_URL + "gene";
	public static final String ABA_BASE_IMAGE_SERIES_URL = ABA_BASE_URL + "imageseries";
	public static final String ABA_BASE_MAP_URL = ABA_BASE_URL + "atlas/map/";
	public static final String ABA_SUFFIX_MAP = ".map";
	public static final String ABA_SUFFIX = ".xml";
	
	public static final String IMAGE_SERIES_META_INFO_FILE = 
		"src/main/resources/devData/ImageSeries71587929MetaInfo.xml";
	public static final String IMAGE_SERIES_ATLAS2IMAGE_MAP_FILE = 
		"/devData/ImageSeries71587929Atlas2ImageMap.txt";
	
	public Get2DImageTest() {
	}
	
	public void execute1() {
		double[] poiCoords = { POI_X, POI_Y, POI_Z };
		Point3d poi = new Point3d(poiCoords);
		
		// divide POI coords by 100
		Point3d poi100 = new Point3d(POI_X / 100, POI_Y / 100, POI_Z / 100);
		
		try {
			String line = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(
					this.getClass().getResourceAsStream(
							IMAGE_SERIES_ATLAS2IMAGE_MAP_FILE)));
			
			// discard first 2 lines
			in.readLine();
			in.readLine();
			double leastDistance = Double.POSITIVE_INFINITY;
			String[] bestLineSegs = null;
			int count = 2;
			while ((line = in.readLine()) != null) {
				String[] lineSegs = line.split(",");
				Point3d abaCoords = new Point3d(
						Double.parseDouble(lineSegs[0]),
						Double.parseDouble(lineSegs[1]),
						Double.parseDouble(lineSegs[2]));
				if (poi100.distanceSquared(abaCoords) < leastDistance) {
					leastDistance = poi100.distanceSquared(abaCoords);
					bestLineSegs = lineSegs;
				}
				
				// debug
				if ((++count % 100) == 0) {
					System.out.printf("%d%n", count);
				}
			}
			in.close();
			
			// debug
			if (logger.isDebugEnabled()) {
				StringBuilder buf = new StringBuilder();
				buf.append("\nGet2DImage POI           : ");
				for (int i = 0; i < 3; i++) {
					buf.append(poiCoords[i]).append(" ");
				}
				buf.append("\nClosest APA point        : ");
				for (int i = 0; i < 3; i++) {
					buf.append(bestLineSegs[i]).append(" ");
				}
				buf.append("\nABA x, y pixels, position: ");
				for (int i = 3; i < 6; i++) {
					buf.append(bestLineSegs[i]).append(" ");
				}
				buf.append("\nABA points examined      : ").append(count);
				logger.debug(buf.toString());
			}
			
			String abaPixelX = bestLineSegs[3];
			String abaPixelY = bestLineSegs[4];
//			int abaImagePosition = Integer.parseInt(bestLineSegs[5]);
			String abaImagePosition = bestLineSegs[5];
			
			String xQuery = 
				"for $x in doc('" + IMAGE_SERIES_META_INFO_FILE 
					+ "')/image-series/images/image "
				+ "where $x/position = " + abaImagePosition + " "
				+ "return $x/imageid/text()";
			String imageId = new XQuery().execute(xQuery);
			
			logger.debug("imageId: {}", imageId);
			
			String imageURL = assembleImageURL(imageId, abaPixelX, abaPixelY, 
					ZOOM_LEVEL);
			
			logger.debug("imageURL: {}", imageURL);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void execute2() {
	    try {
	        URL u = new URL(assembleGeneInfoURI("C1ql2"));
	        InputStream in = u.openStream();
	        XMLInputFactory factory = XMLInputFactory.newInstance();
	        XMLStreamReader parser = factory.createXMLStreamReader(in);
	          
	        boolean inISid = false;
	        boolean inPlane = false;
	        String isId = null;
	        String plane = null;
	        List<ImageSeries> imageSerieses = new ArrayList<ImageSeries>();
	        for (int event = parser.next();  
	        		event != XMLStreamConstants.END_DOCUMENT;
	        		event = parser.next()) {
//	        	switch (event) {
//	        	case XMLStreamConstants.START_ELEMENT:
	        	if (event == XMLStreamConstants.START_ELEMENT) {
	        		if (parser.getLocalName().equals("imageseriesid")) {
	        			inISid = true;
	        		}
	        		if (parser.getLocalName().equals("plane")) {
	        			inPlane = true;
	        		}
//	        		break;
//	        	case XMLStreamConstants.CHARACTERS:
	        	} else if (event == XMLStreamConstants.CHARACTERS) {
	        		if (inISid) {
	        			isId = parser.getText();
	        			inISid = false;
	        		}
	        		if (inPlane) {
	        			plane = parser.getText();
	        			if (plane.equals("coronal")) {
	        				imageSerieses.add(new ImageSeries(isId, 
	        						ImageSeriesType.CORONAL));
	        			} else if (plane.equals("sagittal")) {
	        				imageSerieses.add(new ImageSeries(isId, 
	        						ImageSeriesType.SAGITTAL));
	        			}
	        			inPlane = false;
	        		}
//	        		break;
	        	} // end switch
	        } // end for
	        parser.close();
	        
	        // debug
	        for (ImageSeries is : imageSerieses) {
	        	System.out.printf("is: %s, %s%n", is.id, is.type);
	        }
	    }
	    catch (XMLStreamException ex) {
	    	System.out.println(ex);
	    }
	    catch (IOException ex) {
	    	System.out.println("IOException while parsing ");
	    }
		
	}
	
	private class ImageSeries {
		public String id;
		public ImageSeriesType type;
		public ImageSeries(String id, ImageSeriesType type) {
			this.id = id;
			this.type = type;
		}
	}
	
	private String assembleGeneInfoURI(String geneSymbol) {
		return String.format(
				"http://www.brain-map.org/aba/api/gene/%s.xml", 
				geneSymbol);
	}
	
	/**
	 * Example: http://www.brain-map.org/aba/api/imageseries/71587929.xml
	 * 
	 * @param imageSeriesId
	 * @return
	 */
	private String assembleMetaInfoURL(String imageSeriesId) {
		return String.format(
				"http://www.brain-map.org/aba/api/imageseries/%s.xml", 
				imageSeriesId);
	}
	
	/**
	 * Example: http://www.brain-map.org/aba/api/atlas/map/71587929.map
	 * 
	 * @param imageSeriesId
	 * @return
	 */
	private String assembleAtlasMapURL(String imageSeriesId) {
		return String.format(
				"http://www.brain-map.org/aba/api/atlas/map/%s.map", 
				imageSeriesId);
	}
	
	/**
	 * Example: http://mouse.brain-map.org/viewImage.do?imageId=71424523
	 * 			&coordSystem=pixel&x=5916&y=3356&z=25
	 * 
	 * @param imageId
	 * @param abaPixelX
	 * @param abaPixelY
	 * @param zoomLevel
	 * @return
	 */
	private String assembleImageURL(String imageId, String abaPixelX, 
			String abaPixelY, String zoomLevel) {
		return String.format(
				"http://mouse.brain-map.org/viewImage.do?imageId=%s"
					+ "&coordSystem=pixel&x=%s&y=%s&z=%s", 
				imageId, abaPixelX, abaPixelY, zoomLevel);
	}
	
	public static void main(String[] args) {
		Get2DImageTest t = new Get2DImageTest();
		t.execute2();
	}
	
	public enum ImageSeriesType {
		CORONAL,
		SAGITTAL
	}
	
	public enum SRSName {
		ABA_VOXEL
	}

}
