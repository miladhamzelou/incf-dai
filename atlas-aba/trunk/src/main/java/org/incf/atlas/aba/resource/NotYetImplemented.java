package org.incf.atlas.aba.resource;

import javax.xml.bind.JAXBException;

import org.incf.atlas.aba.util.Constants;
import org.incf.atlas.aba.util.ExceptionCode;
import org.incf.atlas.aba.util.ExceptionHandler;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

public class NotYetImplemented extends Resource {

	public NotYetImplemented(Context context, Request request, 
			Response response) {
		super(context, request, response);
		getVariants().add(new Variant(MediaType.APPLICATION_XML));
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		
		// prepare an ExceptionReport
		ExceptionHandler eh = new ExceptionHandler(
				Constants.getInstance().getDefaultVersion());
		eh.handleException(ExceptionCode.NOT_APPLICABLE_CODE, null, 
				new String[] { "This function has not yet been implemented." });
		
		// generate xml
		String exceptionReportXml = null;
		try {
			eh.getXMLExceptionReport();
		} catch (JAXBException e) {
			throw new ResourceException(e);
		}
		
		// return it
		return new StringRepresentation(exceptionReportXml);
	}

}
