package org.incf.central.atlas.process;

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
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

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
import org.incf.atlas.waxml.generated.Corners.Corner;
import org.incf.atlas.waxml.generated.Image2DType.ImagePosition;
import org.incf.atlas.waxml.generated.Image2DType.ImageSource;
import org.incf.atlas.waxml.generated.ImagesResponseType.Image2Dcollection;
import org.incf.atlas.waxml.generated.QueryInfoType.Criteria;
import org.incf.atlas.waxml.utilities.Utilities;
import org.incf.central.atlas.util.CentralConfigurator;
import org.incf.central.atlas.util.CentralServiceVO;
import org.incf.central.atlas.util.CentralUtil;
import org.incf.central.atlas.util.ReadXML;
import org.incf.central.atlas.util.XMLUtilities;
import org.incf.common.atlas.exception.InvalidDataInputValueException;
import org.incf.common.atlas.util.AllowedValuesValidator;
import org.incf.common.atlas.util.DataInputHandler;
import org.incf.common.atlas.util.Util;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

public class Get2DImagesByPOI implements Processlet {

	private static final Logger LOG = LoggerFactory
			.getLogger(Get2DImagesByPOI.class);

	CentralConfigurator config = CentralConfigurator.INSTANCE;

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
	String filter = "";
	// URI uri = null;
	int randomGMLID1 = 0;
	int randomGMLID2 = 0;

