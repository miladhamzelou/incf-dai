package org.incf.atlas.whs.resource;

import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This resource is used for functions not supported by this hub.
 * 
 * @author dave
 */
public class NotSupported extends BaseResouce {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    String message;

    public NotSupported(Context context, Request request, 
            Response response) {
        super(context, request, response);
        
        logger.debug("Instantiated {}.", getClass());
        
        message = "This process is not supported at this hub.";
        
        logger.info(message);
    }

    @Override
    public Representation represent(Variant variant) throws ResourceException {
        
        // prepare an ExceptionReport
        ExceptionHandler exHandler = getExceptionHandler();
        exHandler.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
                new String[] { message });
        
        // generate xml
        return exHandler.getDomExceptionReport();
    }

}
