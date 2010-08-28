package org.incf.atlas.aba.resource;

import java.net.URI;
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
import org.incf.atlas.aba.util.ABAConfigurator;
import org.incf.atlas.aba.util.ABAUtil;
import org.incf.atlas.aba.util.AtlasNamespacePrefixMapper;
import org.incf.atlas.aba.util.Constants;
import org.incf.atlas.aba.util.DataInputs;
import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.incf.atlas.common.util.XMLUtilities;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.utilities.Utilities;

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

public class CorrelationMapByPOI extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(
			CorrelationMapByPOI.class);

	ABAConfigurator config = ABAConfigurator.INSTANCE;

	String abaReference = config.getValue("srsname.abareference.10");
	String abaVoxel = config.getValue("srsname.abavoxel.10");
	String agea = config.getValue("srsname.agea.10");
	String whs09 = config.getValue("srsname.whs.09");
	String whs10 = config.getValue("srsname.whs.10");
	String emap = config.getValue("srsname.emap.10");
	String paxinos = config.getValue("srsname.paxinos.10");
	URI uri = null;
	
	//private String dataInputString;
	//private DataInputs dataInputs;
	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	int randomGMLID1 = 0;
	int randomGMLID2 = 0;
	String incfDeployHostname = "";
	String incfDeployPortnumber = "";

	String coordinateX = "";
	String coordinateY = "";
	String coordinateZ = "";
	
	public CorrelationMapByPOI(Context context, Request request, 
			Response response) {

		super(context, request, response);

		logger.debug("Instantiated {}.", getClass());

		try { 
			uri = new URI(request.getResourceRef().toString());
			incfDeployHostname = uri.getHost();
			incfDeployPortnumber = String.valueOf(uri.getPort());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public Representation represent(Variant variant) throws ResourceException {

		ABAServiceVO vo = new ABAServiceVO();

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

	        vo.setFromSRSCodeOne(dataInputs.getValue("srsName"));
	        vo.setFromSRSCode(dataInputs.getValue("srsName"));
	        vo.setToSRSCodeOne(agea);
	        vo.setToSRSCode(agea);
	        vo.setFilter(dataInputs.getValue("filter"));

	        System.out.println("From SRS Code: " + vo.getFromSRSCodeOne());
	        System.out.println("Filter: " + vo.getFilter());

	        // validate data inputs
	        validateSrsName(vo.getFromSRSCodeOne());
	        Double[] poiCoords = validateCoordinate(dataInputs);

	        vo.setOriginalCoordinateX(dataInputs.getValue("x"));
	        vo.setOriginalCoordinateY(dataInputs.getValue("y"));
	        vo.setOriginalCoordinateZ(dataInputs.getValue("z"));

	        coordinateX = dataInputs.getValue("x");
	        coordinateY = dataInputs.getValue("y");
	        coordinateZ = dataInputs.getValue("z");
	        
	        // if any validation exceptions, no reason to continue
	        if (exceptionHandler != null) {
	            return getExceptionRepresentation();
	        }


		//Start - Call the main method here
		ABAUtil util = new ABAUtil();
		
        //Start - Common code used for coordinate transformation
        String transformedCoordinatesString = "";
		// Convert the coordinates ABAVOXEL into PAXINOS
        if ( vo.getFromSRSCode().equalsIgnoreCase(agea) ) { 
	        	vo.setTransformedCoordinateX(vo.getOriginalCoordinateX());
	        	vo.setTransformedCoordinateY(vo.getOriginalCoordinateY());
	        	vo.setTransformedCoordinateZ(vo.getOriginalCoordinateZ());
	    } else { 
        	//Call getTransformationChain method here...
	    	//ABAVoxel
	    	vo.setOriginalCoordinateX(";x="+vo.getOriginalCoordinateX());
	    	vo.setOriginalCoordinateY(";y="+vo.getOriginalCoordinateY());
	    	vo.setOriginalCoordinateZ(";z="+vo.getOriginalCoordinateZ());
	    	vo.setToSRSCode(agea);
	    	vo.setToSRSCodeOne(agea);

	    	//Construct GetTransformationChain URL
	    	//http://k:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum
	    	
	    	String hostName = uri.getHost();
	    	String delimitor = config.getValue("incf.deploy.port.delimitor");
	    	String portNumber = delimitor + uri.getPort();

	    	String servicePath = "/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName="+vo.getFromSRSCode()+";outputSrsName="+vo.getToSRSCode()+";filter=Cerebellum";
	    	String transformationChainURL = "http://"+hostName+portNumber+servicePath;
	    	XMLUtilities xmlUtilities = new XMLUtilities();
	    	transformedCoordinatesString = xmlUtilities.coordinateTransformation(transformationChainURL, vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

        	//Start - exception handling
        	if (transformedCoordinatesString.startsWith("Error:")) {
        		//System.out.println("********************ERROR*********************");
		        ExceptionHandler eh = getExceptionHandler();
        		System.out.println("********************ERROR*********************");
		        eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
		                new String[] { transformedCoordinatesString });
		        // there is no point in going further, so return
		        return getExceptionRepresentation();
        	}
        	//End - exception handling
        	String[] tempArray = util.getTabDelimNumbers(transformedCoordinatesString);
        	vo.setTransformedCoordinateX(tempArray[0]);
        	vo.setTransformedCoordinateY(tempArray[1]);
        	vo.setTransformedCoordinateZ(tempArray[2]);
	    }
        //End

		//String completeCoordinatesString = util.spaceTransformation(vo);

/*		if (completeCoordinatesString.equalsIgnoreCase("NOT SUPPORTED")) {
	        ExceptionHandler eh = getExceptionHandler();
	        eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
	                new String[] { "No Such Transformation is available under ABA Hub." });

	        // there is no point in going further, so return
	        return getExceptionRepresentation();
		}
		vo = util.splitCoordinatesFromStringToVO(vo, completeCoordinatesString);
*/

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

		String abaHostName = config.getValue("incf.aba.host.name");
		String abaPortNumber = config.getValue("incf.aba.port.number");
		String abaServicePath = config.getValue("incf.aba.service.path");

        vo.setUrlString(uri.toString());
        vo.setIncfDeployHostname(incfDeployHostname);
        vo.setIncfDeployPortNumber(incfDeployPortnumber);

		//Start - Construct the getcoorelationmap url
		StringBuffer responseString = new StringBuffer();
		String mapType = vo.getFilter().replaceAll("maptype:", "");
		String imageURLPrefix = "http://" + abaHostName + abaPortNumber
		+ abaServicePath + "all_" + mapType + "?correlation&";

		responseString.append(imageURLPrefix).append(
		"seedPoint=" + vo.getTransformedCoordinateX() + ","
					 + vo.getTransformedCoordinateY() + ","
					 + vo.getTransformedCoordinateZ() );
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

        //url = "http://" + hostName + portNumber + servicePath + "&DataInputs=" + dataInputsString;
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();

		CorrelationMapResponseDocument document = CorrelationMapResponseDocument.Factory.newInstance();

		CorrelatioMapType imagesRes = document.addNewCorrelationMapResponse();
	    //QueryInfo and criteria should be done as a utility
		//addQueryInfo(GenesResponseType,srscode,filter,X,Y,Z)
		QueryInfoType query = imagesRes.addNewQueryInfo();

		Utilities.addMethodNameToQueryInfo(query, "GetCorrelationMapByPOI", uri.toString());

		Criteria criterias = query.addNewCriteria();

/*		InputPOIType poiCriteria = (InputPOIType) criterias.addNewInput().changeType(InputPOIType.type);
		poiCriteria.setName("POI");
		PointType pnt = poiCriteria.addNewPOI().addNewPoint();
		pnt.setId("id-onGeomRequiredByGML");
		pnt.setSrsName(abaVoxel);

		pnt.addNewPos();
		pnt.getPos().setStringValue("1 1 1");
*/		
		
		Utilities.addInputStringCriteria(criterias, "srsName", vo.getFromSRSCode());

		InputStringType xCriteria = (InputStringType) criterias.addNewInput()
		.changeType(InputStringType.type);
		xCriteria.setName("x");
		xCriteria.setValue(coordinateX);
		
		InputStringType yCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		yCriteria.setName("y");
		yCriteria.setValue(coordinateY);
		
		InputStringType zCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		zCriteria.setName("z");
		zCriteria.setValue(coordinateZ);

		InputStringType filterCodeCriteria = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		filterCodeCriteria.setName("filter");
		filterCodeCriteria.setValue("maptype:coronal");


		imagesRes.setCorrelationUrl(responseString.toString());
		
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

/*		ObjectFactory of = new ObjectFactory();
		TransformationResponse transformationResponse = 
			of.createTransformationResponse();

		QueryURL queryURL = new QueryURL();
		queryURL.setName("TransformPOI");
		queryURL.setValue(url);//Change
		
		//Query Info setters
		Point srcPoint = new Point();
		srcPoint.setSrsName(vo.getFromSRSCode());//Change
		srcPoint.setPos(vo.getOriginalCoordinateX() + " " + vo.getOriginalCoordinateY() + " " + vo.getOriginalCoordinateZ());//Change

		Input input = new Input();
		input.setName("POI");
		input.setPoint(srcPoint);

		Criteria criteria = new Criteria();
		criteria.setInput(input);

		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setQueryURL(queryURL);
		queryInfo.setTimeCreated(currentTime);

		queryInfo.setCriteria(criteria);

		//TransformPOI responses
		Point destPoint = new Point();
		destPoint.setSrsName(vo.getToSRSCode());//Change
		destPoint.setPos(vo.getTransformedCoordinateX() + " " + vo.getTransformedCoordinateY() + " " + vo.getTransformedCoordinateZ());//Change
				
		generated.Point tempPoint = new generated.Point();
		tempPoint.setPoint(destPoint);
		
		transformationResponse.setQueryInfo(queryInfo);
		transformationResponse.setPOI(tempPoint);
*/
		//generate representation based on media type - Used this method for renaming the prefixes
	 /*		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
			return getDomRepresentation(transformationResponse);
		}
*/
/*		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
			return new JaxbRepresentation<TransformationResponse>(transformationResponse);
		}
*/		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public static synchronized DomRepresentation getDomRepresentation(
			Object object) {
		DomRepresentation representation = null;
		try {
			// create representation and get its empty dom
			representation = new DomRepresentation(MediaType.TEXT_XML);
			System.out.println("1");
			Document d = representation.getDocument();
			System.out.println("2");

			final Marshaller marshaller = getWBCJAXBContext()
					.createMarshaller();
			System.out.println("3");
			
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", 
					new AtlasNamespacePrefixMapper());
			System.out.println("4");

			// marshal object into representation's dom
			Class clazz = object.getClass();
			System.out.println("5");
			QName qName = new QName(
					"http://www.opengis.net/gml/3.2",
					clazz.getSimpleName());
			System.out.println("6");
			JAXBElement jaxbElement = new JAXBElement(qName, clazz, object);
			System.out.println("7");
			marshaller.marshal(jaxbElement, d);
			System.out.println("8");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return representation;
	}

	protected static JAXBContext jaxbContext = null;

	/**
	 * Lazily instantiates a single version of the JAXBContext since it is slow
	 * to create and can be reused throughout the lifetime of the app.
	 * 
	 * @return
	 */
	public static JAXBContext getWBCJAXBContext() {
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance("generated");
			} catch (JAXBException e) {
				throw new RuntimeException(e);
			}
		}
		return jaxbContext;
	}

}
