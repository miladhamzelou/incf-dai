package org.incf.atlas.aba.resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.aba.util.ABAConfigurator;
import org.incf.atlas.aba.util.ABAUtil;
import org.incf.atlas.aba.util.AtlasNamespacePrefixMapper;
import org.incf.atlas.aba.util.DataInputs;
import org.incf.atlas.aba.util.ExceptionCode;
import org.incf.atlas.aba.util.ExceptionHandler;
import org.incf.atlas.waxml.generated.IncfNameType;
import org.incf.atlas.waxml.generated.InputStringType;
import org.incf.atlas.waxml.generated.QueryInfoType;
import org.incf.atlas.waxml.generated.StructureTermType;
import org.incf.atlas.waxml.generated.StructureTermsResponseDocument;
import org.incf.atlas.waxml.generated.StructureTermsResponseType;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.generated.StructureTermType.Code;
import org.incf.atlas.waxml.generated.StructureTermsResponseType.StructureTerms;
import org.incf.atlas.waxml.utilities.Utilities;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class StructureNamesByPOI extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(
			StructureNamesByPOI.class);

	//private String dataInputString;
	//private DataInputs dataInputs;
	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	String url = "";
	String responseString = "";

	public StructureNamesByPOI(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
/*		System.out.println("You are in StructureNamesByPOIResource");
		dataInputString = (String) request.getAttributes().get("dataInputs"); 
		System.out.println("dataInputString " + dataInputString );

		dataInputs = new DataInputs(dataInputString);
*/
		//FIXME - amemon - read the hostname from the config file 
		ABAConfigurator config = ABAConfigurator.INSTANCE;
		hostName = config.getValue("incf.deploy.host.name");
		System.out.println("****HOSTNAME**** - " + hostName);
		portNumber = ":8080";

		servicePath = "atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI";
		//servicePath = "/atlas-aba?Request=Execute&Identifier=GetStructureNamesByPOI";

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

	        vo.setFromSRSCodeOne(dataInputs.getValue("srsName"));
	        vo.setFromSRSCode(dataInputs.getValue("srsName"));
	        vo.setFilter(dataInputs.getValue("filter"));
	        vo.setVocabulary(dataInputs.getValue("vocabulary"));

	        System.out.println("From SRS Code: " + vo.getFromSRSCodeOne());
	        System.out.println("Filter: " + vo.getFilter());

	        // validate data inputs
	        validateSrsName(vo.getFromSRSCodeOne());
	        Double[] poiCoords = validateCoordinate(dataInputs);

	        vo.setOriginalCoordinateX(String.valueOf(poiCoords[0].intValue()));
	        vo.setOriginalCoordinateY(String.valueOf(poiCoords[1].intValue()));
	        vo.setOriginalCoordinateZ(String.valueOf(poiCoords[2].intValue()));

	        // if any validation exceptions, no reason to continue
	        if (exceptionHandler != null) {
	            return getExceptionRepresentation();
	        }
	        
/*		String fromSRSCode = "";
		String vocabulary = "";
		String filter = "";
		String coordinateX = "";
		String coordinateY = "";
		String coordinateZ = "";
		String responseString = "";

		// text return for debugging
		ABAServiceVO vo = new ABAServiceVO();
		Set<String> dataInputKeys = dataInputs.getKeys();
		for (String key : dataInputKeys) {
			if (key.equalsIgnoreCase("srsName")) {
				fromSRSCode = dataInputs.getValue(key);
				vo.setFromSRSCode(fromSRSCode);
				vo.setFromSRSCodeOne(fromSRSCode);
			} else if (key.equalsIgnoreCase("vocabulary")) {
				vocabulary = dataInputs.getValue(key);
				vo.setVocabulary(vocabulary);
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
				coordinateZ = dataInputs.getValue(key);
				vo.setOriginalCoordinateZ(coordinateZ);
			}
		}
*/
		String structureName = ""; 
		//Start - Call the main method here
		ABAUtil util = new ABAUtil();
		if ( vo.getFilter().equalsIgnoreCase("structureset:fine")) {  
			responseString = util.getFineStructureNameByPOI(vo);
			StringTokenizer tokens = new StringTokenizer(responseString); 
			while ( tokens.hasMoreTokens() ) {
				structureName = tokens.nextToken();
				System.out.println("Structure Name is - " + structureName);
			}

			//Start - Exception Handling
			if ( structureName == null || structureName.equals("") ) {
		        ExceptionHandler eh = getExceptionHandler();
		        eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
		                new String[] { "No Structures Found." });

		        // there is no point in going further, so return
		        return getExceptionRepresentation();
			}
			//End

		} else if ( vo.getFilter().equalsIgnoreCase("structureset:anatomic")) { 
			responseString = util.getAnatomicStructureNameByPOI(vo);
			StringTokenizer tokens = new StringTokenizer(responseString); 
			while ( tokens.hasMoreTokens() ) {
				structureName = tokens.nextToken();
				System.out.println("Structure Name is - " + structureName);
			}
			//Start - Exception Handling
			if ( structureName == null || structureName.equals("") ) {
		        ExceptionHandler eh = getExceptionHandler();
		        eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
		                new String[] { "No Structures Found." });

		        // there is no point in going further, so return
		        return getExceptionRepresentation();
			}
			//End
		} else {
	        ExceptionHandler eh = getExceptionHandler();
	        eh.addExceptionToReport(ExceptionCode.INVALID_PARAMETER_VALUE, null, 
	                new String[] { "Filter type - " + vo.getFilter() + " is not supported." });
	        // there is no point in going further, so return
	        return getExceptionRepresentation();
		}
		//End

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        String currentTime = dateFormat.format(date);
        vo.setCurrentTime(currentTime);

        //Generating 2 random number to be used as GMLID
        int randomGMLID1 = 0;
        Random randomGenerator1 = new Random();
	    for (int idx = 1; idx <= 10; ++idx){
	      randomGMLID1 = randomGenerator1.nextInt(100);
	    }

        url = "http://" + hostName + portNumber + servicePath + "&DataInputs=" + dataInputsString;
        vo.setUrlString(url);

    	XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
    	opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
    	opt.setSaveNamespacesFirst();
    	opt.setSaveAggressiveNamespaces();
    	opt.setUseDefaultNamespace();

        StructureTermsResponseDocument document =	StructureTermsResponseDocument.Factory.newInstance(); 
    	
    	StructureTermsResponseType rootDoc =	document.addNewStructureTermsResponse();
    	QueryInfoType query = rootDoc.addNewQueryInfo();
    	
    	Utilities.addMethodNameToQueryInfo(query, "GetStructureNamesByPOI  ", url);

    	Criteria criterias = query.addNewCriteria();
    	
    	//Changes
