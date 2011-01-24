package org.incf.ucsd.atlas.process;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
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
import org.deegree.services.wps.output.ComplexOutput;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.utilities.Utilities;
import org.incf.common.atlas.exception.InvalidDataInputValueException;
import org.incf.common.atlas.util.AllowedValuesValidator;
import org.incf.common.atlas.util.Util;
import org.incf.ucsd.atlas.util.UCSDConfigurator;
import org.incf.ucsd.atlas.util.UCSDServiceVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class Retrieve2DImage implements Processlet {

    private static final Logger LOG = LoggerFactory.getLogger(
            GetStructureNamesByPOI.class);

	UCSDConfigurator config = UCSDConfigurator.INSTANCE;

	int randomGMLID1 = 0;
	int randomGMLID2 = 0;

	String abaReference = config.getValue("srsname.abareference.10");
	String abaVoxel = config.getValue("srsname.abavoxel.10");
	String agea = config.getValue("srsname.agea.10");
	String whs09 = config.getValue("srsname.whs.09");
	String whs10 = config.getValue("srsname.whs.10");
	String emap = config.getValue("srsname.emap.10");
	String paxinos = config.getValue("srsname.paxinos.10");
	
	//private String dataInputString;
	//private DataInputs dataInputs;
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

    		String srsName = Util.getStringInputValue(in, "srsName");
    		String sourceType = Util.getStringInputValue(in, "sourceType");
    		String sourceURL = Util.getStringInputValue(in, "sourceURL");
    		String filter = Util.getStringInputValue(in, "filter");
    		String xMin = String.valueOf(Util.getDoubleInputValue(in, "xmin"));
    		String xMax = String.valueOf(Util.getDoubleInputValue(in, "xmax"));
    		String yMin = String.valueOf(Util.getDoubleInputValue(in, "ymin"));
    		String yMax = String.valueOf(Util.getDoubleInputValue(in, "ymax"));

    		URL processDefinitionUrl = this.getClass().getResource(
    				"/" + this.getClass().getSimpleName() + ".xml");
    		
    		AllowedValuesValidator validator = new AllowedValuesValidator(
    				new File(processDefinitionUrl.toURI()));
    		AllowedValuesValidator.ValidationResult vr = null; 
    		
    		// validate srsName
    		vr = validator.validateNEW("srsName", srsName);
    		if (!vr.isValid()) {
    			if (vr.getDefaultValue() != null) {
    				
    				// if input not valid and there's default, use it
    				srsName = vr.getDefaultValue();
    			} else {
    				
    				// if input not valid and there's no default, exception
        			throw new InvalidDataInputValueException(vr.getMessage(), 
        					"srsName");
    			}
    		}
    		
    		// validate 
    		vr = validator.validateNEW("sourceType", sourceType);
    		if (!vr.isValid()) {
    			if (vr.getDefaultValue() != null) {
    				sourceType = vr.getDefaultValue();
    			} else {
        			throw new InvalidDataInputValueException(vr.getMessage(), 
        					"sourceType");
    			}
    		}

    		// validate 
    		vr = validator.validateNEW("sourceURL", sourceURL);
    		if (!vr.isValid()) {
    			if (vr.getDefaultValue() != null) {
    				sourceURL = vr.getDefaultValue();
    			} else {
        			throw new InvalidDataInputValueException(vr.getMessage(), 
        					"sourceURL");
    			}
    		}
    		// validate 
    		vr = validator.validateNEW("xmin", xMin);
    		if (!vr.isValid()) {
    			if (vr.getDefaultValue() != null) {
    				xMin = vr.getDefaultValue();
    			} else {
        			throw new InvalidDataInputValueException(vr.getMessage(), 
        					"xmin");
    			}
    		}
    		// validate 
    		vr = validator.validateNEW("xmax", xMax);
    		if (!vr.isValid()) {
    			if (vr.getDefaultValue() != null) {
    				xMax = vr.getDefaultValue();
    			} else {
        			throw new InvalidDataInputValueException(vr.getMessage(), 
        					"xmax");
    			}
    		}
    		// validate 
    		vr = validator.validateNEW("ymin", yMin);
    		if (!vr.isValid()) {
    			if (vr.getDefaultValue() != null) {
    				yMin = vr.getDefaultValue();
    			} else {
        			throw new InvalidDataInputValueException(vr.getMessage(), 
        					"ymin");
    			}
    		}
    		// validate 
    		vr = validator.validateNEW("ymax", yMax);
    		if (!vr.isValid()) {
    			if (vr.getDefaultValue() != null) {
    				yMax = vr.getDefaultValue();
    			} else {
        			throw new InvalidDataInputValueException(vr.getMessage(), 
        					"ymax");
    			}
    		}
    		// validate 
    		vr = validator.validateNEW("filter", filter);
    		if (!vr.isValid()) {
    			if (vr.getDefaultValue() != null) {
    				filter = vr.getDefaultValue();
    			} else {
        			throw new InvalidDataInputValueException(vr.getMessage(), 
        					"filter");
    			}
    		}

	        vo.setFromSRSCodeOne(srsName);
	        vo.setFromSRSCode(srsName);
	        vo.setMinX(xMin);
	        vo.setMaxX(xMax);
	        vo.setMinY(yMin);
	        vo.setMaxY(yMax);
	        vo.setFilter(filter);

	        String decodedURL = URLDecoder.decode(sourceURL, "UTF-8");
	        System.out.println("Decoded URL - " + decodedURL);

			String wmsURL = decodedURL+"&SRS=EPSG:4326&WIDTH=256&HEIGHT=256&BBOX="+vo.getMinX()+","+vo.getMinY()+","+vo.getMaxX()+","+vo.getMaxY();

	        vo.setFromSRSCodeOne(srsName);
	        vo.setFromSRSCode(srsName);
	        System.out.println("SRSName - " + vo.getFromSRSCode());

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
		
		Retrieve2DImageResponseDocument document = Retrieve2DImageResponseDocument.Factory.newInstance();
		
		Retrieve2DImageResponseType imagesRes = document.addNewRetrieve2DImageResponse();
/*		QueryInfoType query = imagesRes.addNewQueryInfo();
		query.setTimeCreated(Calendar.getInstance());
		Utilities.addMethodNameToQueryInfo(query,"GetMap",uri.toString());

		Criteria criterias = query.addNewCriteria();
		
		InputStringType crtieria1 = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		crtieria1.setName("sourceType");
		crtieria1.setValue(IncfImageServicesEnum.WMS_PNG.toString());

		InputStringType crtieria2 = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		crtieria2.setName("sourceURL");
		crtieria2.setValue(sourceURL);

		InputStringType crtieria3 = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		crtieria3.setName("srsName");
		crtieria3.setValue(vo.getFromSRSCode());

		InputStringType crtieria4 = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		crtieria4.setName("xmin");
		crtieria4.setValue(vo.getMinX());

		InputStringType crtieria6 = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		crtieria6.setName("xmax");
		crtieria6.setValue(vo.getMaxX());

		InputStringType crtieria7 = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		crtieria7.setName("ymin");
		crtieria7.setValue(vo.getMinY());

		InputStringType crtieria8 = (InputStringType) criterias.addNewInput().changeType(InputStringType.type);
		crtieria8.setName("ymax");
		crtieria8.setValue(vo.getMaxY());
*/
		imagesRes.addImageUrl(wmsURL);

		 ArrayList errorList = new ArrayList();
		 opt.setErrorListener(errorList);
		 boolean isValid = document.validate(opt);

		 // If the XML isn't valid, loop through the listener's contents,
		 // printing contained messages.
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
	 
			ComplexOutput complexOutput = (ComplexOutput) out.getParameter(
			"Retrieve2DImageOutput");

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
