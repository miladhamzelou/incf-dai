package org.incf.atlas.aba.resource;

import java.io.StringWriter;
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
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DescribeProcess  extends Resource {
	
	private static final Logger logger = LoggerFactory.getLogger(GetCapabilities.class);
	
	/*
	 * /incf-services/service/ABAServiceController?request=GetCapabilities&output=xml
	 */
	
	private String responseFormat;

	public DescribeProcess(Context context, Request request, 
			Response response) {
		super(context, request, response);
		responseFormat = (String) 
		        getRequest().getAttributes().get("ResponseFormat");
		getVariants().add(new Variant(MediaType.APPLICATION_XML));
	}

	/* 
	 * Handle GET requests.
	 * 
	 * (non-Javadoc)
	 * @see org.wholebrainproject.wbc.server.resource.DataRepositoryResource#represent(org.restlet.resource.Variant)
	 */
//	@Override
//	public Representation represent(Variant variant) throws ResourceException {
//		
//		// generate representation based on media type
//		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
//			File file = new File(this.getClass().getResource(
//						"/database/ProcessDescriptions.xml").getPath());
//			return new FileRepresentation(file, MediaType.APPLICATION_XML);
//		}
//		
//		return null;
//	}
	
	public Representation represent(Variant variant) throws ResourceException {
		
		// generate representation based on media type
		if (variant.getMediaType().equals(MediaType.APPLICATION_XML)) {
	        final Properties props = new Properties();
	        props.setProperty("method", "xml");
	        props.setProperty("indent", "yes");
	        props.setProperty("omit-xml-declaration", "no");
	        props.setProperty("{http://saxon.sf.net/}indent-spaces", "2");
	        
	        XQDataSource ds = new SaxonXQDataSource();
	        try {
				XQConnection conn = ds.getConnection();
				XQPreparedExpression exp = conn.prepareExpression(
						this.getClass().getResourceAsStream(
								"/database/ProcessDescriptions.xq"));
				XQResultSequence result = exp.executeQuery();
				StringWriter out = new StringWriter();
				result.writeSequence(out, props);
		        return new StringRepresentation(out.toString(), 
		        		MediaType.APPLICATION_XML);
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (XQException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (Exception e) {
				throw new ResourceException(e);
			}

//	        result.wr
	        
		}
		
		return null;
	}
	
}
