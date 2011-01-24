package org.incf.ucsd.atlas.process;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import net.opengis.gml.x32.PointType;

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
import org.deegree.services.wps.input.LiteralInput;
import org.deegree.services.wps.output.ComplexOutput;
import org.incf.atlas.waxml.generated.POIType;
import org.incf.atlas.waxml.generated.TransformationResponseDocument;
import org.incf.atlas.waxml.generated.TransformationResponseType;
import org.incf.atlas.waxml.utilities.Utilities;
import org.incf.common.atlas.exception.InvalidDataInputValueException;
import org.incf.common.atlas.util.AllowedValuesValidator;
import org.incf.common.atlas.util.Util;
import org.incf.common.atlas.util.CommonUtil;
import org.incf.ucsd.atlas.util.UCSDConfigurator;
import org.incf.ucsd.atlas.util.UCSDServiceVO;
import org.incf.ucsd.atlas.util.UCSDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformPOI implements Processlet {

    private static final Logger LOG = LoggerFactory.getLogger(
            TransformPOI.class);

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
	String abareference2abavoxel = config.getValue("code.abareference2abavoxel.v1");
	String abavoxel2abareference = config.getValue("code.abavoxel2abareference.v1");
	String paxinos2whs09 = config.getValue("code.paxinos2whs09.v1");
	String whs092paxinos = config.getValue("code.whs092paxinos.v1");
	
	
	//private String dataInputString;
	//private DataInputs dataInputs;
	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	String responseString = "";
	int randomGMLID1 = 0;
	int randomGMLID2 = 0;
	
    @Override
    public void process(ProcessletInputs in, ProcessletOutputs out, 
            ProcessletExecutionInfo info) throws ProcessletException {
    	
		UCSDServiceVO vo = new UCSDServiceVO();

        try {

		    // parse dataInputs string
    		System.out.println(" Inside TransformPOI... ");
    		// collect input values
    		String transformationCode = Util.getStringInputValue(in, "transformationCode");
    		String x = String.valueOf(Util.getDoubleInputValue(in, "x"));
    		String y = String.valueOf(Util.getDoubleInputValue(in, "y"));
    		String z = String.valueOf(Util.getDoubleInputValue(in, "z"));

    		URL processDefinitionUrl = this.getClass().getResource(
    				"/" + this.getClass().getSimpleName() + ".xml");
/*    		AllowedValuesValidator validator = new AllowedValuesValidator(
    				new File(processDefinitionUrl.toURI()));

    		if (!validator.validate("transformationCode", transformationCode)) {
    			throw new InvalidDataInputValueException("The transformationCode value '" 
    					+ transformationCode + "' is not among the allowed values "
    					+ "specified in the AllowedValues element of the "
    					+ "ProcessDescription.", "transformationCode");
    		}
*/
    		// new
    		AllowedValuesValidator validator = new AllowedValuesValidator(
    				new File(processDefinitionUrl.toURI()));
    		AllowedValuesValidator.ValidationResult vr = null; 
    		
    		// validate srsName
    		vr = validator.validateNEW("transformationCode", transformationCode);
    		if (!vr.isValid()) {
    			if (vr.getDefaultValue() != null) {
    				
    				// if input not valid and there's default, use it
    				transformationCode = vr.getDefaultValue();
    			} else {
    				
    				// if input not valid and there's no default, exception
        			throw new InvalidDataInputValueException(vr.getMessage(), 
        					"transformationCode");
    			}
    		}
    		
	        vo.setTransformationCode(transformationCode);
			String[] transformationNameArray;
			String delimiter = "_To_";
			transformationNameArray = vo.getTransformationCode().split(delimiter);
			String fromSRSCode = transformationNameArray[0];
			String toSRSCode = transformationNameArray[1].replace("_v1.0", "");

			System.out.println(" Input SRS Name: " + fromSRSCode);
			System.out.println(" Output SRS Name: " + toSRSCode);

	        vo.setFromSRSCodeOne(fromSRSCode);
	        vo.setFromSRSCode(fromSRSCode);
	        vo.setToSRSCodeOne(toSRSCode);
	        vo.setToSRSCode(toSRSCode);

	        //vo.setFilter(filter);

	        System.out.println("From SRS Code: " + vo.getFromSRSCodeOne());
	        System.out.println("To SRS Code: " + vo.getToSRSCodeOne());
	        //System.out.println("Filter: " + vo.getFilter());

	        // validate data inputs
/*	        validateSrsName(vo.getFromSRSCodeOne());
	        validateSrsName(vo.getToSRSCodeOne());
*/	        

	        vo.setOriginalCoordinateX(String.valueOf(x));
	        vo.setOriginalCoordinateY(String.valueOf(y));
	        vo.setOriginalCoordinateZ(String.valueOf(z));

	        System.out.println("X: "+vo.getOriginalCoordinateX());
	        System.out.println("Y: "+vo.getOriginalCoordinateY());
	        System.out.println("Z: "+vo.getOriginalCoordinateZ());

	        // if any validation exceptions, no reason to continue
/*	        if (exceptionHandler != null) {
	            return getExceptionRepresentation();
	        }
*/
		// text return for debugging
		//Set<String> dataInputKeys = dataInputs.getKeys();
		System.out.println("-2");

		//Start - Call the main method here
		UCSDUtil util = new UCSDUtil();
		String completeCoordinatesString = util.spaceTransformation(vo);

		if (completeCoordinatesString.equalsIgnoreCase("NOT SUPPORTED")) {
			throw new OWSException(
					"No Such Transformation is available under UCSD Hub.", ControllerException.NO_APPLICABLE_CODE);
		}

		vo = util.splitCoordinatesFromStringToVO(vo, completeCoordinatesString);
		//End

		//Start - Exception Handling
		if (vo.getTransformedCoordinateX().equalsIgnoreCase("out")) {
			throw new OWSException(
					"Coordinates - Out of Range.", ControllerException.NO_APPLICABLE_CODE);
		} 
		
		//Checking out of bound exception
		CommonUtil commonUtil = new CommonUtil();
		String outOfBoundCheck = commonUtil.outOfBoundException(Double.parseDouble(vo.getTransformedCoordinateX()), Double.parseDouble(vo.getTransformedCoordinateY()), Double.parseDouble(vo.getTransformedCoordinateZ()), vo.getToSRSCodeOne());
		
		if (outOfBoundCheck.equalsIgnoreCase("Coordinates - Out of Range")) {
			throw new OWSException(
					"Coordinates - Out of Range.", ControllerException.NO_APPLICABLE_CODE);
		}
		//End

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        String currentTime = dateFormat.format(date);
        vo.setCurrentTime(currentTime);

        //Generating 2 random number to be used as GMLID
	    Random randomGenerator1 = new Random();
	    for (int idx = 1; idx <= 10; ++idx){
	      randomGMLID1 = randomGenerator1.nextInt(100);
	    }
	    Random randomGenerator2 = new Random();
	    for (int idx = 1; idx <= 10; ++idx){
	      randomGMLID2 = randomGenerator2.nextInt(100);
	    }
	    System.out.println("Random GML ID1: - " + randomGMLID1);
	    System.out.println("Random GML ID2: - " + randomGMLID2);

        //vo.setUrlString(uri.toString());

		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();

		ComplexOutput complexOutput = (ComplexOutput) out.getParameter(
	    		"TransformPOIOutput");
	    		LOG.debug("Setting complex output (requested=" + complexOutput.isRequested() + ")");

		TransformationResponseDocument document = TransformationResponseDocument.Factory.newInstance(); 

		TransformationResponseType rootDoc =	document.addNewTransformationResponse();
		// QueryInfo and criteria should be done as a utility
		// addQueryInfo(GenesResponseType,srscode,filter,X,Y,Z)
/*		QueryInfoType query = rootDoc.addNewQueryInfo();
		QueryInfoType.Criteria criterias = query.addNewCriteria();

		query.addNewQueryUrl();
		query.getQueryUrl().setName("TransformPOI");
		query.getQueryUrl().setStringValue(uri.toString());
      		query.setTimeCreated(Calendar.getInstance());

		InputStringType targetsrsCriteria = (InputStringType) criterias
		.addNewInput().changeType(InputStringType.type);

		targetsrsCriteria.setName("transformationCode");
		targetsrsCriteria.setValue(vo.getTransformationCode());
		
		InputStringType xCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		xCriteria.setName("x");
		xCriteria.setValue(vo.getOriginalCoordinateX());
		
		InputStringType yCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		yCriteria.setName("y");
		yCriteria.setValue(vo.getOriginalCoordinateY());
		
		InputStringType zCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		zCriteria.setName("z");
		zCriteria.setValue(vo.getOriginalCoordinateZ());

		InputStringType filterCodeCriteria = (InputStringType) criterias
		.addNewInput().changeType(InputStringType.type);
		filterCodeCriteria.setName("filter");
		filterCodeCriteria.setValue("cerebellum");
*/
		POIType poi = rootDoc.addNewPOI();
		PointType poipnt = poi.addNewPoint();
		poipnt.setId(String.valueOf(randomGMLID2));
		poipnt.setSrsName(vo.getToSRSCode());
		poipnt.addNewPos();
		poipnt.getPos().setStringValue(vo.getTransformedCoordinateX() + " " + vo.getTransformedCoordinateY() + " " + vo.getTransformedCoordinateZ());

		ArrayList errorList = new ArrayList();
		opt.setErrorListener(errorList);
		boolean isValid = document.validate(opt);
	 
	 // If the XML isn't valid, loop through the listener's contents,
	 // printing contained messages.
		
	// get reader on document; reader --> writer
	XMLStreamReader reader = document.newXMLStreamReader();
	XMLStreamWriter writer = complexOutput.getXMLStreamWriter();
	XMLAdapter.writeElement(writer, reader);

	 if (!isValid)
	 {
	      for (int i = 0; i < errorList.size(); i++)
	      {
	          XmlError error = (XmlError)errorList.get(i);
	          
	          System.out.println("\n");
	          System.out.println("Message: " + error.getMessage() + "\n");
	          System.out.println("Location of invalid XML: " + 
	              error.getCursorLocation().xmlText() + "\n");
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
        	throw new ProcessletException(e);	// is already OWSException
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

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }
	
}
