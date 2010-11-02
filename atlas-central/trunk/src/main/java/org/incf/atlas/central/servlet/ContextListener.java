package org.incf.atlas.central.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.incf.atlas.central.server.ServerApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ContextListener implements ServletContextListener {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ServletContext context;

	public void contextInitialized(ServletContextEvent event) {
		
		logger.debug("Entered contextInitialized");
		
		context = event.getServletContext();
		
		// cache xml responses in context attributes
		try {
			String xmlGetCapabilities = readAsString(
					this.getClass().getResourceAsStream("/CentralGetCapabilities.xml"));
			context.setAttribute("getcapabilities", xmlGetCapabilities);

			String xmlDescribeProcess = readAsString(
					this.getClass().getResourceAsStream("/CentralDescribeProcess.xml"));
			context.setAttribute("describeprocess", xmlDescribeProcess);

			String xmlListHubs = readAsString(
					this.getClass().getResourceAsStream("/ListHubs.xml"));
			context.setAttribute("listhubs", xmlListHubs);

			String xmlListProcesses = readAsString(
					this.getClass().getResourceAsStream("/ListProcesses.xml"));
			context.setAttribute("listprocesses", xmlListProcesses);

		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
		context = event.getServletContext();
		context.removeAttribute("getcapabilities");
	}
	
//	private static String readFileAsString(String filePath) 
//			throws java.io.IOException{
//	    byte[] buffer = new byte[(int) new File(filePath).length()];
//	    BufferedInputStream f = null;
//	    try {
//	        f = new BufferedInputStream(new FileInputStream(filePath));
//	        f.read(buffer);
//	    } finally {
//	        if (f != null) try { f.close(); } catch (IOException ignored) { }
//	    }
//	    return new String(buffer);
//	}	
	
    private static String readAsString(InputStream in)
    		throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        char[] buf = new char[1024];
        int numRead = 0;
        while((numRead = reader.read(buf)) != -1){
            fileData.append(buf, 0, numRead);
        }
        reader.close();
        return fileData.toString();
    }
	
}
