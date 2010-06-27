package org.incf.atlas.aba.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.vecmath.Point3d;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.incf.atlas.aba.util.DataInputs;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Expected GET statement: http://<host:port>/atlas-aba$request=Execute
 * 	&identifier=Get2DImagesByPOI&dataInputs=srsName=<srs name>;x=<x-coord>
 * 	;y=<y-coord>;z=<z-coord>;gene=<gene-symbol>;zoom=<zoom-level>
 * 
 * @author dave
 *
 */
public class Images2DByPOI extends Resource {

	private static final Logger logger = LoggerFactory.getLogger(
			Images2DByPOI.class);
	
	private String dataInputsString;
	
	public Images2DByPOI(Context context, Request request, Response response) {
		super(context, request, response);
		dataInputsString = (String) request.getAttributes().get("dataInputs");
	}

	/* 
	 * Handle GET requests.
	 * 
	 * (non-Javadoc)
	 * @see org.wholebrainproject.wbc.server.resource.DataRepositoryResource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		
        // set serialization properties
        Properties props = new Properties();
        props.setProperty("method", "xml");
        props.setProperty("indent", "yes");
        props.setProperty("omit-xml-declaration", "no");
        props.setProperty("{http://saxon.sf.net/}indent-spaces", "2");
        
		if (dataInputsString == null) {
			// TODO error
		}
		
		DataInputs dataInputs = new DataInputs(dataInputsString);

        // validate data inputs 
        //	srsName, x, y, z, gene, zoom
        
		double[] poiCoords = { 
				Double.parseDouble(dataInputs.getValue("x")),
				Double.parseDouble(dataInputs.getValue("y")),
				Double.parseDouble(dataInputs.getValue("z")) };
		Point3d poi = new Point3d(poiCoords);
		String geneSymbol = dataInputs.getValue("gene");
		String zoomLevel = dataInputs.getValue("zoom");
		
		// divide POI coords by 100
		Point3d poi100 = new Point3d();
		poi100.scale(0.01, poi);
		
        // aba get gene response to get coronal and sagittal image series ids
		//  /gene/image-series/image-series/plane { coronal | sagittal }
		//  /gene/image-series/image-series/imageseriesid
		List<ImageSeries> imageSerieses = retrieveImagesSeriesesForGene(
				geneSymbol);
		
		// for both
        for (ImageSeries is : imageSerieses) {

        	// get atlas map
    		// find closest point, get other values including position
        	
    		// get imageseries
    		// match position to find image in series, get imageid
    		//  /image-series/images/image/position
    		//  /image-series/images/image/imageid
    		// assemble aba view image url
        }
        
        // build/return xml response
        
		try {

		} catch (Exception e) {
			throw new ResourceException(e);
		}

		// TODO validate
        
		// return as file
        return new FileRepresentation(new File(""), MediaType.APPLICATION_XML);
	}
	
	public List<ImageSeries> retrieveImagesSeriesesForGene(String geneSymbol) {
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
	        						ImageSeriesType.CORONAL));
	        			} else if (plane.equals("sagittal")) {
	        				imageSerieses.add(new ImageSeries(isId, 
	        						ImageSeriesType.SAGITTAL));
	        			}
	        			inPlane = false;
	        		}
	        	}
	        }
	        parser.close();
	        
	        // debug
	        for (ImageSeries is : imageSerieses) {
	        	logger.debug("imageSeries: {}, {}", is.id, is.type);
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
	private String assembleViewImageURI(String imageId, String abaPixelX, 
			String abaPixelY, String zoomLevel) {
		return String.format(
				"http://mouse.brain-map.org/viewImage.do?imageId=%s"
					+ "&coordSystem=pixel&x=%s&y=%s&z=%s", 
				imageId, abaPixelX, abaPixelY, zoomLevel);
	}
	
	private class ImageSeries {
		public String id;
		public ImageSeriesType type;
		public ImageSeries(String id, ImageSeriesType type) {
			this.id = id;
			this.type = type;
		}
	}
	
	public enum ImageSeriesType {
		CORONAL,
		SAGITTAL
	}
	
}
