package org.incf.atlas.aba.server;

import org.incf.atlas.aba.resource.Capabilities;
import org.incf.atlas.aba.resource.CoordinateTransformationChain;
import org.incf.atlas.aba.resource.CorrelationMapByPOI;
import org.incf.atlas.aba.resource.FaviconResource;
import org.incf.atlas.aba.resource.Images2DByPOI;
import org.incf.atlas.aba.resource.NotYetImplemented;
import org.incf.atlas.aba.resource.PingResource;
import org.incf.atlas.aba.resource.ProcessDescriptions;
import org.incf.atlas.aba.resource.StructureNamesByPOI;
import org.incf.atlas.aba.resource.TransformPOI;
import org.incf.atlas.aba.resource.UnrecognizedUri;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class ServerApplication extends Application {

	private static final Logger logger = LoggerFactory.getLogger(
			ServerApplication.class);
	
	/*
	There are 4 patterns and all have service, version, request, and 
	ResponseForm (note upper case on the latter). For every Execute we will 
	also have Identifier and DataInputs (both leading upper) except for 
	ListSRSs which has no DataInputs.

	1. http://<host:port>/atlas-aba?service={*service}&version={*version}
		&request=GetCapabilities&ResponseForm={*responseForm}

	2. http://<host:port>/atlas-aba?service={*service}&version={*version}
		&request=DescribeProcess&ResponseForm={*responseForm}

	3. http://<host:port>/atlas-aba?service={*service}&version={*version}
		&request=Execute&Identifier={process}&DataInputs={dataInputs}
		&ResponseForm={*responseForm}

	4. http://<host:port>/atlas-aba?service={*service}&version={*version}
		&request=Execute&Identifier=ListSRSs&ResponseForm={*responseForm}

	 * ignore for now
	
	service = WPS
	version = version
	request = { GetCapabilities | DescribeProcess | Execute }
	Identifier = { ..... | .....  our standard set }
	DataInputs = { varies by identifier }
	ResponseForm = [ response format }
	 */

	// elements of Atlas GET query string
	// note query key case!
	//	service, version, request -- leading lower
	//  Identifier, DataInputs, ResponseForm -- leading upper
	private static final String SERVICE = "?service={service}";
	private static final String VERSION = "&version={version}";
	private static final String SERV_VER = SERVICE + VERSION;
	private static final String REQ_KEY = "&request=";
	private static final String EXEC_ID_KEY = REQ_KEY +"Execute&Identifier=";
	private static final String DATA_INPUTS = "&DataInputs={dataInputs}";
	private static final String RESPONSE_FORM = "&ResponseForm={responseForm}";
	
	// query string patterns for routing; each makes ResponseForm optional
	private static final String GET_CAPABILITIES =
		SERVICE + REQ_KEY + "GetCapabilities";
	private static final String GET_CAPABILITIES_R =
		GET_CAPABILITIES + RESPONSE_FORM;
	
	private static final String DESCRIBE_PROCESS =
		SERV_VER + REQ_KEY + "DescribeProcess";
	private static final String DESCRIBE_PROCESS_R = 
		DESCRIBE_PROCESS + RESPONSE_FORM;
	
	private static final String DESCRIBE_SRS =
		SERV_VER + EXEC_ID_KEY + "DescribeSRS" + DATA_INPUTS;
	private static final String DESCRIBE_SRS_R =
		DESCRIBE_SRS + RESPONSE_FORM;
	
	// no data inputs
	private static final String LIST_SRS_S =
		SERV_VER + EXEC_ID_KEY + "ListSRSs";
	private static final String LIST_SRS_S_R =
		LIST_SRS_S + RESPONSE_FORM;
	
	private static final String DESCRIBE_TRANSFORMATION =
		SERV_VER + EXEC_ID_KEY + "DescribeTransfomation" + DATA_INPUTS;
	private static final String DESCRIBE_TRANSFORMATION_R =
		DESCRIBE_TRANSFORMATION + RESPONSE_FORM;
	
	private static final String LIST_TRANSFORMATIONS =
		SERV_VER + EXEC_ID_KEY + "ListTransformations" + DATA_INPUTS;
	private static final String LIST_TRANSFORMATIONS_R =
		LIST_TRANSFORMATIONS + RESPONSE_FORM;
	
	private static final String TRANSFORM_POI =
		SERV_VER + EXEC_ID_KEY + "TransformPOI" + DATA_INPUTS;
	private static final String TRANSFORM_POI_R =
		TRANSFORM_POI + RESPONSE_FORM;
	
	private static final String GET_TRANSFORMATION_CHAIN =
		SERV_VER + EXEC_ID_KEY + "GetTransformationChain" + DATA_INPUTS;
	private static final String GET_TRANSFORMATION_CHAIN_R =
		GET_TRANSFORMATION_CHAIN + RESPONSE_FORM;
	
	private static final String GET_2D_IMAGES_BY_POI =
		SERV_VER + EXEC_ID_KEY + "Get2DImagesByPOI" + DATA_INPUTS;
	private static final String GET_2D_IMAGES_BY_POI_R =
		GET_2D_IMAGES_BY_POI + RESPONSE_FORM;
	
	private static final String GET_CORRELATION_MAP_BY_POI =
		SERV_VER + EXEC_ID_KEY + "GetCorrelationMapByPOI" + DATA_INPUTS;
	private static final String GET_CORRELATION_MAP_BY_POI_R =
		GET_CORRELATION_MAP_BY_POI + RESPONSE_FORM;
	
	private static final String GET_GENES_BY_POI =
		SERV_VER + EXEC_ID_KEY + "GetGenesByPOI" + DATA_INPUTS;
	private static final String GET_GENES_BY_POI_R =
		GET_GENES_BY_POI + RESPONSE_FORM;
	
	private static final String GET_STRUCTURE_NAMES_BY_POI =
		SERV_VER + EXEC_ID_KEY + "GetStructureNamesByPOI" + DATA_INPUTS;
	private static final String GET_STRUCTURE_NAMES_BY_POI_R =
		GET_STRUCTURE_NAMES_BY_POI + RESPONSE_FORM;
	
	private static final String RETRIEVE_2D_IMAGE =
		SERV_VER + EXEC_ID_KEY + "Retrieve2DImage" + DATA_INPUTS;
	private static final String RETRIEVE_2D_IMAGE_R =
		RETRIEVE_2D_IMAGE + RESPONSE_FORM;
	
	public ServerApplication() {

		// redirect restlet logging to slf4j
		// see http://wiki.restlet.org/docs_1.1/13-restlet/48-restlet/101-restlet.html
		// "Replacing default JDK logging with log4j"
		SLF4JBridgeHandler.install();

		StringBuilder buf = new StringBuilder();
		buf.append("\n");
		buf.append("       *******************************************\n");
		buf.append("       *          Starting INCF Server           *\n");
		buf.append("       *******************************************\n");
		buf.append("\n  Server version   : ");
		buf.append("\nWaiting for connections ...");
		logger.info(buf.toString());
	}
	
	@Override
	public Restlet createRoot() {
		Router router = new Router(getContext());
		
		router.attach(GET_CAPABILITIES, Capabilities.class);
		router.attach(GET_CAPABILITIES_R, Capabilities.class);
		
		router.attach(DESCRIBE_PROCESS, ProcessDescriptions.class);
		router.attach(DESCRIBE_PROCESS_R, ProcessDescriptions.class);
		
		router.attach(DESCRIBE_SRS, NotYetImplemented.class);
		router.attach(DESCRIBE_SRS_R, NotYetImplemented.class);
		
		router.attach(LIST_SRS_S, NotYetImplemented.class);
		router.attach(LIST_SRS_S_R, NotYetImplemented.class);
		
		router.attach(DESCRIBE_TRANSFORMATION, NotYetImplemented.class);
		router.attach(DESCRIBE_TRANSFORMATION_R, NotYetImplemented.class);
		
		router.attach(LIST_TRANSFORMATIONS, NotYetImplemented.class);
		router.attach(LIST_TRANSFORMATIONS_R, NotYetImplemented.class);
		
		router.attach(TRANSFORM_POI, TransformPOI.class);
		router.attach(TRANSFORM_POI_R, TransformPOI.class);
		
		router.attach(GET_TRANSFORMATION_CHAIN, CoordinateTransformationChain.class);
		router.attach(GET_TRANSFORMATION_CHAIN_R, CoordinateTransformationChain.class);
		
		router.attach(GET_2D_IMAGES_BY_POI, Images2DByPOI.class);
		router.attach(GET_2D_IMAGES_BY_POI_R, Images2DByPOI.class);
		
		System.out.println(" Before forwarding - Welcome to CorrelationMapByPOI Method");

		router.attach(GET_CORRELATION_MAP_BY_POI, CorrelationMapByPOI.class);
		router.attach(GET_CORRELATION_MAP_BY_POI_R, CorrelationMapByPOI.class);
		
		router.attach(GET_GENES_BY_POI, NotYetImplemented.class);
		router.attach(GET_GENES_BY_POI_R, NotYetImplemented.class);
		
		router.attach(GET_STRUCTURE_NAMES_BY_POI, StructureNamesByPOI.class);
		router.attach(GET_STRUCTURE_NAMES_BY_POI_R, StructureNamesByPOI.class);
		
		router.attach(RETRIEVE_2D_IMAGE, NotYetImplemented.class);
		router.attach(RETRIEVE_2D_IMAGE_R, NotYetImplemented.class);
		
		// attach resource handlers based on the URI
		router.attach("/favicon.ico", FaviconResource.class);

		router.attach("/ping/{pingType}", PingResource.class);
		
/*		router.attach("/{uriSuffix}", UnrecognizedUri.class);
		router.attach("?{uriSuffix}", UnrecognizedUri.class);
*/
		logger.error("No router matched.");

		return router;
	}

	// testing only
	public static void main(String[] args) {

		// redirect restlet logging to slf4j
		// see http://wiki.restlet.org/docs_1.1/13-restlet/48-restlet/101-restlet.html
		// "Replacing default JDK logging with log4j"
		SLF4JBridgeHandler.install();

		StringBuilder buf = new StringBuilder();
		buf.append("\n");
		buf.append("       *******************************************\n");
		buf.append("       *          Starting INCF Server           *\n");
		buf.append("       *******************************************\n");
		buf.append("\n  Server version   : ");
		buf.append("\nWaiting for connections ...");
		logger.info(buf.toString());
		
		try {
			Component component = new Component();
			component.getServers().add(Protocol.HTTP, 8182);
			component.getDefaultHost().attach(new ServerApplication());
			component.start();
		} catch (Exception e) {
			logger.error("Exception in main().", e);
		}
	}

}
