package org.incf.atlas.aba.resource;

import generated.Criteria;
import generated.Input;
import generated.ObjectFactory;
import generated.QueryInfo;
import generated.QueryURL;
import generated.TransformationResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import net.opengis.gml._3.Point;

import org.incf.atlas.aba.util.ABAConfigurator;
import org.incf.atlas.aba.util.ABAUtil;
import org.incf.atlas.aba.util.AtlasNamespacePrefixMapper;
import org.incf.atlas.aba.util.Constants;
import org.incf.atlas.aba.util.DataInputs;
import org.incf.atlas.aba.util.ExceptionCode;
import org.incf.atlas.aba.util.ExceptionHandler;

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
		ABAConfigurator config = ABAConfigurator.INSTANCE;
		hostName = config.getValue("incf.deploy.host.name");
		System.out.println("****HOSTNAME**** - " + hostName);
		portNumber = ":8080";
		servicePath = "/atlas-aba?Request=Execute&Identifier=TransformPOI";
		
		//getVariants().add(new Variant(MediaType.APPLICATION_XML));

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

	        vo.setFromSRSCodeOne(dataInputs.getValue("inputSrsName"));
	        vo.setFromSRSCode(dataInputs.getValue("inputSrsName"));
	        vo.setToSRSCodeOne(dataInputs.getValue("targetSrsName"));
	        vo.setToSRSCode(dataInputs.getValue("targetSrsName"));
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

	        // if any validation exceptions, no reason to continue
	        if (exceptionHandler != null) {
	            return getExceptionRepresentation();
	        }

		// text return for debugging
		//Set<String> dataInputKeys = dataInputs.getKeys();
		System.out.println("-2");
/*		for (String key : dataInputKeys) {
			if (key.equalsIgnoreCase("inputSrsName")) {
				fromSRSCode = dataInputs.getValue(key);
				vo.setFromSRSCode(fromSRSCode);
				vo.setFromSRSCodeOne(fromSRSCode);
			} else if (key.equalsIgnoreCase("targetSrsName")) {
				toSRSCode = dataInputs.getValue(key);
				vo.setToSRSCode(toSRSCode);
				vo.setToSRSCodeOne(toSRSCode);
			} else if (key.equalsIgnoreCase("filter")) {
				filter = dataInputs.getValue(key);
				vo.setFilter(filter);
			} else if (key.equalsIgnoreCase("x")) {
				coordinateX = dataInputs.getValue(key);
				vo.setOriginalCoordinateX(coordinateX);
			} else if (key.equalsIgnoreCase("y")) {
				coordinateY = dataInputs.getValue(key);
				vo.setOriginalCoordinateY(coordinateY);
			} else if (key.equalsIgnoreCase("z")) {
				System.out.println("-1");
				coordinateZ = dataInputs.getValue(key);
				vo.setOriginalCoordinateZ(coordinateZ);
			}
		}
*/		

		//Start - Call the main method here
		ABAUtil util = new ABAUtil();
		String completeCoordinatesString = util.spaceTransformation(vo);
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

        url = "http://" + hostName + portNumber + servicePath + "&DataInputs=" + dataInputsString;
        vo.setUrlString(url);

		ObjectFactory of = new ObjectFactory();
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

		System.out.println("8");

		//generate representation based on media type - Used this method for renaming the prefixes
		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
			return getDomRepresentation(transformationResponse);
		}		
/*
		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
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
