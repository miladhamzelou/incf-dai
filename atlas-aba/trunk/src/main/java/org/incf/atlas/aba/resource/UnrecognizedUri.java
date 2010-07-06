package org.incf.atlas.aba.resource;

import javax.xml.bind.JAXBException;

import org.incf.atlas.aba.util.Constants;
import org.incf.atlas.aba.util.ExceptionCode;
import org.incf.atlas.aba.util.ExceptionHandler;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnrecognizedUri extends Resource {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	String message;

	public UnrecognizedUri(Context context, Request request, Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());
		
		String uriSuffix = (String) request.getAttributes().get("uriSuffix"); 
		message = String.format("Unrecognized URI suffix: %s", uriSuffix);
		logger.warn(message);
		
		getVariants().add(new Variant(MediaType.APPLICATION_XML));
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		
		logger.debug("Executing represent().");
		logger.debug("Message: {}", message);
		
		// prepare an ExceptionReport
		ExceptionHandler eh = new ExceptionHandler(
				Constants.getInstance().getDefaultVersion());
		eh.handleException(ExceptionCode.NOT_APPLICABLE_CODE, null, 
				new String[] { message });
		
		// generate xml
		String exceptionReportXml = null;
		try {
			exceptionReportXml = eh.getXMLExceptionReport();
		} catch (JAXBException e) {
			throw new ResourceException(e);
		}
		
		logger.debug("exceptionReportXml: {}", exceptionReportXml);

		// return it
		getResponse().setStatus(Status.CLIENT_ERROR_FORBIDDEN);
		return new StringRepresentation(exceptionReportXml);
	}

}
