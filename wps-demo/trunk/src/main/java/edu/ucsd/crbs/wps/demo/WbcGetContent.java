package edu.ucsd.crbs.wps.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.deegree.commons.utils.kvp.MissingParameterException;
import org.deegree.services.controller.exception.ControllerException;
import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.wps.Processlet;
import org.deegree.services.wps.ProcessletException;
import org.deegree.services.wps.ProcessletExecutionInfo;
import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.ProcessletOutputs;
import org.deegree.services.wps.input.LiteralInput;
import org.deegree.services.wps.output.ComplexOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WbcGetContent implements Processlet {

    private static final Logger LOG = LoggerFactory.getLogger(
    		WbcGetContent.class);
    
    // test
    // http://incf-dev-local.crbs.ucsd.edu/wps-demo?service=WPS&version=1.0.0&request=Execute&DataInputs=id=wbc7359387362308035140.tmp
    // http://localhost:8080/wps-demo?service=WPS&version=1.0.0&request=Execute&identifier=WbcGetContent&dataInputs=id=wbc7359387362308035140.tmp&rawDataOutput=content
    
    private static final String WBC_SERVER_URL = 
    	"http://data.wholebraincatalog.org/";
    private static final String WBC_CONTENT_BASE = "content/";

    @Override
    public void process(ProcessletInputs in, ProcessletOutputs out, 
            ProcessletExecutionInfo info) throws ProcessletException {
    	InputStream inStream = null;
    	OutputStream outStream = null;
    	try {

    		// collect input values
    		LiteralInput inId = (LiteralInput) in.getParameter("id");
    		if (inId == null) {
    			throw new MissingParameterException(
    					"Missing required parameter", "id");
    			
    		}

    		String contentId = inId.getValue();
    		
    		LOG.debug(String.format("DataInputs: id: %s", contentId));
    		
    		// http://data.wholebraincatalog.org/content/wbc7359387362308035140.tmp
    		
    		URL wbcContentUrl = new URL(WBC_SERVER_URL + WBC_CONTENT_BASE 
    				+ contentId);
    		inStream = wbcContentUrl.openStream();

    		ComplexOutput complexOutput = (ComplexOutput) out.getParameter(
    				"content");
    		
    		LOG.debug("Setting complex output (requested=" 
    				+ complexOutput.isRequested() + ")");
    		
    		outStream = complexOutput.getBinaryOutputStream();
    		
    		byte[] bytes = new byte[1024];
    		int bytesRead;
    		while ((bytesRead = inStream.read(bytes)) != -1) {
    			outStream.write(bytes, 0, bytesRead);
    		}
        } catch (MissingParameterException e) {
            LOG.error(e.getMessage(), e);
        	throw new ProcessletException(new OWSException(e));
        } catch (Throwable e) {
        	String message = "Unexpected exception occured";
        	LOG.error(message, e);
        	OWSException owsException = new OWSException(message, e, 
        			ControllerException.NO_APPLICABLE_CODE);
        	throw new ProcessletException(owsException);
        } finally {
        	close(inStream);
        	close(outStream);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }
    
    private void close(InputStream inStream) {
    	if (inStream != null) {
    		try {
    			inStream.close();
    		} catch (IOException logOnly) {
    			LOG.warn("Problem closing InputStream", logOnly);
    		}
    	}
    }

    private void close(OutputStream outStream) {
    	if (outStream != null) {
    		try {
    			outStream.close();
    		} catch (IOException logOnly) {
    			LOG.warn("Problem closing OutputStream", logOnly);
    		}
    	}
    }

}
