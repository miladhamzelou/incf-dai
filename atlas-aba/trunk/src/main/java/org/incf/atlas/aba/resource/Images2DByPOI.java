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

import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.aba.util.DataInputs;
import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.incf.atlas.waxml.generated.Image2DType;
import org.incf.atlas.waxml.generated.ImagesResponseDocument;
import org.incf.atlas.waxml.generated.ImagesResponseType;
import org.incf.atlas.waxml.generated.IncfRemoteFormatEnum;
import org.incf.atlas.waxml.generated.InputStringType;
import org.incf.atlas.waxml.generated.QueryInfoType;
import org.incf.atlas.waxml.generated.Image2DType.ImageSource;
import org.incf.atlas.waxml.generated.ImagesResponseType.Image2Dcollection;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.utilities.Utilities;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Images2DByPOI extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
// test
// http://incf-dev-local.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName=Mouse_AGEA_1.0;x=6600;y=4000;z=5600;filter=maptype:coronal
	
	// used for ABA Get Image URI query string
	private static final String HI_RES = "-1";	// highest resolution available
	private static final String THUMB = "0";	// thumbnail
	private static final String MIME = "2";		// jpeg/image
	
	private ResponseValues responseValues;
	
	public Images2DByPOI(Context context, Request request, Response response) {
		super(context, request, response);

		logger.debug("Instantiated {}.", getClass());
		
		responseValues = new ResponseValues();
		responseValues.clientUri = request.getOriginalRef().toString();
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
	    
	    logger.debug("dataInputsString: {}", dataInputsString);
		
	    // parse dataInputs string
        DataInputs dataInputs = new DataInputs(dataInputsString);

        String srsName = dataInputs.getValue("srsName");
        responseValues.clientSrsName = srsName;
        responseValues.clientX = dataInputs.getValue("x");
        responseValues.clientY = dataInputs.getValue("y");
        responseValues.clientZ = dataInputs.getValue("z");
        responseValues.clientFilter = dataInputs.getValue("filter");
        
        // TODO validate srsName
        // validate data inputs
        validateSrsName(srsName);

        String srsFromClient = srsName;
        Double[] poiFrClient = validateCoordinate(dataInputs);
        Point3d poiFromClient = new Point3d(poiFrClient[0], poiFrClient[1], 
        		poiFrClient[2]);
        
        logger.debug("POI from client (srs, xyz): {}, {}", srsFromClient, 
        		poiFromClient.toString());
        
        // TODO get/validate filter; defaults to sagittal
        String filter = dataInputs.getValue("filter");
        ImageSeriesPlane desiredPlane = filter.equals("maptype:coronal")
        		? ImageSeriesPlane.CORONAL : ImageSeriesPlane.SAGITTAL;
        
        // TODO translate incoming coordinates to WHS 0.9?
        // TODO need agea coordinates
        // TODO for now just copy
        String srsGetStrongGenes = srsFromClient;
        Point3d poiGetStrongGenes = new Point3d(poiFromClient);
        
        // if any validation exceptions, no reason to continue
        if (exceptionHandler != null) {
            return getExceptionRepresentation();
        }
        
        logger.debug("POI for strong genes (srs, xyz): {}, {}", 
        		srsGetStrongGenes, poiGetStrongGenes.toString());
        
        // 1. get strong gene(s) at POI
        int nbrStrongGenes = 1;			// for now only get 1 strong gene
        List<String> strongGenes = retrieveStrongGenesAtPOI(
        		String.valueOf((int) poiGetStrongGenes.x), 
        		String.valueOf((int) poiGetStrongGenes.y), 
        		String.valueOf((int) poiGetStrongGenes.z), nbrStrongGenes);

        // make sure we have something
        if (strongGenes.size() == 0) {
        	// TODO logger.error none found, exceptionRpt, bail
        }
        
        if (logger.isDebugEnabled()) {
        	StringBuilder buf = new StringBuilder();
        	for (String gene : strongGenes) {
        		buf.append(gene).append(", ");
        	}
        	logger.debug("Strong genes: {}", buf.toString());
        }
      
        // 2. get image series'es for strong genes and desired plane
        List<ImageSeries> imageSerieses = new ArrayList<ImageSeries>();
		for (String geneSymbol : strongGenes) {
			ImageSeries imageSeries = retrieveImagesSeriesForGene(geneSymbol, 
					desiredPlane);
			if (imageSeries != null) {
				imageSerieses.add(imageSeries);
			}
		}
		
        // make sure we have something
        if (imageSerieses.size() == 0) {
        	// TODO logger.error none found, exceptionRpt, bail
        }
      
        if (logger.isDebugEnabled()) {
        	StringBuilder buf = new StringBuilder();
        	for (ImageSeries is : imageSerieses) {
        		buf.append(is.imageSeriesId).append(':');
        		buf.append(is.imageSeriesPlane).append(", ");
        	}
        	logger.debug("Image Serieses id:plane: {}", buf.toString());
        }
      
        // TODO translate incoming coordinates to WHS 0.9?
        // TODO need aba voxel coordinates
        // TODO for now just copy
        String srsForProximity = srsFromClient;
        Point3d poiForProximity = new Point3d(poiFromClient);
        
		// divide POI coords by 100
		Point3d poi100 = new Point3d();
		poi100.scale(0.01, poiForProximity);
		
        logger.debug("POI for closest position (srs, xyz): {}, {}", 
        		srsGetStrongGenes, poi100.toString());
        
        // 3. get ......... for each image series
		List<Image> images = new ArrayList<Image>();
        for (ImageSeries imageSeries : imageSerieses) {
        	
        	// begin to add values to image
        	Image image = new Image(imageSeries.imageSeriesId);
        	
        	// get atlas map
    		// find closest point, get other values including position
        	// add more (atlas map) values to image
        	// TODO transform to aba_voxel coordinates
        	getClosestPosition(imageSeries, poi100, image);
	        
	        logger.debug("Position: {}", image.abaImagePosition);
	                	
    		// get best image id in image series based on position
	        // add more (image elements) values to image
    		// match position to find image in series, get imageid
    		//  /image-series/images/image/position
    		//  /image-series/images/image/imageid
        	retrieveImageForPosition(imageSeries.imageSeriesId, 
        			image.abaImagePosition, image);
        	
	        logger.debug("Image id: {}", image.imageId);       	
        	
        	// zoom level not applicable
        	//image.zoomLevel = zoomLevel;
        	
    		// assemble aba view image uri
        	image.imageURI = assembleImageURI(image.downloadImagePath, 
        			HI_RES, MIME);
        	image.thumbnailurl = assembleImageURI(image.downloadImagePath, 
        			THUMB, MIME);
	        
	        logger.debug("Image URI: {}", image.imageURI.toString());
	        
        	images.add(image);		// TODO need?
        	
        	responseValues.images.add(image);
			
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
//		return null;
		return new StringRepresentation(asXml(), MediaType.APPLICATION_XML);
	}
	
	private List<String> retrieveStrongGenesAtPOI(String x, String y, 
			String z, int nbrStrongGenes) {
        List<String> strongGenes = new ArrayList<String>();
	    try {
	        URL u = new URL(assembleGeneFinderURI(x, y, z));
	        
	        logger.debug("Gene finder URI: {}", u.toString());
	        
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
	        URL u = new URL(assembleGeneURI(geneSymbol));
	        
	        logger.debug("Gene info URI: {}", u.toString());
	        
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
	private void getClosestPosition(ImageSeries imageSeries, Point3d poi100,
			Image image) {
//		Image image = new Image(imageSeries);
		try {
			
			URL u = new URL(assembleAtlasMapURI(imageSeries.imageSeriesId));
	        
	        logger.debug("Atlas map URI: {}", u.toString());
	        
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
	}
	
	private void retrieveImageForPosition(String imageSeriesId, String position, 
			Image image) {
	    try {
	        URL u = new URL(assembleImageSeriesURI(imageSeriesId));
	        
	        logger.debug("Meta info URI: {}", u.toString());
	        
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
	        parser.close();
	        
	        // debug
	        logger.debug("imageId: {}", image.imageId);
	        logger.debug("thumbnailurl: {}", image.thumbnailurl);
	        logger.debug("downloadImagePath: {}", image.downloadImagePath);
	    }
	    catch (XMLStreamException ex) {
	    	System.out.println(ex);
	    }
	    catch (IOException ex) {
	    	System.out.println("IOException while parsing ");
	    }
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
	
	private class Image {
		public String imageId;
//		public ImageSeries imageSeries;			// image series
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
		public String imagecreatedate;
		public String imagedisplayname;
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
		
		public String imageUrl;
		
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
		private String clientUri;
		private String clientSrsName;
		private String clientX;
		private String clientY;
		private String clientZ;
		private String clientFilter;
		private List<Image> images = new ArrayList<Image>();
	}
	
	public String asXml() {
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();

		ImagesResponseDocument document = completeResponse();

//		ArrayList errorList = new ArrayList();
//		opt.setErrorListener(errorList);
//		boolean isValid = document.validate(opt);
//
//		// If the XML isn't valid, loop through the listener's contents,
//		// printing contained messages.
//		if (!isValid) {
//			for (int i = 0; i < errorList.size(); i++) {
//				XmlError error = (XmlError) errorList.get(i);
//
//				System.out.println("\n");
//				System.out.println("Message: " + error.getMessage() + "\n");
//				System.out.println("Location of invalid XML: "
//						+ error.getCursorLocation().xmlText() + "\n");
//			}
//		}
		return document.xmlText(opt);
	}

	public ImagesResponseDocument completeResponse() {
		ImagesResponseDocument document = ImagesResponseDocument.Factory
				.newInstance();

		ImagesResponseType imagesRes = document.addNewImagesResponse();
		// QueryInfo and criteria should be done as a utility
		// addQueryInfo(GenesResponseType,srscode,filter,X,Y,Z)
		QueryInfoType query = imagesRes.addNewQueryInfo();
		Utilities.addMethodNameToQueryInfo(query, "Get2DImagesByPOI", 
				responseValues.clientUri);
		
		Criteria criterias = query.addNewCriteria();

		// InputPOIType poiCriteria = (InputPOIType)
		// criterias.addNewInput().changeType(InputPOIType.type);
		// poiCriteria.setName("POI");
		// PointType pnt = poiCriteria.addNewPOI().addNewPoint();
		// pnt.setId("id-onGeomRequiredByGML");
		// pnt.setSrsName("Mouse_ABAvoxel_1.0");
		// pnt.addNewPos();
		// pnt.getPos().setStringValue("1 1 1");


		
		InputStringType srsNameCriteria = (InputStringType) 
				criterias.addNewInput().changeType(InputStringType.type);
		srsNameCriteria.setName("srsName");
		srsNameCriteria.setValue(responseValues.clientSrsName);

		InputStringType xCriteria = (InputStringType)
				criterias.addNewInput().changeType(InputStringType.type);
		xCriteria.setName("x");
		xCriteria.setValue(responseValues.clientX);

		InputStringType yCriteria = (InputStringType)
				criterias.addNewInput().changeType(InputStringType.type);
		yCriteria.setName("y");
		yCriteria.setValue(responseValues.clientY);

		InputStringType zCriteria = (InputStringType)
				criterias.addNewInput().changeType(InputStringType.type);
		zCriteria.setName("y");
		zCriteria.setValue(responseValues.clientZ);
		
		InputStringType filterCodeCriteria = (InputStringType)
				criterias.addNewInput().changeType(InputStringType.type);
		filterCodeCriteria.setName("filter");
		filterCodeCriteria.setValue(responseValues.clientFilter);
		
//		InputStringType toleranceCodeCriteria = (InputStringType) criterias
//				.addNewInput().changeType(InputStringType.type);
//		toleranceCodeCriteria.setName("Tolerance");
//		toleranceCodeCriteria.setValue("1.0");

		Image2Dcollection images = imagesRes.addNewImage2Dcollection();
		
		for (Image im : responseValues.images) {
		
		Image2DType image1 = images.addNewImage2D();
		ImageSource i1source = image1.addNewImageSource();
		i1source.setStringValue(im.imageURI);
		i1source.setFormat(IncfRemoteFormatEnum.IMAGE_JPEG.toString());
		i1source.setName(im.imagedisplayname);
//		i1source.setRelavance((float) 0.6);
		i1source.setSrsName(responseValues.clientSrsName);
		i1source.setThumbnail(im.thumbnailurl);
//		i1source.setMetadata("URL");
//		i1source.setType(IncfImageServicesEnum.URL.toString());
//
//		ImagePosition i1position = image1.addNewImagePosition();
//		IncfSrsType planeequation = i1position.addNewImagePlaneEquation();
//		planeequation.setSrsName("SRS");
//		planeequation.setStringValue("1 2 3 4");
//		IncfSrsType placement = i1position.addNewImagePlanePlacement();
//		placement.setSrsName("SRS");
//		placement.setStringValue("1 2 3 4 5 6.0");
//		Corners corners = i1position.addNewCorners();
//
//		Corner corner1 = corners.addNewCorner();
//		corner1.setPosition(PositionEnum.TOPLEFT);
//		corner1.addNewPoint().addNewPos().setStringValue("1 1 1");
//		corner1.getPoint().getPos().setSrsName("Mouse_ABAvoxel_1.0");
//		corner1.getPoint().setId("image1TopLeft");
//
//		Corner corner2 = corners.addNewCorner();
//		corner2.setPosition(PositionEnum.BOTTOMLEFT);
//		corner2.addNewPoint().addNewPos().setStringValue("1 1 1");
//		corner2.getPoint().getPos().setSrsName("Mouse_ABAvoxel_1.0");
//		corner2.getPoint().setId("image1BOTTOMLEFT");
//
//		Corner corner3 = corners.addNewCorner();
//		corner3.setPosition(PositionEnum.TOPRIGHT);
//		corner3.addNewPoint().addNewPos().setStringValue("1 1 1");
//		corner3.getPoint().getPos().setSrsName("Mouse_ABAvoxel_1.0");
//		corner3.getPoint().setId("image1TOPRIGHT");
//
//		Corner corner4 = corners.addNewCorner();
//		corner4.setPosition(PositionEnum.BOTTOMRIGHT);
//		corner4.addNewPoint().addNewPos().setStringValue("1 1 1");
//		corner4.getPoint().getPos().setSrsName("Mouse_ABAvoxel_1.0");
//		corner4.getPoint().setId("image1BOTTOMRIGHT");
		
		} // for

		return document;
	}
	
	public static void main(String[] args) {
//		System.out.println(asXml());	
	}

}
