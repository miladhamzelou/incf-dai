package org.incf.atlas.aba.resource;

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

import org.incf.atlas.aba.util.DataInputs;
import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Images2DByPOI_new extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public Images2DByPOI_new(Context context, Request request, Response response) {
		super(context, request, response);

		logger.debug("Instantiated {}.", getClass());
	}

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
        
        // TODO validate srsName
/*
Q: what is required value of srsName in Get2DImagesByPOI for using the x, y, z
values to go to ABA with
http://mouse.brain-map.org/agea/GeneFinder.xml?seedPoint=6600,4000,5600 
Confirm "Mouse_AGEA_1.0"?
 */
        // validate data inputs
        validateSrsName(srsName);

        Double[] poiCoords = validateCoordinate(dataInputs);
        
        // TODO get/validate filter; defaults to sagittal
        String filter = dataInputs.getValue("filter");
        ImageSeriesPlane desiredPlane = filter.equals("maptype:coronal")
        		? ImageSeriesPlane.CORONAL : ImageSeriesPlane.SAGITTAL;
        
        // if any validation exceptions, no reason to continue
        if (exceptionHandler != null) {
            return getExceptionRepresentation();
        }
        
        // 1. get strong gene(s) at POI
        int nbrStrongGenes = 1;
        List<String> strongGenes = retrieveStrongGenesAtPOI(
        		dataInputs.getValue("x"), dataInputs.getValue("y"), 
        		dataInputs.getValue("z"), nbrStrongGenes);

        // make sure we have something
        if (strongGenes.size() == 0) {
        	// TODO logger.error none found, exceptionRpt, bail
        }
      
        // 2. get image series'es for stong genes and desired plane
        List<ImageSeries> imageSerieses = new ArrayList<ImageSeries>();
		for (String geneSymbol : strongGenes) {
			ImageSeries is = retrieveImagesSeriesForGene(geneSymbol, 
					desiredPlane);
			if (is != null) {
				imageSerieses.add(is);
			}
		}
		
        // make sure we have something
        if (imageSerieses.size() == 0) {
        	// TODO logger.error none found, exceptionRpt, bail
        }
      
        // 3. get ......... for each image series
		List<Image> images = new ArrayList<Image>();
        for (ImageSeries is : imageSerieses) {
        	
        	// get atlas map
    		// find closest point, get other values including position
//        	Image image = getClosestPosition(is, poi);
        	
    		// get best image id in image series based on position
    		// match position to find image in series, get imageid
    		//  /image-series/images/image/position
    		//  /image-series/images/image/imageid
//        	String imageId = retrieveImageIdForPosition(is.imageSeriesId, 
//        			image.abaImagePosition);
//        	
//        	image.imageId = imageId;
        	
        	// zoom level not applicable
        	//image.zoomLevel = zoomLevel;
        	
    		// assemble aba view image uri
//        	image.imageURI = assembleViewImageURI(image);
//        	images.add(image);
			
			// debug
			if (logger.isDebugEnabled()) {
				StringBuilder buf = new StringBuilder();
				buf.append("\nGet2DImage POI: ");
//				buf.append(poi.x).append(", ");
//				buf.append(poi.y).append(", ");
//				buf.append(poi.z).append('\n');
//				buf.append(image.toString()).append('\n');
				logger.debug(buf.toString());
			}
        } // for
        
		// TODO
		return null;
	}
	
	private List<String> retrieveStrongGenesAtPOI(String x, String y, 
			String z, int nbrStrongGenes) {
        List<String> strongGenes = new ArrayList<String>();
	    try {
	        URL u = new URL(assembleGeneFinderURI(x, y, z));
	        InputStream in = u.openStream();
	        XMLInputFactory factory = XMLInputFactory.newInstance();
	        XMLStreamReader parser = factory.createXMLStreamReader(in);
	          
	        boolean inGeneSymbol = false;
	        String geneSymbol = null;
	        int i = 0;
	        for (int event = parser.next();  
	        		event != XMLStreamConstants.END_DOCUMENT;
	        		event = parser.next()) {
	        	if (event == XMLStreamConstants.START_ELEMENT) {
	        		if (parser.getLocalName().equals("genesymbol")) {
	        			inGeneSymbol = true;
	        		}
	        	} else if (event == XMLStreamConstants.CHARACTERS) {
	        		if (inGeneSymbol) {
	        			geneSymbol = parser.getText();
	        			strongGenes.add(geneSymbol);
	        			i++;
	        			if (i >= nbrStrongGenes) {
	        				break;
	        			}
	        			inGeneSymbol = false;
	        		}
	        	}
	        }
	        parser.close();
	        
	        // debug
	        for (String gene : strongGenes) {
	        	logger.debug("gene: {}", gene);
	        }
	    }
	    catch (XMLStreamException ex) {
	    	System.out.println(ex);
	    }
	    catch (IOException ex) {
	    	System.out.println("IOException while parsing ");
	    }
	    return strongGenes;
	}
	
	private ImageSeries retrieveImagesSeriesForGene(String geneSymbol,
			ImageSeriesPlane desiredPlane) {
		ImageSeries imageSeries = null;
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
	        			if (plane.equals(desiredPlane.toString())) {
	        				imageSeries = new ImageSeries(isId, 
	        						desiredPlane);
	        			}
	        			inPlane = false;
	        		}
	        	}
	        }
	        parser.close();
	        
	        if (imageSeries != null) {
	        	logger.debug("imageSeries: {}, {}", imageSeries.imageSeriesId, 
	        			imageSeries.imageSeriesPlane);
	        }
	    } catch (XMLStreamException ex) {
	    	System.out.println(ex);
	    }
	    catch (IOException ex) {
	    	System.out.println("IOException while parsing ");
	    }
	    return imageSeries;
	}
	
	/**
	 * Finds the position value in the image series map that is closest to the
	 * POI. This partially populates an Image object but does NOT include the
	 * image id.
	 * 
	 * @param imageSeries
	 * @param poi
	 * @return
	 */
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
//			image.abaImagePosition = bestLineSegs[5];
			
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
	private String assembleGeneFinderURI(String x, String y, String z) {
		return String.format(
			"http://mouse.brain-map.org/agea/GeneFinder.xml?seedPoint=%s,%s,%s", 
			x, y, z);
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
		public ImageSeries imageSeries;			// image series
		public int imagesCheckedForProximity;
		public int zoomLevel;
		public String imageURI;
		
		// from image series map closest point line
		public Point3d abaCoordinates;
		public int abaXPixelPosition;
		public int abaYPixelPosition;
		public int abaImagePosition;
		
		// from (xPath) /image-series/images/image/* (partial)
		public String imagecreatedate;
		public String imageid;
		public String referenceAtlasIndex;
		public String zoomifiednissurl;
		public String thumbnailurl;
		public String expressthumbnailurl;
		public String downloadImagePath;
		public String downloadExpressionPath;
		
		// from (xPath) /IMAGE_PROPERTIES/*
		public int width;
		public int height;
		public int numTiles;
		public int numTiers;
		public int numImages;
		public String version;
		public int tileZize;
		
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
		CORONAL, SAGITTAL;
		
		@Override 
		public String toString() {
			return super.toString().toLowerCase();
		}
	}
	
}
