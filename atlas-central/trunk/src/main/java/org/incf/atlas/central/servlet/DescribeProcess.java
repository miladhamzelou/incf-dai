package org.incf.atlas.central.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.incf.atlas.central.util.DataInputs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DescribeProcess implements ExecuteProcessHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private ServletContext context;
	
	public DescribeProcess(ServletContext context) {
		this.context = context;
	}
	
	@Override
	public String getProcessResponse(DataInputs dataInputs, 
			HttpServletResponse response) {
		
		String xmlResponse = (String) context.getAttribute("describeprocess");
		
		logger.debug("xmlResponse: {}", xmlResponse);
		
		return xmlResponse;
	}

}
