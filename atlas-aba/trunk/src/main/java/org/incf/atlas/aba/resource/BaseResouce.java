package org.incf.atlas.aba.resource;

import javax.xml.bind.JAXBException;

import org.incf.atlas.aba.util.Constants;
import org.incf.atlas.aba.util.ExceptionCode;
import org.incf.atlas.aba.util.ExceptionHandler;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BaseResouce extends Resource {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private Constants constants;
    private String service;
    private String version;
    protected String dataInputsString;
    private String responseForm;
    
    protected ExceptionHandler exceptionHandler;
    
    public BaseResouce(Context context, Request request, Response response) {
        super(context, request, response);
        constants = Constants.getInstance();
        
        service = (String) request.getAttributes().get("service");
        version = (String) request.getAttributes().get("version");
        dataInputsString = (String) request.getAttributes().get("dataInputs");
        responseForm = (String) request.getAttributes().get("responseForm");
        
        // every request must include service key/value
        checkService();
        
        // every request may include responseForm key/value
        checkResponseForm();
        
        // every request's response will be xml
        getVariants().add(new Variant(MediaType.APPLICATION_XML));
    }
    
    protected ExceptionHandler getExceptionHandler() {
        if (exceptionHandler == null) {
            exceptionHandler = new ExceptionHandler();
        }
        return exceptionHandler;
    }
    
    protected Representation getExceptionRepresentation() 
            throws ResourceException {
        
        // generate xml
        String exceptionReportXml = null;
        try {
            exceptionReportXml = exceptionHandler.getXMLExceptionReport();
        } catch (JAXBException e) {
            throw new ResourceException(e);
        }
        
        // return it
        getResponse().setStatus(Status.CLIENT_ERROR_FORBIDDEN);
        return new StringRepresentation(exceptionReportXml);
    }
    
    
    /**
     * Check the value of the service value. This is applicable to all
     * requests.
     */
    protected void checkService() {
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

    /**
     * Check the value of the version value. This is applicable to all
     * requests except GetCapabilities.
     */
    protected void checkVersion() {
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

    /**
     * Check the value of the ResponseForm value. This is an optional parameter.
     */
    protected void checkResponseForm() {
        
        // responseForm is optional
        if (responseForm == null) {
            responseForm = constants.getDefaultResponseForm();
            return;
        }
        
        // if responseForm is there, check it
        if (!responseForm.equals(constants.getDefaultResponseForm())) {
            
            // prepare an ExceptionReport
            ExceptionHandler eh = getExceptionHandler();
            eh.addExceptionToReport(ExceptionCode.INVALID_PARAMETER_VALUE, null, 
                    new String[] { 
                    String.format("Response Form %s is not supported.", 
                            responseForm),
                    String.format("The only supported Response Form is %s.", 
                            constants.getDefaultResponseForm()),
                    });
            
        }
    }
    
    protected void validateSrsName(String srsName) {
        if (!constants.getSrsNames().contains(srsName)) {
            
            // prepare an ExceptionReport
            ExceptionHandler eh = getExceptionHandler();
            StringBuilder validSrsNames = new StringBuilder();
            int i = 0;
            for (String sName : constants.getSrsNames()) {
                if (i > 0) {
                    validSrsNames.append(", ");
                }
                validSrsNames.append(sName);
                i++;
            }
            eh.addExceptionToReport(ExceptionCode.INVALID_PARAMETER_VALUE, null, 
                    new String[] { 
                    String.format("SRS name '%s' is not recognized.", 
                            responseForm),
                    String.format("The supported SRS names are '%s'.", 
                            validSrsNames.toString()),
                    });
        }
    }

    protected Double validateCoordinate(String name, String value)  {
        if (value == null || value.length() == 0) {
            
            // prepare an ExceptionReport
            ExceptionHandler eh = getExceptionHandler();
            eh.addExceptionToReport(ExceptionCode.INVALID_PARAMETER_VALUE, null, 
                    new String[] { 
                    String.format("Value of %s coordinate is missing.", name) 
                    });
            
            // no reason to continue validation
            return null;
        }
        
        Double d = null;
        try {
            d = new Double(value);
        } catch (NumberFormatException e) {
            
            // prepare an ExceptionReport
            ExceptionHandler eh = getExceptionHandler();
            eh.addExceptionToReport(ExceptionCode.INVALID_PARAMETER_VALUE, null, 
                    new String[] { 
                    String.format("Value '%s' of %s coordinate is non-numeric.", 
                            value, name) 
                    });
        }
        return d;
    }

}

