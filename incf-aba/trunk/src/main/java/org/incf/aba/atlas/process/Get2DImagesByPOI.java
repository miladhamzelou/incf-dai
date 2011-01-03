package org.incf.aba.atlas.process;

import java.io.BufferedReader;
import java.io.File;
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
import javax.xml.stream.XMLStreamWriter;

import org.apache.xmlbeans.XmlOptions;
import org.deegree.commons.utils.kvp.InvalidParameterValueException;
import org.deegree.commons.utils.kvp.MissingParameterException;
import org.deegree.commons.xml.XMLAdapter;
import org.deegree.services.controller.exception.ControllerException;
import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.wps.Processlet;
import org.deegree.services.wps.ProcessletException;
import org.deegree.services.wps.ProcessletExecutionInfo;
import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.ProcessletOutputs;
import org.deegree.services.wps.output.ComplexOutput;
import org.incf.atlas.waxml.generated.Image2DType;
import org.incf.atlas.waxml.generated.Image2DType.ImageSource;
import org.incf.atlas.waxml.generated.ImagesResponseDocument;
import org.incf.atlas.waxml.generated.ImagesResponseType;
import org.incf.atlas.waxml.generated.ImagesResponseType.Image2Dcollection;
import org.incf.atlas.waxml.generated.IncfRemoteFormatEnum;
import org.incf.atlas.waxml.utilities.Utilities;
import org.incf.common.atlas.exception.InvalidDataInputValueException;
import org.incf.common.atlas.util.AllowedValuesValidator;
import org.incf.common.atlas.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Get2DImagesByPOI implements Processlet {

    private static final Logger LOG = LoggerFactory.getLogger(
            Get2DImagesByPOI.class);
    
    private static final String PROCESS_DEFINITION_DIR = 
    		"aba/WEB-INF/workspace/processes/";
    private static final String PROCESS_DEFINITION_FILE = 
    		PROCESS_DEFINITION_DIR + "Get2DImagesByPOI.xml";

	// used for ABA Get Image URI query string
	private static final String HI_RES = "-1";	// highest resolution available
	private static final String THUMB = "0";	// thumbnail
	private static final String MIME = "2";		// jpeg/image
	private static final int NBR_STRONG_GENES = 1;	// strong genes to get
	
	private ResponseValues responseValues;
	
    @Override
    public void process(ProcessletInputs in, ProcessletOutputs out, 
            ProcessletExecutionInfo info) throws ProcessletException {
    	try {

    		// collect input values
    		String srsName = Util.getStringInputValue(in, "srsName");
    		double x = Util.getDoubleInputValue(in, "x");
    		double y = Util.getDoubleInputValue(in, "y");
    		double z = Util.getDoubleInputValue(in, "z");
    		String filter = Util.getStringInputValue(in, "filter");
    		
    		// validate against allowed values in process definition file
    		AllowedValuesValidator validator = new AllowedValuesValidator(
    				new File(PROCESS_DEFINITION_FILE));
    		if (!validator.validate("srsName", srsName)) {
    			throw new InvalidDataInputValueException("The srsName value '" 
    					+ srsName + "' is not amoung the allowed values "
    					+ "specified in the AllowedValues element of the "
    					+ "ProcessDescription.", "srsName");
    		}
    		if (!validator.validate("filter", filter)) {
    			throw new InvalidDataInputValueException("The filter value '" 
    					+ filter + "' is not amoung the allowed values "
    					+ "specified in the AllowedValues element of the "
    					+ "ProcessDescription.", "filter");
    		}

    		LOG.debug(String.format(
    				"DataInputs: srsName: %s, poi: (%f, %f, %f), filter: %s",
    				srsName, x, y, z, filter));

    		responseValues = new ResponseValues();
    		responseValues.clientSrsName = srsName;

    		// TODO convert srs to agea, if not agea

    		String srsFromClient = srsName;
    		Point3d poiFromClient = new Point3d(x, y, z);

    		// get plane; defaults to sagittal
    		ImageSeriesPlane desiredPlane = filter.equals("maptype:coronal")
    				? ImageSeriesPlane.CORONAL : ImageSeriesPlane.SAGITTAL;

    		// 1. get strong gene(s) at POI
    		List<String> strongGenes = retrieveStrongGenesAtPOI(x, y, z,
    				NBR_STRONG_GENES);

    		// make sure we have something
    		if (strongGenes.size() == 0) {
    			throw new OWSException("No 'strong genes' found at "
    						+ "coordinates, hence no images to return.", 
    					ControllerException.NO_APPLICABLE_CODE);
    		}

    		if (LOG.isDebugEnabled()) {
    			StringBuilder buf = new StringBuilder();
    			for (String gene : strongGenes) {
    				buf.append(gene).append(", ");
    			}
    			LOG.debug("Strong genes: {}", buf.toString());
    		}

    		// 2. get image series'es for strong genes and desired plane
    		List<ImageSeries> imageSerieses = new ArrayList<ImageSeries>();
    		for (String geneSymbol : strongGenes) {
    			ImageSeries imageSeries = retrieveImagesSeriesForGene(
    					geneSymbol, desiredPlane);
    			if (imageSeries != null) {
    				imageSerieses.add(imageSeries);
    			}
    		}

    		// make sure we have something
    		if (imageSerieses.size() == 0) {
    			throw new OWSException("No image series found for 'strong "
    					+ "genes' and desired plane, hence no images to "
    					+ "return.", 
					ControllerException.NO_APPLICABLE_CODE);
    		}

    		if (LOG.isDebugEnabled()) {
    			StringBuilder buf = new StringBuilder();
    			for (ImageSeries is : imageSerieses) {
    				buf.append(is.imageSeriesId).append(':');
    				buf.append(is.imageSeriesPlane).append(", ");
    			}
    			LOG.debug("Image Serieses id:plane: {}", buf.toString());
    		}

    		Point3d poiForProximity = new Point3d(poiFromClient);

    		// divide POI coords by 100
    		Point3d poi100 = new Point3d();
    		poi100.scale(0.01, poiForProximity);

    		LOG.debug("POI for closest position (srs, xyz): {}, {}", 
    				srsFromClient, poi100.toString());

    		// 3. get ......... for each image series
    		for (ImageSeries imageSeries : imageSerieses) {

    			// begin to add values to image
    			Image image = new Image(imageSeries.imageSeriesId);

    			// get atlas map
    			// find closest point, get other values including position
    			// add more (atlas map) values to image
    			getClosestPosition(imageSeries, poi100, image);

    			LOG.debug("Position: {}", image.abaImagePosition);

    			// get best image id in image series based on position
    			// add more (image elements) values to image
    			// match position to find image in series, get imageid
    			//  /image-series/images/image/position
    			//  /image-series/images/image/imageid
    			retrieveImageForPosition(imageSeries.imageSeriesId, 
    					image.abaImagePosition, image);

    			LOG.debug("Image id: {}", image.imageId);       	

    			// zoom level not applicable
    			//image.zoomLevel = zoomLevel;

    			// assemble aba view image uri
    			image.imageURI = assembleImageURI(image.downloadImagePath, 
    					HI_RES, MIME);
    			image.thumbnailurl = assembleImageURI(image.downloadImagePath, 
    					THUMB, MIME);

    			LOG.debug("Image URI: {}", image.imageURI.toString());

    			responseValues.images.add(image);
    		} // for

    		// ComplexOutput objects can become very huge; it is essential to 
    		// stream result.
    		// get ComplexOutput object from ProcessletOutput...
    		ComplexOutput complexOutput = (ComplexOutput) out.getParameter(
    				"Get2DImagesByPOIOutput");
    		LOG.debug("Setting complex output (requested=" 
    				+ complexOutput.isRequested() + ")");

    		// ImagesResponseDocument 'is a' org.apache.xmlbeans.XmlObject
    		//	'is a' org.apache.xmlbeans.XmlTokenSource
    		ImagesResponseDocument document = completeResponse();

    		if (LOG.isDebugEnabled()) {
    			XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
    			opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
    			opt.setSaveNamespacesFirst();
    			opt.setSaveAggressiveNamespaces();
    			opt.setUseDefaultNamespace();
    			LOG.debug("Xml:\n{}", document.xmlText(opt));
    		}

    		// get reader on document; reader --> writer
    		XMLStreamReader reader = document.newXMLStreamReader();
    		XMLStreamWriter writer = complexOutput.getXMLStreamWriter();
    		XMLAdapter.writeElement(writer, reader);
    		
    		// transform any exceptions into ProcessletException wrapping
    		// OWSException
        } catch (MissingParameterException e) {
            LOG.error(e.getMessage(), e);
        	throw new ProcessletException(new OWSException(e));
        } catch (InvalidParameterValueException e) {
            LOG.error(e.getMessage(), e);
        	throw new ProcessletException(new OWSException(e));
        } catch (InvalidDataInputValueException e) {
            LOG.error(e.getMessage(), e);
        	throw new ProcessletException(e);	// is already OWSException
        } catch (OWSException e) {
            LOG.error(e.getMessage(), e);
        	throw new ProcessletException(e);	// is already OWSException
        } catch (Throwable e) {
        	String message = "Unexpected exception occurred: " + e.getMessage();
        	LOG.error(message, e);
        	throw new ProcessletException(new OWSException(message, e, 
        			ControllerException.NO_APPLICABLE_CODE));
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }
    
	private List<String> retrieveStrongGenesAtPOI(double x, double y, 
			double z, int nbrStrongGenes) throws IOException, 
					XMLStreamException {
        List<String> strongGenes = new ArrayList<String>();
	    	
        // round coords to nearest xx00, where xx is even
        URL u = new URL(assembleGeneFinderURI(
        		String.valueOf(round200(x)), 
        		String.valueOf(round200(y)), 
        		String.valueOf(round200(z))));

        LOG.debug("Gene finder URI: {}", u.toString());

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
        try {
        	parser.close();
        } catch (XMLStreamException e) {
        	LOG.warn(e.getMessage(), e);		// log but go on
        }

        // debug
        for (String gene : strongGenes) {
        	LOG.debug("gene: {}", gene);
        }
	    return strongGenes;
	}
	
	private ImageSeries retrieveImagesSeriesForGene(String geneSymbol,
			ImageSeriesPlane desiredPlane) throws IOException, 
					XMLStreamException {
		ImageSeries imageSeries = null;
		URL u = new URL(assembleGeneURI(geneSymbol));

		LOG.debug("Gene info URI: {}", u.toString());

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
		try {
			parser.close();
		} catch (XMLStreamException e) {
			LOG.warn(e.getMessage(), e);		// log but go on
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
	 * @throws IOException 
	 */
	private void getClosestPosition(ImageSeries imageSeries, Point3d poi100,
			Image image) throws IOException {
		URL u = new URL(assembleAtlasMapURI(imageSeries.imageSeriesId));

		LOG.debug("Atlas map URI: {}", u.toString());

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

		LOG.debug("Points examined: {}", count);
	}
	
	private void retrieveImageForPosition(String imageSeriesId, String position, 
			Image image) throws IOException, XMLStreamException {
		URL u = new URL(assembleImageSeriesURI(imageSeriesId));

		LOG.debug("Meta info URI: {}", u.toString());

		InputStream in = u.openStream();
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory.createXMLStreamReader(in);

		boolean inImDisplayName = false;
		boolean inImId = false;
		boolean inPosition = false;
		boolean inThumbnailurl = false;
		boolean inDownloadImagePath = false;
		boolean positionMatch = false;
		for (int event = parser.next();  
		event != XMLStreamConstants.END_DOCUMENT;
		event = parser.next()) {
			if (event == XMLStreamConstants.START_ELEMENT) {
				if (parser.getLocalName().equals("imagedisplayname")) {
					inImDisplayName = true;
				} else if (parser.getLocalName().equals("imageid")) {
					inImId = true;
				} else if (parser.getLocalName().equals("position")) {
					inPosition = true;
				} else if (parser.getLocalName().equals("thumbnailurl")) {
					inThumbnailurl = true;
				} else if (parser.getLocalName().equals("downloadImagePath")) {
					inDownloadImagePath = true;
				}
			} else if (event == XMLStreamConstants.CHARACTERS) {

				// element sequence is significant! imagedisplayname and
				// imageid precede position which is match value
				if (inImDisplayName) {
					image.imagedisplayname = parser.getText();
					inImDisplayName = false;
				} else if (inImId) {
					image.imageId = parser.getText();
					inImId = false;
				} else if (inPosition) {
					if (parser.getText().equals(position)) {
						positionMatch = true;
					}
					inPosition = false;
				} else if (inThumbnailurl) {
					if (positionMatch) {
						image.thumbnailurl = parser.getText();
					}
					inThumbnailurl = false;
				} else if (inDownloadImagePath) {
					if (positionMatch) {
						image.downloadImagePath = parser.getText();
						break;
					}
					inDownloadImagePath = false;
				}
			}
		}
		try {
			parser.close();
		} catch (XMLStreamException e) {
			LOG.warn(e.getMessage(), e);		// log but go on
		}

		LOG.debug(
				"imageId: {}\n  thumbnailurl: {}\n  downloadImagePath: {}", 
				new String[] { image.imageId, image.thumbnailurl, 
						image.downloadImagePath });
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
	private String assembleGeneURI(String geneSymbol) {
		return String.format(
				"http://www.brain-map.org/aba/api/gene/%s.xml", 
				geneSymbol);
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
	 * Example: http://www.brain-map.org/aba/api/imageseries/71587929.xml
	 * 
	 * @param imageSeriesId
	 * @return
	 */
	private String assembleImageSeriesURI(String imageSeriesId) {
		return String.format(
				"http://www.brain-map.org/aba/api/imageseries/%s.xml", 
				imageSeriesId);
	}
	
	/**
	 * Example: 
	 * http://www.brain-map.org/aba/api/image
	 * ?zoom=[zoom]&path=[filePath]&mime=[mime]
	 * 
	 * @param filePath
	 * @param zoom
	 * @param mime
	 * @return
	 */
	private String assembleImageURI(String filePath, String zoom, String mime) {
		return String.format(
			"http://www.brain-map.org/aba/api/image?zoom=%s&path=%s&mime=%s", 
			zoom, filePath, mime);
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
	
	// image data available from ABA
	private class Image {
		public String imageId;
		public String imageSeriesId;
		public int imagesCheckedForProximity;
		public int zoomLevel;
		public String imageURI;
		
		// from image series map closest point line
		public Point3d abaCoordinates;
		public int abaXPixelPosition;
		public int abaYPixelPosition;
		public String abaImagePosition;
		
		// from (xPath) /image-series/images/image/* (partial)
		//public String imagecreatedate;
		public String imagedisplayname;
		//public String imageid;
		//public String referenceAtlasIndex;
		//public String zoomifiednissurl;
		public String thumbnailurl;
		//public String expressthumbnailurl;
		public String downloadImagePath;
		//public String downloadExpressionPath;
		
		// from (xPath) /IMAGE_PROPERTIES/*
		//public int width;
		//public int height;
		//public int numTiles;
		//public int numTiers;
		//public int numImages;
		//public String version;
		//public int tileZize;
		
		//public String imageUrl;
		
		public Image(String imageSeriesId) {
			this.imageSeriesId = imageSeriesId;
		}
		
		public String toString() {
			return String.format("Image: id: %s; ImageSeries: %s, "
					+ "abaCoordinates: %d, %d, %d; abaXYPixelPosition %d, %d; "
					+ "abaImagePosition: %s, zoomLevel: %d, imageURI: %s", 
					imageId, imageSeriesId, (int) abaCoordinates.x, 
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
	
	private class ResponseValues {
		private String clientSrsName;
		private List<Image> images = new ArrayList<Image>();
	}
	
	private ImagesResponseDocument completeResponse() {
		ImagesResponseDocument document = 
				ImagesResponseDocument.Factory.newInstance();
		ImagesResponseType eImagesResponse = document.addNewImagesResponse();
		Image2Dcollection eImage2DCollection = 
				eImagesResponse.addNewImage2Dcollection();
		for (Image im : responseValues.images) {
			Image2DType eImage2D = eImage2DCollection.addNewImage2D();
			ImageSource eImageSource = eImage2D.addNewImageSource();
			eImageSource.setStringValue(im.imageURI);
			eImageSource.setFormat(IncfRemoteFormatEnum.IMAGE_JPEG.toString());
			eImageSource.setName(im.imagedisplayname);
			eImageSource.setSrsName(responseValues.clientSrsName);
			eImageSource.setThumbnail(im.thumbnailurl);
		}
		return document;
	}
	
	public static int round200(double a) {
		return ((int) ((a / 200) + 0.5)) * 200;
	}
	
}
