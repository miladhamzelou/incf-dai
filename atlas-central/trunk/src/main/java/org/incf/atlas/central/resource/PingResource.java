package org.incf.atlas.central.resource;

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

public class PingResource extends Resource {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	// resource object
	private String pingResponse;

	public PingResource(Context context, Request request, Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());
		
		String pingType = (String) request.getAttributes().get("pingType"); 
//		if (pingType.equals("server")) {
			StringBuilder buf = new StringBuilder();
			buf.append("Server responds to ping type: ").append(pingType);
			pingResponse = buf.toString();
//		}

		// if found, set media type
		// by default: available is true; modifiable is false
		if (pingResponse != null) {
			getVariants().add(new Variant(MediaType.TEXT_PLAIN));
		} else {
			setAvailable(false);
		}
		
		logger.debug("pingType: {}", pingType);
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
		if (variant.getMediaType().equals(MediaType.TEXT_PLAIN)) {
			return new StringRepresentation(pingResponse);
		}
		
		return null;
	}

}
