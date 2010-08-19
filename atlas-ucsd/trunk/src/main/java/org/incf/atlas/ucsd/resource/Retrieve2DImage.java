package org.incf.atlas.ucsd.resource;

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

import net.opengis.gml.x32.PointType;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.incf.atlas.ucsd.util.DataInputs;
import org.incf.atlas.ucsd.util.UCSDConfigurator;
import org.incf.atlas.ucsd.util.UCSDUtil;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.utilities.Utilities;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class Retrieve2DImage extends BaseResouce {

	private final Logger logger = LoggerFactory.getLogger(
			Retrieve2DImage.class);

	//private String dataInputString;
	//private DataInputs dataInputs;
	String hostName = "";
	String portNumber = "";
	String servicePath = "";
	String url = "";
	int randomGMLID1 = 0;
	int randomGMLID2 = 0;
	String sourceURL = "";
	
	public Retrieve2DImage(Context context, Request request, 
			Response response) {
		super(context, request, response);
		
		logger.debug("Instantiated {}.", getClass());

		//FIXME - amemon - read the hostname from the config file 
		UCSDConfigurator config = UCSDConfigurator.INSTANCE;
		hostName = config.getValue("incf.deploy.host.name");
		System.out.println("****HOSTNAME**** - " + hostName);
		portNumber = ":8080";

/*		System.out.println("Before");
		sourceURL = (String)request.getAttributes().get("sourceURL");
		System.out.println("After");
*/		
		servicePath = "/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=Retrieve2DImage"; 

	}


	@Override
	public Representation represent(Variant variant) throws ResourceException {

		UCSDServiceVO vo = new UCSDServiceVO();
		
        try { 

		    // make sure we have something in dataInputs
		    if (dataInputsString == null || dataInputsString.length() == 0) {
		        ExceptionHandler eh = getExceptionHandler();
		        eh.addExceptionToReport(ExceptionCode.MISSING_PARAMETER_VALUE, null, 
		                new String[] { "All DataInputs were missing." });

		        // there is no point in going further, so return
		        return getExceptionRepresentation();
		    }
		    
		    System.out.println("*********<<dataInputsString>>********* - " + dataInputsString );
		    
		    // parse dataInputs string
	        DataInputs dataInputs = new DataInputs(dataInputsString);

	        vo.setFromSRSCodeOne(dataInputs.getValue("srsName"));
	        vo.setFromSRSCode(dataInputs.getValue("srsName"));
	        System.out.println("SRSName - " + vo.getFromSRSCode());
	        String sourceType = dataInputs.getValue("sourceType");
	        System.out.println("SourceType - " + sourceType);
	        vo.setMinX(dataInputs.getValue("xmin"));
	        vo.setMaxX(dataInputs.getValue("xmax"));
	        vo.setMinY(dataInputs.getValue("ymin"));
	        vo.setMaxY(dataInputs.getValue("ymax"));
	        vo.setFilter(dataInputs.getValue("filter"));

	        String sourceURL = dataInputs.getValue("sourceURL");
	        System.out.println("SourceURL - " + sourceURL);

	        String decodedURL = URLDecoder.decode(sourceURL, "UTF-8");
	        System.out.println("Decoded URL - " + decodedURL);

	        //URLDecoder decodedURL = new URLDecoder("sourceURL");

	        // validate data inputs
	        validateSrsName(vo.getFromSRSCodeOne());

	        // if any validation exceptions, no reason to continue
	        if (exceptionHandler != null) {
	            return getExceptionRepresentation();
	        }

		//Start - Call the main method here
	    //String wmsURL = "http://"+imageServerHostname+servicePath+vo.getImageServiceName()+".map&LAYERS="+vo.getImageBaseName()+"&FORMAT=png24&VERSION=1.1.1&REQUEST=GetMap&SRS=EPSG:4326&WIDTH=256&HEIGHT=256&BBOX="+vo.getMinX()+","+vo.getMinY()+","+vo.getMaxX()+","+vo.getMaxY();
		String wmsURL = decodedURL+"&SRS=EPSG:4326&WIDTH=256&HEIGHT=256&BBOX="+vo.getMinX()+","+vo.getMinY()+","+vo.getMaxX()+","+vo.getMaxY();
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

        url = "http://" + hostName + portNumber + servicePath + "&DataInputs=" + dataInputsString;
        vo.setUrlString(url);

		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();
		
		Retrieve2DImageResponseDocument document = Retrieve2DImageResponseDocument.Factory.newInstance();
		
		Retrieve2DImageResponseType imagesRes = document.addNewRetrieve2DImageResponse();
		QueryInfoType query = imagesRes.addNewQueryInfo();
		query.setTimeCreated(Calendar.getInstance());
		Utilities.addMethodNameToQueryInfo(query,"GetMap",url);

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
	 
		return new StringRepresentation(document.xmlText(opt),MediaType.APPLICATION_XML);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

}
