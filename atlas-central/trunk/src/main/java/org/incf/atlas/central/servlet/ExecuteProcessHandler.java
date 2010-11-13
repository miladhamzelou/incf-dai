package org.incf.atlas.central.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.incf.atlas.central.util.DataInputs;

public interface ExecuteProcessHandler {
	
	public String getProcessResponse(ServletContext context, HttpServletRequest request, 
							         HttpServletResponse response, DataInputs dataInputs);

}
