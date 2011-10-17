//This class is not implemented yet.
package org.incf.ucsd.atlas.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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

public class UpdateITKData implements Processlet {

	private static final Logger LOG = LoggerFactory
			.getLogger(UpdateITKData.class);

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

			LOG.debug(" Inside UpdateITKData... ");

			URL processDefinitionUrl = this.getClass().getResource(
					"/" + this.getClass().getSimpleName() + ".xml");
			DataInputHandler dataInputHandler = new DataInputHandler(new File(
					processDefinitionUrl.toURI()));

			//FIXME - Start Code

/*			//Start - Update SRS Table
			StringBuffer updateSRSURL = new StringBuffer("http://132.239.131.188:8080/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=updateSRSs&DataInputs=srsCode=0959;srsName=Mouse_Yuko_1.0;srsDescription=Mouse_Yuko_1.0;srsAuthorCode=UCSD;origin=back-left-bottom;unitsAbbreviation=px;unitsName=pixel;neuroPlusX=Right;neuroMinusX=Left;neuroPlusY=Anterior;neuroMinusY=Posterior;neuroPlusZ=Dorsal;neuroMinusZ=Ventral;sourceURI=http://software.incf.org/software/waxholm-space;sourceDescriptionURI=TBD;sourceFileFormat=gzipped nifti;abstract=Waxholm Space is a coordinate-based reference space for the mapping and registration of neuroanatomical data in the mouse brain;derivedFromSRSCode=WHS;derivedMethod=WHS;species=Mouse;srsBase=WHS;regionOfValidity=Whole Brain;regionURI=urn:incf:pons-repository.org:structurenames:Brain;srsVersion=1.0;dimensionMinX=0;dimensionMaxX=511;dimensionMinY=0;dimensionMaxY=1023;dimensionMinZ=0;dimensionMaxZ=511");
			//String updateSRSURL = "http://www.google.com";
			URL url = new URL(updateSRSURL.toString());
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader inBuff = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine;
			while ((inputLine = inBuff.readLine()) != null) {
				LOG.debug("inputLine - {}",inputLine);
			}
*/			//End - Update SRS Table

			//Start - Update Space_Transformations 1 Table
			String updateSpaceTransformationsURL1 = "http://132.239.131.188:8080/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=updateSpaceTransformations&DataInputs=source=Mouse_Yuko_1.0;destination=Mouse_WHS_0.9;hub=UCSD;transformationURL=yuko12whs09"; 

			URL url = new URL(updateSpaceTransformationsURL1);
			URLConnection urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			BufferedReader inBuff = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			String inputLine = "";
			while ((inputLine = inBuff.readLine()) != null) {
				LOG.debug("inputLine - {}",inputLine);
			}
			//End - Update SRS Table

			//Start - Update Space_Transformations 2 Table
			String updateSpaceTransformationsURL2 = "http://132.239.131.188:8080/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=updateSpaceTransformations&DataInputs=source=Mouse_WHS_0.9;destination=Mouse_Yuko_1.0;hub=UCSD;transformationURL=whs092yuko1"; 

			url = new URL(updateSpaceTransformationsURL2);
			urlCon = url.openConnection();
			urlCon.setUseCaches(false);
			inBuff = new BufferedReader(new InputStreamReader(urlCon
					.getInputStream()));
			while ((inputLine = inBuff.readLine()) != null) {
				LOG.debug("inputLine - {}",inputLine);
			}
			//End - Update SRS Table

			//End - End Code

			XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
			opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
			opt.setSaveNamespacesFirst();
			opt.setSaveAggressiveNamespaces();
			opt.setUseDefaultNamespace();

			System.out.println("Before Submit: " );
			RegistrationResponseDocument document = completeResponse("");
			System.out.println("After Submit: " );

			ComplexOutput complexOutput = (ComplexOutput) out
					.getParameter("UpdateITKDataOutput");

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
/*        if (status.startsWith("Error:")) {
        	System.out.println("Error");
        	rr.setStatusString("Error");
        } else {
        	System.out.println("Success");
        	rr.setStatusString("Success");
        }
*/    	System.out.println("Set the status");
    	rr.setStatusString("Success");
        rr.setMessage("Successfully registered the ITK data");
		
		return doc;
	        
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

}
