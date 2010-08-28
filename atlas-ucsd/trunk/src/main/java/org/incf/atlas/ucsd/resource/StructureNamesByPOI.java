package org.incf.atlas.ucsd.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.incf.atlas.common.util.XMLUtilities;
import org.incf.atlas.ucsd.util.DataInputs;
import org.incf.atlas.ucsd.util.UCSDConfigurator;
import org.incf.atlas.ucsd.util.UCSDUtil;
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
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
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
import org.xml.sax.InputSource;

public class StructureNamesByPOI extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(
			StructureNamesByPOI.class);

	UCSDConfigurator config = UCSDConfigurator.INSTANCE;

	String abaReference = config.getValue("srsname.abareference.10");
	String abaVoxel = config.getValue("srsname.abavoxel.10");
	String agea = config.getValue("srsname.agea.10");
	String whs09 = config.getValue("srsname.whs.09");
	String whs10 = config.getValue("srsname.whs.10");
	String emap = config.getValue("srsname.emap.10");
	String paxinos = config.getValue("srsname.paxinos.10");

	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	String responseString = "";

	URI uri = null;
	
	public StructureNamesByPOI(Context context, Request request, 
			Response response) {
		super(context, request, response);

		try { 
			uri = new URI(request.getResourceRef().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public Representation represent(Variant variant) throws ResourceException {

		UCSDServiceVO vo = new UCSDServiceVO();

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
	        if ( vo.getFromSRSCode().equalsIgnoreCase(paxinos) ) { 
		        	vo.setTransformedCoordinateX(vo.getOriginalCoordinateX());
		        	vo.setTransformedCoordinateY(vo.getOriginalCoordinateY());
		        	vo.setTransformedCoordinateZ(vo.getOriginalCoordinateZ());
		    } else { 
	        	//Call getTransformationChain method here...
		    	//ABAVoxel
		    	vo.setOriginalCoordinateX(";x="+vo.getOriginalCoordinateX());
		    	vo.setOriginalCoordinateY(";y="+vo.getOriginalCoordinateY());
		    	vo.setOriginalCoordinateZ(";z="+vo.getOriginalCoordinateZ());
		    	vo.setToSRSCode(paxinos);
		    	vo.setToSRSCodeOne(paxinos);

		    	//Construct GetTransformationChain URL
		    	//http://132.239.131.188:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum
		    	String hostName = uri.getHost();
		    	String delimitor = config.getValue("incf.deploy.port.delimitor");
		    	String portNumber = delimitor + uri.getPort();
		    	String servicePath = "/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName="+vo.getFromSRSCode()+";outputSrsName="+vo.getToSRSCode()+";filter=Cerebellum";
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
	        	UCSDUtil util = new UCSDUtil();
	        	String[] tempArray = util.getTabDelimNumbers(transformedCoordinatesString);
	        	vo.setTransformedCoordinateX(tempArray[0]);
	        	vo.setTransformedCoordinateY(tempArray[1]);
	        	vo.setTransformedCoordinateZ(tempArray[2]);
		    }
	        //End
	        
		String structureName = ""; 
		//Start - Call the main method here
		
		String xmlQueryString = xmlQueryStringToGetBrainRegionNames(vo.getTransformedCoordinateX(), 
				vo.getTransformedCoordinateY(), vo.getTransformedCoordinateZ());

    	String hostName = config.getValue("ucsd.webservice.host.name");
    	String portNumber = config.getValue("ucsd.webservice.port.number");

		//Code for webservice client
		String endpoint = "http://"+hostName+portNumber+"/axis/services/SpatialAtlasWebservice";
		Service service = new Service();
		Call call = (Call) service.createCall();
		QName operationName = new QName(endpoint, "getBrainRegionNames");
		call.setOperationName(operationName);
		call.setTargetEndpointAddress(endpoint);
		StringBuffer sb = new StringBuffer();
		sb.append(xmlQueryString);

		responseString = (String) call.invoke(new Object[] { sb
				.toString() }); 

/*		StringTokenizer tokens = new StringTokenizer(responseString); 

		while ( tokens.hasMoreTokens() ) {
			structureName = tokens.nextToken();
			System.out.println("Structure Name is - " + structureName);
		}
*/
		
		vo = parseResponseStringForBrainRegionNames(responseString, vo);

		//Start - Exception Handling
		if ( responseString == null || responseString.equals("") ) {
	        ExceptionHandler eh = getExceptionHandler();
	        eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
	                new String[] { "No Structures Found." });

	        // there is no point in going further, so return
	        return getExceptionRepresentation();
		} else if ( structureName.endsWith("found") ) {//No structures found
	        ExceptionHandler eh = getExceptionHandler();
	        eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
	                new String[] { "No Structures Found." });

	        // there is no point in going further, so return
	        return getExceptionRepresentation();
		} else if ( structureName.endsWith("range") ) {
	        ExceptionHandler eh = getExceptionHandler();
	        eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
	                new String[] { "Coordinates - Out of Range." });

	        // there is no point in going further, so return
	        return getExceptionRepresentation();
		} else if ( structureName.endsWith("issue") ) {
	        ExceptionHandler eh = getExceptionHandler();
	        eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
	                new String[] { "Please contact the administrator to resolve this issue" }); 

	        // there is no point in going further, so return
	        return getExceptionRepresentation();
		}
		//End

		vo = parseResponseStringForBrainRegionNames(responseString, vo);


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

        vo.setUrlString(uri.toString());

    	XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
    	opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
    	opt.setSaveNamespacesFirst();
    	opt.setSaveAggressiveNamespaces();
    	opt.setUseDefaultNamespace();

        StructureTermsResponseDocument document =	StructureTermsResponseDocument.Factory.newInstance(); 
    	
    	StructureTermsResponseType rootDoc =	document.addNewStructureTermsResponse();
    	QueryInfoType query = rootDoc.addNewQueryInfo();
    	
    	Utilities.addMethodNameToQueryInfo(query, "GetStructureNamesByPOI  ", uri.toString());

    	Criteria criterias = query.addNewCriteria();
    	
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
    	srsCodeCriteria.setName("vocabulary");
    	srsCodeCriteria.setValue(vo.getVocabulary());
    	InputStringType filterCodeCriteria = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
    	filterCodeCriteria.setName("filter");
		filterCodeCriteria.setValue(vo.getFilter());

		query.addNewQueryUrl();
		query.getQueryUrl().setName("GetStructureNamesByPOI");
		query.getQueryUrl().setStringValue(uri.toString());
		query.setTimeCreated(Calendar.getInstance());

    	StructureTerms terms = rootDoc.addNewStructureTerms();
    	StructureTermType term1 = terms.addNewStructureTerm();
    	Code t1code =  term1.addNewCode();
    	t1code.setCodeSpace(vo.getFromSRSCode());
    	t1code.setIsDefault(true);
    	//t1code.setStructureID("");
    	t1code.setStringValue(vo.getStructureName());

    	//term1.setUri("");
    	IncfNameType t1name = term1.addNewName();
    	//t1name.setStringValue("");
    	term1.addNewDescription().setStringValue(vo.getStructureDescription());
    	
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

	//Retrieves the unique names for brain regions
	private String xmlQueryStringToGetBrainRegionNames(String transformedCoordinateX, String transformedCoordinateY, String transformedCoordinateZ) { 
		
		StringBuffer xmlQuery = new StringBuffer();
		xmlQuery.append("<request>");
		xmlQuery.append("<coordinateX>").append(transformedCoordinateX.trim()).append("</coordinateX>");
		xmlQuery.append("<coordinateY>").append(transformedCoordinateY.trim()).append("</coordinateY>");
		xmlQuery.append("<coordinateZ>").append(transformedCoordinateZ.trim()).append("</coordinateZ>");
		xmlQuery.append("</request>");

		return xmlQuery.toString();
		
	}
	
	/* 
	 * This method will parse the xmlString coming as a parameter and
	 * prepare the data model which then returns back to the calling method.
	 */ 
	private UCSDServiceVO parseResponseStringForBrainRegionNames( String xmlData, UCSDServiceVO vo ) { 

		//Make the xml document from the xml string
        org.jdom.Document document;
        	
        try {
        	
			SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser",
			        false);

			document = builder.build(new InputSource(
			            new ByteArrayInputStream(xmlData.getBytes())));

			// Get all the childrens
			List structuresList = document.getRootElement().getChildren();
			Iterator iterator = structuresList.iterator();

			// Start - xml parsing
			org.jdom.Element requestElement;

			//We know the structure name is going to be only one, so not running the loop
			for (int i = 0; iterator.hasNext(); i++) {

				requestElement = (org.jdom.Element) iterator.next();
				vo.setStructureName(requestElement.getChildText("brainRegionCode").trim());
				vo.setStructureDescription(requestElement.getChildText("brainRegionDescription").trim());
				//list.add(vo);
				
			}
			//End - xml parsing

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
 			
		return vo;
		
	}


}
