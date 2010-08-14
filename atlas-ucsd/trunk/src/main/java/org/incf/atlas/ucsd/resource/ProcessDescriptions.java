package org.incf.atlas.ucsd.resource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
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

// test:
// http://incf-dev-local.crbs.ucsd.edu:8080/atlas-ucsd?service=WPS&version=1.0.0&request=DescribeProcess

public class ProcessDescriptions extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    // cache directory relative to tomcat/webapps/
    private static final String CACHE_DIR_NAME = 
        "/usr/local/tomcat/webapps/"
        + "atlas-ucsd/WEB-INF/cache";
    
    // cached file
    private static final String RESPONSE_FILE_NAME = "ProcessDescriptions.xml";
    
    // xquery file from which to build cached file
    private static final String RESPONSE_BASE_NAME = 
        "/usr/local/tomcat/webapps/atlas-ucsd/WEB-INF/classes/database/ProcessDescriptions.xq";
    
	public ProcessDescriptions(Context context, Request request,
			Response response) {
		super(context, request, response);

        logger.debug("Instantiated {}.", getClass());
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
        cacheDir.mkdir();
        
        logger.debug("cacheDir: {}", cacheDir.getAbsolutePath());
        
        String prelude = "declare variable $doc := doc(\"file:///usr/local/tomcat/webapps/atlas-ucsd/WEB-INF/classes/database/ProcessInputs.xml\");";
        try {

            // run query
            XQDataSource ds = new SaxonXQDataSource();
            XQConnection conn = ds.getConnection();
            XQPreparedExpression exp = conn.prepareExpression(prelude
                    + readFileAsString(RESPONSE_BASE_NAME));
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
	
	private String readFileAsString(String filePath) throws IOException{
	    byte[] buffer = new byte[(int) new File(filePath).length()];
	    BufferedInputStream f = new BufferedInputStream(
	    		new FileInputStream(filePath));
	    f.read(buffer);
	    f.close();
	    return new String(buffer);
	}
	
}