	@Override
	public void process(ProcessletInputs in, ProcessletOutputs out,
			ProcessletExecutionInfo info) throws ProcessletException {

		try {

			CentralServiceVO vo = new CentralServiceVO();
			String paxinosCoordinatesString = "";
			String abareferenceCoordinatesString = "";
			CentralUtil util = new CentralUtil();
			XMLUtilities xmlUtilities = new XMLUtilities();
			ArrayList completeImageList = new ArrayList();

			// validate against allowed values in process definition file
			URL processDefinitionUrl = this.getClass().getResource(
					"/" + this.getClass().getSimpleName() + ".xml");

			// get validated data inputs or default values
			DataInputHandler dataInputHandler = new DataInputHandler(new File(
					processDefinitionUrl.toURI()));

			String srsName = dataInputHandler.getValidatedStringValue(in,
					"srsName");
			filter = dataInputHandler.getValidatedStringValue(in, "filter");
			double x = DataInputHandler.getDoubleInputValue(in, "x");
			double y = DataInputHandler.getDoubleInputValue(in, "y");
			double z = DataInputHandler.getDoubleInputValue(in, "z");
			String tolerance = dataInputHandler.getValidatedStringValue(in,
					"tolerance");

			LOG.debug("srsName Before ");
			// parse dataInputs string
			vo.setFromSRSCodeOne(srsName);
			vo.setFromSRSCode(srsName);
			vo.setOriginalCoordinateX(String.valueOf(x));
			vo.setOriginalCoordinateY(String.valueOf(y));
			vo.setOriginalCoordinateZ(String.valueOf(z));
			//vo.setFilter(filter.replace("maptype:", ""));
			vo.setFilter(filter);
			vo.setTolerance(tolerance);

			LOG.debug("Filter Before {}" , filter);
			filter = vo.getFilter();

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
		    LOG.debug("Random GML ID1: - {}" , randomGMLID1);
		    LOG.debug("Random GML ID2: - {}" , randomGMLID2);
		    LOG.debug("SRS Code: - {}" , vo.getFromSRSCode());

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

			XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();
			opt.setSaveSuggestedPrefixes(Utilities.SuggestedNamespaces());
			opt.setSaveNamespacesFirst();
			opt.setSaveAggressiveNamespaces();
			opt.setUseDefaultNamespace();

			ImagesResponseDocument document = ImagesResponseDocument.Factory
			.newInstance();

			ImagesResponseType imagesRes = document.addNewImagesResponse();
/*			QueryInfoType query = imagesRes.addNewQueryInfo();
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
*/
			String wmsURL = "";
			int count = 0; 

			//Step 1 - Make a call to GetProcessByIdentier - I am bypassing this step for SfN due to rush

	        //Step 2 - 
	        ArrayList complete2DImageList = new ArrayList();
	        ReadXML readXML = new ReadXML();

	        // 2a - Call the method from ABA Hub
		    LOG.debug("Before calling the url - SRS Code: - {}" , vo.getFromSRSCode());

			//images.setHubCode("");
	        String ucsdURL = "http://"+hostName+portNumber+"/ucsd/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName="+vo.getFromSRSCode()+";x="+vo.getOriginalCoordinateX()+";y="+vo.getOriginalCoordinateY()+";z="+vo.getOriginalCoordinateZ()+";filter="+vo.getFilter()+";tolerance="+vo.getTolerance();
		    LOG.debug("UCSD url - {}" , ucsdURL);
	        complete2DImageList = readXML.get2DImageDataList(ucsdURL);

	        Iterator iterator1 = complete2DImageList.iterator();
			Image2Dcollection images = imagesRes.addNewImage2Dcollection();
			images.setHubCode("UCSD");

			CentralServiceVO vo1 = new CentralServiceVO();
			while (iterator1.hasNext()) {
				count++;
		        LOG.debug("Inside While Loop - {}" , count);
				vo1 = (CentralServiceVO)iterator1.next();

				wmsURL = vo1.getWms();
				Image2DType image1 = images.addNewImage2D();
				ImageSource i1source = image1.addNewImageSource();
				i1source.setStringValue(wmsURL);
				i1source.setFormat(IncfRemoteFormatEnum.IMAGE_PNG.toString());
				i1source.setSrsName(srsName);
				i1source.setMetadata(vo1.getRegistrationID());
				i1source.setType("WMS");

				ImagePosition i1position = image1.addNewImagePosition();
				IncfSrsType planeequation = i1position
						.addNewImagePlaneEquation();
				planeequation.setSrsName(srsName);
				planeequation.setStringValue(vo1.getImagePlaneEquation());
				IncfSrsType placement = i1position.addNewImagePlanePlacement();
				placement.setSrsName(srsName);
				placement.setStringValue(vo1.getTfwValues());
				Corners corners = i1position.addNewCorners();

				Corner corner1 = corners.addNewCorner();
				corner1.setPosition(PositionEnum.TOPLEFT);

				LOG.debug("1st corner - filter - {}" , filter);
				corner1.addNewPoint().addNewPos().setStringValue(vo1.getTopLeft());
				corner1.getPoint().setId("image" + count + "topleft");
				corner1.getPoint().getPos().setSrsName(srsName);

				Corner corner2 = corners.addNewCorner();
				corner2.setPosition(PositionEnum.BOTTOMLEFT);

				LOG.debug("2nd corner - filter - {}" , filter);
				corner2.addNewPoint().addNewPos().setStringValue(vo1.getBottomLeft());
				corner2.getPoint().getPos().setSrsName(srsName);
				corner2.getPoint().setId("image" + count + "bottomleft");

				Corner corner3 = corners.addNewCorner();
				corner3.setPosition(PositionEnum.TOPRIGHT);
 
				LOG.debug("3rd corner - filter - {}" , filter);
				corner3.addNewPoint().addNewPos().setStringValue(vo1.getTopRight());
				corner3.getPoint().getPos().setSrsName(srsName);
				corner3.getPoint().setId("image" + count + "topright");

				Corner corner4 = corners.addNewCorner();
				corner4.setPosition(PositionEnum.BOTTOMRIGHT);

				LOG.debug("4th corner - filter - {}" , filter);
				corner4.addNewPoint().addNewPos().setStringValue(vo1.getBottomRight());
				corner4.getPoint().getPos().setSrsName(srsName);
				corner4.getPoint().setId("image" + count + "bottomright");
			}

			complete2DImageList = new ArrayList();
	        String abaURL = "http://"+hostName+portNumber+"/aba/atlas?service=WPS&version=1.0.0&request=Execute&Identifier=Get2DImagesByPOI&DataInputs=srsName="+vo.getFromSRSCode()+";x="+vo.getOriginalCoordinateX()+";y="+vo.getOriginalCoordinateY()+";z="+vo.getOriginalCoordinateZ()+";filter="+vo.getFilter();
		    LOG.debug("ABA URL- {}" , abaURL);
	        
		    complete2DImageList = readXML.get2DImageDataList(abaURL);

	        LOG.debug("List size in central is - {}" , complete2DImageList.size());
	        Iterator iterator = complete2DImageList.iterator();
			images = imagesRes.addNewImage2Dcollection();
			images.setHubCode("ABA");

			while (iterator.hasNext()) {
				count++;
		        LOG.debug("Inside While Loop - {}" , count);
				vo = (CentralServiceVO)iterator.next();

				wmsURL = vo.getWms();
				Image2DType image1 = images.addNewImage2D();
				ImageSource i1source = image1.addNewImageSource();
				i1source.setStringValue(wmsURL);
				i1source.setFormat(IncfRemoteFormatEnum.IMAGE_JPEG.toString());
				i1source.setSrsName(srsName);
				i1source.setType("HTTP");

				ImagePosition i1position = image1.addNewImagePosition();
				IncfSrsType planeequation = i1position
						.addNewImagePlaneEquation();
				planeequation.setSrsName(srsName);
				planeequation.setStringValue(vo.getImagePlaneEquation());
				IncfSrsType placement = i1position.addNewImagePlanePlacement();
				placement.setSrsName(srsName);
				placement.setStringValue(vo.getTfwValues());
				Corners corners = i1position.addNewCorners();

				Corner corner1 = corners.addNewCorner();
				corner1.setPosition(PositionEnum.TOPLEFT);

				LOG.debug("1st corner - filter - {}" , filter);
				corner1.addNewPoint().addNewPos().setStringValue(vo.getTopLeft());
				corner1.getPoint().setId("image" + count + "topleft");
				corner1.getPoint().getPos().setSrsName(srsName);

				Corner corner2 = corners.addNewCorner();
				corner2.setPosition(PositionEnum.BOTTOMLEFT);

				LOG.debug("2nd corner - filter - {}" , filter);
				corner2.addNewPoint().addNewPos().setStringValue(vo.getBottomLeft());
				corner2.getPoint().getPos().setSrsName(srsName);
				corner2.getPoint().setId("image" + count + "bottomleft");

				Corner corner3 = corners.addNewCorner();
				corner3.setPosition(PositionEnum.TOPRIGHT);
 
				LOG.debug("3rd corner - filter - {}" , filter);
				corner3.addNewPoint().addNewPos().setStringValue(vo.getTopRight());
				corner3.getPoint().getPos().setSrsName(srsName);
				corner3.getPoint().setId("image" + count + "topright");

				Corner corner4 = corners.addNewCorner();
				corner4.setPosition(PositionEnum.BOTTOMRIGHT);

				LOG.debug("4th corner - filter - {}" , filter);
				corner4.addNewPoint().addNewPos().setStringValue(vo.getBottomRight());
				corner4.getPoint().getPos().setSrsName(srsName);
				corner4.getPoint().setId("image" + count + "bottomright");
			}
			ComplexOutput complexOutput = (ComplexOutput) out
					.getParameter("Get2DImagesByPOIOutput");

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
