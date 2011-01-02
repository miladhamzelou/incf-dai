package org.incf.aba.atlas.process;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import net.opengis.gml.x32.PointType;

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
import org.incf.atlas.waxml.generated.POIType;
import org.incf.atlas.waxml.generated.TransformationResponseDocument;
import org.incf.atlas.waxml.generated.TransformationResponseType;
import org.incf.atlas.waxml.utilities.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListTransformations implements Processlet {

    private static final Logger LOG = LoggerFactory.getLogger(
            ListTransformations.class);

	ABAConfigurator config = ABAConfigurator.INSTANCE;

	String abaReference = config.getValue("srsname.abareference.10");
	String abaVoxel = config.getValue("srsname.abavoxel.10");
	String agea = config.getValue("srsname.agea.10");
	String whs09 = config.getValue("srsname.whs.09");
	String whs10 = config.getValue("srsname.whs.10");
	String emap = config.getValue("srsname.emap.10");
	String paxinos = config.getValue("srsname.paxinos.10");

	String abavoxel2agea = config.getValue("code.abavoxel2agea.v1");
	String agea2abavoxel = config.getValue("code.agea2abavoxel.v1");
	String whs092agea = config.getValue("code.whs092agea.v1");
	String agea2whs09 = config.getValue("code.agea2whs09.v1");
	String whs092whs10 = config.getValue("code.whs092whs10.v1");
	String whs102whs09 = config.getValue("code.whs102whs09.v1");
	String abareference2abavoxel = config.getValue("code.abareference2abavoxel.v1");
	String abavoxel2abareference = config.getValue("code.abavoxel2abareference.v1");
	String paxinos2whs09 = config.getValue("code.paxinos2whs09.v1");
	String whs092paxinos = config.getValue("code.whs092paxinos.v1");
	
	
	//private String dataInputString;
	//private DataInputs dataInputs;
	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	String responseString = "";
	int randomGMLID1 = 0;
	int randomGMLID2 = 0;
	
    @Override
    public void process(ProcessletInputs in, ProcessletOutputs out, 
            ProcessletExecutionInfo info) throws ProcessletException {
    	
		ABAServiceVO vo = new ABAServiceVO();

		try { 

    		System.out.println(" Inside ListTransformations");
    		String transformationCode = "";
    		if (in != null){
        		System.out.println(" Inside parameter value... ");
    			transformationCode = ((LiteralInput) in.getParameter("transformationCode")).getValue();
    		}
/*    		if (transformationCode == null) {
    			throw new MissingParameterException(
    					"transformationCode is a required parameter", "transformationCode");
    		}
*/
			//transformationCode = ((LiteralInput) in.getParameter("transformationCode")).getValue();
    		System.out.println("Before the check condition");
			if ( transformationCode.equals("") || transformationCode == null ) { 
	        	System.out.println("Inside Empty DataInputString.");
		        vo.setTransformationCode("all_To_all_v1.0");
	        } else {
		        vo.setTransformationCode(transformationCode);
	        }
    		System.out.println("After the check condition");

			String[] transformationNameArray;
			String delimiter = "_To_";
			transformationNameArray = vo.getTransformationCode().split(delimiter);
			String fromSRSCode = transformationNameArray[0];
			String toSRSCode = transformationNameArray[1].replace("_v1.0", "");

			System.out.println(" Input SRS Name: " + fromSRSCode);
			System.out.println(" Output SRS Name: " + toSRSCode);

	        vo.setFromSRSCodeOne(fromSRSCode);
	        vo.setFromSRSCode(fromSRSCode);
	        vo.setToSRSCodeOne(toSRSCode);
	        vo.setToSRSCode(toSRSCode);

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        String currentTime = dateFormat.format(date);
        vo.setCurrentTime(currentTime);

        //Start - FIXME - Uncomment below two lines and comment the other three lines 
    	//String hostName = uri.getHost();
    	//String portNumber = delimitor + uri.getPort();
        hostName = config.getValue("incf.deploy.host.name");
        portNumber = config.getValue("incf.aba.port.number");
    	String delimitor = config.getValue("incf.deploy.port.delimitor");
    	portNumber = delimitor + portNumber;
        //End - FIXME
        
        //vo.setUrlString(uri.toString());
        vo.setIncfDeployHostname(hostName);
        vo.setIncfDeployPortNumber(portNumber);

		ComplexOutput complexOutput = (ComplexOutput) out.getParameter( 
	 			"ListTransformationsOutput");

		ABAUtil util = new ABAUtil(); 
		vo.setFlag("ListTransformations");
		String responseString = util.getCoordinateTransformationChain(vo, complexOutput);

		if ( responseString.startsWith("Error:")) {
			responseString = responseString.replaceAll("Error: ", "");
			throw new MissingParameterException(
					responseString, responseString);
		}

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
