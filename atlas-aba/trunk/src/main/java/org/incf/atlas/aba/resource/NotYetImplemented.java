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

/**
 * This resource is used for functions planned but not yet implemented.
 * 
 * @author dave
 */
public class NotYetImplemented extends Resource {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public NotYetImplemented(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());
		
		getVariants().add(new Variant(MediaType.APPLICATION_XML));
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		
		// prepare an ExceptionReport
		ExceptionHandler eh = new ExceptionHandler(
				Constants.getInstance().getDefaultVersion());
		eh.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
				new String[] { "This function has not yet been implemented." });
		
		// generate xml
		String exceptionReportXml = null;
		try {
			exceptionReportXml = eh.getXMLExceptionReport();
		} catch (JAXBException e) {
			throw new ResourceException(e);
		}
		
		// return it
		getResponse().setStatus(Status.CLIENT_ERROR_FORBIDDEN);
		return new StringRepresentation(exceptionReportXml);
	}

}
