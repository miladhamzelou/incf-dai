package org.incf.atlas.ucsd.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
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

public class Capabilities extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String CACHE_DIR_NAME = 
//		"/usr/local/tomcat/webapps/atlas-ucsd/WEB-INF/cache";
	"atlas-ucsd/WEB-INF/cache";
	private static final String RESPONSE_FILE_NAME = "Capabilities.xml";
	private static final String RESPONSE_BASE_NAME = 
		"/database/Capabilities.xq";
	
	public Capabilities(Context context, Request request, Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());
		logger.debug("* * * path? {}", request.getResourceRef().getPath());
		
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
	    File cachedResponse = new File(CACHE_DIR_NAME, RESPONSE_FILE_NAME);
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
        File cacheDir = new File(CACHE_DIR_NAME);
        
        logger.debug("cacheDir: {}", cacheDir.getAbsolutePath());
        
        cacheDir.mkdir();
		try {

			// run query
			XQDataSource ds = new SaxonXQDataSource();
			XQConnection conn = ds.getConnection();
			XQPreparedExpression exp = conn.prepareExpression(
					this.getClass().getResourceAsStream(RESPONSE_BASE_NAME));
			XQResultSequence result = exp.executeQuery();
			
	        // serialize to cache
			result.writeSequence(new FileWriter(cachedResponse), props);
		} catch (Exception e) {
			logger.error("Exception in query exection and caching: ", e);
			throw new ResourceException(e);
		}

		// TODO validate
        
		// return as file
        return new FileRepresentation(cachedResponse, 
        		MediaType.APPLICATION_XML);
	}
	
}
