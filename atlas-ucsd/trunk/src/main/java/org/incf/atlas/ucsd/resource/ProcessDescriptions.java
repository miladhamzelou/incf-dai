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
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessDescriptions extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
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
            return getExceptionRepresentation();
        }
        
        // set serialization properties
        Properties props = new Properties();
        props.setProperty("method", "xml");
        props.setProperty("indent", "yes");
        props.setProperty("omit-xml-declaration", "no");
        props.setProperty("{http://saxon.sf.net/}indent-spaces", "2");
        
        String prelude = "declare variable $doc := doc(\"file:///usr/local/tomcat/webapps/atlas-aba/WEB-INF/classes/database/ProcessInputs.xml\");";
        File tempDir = new File("/usr/local/tomcat/webapps/atlas-aba/WEB-INF/temp");
        tempDir.mkdir();
		File tempFile = new File(tempDir, "ProcessDescriptions.xml");
		try {

			// run query
			XQDataSource ds = new SaxonXQDataSource();
			XQConnection conn = ds.getConnection();
	        XQPreparedExpression exp = conn.prepareExpression(prelude
	                + readFileAsString("/usr/local/tomcat/webapps/atlas-aba/WEB-INF/classes/database/ProcessDescriptions.xq"));
			XQResultSequence result = exp.executeQuery();
			
	        // serialize to temporary file (TODO could be cached)
			result.writeSequence(new FileWriter(tempFile), props);
		} catch (Exception e) {
			throw new ResourceException(e);
		}

		// TODO validate
        
		// return as file
        return new FileRepresentation(tempFile, MediaType.APPLICATION_XML);
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
