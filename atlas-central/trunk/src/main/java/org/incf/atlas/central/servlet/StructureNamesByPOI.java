package org.incf.atlas.central.servlet;

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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.central.resource.CentralServiceVO;
import org.incf.atlas.central.resource.KeyValueBean;
import org.incf.atlas.central.util.CentralConfigurator;
import org.incf.atlas.central.util.DataInputs;
import org.incf.atlas.central.util.ReadXML;
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

public class StructureNamesByPOI implements ExecuteProcessHandler {

	private final Logger logger = LoggerFactory.getLogger(
			StructureNamesByPOI.class);

	CentralConfigurator config = CentralConfigurator.INSTANCE;

	String uri = "";
	String incfDeployHostName = "";
	String incfDeployPortNumber = "";
	
	private ServletContext context;
	
	public StructureNamesByPOI(ServletContext context) {
		this.context = context;
	}
	
	public String getProcessResponse(ServletContext context, HttpServletRequest request,  
			HttpServletResponse response, DataInputs dataInputs) {

		uri = request.getRequestURL().toString(); 
		incfDeployHostName = request.getServerName();
		incfDeployPortNumber = String.valueOf(request.getServerPort());

		CentralServiceVO vo = new CentralServiceVO();

		try {

		    // make sure we have something in dataInputs
/*		    if (dataInputsString == null || dataInputsString.length() == 0) {
		        ExceptionHandler eh = getExceptionHandler();
		        eh.addExceptionToReport(ExceptionCode.MISSING_PARAMETER_VALUE, null, 
		                new String[] { "All DataInputs were missing." });

		        // there is no point in going further, so return
		        return getExceptionRepresentation();
		    }
*/

	        vo.setFromSRSCodeOne(dataInputs.getValue("srsname"));
	        vo.setFromSRSCode(dataInputs.getValue("srsname"));
	        vo.setFilter(dataInputs.getValue("filter"));
	        vo.setVocabulary(dataInputs.getValue("vocabulary"));

	        System.out.println("From SRS Code: " + vo.getFromSRSCodeOne());
	        System.out.println("Filter: " + vo.getFilter());

	        //validate data inputs
	        //validateSrsName(vo.getFromSRSCodeOne());
	        //Double[] poiCoords = validateCoordinate(dataInputs);

	        vo.setOriginalCoordinateX(dataInputs.getValue("x"));
	        vo.setOriginalCoordinateY(dataInputs.getValue("y"));
	        vo.setOriginalCoordinateZ(dataInputs.getValue("z"));

	        ArrayList list = new ArrayList();

	        // if any validation exceptions, no reason to continue
/*	        if (exceptionHandler != null) {
	            return getExceptionRepresentation();
	        }
*/

	        //Step 1 - Call GetProcessByIdentifier(identifier) - I am ignoring this method right now due to short of time

	        //Step 2 - 
	        ArrayList completeStructureList = new ArrayList();
	        ReadXML readXML = new ReadXML();

	        // 2a - Call the method from ABA Hub
	        String abaURL = "http://"+incfDeployHostName+":"+incfDeployPortNumber+"/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName="+vo.getFromSRSCode()+";x="+vo.getOriginalCoordinateX()+";y="+vo.getOriginalCoordinateY()+";z="+vo.getOriginalCoordinateZ()+";vocabulary="+vo.getVocabulary()+";filter="+vo.getFilter();
	        completeStructureList = readXML.getStructureData(abaURL, completeStructureList);

	        // 2b - Call the method from UCSD Hub
	        String ucsdURL = "http://"+incfDeployHostName+":"+incfDeployPortNumber+"/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName="+vo.getFromSRSCode()+";x="+vo.getOriginalCoordinateX()+";y="+vo.getOriginalCoordinateY()+";z="+vo.getOriginalCoordinateZ()+";vocabulary="+vo.getVocabulary()+";filter="+vo.getFilter();
	        completeStructureList = readXML.getStructureData(ucsdURL, completeStructureList);

	        // 2c - Call the method from WHS Hub
	        String whsURL = "http://"+incfDeployHostName+":"+incfDeployPortNumber+"/atlas-whs?service=WPS&version=1.0.0&request=Execute&Identifier=GetStructureNamesByPOI&DataInputs=srsName="+vo.getFromSRSCode()+";x="+vo.getOriginalCoordinateX()+";y="+vo.getOriginalCoordinateY()+";z="+vo.getOriginalCoordinateZ()+";vocabulary="+vo.getVocabulary()+";filter="+vo.getFilter();
	        completeStructureList = readXML.getStructureData(whsURL, completeStructureList);

/*		vo = parseResponseStringForBrainRegionNames(responseString, vo);

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
*/		//End

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
        vo.setIncfDeployHostname(incfDeployHostName);
        vo.setIncfDeployPortNumber(incfDeployPortNumber);

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

		Iterator iterator = completeStructureList.iterator();
		KeyValueBean keyValue = null;
		while ( iterator.hasNext() ) {
			
			keyValue = (KeyValueBean)iterator.next();
	    	StructureTerms terms = rootDoc.addNewStructureTerms();
	    	StructureTermType term1 = terms.addNewStructureTerm();
	    	
	    	Code t1code =  term1.addNewCode();
	    	t1code.setCodeSpace(vo.getFromSRSCode());
	    	t1code.setIsDefault(true);
	    	//t1code.setStructureID("");
	    	t1code.setStringValue(keyValue.getKey());
	
	    	//term1.setUri("");
	    	IncfNameType t1name = term1.addNewName();
	    	//t1name.setStringValue("");
	    	term1.addNewDescription().setStringValue(keyValue.getValue());
		}
		
    	ArrayList errorList = new ArrayList();
    	opt.setErrorListener(errorList);
    	boolean isValid = document.validate(opt);

		return document.xmlText(opt);
        
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
	private CentralServiceVO parseResponseStringForBrainRegionNames( String xmlData, CentralServiceVO vo ) { 

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
