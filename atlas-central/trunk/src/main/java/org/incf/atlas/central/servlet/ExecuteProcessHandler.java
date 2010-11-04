package org.incf.atlas.central.servlet;

import javax.servlet.http.HttpServletResponse;

import org.incf.atlas.central.util.DataInputs;

public interface ExecuteProcessHandler {
	
	public String getProcessResponse(DataInputs dataInputs, 
			HttpServletResponse response);

}
