package org.incf.atlas.central.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.incf.atlas.central.util.DataInputs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetCapabilities implements ExecuteProcessHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private ServletContext context;
	
	public GetCapabilities(ServletContext context) {
		this.context = context;
	}
	
	public String getProcessResponse(DataInputs dataInputs, 
			HttpServletResponse response) {
		
		String xmlResponse = (String) context.getAttribute("getcapabilities");
		
		logger.debug("xmlResponse: {}", xmlResponse);
		
		return xmlResponse;
	}

}
