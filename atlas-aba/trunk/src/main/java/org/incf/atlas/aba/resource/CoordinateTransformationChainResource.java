package org.incf.atlas.aba.resource;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.incf.atlas.aba.util.ABAUtil;
import org.incf.atlas.aba.util.DataInputs;
import org.incf.atlas.generated.CoordinateTransformationChainResponse;
import org.incf.atlas.generated.ObjectFactory;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoordinateTransformationChainResource extends Resource {

	private final Logger logger = LoggerFactory.getLogger(
			CoordinateTransformationChainResource.class);
	
	private String dataInputString; 
	private DataInputs dataInputs;
	String hostName = "";
	String servicePath = "";
	String url = "";

	public CoordinateTransformationChainResource(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
		System.out.println("You are in GetCoordinateTransformationChain");
		dataInputString = (String) request.getAttributes().get("dataInputs"); 
		System.out.println("dataInputString " + dataInputString );
		
		dataInputs = new DataInputs(dataInputString);

		hostName = "http://132.239.131.188:8080/atlas";
		servicePath = "?Request=Execute&Identifier=TransformationChain";

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

		try { 

		String fromSRSCode = "";
		String toSRSCode = "";
		String filter = "";
		
		// text return for debugging
		ABAServiceVO vo = new ABAServiceVO();
		Set<String> dataInputKeys = dataInputs.getKeys();
		for (String key : dataInputKeys) {
			if (key.equalsIgnoreCase("inputSrsCode")) {
				fromSRSCode = dataInputs.getValue(key);
				vo.setFromSRSCode(fromSRSCode);
				vo.setFromSRSCodeOne(fromSRSCode);
			} else if (key.equalsIgnoreCase("targetSrsCode")) {
				toSRSCode = dataInputs.getValue(key);
				vo.setToSRSCode(toSRSCode);
				vo.setToSRSCodeOne(toSRSCode);
			} else if (key.equalsIgnoreCase("filter")) {
				filter = dataInputs.getValue(key);
				vo.setFilter(filter);
			}
		}

		System.out.println("fromSRSCode - " + fromSRSCode);
		System.out.println("toSRSCode - " + toSRSCode);
		System.out.println("filter - " + filter);

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        String currentTime = dateFormat.format(date);
        vo.setCurrentTime(currentTime);

        url = hostName + servicePath + "&DataInputs=" + dataInputString;
        vo.setUrlString(url);
        
		ObjectFactory of = new ObjectFactory();
		CoordinateTransformationChainResponse coordinateChain = of.createCoordinateTransformationChainResponse();

		ABAUtil util = new ABAUtil();
		coordinateChain = util.getCoordinateTransformationChain(vo, coordinateChain);
		//generate representation based on media type
		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
			return new JaxbRepresentation<CoordinateTransformationChainResponse>(coordinateChain);
		}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

}
