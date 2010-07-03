package org.incf.atlas.aba.resource;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestResource extends Resource {

	private final Logger logger = LoggerFactory.getLogger(TestResource.class);
	
	String message;
	
	public TestResource(Context context, Request request, Response response) {
		super(context, request, response);
		String service = (String) request.getAttributes().get("service"); 
		String version = (String) request.getAttributes().get("version"); 
		String request_ = (String) request.getAttributes().get("request"); 
		String identifier = (String) request.getAttributes().get("identifier"); 
		String dataInputs = (String) request.getAttributes().get("dataInputs"); 
		String responseForm = (String) request.getAttributes().get("responseForm"); 
		
		message = String.format(
				"service     : %s%n" +
				"version     : %s%n" +
				"request     : %s%n" +
				"identifier  : %s%n" +
				"dataInputs  : %s%n" +
				"responseForm: %s",
				service, version, request_, identifier, dataInputs, responseForm);
		
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		return new StringRepresentation("TestResource\n" + message);
	}

}
