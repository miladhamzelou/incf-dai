package org.incf.central.atlas.process;

import org.deegree.commons.utils.kvp.MissingParameterException;
import org.deegree.services.controller.exception.ControllerException;
import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.wps.Processlet;
import org.deegree.services.wps.ProcessletException;
import org.deegree.services.wps.ProcessletExecutionInfo;
import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.ProcessletOutputs;
import org.deegree.services.wps.input.LiteralInput;
import org.deegree.services.wps.output.LiteralOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Addition implements Processlet {

    private static final Logger LOG = LoggerFactory.getLogger(
            Addition.class);

    @Override
    public void process(ProcessletInputs in, ProcessletOutputs out, 
            ProcessletExecutionInfo info) throws ProcessletException {
    	try {

    		// collect input values
    		LiteralInput inA = (LiteralInput) in.getParameter("a");
    		LiteralInput inB = (LiteralInput) in.getParameter("b");
    		if (inA == null) {
    			throw new MissingParameterException(
    					"Missing required parameter", "a");
    		}
    		if (inB == null) {
    			throw new MissingParameterException(
    					"Missing required parameter", "b");
    		}

    		double a = Double.parseDouble(inA.getValue());
    		double b = Double.parseDouble(inB.getValue());

    		LOG.debug(String.format("DataInputs: a: %f, b: %f", a, b));
    		
    		// do actual work of process
    		double sum = a + b;
    		
    		LOG.debug(String.format("Sum: %f", sum));
    		
    		LiteralOutput output = (LiteralOutput) out.getParameter("sum");
    		output.setValue(String.valueOf(sum));
        } catch (MissingParameterException e) {
            LOG.error(e.getMessage(), e);
        	throw new ProcessletException(new OWSException(e));
        } catch (Throwable e) {
        	String message = "Unexpected exception occured";
        	LOG.error(message, e);
        	OWSException owsException = new OWSException(message, e, 
        			ControllerException.NO_APPLICABLE_CODE);
        	throw new ProcessletException(owsException);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }

}
