package org.incf.atlas.central.resource;

import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.InputRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetProcessesByIdentifier extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	// cached file
	private static final String RESPONSE_FILE_NAME = "/GetProcessesByIdentifier.xml";
	
	public GetProcessesByIdentifier(Context context, Request request, Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());
	}

	/* 
	 * Handle GET requests.
	 * 
	 * (non-Javadoc)
	 * @see org.wholebrainproject.wbc.server.resource.DataRepositoryResource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
	    
	    // if there are exceptions, send an excepton report
	    if (exceptionHandler != null) {
	    	String xmlReport = exceptionHandler.getXMLExceptionReport();
	    	logger.error("Exception Report returned to client: \n{}", 
	    			xmlReport);
	        return getExceptionRepresentation();
	    }
	    
	    Representation representation = null;
	    try {
	    	representation = new InputRepresentation(
					this.getClass().getResourceAsStream(RESPONSE_FILE_NAME), 
					MediaType.APPLICATION_XML);
		} catch (Exception e) {
			logger.error("Exception:", e);
		    
	        // prepare an ExceptionReport
		    String message = "File " + RESPONSE_FILE_NAME + " not found.";
	        ExceptionHandler exHandler = getExceptionHandler();
	        exHandler.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
	                new String[] { message });
	        logger.error(message);
	        representation = getExceptionRepresentation();
		}
        return representation;
	}
	
}
