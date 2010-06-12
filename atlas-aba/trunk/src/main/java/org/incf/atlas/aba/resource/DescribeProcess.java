package org.incf.atlas.aba.resource;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.incf.atlas.aba.util.AtlasNamespacePrefixMapper;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class DescribeProcess  extends Resource {
	
	private static final Logger logger = LoggerFactory.getLogger(GetCapabilities.class);
	
	/*
	 * /incf-services/service/ABAServiceController?request=GetCapabilities&output=xml
	 */
	
	private String responseFormat;

	public DescribeProcess(Context context, Request request, 
			Response response) {
		super(context, request, response);
		responseFormat = (String) 
		        getRequest().getAttributes().get("ResponseFormat");
		getVariants().add(new Variant(MediaType.APPLICATION_XML));
	}

	/* 
	 * Handle GET requests.
	 * 
	 * (non-Javadoc)
	 * @see org.wholebrainproject.wbc.server.resource.DataRepositoryResource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		
		// generate representation based on media type
		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
			File file = new File(this.getClass().getResource(
						"/database/ProcessDescriptions.xml").getPath());
			return new FileRepresentation(file, MediaType.APPLICATION_XML);
		}
		
		return null;
	}
	
}
