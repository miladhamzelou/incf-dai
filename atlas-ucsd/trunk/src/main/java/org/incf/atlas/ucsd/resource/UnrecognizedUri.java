package org.incf.atlas.ucsd.resource;

import net.opengis.ows._1.ExceptionReport;

import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.incf.atlas.common.util.XMLUtilities;
import org.incf.atlas.ucsd.util.Constants;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnrecognizedUri extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	String message;

	public UnrecognizedUri(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());
		
		message = "Unrecognized URI.";
		
		logger.info(message);
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		
		// prepare an ExceptionReport
	    ExceptionHandler exHandler = getExceptionHandler();
		exHandler.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
				new String[] { message });
		
		// generate xml
		return exHandler.getDomExceptionReport();
	}

}
