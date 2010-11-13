package org.incf.atlas.central.resource;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.incf.atlas.central.util.CentralConfigurator;
import org.incf.atlas.central.util.CentralUtil;
import org.incf.atlas.central.util.DataInputs;
import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoordinateTransformationChain extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(
			CoordinateTransformationChain.class);

	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	URI uri = null;

	String incfDeployHostName = "";
	String incfDeployPortNumber = "";
	CentralConfigurator config = CentralConfigurator.INSTANCE;

	public CoordinateTransformationChain(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());
		try { 
			uri = new URI(request.getResourceRef().toString());
			incfDeployHostName = uri.getHost();
 			incfDeployPortNumber = String.valueOf(uri.getPort());
		} catch (Exception e) {
			e.printStackTrace();
		}

		}

	/* 
	 * Handle GET requests.
	 * 
	 * (non-Javadoc)
	 * @see org.wholebrainproject.wbc.server.resource.DataRepositoryResource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {

		CentralServiceVO vo = new CentralServiceVO();

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

	        vo.setFromSRSCodeOne(dataInputs.getValue("inputsrsname"));
	        vo.setFromSRSCode(dataInputs.getValue("inputsrsname"));
	        vo.setToSRSCodeOne(dataInputs.getValue("outputsrsname"));
	        vo.setToSRSCode(dataInputs.getValue("outputsrsname"));
	        vo.setFilter(dataInputs.getValue("filter"));

	        System.out.println("From SRS Code: " + vo.getFromSRSCodeOne());
	        System.out.println("To SRS Code: " + vo.getToSRSCodeOne());
	        System.out.println("Filter: " + vo.getFilter());

	        // validate data inputs
	        validateSrsName(vo.getFromSRSCodeOne());
	        validateSrsName(vo.getToSRSCodeOne());

	        // if any validation exceptions, no reason to continue
	        if (exceptionHandler != null) {
	            return getExceptionRepresentation();
	        }

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        String currentTime = dateFormat.format(date);
        vo.setCurrentTime(currentTime);

        vo.setUrlString(uri.toString());
        vo.setIncfDeployHostname(incfDeployHostName);
        vo.setIncfDeployPortNumber(incfDeployPortNumber);

		CentralUtil util = new CentralUtil(); 
		String responseString = util.getCoordinateTransformationChain(vo);

		if ( responseString.startsWith("Error:")) {
			responseString = responseString.replaceAll("Error: ", "");
	        ExceptionHandler eh = getExceptionHandler();
	        eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
	                new String[] { responseString });
	        
	        // there is no point in going further, so return
	        return getExceptionRepresentation();
		}

		//return document.xmlText(opt);
		return new StringRepresentation(responseString, MediaType.APPLICATION_XML);

		//generate representation based on media type
/*		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
			return new JaxbRepresentation<CoordinateTransformationChainResponse>(coordinateChain);
		}
*/
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

}
