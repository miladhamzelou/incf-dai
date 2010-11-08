package org.incf.atlas.central.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.incf.atlas.central.util.DataInputs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetProcessesByIdentifier implements ExecuteProcessHandler {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ServletContext context;
	
	public GetProcessesByIdentifier(ServletContext context) {
		this.context = context;
	}
	
	public String getProcessResponse(DataInputs dataInputs, 
			HttpServletResponse response) {
		
		if (dataInputs == null) {
			// ERROR 
		}
		
		String processIdentifier = dataInputs.getValue("processidentifier");
		
		logger.debug("processIdentifier: {}", processIdentifier);
		
		String xmlResponse = (String) context.getAttribute(processIdentifier);
		
		logger.debug("xmlResponse: {}", xmlResponse);
		
		return xmlResponse;
	}

}