/*    	InputPOIType poiCriteria = (InputPOIType) criterias.addNewInput().changeType(InputPOIType.type);
    	poiCriteria.setName("POI");
    	PointType pnt = poiCriteria.addNewPOI().addNewPoint();
    	pnt.setId(String.valueOf(randomGMLID1));
    	pnt.setSrsName(vo.getFromSRSCode());
    	pnt.addNewPos();
    	//pnt.getPos().setStringValue(Double.parseDouble(vo.getOriginalCoordinateX()) + " "+ Double.parseDouble(vo.getOriginalCoordinateY()) + " " +Double.parseDouble(vo.getOriginalCoordinateZ()));
    	pnt.getPos().setStringValue(vo.getOriginalCoordinateX() + " "+ vo.getOriginalCoordinateY() + " " +vo.getOriginalCoordinateZ());
*/
    	
    	InputStringType srsCriteria = (InputStringType) criterias.addNewInput()
    	.changeType(InputStringType.type);
    	srsCriteria.setName("srsName");
    	srsCriteria.setValue(vo.getFromSRSCode());

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

    	InputStringType srsCodeCriteria = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
    	srsCodeCriteria.setName("StructureVocabulary");
    	srsCodeCriteria.setValue(vo.getVocabulary());
    	InputStringType filterCodeCriteria = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
    	filterCodeCriteria.setName("StructureFilter");
		filterCodeCriteria.setValue(vo.getFilter());

		query.addNewQueryUrl();
		query.getQueryUrl().setName("StructureNamesByPOI");
		query.getQueryUrl().setStringValue(url);
		query.setTimeCreated(Calendar.getInstance());

    	StructureTerms terms = rootDoc.addNewStructureTerms();
    	StructureTermType term1 = terms.addNewStructureTerm();
    	Code t1code =  term1.addNewCode();
    	t1code.setCodeSpace(vo.getFromSRSCode());
    	t1code.setIsDefault(true);
    	//t1code.setStructureID("");
    	t1code.setStringValue(structureName);

    	//term1.setUri("");
    	IncfNameType t1name = term1.addNewName();
    	//t1name.setStringValue("");
    	term1.addNewDescription().setStringValue("Term - " + structureName + " derived from ABA hub based on the supplied POI.");

