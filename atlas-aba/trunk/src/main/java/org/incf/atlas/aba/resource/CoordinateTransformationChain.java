package org.incf.atlas.aba.resource;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.incf.atlas.aba.util.ABAConfigurator;
import org.incf.atlas.aba.util.ABAUtil;
import org.incf.atlas.aba.util.DataInputs;
import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoordinateTransformationChain extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(
			CoordinateTransformationChain.class);

	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	String url = "";
	ABAConfigurator config = ABAConfigurator.INSTANCE;

	public CoordinateTransformationChain(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());

/*		System.out.println("You are in GetCoordinateTransformationChain");
		dataInputString = (String) request.getAttributes().get("dataInputs"); 
		System.out.println("dataInputString " + dataInputString );
		
		dataInputs = new DataInputs(dataInputString);

		getVariants().add(new Variant(MediaType.APPLICATION_XML));
*/	
		}

	/* 
	 * Handle GET requests.
	 * 
	 * (non-Javadoc)
	 * @see org.wholebrainproject.wbc.server.resource.DataRepositoryResource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {

		ABAServiceVO vo = new ABAServiceVO();

		try { 

		    // make sure we have something in dataInputs
		    if (dataInputsString == null || dataInputsString.length() == 0) {
		        ExceptionHandler eh = getExceptionHandler();
		        eh.addExceptionToReport(ExceptionCode.MISSING_PARAMETER_VALUE, null, 
		                new String[] { "All DataInputs were missing." });
		        
		        // there is no point in going further, so return
		        return getExceptionRepresentation();
		    }
			
		    // parse dataInputs string
	        DataInputs dataInputs = new DataInputs(dataInputsString);

	        vo.setFromSRSCodeOne(dataInputs.getValue("inputSrsName"));
	        vo.setFromSRSCode(dataInputs.getValue("inputSrsName"));
	        vo.setToSRSCodeOne(dataInputs.getValue("outputSrsName"));
	        vo.setToSRSCode(dataInputs.getValue("outputSrsName"));
	        vo.setFilter(dataInputs.getValue("filter"));

	        System.out.println("From SRS Code: " + vo.getFromSRSCodeOne());
	        System.out.println("To SRS Code: " + vo.getToSRSCodeOne());
	        System.out.println("Filter: " + vo.getFilter());

	        // validate data inputs
	        validateSrsName(vo.getFromSRSCodeOne());
	        validateSrsName(vo.getToSRSCodeOne());

	        // if any validation exceptions, no reason to continue
	        if (exceptionHandler != null) {
	            return getExceptionRepresentation();
	        }

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        String currentTime = dateFormat.format(date);
        vo.setCurrentTime(currentTime);

		hostName = config.getValue("incf.deploy.host.name");
		System.out.println("****HOSTNAME**** - " + hostName);
		String portNumber = ":8080";

		servicePath = "/atlas-aba?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain";
		//servicePath = "/atlas-aba?Request=Execute&Identifier=GetTransformationChain";

        url = "http://" + hostName + portNumber + servicePath + "&DataInputs=" + dataInputsString;
        vo.setUrlString(url);

/*		ObjectFactory of = new ObjectFactory();
		CoordinateTransformationChainResponse coordinateChain = of.createCoordinateTransformationChainResponse();
*/
		ABAUtil util = new ABAUtil(); 
		String responseString = util.getCoordinateTransformationChain(vo);

		//return document.xmlText(opt);
		return new StringRepresentation(responseString, MediaType.APPLICATION_XML);

		//generate representation based on media type
/*		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
			return new JaxbRepresentation<CoordinateTransformationChainResponse>(coordinateChain);
		}
*/
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

}
