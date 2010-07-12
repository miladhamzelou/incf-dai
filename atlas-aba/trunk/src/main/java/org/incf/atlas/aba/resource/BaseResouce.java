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
    
    private Constants constants;
    private String service;
    private String version;
    private ExceptionHandler exceptionHandler;

    public BaseResouce(Context context, Request request, Response response) {
        super(context, request, response);
        constants = Constants.getInstance();
        
        service = (String) request.getAttributes().get("service");
        version = (String) request.getAttributes().get("version");
        
        checkService();
        if (this instanceOf Capabilities) {
            checkVersion();
        }
        
        getVariants().add(new Variant(MediaType.APPLICATION_XML));
    }
    
    protected ExceptionHandler getExceptionHandler() {
        if (exceptionHandler == null) {
            exceptionHandler = new ExceptionHandler();
        }
        return exceptionHandler;
    }
    
    private void checkService() {
        if (!service.equals(constants.getDefaultService())) {
            
            // prepare an ExceptionReport
            ExceptionHandler eh = getExceptionHandler();
            eh.addExceptionToReport(ExceptionCode.INVALID_PARAMETER_VALUE, null, 
                    new String[] { 
                    String.format("Service %s is not supported.", service),
                    String.format("The supported service is %s.", 
                                    constants.getDefaultService()),
                    });
            
        }
    }

    // TODO all except GetCapabilities
    private void checkVersion() {
        if (!version.equals(constants.getDefaultVersion())) {
            
            // prepare an ExceptionReport
            ExceptionHandler eh = getExceptionHandler();
            eh.addExceptionToReport(ExceptionCode.INVALID_PARAMETER_VALUE, null, 
                    new String[] { 
                    String.format("Version %s is not supported.", version),
                    String.format("The supported version is %s.", 
                                    constants.getDefaultVersion()),
                    });
            
        }
    }

}
