package org.incf.central.atlas.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import net.opengis.wps.x100.ProcessDescriptionType;
import net.opengis.wps.x100.ProcessDescriptionsDocument;
import net.opengis.wps.x100.ProcessDescriptionsDocument.ProcessDescriptions;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.deegree.commons.xml.XMLAdapter;
import org.deegree.services.controller.exception.ControllerException;
import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.wps.Processlet;
import org.deegree.services.wps.ProcessletException;
import org.deegree.services.wps.ProcessletExecutionInfo;
import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.ProcessletOutputs;
import org.deegree.services.wps.output.ComplexOutput;
import org.incf.atlas.waxml.generated.DescribeProcessForHubsResponseDocument;
import org.incf.atlas.waxml.generated.DescribeProcessForHubsResponseType;
import org.incf.atlas.waxml.generated.DescribeProcessForHubsResponseType.DescribeProcessCollection;
import org.incf.atlas.waxml.utilities.Utilities;
import org.incf.common.atlas.util.DataInputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetObjectsByPOI implements Processlet {

    private static final Logger LOG = LoggerFactory.getLogger(
            GetObjectsByPOI.class);
    
	private static final String SERVER = "incf-dev.crbs.ucsd.edu";
	private static final int PORT = 80;
	
	private String server;
	private int port;
	
	private String srsName;
	private double x;
	private double y;
	private double z;
	
	// response body DOM
	private DescribeProcessForHubsResponseDocument document;
	private DescribeProcessForHubsResponseType eDescribeProcessForHubsResponse;
	
	/**
	 * Applicable "...ByPOI" web services:
	 *                            aba  emap ucsd whs
	 *                            ---- ---- ---- ---- 
	 * - Get2DImagesByPOI         yes  no   chk  no
	 * - GetAnnotationsByPOI      chk  chk  chk  chk
	 * - GetCorrelationMapByPOI   yes  no   no   no
	 * - GetGenesByPOI            yes  yes  no   no
	 * - GetStructureNamesByPOI   yes  no   chk  yes
	 *     total (min/max)        4/5  1/2  0/3  1/2 
	 *     
	 * Legend:
	 * - yes -- automatically include because there will be non-empty response
	 * 			regardless of POI coordinates
	 * - no  -- exclude because the process is not applicable to the hub
	 * - chk -- include if process returns non-empty response for given POI,
	 * 			otherwise exclude
	 * 
	 * Go to each hub other than central that offers one of the applicable
	 * web services. If there is a non-negative, non-empty return, add it to a
	 * list. If there is a negative of empty return, ignore it.
	 * 
	 * @see org.deegree.services.wps.Processlet#process(org.deegree.services.wps.ProcessletInputs, org.deegree.services.wps.ProcessletOutputs, org.deegree.services.wps.ProcessletExecutionInfo)
	 */
	@Override
	public void process(ProcessletInputs in, ProcessletOutputs out,
			ProcessletExecutionInfo info) throws ProcessletException {
    	try {
    		
    		// validate against allowed values in process definition file
    		URL processDefinitionUrl = this.getClass().getResource(
    				"/" + this.getClass().getSimpleName() + ".xml");
    		
    		// retrieve validated inputs or default values
    		DataInputHandler dataInputHandler = new DataInputHandler(
    				new File(processDefinitionUrl.toURI()));
    		srsName = dataInputHandler.getValidatedStringValue(in, 
    				"srsName");
    		x = DataInputHandler.getDoubleInputValue(in, "x");
    		y = DataInputHandler.getDoubleInputValue(in, "y");
    		z = DataInputHandler.getDoubleInputValue(in, "z");
    		
    		LOG.debug(String.format(
    				"DataInputs: srsName: %s, poi: (%f, %f, %f)",
    				srsName, x, y, z));
    		
    		// prepare response document
    		prepareResponse();
    		LOG.debug("ObjectsByPOI 1");
    		
    		if (LOG.isDebugEnabled()) {
    			XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
    			opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
    			opt.setSaveNamespacesFirst();
    			opt.setSaveAggressiveNamespaces();
    			opt.setUseDefaultNamespace();
    			LOG.debug("Xml:\n{}", document.xmlText(opt));
    		}
    		LOG.debug("ObjectsByPOI 2");

    		// send it
    		// get reader on document
    		XMLStreamReader reader = document.newXMLStreamReader();
    		LOG.debug("ObjectsByPOI 3");
    		
    		// get ComplexOutput object from ProcessletOutput...
    		ComplexOutput complexOutput = (ComplexOutput) out.getParameter(
    				"GetObjectsByPOIOutput");

    		LOG.debug("Setting complex output (requested=" 
    				+ complexOutput.isRequested() + ")");
    		
    		// ComplexOutput objects can be huge so stream it 
    		XMLStreamWriter writer = complexOutput.getXMLStreamWriter();
    		XMLAdapter.writeElement(writer, reader);
    		
    		// transform any exceptions into ProcessletException wrapping
    		// OWSException
        } catch (OWSException e) {
            LOG.error(e.getMessage(), e);
        	throw new ProcessletException(e);	// is already OWSException
        } catch (Throwable e) {
        	String message = "Unexpected exception occurred: " + e.getMessage();
        	LOG.error(message, e);
        	throw new ProcessletException(new OWSException(message, e, 
        			ControllerException.NO_APPLICABLE_CODE));
        }
	}
	
	private void prepareResponse() throws IOException, XmlException {
		
		// prepare response document
		document = DescribeProcessForHubsResponseDocument.Factory.newInstance();
		eDescribeProcessForHubsResponse = 
				document.addNewDescribeProcessForHubsResponse();
		
		// add elements for each hub
		for (Hub hub : Hub.values()) {
			composeCollection(hub);
		}

	}
	
	private void composeCollection(Hub hub) throws IOException, XmlException {
		String hubCode = hub.getHubCode();
		
		// make list for process descriptions
		List<ProcessDescriptionType> processDescriptionList = 
				new ArrayList<ProcessDescriptionType>();
		
		// Get2DIMagesByPOI
		if (
				   (hub == Hub.ABA)
				|| (hub == Hub.UCSD && doesGet2DImagesContainResults(hub))
				) {
			processDescriptionList.add(
					getProcessDescription(hub, "Get2DImagesByPOI"));
		}

		// GetAnnotationsByPOI
		if (
				   (hub == Hub.ABA  && doesGetAnnotationsContainResults(hub))
				//|| (hub == Hub.EMAP && doesGetAnnotationsContainResults(hub)) -- not yet implmented
				|| (hub == Hub.UCSD && doesGetAnnotationsContainResults(hub))
				|| (hub == Hub.WHS  && doesGetAnnotationsContainResults(hub))
				) {
				processDescriptionList.add(
						getProcessDescription(hub, "GetAnnotationsByPOI"));
		}
		
		// add GetCorellationMapByPOI
		if (
				   (hub == Hub.ABA)
				) {
			processDescriptionList.add(
					getProcessDescription(hub, "GetCorrelationMapByPOI"));
		}

		// add GetGenesByPOI
		if (    
				   (hub == Hub.ABA)
				//|| (hub == Hub.EMAP)		-- not yet implemented
				) {
			processDescriptionList.add(
					getProcessDescription(hub, "GetGenesByPOI"));
		}

		// add GetStructureNamesByPOI
		if (       (hub == Hub.ABA)
				|| (hub == Hub.UCSD && doesGetStructureNamesContainResults(hub))
				|| (hub == Hub.WHS)
				) {
			processDescriptionList.add(
					getProcessDescription(hub, "GetStructureNamesByPOI"));
		}
		
		// if we have something,convert to array, add collection for this hub
		if (processDescriptionList.size() > 0) {
			ProcessDescriptionType[] processDescriptionArray = 
				processDescriptionList.toArray(new ProcessDescriptionType[0]);
			DescribeProcessCollection eDescribeProcessCollection =
				eDescribeProcessForHubsResponse.addNewDescribeProcessCollection();
			eDescribeProcessCollection.setProcessDescriptionArray(processDescriptionArray);
			eDescribeProcessCollection.setHubCode(hubCode);
		}
	}
		
	private boolean doesGet2DImagesContainResults(Hub hub) throws IOException {
		URL url = new URL(assembleGet2DImagesByPOIURI(hub.getHubCode(), srsName, x, y, z));
		
		LOG.debug("Get2DImages URI: {}", url.toString());

        InputStream in = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while ((line = reader.readLine()) != null) {
        	if (line.contains("<Image2Dcollection hubCode=\"" + hub.getHubCode() + "\"/>")
    			|| line.contains("<wps:ProcessFailed>")) {
        		return false;
        	}
        	
        	// once we see this, no need to read further
        	if (line.contains("<Image2D>")) {
        		return true;
        	}
        }
		return false;		// essentially superfluous
	}
	
	private boolean doesGetAnnotationsContainResults(Hub hub) throws IOException {
		URL url = new URL(assembleGetAnnotationsByPOIURI(hub.getHubCode(), srsName, x, y, z));
		
		LOG.debug("GetAnnotations URI: {}", url.toString());

        InputStream in = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while ((line = reader.readLine()) != null) {
        	if (line.contains("<AnnotationResponse/>")
        			|| line.contains("<wps:ProcessFailed>")) {
        		return false;
        	}
        	
        	// once we see this, no need to read further
        	if (line.contains("<Annotation>")) {
        		return true;
        	}
        }
		return false;		// essentially superfluous
	}
	
	private boolean doesGetStructureNamesContainResults(Hub hub) throws IOException {
		URL url = new URL(assembleGetStructureNamesByPOIURI(hub.getHubCode(), srsName, x, y, z));
		
		LOG.debug("GetStructureNames URI: {}", url.toString());

        InputStream in = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while ((line = reader.readLine()) != null) {
        	if (line.contains("<StructureTerms hubCode=\"" + hub.getHubCode() + "\"/>")
        			|| line.contains("<wps:ProcessFailed>")) {
        		return false;
        	}
        	
        	// once we see this, no need to read further
        	if (line.contains("<StructureTerm>")) {
        		return true;
        	}
        }
		return false;		// essentially superfluous
	}
	
	@Override
	public void destroy() {
	}

	@Override
	public void init() {
		server = SERVER;
		port = PORT;
	}
	
	
	// http://incf-dev.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0
	// &request=DescribeProcess&Identifier=GetAnnotationsByPOI
	private String assembleDescribeProcessURI(String hubCode, String identifier) {
		return String.format("%s&request=DescribeProcess&Identifier=%s", 
				assembleWPSBaseURI(hubCode), identifier);
	}
	
	// http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0
	// &request=Execute&Identifier=Get2DImagesByPOI
	// &DataInputs=srsName=Mouse_ABAvoxel_1.0;x=263;y=159;z=227;filter=maptype:sagittal;tolerance=3
	private String assembleGet2DImagesByPOIURI(String hubCode, 
			String srsName, double x, double y, double z) {
		return String.format("%s&request=Execute&Identifier=GetAnnotationsByPOI"
				+ "&DataInputs=srsName=%s;x=%f;y=%f;z=%f;filter=maptype:sagittal;tolerance=3", 
				assembleWPSBaseURI(hubCode), srsName, x, y, z);
	}
	
	// http://incf-dev-local.crbs.ucsd.edu/aba/atlas?service=WPS&version=1.0.0
	// &request=Execute&Identifier=GetAnnotationsByPOI
	// &DataInputs=srsName=Mouse_ABAreference_1.0;x=-2;y=-1;z=0;tolerance=3
	private String assembleGetAnnotationsByPOIURI(String hubCode, 
			String srsName, double x, double y, double z) {
		return String.format("%s&request=Execute&Identifier=GetAnnotationsByPOI"
				+ "&DataInputs=srsName=%s;x=%f;y=%f;z=%f;tolerance=3", 
				assembleWPSBaseURI(hubCode), srsName, x, y, z);
	}
	
	// http://incf-dev.crbs.ucsd.edu/ucsd/atlas?service=WPS&version=1.0.0
	// &request=Execute&Identifier=GetStructureNamesByPOI
	// &DataInputs=srsName=Mouse_Paxinos_1.0;x=-4;y=-2.3;z=2;vocabulary=Mouse_Paxinos_1.0;filter=NONE
	private String assembleGetStructureNamesByPOIURI(String hubCode, 
			String srsName, double x, double y, double z) {
		return String.format("%s&request=Execute&Identifier=GetStructureNamesByPOI"
				+ "&DataInputs=srsName=%s;x=%f;y=%f;z=%f;vocabulary=Mouse_Paxinos_1.0;filter=NONE", 
				assembleWPSBaseURI(hubCode), srsName, x, y, z);
	}
	
	private String assembleWPSBaseURI(String hubCode) {
		String portString = (port == 0 || port == -1 || port == 80) ? "" 
				: ':' + String.valueOf(port);
		return String.format("http://%s%s/%s/atlas?service=WPS&version=1.0.0", 
				server, portString, hubCode);
	}
	
	private ProcessDescriptionType getProcessDescription(Hub hub, 
			String identifier) throws IOException, XmlException {
		URL u = new URL(assembleDescribeProcessURI(hub.getHubCode(), identifier));		
		
		LOG.debug("DescribeProcess URI: {}", u.toString());

        InputStream in = u.openStream();
        ProcessDescriptionsDocument pdDoc = ProcessDescriptionsDocument.Factory.parse(in);
        ProcessDescriptions pds = pdDoc.getProcessDescriptions();
        ProcessDescriptionType eProcessDescription = pds.getProcessDescriptionArray(0);
		return eProcessDescription;
	}
	
	private enum Hub {
		ABA  ("aba"), 
		EMAP ("emap"),
		UCSD ("ucsd"), 
		WHS  ("whs");
		
		private final String hubCode;
		
		private Hub (String hubCode) {
			this.hubCode = hubCode;
		}
		
		public String getHubCode() {
			return hubCode;
		}
	}
	
	// testing constructor
	public GetObjectsByPOI() {
		server = SERVER;
		port = PORT;
		srsName = "Mouse_ABAreference_1.0";
		x = -2;
		y = -1;
		z = 0;
	}
	
	// testing
	public void execute() throws IOException, XmlException {
		
		prepareResponse();
		
		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();
		System.out.printf("Xml:%n%s%n", document.xmlText(opt));
	}
	
	// testing
	public static void main(String[] args) throws IOException, XmlException {
		GetObjectsByPOI testApp = new GetObjectsByPOI();
		testApp.execute();
	}

}
