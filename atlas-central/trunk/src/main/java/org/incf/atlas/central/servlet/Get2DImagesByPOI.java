package org.incf.atlas.central.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import net.opengis.gml.x32.PointType;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.incf.atlas.central.resource.CentralServiceVO;
import org.incf.atlas.central.util.CentralConfigurator;
import org.incf.atlas.central.util.CentralUtil;
import org.incf.atlas.central.util.DataInputs;
import org.incf.atlas.central.util.ReadXML;
import org.incf.atlas.common.util.ExceptionCode;
import org.incf.atlas.common.util.ExceptionHandler;
import org.incf.atlas.common.util.XMLUtilities;
import org.incf.atlas.waxml.generated.*;
import org.incf.atlas.waxml.generated.Corners.Corner;
import org.incf.atlas.waxml.generated.Image2DType.ImagePosition;
import org.incf.atlas.waxml.generated.Image2DType.ImageSource;
import org.incf.atlas.waxml.generated.ImagesResponseType.Image2Dcollection;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.utilities.Utilities;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

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
import org.xml.sax.InputSource;

public class Get2DImagesByPOI implements ExecuteProcessHandler {

	private final Logger logger = LoggerFactory.getLogger(
			Get2DImagesByPOI.class);

	CentralConfigurator config = CentralConfigurator.INSTANCE;

	int randomGMLID1 = 0;
	int randomGMLID2 = 0;

	String uri = "";
	String incfDeployHostName = "";
	String incfDeployPortNumber = "";
	
	private ServletContext context;
	
	public Get2DImagesByPOI(ServletContext context) {
		this.context = context;
	}
	
