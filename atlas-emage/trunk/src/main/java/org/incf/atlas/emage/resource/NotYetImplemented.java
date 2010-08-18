package org.incf.atlas.emage.resource;

import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This resource is used for functions planned but not yet implemented.
 * 
 * @author dave
 */
public class NotYetImplemented extends BaseResouce {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	String message;

	public NotYetImplemented(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());
		
		message = "This process has not yet been implemented.";
		
		logger.info(message);
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
        
        // prepare an ExceptionReport
        ExceptionHandler exHandler = getExceptionHandler();
        exHandler.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
                new String[] { message });
        return getExceptionRepresentation();
	}

}
