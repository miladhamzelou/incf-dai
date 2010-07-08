package org.incf.atlas.aba.resource;

import org.incf.atlas.aba.util.Constants;
import org.incf.atlas.aba.util.ExceptionCode;
import org.incf.atlas.aba.util.ExceptionHandler;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BaseResouce extends Resource {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private String service;
    private String version;

    public BaseResouce(Context context, Request request, Response response) {
        super(context, request, response);
        
        logger.debug("Instantiated {}.", getClass());
        
        service = (String) request.getAttributes().get("service");
        version = (String) request.getAttributes().get("version");
        
        Constants constants = Constants.getInstance();
        if (!service.equals(constants.getDefaultService())) {
            
            // prepare an ExceptionReport
            ExceptionHandler eh = new ExceptionHandler(
                    Constants.getInstance().getDefaultVersion());
            eh.handleException(ExceptionCode.NOT_APPLICABLE_CODE, null, 
                    new String[] { "This function has not yet been implemented." });
            
        }
        
        // TODO but not for GetCapabilities
        if (!version.equals(constants.getDefaultVersion())) {
            
        }
        
        getVariants().add(new Variant(MediaType.APPLICATION_XML));
    }
    
    // TODO all except GetCapabilities
    public void checkVersion() {
        
    }

}
