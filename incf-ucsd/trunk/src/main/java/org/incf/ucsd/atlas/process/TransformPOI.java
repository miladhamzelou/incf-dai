package org.incf.ucsd.atlas.process;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import net.opengis.gml.x32.LengthType;
import net.opengis.gml.x32.MetaDataPropertyType;
import net.opengis.gml.x32.MultiPointType;
import net.opengis.gml.x32.PointType;

import org.incf.ucsd.atlas.util.UCSDUtil;
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
import org.incf.ucsd.atlas.util.UCSDConfigurator;
import org.incf.ucsd.atlas.util.UCSDServiceVO;
import org.incf.atlas.waxml.generated.DisplacementDocument;
import org.incf.atlas.waxml.generated.DisplacementMetaDataType;
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

	UCSDConfigurator config = UCSDConfigurator.INSTANCE;

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

		String displacement = "";
		
		try {

			UCSDServiceVO vo = null;

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
			
			//--------------------------------------------------------------------------------------------------------
			//Starts - Insert here..

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
			//Ends... Insert here...
			
			List pointsList = new ArrayList(); 
			
			if ( points == null || points.equals("") ) {

				//Starts - Read the points, validate and make it available in a format that can be used for passing in the main method
				vo = new UCSDServiceVO();
				
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
				
				//End - Read, parse and validate the parameters and make it available for the main method
				
				//--------------------------------------------------------------------------------------------------------

				//Start - main method here
				UCSDUtil util = new UCSDUtil();
				//UCSDServiceVO vo1 = null;
				//List transformedPointsList = new ArrayList();

				//Iterator iterator = pointsList.iterator();
				LOG.debug("*****************COUNT SIZE***************" + pointsList.size() );
				int b = 0;
				//while ( iterator.hasNext() ) {

					LOG.debug("*****************UNMARSHALLING COUNT***************" + b++ );

					//vo1 = (UCSDServiceVO)iterator.next();
					String completeCoordinatesString = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());
					if (completeCoordinatesString.equalsIgnoreCase("NOT SUPPORTED")) {
						throw new OWSException(
								"No Such Transformation is available under UCSD Hub.",
								ControllerException.NO_APPLICABLE_CODE);
					}

					UCSDServiceVO vo1 = new UCSDServiceVO();
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

					StringTokenizer tokens = null;
					String var = "";
					String minRange = "";
					String maxRange = "";

					System.out.println("Before" + outOfBoundCheck);
					if (outOfBoundCheck.startsWith("Coordinates - Out of Range:x:")) {

						tokens = new StringTokenizer(outOfBoundCheck, ":");
						tokens.nextToken();
						var = tokens.nextToken();
						minRange = tokens.nextToken();
						maxRange = tokens.nextToken();
						//System.out.println("Tokens: "+var+minRange+maxRange);
						String message = "The transformed coordinates for the transformation "+transformationCode+" has allowed range from \""+minRange+"\" to \""+maxRange+"\" for \""+var+"\" "+ "("+var+"=\""+vo1.getTransformedCoordinateX()+"\")";
						System.out.println("Message is - " + message);
						throw new OWSException(message,
								ControllerException.NO_APPLICABLE_CODE); 
						
					} else if (outOfBoundCheck.startsWith("Coordinates - Out of Range:y:")) {

						tokens = new StringTokenizer(outOfBoundCheck, ":");
						tokens.nextToken();
						var = tokens.nextToken();
						minRange = tokens.nextToken();
						maxRange = tokens.nextToken();
						//System.out.println("Tokens: "+var+minRange+maxRange);
						String message = "The transformed coordinates for the transformation "+transformationCode+" has allowed range from \""+minRange+"\" to \""+maxRange+"\" for \""+var+"\" "+ "("+var+"=\""+vo1.getTransformedCoordinateY()+"\")";
						System.out.println("Message is - " + message);
						throw new OWSException(message,
								ControllerException.NO_APPLICABLE_CODE); 
						
					} else if (outOfBoundCheck.startsWith("Coordinates - Out of Range:z:")) {

						tokens = new StringTokenizer(outOfBoundCheck, ":");
						tokens.nextToken();
						var = tokens.nextToken();
						minRange = tokens.nextToken();
						maxRange = tokens.nextToken();
						System.out.println("Tokens: "+var+minRange+maxRange);
						String message = "The transformed coordinates for the transformation "+transformationCode+" has allowed range from \""+minRange+"\" to \""+maxRange+"\" for \""+var+"\" "+ "("+var+"=\""+vo1.getTransformedCoordinateZ()+"\")";
						System.out.println("Message is - " + message);
						throw new OWSException(message,
								ControllerException.NO_APPLICABLE_CODE); 

					}

					displacement = util.calculateAccuracy(vo);
					//transformedPointsList.add(vo1);
					//}
					//End - main method here

					//--------------------------------------------------------------------------------------------------------

					//Starts - Start outputting the data to xml
					POIType poi = rootDoc.addNewPOI();
					//poi.setDisplacement(1.0);
					PointType poipnt = poi.addNewPoint();
					poipnt.setId(String.valueOf(randomGMLID1));
					//poipnt.newCursor().insertComment("id on Point Required By GML\n Scoped to the document only");
					poipnt.setSrsName(vo.getFromSRSCode());

					//Starts - Displacement info
					QName newName = new QName("Displacement");
					MetaDataPropertyType md10 =   poipnt.addNewMetaDataProperty();

					DisplacementDocument dis = DisplacementDocument.Factory.newInstance();
					DisplacementMetaDataType dmd = dis.addNewDisplacement();
					
					LengthType dist1 = dmd.addNewDistance();
					//FIXME: put the unit here, read this from one of the database table
					//dist1.setUom("mm");
					dist1.setDoubleValue(Double.parseDouble(displacement));
					md10.set(dis);

					//Ends - Displacement info

					//One time
					poipnt.addNewPos();
					
					poipnt.getPos().setStringValue(vo.getTransformedCoordinateX() +" "+vo.getTransformedCoordinateY() +" "+vo.getTransformedCoordinateZ());
				
				//pointsList.add(vo);

			} else {

				vo = new UCSDServiceVO();

				//Parse the string and create a list
				POIType poi = rootDoc.addNewPOI();
				MultiPointType poipnt = poi.addNewMultiPoint();
				//poipnt.setId("AnyIndentifier");
				//poipnt.newCursor().insertComment("id on Point Required By GML\n Scoped to the document only");
				poipnt.setSrsName(vo.getFromSRSCode());
				poipnt.setId(String.valueOf(randomGMLID1));

				//String points = "(1,2,3)(4,5,6)(7,8,9)";
				StringTokenizer tokens1 = new StringTokenizer(points, ")(");

				UCSDServiceVO vo1 = null;

				int a = 0;
				while (tokens1.hasMoreTokens()){

					//Starts - Parse the list of point one by one
					vo = new UCSDServiceVO();
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
	
					poipnt.setSrsName(vo.getFromSRSCode());

					LOG.debug("From SRS Code: {}" , vo.getFromSRSCodeOne());
					LOG.debug("To SRS Code: {}" , vo.getToSRSCodeOne());
					//Ends - Parse list of points one by one

					//--------------------------------------------------------------------------------------------------------

					//Starts - Main Method to retrieve the data
					UCSDUtil util = new UCSDUtil();
					vo1 = new UCSDServiceVO();
					//List transformedPointsList = new ArrayList();

					//Iterator iterator = pointsList.iterator();
					LOG.debug("*****************COUNT SIZE***************" + pointsList.size() );
					int b = 0;
					//while ( iterator.hasNext() ) {

						LOG.debug("*****************UNMARSHALLING COUNT***************" + b++ );

						//vo1 = (UCSDServiceVO)iterator.next();
						String completeCoordinatesString = util.directSpaceTransformation(vo.getFromSRSCodeOne(), vo.getToSRSCodeOne(), vo.getOriginalCoordinateX(), vo.getOriginalCoordinateY(), vo.getOriginalCoordinateZ());
						if (completeCoordinatesString.equalsIgnoreCase("NOT SUPPORTED")) {
							throw new OWSException(
									"No Such Transformation is available under UCSD Hub.",
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

						StringTokenizer tokens = null;
						String var = "";
						String minRange = "";
						String maxRange = "";

						System.out.println("Before" + outOfBoundCheck);
						if (outOfBoundCheck.startsWith("Coordinates - Out of Range:x:")) {

							tokens = new StringTokenizer(outOfBoundCheck, ":");
							tokens.nextToken();
							var = tokens.nextToken();
							minRange = tokens.nextToken();
							maxRange = tokens.nextToken();
							//System.out.println("Tokens: "+var+minRange+maxRange);
							String message = "The transformed coordinates for the transformation "+transformationCode+" has allowed range from \""+minRange+"\" to \""+maxRange+"\" for \""+var+"\" "+ "("+var+"=\""+vo.getTransformedCoordinateX()+"\")";
							System.out.println("Message is - " + message);
							throw new OWSException(message,
									ControllerException.NO_APPLICABLE_CODE); 
							
						} else if (outOfBoundCheck.startsWith("Coordinates - Out of Range:y:")) {

							tokens = new StringTokenizer(outOfBoundCheck, ":");
							tokens.nextToken();
							var = tokens.nextToken();
							minRange = tokens.nextToken();
							maxRange = tokens.nextToken();
							//System.out.println("Tokens: "+var+minRange+maxRange);
							String message = "The transformed coordinates for the transformation "+transformationCode+" has allowed range from \""+minRange+"\" to \""+maxRange+"\" for \""+var+"\" "+ "("+var+"=\""+vo.getTransformedCoordinateY()+"\")";
							System.out.println("Message is - " + message);
							throw new OWSException(message,
									ControllerException.NO_APPLICABLE_CODE); 
							
						} else if (outOfBoundCheck.startsWith("Coordinates - Out of Range:z:")) {

							tokens = new StringTokenizer(outOfBoundCheck, ":");
							tokens.nextToken();
							var = tokens.nextToken();
							minRange = tokens.nextToken();
							maxRange = tokens.nextToken();
							System.out.println("Tokens: "+var+minRange+maxRange);
							String message = "The transformed coordinates for the transformation "+transformationCode+" has allowed range from \""+minRange+"\" to \""+maxRange+"\" for \""+var+"\" "+ "("+var+"=\""+vo.getTransformedCoordinateZ()+"\")";
							System.out.println("Message is - " + message);
							throw new OWSException(message,
									ControllerException.NO_APPLICABLE_CODE); 

						}

						displacement = util.calculateAccuracy(vo);
						//transformedPointsList.add(vo1);
						//}

						//--------------------------------------------------------------------------------------------------------
					//Ends - Main method to retrieve the data

					//Starts - Display the data in xml 
					PointType pointCount =	poipnt.addNewPointMember().addNewPoint();
					DisplacementDocument dis = DisplacementDocument.Factory.newInstance();
					DisplacementMetaDataType dmd = dis.addNewDisplacement();
					LengthType dist1 = dmd.addNewDistance();
					//FIXME - Read the unit from the database table and uncomment the below line
					//dist1.setUom("mm");
					dist1.setDoubleValue(Double.parseDouble(displacement));
					pointCount.addNewMetaDataProperty().set(dis);

					pointCount.addNewPos().setStringValue(vo.getTransformedCoordinateX() +" "+vo.getTransformedCoordinateY() +" "+vo.getTransformedCoordinateZ());
					pointCount.setId("p"+a+1);
					//Ends - Display the data in xml
					
					//pointsList.add(vo);
					a++;
				}

			}

			// text return for debugging
			// Set<String> dataInputKeys = dataInputs.getKeys();
			LOG.debug("-2");

			// Start - Call the main method here
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
