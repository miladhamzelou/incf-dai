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
import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.incf.atlas.common.util.XMLUtilities;
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
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.VirtualHost;
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

	ABAConfigurator config = ABAConfigurator.INSTANCE;

	String abaReference = config.getValue("srsname.abareference.10");
	String abaVoxel = config.getValue("srsname.abavoxel.10");
	String agea = config.getValue("srsname.agea.10");
	String whs09 = config.getValue("srsname.whs.09");
	String whs10 = config.getValue("srsname.whs.10");
	String emap = config.getValue("srsname.emap.10");
	String paxinos = config.getValue("srsname.paxinos.10");

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

		servicePath = "/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI";
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

	        //Start - Common code used for coordinate transformation
	        String transformedCoordinatesString = "";
			// Convert the coordinates ABAVOXEL into PAXINOS
	        if ( vo.getFromSRSCode().equalsIgnoreCase(abaVoxel) ) { 
		        	vo.setTransformedCoordinateX(vo.getOriginalCoordinateX());
		        	vo.setTransformedCoordinateY(vo.getOriginalCoordinateY());
		        	vo.setTransformedCoordinateZ(vo.getOriginalCoordinateZ());
		    } else { 
	        	//Call getTransformationChain method here...
		    	//ABAVoxel
		    	vo.setOriginalCoordinateX(";x="+vo.getOriginalCoordinateX());
		    	vo.setOriginalCoordinateY(";y="+vo.getOriginalCoordinateY());
		    	vo.setOriginalCoordinateZ(";z="+vo.getOriginalCoordinateZ());
		    	vo.setToSRSCode(abaVoxel);
		    	vo.setToSRSCodeOne(abaVoxel);

		    	//Construct GetTransformationChain URL
		    	//http://132.239.131.188:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum
		    	String hostName = config.getValue("incf.deploy.host.name");
		    	String portNumber = ":8080";
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
	        	ABAUtil util = new ABAUtil();
	        	String[] tempArray = util.getTabDelimNumbers(transformedCoordinatesString);
	        	vo.setTransformedCoordinateX(tempArray[0]);
	        	vo.setTransformedCoordinateY(tempArray[1]);
	        	vo.setTransformedCoordinateZ(tempArray[2]);
		    }
	        //End
	        
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
		xCriteria.setValue(vo.getOriginalCoordinateX().replace(";x=", ""));
		
		InputStringType yCriteria = (InputStringType) criterias.addNewInput()
			.changeType(InputStringType.type);
		yCriteria.setName("y");
		yCriteria.setValue(vo.getOriginalCoordinateY().replace(";y=", ""));
		
		InputStringType zCriteria = (InputStringType) criterias.addNewInput()
			.changeType(InputStringType.type);
		zCriteria.setName("z");
		zCriteria.setValue(vo.getOriginalCoordinateZ().replace(";z=", ""));

    	InputStringType srsCodeCriteria = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
    	srsCodeCriteria.setName("StructureVocabulary");
    	srsCodeCriteria.setValue(vo.getVocabulary());
    	InputStringType filterCodeCriteria = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
    	filterCodeCriteria.setName("StructureFilter");
		filterCodeCriteria.setValue(vo.getFilter());

		query.addNewQueryUrl();
		query.getQueryUrl().setName("GetStructureNamesByPOI");
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

    	ArrayList errorList = new ArrayList();
    	opt.setErrorListener(errorList);
    	boolean isValid = document.validate(opt);

		return new StringRepresentation(document.xmlText(opt),MediaType.APPLICATION_XML);
        
        //End - old implementation

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
