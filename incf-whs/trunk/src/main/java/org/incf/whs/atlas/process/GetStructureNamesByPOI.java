package org.incf.whs.atlas.process;


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
import org.incf.atlas.waxml.generated.FeatureReferenceType;
import org.incf.atlas.waxml.generated.IncfNameType;
import org.incf.atlas.waxml.generated.InputStringType;
import org.incf.atlas.waxml.generated.QueryInfoType;
import org.incf.atlas.waxml.generated.StructureTermType;
import org.incf.atlas.waxml.generated.FeatureReferenceType.Url;
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
import org.incf.whs.atlas.util.WHSConfigurator;
import org.incf.whs.atlas.util.WHSServiceDAOImpl;
import org.incf.whs.atlas.util.WHSServiceVO;
import org.incf.whs.atlas.util.WHSUtil;
import org.incf.whs.atlas.util.XMLUtilities;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

public class GetStructureNamesByPOI implements Processlet {

	private static final Logger LOG = LoggerFactory
			.getLogger(GetStructureNamesByPOI.class);

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

			LOG.debug(" Inside GetStructureNamesByPOI... ");
			// collect input values
			WHSServiceVO vo = new WHSServiceVO();

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
			if (vo.getFromSRSCode().equalsIgnoreCase(whs09)) {
				vo.setTransformedCoordinateX(vo.getOriginalCoordinateX());
				vo.setTransformedCoordinateY(vo.getOriginalCoordinateY());
				vo.setTransformedCoordinateZ(vo.getOriginalCoordinateZ());
			} else {
				// Call getTransformationChain method here...
				// ABAVoxel
				vo.setOriginalCoordinateX(";x=" + vo.getOriginalCoordinateX());
				vo.setOriginalCoordinateY(";y=" + vo.getOriginalCoordinateY());
				vo.setOriginalCoordinateZ(";z=" + vo.getOriginalCoordinateZ());
				vo.setToSRSCode(whs09);
				vo.setToSRSCodeOne(whs09);

				// Construct GetTransformationChain URL

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
				String servicePath = "/central/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=GetTransformationChain&DataInputs=inputSrsName="
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
	        	WHSUtil util = new WHSUtil();

	        	//End - exception handling
	        	String[] tempArray = util.getTabDelimNumbers(transformedCoordinatesString);
	        	vo.setTransformedCoordinateX(tempArray[0]);
	        	vo.setTransformedCoordinateY(tempArray[1]);
	        	vo.setTransformedCoordinateZ(tempArray[2]);
		    }
	        //End

			String structureName = "";
			// Start - Call the main method here
			WHSUtil util = new WHSUtil();
			responseString = util.getStructureNameLookup(whs09, vo.getTransformedCoordinateX(), vo.getTransformedCoordinateY(), vo.getTransformedCoordinateZ() );
			StringTokenizer tokens = new StringTokenizer(responseString);

			while ( tokens.hasMoreTokens() ) {
				structureName = tokens.nextToken();
				LOG.debug("Structure Name is - {}", structureName);
			}

			//Start - Exception Handling
			if ( structureName == null || structureName.equals("") ) {
				throw new OWSException("No Structures Found.",
						ControllerException.NO_APPLICABLE_CODE);
			} else if ( structureName.endsWith("found") ) { //No structure found
				throw new OWSException("No Structures Found.",
						ControllerException.NO_APPLICABLE_CODE);
			} else if ( structureName.endsWith("Bckgrnd") ) { //No structure found
				throw new OWSException("No Structures Found.",
						ControllerException.NO_APPLICABLE_CODE);
			} else if ( structureName.endsWith("range") ) { //Out of range
				throw new OWSException("Coordinates - Out of Range.",
						ControllerException.NO_APPLICABLE_CODE);
			} else if ( structureName.endsWith("issue") ) {
				throw new OWSException("Please contact the administrator to resolve this issue",
						ControllerException.NO_APPLICABLE_CODE);
			}
			//End

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
			terms.setHubCode("WHS");

			Code t1code = term1.addNewCode();
			t1code.setCodeSpace(vo.getFromSRSCode());
			t1code.setIsDefault(true);
			// t1code.setStructureID("");
			t1code.setStringValue(structureName);

			// term1.setUri("");
			IncfNameType t1name = term1.addNewName();
			// t1name.setStringValue("");

			FeatureReferenceType t1ft = term1.addNewFeature();
			WHSServiceDAOImpl impl = new WHSServiceDAOImpl();
			ArrayList list = impl.getStructureData();
			Iterator iterator = list.iterator();
			WHSServiceVO vo1 = null;
			String matchingStructureName = "";
			while (iterator.hasNext()) {
				vo1 = (WHSServiceVO) iterator.next();
				matchingStructureName = vo1.getStructureName();
				if (structureName.equalsIgnoreCase(matchingStructureName)) {
					LOG.debug("Inside matching structureName");
					term1.addNewDescription().setStringValue(vo1.getStructureDescription());
					
					Url u1=	t1ft.addNewUrl();
				 	//u1.setType(org.w3.x1999.xlink.TypeType.Enum.forString("URL"));
					u1.setTitle("Preview");
					u1.setStringValue("http://www.3dbar.org:8080/getPreview?cafDatasetName=whs_0.5&structureName="+structureName);

					Url u2=	t1ft.addNewUrl();
					//u2.setType(org.w3.x1999.xlink.TypeType.Enum.forString("Thumbnail"));
					u2.setTitle("Thumbnail");
					u2.setStringValue("http://www.3dbar.org:8080/getThumbnail?cafDatasetName=whs_0.5;structureName="+structureName);

					Url u3=	t1ft.addNewUrl();
					//u3.setType(org.w3.x1999.xlink.TypeType.Enum.forString("Mesh"));
					u3.setTitle("Mesh");
					u3.setStringValue("http://service.3dbar.org/getReconstruction?cafDatasetName=whs_0.5;structureName="+structureName+";qualityPreset=high;outputFormat=vrml");

					Url u4=	t1ft.addNewUrl();
					//u4.setType(org.w3.x1999.xlink.TypeType.Enum.forString("Volumetric mask"));
					u4.setTitle("Volumetric mask");
					u4.setStringValue("http://www.3dbar.org:8080/getReconstruction?cafDatasetName=whs_0.5;structureName="+structureName+";qualityPreset=high;outputFormat=niftii");

					//t1ft.addNewUrl().setStringValue("http://www.3dbar.org:8080/getPreview?cafDatasetName=whs_0.5&structureName="+structureName);
					break;
				} 
			}

			if (!structureName.equalsIgnoreCase(matchingStructureName)) {
				LOG.debug("Inside un-matching structureName");
				t1ft.addNewUrl().setStringValue("");
			}

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

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

}