/*    	FeatureReferenceType t1ft = term1.addNewFeature();
    	Centroid t1c = t1ft.addNewCentroid();
    	t1c.addNewPoint().addNewPos().setStringValue(" ");
    	t1c.getPoint().setId(" ");
    	t1c.getPoint().setSrsName(" ");
    	BoundingShapeType t1bound = t1ft.addNewBoundedBy();
    	t1bound.addNewEnvelope();
    	t1bound.getEnvelope().setSrsName(" ");
    	DirectPositionType t1lc = t1bound.getEnvelope().addNewLowerCorner();
    	DirectPositionType t1uc = t1bound.getEnvelope().addNewUpperCorner();
    	t1lc.setStringValue(" ");
    	t1uc.setStringValue(" ");
    	
    	t1ft.addNewUrl().setStringValue(" ");
    	t1ft.getUrl().setSrsName(" ");
    	t1ft.setFormat(GeomFormatEnum.SHAPE.toString());
*/    	
    	ArrayList errorList = new ArrayList();
    	 opt.setErrorListener(errorList);
    	 boolean isValid = document.validate(opt);
    	 
    	 // If the XML isn't valid, loop through the listener's contents,
    	 // printing contained messages.
/*    	 if (!isValid)
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
*/    		
			return new StringRepresentation(document.xmlText(opt),MediaType.APPLICATION_XML);
        
        //Start - old implementation
/*		ObjectFactory of = new ObjectFactory();
		StructureTermsResponse structureTermsResponse = 
			of.createStructureTermsResponse();

		java.math.BigInteger intValue = new BigInteger("1369");

		StructureCode structureCode = new StructureCode();
		structureCode.setCodeSpace(vo.getVocabulary());
		structureCode.setId(intValue);
		structureCode.setValue(structureName);

		StructureTerm structureTerm = new StructureTerm();
		structureTerm.setDescription("");
		structureTerm.setStructureCode(structureCode);

		StructureTerms structureTerms = new StructureTerms();
		structureTerms.setStructureTerm(structureTerm);

		Point point = new Point();
		point.setPos(vo.getOriginalCoordinateX() + ", "+ vo.getOriginalCoordinateY() + ", " +vo.getOriginalCoordinateZ());
		point.setSrsName(vo.getFromSRSCode());

		Input input1 = new Input();
		input1.setName("POI");
		input1.setPoint(point);

		Input input2 = new Input();
		input2.setName("StructureVocabulary");
		input2.setValue(vo.getVocabulary());

		Input input3 = new Input();
		input3.setName("StructureFilter");
		input3.setValue(vo.getFilter());

		QueryInfo.Criteria criteria = new QueryInfo.Criteria();
		criteria.getInput().add(input1);
		criteria.getInput().add(input2);
		criteria.getInput().add(input3);

		//Query Info setters
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.setQueryUrl(vo.getUrlString());
		queryInfo.setTimeCreated(currentTime);
		queryInfo.setCriteria(criteria);

		structureTermsResponse.setQueryInfo(queryInfo);
		structureTermsResponse.setStructureTerms(structureTerms);
*/
        //End - old implementation


		//generate representation based on media type
/*		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
			return getDomRepresentation(document);
		}
*/
/*
		//generate representation based on media type
		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
			return new JaxbRepresentation<StructureTermsResponse>(structureTermsResponse);
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
					"http://www.incf.org/waxML",
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
				jaxbContext = JAXBContext.newInstance("org.incf.waxml");
			} catch (JAXBException e) {
				throw new RuntimeException(e);
			}
		}
		return jaxbContext;
	}

}