	public String getProcessResponse(ServletContext context, HttpServletRequest request,  
			HttpServletResponse response, DataInputs dataInputs) {

		CentralServiceVO vo = new CentralServiceVO();
		uri = request.getRequestURL().toString(); 
		incfDeployHostName = request.getServerName();
		incfDeployPortNumber = String.valueOf(request.getServerPort());

        try { 

		    // make sure we have something in dataInputs
/*		    if (dataInputsString == null || dataInputsString.length() == 0) {
		        ExceptionHandler eh = getExceptionHandler();
		        eh.addExceptionToReport(ExceptionCode.MISSING_PARAMETER_VALUE, null, 
		                new String[] { "All DataInputs were missing." });

		        // there is no point in going further, so return
		        return getExceptionRepresentation();
		    }
*/
		    // parse dataInputs string
        	String srsName = dataInputs.getValue("srsname");
	        vo.setFromSRSCodeOne(srsName);
	        vo.setFromSRSCode(srsName);
	        vo.setFilter(dataInputs.getValue("filter"));
	        vo.setTolerance(dataInputs.getValue("tolerance"));

	        vo.setOriginalCoordinateX(dataInputs.getValue("x"));
	        vo.setOriginalCoordinateY(dataInputs.getValue("y"));
	        vo.setOriginalCoordinateZ(dataInputs.getValue("z"));

	        System.out.println("SRSName - " + vo.getFromSRSCode());
	        System.out.println("SRSName - " + vo.getFromSRSCodeOne());
	        System.out.println("Filter - " + vo.getFilter());
	        
	        // validate data inputs
	        //validateSrsName(vo.getFromSRSCodeOne());
	        //Double[] poiCoords = validateCoordinate(dataInputs);

	        // if any validation exceptions, no reason to continue
/*	        if (exceptionHandler != null) {
	            return getExceptionRepresentation();
	        }
*/

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
	    System.out.println("SRS Code: - " + vo.getFromSRSCode());

        vo.setUrlString(uri.toString());
        vo.setIncfDeployHostname(incfDeployHostName);
        vo.setIncfDeployPortNumber(incfDeployPortNumber);

		XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
		opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
		opt.setSaveNamespacesFirst();
		opt.setSaveAggressiveNamespaces();
		opt.setUseDefaultNamespace();

		ImagesResponseDocument document = ImagesResponseDocument.Factory
		.newInstance();

		ImagesResponseType imagesRes = document.addNewImagesResponse();
		// QueryInfo and criteria should be done as a utility
		// addQueryInfo(GenesResponseType,srscode,filter,X,Y,Z)
		QueryInfoType query = imagesRes.addNewQueryInfo();
		query.setTimeCreated(Calendar.getInstance());
		Utilities.addMethodNameToQueryInfo(query,"Get2DImagesByPOI", uri.toString());
		
		Criteria criterias = query.addNewCriteria();

		InputStringType xCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		xCriteria.setName("x");
		xCriteria.setValue(vo.getOriginalCoordinateX().replace(";x=", ""));

		InputStringType yCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		yCriteria.setName("y");
		yCriteria.setValue(vo.getOriginalCoordinateY().replace(";y=", ""));

		InputStringType zCriteria = (InputStringType) criterias.addNewInput()
				.changeType(InputStringType.type);
		zCriteria.setName("z");
		zCriteria.setValue(vo.getOriginalCoordinateZ().replace(";z=", ""));
		InputStringType filterCodeCriteria = (InputStringType) criterias
				.addNewInput().changeType(InputStringType.type);
		filterCodeCriteria.setName("filter");
		filterCodeCriteria.setValue(vo.getFilter());
		InputStringType toleranceCodeCriteria = (InputStringType) criterias
				.addNewInput().changeType(InputStringType.type);
		toleranceCodeCriteria.setName("Tolerance");
		toleranceCodeCriteria.setValue(vo.getTolerance());

		String wmsURL = "";
		int count = 0; 

		//Step 1 - Make a call to GetProcessByIdentier - I am bypassing this step for SfN due to rush

        //Step 2 - 
        ArrayList complete2DImageList = new ArrayList();
        ReadXML readXML = new ReadXML();

        // 2a - Call the method from ABA Hub
	    System.out.println("Before calling the url - SRS Code: - " + vo.getFromSRSCode());

        String ucsdURL = "http://"+incfDeployHostName+":"+incfDeployPortNumber+"/atlas-ucsd?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName="+vo.getFromSRSCode()+";x="+vo.getOriginalCoordinateX()+";y="+vo.getOriginalCoordinateY()+";z="+vo.getOriginalCoordinateZ()+";filter="+vo.getFilter()+";tolerance="+vo.getTolerance();
	    System.out.println("UCSD url - " + ucsdURL);
        complete2DImageList = readXML.get2DImageDataList(ucsdURL, complete2DImageList);
        System.out.println("List size in central is - " + complete2DImageList.size());
        Iterator iterator = complete2DImageList.iterator();

		while (iterator.hasNext()) {
			count++;
	        System.out.println("Inside While Loop - " + count);
			vo = (CentralServiceVO)iterator.next();

			wmsURL = vo.getWms();
			Image2Dcollection images = imagesRes.addNewImage2Dcollection();
			Image2DType image1 = images.addNewImage2D();
			ImageSource i1source = image1.addNewImageSource();
			i1source.setStringValue(wmsURL);
			i1source.setFormat(IncfRemoteFormatEnum.IMAGE_PNG.toString());
			//i1source.setRelavance((float) 0.6);
			i1source.setSrsName(srsName);
			//i1source.setThumbnanil("http://example.com/image.jpg");
			//i1source.setMetadata("URL");
			i1source.setType(IncfImageServicesEnum.URL.toString());

			ImagePosition i1position = image1.addNewImagePosition();
			IncfSrsType planeequation = i1position.addNewImagePlaneEquation();
			planeequation.setSrsName(srsName);
			//planeequation.setStringValue(vo.getCoefficientA() + " " + vo.getCoefficientB() + " " + vo.getCoefficientC() + " " + vo.getCoefficientD()); 
			IncfSrsType placement = i1position.addNewImagePlanePlacement();
			placement.setSrsName(srsName);

			//Change
			//placement.setStringValue(vo.getTfw1() + " " + vo.getTfw2() + " " + vo.getTfw3() + " " + vo.getTfw4()+ " " + vo.getTfw5() + " " + vo.getTfw6());
			placement.setStringValue(vo.getTfwValues());
			//Corners corners = i1position.addNewCorners();
			
/*			Corner corner1 = corners.addNewCorner();
			corner1.setPosition(PositionEnum.TOPLEFT);
			corner1.addNewPoint().addNewPos().setStringValue( vo.getMinX() + " " + vo.getMaxY() );
			corner1.getPoint().getPos().setSrsName(vo.getFromSRSCode());
			corner1.getPoint().setId("image"+count+"TopLeft");
	
			Corner corner2 = corners.addNewCorner();
			corner2.setPosition(PositionEnum.BOTTOMLEFT);
			corner2.addNewPoint().addNewPos().setStringValue( vo.getMinX() + " " + vo.getMinY() );
			corner2.getPoint().getPos().setSrsName(vo.getFromSRSCode());
			corner2.getPoint().setId("image"+count+"BOTTOMLEFT");
	
			Corner corner3 = corners.addNewCorner();
			corner3.setPosition(PositionEnum.TOPRIGHT);
			corner3.addNewPoint().addNewPos().setStringValue( vo.getMaxX() + " " + vo.getMaxY() );
			corner3.getPoint().getPos().setSrsName(vo.getFromSRSCode());
			corner3.getPoint().setId("image"+count+"TOPRIGHT");
	
			Corner corner4 = corners.addNewCorner();
			corner4.setPosition(PositionEnum.BOTTOMRIGHT);
			corner4.addNewPoint().addNewPos().setStringValue( vo.getMaxX() + " " + vo.getMinY() );
			corner4.getPoint().getPos().setSrsName(vo.getFromSRSCode());
			corner4.getPoint().setId("image"+count+"BOTTOMRIGHT");
*/		}
		
		ArrayList errorList = new ArrayList();
		opt.setErrorListener(errorList);
		boolean isValid = document.validate(opt);

		// If the XML isn't valid, loop through the listener's contents,
		// printing contained messages.
		if (!isValid) {
			for (int i = 0; i < errorList.size(); i++) {
				XmlError error = (XmlError) errorList.get(i);

				System.out.println("\n");
				System.out.println("Message: " + error.getMessage() + "\n");
				System.out.println("Location of invalid XML: "
						+ error.getCursorLocation().xmlText() + "\n");
			}
		}
	 
		return document.xmlText(opt);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

}
