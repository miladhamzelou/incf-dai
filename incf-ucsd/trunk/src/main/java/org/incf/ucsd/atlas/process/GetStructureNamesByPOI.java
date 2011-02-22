package org.incf.ucsd.atlas.process;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
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
import org.incf.atlas.waxml.generated.IncfNameType;
import org.incf.atlas.waxml.generated.InputStringType;
import org.incf.atlas.waxml.generated.QueryInfoType;
import org.incf.atlas.waxml.generated.StructureTermType;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.generated.StructureTermType.Code;
import org.incf.atlas.waxml.generated.StructureTermsResponseDocument;
import org.incf.atlas.waxml.generated.StructureTermsResponseType;
import org.incf.atlas.waxml.generated.StructureTermsResponseType.StructureTerms;
import org.incf.atlas.waxml.utilities.Utilities;
import org.incf.common.atlas.exception.InvalidDataInputValueException;
import org.incf.common.atlas.util.AllowedValuesValidator;
import org.incf.common.atlas.util.DataInputHandler;
import org.incf.common.atlas.util.Util;
import org.incf.ucsd.atlas.util.UCSDConfigurator;
import org.incf.ucsd.atlas.util.UCSDServiceVO;
import org.incf.ucsd.atlas.util.UCSDUtil;
import org.incf.ucsd.atlas.util.XMLUtilities;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

public class GetStructureNamesByPOI implements Processlet {

	private static final Logger LOG = LoggerFactory
			.getLogger(GetStructureNamesByPOI.class);

	UCSDConfigurator config = UCSDConfigurator.INSTANCE;

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

			System.out.println(" Inside GetStructureNamesByPOI... ");
			// collect input values
			UCSDServiceVO vo = new UCSDServiceVO();

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

			vo.setFromSRSCodeOne(srsName);
			vo.setFromSRSCode(srsName);
			vo.setFilter(filter);
			vo.setVocabulary(vocabulary);
			vo.setOriginalCoordinateX(x);
			vo.setOriginalCoordinateY(y);
			vo.setOriginalCoordinateZ(z);

			// Start - Common code used for coordinate transformation
			String transformedCoordinatesString = "";

			// Convert the coordinates ABAVOXEL into PAXINOS
			if (vo.getFromSRSCode().equalsIgnoreCase(paxinos)) {
				vo.setTransformedCoordinateX(vo.getOriginalCoordinateX());
				vo.setTransformedCoordinateY(vo.getOriginalCoordinateY());
				vo.setTransformedCoordinateZ(vo.getOriginalCoordinateZ());
			} else {
				// Call getTransformationChain method here...
				// ABAVoxel
				vo.setOriginalCoordinateX(";x=" + vo.getOriginalCoordinateX());
				vo.setOriginalCoordinateY(";y=" + vo.getOriginalCoordinateY());
				vo.setOriginalCoordinateZ(";z=" + vo.getOriginalCoordinateZ());
				vo.setToSRSCode(paxinos);
				vo.setToSRSCodeOne(paxinos);

				// Construct GetTransformationChain URL
				// http://132.239.131.188:8080/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName=Mouse_Paxinos_1.0;outputSrsName=Mouse_ABAreference_1.0;filter=Cerebellum

				// Start - FIXME - Uncomment below two lines and comment the
				// other three lines
				// String hostName = uri.getHost();
				// String portNumber = delimitor + uri.getPort();
				String delimitor = config
						.getValue("incf.deploy.port.delimitor");
				hostName = config.getValue("incf.deploy.host.name");
				portNumber = config.getValue("incf.aba.port.number");
				portNumber = delimitor + portNumber;
				// End - FIXME

				//central/atlas
				String servicePath = "/atlas-central?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName="
						+ vo.getFromSRSCode()
						+ ";outputSrsName="
						+ vo.getToSRSCode() + ";filter=NONE";
				String transformationChainURL = "http://" + hostName
						+ portNumber + servicePath;
				XMLUtilities xmlUtilities = new XMLUtilities();
				transformedCoordinatesString = xmlUtilities
						.coordinateTransformation(transformationChainURL, vo
								.getOriginalCoordinateX(), vo
								.getOriginalCoordinateY(), vo
								.getOriginalCoordinateZ());

				// Start - exception handling
				if (transformedCoordinatesString.startsWith("Error:")) {
					throw new OWSException("Transformed Coordinates Error: ",
							transformedCoordinatesString);
				}
				// End - exception handling
				UCSDUtil util = new UCSDUtil();
				String[] tempArray = util
						.getTabDelimNumbers(transformedCoordinatesString);
				vo.setTransformedCoordinateX(tempArray[0]);
				vo.setTransformedCoordinateY(tempArray[1]);
				vo.setTransformedCoordinateZ(tempArray[2]);
			}
			// End

			String structureName = "";
			// Start - Call the main method here

			String xmlQueryString = xmlQueryStringToGetBrainRegionNames(vo
					.getTransformedCoordinateX(), vo
					.getTransformedCoordinateY(), vo
					.getTransformedCoordinateZ());

