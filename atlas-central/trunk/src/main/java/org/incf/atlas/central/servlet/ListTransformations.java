package org.incf.atlas.central.servlet;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.incf.atlas.central.resource.CentralServiceVO;
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

public class ListTransformations implements ExecuteProcessHandler {

	private final Logger logger = LoggerFactory.getLogger(
			ListTransformations.class);
	
	String hostName = "";
	String portNumber = "";
	String servicePath = "";

	CentralConfigurator config = CentralConfigurator.INSTANCE;

	private ServletContext context;

	String uri = "";
	String incfDeployHostName = "";
	String incfDeployPortNumber = "";

	public ListTransformations(ServletContext context) {
		this.context = context;
	}

/*	public ListTransformations(Context context, Request request, 
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
*/
	/* 
	 * Handle GET requests.
	 * 
	 * (non-Javadoc)
	 * @see org.wholebrainproject.wbc.server.resource.DataRepositoryResource#represent(org.restlet.resource.Variant)
	 */
	public String getProcessResponse(ServletContext context, HttpServletRequest request,  
			HttpServletResponse response, DataInputs dataInputsString) {

		CentralServiceVO vo = new CentralServiceVO();

		uri = request.getRequestURL().toString(); 
		incfDeployHostName = request.getServerName();
		incfDeployPortNumber = String.valueOf(request.getServerPort());

		try { 

		    // Uncomment this after putting exception handling in place in servlet
/*		    if (dataInputsString == null || dataInputsString.length() == 0) {
		        ExceptionHandler eh = getExceptionHandler();
		        eh.addExceptionToReport(ExceptionCode.MISSING_PARAMETER_VALUE, null, 
		                new String[] { "All DataInputs were missing." });

		        // there is no point in going further, so return
		        return getExceptionRepresentation();
		    }
*/			
		    // parse dataInputs string
	        //DataInputs dataInputs = new DataInputs(dataInputsString);

	        vo.setFromSRSCodeOne(dataInputsString.getValue("inputsrsname"));
	        vo.setFromSRSCode(dataInputsString.getValue("inputsrsname"));
	        vo.setToSRSCodeOne(dataInputsString.getValue("outputsrsname"));
	        vo.setToSRSCode(dataInputsString.getValue("outputsrsname"));
	        vo.setFilter(dataInputsString.getValue("filter"));

	        System.out.println("From SRS Code: " + vo.getFromSRSCodeOne());
	        System.out.println("To SRS Code: " + vo.getToSRSCodeOne());
	        System.out.println("Filter: " + vo.getFilter());

		    // Uncomment this after putting exception handling in place in servlet
	        //validateSrsName(vo.getFromSRSCodeOne());
	        //validateSrsName(vo.getToSRSCodeOne());

		    // Uncomment this after putting exception handling in place in servlet
/*	        if (exceptionHandler != null) {
	            return getExceptionRepresentation();
	        }
*/
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        String currentTime = dateFormat.format(date);
        vo.setCurrentTime(currentTime);

        vo.setUrlString(uri.toString());
        vo.setIncfDeployHostname(incfDeployHostName);
        vo.setIncfDeployPortNumber(incfDeployPortNumber);

/*		ObjectFactory of = new ObjectFactory();
		CoordinateTransformationChainResponse coordinateChain = of.createCoordinateTransformationChainResponse();
*/
        //Start - ListTransformation Method Call Identifier 
        vo.setFlag("ListTransformations");
        //End

        CentralUtil util = new CentralUtil(); 
		String responseString = util.getCoordinateTransformationChain(vo);

	    // Uncomment this after putting exception handling in place in servlet
/*		if ( responseString.startsWith("Error:")) {
			responseString = responseString.replaceAll("Error: ", "");
	        ExceptionHandler eh = getExceptionHandler();
	        eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
	                new String[] { responseString });

	        // there is no point in going further, so return
	        return getExceptionRepresentation();
		}
*/
		//return document.xmlText(opt);
		return responseString;

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
