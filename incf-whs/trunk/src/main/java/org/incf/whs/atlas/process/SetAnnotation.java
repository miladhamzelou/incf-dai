package org.incf.whs.atlas.process;

import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.DirectPositionListType;
import net.opengis.gml.x32.DirectPositionType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PointPropertyType;
import net.opengis.gml.x32.PointType;

import org.incf.atlas.waxml.generated.AnnotationResponseDocument.AnnotationResponse;
import org.incf.atlas.waxml.generated.AnnotationType.GEOMETRIES;
import org.incf.atlas.waxml.generated.AnnotationType.ONTOTERMS;
import org.incf.atlas.waxml.generated.AnnotationType.RESOURCE;
import org.incf.atlas.waxml.generated.CorrelatioMapType.CorrelationCollection;
import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.deegree.commons.utils.kvp.InvalidParameterValueException;
import org.deegree.commons.utils.kvp.MissingParameterException;
import org.deegree.commons.xml.XMLAdapter;
import org.deegree.services.controller.exception.ControllerException;
import org.deegree.services.controller.ows.OWSException;
import org.deegree.services.wps.Processlet;
import org.deegree.services.wps.ProcessletException;
import org.deegree.services.wps.ProcessletExecutionInfo;
import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.ProcessletOutputs;
import org.deegree.services.wps.input.LiteralInput;
import org.deegree.services.wps.output.ComplexOutput;
import org.incf.atlas.waxml.generated.AnnPolygonType;
import org.incf.atlas.waxml.generated.AnnotationResponseDocument;
import org.incf.atlas.waxml.generated.AnnotationType;
import org.incf.atlas.waxml.generated.CorrelatioMapType;
import org.incf.atlas.waxml.generated.CorrelationMapResponseDocument;
import org.incf.atlas.waxml.generated.GENERALONTOMODEL;
import org.incf.atlas.waxml.generated.GEOMETRYTYPE;
import org.incf.atlas.waxml.generated.OBJECTTYPE;
import org.incf.atlas.waxml.generated.ONTOPROPERTYTYPE;
import org.incf.atlas.waxml.generated.ONTOTERMTYPE;
import org.incf.atlas.waxml.generated.RELATIONSTYPE;
import org.incf.atlas.waxml.generated.SUBJECTTYPE;
import org.incf.atlas.waxml.utilities.Utilities;
import org.incf.common.atlas.exception.InvalidDataInputValueException;
import org.incf.common.atlas.util.AllowedValuesValidator;
import org.incf.common.atlas.util.DataInputHandler;
import org.incf.common.atlas.util.Util;
import org.incf.whs.atlas.util.AnnotationModel;
import org.incf.whs.atlas.util.IncfDBUtil;
import org.incf.whs.atlas.util.WHSConfigurator;
import org.incf.whs.atlas.util.WHSServiceVO;
import org.incf.whs.atlas.util.XML2AnnotObjects;
import org.incf.whs.atlas.util.XMLUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetAnnotation implements Processlet {

	private static final Logger LOG = LoggerFactory
			.getLogger(SetAnnotation.class);

	WHSConfigurator config = WHSConfigurator.INSTANCE;

	String abaReference = config.getValue("srsname.abareference.10");
	String abaVoxel = config.getValue("srsname.abavoxel.10");
	String agea = config.getValue("srsname.agea.10");
	String whs09 = config.getValue("srsname.whs.09");
	String whs10 = config.getValue("srsname.whs.10");
	String emap = config.getValue("srsname.emap.10");
	String paxinos = config.getValue("srsname.paxinos.10");

	// private String dataInputString;
	// private DataInputs dataInputs;
	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	String responseString = "";

	@Override
	public void process(ProcessletInputs in, ProcessletOutputs out,
			ProcessletExecutionInfo info) throws ProcessletException {

		try {

			LOG.debug(" Inside SetAnnotation... ");

			WHSServiceVO vo = new WHSServiceVO();

			URL processDefinitionUrl = this.getClass().getResource(
					"/" + this.getClass().getSimpleName() + ".xml");
			DataInputHandler dataInputHandler = new DataInputHandler(new File(
					processDefinitionUrl.toURI()));
			String filePath = dataInputHandler.getValidatedStringValue(in,
					"filePath");

			String filter = dataInputHandler.getValidatedStringValue(in,
			"filter");

			//Start - Seperate userID and password from the filter attribute
			String serviceUserID = "";
			String servicePassword = "";
			StringTokenizer tokens1 = new StringTokenizer(filter, "=");
			System.out.println("Tokens: " + tokens1.countTokens() );
			while (tokens1.hasMoreTokens()){
				System.out.println("passtoken: " +tokens1.nextToken());	
				serviceUserID = tokens1.nextToken();
				servicePassword = tokens1.nextToken();
			}
			serviceUserID = serviceUserID.replaceAll(":password", "");
			System.out.println("userID after: " + serviceUserID);
			System.out.println("Password: " + servicePassword);
			//End - Seperate userID and password from the filter attribute
			
			//Check for userID and password
			if ( serviceUserID == null || serviceUserID.equals("") ) {
    			throw new OWSException("Please provide a valid \"userID\" to access the service.", 
					ControllerException.NO_APPLICABLE_CODE);
			}

			if ( servicePassword == null || servicePassword.equals("") ) {
    			throw new OWSException("Please provide a valid \"password\" to access the service.", 
    					ControllerException.NO_APPLICABLE_CODE);
			}

			if ( !serviceUserID.equals("incfadmin") ) {
    			throw new OWSException("User Authentication Fails. Please provide the correct User ID.", 
    					ControllerException.NO_APPLICABLE_CODE);
			}

			if ( !servicePassword.equals("1ncfA1la5") ) {
    			throw new OWSException("User Authentication Fails. Please provide the correct Password.", 
    					ControllerException.NO_APPLICABLE_CODE);
			}

	    	org.incf.whs.atlas.util.PostgresDBService.URL = "jdbc:postgresql://incf-dev-local.crbs.ucsd.edu:5432/atlas-whs-db";
	    	org.incf.whs.atlas.util.PostgresDBService.USERNAME = "atlas-whs-db";
	    	org.incf.whs.atlas.util.PostgresDBService.PASSWORD = "whs4321";

	        XML2AnnotObjects xml2o = new XML2AnnotObjects();
	        IncfDBUtil util = new IncfDBUtil();
	        //String xml = util.getXMLString(filePath);
	        XMLUtilities xmlUtil = new XMLUtilities();
	        String xml = xmlUtil.convertFromURLToString(filePath);
	        System.out.println(xml);
	        AnnotationModel amodel = xml2o.xml2polygons(xml);
	        System.out.println("Resource path:"+amodel.getResourcePath());

	        IncfDBUtil iutil = new IncfDBUtil();
	        iutil.insertAnnotation(amodel);


			// Generating 2 random number to be used as GMLID
			Random randomGenerator1 = new Random();
			int randomGMLID1 = 0;
			int randomGMLID2 = 0;

			for (int idx = 1; idx <= 10; ++idx) {
				randomGMLID1 = randomGenerator1.nextInt(100);
			}
			Random randomGenerator2 = new Random();
			for (int idx = 1; idx <= 10; ++idx) {
				randomGMLID2 = randomGenerator2.nextInt(100);
			}
			LOG.debug("Random GML ID1: - {}" , randomGMLID1);
			LOG.debug("Random GML ID2: - {}" , randomGMLID2);

			// url = "http://" + hostName + portNumber + servicePath +
			// "&DataInputs=" + dataInputsString;
			XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
			opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
			opt.setSaveNamespacesFirst();
			opt.setSaveAggressiveNamespaces();
			opt.setUseDefaultNamespace();

			System.out.println("Before Submit: " );
			AnnotationResponseDocument document = completeResponse(vo);
			System.out.println("After Submit: " );

			/*			
			ArrayList errorList = new ArrayList();
			opt.setErrorListener(errorList);
			boolean isValid = document.validate(opt);

			// If the XML isn't valid, loop through the listener's contents,
			// printing contained messages.
			if (!isValid) {
				for (int i = 0; i < errorList.size(); i++) {
					XmlError error = (XmlError) errorList.get(i);

					LOG.debug("\n");
					LOG.debug("Message: {}" , error.getMessage() + "\n");
					LOG.debug("Location of invalid XML: {}"
							, error.getCursorLocation().xmlText() + "\n");
				}
			}
*/

			ComplexOutput complexOutput = (ComplexOutput) out
					.getParameter("SetAnnotationOutput");

			//get reader on document; reader --> writer
			XMLStreamReader reader = document.newXMLStreamReader();
			XMLStreamWriter writer = complexOutput.getXMLStreamWriter();
			XMLAdapter.writeElement(writer, reader);

		} catch (MissingParameterException e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessletException(new OWSException(e));
		} catch (InvalidParameterValueException e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessletException(new OWSException(e));
		} catch (InvalidDataInputValueException e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessletException(e); // is already OWSException
		} catch (OWSException e) {
			LOG.error(e.getMessage(), e);
			throw new ProcessletException(e); // is already OWSException
		} catch (Throwable e) {
			String message = "Unexpected exception occurred: " + e.getMessage();
			LOG.error(message, e);
			throw new ProcessletException(new OWSException(message, e,
					ControllerException.NO_APPLICABLE_CODE));
		}
	}

	
	public AnnotationResponseDocument completeResponse(WHSServiceVO vo) {

		System.out.println("****Before Complete Response****");
		AnnotationResponseDocument doc = AnnotationResponseDocument.Factory.newInstance();
		AnnotationResponse annResp = doc.addNewAnnotationResponse();
		System.out.println("****After Complete Response****");

		return doc;

	}


	static String gmlId(String id){
		
		final String baseId = "ANN";
		if (isValidGmlId(id)){
			return id;
		} else
		{
			return baseId + id;
		}
	}
	static String gmlId(int id){
		
		final String baseId = "ANN";
		
			return String.format("{0}{1}", baseId,id);
		
	}
	static String gmlIdInteralReference(String id){
		 if (id == null || id.isEmpty()) throw new IllegalArgumentException("Parameter Cannot be null");
		final String baseReference = "#";
		 return baseReference+ id;
	}
	static Boolean isValidGmlId(String id){
		final  String NCNamePattern =  "[i-[:]][c-[:]]*";
		
		return id.matches(NCNamePattern);
			
	}
	
	static String posListString(double x, double y, double z) {
		String s = String.format("{0},{1},{2}", x, y, z);
		return s;
	}

	static DirectPositionListType ArrayToDirectPositionList(double[][] points) {
		DirectPositionListType posList = DirectPositionListType.Factory
				.newInstance();
		StringBuffer sb = new StringBuffer();
		for (int point = 0; point < points.length; point++) {
			String s = posListString( points[point][0],
					points[point][1], points[point][2]);
			sb.append(s);
			if (point < points.length + 1)
				sb.append(" ");

		}
		return posList;
	}

	
	static PointPropertyType TopointPropertyType(double x, double y, double z) {
		PointPropertyType ppt = PointPropertyType.Factory.newInstance();
		PointType pnt = ppt.addNewPoint();
		DirectPositionType pos = pnt.addNewPos();

		pos.setStringValue(posListString(x, y, z));
		return ppt;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

}
