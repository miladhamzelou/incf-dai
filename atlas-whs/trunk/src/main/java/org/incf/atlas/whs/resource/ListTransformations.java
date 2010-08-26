package org.incf.atlas.whs.resource;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.incf.atlas.whs.util.DataInputs;
import org.incf.atlas.whs.util.WHSConfigurator;
import org.incf.atlas.whs.util.WHSUtil;
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

public class ListTransformations extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(
			ListTransformations.class);
	
	String hostName = "";
	String portNumber = "";
	String servicePath = "";

	URI uri = null;
	String incfDeployHostname = "";
	String incfDeployPortnumber = "";


	WHSConfigurator config = WHSConfigurator.INSTANCE;

	public ListTransformations(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());

		try { 
			uri = new URI(request.getResourceRef().toString());
			incfDeployHostname = uri.getHost();
			incfDeployPortnumber = String.valueOf(uri.getPort());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/* 
	 * Handle GET requests.
	 * 
	 * (non-Javadoc)
	 * @see org.wholebrainproject.wbc.server.resource.DataRepositoryResource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {

		WHSServiceVO vo = new WHSServiceVO();

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

		// text return for debugging
/*		Set<String> dataInputKeys = dataInputs.getKeys();
		for (String key : dataInputKeys) {
			if (key.equalsIgnoreCase("inputSrsName")) {
				fromSRSCode = dataInputs.getValue(key);
				vo.setFromSRSCode(fromSRSCode);
				vo.setFromSRSCodeOne(fromSRSCode);
			} else if (key.equalsIgnoreCase("targetSrsName")) {
				toSRSCode = dataInputs.getValue(key);
				vo.setToSRSCode(toSRSCode);
				vo.setToSRSCodeOne(toSRSCode);
			} else if (key.equalsIgnoreCase("filter")) {
				filter = dataInputs.getValue(key);
				vo.setFilter(filter);
			}
		}
*/

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        String currentTime = dateFormat.format(date);
        vo.setCurrentTime(currentTime);

        vo.setUrlString(uri.toString());
        vo.setIncfDeployHostname(incfDeployHostname);
        vo.setIncfDeployPortNumber(incfDeployPortnumber);

/*		ObjectFactory of = new ObjectFactory();
		CoordinateTransformationChainResponse coordinateChain = of.createCoordinateTransformationChainResponse();
*/
        //Start - ListTransformation Method Call Identifier 
        vo.setFlag("ListTransformations");
        //End

		WHSUtil util = new WHSUtil(); 
		String responseString = util.getCoordinateTransformationChain(vo);

		//return document.xmlText(opt);
		return new StringRepresentation(responseString,MediaType.APPLICATION_XML);

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
