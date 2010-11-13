package org.incf.atlas.central.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.incf.atlas.central.util.DataInputs;
import org.incf.atlas.central.util.QueryString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends HttpServlet {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
				
		ServletContext context = getServletContext();

		String response = null;
		
		QueryString queryString = new QueryString(
				req.getQueryString());
		
		// TODO validate "service"
		
		// TODO validate "responseform
		
		// handle requests (GetCapabilities, DescribeProcess, or Execute)
		String qsRequest = queryString.getValue("request");
		if (qsRequest == null) {
			// TODO error - request is required
		}
		
		ExecuteProcessHandler handler = null;
		if (qsRequest.equals("getcapabilities")) {
			handler = new GetCapabilities(context);
			response = handler.getProcessResponse(context, req, res, null);
		}
		
		// TODO validate "version"
		
		if (qsRequest.equals("describeprocess")) {
			handler = new DescribeProcess(context);
			response = handler.getProcessResponse(context, req, res, null);
		} else if (qsRequest.equals("execute")) {

			// get dataInputs
			DataInputs dataInputs = queryString.getDataInputs();
			
			// handle identifiers for request=Execute
			String qsIdentifier = queryString.getValue("identifier");
			if (qsIdentifier == null) {
				// TODO error - identifier is required when request = Execute
			}
			if (qsIdentifier.equals("getprocessesbyidentifier")) {
				handler = new GetProcessesByIdentifier(context);
				response = handler.getProcessResponse(context, req, res, dataInputs);
			} else if (qsIdentifier.equals("listhubs")) {
				handler = new ListHubs(context);
				response = handler.getProcessResponse(context, req, res, dataInputs);
			} else if (qsIdentifier.equals("listprocesses")) {
				handler = new ListProcesses(context);
				response = handler.getProcessResponse(context, req, res, dataInputs);
			} else if (qsIdentifier.equals("listsrss")) {
				handler = new ListSRS(context);
				response = handler.getProcessResponse(context, req, res, dataInputs);
			} else if (qsIdentifier.equals("describesrs")) {
				handler = new DescribeSRS(context);
				response = handler.getProcessResponse(context, req, res, dataInputs);
			} else if (qsIdentifier.equals("gettransformationchain")) {
				handler = new CoordinateTransformationChain(context);
				response = handler.getProcessResponse(context, req, res, dataInputs);
			} else if (qsIdentifier.equals("listtransformations")) {
				handler = new ListTransformations(context);
				response = handler.getProcessResponse(context, req, res, dataInputs);
			} else if (qsIdentifier.equals("get2dimagesbypoi")) {
				handler = new Get2DImagesByPOI(context);
				response = handler.getProcessResponse(context, req, res, dataInputs);
			} else if (qsIdentifier.equals("getstructurenamesbypoi")) { 
				handler = new StructureNamesByPOI(context);
				response = handler.getProcessResponse(context, req, res, dataInputs);
			} else {
				// TODO error - identifier not recognized
			}
			
		} else {
			// TODO error - request no GD, DP, or Execute
		}
		
		res.setContentType("text/xml");
		PrintWriter out = res.getWriter();
		out.println(response);
		out.close();
	}

	public String getServletInfo() {
		return "A servlet that requests to atlas central.";
	}
	
}