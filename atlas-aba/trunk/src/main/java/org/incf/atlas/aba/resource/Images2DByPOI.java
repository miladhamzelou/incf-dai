package org.incf.atlas.aba.resource;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.vecmath.Point3d;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.incf.atlas.aba.util.DataInputs;
import org.incf.atlas.aba.util.ExceptionCode;
import org.incf.atlas.aba.util.ExceptionHandler;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/*
$ mysql -A -h mysql.crbs.ucsd.edu -u allen -p***** -D AllenBrainAtlas

Existing tables:
image_series (26,072 rows)
image (685,744 rows)

(1) Set up (one-time)
Create a new table:
CREATE TABLE image_series_map (
  id BIGINT AUTO INCREMENT PRIMARY KEY,
  imageseriesid BIGINT,
  plane VARCHAR(255),
  xcoord DOUBLE,
  ycoord DOUBLE,
  zcoord DOUBLE,
  xpixel INT,
  ypixel INT,
  position INT
) ENGINE = InnoDB;

Get list of imageSeriesId's:
SELECT imageseriesid. plane
FROM imageseries;

Populate image_series_map:
for each imageseriesid (26,072 times)
  read map file: http://www.brain-map.org/aba/api/map/[imageseriesid].map
  for each map file line (hundreds of thousands)
    INSERT INTO image_series_map
      (imageseriesid, plane, xcoord, ycoord, zcoord, xpixel, ypixel, position)
    VALUES (?, ?, ?, ?, ?, ?, ?);

(2) Get2DImagesByPOI procedure:
DataInputs: x, y, z, filter (coronal | sagittal)
Scale x, y, z by 0.01

SELECT imageseriesid, xpixel, ypixel, position
FROM image_series_map
WHERE [(closest | close) fit]
AND plane = '(coronal | sagittal)'

Using imageseriesid and position:
SELECT imageid, zoomifiednisslurl,thumbnailurl, expressthumbnailurl,
  downloadimagepath, [other metadata?]
FROM image
WHERE imageseriesid = ?
AND position = ?

Several question arise:
- Performance?
- What do we return?
	zoomifiednisslurl
	thumbnailurl
	expressthumbnailurl
    downloadimagepath
    other metadata
    ABA's Get Image URL (need more values: zoom, mime, top, left, width, height)
    Lydia's web app image URL (need more values: zoom level)
 - In the "closest | close" fit search
 	which, and if "close" how do we handle a list
 	what's the algorithm -- this is were PostGIS/Postgres is needed
   

 */

/**
 * Expected GET statement: http://<host:port>/atlas-aba
 *  ?service={service}
 *  &version={version}
 *  &request=Execute
 * 	&Identifier=Get2DImagesByPOI
 *  &DataInputs=srsName={srsName};x={x};y={y};z={z};gene={geneSymbol};zoom={zoomLevel}
 *  &ResponseForm={responseForm}
 *  
 *      
 * search procedure
 * with inputs x, y, z find closest line in image_series_map
 * with position, imageseriesid
 * 
 * 
 * 
 * 
 * @author dave
 *
 */
