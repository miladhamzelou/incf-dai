package org.incf.atlas.aba.resource;

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
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Capabilities extends Resource {

	private static final Logger logger = LoggerFactory.getLogger(GetCapabilities.class);
	
	public Capabilities(Context context, Request request, Response response) {
		super(context, request, response);
		
		
		logger.info("* * * here");
		logger.debug("* * * here");
		System.out.println("* * * * * here");
		
		
		getVariants().add(new Variant(MediaType.APPLICATION_XML));
	}

	/* 
	 * Handle GET requests.
	 * 
	 * (non-Javadoc)
	 * @see org.wholebrainproject.wbc.server.resource.DataRepositoryResource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		
        // set serialization properties
        Properties props = new Properties();
        props.setProperty("method", "xml");
        props.setProperty("indent", "yes");
        props.setProperty("omit-xml-declaration", "no");
        props.setProperty("{http://saxon.sf.net/}indent-spaces", "2");
        
		// run query
        // serialize to temporary file (TODO could be cached)
		File tempFile;
		try {
			XQDataSource ds = new SaxonXQDataSource();
			XQConnection conn = ds.getConnection();
			XQPreparedExpression exp = conn.prepareExpression(
					this.getClass().getResourceAsStream("/database/Capabilities.xq"));
			XQResultSequence result = exp.executeQuery();
			
			tempFile = new File("Capabilities.xml");
			result.writeSequence(new FileWriter(tempFile), props);
		} catch (Exception e) {
			throw new ResourceException(e);
		}

		// TODO validate
        
		// return as file
        return new FileRepresentation(tempFile, MediaType.APPLICATION_XML);
	}
	
}
