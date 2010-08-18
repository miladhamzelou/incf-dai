package org.incf.atlas.aba.resource;

import java.io.File;

import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
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

/* Email fr Ilya Aug 13
Dave (and Asif),
 
We’ve discussed relying on GetGenesByPOI as the first filtering step in the 
Get2DImagesByPOI implementation. For ABA, GetGenesByPOI means wrapping an AGEA 
request such as  
 
http://mouse.brain-map.org/agea/GeneFinder.xml?seedPoint=6600,4000,5600
 
and generating WaxML. In the previous version, we just displayed a URL of the 
page, as in
 
http://whs0.pdc.kth.se:8080/incf-services/service/ABAServiceController?request=GetGenesByPOI&SRSCode=ABAVoxel&x=263&y=159&z=227&output=txt
 
 
In the current version, we cannot populate the entire set of elements in the 
schema, but at least <Level> in our schema would be the same as <energy> in 
AGEA output. I am not sure how to map other things, they look specific to AGEA 
(see http://mouse.brain-map.org/pdf/AGEA.pdf ). I don’t think “rank” 
corresponds to anything in our schema (“rank” is a precomputed ratio of 
expression energy in a set of voxels that are in the top 1/3 of correlation 
range, to expression energy in a larger region, i.e. all voxels above a 
threshold).
 
Not sure how you guys want to divide up the work, but I thought that since Dave 
is already working on Get2DImagesByPOI, he might do the GetGenesByPOI for the 
ABA hub as well? Let me know,
 
Thanks,
-          ilya 
*/
/*
 GET: http://[host:port]/[hub]$service=WPS&version=[version]&request=Execute
 &Identifier=GetGenesByPOI&DataInputs=srsName=[srsName];x=[x];y=[y];z=[z]
 
 
 */

public class GenesByPOI extends BaseResouce {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	// cached file
	private static final String RESPONSE_FILE_NAME = "Capabilities.xml";
	
	public GenesByPOI(Context context, Request request, Response response) {
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
		
	    /*
	     validate srsName - s/b AGEA?
	     validate x y z
	     validate filter - what to do with it?
	     prepare http GET to ABA
	     get ABA response
	     how many <agea-rank>'s to return? criteria for which? top 1
	     aba provides
	    	ageapositionid
	      	count
	      	energy
	      	exampleimageid
	      	genesymbol
	      	imageseriesid
	      	rank
	     WaxML GenesResponse.xsd returns
	     	Gene
		     	Symbol
		     		codeSpace = "ABA"
		     		gml:id = symbol
		     		symbol = symbol
		     	MarkerAccessionId -- make optional
		     	    separator
		     	    Prefix
		     	    Identifier
		     	    FullIdentifier
		     	Name = symbol
		     	Organism = "mouse"
	     	ExpressionLevel
	     		GeneSymbol xlin:href = gml:id (symbol)
	     		Stage
	     		Level = aba energy
	     		ResourceUri
	    how to map aba response to Atlas response?
	    do the mapping using XMLBeans objects
	    return Atlas GenesResponse 		
	     */

	    
        // prepare an ExceptionReport
	    String message = "File " + RESPONSE_FILE_NAME + " not found.";
        ExceptionHandler exHandler = getExceptionHandler();
        exHandler.addExceptionToReport(ExceptionCode.NOT_APPLICABLE_CODE, null, 
                new String[] { message });
        logger.error(message);
        
        // TODO
        return null;
	}
	
}
