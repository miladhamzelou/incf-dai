package org.incf.atlas.ucsd.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import net.opengis.gml.x32.PointType;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.ows.ServiceException;
import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.incf.atlas.ucsd.util.DataInputs;
import org.incf.atlas.ucsd.util.UCSDConfigurator;
import org.incf.atlas.ucsd.util.UCSDUtil;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.Corners.Corner;
import org.incf.atlas.waxml.generated.Image2DType.ImagePosition;
import org.incf.atlas.waxml.generated.Image2DType.ImageSource;
import org.incf.atlas.waxml.generated.ImagesResponseType.Image2Dcollection;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.utilities.Utilities;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class Get2DImagesByPOI extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(
			Get2DImagesByPOI.class);

	//private String dataInputString;
	//private DataInputs dataInputs;
	UCSDConfigurator config = UCSDConfigurator.INSTANCE;
	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	String url = "";
	int randomGMLID1 = 0;
	int randomGMLID2 = 0;

	public Get2DImagesByPOI( Context context, Request request, 
						     Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());

		//FIXME - amemon - read the hostname from the config file 
		hostName = config.getValue("incf.deploy.host.name");
		System.out.println("****HOSTNAME**** - " + hostName);
		portNumber = ":8080";

		servicePath = "/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI"; 

		//getVariants().add(new Variant(MediaType.APPLICATION_XML));
	}


	@Override
	public Representation represent(Variant variant) throws ResourceException {

		UCSDServiceVO vo = new UCSDServiceVO();
		String paxinosCoordinatesString = "";
		String abareferenceCoordinatesString = "";
		UCSDUtil util = new UCSDUtil();
		ArrayList completeImageList = new ArrayList();
		
        try { 

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
	        vo.setFromSRSCodeOne(dataInputs.getValue("srsName"));
	        vo.setFromSRSCode(dataInputs.getValue("srsName"));
	        vo.setFilter(dataInputs.getValue("filter").replace("maptype:", ""));
	        vo.setTolerance(dataInputs.getValue("tolerance"));

	        vo.setOriginalCoordinateX(dataInputs.getValue("x"));
	        vo.setOriginalCoordinateY(dataInputs.getValue("y"));
	        vo.setOriginalCoordinateZ(dataInputs.getValue("z"));

	        // validate data inputs
	        validateSrsName(vo.getFromSRSCodeOne());
	        Double[] poiCoords = validateCoordinate(dataInputs);

	        // if any validation exceptions, no reason to continue
	        if (exceptionHandler != null) {
	            return getExceptionRepresentation();
	        }

			System.out.println("Before getPaxinosImageList");

			// Convert the coordinates ABAVOXEL into PAXINOS
	        if ( vo.getFromSRSCode().equalsIgnoreCase("mouse_abareference_1.0") ||
		         vo.getFromSRSCode().equalsIgnoreCase("mouse_paxinos_1.0") ) { 
		        	vo.setTransformedCoordinateX(vo.getOriginalCoordinateX());
		        	vo.setTransformedCoordinateY(vo.getOriginalCoordinateY());
		        	vo.setTransformedCoordinateZ(vo.getOriginalCoordinateZ());
		    } else {
	        	//Call getTransformationChain method here...
		    	//PAXINOS
		    	vo.setOriginalCoordinateX(";x="+vo.getOriginalCoordinateX());
		    	vo.setOriginalCoordinateY(";y="+vo.getOriginalCoordinateY());
		    	vo.setOriginalCoordinateZ(";z="+vo.getOriginalCoordinateZ());
		    	vo.setToSRSCode("Mouse_Paxinos_1.0");

		    	//Construct GetTransformationChain URL
		    	//http://132.239.131.188:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum
		    	String hostName = config.getValue("incf.deploy.host.name");
		    	String portNumber = ":8080";
		    	String servicePath = "/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName="+vo.getFromSRSCode()+";outputSrsName="+vo.getToSRSCode()+";filter=Cerebellum";
		    	String transformationChainURL = "http://"+hostName+portNumber+servicePath;
	        	paxinosCoordinatesString = util.internalCoordinateTransformations(transformationChainURL, vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

	        	//Start - exception handling
	        	if (paxinosCoordinatesString.startsWith("Error:")) {
	        		//System.out.println("********************ERROR*********************");
			        ExceptionHandler eh = getExceptionHandler();
	        		System.out.println("********************ERROR*********************");
			        eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
			                new String[] { paxinosCoordinatesString });
			        // there is no point in going further, so return
			        return getExceptionRepresentation();
	        	}
	        	//End - exception handling

	        	String[] tempArray = util.getTabDelimNumbers(paxinosCoordinatesString);
	        	vo.setTransformedCoordinateX(tempArray[0]);
	        	vo.setTransformedCoordinateY(tempArray[1]);
	        	vo.setTransformedCoordinateZ(tempArray[2]);

				completeImageList = getPaxinosImageList(
						vo.getFromSRSCode(), "paxinos", vo.getTransformedCoordinateX(), vo.getTransformedCoordinateY(), vo.getTransformedCoordinateZ(), vo.getFilter(), vo.getTolerance());

		    	//ABAREFERENCE Images
		    	vo.setToSRSCode("Mouse_ABAreference_1.0");

		    	//Construct GetTransformationChain URL
		    	//http://132.239.131.188:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum
		    	servicePath = "/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName="+vo.getFromSRSCode()+";outputSrsName="+vo.getToSRSCode()+";filter=Cerebellum";
		    	transformationChainURL = "http://"+hostName+portNumber+servicePath;	
		    	abareferenceCoordinatesString = util.internalCoordinateTransformations(transformationChainURL, vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

	        	//Start - exception handling
	        	if (abareferenceCoordinatesString.startsWith("ERROR:")) {
			        ExceptionHandler eh = getExceptionHandler();
			        eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
			                new String[] { abareferenceCoordinatesString });
			        // there is no point in going further, so return
			        //return getExceptionRepresentation();
	        	}
	        	//End - exception handling

	        	String[] tempArray1 = util.getTabDelimNumbers(abareferenceCoordinatesString);
	        	vo.setTransformedCoordinateX(tempArray1[0]);
	        	vo.setTransformedCoordinateY(tempArray1[1]);
	        	vo.setTransformedCoordinateZ(tempArray1[2]);

				System.out.println("Before getABAReferenceImageList");
				completeImageList = getABAReferenceImageList(completeImageList,
						vo.getFromSRSCode(), "abareference", vo.getTransformedCoordinateX(), vo.getTransformedCoordinateY(), vo.getTransformedCoordinateZ(), vo.getFilter(), vo.getTolerance());

		    }

	        if (completeImageList.size() < 0) {
		        ExceptionHandler eh = getExceptionHandler();
		        eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
		                new String[] { "No images found within " + vo.getTolerance() + " mm."}); 
		        // there is no point in going further, so return
		        return getExceptionRepresentation();
	        }
	    //Image Source URL for WMS-GETMAP:
	    //http://image.wholebraincatalog.org/cgi-bin/mapserv?map=crbsatlas/mapfiles/asyn1_montage_downsampled1280384354411.map&LAYERS=BNSTBregma0_265x01_warped-ms&FORMAT=png24&VERSION=1.1.1&REQUEST=GetMap&SRS=EPSG:4326&WIDTH=256&HEIGHT=256&BBOX=0.240823,-9.24046,4.53082,-3.99522
		
	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        String currentTime = dateFormat.format(date);
        vo.setCurrentTime(currentTime);

        //Generating 2 random number to be used as GMLID
	    Random randomGenerator1 = new Random();
	    for (int idx = 1; idx <= 10; ++idx){
	      randomGMLID1 = randomGenerator1.nextInt(100);
	    }
	    Random randomGenerator2 = new Random();
	    for (int idx = 1; idx <= 10; ++idx){
	      randomGMLID2 = randomGenerator2.nextInt(100);
	    }
	    System.out.println("Random GML ID1: - " + randomGMLID1);
	    System.out.println("Random GML ID2: - " + randomGMLID2);

        url = "http://" + hostName + portNumber + servicePath + "&DataInputs=" + dataInputsString;
        vo.setUrlString(url);

		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();

		ImagesResponseDocument document = ImagesResponseDocument.Factory
		.newInstance();

		ImagesResponseType imagesRes = document.addNewImagesResponse();
		// QueryInfo and criteria should be done as a utility
		// addQueryInfo(GenesResponseType,srscode,filter,X,Y,Z)
		QueryInfoType query = imagesRes.addNewQueryInfo();
		query.setTimeCreated(Calendar.getInstance());
		Utilities.addMethodNameToQueryInfo(query,"Get2DImagesByPOI", url);
		
		Criteria criterias = query.addNewCriteria();

		InputStringType xCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		xCriteria.setName("x");
		xCriteria.setValue(vo.getOriginalCoordinateX().replace(";x=", ""));

		InputStringType yCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		yCriteria.setName("y");
		yCriteria.setValue(vo.getOriginalCoordinateY().replace(";y=", ""));

		InputStringType zCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		zCriteria.setName("z");
		zCriteria.setValue(vo.getOriginalCoordinateZ().replace(";z=", ""));
		InputStringType filterCodeCriteria = (InputStringType) criterias
				.addNewInput().changeType(InputStringType.type);
		filterCodeCriteria.setName("filter");
		filterCodeCriteria.setValue(vo.getFilter());
		InputStringType toleranceCodeCriteria = (InputStringType) criterias
				.addNewInput().changeType(InputStringType.type);
		toleranceCodeCriteria.setName("Tolerance");
		toleranceCodeCriteria.setValue(vo.getTolerance());

		String servicePath = "/cgi-bin/mapserv?map=crbsatlas/mapfiles/";
		String imageServerHostname = config.getValue("incf.imageserver.host.name");
		Iterator iterator = completeImageList.iterator();
		String wmsURL = "";
		int count = 0; 
		while (iterator.hasNext()) {
			count++;
			vo = (UCSDServiceVO)iterator.next();
			//wmsURL = "http://"+imageServerHostname+servicePath+vo.getImageServiceName()+".map&LAYERS="+vo.getImageBaseName()+"&FORMAT=png24&VERSION=1.1.1&REQUEST=GetMap&SRS=EPSG:4326&WIDTH=256&HEIGHT=256&BBOX="+vo.getMinX()+","+vo.getMinY()+","+vo.getMaxX()+","+vo.getMaxY();
			wmsURL = "http://"+imageServerHostname+servicePath+vo.getImageServiceName()+".map&LAYERS="+vo.getImageBaseName()+"&FORMAT=png24&VERSION=1.1.1&REQUEST=GetMap";
			Image2Dcollection images = imagesRes.addNewImage2Dcollection();
			Image2DType image1 = images.addNewImage2D();
			ImageSource i1source = image1.addNewImageSource();
			i1source.setStringValue(wmsURL);
			i1source.setFormat(IncfRemoteFormatEnum.IMAGE_PNG.toString());
			//i1source.setRelavance((float) 0.6);
			i1source.setSrsName(srsName);
			//i1source.setThumbnanil("http://example.com/image.jpg");
			//i1source.setMetadata("URL");
			i1source.setType(IncfImageServicesEnum.URL.toString());

			System.out.println("coefficientA - "+vo.getCoefficientA());
			
			ImagePosition i1position = image1.addNewImagePosition();
			IncfSrsType planeequation = i1position.addNewImagePlaneEquation();
			planeequation.setSrsName(srsName);
			planeequation.setStringValue(vo.getCoefficientA() + " " + vo.getCoefficientB() + " " + vo.getCoefficientC() + " " + vo.getCoefficientD()); 
			IncfSrsType placement = i1position.addNewImagePlanePlacement();
			placement.setSrsName(srsName);
			placement.setStringValue(vo.getTfw1() + " " + vo.getTfw2() + " " + vo.getTfw3() + " " + vo.getTfw4()+ " " + vo.getTfw5() + " " + vo.getTfw6());
			Corners corners = i1position.addNewCorners();
	
			Corner corner1 = corners.addNewCorner();
			corner1.setPosition(PositionEnum.TOPLEFT);
			corner1.addNewPoint().addNewPos().setStringValue( vo.getMinX() + " " + vo.getMaxY() );
			corner1.getPoint().getPos().setSrsName(vo.getFromSRSCode());
			corner1.getPoint().setId("image"+count+"TopLeft");
	
			Corner corner2 = corners.addNewCorner();
			corner2.setPosition(PositionEnum.BOTTOMLEFT);
			corner2.addNewPoint().addNewPos().setStringValue( vo.getMinX() + " " + vo.getMinY() );
			corner2.getPoint().getPos().setSrsName(vo.getFromSRSCode());
			corner2.getPoint().setId("image"+count+"BOTTOMLEFT");
	
			Corner corner3 = corners.addNewCorner();
			corner3.setPosition(PositionEnum.TOPRIGHT);
			corner3.addNewPoint().addNewPos().setStringValue( vo.getMaxX() + " " + vo.getMaxY() );
			corner3.getPoint().getPos().setSrsName(vo.getFromSRSCode());
			corner3.getPoint().setId("image"+count+"TOPRIGHT");
	
			Corner corner4 = corners.addNewCorner();
			corner4.setPosition(PositionEnum.BOTTOMRIGHT);
			corner4.addNewPoint().addNewPos().setStringValue( vo.getMaxX() + " " + vo.getMinY() );
			corner4.getPoint().getPos().setSrsName(vo.getFromSRSCode());
			corner4.getPoint().setId("image"+count+"BOTTOMRIGHT");
		}
		
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
	 
		return new StringRepresentation(document.xmlText(opt),MediaType.APPLICATION_XML);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}


