package org.incf.whs.atlas.process;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import net.opengis.gml.x32.DirectPositionListType;
import net.opengis.gml.x32.PointType;

import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlError;
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
import org.deegree.services.wps.output.ComplexOutput;
import org.incf.whs.atlas.util.WHSConfigurator;
import org.incf.whs.atlas.util.WHSServiceVO;
import org.incf.whs.atlas.util.WHSUtil;
import org.incf.atlas.waxml.generated.POIType;
import org.incf.atlas.waxml.generated.TransformationResponseDocument;
import org.incf.atlas.waxml.generated.TransformationResponseType;
import org.incf.atlas.waxml.utilities.Utilities;
import org.incf.common.atlas.exception.InvalidDataInputValueException;
import org.incf.common.atlas.util.AllowedValuesValidator;
import org.incf.common.atlas.util.DataInputHandler;
import org.incf.common.atlas.util.Util;
import org.incf.common.atlas.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformPOI implements Processlet {

	private static final Logger LOG = LoggerFactory
			.getLogger(TransformPOI.class);

	WHSConfigurator config = WHSConfigurator.INSTANCE;

	String abaReference = config.getValue("srsname.abareference.10");
	String abaVoxel = config.getValue("srsname.abavoxel.10");
	String agea = config.getValue("srsname.agea.10");
	String whs09 = config.getValue("srsname.whs.09");
	String whs10 = config.getValue("srsname.whs.10");
	String emap = config.getValue("srsname.emap.10");
	String paxinos = config.getValue("srsname.paxinos.10");

	String abavoxel2agea = config.getValue("code.abavoxel2agea.v1");
	String agea2abavoxel = config.getValue("code.agea2abavoxel.v1");
	String whs092agea = config.getValue("code.whs092agea.v1");
	String agea2whs09 = config.getValue("code.agea2whs09.v1");
	String whs092whs10 = config.getValue("code.whs092whs10.v1");
	String whs102whs09 = config.getValue("code.whs102whs09.v1");
	String abareference2abavoxel = config
			.getValue("code.abareference2abavoxel.v1");
	String abavoxel2abareference = config
			.getValue("code.abavoxel2abareference.v1");
	String paxinos2whs09 = config.getValue("code.paxinos2whs09.v1");
	String whs092paxinos = config.getValue("code.whs092paxinos.v1");

	// private String dataInputString;
	// private DataInputs dataInputs;
	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	String responseString = "";
	int randomGMLID1 = 0;
	int randomGMLID2 = 0;

	@Override
	public void process(ProcessletInputs in, ProcessletOutputs out,
			ProcessletExecutionInfo info) throws ProcessletException {

		try {

			WHSServiceVO vo = null;

			// parse dataInputs string
			LOG.debug(" Inside TransformPOI... ");

			URL processDefinitionUrl = this.getClass().getResource(
					"/" + this.getClass().getSimpleName() + ".xml");
			
			DataInputHandler dataInputHandler = new DataInputHandler(new File(
					processDefinitionUrl.toURI()));
			String transformationCode = "";
			transformationCode = dataInputHandler.getValidatedStringValue(in, "transformationCode");
			String points = dataInputHandler.getValidatedStringValue(in, "points");
			String x = "";
			String y = "";
			String z = "";
			List pointsList = new ArrayList(); 
			
			if ( points == null || points.equals("") ) {

				vo = new WHSServiceVO();
				
				x = String.valueOf(DataInputHandler.getDoubleInputValue(in,
						"x"));
				y = String.valueOf(DataInputHandler.getDoubleInputValue(in,
						"y"));
				z = String.valueOf(DataInputHandler.getDoubleInputValue(in,
						"z"));
				vo.setOriginalCoordinateX(String.valueOf(x));
				vo.setOriginalCoordinateY(String.valueOf(y));
				vo.setOriginalCoordinateZ(String.valueOf(z));
				LOG.debug("X: {}" , vo.getOriginalCoordinateX());
				LOG.debug("Y: {}" , vo.getOriginalCoordinateY());
				LOG.debug("Z: {}" , vo.getOriginalCoordinateZ());
				vo.setTransformationCode(transformationCode);
				String[] transformationNameArray;
				String delimiter = "_To_";
				transformationNameArray = vo.getTransformationCode().split(
						delimiter);
				String fromSRSCode = transformationNameArray[0];
				String toSRSCode = transformationNameArray[1].replace("_v1.0", "");

				LOG.debug(" Input SRS Name: {}" , fromSRSCode);
				LOG.debug(" Output SRS Name: {}" , toSRSCode);

				vo.setFromSRSCodeOne(fromSRSCode);
				vo.setFromSRSCode(fromSRSCode);
				vo.setToSRSCodeOne(toSRSCode);
				vo.setToSRSCode(toSRSCode);

				LOG.debug("From SRS Code: {}" , vo.getFromSRSCodeOne());
				LOG.debug("To SRS Code: {}" , vo.getToSRSCodeOne());
				pointsList.add(vo);

			} else {
				//Parse the string and create a list

				//String points = "(1,2,3)(4,5,6)(7,8,9)";
				StringTokenizer tokens1 = new StringTokenizer(points, ")(");

				int a = 0;
				while (tokens1.hasMoreTokens()){

					vo = new WHSServiceVO();
					LOG.debug("*****************COUNT***************" + a++ );
					StringTokenizer tokens2 = new StringTokenizer(tokens1.nextToken(), ",");

					vo.setOriginalCoordinateX(tokens2.nextToken());
					vo.setOriginalCoordinateY(tokens2.nextToken());
					vo.setOriginalCoordinateZ(tokens2.nextToken());

					LOG.debug("X: {}" , vo.getOriginalCoordinateX());
					LOG.debug("Y: {}" , vo.getOriginalCoordinateY());
					LOG.debug("Z: {}" , vo.getOriginalCoordinateZ());
					vo.setTransformationCode(transformationCode);
					String[] transformationNameArray;
					String delimiter = "_To_";
					transformationNameArray = vo.getTransformationCode().split(
							delimiter);
					String fromSRSCode = transformationNameArray[0];
					String toSRSCode = transformationNameArray[1].replace("_v1.0", "");

					LOG.debug(" Input SRS Name: {}" , fromSRSCode);
					LOG.debug(" Output SRS Name: {}" , toSRSCode);

					vo.setFromSRSCodeOne(fromSRSCode);
					vo.setFromSRSCode(fromSRSCode);
					vo.setToSRSCodeOne(toSRSCode);
					vo.setToSRSCode(toSRSCode);
	
					LOG.debug("From SRS Code: {}" , vo.getFromSRSCodeOne());
					LOG.debug("To SRS Code: {}" , vo.getToSRSCodeOne());
					pointsList.add(vo);

				}

			}

			// text return for debugging
			// Set<String> dataInputKeys = dataInputs.getKeys();
			LOG.debug("-2");

			// Start - Call the main method here
			WHSUtil util = new WHSUtil();
			WHSServiceVO vo1 = null;
			List transformedPointsList = new ArrayList();

			Iterator iterator = pointsList.iterator();
			LOG.debug("*****************COUNT SIZE***************" + pointsList.size() );
			int b = 0;
			while ( iterator.hasNext() ) {

				LOG.debug("*****************UNMARSHALLING COUNT***************" + b++ );

				vo1 = (WHSServiceVO)iterator.next();
				String completeCoordinatesString = util.spaceTransformation(vo1);
				if (completeCoordinatesString.equalsIgnoreCase("NOT SUPPORTED")) {
					throw new OWSException(
							"No Such Transformation is available under WHS Hub.",
							ControllerException.NO_APPLICABLE_CODE);
				}
	
				vo1 = util.splitCoordinatesFromStringToVO(vo1,
						completeCoordinatesString);
				// End
	
				// Start - Exception Handling
				if (vo1.getTransformedCoordinateX().equalsIgnoreCase("out")) {
					throw new OWSException("Coordinates - Out of Range.",
							ControllerException.NO_APPLICABLE_CODE);
				}

				// Checking out of bound exception
				CommonUtil commonUtil = new CommonUtil();
				String outOfBoundCheck = commonUtil.outOfBoundException(Double
						.parseDouble(vo1.getTransformedCoordinateX()), Double
						.parseDouble(vo1.getTransformedCoordinateY()), Double
						.parseDouble(vo1.getTransformedCoordinateZ()), vo1
						.getToSRSCodeOne());

				LOG.debug("***Before OutputX***" + vo1.getTransformedCoordinateX().toString());
				LOG.debug("***Before OutputY***" + vo1.getTransformedCoordinateY().toString());
				LOG.debug("***Before OutputZ***" + vo1.getTransformedCoordinateZ().toString());

				if (outOfBoundCheck.equalsIgnoreCase("Coordinates - Out of Range")) {
					throw new OWSException("Coordinates - Out of Range.",
							ControllerException.NO_APPLICABLE_CODE);
				}

				transformedPointsList.add(vo1);
			} 				

			// End

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			java.util.Date date = new java.util.Date();
			String currentTime = dateFormat.format(date);
			vo.setCurrentTime(currentTime);

			// Generating 2 random number to be used as GMLID
			Random randomGenerator1 = new Random();
			for (int idx = 1; idx <= 10; ++idx) {
				randomGMLID1 = randomGenerator1.nextInt(100);
			}
			Random randomGenerator2 = new Random();
			for (int idx = 1; idx <= 10; ++idx) {
				randomGMLID2 = randomGenerator2.nextInt(100);
			}
			LOG.debug("Random GML ID1: - {}" , randomGMLID1);
			LOG.debug("Random GML ID2: - {}" , randomGMLID2);

			// vo.setUrlString(uri.toString());

			XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
			opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
			opt.setSaveNamespacesFirst();
			opt.setSaveAggressiveNamespaces();
			opt.setUseDefaultNamespace();

			ComplexOutput complexOutput = (ComplexOutput) out
					.getParameter("TransformPOIOutput");
			LOG.debug("Setting complex output (requested="
					+ complexOutput.isRequested() + ")");

			TransformationResponseDocument document = TransformationResponseDocument.Factory
					.newInstance();

			TransformationResponseType rootDoc = document
					.addNewTransformationResponse();

			POIType poi = rootDoc.addNewPOI();
			net.opengis.gml.x32.MultiPointType poipnt = poi.addNewMultiPoint();
			poipnt.setSrsName(vo.getToSRSCode());
			poipnt.setId(String.valueOf(randomGenerator1));

			PointType pType = null;
			Iterator iterator1 = transformedPointsList.iterator();
			while (iterator1.hasNext()) {

				WHSServiceVO transformedVO = (WHSServiceVO) iterator1.next();
				LOG.debug("***Inside OutputX***" + transformedVO.getTransformedCoordinateX().toString());
				LOG.debug("***Inside OutputY***" + transformedVO.getTransformedCoordinateY().toString());
				LOG.debug("***Inside OutputZ***" + transformedVO.getTransformedCoordinateZ().toString());

				pType =	poipnt.addNewPointMember().addNewPoint();
				pType.addNewPos().setStringValue(transformedVO.getTransformedCoordinateX().toString() +" "+ transformedVO.getTransformedCoordinateX().toString() +" "+transformedVO.getTransformedCoordinateZ().toString() );
				//pType.setId("p2");
				
			}

			//poipnt.setListValue(list);
			poipnt.setSrsDimension( BigInteger.valueOf(3));
			//poipnt.setCount( BigInteger.valueOf(list.size()));
			
			ArrayList errorList = new ArrayList();
			opt.setErrorListener(errorList);
			boolean isValid = document.validate(opt);

			// If the XML isn't valid, loop through the listener's contents,
			// printing contained messages.

			// get reader on document; reader --> writer
			XMLStreamReader reader = document.newXMLStreamReader();
			XMLStreamWriter writer = complexOutput.getXMLStreamWriter();
			XMLAdapter.writeElement(writer, reader);

			if (!isValid) {
				for (int i = 0; i < errorList.size(); i++) {
					XmlError error = (XmlError) errorList.get(i);

					LOG.debug("\n");
					LOG.debug("Message: {}" , error.getMessage() + "\n");
					LOG.debug("Location of invalid XML: {}"
							,error.getCursorLocation().xmlText() + "\n");
				}
			}

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

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

}
