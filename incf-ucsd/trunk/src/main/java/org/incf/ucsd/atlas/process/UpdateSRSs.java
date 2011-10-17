package org.incf.ucsd.atlas.process;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.Random;

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

import org.incf.atlas.waxml.generated.AnnotationResponseDocument;
import org.incf.atlas.waxml.generated.RegistrationResponseDocument;
import org.incf.atlas.waxml.generated.RegistrationResponseType;
import org.incf.atlas.waxml.generated.AnnotationResponseDocument.AnnotationResponse;
import org.incf.atlas.waxml.utilities.Utilities;
import org.incf.common.atlas.exception.InvalidDataInputValueException;
import org.incf.common.atlas.util.DataInputHandler;
import org.incf.ucsd.atlas.util.UCSDConfigurator;
import org.incf.ucsd.atlas.util.UCSDServiceDAOImpl;
import org.incf.ucsd.atlas.util.UCSDServiceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateSRSs implements Processlet {

	private static final Logger LOG = LoggerFactory
			.getLogger(UpdateSRSs.class);

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
	public void process( ProcessletInputs in, ProcessletOutputs out,
						 ProcessletExecutionInfo info ) throws ProcessletException {

		try {

			LOG.debug(" Inside UpdateSRSs... ");

			UCSDServiceVO vo = new UCSDServiceVO();

			URL processDefinitionUrl = this.getClass().getResource(
					"/" + this.getClass().getSimpleName() + ".xml");
			DataInputHandler dataInputHandler = new DataInputHandler(new File(
					processDefinitionUrl.toURI()));

			String filePath = dataInputHandler.getValidatedStringValue(in,
					"filePath");

			//FIXME - Start Code
/*			INSERT INTO srs(
			        srs_code, srs_name, srs_description, srs_author_code, srs_date_submitted, 
			        origin, units_abbreviation, units_name, neuro_plus_x_code, neuro_minus_x_code, 
			        neuro_plus_y_code, neuro_minus_y_code, neuro_plus_z_code, neuro_minus_z_code, 
			        source_uri, source_description_uri, source_file_format, abstract, 
			        derived_from_srs_code, derived_method, species, srs_base, region_of_validity, 
			        region_uri, srs_version, dimension_min_x, dimension_max_x, dimension_min_y, 
			        dimension_max_y, dimension_min_z, dimension_max_z, status)
*/

			Date date = new Date();
			
			vo.setSrcSRSCode(dataInputHandler.getValidatedStringValue(in, "srsCode"));
			vo.setSrsName(dataInputHandler.getValidatedStringValue(in, "srsName"));
			vo.setSrsDescription(dataInputHandler.getValidatedStringValue(in, "srsDescription"));
			vo.setAuthorCode(dataInputHandler.getValidatedStringValue(in, "srsAuthorCode"));
			vo.setOrigin(dataInputHandler.getValidatedStringValue(in, "origin"));
			vo.setUnitsAbbreviation(dataInputHandler.getValidatedStringValue(in, "unitsAbbreviation"));
			vo.setUnitsName(dataInputHandler.getValidatedStringValue(in, "unitsName"));
			vo.setNeuroPlusXCode(dataInputHandler.getValidatedStringValue(in, "neuroPlusX"));
			vo.setNeuroMinusXCode(dataInputHandler.getValidatedStringValue(in, "neuroMinusX"));
			vo.setNeuroPlusYCode(dataInputHandler.getValidatedStringValue(in, "neuroPlusY"));
			vo.setNeuroMinusYCode(dataInputHandler.getValidatedStringValue(in, "neuroMinusY"));
			vo.setNeuroPlusZCode(dataInputHandler.getValidatedStringValue(in, "neuroPlusZ"));
			vo.setNeuroMinusZCode(dataInputHandler.getValidatedStringValue(in, "neuroMinusZ"));
			vo.setSourceURI(dataInputHandler.getValidatedStringValue(in, "sourceURI"));
			vo.setSourceDescriptionURI(dataInputHandler.getValidatedStringValue(in, "sourceDescriptionURI"));
			vo.setSourceFileFormat(dataInputHandler.getValidatedStringValue(in, "sourceFileFormat"));
			vo.setSrsAbstract(dataInputHandler.getValidatedStringValue(in, "abstract"));
			vo.setDerivedFromSRSCode(dataInputHandler.getValidatedStringValue(in, "derivedFromSRSCode"));
			vo.setDerivedMethod(dataInputHandler.getValidatedStringValue(in, "derivedMethod"));
			vo.setSpecies(dataInputHandler.getValidatedStringValue(in, "species"));
			vo.setSrsBase(dataInputHandler.getValidatedStringValue(in, "srsBase"));
			vo.setRegionOfValidity(dataInputHandler.getValidatedStringValue(in, "regionOfValidity"));
			vo.setRegionURI(dataInputHandler.getValidatedStringValue(in, "regionURI"));
			vo.setSrsVersion(dataInputHandler.getValidatedStringValue(in, "srsVersion"));
			vo.setDimensionMinX(dataInputHandler.getValidatedStringValue(in, "dimensionMinX"));
			vo.setDimensionMaxX(dataInputHandler.getValidatedStringValue(in, "dimensionMaxX"));
			vo.setDimensionMinY(dataInputHandler.getValidatedStringValue(in, "dimensionMinY"));
			vo.setDimensionMaxY(dataInputHandler.getValidatedStringValue(in, "dimensionMaxY"));
			vo.setDimensionMinZ(dataInputHandler.getValidatedStringValue(in, "dimensionMinZ"));
			vo.setDimensionMaxZ(dataInputHandler.getValidatedStringValue(in, "dimensionMaxZ"));

			vo.setDateSubmitted(String.valueOf(date));
			vo.setStatus("ACTIVE");

			UCSDServiceDAOImpl impl = new UCSDServiceDAOImpl();
			String status = impl.updateSRSs(vo);

			System.out.println("Status is - " + status);
			
			//End - End Code

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
			RegistrationResponseDocument document = completeResponse(status);
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
					.getParameter("UpdateSRSsOutput");

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


    public RegistrationResponseDocument completeResponse(String status) {
        RegistrationResponseDocument doc =      RegistrationResponseDocument.Factory.newInstance();
        
        RegistrationResponseType rr=    doc.addNewRegistrationResponse();
        System.out.println("Status in Final output is - " + status);
        if (status.startsWith("Error:")) {
        	System.out.println("Error");
        	rr.setStatusString("Error");
        } else {
        	System.out.println("Success");
        	rr.setStatusString("Success");
        }
    	System.out.println("Set the status");
        rr.setMessage(status);
		
		return doc;
	        
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

}