public ArrayList getPaxinosImageList(String fromSpaceName, String toSpaceName, 
		String transformedCoordinateX, String transformedCoordinateY,
		String transformedCoordinateZ, String filterValue, String tolerance) {

	UCSDConfigurator config = UCSDConfigurator.INSTANCE;
	String hostName = config.getValue("ucsd.webservice.host.name");
	String port = config.getValue("ucsd.webservice.port.number");
	System.out.println(" - hostName - " + hostName);
	System.out.println(" - PortNumber - " + port);
	StringBuffer responseString = new StringBuffer();
	ArrayList list = new ArrayList();

	// FIXME - It is used for database "SPACE" value
	String tempCoordinateString = "";
	String srscode = "PAXINOS";
	UCSDServiceVO vo = new UCSDServiceVO();
	UCSDUtil util = new UCSDUtil();
	
	try {

		String webserviceName = "ImageMetadataForROI";
		String methodName = "get2DImageListForROI";

		// Create an arbitrary polygon with + or - 3
		String x1 = transformedCoordinateX;
		String x2 = String.valueOf(Double.parseDouble(transformedCoordinateX) + Double.parseDouble(tolerance));
		String x3 = String.valueOf(Double.parseDouble(transformedCoordinateX) + Double.parseDouble(tolerance));
		String x4 = transformedCoordinateX;
		String y1 = transformedCoordinateY;
		String y2 = transformedCoordinateY;
		String y3 = String.valueOf(Double.parseDouble(transformedCoordinateY) - Double.parseDouble(tolerance));
		String y4 = String.valueOf(Double.parseDouble(transformedCoordinateY) - Double.parseDouble(tolerance));

		String polygonString = x1 + "," + y1 + "," + x2 + "," + y2 + ","
				+ x3 + "," + y3 + "," + x4 + "," + y4 + "," + x1 + "," + y1;
		System.out.println("PAXINOS - Made up polygon string - "
				+ polygonString);

		String xmlQueryString = createXMLQueryStringForImageList(
				polygonString, srscode, filterValue);

		// Starts - Webserviceclient code
		String endpoint = "http://" + hostName + port + "/axis/services/"
				+ webserviceName;
		Service service = new Service();
		Call call = (Call) service.createCall();
		QName operationName = new QName(endpoint, methodName);
		call.setOperationName(operationName);
		call.setTargetEndpointAddress(endpoint);
		StringBuffer sb = new StringBuffer();
		sb.append(xmlQueryString);

		responseString.append((String) call.invoke(new Object[] { sb
				.toString() }));

		System.out.println("Response String - " + responseString);
		// Ends

		list = parseXMLResponseStringForImageList(list, responseString
				.toString());
		Iterator iterator = list.iterator();
		vo = new UCSDServiceVO();

		while (iterator.hasNext()) {
			vo = (UCSDServiceVO) iterator.next();
			System.out.println("WMS - " + vo.getWms());
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	return list;

}

public ArrayList getABAReferenceImageList(ArrayList completeImageList,
		String fromSpaceName, String toSpaceName, String transformedCoordinateX,
		String transformedCoordinateY, String transformedCoordinateZ,
		String filterValue, String tolerance) {

	UCSDConfigurator config = UCSDConfigurator.INSTANCE;
	UCSDUtil util = new UCSDUtil();

	String hostName = config.getValue("ucsd.webservice.host.name");
	String port = config.getValue("ucsd.webservice.port.number");
	System.out.println(" - hostName - " + hostName);
	System.out.println(" - PortNumber - " + port);
	StringBuffer responseString = new StringBuffer();

	try {

		UCSDServiceVO vo = new UCSDServiceVO();
		vo.setFromSRSCode(fromSpaceName);
		vo.setToSRSCode(toSpaceName);
		vo.setFromSRSCodeOne(fromSpaceName);
		vo.setToSRSCodeOne(toSpaceName);

		// Convert the coordinates ABAVoxel into ABAReference
/*		String tempCoordinateString1 = incfUtil.spaceTransformation(vo);
		String[] tempArray1 = incfUtil
				.getTabDelimNumbers(tempCoordinateString1);

		coordinateX = incfUtil.getRoundCoordinateValue(tempArray1[0]);
		coordinateY = incfUtil.getRoundCoordinateValue(tempArray1[1]);
		coordinateZ = incfUtil.getRoundCoordinateValue(tempArray1[2]);

		System.out.println("***Coordinate X for Polygon String - "
				+ coordinateX);
		System.out.println("***Coordinate Y for Polygon String - "
				+ coordinateY);
		System.out.println("***Coordinate Z for Polygon String - "
				+ coordinateZ);
*/
		// FIXME
		String webserviceName = "ImageMetadataForROI";
		String methodName = "get2DImageListForROI";
		String srscode = "ABA_REFERENCE";

		// Create an arbitrary polygon with + or - 3
		String x1 = transformedCoordinateX;
		String x2 = String.valueOf(Double.parseDouble(transformedCoordinateX) + Double.parseDouble(tolerance));
		String x3 = String.valueOf(Double.parseDouble(transformedCoordinateX) + Double.parseDouble(tolerance));
		String x4 = transformedCoordinateX;
		String y1 = transformedCoordinateY;
		String y2 = transformedCoordinateY;
		String y3 = String.valueOf(Double.parseDouble(transformedCoordinateY) - Double.parseDouble(tolerance));
		String y4 = String.valueOf(Double.parseDouble(transformedCoordinateY) - Double.parseDouble(tolerance));
		
		String polygonString = x1 + "," + y1 + "," + x2 + "," + y2 + ","
				+ x3 + "," + y3 + "," + x4 + "," + y4 + "," + x1 + "," + y1;
		System.out.println("ABAREFERENCE - Made up polygon string - "
				+ polygonString);

		String xmlQueryString = createXMLQueryStringForImageList(
				polygonString, srscode, filterValue);

		// Starts - Webserviceclient code
		String endpoint = "http://" + hostName + port + "/axis/services/"
				+ webserviceName;
		Service service = new Service();
		Call call = (Call) service.createCall();
		QName operationName = new QName(endpoint, methodName);
		call.setOperationName(operationName);
		call.setTargetEndpointAddress(endpoint);
		StringBuffer sb = new StringBuffer();
		sb.append(xmlQueryString);

		responseString.append((String) call.invoke(new Object[] { sb
				.toString() }));

		System.out.println("Response String - " + responseString);
		// Ends

		completeImageList = parseXMLResponseStringForImageList(
				completeImageList, responseString.toString());

		// It is a complete list containing paxinos as well as abaref images
		Iterator iterator = completeImageList.iterator();
		vo = new UCSDServiceVO();
		while (iterator.hasNext()) {
			vo = (UCSDServiceVO) iterator.next();
			System.out.println("WMS - " + vo.getWms());
			System.out
					.println("Image Base Name - " + vo.getImageBaseName());
			System.out.println("Image service Name - "
					+ vo.getImageServiceName());
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	return completeImageList;
	
}

// Creates an xml string with buch of parameters to be passed in a
// webservice
private String createXMLQueryStringForImageList(String polygonString,
		String srsCode, String filterValue) {

	StringBuffer xmlString = new StringBuffer();

	// Read the proxy from a file
	try {

		xmlString.append("<request>");

		xmlString.append("<category>").append("mouse")
				.append("</category>");
		xmlString.append("<regionofinterest>").append(polygonString)
				.append("</regionofinterest>");
		xmlString.append("<maptype>").append(filterValue).append(
				"</maptype>");
		xmlString.append("<srscode>").append(srsCode).append("</srscode>");

		xmlString.append("</request>");

	} catch (Exception e) {

		e.getStackTrace();

	}

	System.out.println(" Query String for getImageMetaDataForROI - "
			+ xmlString.toString());
	return xmlString.toString();

}

	private ArrayList parseXMLResponseStringForImageList(ArrayList list,
						String xmlData) {

	// Make the xml document from the xml string
	org.jdom.Document document;
	String imageServerHostname = config.getValue("incf.imageserver.host.name");
	
	try {

		SAXBuilder builder = new SAXBuilder(
				"org.apache.xerces.parsers.SAXParser", false);

		document = builder.build(new InputSource(new ByteArrayInputStream(
				xmlData.getBytes())));

		// Get all the childrens
		List structuresList = document.getRootElement().getChildren();

		// Start - xml parsing
		Element responseElement;
		// Element responseElement =
		// document.getRootElement().getChild("imageinfo");
		UCSDServiceVO vo;
		UCSDUtil util = new UCSDUtil();

		for (int i = 0; i < structuresList.size(); i++) {

			responseElement = (Element) structuresList.get(i);
			vo = new UCSDServiceVO();

			vo = getSimpleImageEnvelope(imageServerHostname,
					responseElement.getChildText("imageServiceName"),
					"MAPSERVER");

			// vo.setWms(responseElement.getChildText("wmsURL"));
			vo.setImageBaseName(responseElement.getChildText("imageName"));
			vo.setImageServiceName(responseElement
					.getChildText("imageServiceName"));
			vo.setTfw1(responseElement.getChildText("tfw1"));
			vo.setTfw2(responseElement.getChildText("tfw2"));
			vo.setTfw3(responseElement.getChildText("tfw3"));
			vo.setTfw4(responseElement.getChildText("tfw4"));
			vo.setTfw5(responseElement.getChildText("tfw5"));
			vo.setTfw6(responseElement.getChildText("tfw6"));
			
			if (responseElement.getChildText("coefficientA") == null){
				vo.setCoefficientA("0");
			}
			if (responseElement.getChildText("coefficientB") == null){
				vo.setCoefficientB("0");
			}
			if (responseElement.getChildText("coefficientC") == null){
				vo.setCoefficientC("0");
			}
			if (responseElement.getChildText("coefficientD") == null){
				vo.setCoefficientD("0");
			}

			vo.setSliceConstant(responseElement.getChildText("sliceConstant"));
			
			list.add(vo);// Since data for multiimageservice is the same
							// for all the records based on sliceID, so we
							// are commenting this line

		}
		// End - xml parsing

	} catch (JDOMException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return list;

	}

	  /**
	  *
	  * @param host String Host name of the computer running ArcIMS
	  * @param imageService String Name of Image Service from which to get image
	  *
	  * @return SpatialAtlasClientDataModel   dataModel
	  */
	 public UCSDServiceVO getSimpleImageEnvelope( String host, String serviceName, String imageServerName ){
		
		System.out.println( "Begin - getSimpleImageEnvelope");

		String returnString = new String("");
		UCSDServiceVO dataModel = new UCSDServiceVO();
		System.out.println( "imageServerName - " + imageServerName );
		
	    try {

	    if ( imageServerName.trim().equalsIgnoreCase("MAPSERVER")) {
		
	   	 	System.out.println( "Inside MAPSERVER" );

	   	    try {

	   	    	//FIXME - Needs to come from the config file.
	   	    	String port = ":9090";
	   	    	String webDir = "crbsatlas/mapfiles";
	   	    	
	   	    	//Linux Map Server URL
	   	    	String imageURLString = "http://" + host + "/cgi-bin/mapserv?map="+webDir+"/" + serviceName + ".map";
	   	    	
	   	    	//Windows Map Server URL
	   	    	//String imageURLString = "http://" + host + port + "/cgi-bin/mapserv.exe?map="+webDir+"/" + serviceName + ".map";

	   	    	System.out.println("WMS URL String - " + imageURLString );

				URL url = new URL( imageURLString );
				
				WebMapServer wms = new WebMapServer(url);
				
				WMSCapabilities capabilities = wms.getCapabilities(); 
				Layer layer = capabilities.getLayer();
				dataModel.setMinX(String.valueOf( layer.getLatLonBoundingBox().getMinX() ) );
				dataModel.setMaxX(String.valueOf( layer.getLatLonBoundingBox().getMaxX() ) );
				dataModel.setMinY(String.valueOf( layer.getLatLonBoundingBox().getMinY() ) );
				dataModel.setMaxY(String.valueOf( layer.getLatLonBoundingBox().getMaxY() ) );
				
				dataModel.setBottomLeft(dataModel.getMinX() + ", " + dataModel.getMinY());
				dataModel.setTopRight(dataModel.getMaxX() + ", " + dataModel.getMaxY());
				dataModel.setBottomRight(dataModel.getMaxX() + ", " + dataModel.getMinY());
				dataModel.setTopLeft(dataModel.getMinX() + ", " + dataModel.getMaxY());
				
			    System.out.println("minX is - " + dataModel.getMinX());
			    System.out.println("maxX is - " + dataModel.getMaxX());
			    System.out.println("minY is - " + dataModel.getMinY());
			    System.out.println("maxY is - " + dataModel.getMaxY());

				} catch (ServiceException e) { 

					e.printStackTrace(); 

				} catch (Exception e) { 

					e.printStackTrace();

				}

		}

	    } catch(Exception e){
	     e.printStackTrace();
	   }

	   System.out.println( "End - getSimpleImageEnvelope");

	   return dataModel;

	 }

}

