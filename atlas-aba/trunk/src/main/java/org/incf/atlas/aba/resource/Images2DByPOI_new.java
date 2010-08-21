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

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.aba.util.DataInputs;
import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.incf.atlas.waxml.generated.Corners;
import org.incf.atlas.waxml.generated.Image2DType;
import org.incf.atlas.waxml.generated.ImagesResponseDocument;
import org.incf.atlas.waxml.generated.ImagesResponseType;
import org.incf.atlas.waxml.generated.IncfImageServicesEnum;
import org.incf.atlas.waxml.generated.IncfRemoteFormatEnum;
import org.incf.atlas.waxml.generated.IncfSrsType;
import org.incf.atlas.waxml.generated.InputStringType;
import org.incf.atlas.waxml.generated.PositionEnum;
import org.incf.atlas.waxml.generated.QueryInfoType;
import org.incf.atlas.waxml.generated.Corners.Corner;
import org.incf.atlas.waxml.generated.Image2DType.ImagePosition;
import org.incf.atlas.waxml.generated.Image2DType.ImageSource;
import org.incf.atlas.waxml.generated.ImagesResponseType.Image2Dcollection;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.utilities.Utilities;
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
	
	// used for ABA Get Image URI query string
	private static final String ZOOM = "-1";	// highest resolution available
	private static final String MIME = "2";		// jpeg/image
	
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

        Double[] poiFromClient = validateCoordinate(dataInputs);
        
        logger.debug("POI from client (srs, xyz): {}, {}, {}, {}", 
        		new String[] { srsName, 
        			String.valueOf(poiFromClient[0]), 
        			String.valueOf(poiFromClient[1]), 
        			String.valueOf(poiFromClient[2]) } );
        
        // TODO translate incoming coordinates to WHS 0.9
        
        // TODO get/validate filter; defaults to sagittal
        String filter = dataInputs.getValue("filter");
        ImageSeriesPlane desiredPlane = filter.equals("maptype:coronal")
        		? ImageSeriesPlane.CORONAL : ImageSeriesPlane.SAGITTAL;
        
        // if any validation exceptions, no reason to continue
        if (exceptionHandler != null) {
            return getExceptionRepresentation();
        }
        
        // 1. get strong gene(s) at POI
        // TODO transform to agea coordinates
        int nbrStrongGenes = 1;			// for now only get 1 strong gene
        List<String> strongGenes = retrieveStrongGenesAtPOI(
        		dataInputs.getValue("x"), dataInputs.getValue("y"), 
        		dataInputs.getValue("z"), nbrStrongGenes);

        // make sure we have something
        if (strongGenes.size() == 0) {
        	// TODO logger.error none found, exceptionRpt, bail
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
      
        // 3. get ......... for each image series
		List<Image> images = new ArrayList<Image>();
        for (ImageSeries is : imageSerieses) {
        	
        	// get atlas map
    		// find closest point, get other values including position
        	// TODO transform to aba_voxel coordinates
        	Point3d abaVoxelPoi= new Point3d();
        	Image image = getClosestPosition(is, abaVoxelPoi);
        	
    		// get best image id in image series based on position
    		// match position to find image in series, get imageid
    		//  /image-series/images/image/position
    		//  /image-series/images/image/imageid
        	String imageId = retrieveImageIdForPosition(is.imageSeriesId, 
        			image.abaImagePosition);
        	
        	image.imageId = imageId;
        	
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
		public ImageSeries imageSeries;			// image series
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
	
	public static String AsXml() {
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();

		ImagesResponseDocument document = completeResponse();

		ArrayList errorList = new ArrayList();
		opt.setErrorListener(errorList);
		boolean isValid = document.validate(opt);

		// If the XML isn't valid, loop through the listener's contents,
		// printing contained messages.
		if (!isValid) {
			for (int i = 0; i < errorList.size(); i++) {
				XmlError error = (XmlError) errorList.get(i);

				System.out.println("\n");
				System.out.println("Message: " + error.getMessage() + "\n");
				System.out.println("Location of invalid XML: "
						+ error.getCursorLocation().xmlText() + "\n");
			}
		}
		return document.xmlText(opt);
	}

	public static ImagesResponseDocument completeResponse() {
		ImagesResponseDocument document = ImagesResponseDocument.Factory
				.newInstance();

		ImagesResponseType imagesRes = document.addNewImagesResponse();
		// QueryInfo and criteria should be done as a utility
		// addQueryInfo(GenesResponseType,srscode,filter,X,Y,Z)
		QueryInfoType query = imagesRes.addNewQueryInfo();
		Utilities.addMethodNameToQueryInfo(query,"Get2DImagesByPOI","URL");
		
		Criteria criterias = query.addNewCriteria();

		// InputPOIType poiCriteria = (InputPOIType)
		// criterias.addNewInput().changeType(InputPOIType.type);
		// poiCriteria.setName("POI");
		// PointType pnt = poiCriteria.addNewPOI().addNewPoint();
		// pnt.setId("id-onGeomRequiredByGML");
		// pnt.setSrsName("Mouse_ABAvoxel_1.0");
		// pnt.addNewPos();
		// pnt.getPos().setStringValue("1 1 1");


		
		InputStringType xCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		xCriteria.setName("x");
		xCriteria.setValue("263");

		InputStringType yCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		yCriteria.setName("y");
		yCriteria.setValue("159");

		InputStringType zCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		zCriteria.setName("y");
		zCriteria.setValue("227");
		InputStringType filterCodeCriteria = (InputStringType) criterias
				.addNewInput().changeType(InputStringType.type);
		filterCodeCriteria.setName("filter");
		filterCodeCriteria.setValue("maptype:coronal");
		InputStringType toleranceCodeCriteria = (InputStringType) criterias
				.addNewInput().changeType(InputStringType.type);
		toleranceCodeCriteria.setName("Tolerance");
		toleranceCodeCriteria.setValue("1.0");

		Image2Dcollection images = imagesRes.addNewImage2Dcollection();
		Image2DType image1 = images.addNewImage2D();
		ImageSource i1source = image1.addNewImageSource();
		i1source.setStringValue("URL");
		i1source.setFormat(IncfRemoteFormatEnum.IMAGE_JPEG.toString());
		i1source.setRelavance((float) 0.6);
		i1source.setSrsName("srscode");
		i1source.setThumbnanil("http://example.com/image.jpg");
		i1source.setMetadata("URL");
		i1source.setType(IncfImageServicesEnum.URL.toString());

		ImagePosition i1position = image1.addNewImagePosition();
		IncfSrsType planeequation = i1position.addNewImagePlaneEquation();
		planeequation.setSrsName("SRS");
		planeequation.setStringValue("1 2 3 4");
		IncfSrsType placement = i1position.addNewImagePlanePlacement();
		placement.setSrsName("SRS");
		placement.setStringValue("1 2 3 4 5 6.0");
		Corners corners = i1position.addNewCorners();

		Corner corner1 = corners.addNewCorner();
		corner1.setPosition(PositionEnum.TOPLEFT);
		corner1.addNewPoint().addNewPos().setStringValue("1 1 1");
		corner1.getPoint().getPos().setSrsName("Mouse_ABAvoxel_1.0");
		corner1.getPoint().setId("image1TopLeft");

		Corner corner2 = corners.addNewCorner();
		corner2.setPosition(PositionEnum.BOTTOMLEFT);
		corner2.addNewPoint().addNewPos().setStringValue("1 1 1");
		corner2.getPoint().getPos().setSrsName("Mouse_ABAvoxel_1.0");
		corner2.getPoint().setId("image1BOTTOMLEFT");

		Corner corner3 = corners.addNewCorner();
		corner3.setPosition(PositionEnum.TOPRIGHT);
		corner3.addNewPoint().addNewPos().setStringValue("1 1 1");
		corner3.getPoint().getPos().setSrsName("Mouse_ABAvoxel_1.0");
		corner3.getPoint().setId("image1TOPRIGHT");

		Corner corner4 = corners.addNewCorner();
		corner4.setPosition(PositionEnum.BOTTOMRIGHT);
		corner4.addNewPoint().addNewPos().setStringValue("1 1 1");
		corner4.getPoint().getPos().setSrsName("Mouse_ABAvoxel_1.0");
		corner4.getPoint().setId("image1BOTTOMRIGHT");
		return document;
	}
	
	public static void main(String[] args) {
		System.out.println(AsXml());	
	}
}
