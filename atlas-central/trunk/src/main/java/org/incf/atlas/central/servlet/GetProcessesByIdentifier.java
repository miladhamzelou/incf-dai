package org.incf.atlas.central.servlet;

import javax.servlet.ServletContext;

import org.incf.atlas.central.util.DataInputs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetProcessesByIdentifier {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ServletContext context;
	
	private String fileName;

	public GetProcessesByIdentifier(ServletContext context, DataInputs dataInputs) {
		this.context = context;
		
		if (dataInputs == null) {
			// ERROR 
		}
		
		String processIdentifier = dataInputs.getValue("processidentifier");
		
		logger.debug("processIdentifier: {}", processIdentifier);
		
		fileName = (String) context.getAttribute(processIdentifier);
		
		logger.debug("fileName: {}", fileName);
	}
	
	public String getResponse() {
		return (String) context.getAttribute(fileName);
	}

}
