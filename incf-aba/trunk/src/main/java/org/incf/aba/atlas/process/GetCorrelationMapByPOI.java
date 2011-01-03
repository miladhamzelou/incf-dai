package org.incf.aba.atlas.process;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.deegree.commons.utils.kvp.InvalidParameterValueException;
import org.deegree.commons.utils.kvp.MissingParameterException;
import org.deegree.commons.xml.XMLAdapter;
import org.deegree.services.controller.exception.ControllerException;
import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.wps.Processlet;
import org.deegree.services.wps.ProcessletException;
import org.deegree.services.wps.ProcessletExecutionInfo;
import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.ProcessletOutputs;
import org.deegree.services.wps.input.LiteralInput;
import org.deegree.services.wps.output.ComplexOutput;
import org.incf.aba.atlas.util.ABAConfigurator;
import org.incf.aba.atlas.util.ABAServiceVO;
import org.incf.aba.atlas.util.ABAUtil;
import org.incf.aba.atlas.util.XMLUtilities;
import org.incf.atlas.waxml.generated.CorrelatioMapType;
import org.incf.atlas.waxml.generated.CorrelationMapResponseDocument;
import org.incf.atlas.waxml.utilities.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetCorrelationMapByPOI implements Processlet {

    private static final Logger LOG = LoggerFactory.getLogger(
            GetCorrelationMapByPOI.class);

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
	String responseString = "";
    
    @Override
    public void process(ProcessletInputs in, ProcessletOutputs out, 
            ProcessletExecutionInfo info) throws ProcessletException {
    	
		ABAServiceVO vo = new ABAServiceVO();

		try {

    		System.out.println(" Inside GetCorrelationMapByPOI... ");
    		// collect input values
    		String srsName = ((LiteralInput) in.getParameter("srsName")).getValue();
    		String x = ((LiteralInput) in.getParameter("x")).getValue();
    		String y = ((LiteralInput) in.getParameter("y")).getValue();
    		String z = ((LiteralInput) in.getParameter("z")).getValue();
    		String filter = ((LiteralInput) in.getParameter("filter")).getValue();

    		if (srsName == null) {
    			throw new MissingParameterException(
    					"srsName is a required parameter", "srsName");
    		}

    		if (x == null) {
    			throw new MissingParameterException(
    					"x is a required parameter", "x");
    		}

    		if (y == null) {
    			throw new MissingParameterException(
    					"y is a required parameter", "y");
    		}

    		if (z == null) {
    			throw new MissingParameterException(
    					"z is a required parameter", "z");
    		}

    		if (filter == null) {
    			throw new MissingParameterException(
    					"filter is a required parameter", "filter");
    		}
    		
    		// make sure we have something in dataInputs

    	        vo.setFromSRSCodeOne(srsName);
    	        vo.setFromSRSCode(srsName);
    	        vo.setToSRSCodeOne(agea);
    	        vo.setToSRSCode(agea);
    	        vo.setFilter(filter);

    	        System.out.println("From SRS Code: " + vo.getFromSRSCodeOne());
    	        System.out.println("Filter: " + vo.getFilter());

    	        // validate data inputs
    	        vo.setOriginalCoordinateX(x);
    	        vo.setOriginalCoordinateY(y);
    	        vo.setOriginalCoordinateZ(z);

    	        String coordinateX = x;
    	        String coordinateY = y;
    	        String coordinateZ = z;

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

    	        //Start - FIXME - Uncomment below two lines and comment the other three lines 
    	    	//String hostName = uri.getHost();
    	    	//String portNumber = delimitor + uri.getPort();
    	        hostName = config.getValue("incf.deploy.host.name");
    	        portNumber = config.getValue("incf.aba.port.number");
    	    	String delimitor = config.getValue("incf.deploy.port.delimitor");
    	    	portNumber = delimitor + portNumber;
    	        //End - FIXME

    	    	String servicePath = "/atlas-central?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName="+vo.getFromSRSCode()+";outputSrsName="+vo.getToSRSCode()+";filter=Cerebellum";
    	    	String transformationChainURL = "http://"+hostName+portNumber+servicePath;
    	    	XMLUtilities xmlUtilities = new XMLUtilities();
    	    	transformedCoordinatesString = xmlUtilities.coordinateTransformation(transformationChainURL, vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());

            	//Start - exception handling
            	if (transformedCoordinatesString.startsWith("Error:")) {
        			throw new MissingParameterException(
        					"Error: ", transformedCoordinatesString);
            	}
            	//End - exception handling
            	String[] tempArray = util.getTabDelimNumbers(transformedCoordinatesString);
            	vo.setTransformedCoordinateX(tempArray[0]);
            	vo.setTransformedCoordinateY(tempArray[1]);
            	vo.setTransformedCoordinateZ(tempArray[2]);
    	    }
            //End

    		//Start - Exception Handling
    		if (vo.getTransformedCoordinateX().equalsIgnoreCase("out")) {
    			throw new MissingParameterException(
    					"Coordinates - Out of Range.", "Coordinates - Out of Range.");
    		}
    		//End

    		String abaHostName = config.getValue("incf.aba.host.name");
    		String abaPortNumber = config.getValue("incf.aba.port.number");
    		String abaServicePath = config.getValue("incf.aba.service.path");

    		//FIXME - uncomment the below three lines after fixing the hostname issue
/*            vo.setUrlString(uri.toString());
            vo.setIncfDeployHostname(incfDeployHostname);
            vo.setIncfDeployPortNumber(incfDeployPortnumber);
*/
    		//Start - Construct the getcoorelationmap url
    		StringBuffer responseString = new StringBuffer();
    		String mapType = vo.getFilter().replaceAll("maptype:", "");
    		String imageURLPrefix = "http://" + abaHostName
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
    	    int randomGMLID1 = 0;
    	    int randomGMLID2 = 0;

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
/*    		QueryInfoType query = imagesRes.addNewQueryInfo();

    		Utilities.addMethodNameToQueryInfo(query, "GetCorrelationMapByPOI", uri.toString());

    		Criteria criterias = query.addNewCriteria();

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

*/
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

    		ComplexOutput complexOutput = (ComplexOutput) out.getParameter( 
 			"GetCorrelationMapByPOIOutput");

    		// get reader on document; reader --> writer
    		XMLStreamReader reader = document.newXMLStreamReader();
    		XMLStreamWriter writer = complexOutput.getXMLStreamWriter();
    		XMLAdapter.writeElement(writer, reader);

    	} catch (MissingParameterException e) {
            LOG.error(e.getMessage(), e);
        	throw new ProcessletException(new OWSException(e));
        } catch (InvalidParameterValueException e) {
            LOG.error(e.getMessage(), e);
        	throw new ProcessletException(new OWSException(e));
        } catch (Throwable e) {
        	String message = "Unexpected exception occured";
        	LOG.error(message, e);
        	OWSException owsException = new OWSException(message, e, 
        			ControllerException.NO_APPLICABLE_CODE);
        	throw new ProcessletException(owsException);
        } 
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }
	
}
