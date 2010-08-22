package org.incf.atlas.whs.resource;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;


import net.opengis.gml.x32.PointType;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.utilities.Utilities;
import org.incf.atlas.whs.util.DataInputs;
import org.incf.atlas.whs.util.WHSConfigurator;
import org.incf.atlas.whs.util.WHSUtil;

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

public class TransformPOI extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(
			TransformPOI.class);

	//private String dataInputString;
	//private DataInputs dataInputs;
	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	String url = "";
	int randomGMLID1 = 0;
	int randomGMLID2 = 0;

	public TransformPOI(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());

/*		System.out.println("You are in TransformPOIResource");
		dataInputString = (String) request.getAttributes().get("dataInputs"); 
		System.out.println("dataInputString " + dataInputString );
*/
		//dataInputs = new DataInputs(dataInputString);

		//FIXME - amemon - read the hostname from the config file 
		WHSConfigurator config = WHSConfigurator.INSTANCE;
		hostName = config.getValue("incf.deploy.host.name");
		System.out.println("****HOSTNAME**** - " + hostName);
		portNumber = ":8080";

		servicePath = "/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=TransformPOI"; 

		//getVariants().add(new Variant(MediaType.APPLICATION_XML));
	}


	@Override
	public Representation represent(Variant variant) throws ResourceException {

		WHSServiceVO vo = new WHSServiceVO();

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

	        vo.setFromSRSCodeOne(dataInputs.getValue("inputSrsName"));
	        vo.setFromSRSCode(dataInputs.getValue("inputSrsName"));
	        vo.setToSRSCodeOne(dataInputs.getValue("outputSrsName"));
	        vo.setToSRSCode(dataInputs.getValue("outputSrsName"));
	        vo.setFilter(dataInputs.getValue("filter"));

	        System.out.println("From SRS Code: " + vo.getFromSRSCodeOne());
	        System.out.println("To SRS Code: " + vo.getToSRSCodeOne());
	        System.out.println("Filter: " + vo.getFilter());

	        // validate data inputs
	        validateSrsName(vo.getFromSRSCodeOne());
	        validateSrsName(vo.getToSRSCodeOne());
	        Double[] poiCoords = validateCoordinate(dataInputs);

	        vo.setOriginalCoordinateX(String.valueOf(poiCoords[0].intValue()));
	        vo.setOriginalCoordinateY(String.valueOf(poiCoords[1].intValue()));
	        vo.setOriginalCoordinateZ(String.valueOf(poiCoords[2].intValue()));

	        System.out.println("X: "+vo.getOriginalCoordinateX());
	        System.out.println("Y: "+vo.getOriginalCoordinateY());
	        System.out.println("Z: "+vo.getOriginalCoordinateZ());

	        System.out.println("X1: "+dataInputs.getValue("x"));
	        System.out.println("Y1: "+dataInputs.getValue("y"));
	        System.out.println("Z1: "+dataInputs.getValue("z"));

	        // if any validation exceptions, no reason to continue
	        if (exceptionHandler != null) {
	            return getExceptionRepresentation();
	        }

		// text return for debugging
		//Set<String> dataInputKeys = dataInputs.getKeys();
		System.out.println("-2");

		//Start - Call the main method here
		WHSUtil util = new WHSUtil();
		String completeCoordinatesString = util.spaceTransformation(vo);

		if (completeCoordinatesString.equalsIgnoreCase("NOT SUPPORTED")) {
	        ExceptionHandler eh = getExceptionHandler();
	        eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
	                new String[] { "No Such Transformation is available under WHS Hub." });

	        // there is no point in going further, so return
	        return getExceptionRepresentation();
		}

		vo = util.splitCoordinatesFromStringToVO(vo, completeCoordinatesString);
		//End

		//Start - Exception Handling
		if (vo.getTransformedCoordinateX().equalsIgnoreCase("out")) {
	        ExceptionHandler eh = getExceptionHandler();
	        eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
	                new String[] { "Coordinates - Out of Range." });

	        // there is no point in going further, so return
	        return getExceptionRepresentation();
		}
		//End
		
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
		
		TransformationResponseDocument document = TransformationResponseDocument.Factory.newInstance(); 

		TransformationResponseType rootDoc =	document.addNewTransformationResponse();
		// QueryInfo and criteria should be done as a utility
		// addQueryInfo(GenesResponseType,srscode,filter,X,Y,Z)
		QueryInfoType query = rootDoc.addNewQueryInfo();
		QueryInfoType.Criteria criterias = query.addNewCriteria();

		query.addNewQueryUrl();
		query.getQueryUrl().setName("TransformPOI");
		query.getQueryUrl().setStringValue(url);
		query.setTimeCreated(Calendar.getInstance());

		InputStringType targetsrsCriteria = (InputStringType) criterias
		.addNewInput().changeType(InputStringType.type);

		targetsrsCriteria.setName("outputSrsName");
		targetsrsCriteria.setValue(vo.getToSRSCode());
		
		InputStringType xCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		xCriteria.setName("x");
		xCriteria.setValue(vo.getOriginalCoordinateX());
		
		InputStringType yCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		yCriteria.setName("y");
		yCriteria.setValue(vo.getOriginalCoordinateY());
		
		InputStringType zCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		zCriteria.setName("z");
		zCriteria.setValue(vo.getOriginalCoordinateZ());

		InputStringType filterCodeCriteria = (InputStringType) criterias
		.addNewInput().changeType(InputStringType.type);
		filterCodeCriteria.setName("filter");
		filterCodeCriteria.setValue("cerebellum");

		POIType poi = rootDoc.addNewPOI();
		PointType poipnt = poi.addNewPoint();
		poipnt.setId(String.valueOf(randomGMLID2));
		poipnt.setSrsName(vo.getToSRSCode());
		poipnt.addNewPos();
		poipnt.getPos().setStringValue(vo.getTransformedCoordinateX() + " " + vo.getTransformedCoordinateY() + " " + vo.getTransformedCoordinateZ());

		ArrayList errorList = new ArrayList();
		opt.setErrorListener(errorList);
		boolean isValid = document.validate(opt);
	 
	 // If the XML isn't valid, loop through the listener's contents,
	 // printing contained messages.
	 if (!isValid)
	 {
	      for (int i = 0; i < errorList.size(); i++)
	      {
	          XmlError error = (XmlError)errorList.get(i);
	          
	          System.out.println("\n");
	          System.out.println("Message: " + error.getMessage() + "\n");
	          System.out.println("Location of invalid XML: " + 
	              error.getCursorLocation().xmlText() + "\n");
	      }
	 }
	 
		//return document.xmlText(opt);
		return new StringRepresentation(document.xmlText(opt),MediaType.APPLICATION_XML);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}


}
