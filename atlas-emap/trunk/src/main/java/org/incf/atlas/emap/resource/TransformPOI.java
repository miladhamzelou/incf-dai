package org.incf.atlas.emap.resource;

import java.net.URI;

import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.incf.atlas.emap.util.DataInputs;
import org.incf.atlas.emap.util.EMAPConfigurator;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformPOI extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(
			TransformPOI.class);

	EMAPConfigurator config = EMAPConfigurator.INSTANCE;

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
	int randomGMLID1 = 0;
	int randomGMLID2 = 0;
	Response response = null;
	URI uri = null;
	
	public TransformPOI(Context context, Request request, 
			Response response) {
		super(context, request, response);

		logger.debug("Instantiated {}.", getClass());
		this.response = response;
		
		try { 
			uri = new URI(request.getResourceRef().toString());
			//response.redirectPermanent("http://www.google.com");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public Representation represent(Variant variant) throws ResourceException {

		EMAPServiceVO vo = new EMAPServiceVO();

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

	        vo.setOriginalCoordinateX(dataInputs.getValue("x"));
	        vo.setOriginalCoordinateY(dataInputs.getValue("y"));
	        vo.setOriginalCoordinateZ(dataInputs.getValue("z"));

	        // if any validation exceptions, no reason to continue
	        if (exceptionHandler != null) {
	            return getExceptionRepresentation();
	        }

			System.out.println("EMAP: " + emap);
			System.out.println("EHS09: " + whs09);
	        //http://lxbisel.macs.hw.ac.uk:8080/EMAPServiceController?request=Execute&identifier=TransformPOI&dataInputs=inputSRSCode=Mouse_WHS_1.0;targetSRSCode=Mouse_EMAP-T26_1.0;x=12;y=-29;z=-73
			if ( vo.getFromSRSCodeOne().equalsIgnoreCase(whs10) && vo.getToSRSCodeOne().equalsIgnoreCase(emap) ) {
			System.out.println("Inside WHS 09 to EMAP Transformation");
			String emapServerHostName = config.getValue("incf.emap.host.name");
	        String emapServerPortNumber = config.getValue("incf.emap.port.number");
	        String emapServerServicePath = config.getValue("incf.emap.service.path");
	        String identifier = "&identifier=TransformPOI";
	        String datainputs = "&dataInputs=inputSRSCode="+vo.getFromSRSCode()+";targetSRSCode="+vo.getToSRSCode()+";x="+vo.getOriginalCoordinateX()+";y="+vo.getOriginalCoordinateY()+";z="+vo.getOriginalCoordinateZ();

	        String emapTransformPOIURL = "http://"+emapServerHostName + emapServerPortNumber + emapServerServicePath + identifier + datainputs;

	        System.out.println("emapTransformPOIURL - "+ emapTransformPOIURL);
	        response.redirectPermanent(emapTransformPOIURL);
	        
	        } else {
				System.out.println("No Such Transformation");
		        ExceptionHandler eh = getExceptionHandler();
		        eh.addExceptionToReport(ExceptionCode.MISSING_PARAMETER_VALUE, null, 
		                new String[] { "No such transformation is supported under EMAP Hub." });

		        // there is no point in going further, so return
		        return getExceptionRepresentation();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}


}