public class Images2DByPOI extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(getClass());
		
	public Images2DByPOI(Context context, Request request, Response response) {
		super(context, request, response);

		logger.debug("Instantiated {}.", getClass());
		
	}

	/* 
	 * Handle GET requests.
	 * 
	 * (non-Javadoc)
	 * @see org.wholebrainproject.wbc.server.resource.DataRepositoryResource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
	    
	    // make sure we have something in dataInputs
	    if (dataInputsString == null || dataInputsString.length() == 0) {
	        ExceptionHandler eh = getExceptionHandler();
	        eh.addExceptionToReport(ExceptionCode.MISSING_PARAMETER_VALUE, null, 
	                new String[] { "All DataInputs were missing." });
	        
	        // there is no point in going further, so return
	        return getExceptionRepresentation();
	    }
		
	    // parse dataInputs string
        DataInputs dataInputs = new DataInputs(dataInputsString);

        String srsName = dataInputs.getValue("srsName");
        
        // validate data inputs
        validateSrsName(srsName);
        Double[] poiCoords = validateCoordinate(dataInputs);
        
        // if any validation exceptions, no reason to continue
        if (exceptionHandler != null) {
            return getExceptionRepresentation();
        }
        
        // ok, now we have valid data input values
        Point3d poi = new Point3d(
                new double[] { poiCoords[0], poiCoords[1], poiCoords[2] });
        String geneSymbol = dataInputs.getValue("gene");
        int zoomLevel = Integer.parseInt(dataInputs.getValue("zoom"));
        
        // set serialization properties
        Properties props = new Properties();
        props.setProperty("method", "xml");
        props.setProperty("indent", "yes");
        props.setProperty("omit-xml-declaration", "no");
        props.setProperty("{http://saxon.sf.net/}indent-spaces", "2");
        
        // get aba coronal and sagittal image series ids based on gene
		//  /gene/image-series/image-series/plane { coronal | sagittal }
		//  /gene/image-series/image-series/imageseriesid
		List<ImageSeries> imageSerieses = retrieveImagesSeriesesForGene(
				geneSymbol);
		
		List<Image> images = new ArrayList<Image>();
		
		// for both
        for (ImageSeries is : imageSerieses) {
        	
        	// get atlas map
    		// find closest point, get other values including position
        	Image image = getClosestPosition(is, poi);
        	
    		// get best image id in image series based on position
    		// match position to find image in series, get imageid
    		//  /image-series/images/image/position
    		//  /image-series/images/image/imageid
        	String imageId = retrieveImageIdForPosition(is.imageSeriesId, 
        			image.abaImagePosition);
        	
        	image.imageId = imageId;
        	image.zoomLevel = zoomLevel;
        	
    		// assemble aba view image uri
        	image.imageURI = assembleViewImageURI(image);
        	images.add(image);
			
			// debug
			if (logger.isDebugEnabled()) {
				StringBuilder buf = new StringBuilder();
				buf.append("\nGet2DImage POI: ");
				buf.append(poi.x).append(", ");
				buf.append(poi.y).append(", ");
				buf.append(poi.z).append('\n');
				buf.append(image.toString()).append('\n');
				logger.debug(buf.toString());
			}
        }
        
        // build/return xml response
        /*
        Get2DImageByPOI-response
          dataInputs
            srsName
            poi
              x
              y
              z
            geneSymbol
            zoomLevel
          images
            image
              imagePlane
              imageURI
            image
              imagePlane
              imageURI
         */
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder parser = null;
		try {
			parser = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new ResourceException(e);
		}
	    Document doc = parser.newDocument();		
	    Element root = doc.createElement("Get2DImageByPOI-response");
	    doc.appendChild(root);
	    
	    Element eDataInputs = doc.createElement("dataInputs");
	    
	    Element eSrsName = doc.createElement("srsName");
	    eSrsName.setTextContent(srsName);
	    eDataInputs.appendChild(eSrsName);
	    
	    Element ePoi = doc.createElement("poi");
	    Element eX = doc.createElement("x");
	    eX.setTextContent(String.valueOf(poi.x));
	    ePoi.appendChild(eX);
	    Element eY = doc.createElement("y");
	    eY.setTextContent(String.valueOf(poi.y));
	    ePoi.appendChild(eY);
	    Element eZ = doc.createElement("x");
	    eZ.setTextContent(String.valueOf(poi.x));
	    ePoi.appendChild(eZ);
	    eDataInputs.appendChild(ePoi);

	    Element eGeneSymbol = doc.createElement("geneSymbol");
	    eGeneSymbol.setTextContent(geneSymbol);
	    eDataInputs.appendChild(eGeneSymbol);
	    
	    Element eZoomLevel = doc.createElement("zoomLevel");
	    eZoomLevel.setTextContent(String.valueOf(zoomLevel));
	    eDataInputs.appendChild(eZoomLevel);
	    
	    root.appendChild(eDataInputs);

	    Element eImages = doc.createElement("images");
	    for (Image im : images) {
	    	Element eImage = doc.createElement("image");
	    	
	    	Element eImageId = doc.createElement("imageId");
	    	eImageId.setTextContent(im.imageId);
	    	eImage.appendChild(eImageId);
	    	
	    	Element eImageSeriesId = doc.createElement("imageSeriesId");
	    	eImageSeriesId.setTextContent(im.imageSeries.imageSeriesId);
	    	eImage.appendChild(eImageSeriesId);
	    	
	    	Element eImageSeriesPlane = doc.createElement("imageSeriesPlane");
	    	eImageSeriesPlane.setTextContent(im.imageSeries.imageSeriesPlane.toString());
	    	eImage.appendChild(eImageSeriesPlane);
	    	
	    	Element eImagesCheckedForProximity = doc.createElement("imagesCheckedForProximity");
	    	eImagesCheckedForProximity.setTextContent(String.valueOf(im.imagesCheckedForProximity));
	    	eImage.appendChild(eImagesCheckedForProximity);

	    	Element eAbaCoordinates = doc.createElement("abaCoordinates");
		    Element eAbaX = doc.createElement("x");
		    eAbaX.setTextContent(String.valueOf(im.abaCoordinates.x));
		    eAbaCoordinates.appendChild(eAbaX);
		    Element eAbaY = doc.createElement("y");
		    eAbaY.setTextContent(String.valueOf(im.abaCoordinates.y));
		    eAbaCoordinates.appendChild(eAbaY);
		    Element eAbaZ = doc.createElement("x");
		    eAbaZ.setTextContent(String.valueOf(im.abaCoordinates.x));
		    eAbaCoordinates.appendChild(eAbaZ);
		    eImage.appendChild(eAbaCoordinates);

	    	Element eAbaXPixelPosition = doc.createElement("abaXPixelPosition");
	    	eAbaXPixelPosition.setTextContent(String.valueOf(im.abaXPixelPosition));
	    	eImage.appendChild(eAbaXPixelPosition);
	    	
	    	Element eAbaYPixelPosition = doc.createElement("abaYPixelPosition");
	    	eAbaYPixelPosition.setTextContent(String.valueOf(im.abaYPixelPosition));
	    	eImage.appendChild(eAbaYPixelPosition);
	    	
	    	Element eAbaImagePosition = doc.createElement("abaImagePosition");
	    	eAbaImagePosition.setTextContent(String.valueOf(im.abaImagePosition));
	    	eImage.appendChild(eAbaImagePosition);
	    	
	    	Element eImageURI = doc.createElement("imageURI");
	    	eImageURI.setTextContent(im.imageURI);
	    	eImage.appendChild(eImageURI);

	    	eImages.appendChild(eImage);
	    }
	    root.appendChild(eImages);

        
		try {

		} catch (Exception e) {
			throw new ResourceException(e);
		}

		// TODO validate
        
		String s = "\n" + images.get(0).imageURI + "\n" 
				+ images.get(1).imageURI + "\n";
		
		// return as file
        return new DomRepresentation(MediaType.APPLICATION_XML, doc);
	}
	
	private List<ImageSeries> retrieveImagesSeriesesForGene(String geneSymbol) {
        List<ImageSeries> imageSerieses = new ArrayList<ImageSeries>();
	    try {
	        URL u = new URL(assembleGeneInfoURI(geneSymbol));
	        InputStream in = u.openStream();
	        XMLInputFactory factory = XMLInputFactory.newInstance();
	        XMLStreamReader parser = factory.createXMLStreamReader(in);
	          
	        boolean inISid = false;
	        boolean inPlane = false;
	        String isId = null;
	        String plane = null;
	        for (int event = parser.next();  
	        		event != XMLStreamConstants.END_DOCUMENT;
	        		event = parser.next()) {
	        	if (event == XMLStreamConstants.START_ELEMENT) {
	        		if (parser.getLocalName().equals("imageseriesid")) {
	        			inISid = true;
	        		} else if (parser.getLocalName().equals("plane")) {
	        			inPlane = true;
	        		}
	        	} else if (event == XMLStreamConstants.CHARACTERS) {
	        		if (inISid) {
	        			isId = parser.getText();
	        			inISid = false;
	        		} else if (inPlane) {
	        			plane = parser.getText();
	        			if (plane.equals("coronal")) {
	        				imageSerieses.add(new ImageSeries(isId, 
	        						ImageSeriesPlane.CORONAL));
	        			} else if (plane.equals("sagittal")) {
	        				imageSerieses.add(new ImageSeries(isId, 
	        						ImageSeriesPlane.SAGITTAL));
	        			}
	        			inPlane = false;
	        		}
	        	}
	        }
	        parser.close();
	        
	        // debug
	        for (ImageSeries is : imageSerieses) {
	        	logger.debug("imageSeries: {}, {}", is.imageSeriesId, is.imageSeriesPlane);
	        }
	    }
	    catch (XMLStreamException ex) {
	    	System.out.println(ex);
	    }
	    catch (IOException ex) {
	    	System.out.println("IOException while parsing ");
	    }
	    return imageSerieses;
	}
	
	private Image getClosestPosition(ImageSeries imageSeries, Point3d poi) {
		Image image = new Image(imageSeries);
		try {
			
			// divide POI coords by 100
			Point3d poi100 = new Point3d();
			poi100.scale(0.01, poi);
			
			URL u = new URL(assembleAtlasMapURI(imageSeries.imageSeriesId));
			BufferedReader in = new BufferedReader(new InputStreamReader(
					u.openStream()));
			String line = null;
			
			// discard first 2 lines
			in.readLine();
			in.readLine();
			double leastDistance = Double.POSITIVE_INFINITY;
			String[] bestLineSegs = null;
			int count = 2;
			while ((line = in.readLine()) != null) {
				String[] lineSegs = line.split(",");
				Point3d abaCoordinates = new Point3d(
						Double.parseDouble(lineSegs[0]),
						Double.parseDouble(lineSegs[1]),
						Double.parseDouble(lineSegs[2]));
				if (poi100.distanceSquared(abaCoordinates) < leastDistance) {
					leastDistance = poi100.distanceSquared(abaCoordinates);
					bestLineSegs = lineSegs;
				}
				
				// debug
				if ((++count % 1000) == 0) {
					System.out.printf("%d%n", count);
				}
			}
			in.close();
			
			image.imagesCheckedForProximity = count;
			image.abaCoordinates = new Point3d(
					Double.parseDouble(bestLineSegs[0]),
					Double.parseDouble(bestLineSegs[1]),
					Double.parseDouble(bestLineSegs[2]));
			image.abaXPixelPosition = Integer.parseInt(bestLineSegs[3]);
			image.abaYPixelPosition = Integer.parseInt(bestLineSegs[4]);
			image.abaImagePosition = bestLineSegs[5];
			
			logger.debug("Points examined: {}", count);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	private String retrieveImageIdForPosition(String imageSeriesId,
			String position) {
        String imageId = null;
	    try {
	        URL u = new URL(assembleMetaInfoURI(imageSeriesId));
	        InputStream in = u.openStream();
	        XMLInputFactory factory = XMLInputFactory.newInstance();
	        XMLStreamReader parser = factory.createXMLStreamReader(in);
	          
	        boolean inImId = false;
	        boolean inPosition = false;
//	        String imId = null;
	        for (int event = parser.next();  
	        		event != XMLStreamConstants.END_DOCUMENT;
	        		event = parser.next()) {
	        	if (event == XMLStreamConstants.START_ELEMENT) {
	        		if (parser.getLocalName().equals("imageid")) {
	        			inImId = true;
	        		} else if (parser.getLocalName().equals("position")) {
	        			inPosition = true;
	        		}
	        	} else if (event == XMLStreamConstants.CHARACTERS) {
	        		if (inImId) {
	        			imageId = parser.getText();
	        			inImId = false;
	        		} else if (inPosition) {
	        			if (parser.getText().equals(position)) {
//	        				imageId = imId;
	        				break;
	        			}
	        			inPosition = false;
	        		}
	        	}
	        }
	        parser.close();
	        
	        // debug
	        logger.debug("imageId: {}", imageId);
	    }
	    catch (XMLStreamException ex) {
	    	System.out.println(ex);
	    }
	    catch (IOException ex) {
	    	System.out.println("IOException while parsing ");
	    }
	    return imageId;
	}
	
	/**
	 * Example: http://www.brain-map.org/aba/api/gene/C1ql2.xml
	 * 
	 * @param geneSymbol
	 * @return
	 */
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
	private String assembleMetaInfoURI(String imageSeriesId) {
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
	private String assembleAtlasMapURI(String imageSeriesId) {
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
//	private String assembleViewImageURI(String imageId, String abaPixelX, 
//			String abaPixelY, String zoomLevel) {
//		return String.format(
//				"http://mouse.brain-map.org/viewImage.do?imageId=%s"
//					+ "&coordSystem=pixel&x=%s&y=%s&z=%s", 
//				imageId, abaPixelX, abaPixelY, zoomLevel);
//	}
	
	private String assembleViewImageURI(Image image) {
		return String.format(
				"http://mouse.brain-map.org/viewImage.do?imageId=%s"
					+ "&coordSystem=pixel&x=%s&y=%s&z=%s", 
				image.imageId, image.abaXPixelPosition, 
				image.abaYPixelPosition, image.zoomLevel);
	}
	
	private class ImageSeries {
		public String imageSeriesId;
		public ImageSeriesPlane imageSeriesPlane;
		public ImageSeries(String id, ImageSeriesPlane plane) {
			this.imageSeriesId = id;
			this.imageSeriesPlane = plane;
		}
		public String toString() {
				return String.format("ImageSeries id: %s, type: %s", 
				imageSeriesId, imageSeriesPlane);
		}
	}
	
	private class Image {
		public String imageId;
		public ImageSeries imageSeries;
		public Point3d abaCoordinates;
		public int imagesCheckedForProximity;
		public int abaXPixelPosition;
		public int abaYPixelPosition;
		public String abaImagePosition;
		public int zoomLevel;
		public String imageURI;
		public Image(ImageSeries imageSeries) {
			this.imageSeries = imageSeries;
		}
		public String toString() {
			return String.format("Image: id: %s; ImageSeries: %s, "
					+ "abaCoordinates: %d, %d, %d; abaXYPixelPosition %d, %d; "
					+ "abaImagePosition: %s, zoomLevel: %d, imageURI: %s", 
					imageId, imageSeries, (int) abaCoordinates.x, 
					(int) abaCoordinates.y, (int) abaCoordinates.z,
					abaXPixelPosition, abaYPixelPosition, abaImagePosition, 
					zoomLevel, imageURI);
		}
	}
	
	public enum ImageSeriesPlane {
		CORONAL,
		SAGITTAL
	}
	
}
