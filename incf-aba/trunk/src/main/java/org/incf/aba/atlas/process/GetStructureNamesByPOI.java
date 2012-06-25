package org.incf.aba.atlas.process;


import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

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
import org.incf.aba.atlas.util.ABAConfigurator;
import org.incf.aba.atlas.util.ABAServiceDAOImpl;
import org.incf.aba.atlas.util.ABAServiceVO;
import org.incf.aba.atlas.util.ABAUtil;
import org.incf.aba.atlas.util.XMLUtilities;
import org.incf.atlas.waxml.generated.IncfNameType;
import org.incf.atlas.waxml.generated.StructureTermType;
import org.incf.atlas.waxml.generated.StructureTermType.Code;
import org.incf.atlas.waxml.generated.StructureTermsResponseDocument;
import org.incf.atlas.waxml.generated.StructureTermsResponseType;
import org.incf.atlas.waxml.generated.StructureTermsResponseType.StructureTerms;
import org.incf.atlas.waxml.utilities.Utilities;
import org.incf.common.atlas.exception.InvalidDataInputValueException;
import org.incf.common.atlas.util.AllowedValuesValidator;
import org.incf.common.atlas.util.DataInputHandler;
import org.incf.common.atlas.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetStructureNamesByPOI implements Processlet {

	private static final Logger LOG = LoggerFactory
			.getLogger(GetStructureNamesByPOI.class);

	ABAConfigurator config = ABAConfigurator.INSTANCE;

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

			LOG.debug(" Inside GetStructureNamesByPOI... ");

			URL processDefinitionUrl = this.getClass().getResource(
					"/" + this.getClass().getSimpleName() + ".xml");
			DataInputHandler dataInputHandler = new DataInputHandler(new File(
					processDefinitionUrl.toURI()));
			String srsName = dataInputHandler.getValidatedStringValue(in,
					"srsName");
			String x = String.valueOf(DataInputHandler.getDoubleInputValue(in,
					"x"));
			String y = String.valueOf(DataInputHandler.getDoubleInputValue(in,
					"y"));
			String z = String.valueOf(DataInputHandler.getDoubleInputValue(in,
					"z"));
			String filter = dataInputHandler.getValidatedStringValue(in,
					"filter");
			String vocabulary = dataInputHandler.getValidatedStringValue(in,
					"vocabulary");

			ABAServiceVO vo = new ABAServiceVO();

			vo.setFromSRSCodeOne(srsName);
			vo.setFromSRSCode(srsName);
			vo.setFilter(filter);
			vo.setVocabulary(vocabulary);

			LOG.debug("From SRS Code: {}" , vo.getFromSRSCodeOne());
			LOG.debug("Filter: {}" , vo.getFilter());

			vo.setOriginalCoordinateX(x);
			vo.setOriginalCoordinateY(y);
			vo.setOriginalCoordinateZ(z);

			LOG.debug("Coordinate X: {}" , vo.getOriginalCoordinateX());

			// Start - Common code used for coordinate transformation
			String transformedCoordinatesString = "";
			// Convert the coordinates ABAVOXEL into PAXINOS
			LOG.debug("1:");
			if (vo.getFromSRSCode().equalsIgnoreCase(abaVoxel)) {
				vo.setTransformedCoordinateX(vo.getOriginalCoordinateX());
				vo.setTransformedCoordinateY(vo.getOriginalCoordinateY());
				vo.setTransformedCoordinateZ(vo.getOriginalCoordinateZ());
			} else {
				// Call getTransformationChain method here...
				// ABAVoxel
				LOG.debug("1.1:");

				vo.setOriginalCoordinateX(";x=" + vo.getOriginalCoordinateX());
				vo.setOriginalCoordinateY(";y=" + vo.getOriginalCoordinateY());
				vo.setOriginalCoordinateZ(";z=" + vo.getOriginalCoordinateZ());
				vo.setToSRSCode(abaVoxel);
				vo.setToSRSCodeOne(abaVoxel);

				LOG.debug("1.2: {}" , vo.getOriginalCoordinateX());
				LOG.debug("1.3: {}" , vo.getOriginalCoordinateX());
				String delimitor = config.getValue("incf.deploy.port.delimitor");

				// Start - FIXME - Uncomment below two lines and comment the
				// other three lines
				// String hostName = uri.getHost();
				// String portNumber = delimitor + uri.getPort();
				hostName = config.getValue("incf.deploy.host.name");
				portNumber = config.getValue("incf.aba.port.number");
				portNumber = delimitor + portNumber;
				// End - FIXME

				String servicePath = "/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName="
						+ vo.getFromSRSCode()
						+ ";outputSrsName="
						+ vo.getToSRSCode() + ";filter=NONE";
				String transformationChainURL = "http://" + hostName
						+ portNumber + servicePath;
				LOG.debug("1.4: {}" , transformationChainURL);

				XMLUtilities xmlUtilities = new XMLUtilities();
				transformedCoordinatesString = xmlUtilities
						.coordinateTransformation(transformationChainURL, vo
								.getOriginalCoordinateX(), vo
								.getOriginalCoordinateY(), vo
								.getOriginalCoordinateZ());

				LOG.debug("2:");
				// Start - exception handling
				if (transformedCoordinatesString.startsWith("Error:")) {
					System.out
							.println("********************ERROR*********************");
					throw new OWSException("Transformed Coordinates Error: ",
							transformedCoordinatesString);
				}
				// End - exception handling
				ABAUtil util = new ABAUtil();
				String[] tempArray = util
						.getTabDelimNumbers(transformedCoordinatesString);
				vo.setTransformedCoordinateX(tempArray[0]);
				vo.setTransformedCoordinateY(tempArray[1]);
				vo.setTransformedCoordinateZ(tempArray[2]);
			}
			// End

			LOG.debug("3:");
			String structureName = "";
			// Start - Call the main method here
			ABAUtil util = new ABAUtil();
			if (vo.getFilter().equalsIgnoreCase("structureset:fine")) {

				responseString = util.getFineStructureNameByPOI(vo);
				StringTokenizer tokens = new StringTokenizer(responseString);
				while (tokens.hasMoreTokens()) {
					structureName = tokens.nextToken();
					LOG.debug("Structure Name is - {}" , structureName);
				}

				// Start - Exception Handling
				if (structureName == null || structureName.equals("")) {
					System.out
							.println("********************ERROR*********************");
					throw new OWSException("No Structures Found... ",
							ControllerException.NO_APPLICABLE_CODE);
				} else if (structureName.endsWith("found")) {// No structures
					// found
					System.out
							.println("********************ERROR*********************");
					throw new OWSException("No Structures Found... ",
							ControllerException.NO_APPLICABLE_CODE);
				} else if (structureName.endsWith("range")) {
					System.out
							.println("********************ERROR*********************");
					throw new OWSException("Coordinates - Out of Range",
							ControllerException.NO_APPLICABLE_CODE);
				} else if (structureName.endsWith("issue")) {
					System.out
							.println("********************ERROR*********************");
					throw new OWSException(
							"Please contact the administrator to resolve this issue",
							ControllerException.NO_APPLICABLE_CODE);
				}

				// End

			} else if (vo.getFilter().equalsIgnoreCase("structureset:anatomic")) {
				responseString = util.getAnatomicStructureNameByPOI(vo);
				StringTokenizer tokens = new StringTokenizer(responseString);
				while (tokens.hasMoreTokens()) {
					structureName = tokens.nextToken();
					LOG.debug("Structure Name is - {}" , structureName);
				}
				// Start - Exception Handling
				if (structureName == null || structureName.equals("")) {
					throw new OWSException("No Structures Found...",
							ControllerException.NO_APPLICABLE_CODE);
				} else if (structureName.endsWith("found")) { // No structure
					// found
					throw new OWSException("No Structures Found...",
							ControllerException.NO_APPLICABLE_CODE);
				} else if (structureName.endsWith("range")) { // Out of range
					throw new OWSException("Coordinates - Out of Range",
							ControllerException.NO_APPLICABLE_CODE);
				} else if (structureName.endsWith("issue")) {
					throw new OWSException(
							"Please contact the administrator to resolve this issue",
							ControllerException.NO_APPLICABLE_CODE);
				}
				// End
			} else {
				throw new OWSException("Filter type - " + vo.getFilter()
						+ " is not supported",
						ControllerException.NO_APPLICABLE_CODE);
			}
			// End

			LOG.debug("4:");
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			java.util.Date date = new java.util.Date();
			String currentTime = dateFormat.format(date);
			vo.setCurrentTime(currentTime);

			// Generating 2 random number to be used as GMLID
			int randomGMLID1 = 0;
			Random randomGenerator1 = new Random();
			for (int idx = 1; idx <= 10; ++idx) {
				randomGMLID1 = randomGenerator1.nextInt(100);
			}

			// vo.setUrlString(uri.toString());

			XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
			opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
			opt.setSaveNamespacesFirst();
			opt.setSaveAggressiveNamespaces();
			opt.setUseDefaultNamespace();

			ComplexOutput complexOutput = (ComplexOutput) out
					.getParameter("GetStructureNamesByPOIOutput");
			LOG.debug("Setting complex output (requested="
					+ complexOutput.isRequested() + ")");

			StructureTermsResponseDocument document = StructureTermsResponseDocument.Factory
					.newInstance();

			StructureTermsResponseType rootDoc = document
					.addNewStructureTermsResponse();

			// Start - Comment the query element from the response
			/*
			 * QueryInfoType query = rootDoc.addNewQueryInfo();
			 * 
			 * Utilities.addMethodNameToQueryInfo(query,
			 * "GetStructureNamesByPOI  ", "" );
			 * 
			 * Criteria criterias = query.addNewCriteria();
			 * 
			 * //Changes LOG.debug("5:" );
			 * 
			 * InputStringType srsCriteria = (InputStringType)
			 * criterias.addNewInput() .changeType(InputStringType.type);
			 * srsCriteria.setName("srsName");
			 * srsCriteria.setValue(vo.getFromSRSCode());
			 * 
			 * InputStringType xCriteria = (InputStringType)
			 * criterias.addNewInput() .changeType(InputStringType.type);
			 * 
			 * xCriteria.setName("x");
			 * xCriteria.setValue(vo.getOriginalCoordinateX().replace(";x=",
			 * ""));
			 * 
			 * InputStringType yCriteria = (InputStringType)
			 * criterias.addNewInput() .changeType(InputStringType.type);
			 * yCriteria.setName("y");
			 * yCriteria.setValue(vo.getOriginalCoordinateY().replace(";y=",
			 * ""));
			 * 
			 * InputStringType zCriteria = (InputStringType)
			 * criterias.addNewInput() .changeType(InputStringType.type);
			 * zCriteria.setName("z");
			 * zCriteria.setValue(vo.getOriginalCoordinateZ().replace(";z=",
			 * ""));
			 * 
			 * InputStringType srsCodeCriteria = (InputStringType)
			 * criterias.addNewInput().changeType(InputStringType.type);
			 * srsCodeCriteria.setName("vocabulary");
			 * srsCodeCriteria.setValue(vo.getVocabulary()); InputStringType
			 * filterCodeCriteria = (InputStringType)
			 * criterias.addNewInput().changeType(InputStringType.type);
			 * filterCodeCriteria.setName("filter");
			 * filterCodeCriteria.setValue(vo.getFilter());
			 * 
			 * query.getQueryUrl().setName("GetStructureNamesByPOI");
			 * query.setTimeCreated(Calendar.getInstance());
			 */
			// End - commented the query element
			StructureTerms terms = rootDoc.addNewStructureTerms();
			StructureTermType term1 = terms.addNewStructureTerm();
			terms.setHubCode("ABA");
			
			Code t1code = term1.addNewCode();
			t1code.setCodeSpace(vo.getFromSRSCode());
			t1code.setIsDefault(true);
			// t1code.setStructureID("");
			t1code.setStringValue(structureName);

			// term1.setUri("");
			IncfNameType t1name = term1.addNewName();
			// t1name.setStringValue("");
			
			ABAServiceDAOImpl impl = new ABAServiceDAOImpl();
			ArrayList list = impl.getStructureData();
			Iterator iterator = list.iterator();
			ABAServiceVO vo1 = null;
			String matchingStructureName = "";
			while (iterator.hasNext()) {
				vo1 = (ABAServiceVO) iterator.next();
				matchingStructureName = vo1.getStructureName();
				if (structureName.equalsIgnoreCase(matchingStructureName)) {
					LOG.debug("Inside matching structureName");
					term1.addNewDescription().setStringValue(vo1.getStructureDescription());
					break;
				} 
			}


			
			ArrayList errorList = new ArrayList();
			opt.setErrorListener(errorList);
			boolean isValid = document.validate(opt);
			LOG.debug("6:");

			// get reader on document; reader --> writer
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

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

}
