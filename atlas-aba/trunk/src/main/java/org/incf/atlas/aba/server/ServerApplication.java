package org.incf.atlas.aba.server;

import org.incf.atlas.aba.resource.Capabilities;
import org.incf.atlas.aba.resource.CoordinateTransformationChainResource;
import org.incf.atlas.aba.resource.DescribeProcess;
import org.incf.atlas.aba.resource.FaviconResource;
import org.incf.atlas.aba.resource.PingResource;
import org.incf.atlas.aba.resource.StructureNamesByPOIResource;
import org.incf.atlas.aba.resource.TransformPOIResource;
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

	@Override
	public Restlet createRoot() {

		System.out.println("***CreateRoot***");
		Router router = new Router(getContext());

		// attach resource handlers based on the URI
		router.attach("/favicon.ico", FaviconResource.class);

		router.attach("/ping/{pingType}", PingResource.class);
		
		/* GetCapabilities
		 * ?request=GetCapabilities&output=xml
		 */
		router.attach("?Request=GetCapabilities", Capabilities.class);
		router.attach("?request=GetCapabilities", Capabilities.class);
		
		router.attach("?Request=DescribeProcess", DescribeProcess.class);
		router.attach("?request=DescribeProcess", DescribeProcess.class);

		router.attach("?Request=Execute&Identifier=GetTransformationChain"
				+ "&DataInputs={dataInputs}",
				CoordinateTransformationChainResource.class);
		router.attach("?request=Execute&identifier=GetTransformationChain"
				+ "&dataInputs={dataInputs}",
				CoordinateTransformationChainResource.class);

		router.attach("?Request=Execute&Identifier=TransformPOI"
				+ "&DataInputs={dataInputs}",
				TransformPOIResource.class);
		router.attach("?request=Execute&identifier=TransformPOI"
				+ "&dataInputs={dataInputs}",
				TransformPOIResource.class);

		router.attach("?Request=Execute&Identifier=GetStructureNamesByPOI"
				+ "&DataInputs={dataInputs}",
				StructureNamesByPOIResource.class);
		router.attach("?request=Execute&identifier=GetStructureNamesByPOI"
				+ "&dataInputs={dataInputs}",
				StructureNamesByPOIResource.class);

		return router;

	}

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
