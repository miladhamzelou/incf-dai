package org.incf.atlas.aba.resource;

import javax.xml.bind.JAXBException;

import org.incf.atlas.aba.util.Constants;
import org.incf.atlas.aba.util.ExceptionCode;
import org.incf.atlas.aba.util.ExceptionHandler;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
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

	public NotYetImplemented(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());
		
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		
		// prepare an ExceptionReport
	    ExceptionHandler eh = getExceptionHandler();
		eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
				new String[] { "This function has not yet been implemented." });
		
		return getExceptionRepresentation();
	}

}
