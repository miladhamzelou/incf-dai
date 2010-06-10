package org.incf.atlas.aba.resource;

import java.io.File;
import java.net.URISyntaxException;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FaviconResource extends Resource {
	
	private final Logger logger = LoggerFactory.getLogger(
			FaviconResource.class);

	// resource object
	private File favicon;
	
	public FaviconResource(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
		// get resource
		try {
			favicon = new File(this.getClass().getResource("/favicon.ico")
					.toURI());
		} catch (URISyntaxException e) {
			logger.error("Error getting favicon.", e);
		}
		
		// if found, set media type
		// by default: available is true; modifiable is false
		if (favicon != null) {
			getVariants().add(new Variant(MediaType.IMAGE_ICON));
		} else {
			setAvailable(false);
		}
	}

	/**
	 * Handle GET requests. This method responds to root ("/") GET requests,
	 * which are typically requests for a favicon.
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		
		// generate representation based on media type
		if (variant.getMediaType().equals(MediaType.IMAGE_ICON)) {
			return new FileRepresentation(favicon, MediaType.IMAGE_ICON);
		}
		
		return null;
	}

}
