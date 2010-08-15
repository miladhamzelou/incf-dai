package org.incf.atlas.ucsd.resource;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import net.sf.saxon.xqj.SaxonXQDataSource;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//test:
//http://incf-dev-local.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&request=GetCapabilities

public class Capabilities extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	// cached file
	private static final String RESPONSE_FILE_NAME = "Capabilities.xml";
	
	// xquery file from which to build cached file
	private static final String RESPONSE_BASE_NAME = 
//		"/database/Capabilities.xq";
		"Capabilities.xq";
	
	public Capabilities(Context context, Request request, Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());
		
		logger.debug("user.dir: {}", System.getProperty("user.dir"));
		logger.debug("catalina.base: {}", System.getProperty("catalina.base"));
	}

	/* 
	 * Handle GET requests.
	 * 
	 * (non-Javadoc)
	 * @see org.wholebrainproject.wbc.server.resource.DataRepositoryResource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
	    
	    // if there are exceptions, send an excepton report
	    if (exceptionHandler != null) {
	    	logger.error("Exception Report returned to client: \n{}", 
	    			exceptionHandler.toString());
	        return getExceptionRepresentation();
	    }
	    
	    // look for cached file fisrt
	    File cachedResponse = new File(cacheDir, RESPONSE_FILE_NAME);
	    if (cachedResponse.exists()) {
	        return new FileRepresentation(cachedResponse, 
	        		MediaType.APPLICATION_XML);
	    }
	
        // set serialization properties
        Properties props = new Properties();
        props.setProperty("method", "xml");
        props.setProperty("indent", "yes");
        props.setProperty("omit-xml-declaration", "no");
        props.setProperty("{http://saxon.sf.net/}indent-spaces", "2");
        
        // make cache directory
        cacheDir.mkdir();
        
        logger.debug("cacheDir: {}", cacheDir.getAbsolutePath());
        
//        String prelude = "declare variable $doc := doc(\"file://" + tomcatDir
//			+ "/webapps/atlas-ucsd/WEB-INF/classes/database/ProcessDescriptions.xq\");";
        String prelude = "declare variable $doc := doc(\"file://" 
    		+ xmlDbDir.getAbsolutePath() + "/ProcessDescriptions.xq\");";
		try {

			// run query
			XQDataSource ds = new SaxonXQDataSource();
			
			logger.debug("cp1");
			
			XQConnection conn = ds.getConnection();
			
			logger.debug("cp2");
			
//			XQPreparedExpression exp = conn.prepareExpression(
//					this.getClass().getResourceAsStream(RESPONSE_BASE_NAME));
            XQPreparedExpression exp = conn.prepareExpression(prelude
                    + readFileAsString(new File(xmlDbDir, RESPONSE_BASE_NAME)));
			
			logger.debug("cp3");
			
			XQResultSequence result = exp.executeQuery();
			
	        // serialize to cache
			
			logger.debug("cp4");
			
			result.writeSequence(new FileWriter(cachedResponse), props);
		} catch (Exception e) {
			logger.error("Exception in query exection and caching: ", e);
			throw new ResourceException(e);
		}

		// TODO validate
        
		
		logger.debug("cp5");
		
		// return as file
        return new FileRepresentation(cachedResponse, 
        		MediaType.APPLICATION_XML);
	}
	
}
