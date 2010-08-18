package org.incf.atlas.aba.resource;

import java.io.File;

import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//test:
//http://incf-dev-local.crbs.ucsd.edu:8080/atlas-aba?service=WPS&version=1.0.0&request=DescribeProcess

public class ProcessDescriptions extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    // cached file
    private static final String RESPONSE_FILE_NAME = "ProcessDescriptions.xml";
    
	public ProcessDescriptions(Context context, Request request,
			Response response) {
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
	    
	    // if there are exceptions, send an exception report
	    if (exceptionHandler != null) {
	    	String xmlReport = exceptionHandler.getXMLExceptionReport();
	    	logger.error("Exception Report returned to client: \n{}", 
	    			xmlReport);
	        return getExceptionRepresentation();
	    }
	    
	    // look for cached file first
	    File cachedResponse = new File(cacheDir, RESPONSE_FILE_NAME);
	    if (cachedResponse.exists()) {
	        return new FileRepresentation(cachedResponse, 
	        		MediaType.APPLICATION_XML);
	    }
	    
        // prepare an ExceptionReport
	    String message = "File " + RESPONSE_FILE_NAME + " not found.";
        ExceptionHandler exHandler = getExceptionHandler();
        exHandler.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
                new String[] { message });
        logger.error(message);
        return getExceptionRepresentation();
	}
	
}