			String webserviceHostName = config
					.getValue("ucsd.webservice.host.name");
			String webservicePortNumber = config
					.getValue("ucsd.webservice.port.number");

			// Code for webservice client
			String endpoint = "http://" + webserviceHostName
					+ webservicePortNumber
					+ "/axis/services/SpatialAtlasWebservice";
			Service service = new Service();
			Call call = (Call) service.createCall();
			QName operationName = new QName(endpoint, "getBrainRegionNames");
			call.setOperationName(operationName);
			call.setTargetEndpointAddress(endpoint);
			StringBuffer sb = new StringBuffer();
			sb.append(xmlQueryString);

			responseString = (String) call
					.invoke(new Object[] { sb.toString() });

			vo = parseResponseStringForBrainRegionNames(responseString, vo);

			// Start - Exception Handling
			if (responseString == null || responseString.equals("")) {
				throw new OWSException("No Structures Found.",
						ControllerException.NO_APPLICABLE_CODE);
			} else if (structureName.endsWith("found")) {// No structures found
				throw new OWSException("No Structures Found.",
						ControllerException.NO_APPLICABLE_CODE);
			} else if (structureName.endsWith("range")) {
				throw new OWSException("Coordinates - Out of Range.",
						ControllerException.NO_APPLICABLE_CODE);
			} else if (structureName.endsWith("issue")) {
				throw new OWSException(
						"Please contact the administrator to resolve this issue.",
						ControllerException.NO_APPLICABLE_CODE);
			}
			// End

			vo = parseResponseStringForBrainRegionNames(responseString, vo);

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

			StructureTermsResponseDocument document = StructureTermsResponseDocument.Factory
					.newInstance();

			StructureTermsResponseType rootDoc = document
					.addNewStructureTermsResponse();
			/*
			 * QueryInfoType query = rootDoc.addNewQueryInfo();
			 * 
			 * Utilities.addMethodNameToQueryInfo(query,
			 * "GetStructureNamesByPOI  ", uri.toString());
			 * 
			 * Criteria criterias = query.addNewCriteria();
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
			 * query.addNewQueryUrl();
			 * query.getQueryUrl().setName("GetStructureNamesByPOI");
			 * query.getQueryUrl().setStringValue(uri.toString());
			 * query.setTimeCreated(Calendar.getInstance());
			 */StructureTerms terms = rootDoc.addNewStructureTerms();
			StructureTermType term1 = terms.addNewStructureTerm();
			Code t1code = term1.addNewCode();
			t1code.setCodeSpace(vo.getFromSRSCode());
			t1code.setIsDefault(true);
			// t1code.setStructureID("");
			t1code.setStringValue(vo.getStructureName());

			// term1.setUri("");
			IncfNameType t1name = term1.addNewName();
			// t1name.setStringValue("");
			term1.addNewDescription().setStringValue(
					vo.getStructureDescription());

			ArrayList errorList = new ArrayList();
			opt.setErrorListener(errorList);
			boolean isValid = document.validate(opt);

			ComplexOutput complexOutput = (ComplexOutput) out
					.getParameter("GetStructureNamesByPOIOutput");

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

	// Retrieves the unique names for brain regions
	private String xmlQueryStringToGetBrainRegionNames(
			String transformedCoordinateX, String transformedCoordinateY,
			String transformedCoordinateZ) {

		StringBuffer xmlQuery = new StringBuffer();
		xmlQuery.append("<request>");
		xmlQuery.append("<coordinateX>").append(transformedCoordinateX.trim())
				.append("</coordinateX>");
		xmlQuery.append("<coordinateY>").append(transformedCoordinateY.trim())
				.append("</coordinateY>");
		xmlQuery.append("<coordinateZ>").append(transformedCoordinateZ.trim())
				.append("</coordinateZ>");
		xmlQuery.append("</request>");

		return xmlQuery.toString();

	}

	/*
	 * This method will parse the xmlString coming as a parameter and prepare
	 * the data model which then returns back to the calling method.
	 */
	private UCSDServiceVO parseResponseStringForBrainRegionNames(
			String xmlData, UCSDServiceVO vo) {

		// Make the xml document from the xml string
		org.jdom.Document document;

		try {

			SAXBuilder builder = new SAXBuilder(
					"org.apache.xerces.parsers.SAXParser", false);

			document = builder.build(new InputSource(new ByteArrayInputStream(
					xmlData.getBytes())));

			// Get all the childrens
			List structuresList = document.getRootElement().getChildren();
			Iterator iterator = structuresList.iterator();

			// Start - xml parsing
			org.jdom.Element requestElement;

			// We know the structure name is going to be only one, so not
			// running the loop
			for (int i = 0; iterator.hasNext(); i++) {

				requestElement = (org.jdom.Element) iterator.next();
				vo.setStructureName(requestElement.getChildText(
						"brainRegionCode").trim());
				vo.setStructureDescription(requestElement.getChildText(
						"brainRegionDescription").trim());
				// list.add(vo);

			}
			// End - xml parsing

		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return vo;

	}

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

}
